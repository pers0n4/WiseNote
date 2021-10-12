from typing import Any, List
from uuid import UUID

from fastapi import APIRouter, Depends, HTTPException, status
from sqlalchemy.orm import Session

from database.session import get_db

from .. import models, schemas, services

router = APIRouter()


@router.get("", response_model=List[schemas.Note], status_code=status.HTTP_200_OK)
def read_notes_by_user(
    *,
    db: Session = Depends(get_db),
    current_user: models.User = Depends(services.get_current_user),
) -> Any:
    notes = services.note.find_by_user(db, user_id=current_user.id)  # type: ignore
    return notes


@router.post("", response_model=schemas.Note, status_code=status.HTTP_201_CREATED)
def create_note(
    *,
    db: Session = Depends(get_db),
    note_in: schemas.NoteCreate,
    current_user: models.User = Depends(services.get_current_user),
) -> Any:
    note = services.note.create_with_user(
        db, object_in=note_in, user_id=current_user.id  # type: ignore
    )
    return note


@router.get("/{note_id}", response_model=schemas.Note, status_code=status.HTTP_200_OK)
def read_note(
    *,
    db: Session = Depends(get_db),
    note_id: UUID,
    current_user: models.User = Depends(services.get_current_user),
) -> Any:
    note = services.note.find_one(db, id=note_id)
    if not note:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail="Note not found",
        )
    return note


@router.patch("/{note_id}", response_model=schemas.Note, status_code=status.HTTP_200_OK)
def update_note(
    *,
    db: Session = Depends(get_db),
    note_id: UUID,
    note_in: schemas.NoteUpdate,
    current_user: models.User = Depends(services.get_current_user),
) -> Any:
    note = services.note.find_one(db, id=note_id)
    if not note:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail="Note not found",
        )
    note = services.note.update(db, object_model=note, object_in=note_in)
    return note


@router.delete("/{note_id}", status_code=status.HTTP_204_NO_CONTENT)
def delete_note(
    *,
    db: Session = Depends(get_db),
    note_id: UUID,
    current_user: models.User = Depends(services.get_current_user),
) -> Any:
    note = services.note.find_one(db, id=note_id)
    if not note:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail="Note not found",
        )
    services.note.remove(db, object_model=note)
