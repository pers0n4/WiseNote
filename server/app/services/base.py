from typing import Any, Dict, Generic, List, Optional, Type, TypeVar, Union

from fastapi.encoders import jsonable_encoder
from pydantic import BaseModel
from sqlalchemy.orm import Session

from database.base import Base

ModelType = TypeVar("ModelType", bound=Base)
CreateSchemaType = TypeVar("CreateSchemaType", bound=BaseModel)
UpdateSchemaType = TypeVar("UpdateSchemaType", bound=BaseModel)


class Service(Generic[ModelType, CreateSchemaType, UpdateSchemaType]):
    def __init__(self, model: Type[ModelType]):
        self.model = model

    def create(self, db: Session, *, object_in: CreateSchemaType) -> ModelType:
        object_in_data = jsonable_encoder(object_in)
        object_model = self.model(**object_in_data)  # type: ignore
        db.add(object_model)
        db.commit()
        db.refresh(object_model)
        return object_model

    def find_all(
        self, db: Session, *, limit: int = 100, offset: int = 0
    ) -> List[ModelType]:
        return db.query(self.model).limit(limit).offset(offset).all()

    def find_one(self, db: Session, id: Any) -> Optional[ModelType]:
        return db.query(self.model).filter(self.model.id == id).first()

    def update(
        self,
        db: Session,
        *,
        object_model: ModelType,
        object_in: Union[UpdateSchemaType, Dict[str, Any]],
    ) -> ModelType:
        object_in_data = jsonable_encoder(object_model)
        if isinstance(object_in, dict):
            update_data = object_in
        else:
            update_data = object_in.dict(exclude_unset=True)
        for field in object_in_data:
            if field in update_data:
                setattr(object_model, field, update_data[field])
        db.add(object_model)
        db.commit()
        db.refresh(object_model)
        return object_model

    def remove(self, db: Session, *, object_model: ModelType) -> None:
        db.delete(object_model)
        db.commit()
