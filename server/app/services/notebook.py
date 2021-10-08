from typing import List, Optional
from uuid import UUID

from sqlalchemy.orm import Session

from ..models import Notebook
from ..schemas import NotebookCreate, NotebookUpdate
from .base import Service


class NotebookService(Service[Notebook, NotebookCreate, NotebookUpdate]):
    def create_with_user(
        self, db: Session, *, object_in: NotebookCreate, user_id: UUID
    ) -> Notebook:
        note = Notebook(**object_in.dict(), user_id=user_id)  # type: ignore
        db.add(note)
        db.commit()
        db.refresh(note)
        return note

    def find_by_user(
        self, db: Session, *, user_id: UUID, limit: int = 100, offset: int = 0
    ) -> List[Notebook]:
        return (
            db.query(self.model)
            .filter(Notebook.user_id == user_id)
            .limit(limit)
            .offset(offset)
            .all()
        )

    def find_by_user_and_name(
        self, db: Session, *, user_id: UUID, name: str
    ) -> Optional[Notebook]:
        return (
            db.query(self.model)
            .filter(Notebook.user_id == user_id)
            .filter(Notebook.name == name)
            .first()
        )


notebook = NotebookService(Notebook)
