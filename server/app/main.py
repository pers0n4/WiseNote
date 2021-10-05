from fastapi import FastAPI
from fastapi.responses import RedirectResponse

from database import Base
from database.session import engine

Base.metadata.create_all(bind=engine)

app = FastAPI()


@app.get("/", response_class=RedirectResponse)
def root():
    return "/docs"
