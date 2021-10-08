from typing import Optional
from uuid import UUID

from pydantic import BaseModel, EmailStr


class UserBase(BaseModel):
    email: Optional[EmailStr] = None
    password: Optional[str] = None
    name: Optional[str] = None
    phone: Optional[str] = None


class UserCreate(UserBase):
    email: EmailStr
    password: str
    name: str
    phone: str


class UserUpdate(UserBase):
    pass


class UserInDBBase(BaseModel):
    id: UUID
    email: EmailStr
    name: str
    phone: str

    class Config:
        orm_mode = True


class UserInDB(UserInDBBase):
    password: str


class User(UserInDBBase):
    pass
