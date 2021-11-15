from datetime import datetime
from typing import Optional
from uuid import UUID

from pydantic import BaseModel


class NoteBase(BaseModel):
    title: Optional[str] = None
    content: Optional[str] = None
    memo: Optional[str] = None
    is_favorite: Optional[bool] = None
    notebook: Optional[str] = None
    latitude: Optional[float] = None
    longitude: Optional[float] = None


class NoteCreate(NoteBase):
    title: str
    content: str
    notebook: str


class NoteUpdate(NoteBase):
    pass


class NoteInDBBase(BaseModel):
    id: Optional[UUID] = None
    title: Optional[str] = None
    content: Optional[str] = None
    user_id: Optional[UUID] = None
    notebook_id: Optional[UUID] = None
    summary: Optional[str] = None
    memo: Optional[str] = None
    is_favorite: Optional[bool] = None
    latitude: Optional[float] = None
    longitude: Optional[float] = None
    created_at: Optional[datetime] = None
    updated_at: Optional[datetime] = None

    class Config:
        orm_mode = True


class NoteInDB(NoteInDBBase):
    id: UUID
    title: str
    content: str
    user_id: UUID
    notebook_id: UUID
    is_favorite: bool
    created_at: datetime
    updated_at: datetime


class Note(NoteInDBBase):
    pass
