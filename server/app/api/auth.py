from typing import Any

from fastapi import APIRouter, Depends, HTTPException, status
from fastapi.security import OAuth2PasswordRequestForm
from sqlalchemy.orm import Session

from database.session import get_db

from .. import schemas, services
from ..models import User

router = APIRouter()


@router.post("/token", response_model=schemas.Token, status_code=status.HTTP_200_OK)
def authenticate(
    db: Session = Depends(get_db), form_data: OAuth2PasswordRequestForm = Depends()
) -> Any:
    user = services.user.authenticate(
        db, email=form_data.username, password=form_data.password
    )
    if user is None:
        raise HTTPException(
            status_code=status.HTTP_401_UNAUTHORIZED,
            detail="Incorrect email or password",
            headers={"WWW-Authenticate": "Bearer"},
        )
    return {
        "access_token": services.create_access_token(user.id),
        "token_type": "bearer",
    }


@router.get("/token", response_model=schemas.User, status_code=status.HTTP_200_OK)
def test_token(current_user: User = Depends(services.get_current_user)) -> Any:
    return current_user
