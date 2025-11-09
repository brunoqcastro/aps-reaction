from fastapi import APIRouter, Depends
from sqlalchemy.orm import Session
from .. import crud, schemas
from ..database import SessionLocal

router = APIRouter(prefix="/reactions", tags=["Reactions"])

def get_db():
    db = SessionLocal()
    try:
        yield db
    finally:
        db.close()


@router.post("/", response_model=schemas.ReactionOut)
def create_reaction(reaction: schemas.ReactionCreate, db: Session = Depends(get_db)):
    return crud.create_reaction(db, reaction)


@router.get("/top10", response_model=list[schemas.ReactionOut])
def get_top10(db: Session = Depends(get_db)):
    return crud.get_top_reactions(db)
