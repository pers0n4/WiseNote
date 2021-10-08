from fastapi.routing import APIRouter

from . import auth, note, user

router = APIRouter()

router.include_router(user.router, prefix="/users", tags=["user"])
router.include_router(auth.router, prefix="/auth", tags=["auth"])
router.include_router(note.router, prefix="/notes", tags=["note"])
