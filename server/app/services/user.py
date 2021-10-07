from app.models.user import User
from app.schemas.user import UserCreate, UserUpdate

from .base import Service


class UserService(Service[User, UserCreate, UserUpdate]):
    # TODO: Encrypt password
    pass


user = UserService(User)
