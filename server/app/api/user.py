from typing import Any, List
from uuid import UUID

from fastapi import APIRouter, Depends, HTTPException, status
from sqlalchemy.orm import Session

from database.session import get_db

from .. import schemas, services

router = APIRouter()


@router.get("", response_model=List[schemas.User], status_code=status.HTTP_200_OK)
def read_users(*, db: Session = Depends(get_db)) -> Any:
    users = services.user.find_all(db)
    return users


@router.post("", response_model=schemas.User, status_code=status.HTTP_201_CREATED)
def create_user(*, db: Session = Depends(get_db), user_in: schemas.UserCreate) -> Any:
    user = services.user.create(db, object_in=user_in)
    return user


# TODO: HTTPException를 상속하는 도메인 exception 구현
@router.get("/{user_id}", response_model=schemas.User, status_code=status.HTTP_200_OK)
def read_user(*, db: Session = Depends(get_db), user_id: UUID) -> Any:
    user = services.user.find_one(db, id=user_id)
    if not user:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail="User not found",
        )
    return user


@router.patch("/{user_id}", response_model=schemas.User, status_code=status.HTTP_200_OK)
def update_user(
    *, db: Session = Depends(get_db), user_id: UUID, user_in: schemas.UserUpdate
) -> Any:
    user = services.user.find_one(db, id=user_id)
    if not user:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail="User not found",
        )
    user = services.user.update(db, object_model=user, object_in=user_in)
    return user


# TODO: Check authorization to delete user
@router.delete("/{user_id}", status_code=status.HTTP_204_NO_CONTENT)
def delete_user(*, db: Session = Depends(get_db), user_id: UUID) -> Any:
    user = services.user.find_one(db, id=user_id)
    if not user:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail="User not found",
        )
    services.user.remove(db, object_model=user)
