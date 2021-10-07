from typing import Any

from fastapi import FastAPI
from fastapi.responses import RedirectResponse

from app.api import router
from database.base import Base
from database.session import engine

Base.metadata.create_all(bind=engine)  # type: ignore

app = FastAPI()


@app.get("/", response_class=RedirectResponse)
def root() -> Any:
    return "/docs"


app.include_router(router)
