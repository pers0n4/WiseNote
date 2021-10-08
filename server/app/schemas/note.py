from datetime import datetime
from typing import Optional
from uuid import UUID

from pydantic import BaseModel


class NoteBase(BaseModel):
    title: Optional[str] = None
    content: Optional[str] = None
    memo: Optional[str] = None
    is_favorite: Optional[bool] = None


class NoteCreate(NoteBase):
    title: str
    content: str


class NoteUpdate(NoteBase):
    pass


class NoteInDBBase(BaseModel):
    id: UUID
    title: str
    content: str
    user_id: UUID
    summary: Optional[str] = None
    memo: Optional[str] = None
    is_favorite: bool
    created_at: datetime
    updated_at: datetime

    class Config:
        orm_mode = True


class NoteInDB(NoteInDBBase):
    pass


class Note(NoteInDBBase):
    pass
