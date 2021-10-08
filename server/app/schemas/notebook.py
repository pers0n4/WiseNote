from typing import List, Optional
from uuid import UUID

from pydantic import BaseModel

from ..schemas import Note


class NotebookBase(BaseModel):
    name: Optional[str] = None


class NotebookCreate(NotebookBase):
    name: str


class NotebookUpdate(NotebookBase):
    pass


class NotebookInDBBase(BaseModel):
    id: UUID
    name: str
    user_id: UUID

    class Config:
        orm_mode = True


class NotebookInDB(NotebookInDBBase):
    pass


class Notebook(NotebookInDBBase):
    notes: List[Note]
