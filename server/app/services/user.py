from typing import Any, Dict, Optional, Union

from sqlalchemy.orm import Session

from ..models import User
from ..schemas import UserCreate, UserUpdate
from ..services import get_password_hash, verify_password
from .base import Service


class UserService(Service[User, UserCreate, UserUpdate]):
    def create(self, db: Session, *, object_in: UserCreate) -> User:
        user_data = object_in.dict(exclude_unset=True)
        user_data.update({"password": get_password_hash(user_data["password"])})
        user = User(**user_data)  # type: ignore
        db.add(user)
        db.commit()
        db.refresh(user)
        return user

    def update(
        self,
        db: Session,
        *,
        object_model: User,
        object_in: Union[UserUpdate, Dict[str, Any]],
    ) -> User:
        if isinstance(object_in, dict):
            update_data = object_in
        else:
            update_data = object_in.dict(exclude_unset=True)
        if update_data["password"]:
            hashed_password = get_password_hash(update_data["password"])
            update_data["password"] = hashed_password
        return super().update(db, object_model=object_model, object_in=update_data)

    def authenticate(self, db: Session, *, email: str, password: str) -> Optional[User]:
        user = db.query(User).filter(User.email == email).first()
        if user is None:
            return None
        if not verify_password(password, user.password):
            return None
        return user


user = UserService(User)
