import uuid
from datetime import datetime

from sqlalchemy import Column, DateTime, ForeignKey, String
from sqlalchemy.orm import RelationshipProperty, relationship

from database.base import GUID, Base


class Note(Base):
    id = Column(GUID, primary_key=True, default=uuid.uuid4)
    title = Column(String(100), nullable=False)
    content = Column(String, nullable=False)
    user_id = Column(GUID, ForeignKey("user.id"), nullable=False)
    summary = Column(String(255))
    memo = Column(String)
    created_at = Column(DateTime, default=datetime.now)
    updated_at = Column(DateTime, default=datetime.now, onupdate=datetime.now)

    user: RelationshipProperty = relationship("User", back_populates="notes")
