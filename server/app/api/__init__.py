from fastapi.routing import APIRouter

from . import user

router = APIRouter()

router.include_router(user.router, prefix="/users", tags=["user"])
