from fastapi import FastAPI, Depends, HTTPException
from sqlalchemy.orm import Session
from typing import List
import models, schemas, database

# Inicializa o banco
models.Base.metadata.create_all(bind=database.engine)

app = FastAPI(title="Reflex Hunter API")

# Dependência de sessão
def get_db():
    db = database.SessionLocal()
    try:
        yield db
    finally:
        db.close()

@app.get("/")
def root():
    return {"message": "Reflex Hunter API online!"}

# -------------------------------
# POST /score → Recebe resultados do ESP8266
# -------------------------------
@app.post("/score", response_model=schemas.ScoreResponse)
def create_score(score: schemas.ScoreCreate, db: Session = Depends(get_db)):
    db_score = models.Score(**score.dict())
    db.add(db_score)
    db.commit()
    db.refresh(db_score)
    return db_score

# -------------------------------
# GET /history → Retorna histórico de partidas
# -------------------------------
@app.get("/history", response_model=List[schemas.ScoreResponse])
def get_history(db: Session = Depends(get_db)):
    scores = db.query(models.Score).order_by(models.Score.timestamp.desc()).limit(20).all()
    return scores
