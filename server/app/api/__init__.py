from fastapi.routing import APIRouter

from . import auth, user

router = APIRouter()

router.include_router(user.router, prefix="/users", tags=["user"])
router.include_router(auth.router, prefix="/auth", tags=["auth"])
