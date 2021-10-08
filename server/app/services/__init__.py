from .auth import (
    create_access_token,
    get_current_user,
    get_password_hash,
    verify_password,
)
from .user import user

# from app.models.base import Base
# from app.schemas.base import BaseCreate, BaseUpdate
# from .base import Service
# base = Service[Base, BaseCreate, BaseUpdate]

__all__ = [
    "user",
    "get_current_user",
    "get_password_hash",
    "verify_password",
    "create_access_token",
]
