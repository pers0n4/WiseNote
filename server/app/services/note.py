from typing import Any, List
from uuid import UUID

from sqlalchemy.orm import Session

from .. import services
from ..models import Note
from ..schemas import NotebookCreate, NoteCreate, NoteInDBBase, NoteUpdate
from .base import Service


class NoteService(Service[Note, NoteCreate, NoteUpdate]):
    def create_with_user(
        self, db: Session, *, object_in: NoteCreate, user_id: UUID
    ) -> Any:
        note_data = object_in.dict(exclude_unset=True)

        notebook = services.notebook.find_by_user_and_name(
            db, user_id=user_id, name=note_data["notebook"]
        )
        if notebook is None:
            print("Notebook not found")
            notebook = services.notebook.create_with_user(
                db,
                object_in=NotebookCreate(name=note_data["notebook"]),
                user_id=user_id,
            )
        print(notebook.name, notebook.id)
        print(note_data)

        note = Note(
            **NoteInDBBase(
                **note_data,
                user_id=user_id,
                notebook_id=notebook.id,
            ).dict(exclude_unset=True)
        )  # type: ignore
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
