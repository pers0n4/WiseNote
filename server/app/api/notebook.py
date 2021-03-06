from typing import Any, List

from fastapi import APIRouter, Depends, HTTPException, status
from sqlalchemy.orm import Session

from database.session import get_db

from .. import models, schemas, services

router = APIRouter()


@router.get(
    "", response_model=List[schemas.NotebookInDB], status_code=status.HTTP_200_OK
)
def read_notebooks(
    *,
    db: Session = Depends(get_db),
    current_user: models.User = Depends(services.get_current_user),
) -> Any:
    notebooks = services.notebook.find_by_user(
        db,
        user_id=current_user.id,  # type: ignore
    )
    return notebooks


@router.post("", response_model=schemas.Notebook, status_code=status.HTTP_201_CREATED)
def create_notebook(
    *,
    db: Session = Depends(get_db),
    notebook_in: schemas.NotebookCreate,
    current_user: models.User = Depends(services.get_current_user),
) -> Any:
    notebook = services.notebook.find_by_user_and_name(
        db,
        user_id=current_user.id,  # type: ignore
        name=notebook_in.name,
    )
    if notebook:
        raise HTTPException(
            status_code=status.HTTP_400_BAD_REQUEST,
            detail="Notebook with this name already exists",
        )
    return services.notebook.create_with_user(
        db,
        object_in=notebook_in,
        user_id=current_user.id,  # type: ignore
    )


@router.get(
    "/{notebook_name}",
    response_model=schemas.Notebook,
    status_code=status.HTTP_200_OK,
)
def read_notebook_by_name(
    *,
    db: Session = Depends(get_db),
    notebook_name: str,
    current_user: models.User = Depends(services.get_current_user),
) -> Any:
    notebook = services.notebook.find_by_user_and_name(
        db,
        user_id=current_user.id,  # type: ignore
        name=notebook_name,
    )
    return notebook


@router.patch(
    "/{notebook_name}",
    response_model=schemas.Notebook,
    status_code=status.HTTP_200_OK,
)
def update_notebook(
    *,
    db: Session = Depends(get_db),
    notebook_name: str,
    notebook_in: schemas.NotebookUpdate,
    current_user: models.User = Depends(services.get_current_user),
) -> Any:
    notebook = services.notebook.find_by_user_and_name(
        db, user_id=current_user.id, name=notebook_name  # type: ignore
    )
    if notebook is None:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail="Notebook not found",
        )
    notebook = services.notebook.update(
        db, object_model=notebook, object_in=notebook_in
    )
    return notebook


@router.delete("/{notebook_name}", status_code=status.HTTP_204_NO_CONTENT)
def delete_notebook(
    *,
    db: Session = Depends(get_db),
    notebook_name: str,
    current_user: models.User = Depends(services.get_current_user),
) -> Any:
    notebook = services.notebook.find_by_user_and_name(
        db,
        user_id=current_user.id,  # type: ignore
        name=notebook_name,
    )
    if not notebook:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail="Notebook not found",
        )
    services.notebook.remove(db, object_model=notebook)
