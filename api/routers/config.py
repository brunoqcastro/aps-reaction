from fastapi import APIRouter, Depends
from sqlalchemy.orm import Session
from .. import crud, schemas
from ..database import SessionLocal

router = APIRouter(prefix="/config", tags=["Config"])

def get_db():
    db = SessionLocal()
    try:
        yield db
    finally:
        db.close()


@router.get("/", response_model=schemas.ConfigOut)
def get_config(db: Session = Depends(get_db)):
    return crud.get_config(db)


@router.put("/", response_model=schemas.ConfigOut)
def update_config(config: schemas.ConfigBase, db: Session = Depends(get_db)):
    return crud.update_config(db, config)
