import uuid
from datetime import datetime

from sqlalchemy import Boolean, Column, DateTime, ForeignKey, String
from sqlalchemy.orm import RelationshipProperty, relationship

from database.base import GUID, Base


class Notebook(Base):
    id = Column(GUID, primary_key=True, default=uuid.uuid4)
    name = Column(String(100), nullable=False)
    user_id = Column(GUID, ForeignKey("user.id"), nullable=False)

    user: RelationshipProperty = relationship("User", back_populates="notebooks")
    notes: RelationshipProperty = relationship("Note", back_populates="notebook")


class Note(Base):
    id = Column(GUID, primary_key=True, default=uuid.uuid4)
    title = Column(String(100), nullable=False)
    content = Column(String, nullable=False)
    user_id = Column(GUID, ForeignKey("user.id"), nullable=False)
    notebook_id = Column(GUID, ForeignKey("notebook.id"), nullable=False)
    summary = Column(String(255))
    memo = Column(String)
    is_favorite = Column(Boolean, default=False)
    created_at = Column(DateTime, default=datetime.now)
    updated_at = Column(DateTime, default=datetime.now, onupdate=datetime.now)

    user: RelationshipProperty = relationship("User", back_populates="notes")
    notebook: RelationshipProperty = relationship("Notebook", back_populates="notes")
