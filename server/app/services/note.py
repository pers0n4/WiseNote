from typing import List
from uuid import UUID

from sqlalchemy.orm import Session

from ..models import Note
from ..schemas import NoteCreate, NoteUpdate
from .base import Service


class NoteService(Service[Note, NoteCreate, NoteUpdate]):
    def create_with_user(
        self, db: Session, *, object_in: NoteCreate, user_id: UUID
    ) -> Note:
        note = Note(**object_in.dict(), user_id=user_id)  # type: ignore
        db.add(note)
        db.commit()
        db.refresh(note)
        return note

    def find_by_user(
        self, db: Session, *, user_id: UUID, limit: int = 100, offset: int = 0
    ) -> List[Note]:
        return (
            db.query(self.model)
            .filter(Note.user_id == user_id)
            .limit(limit)
            .offset(offset)
            .all()
        )


note = NoteService(Note)
