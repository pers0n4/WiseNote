import uuid

from sqlalchemy import Column, String

from database.base import GUID, Base


class User(Base):
    id = Column(GUID, primary_key=True, default=uuid.uuid4)
    email = Column(String, nullable=False)
    password = Column(String, nullable=False)
    name = Column(String, nullable=False)
    phone = Column(String(13), nullable=False)
