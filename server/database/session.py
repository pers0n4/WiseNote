from typing import Generator

from sqlalchemy import create_engine
from sqlalchemy.orm import sessionmaker

# `check_same_thread` needed fot sqlite
engine = create_engine(
    "sqlite:///./db.sqlite3", connect_args={"check_same_thread": False}
)

SessionLocal = sessionmaker(autocommit=False, autoflush=False, bind=engine)


def get_db() -> Generator:
    db = SessionLocal()
    try:
        yield db
    finally:
        db.close()
