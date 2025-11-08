from pydantic import BaseModel
from datetime import datetime

class ScoreCreate(BaseModel):
    player_id: str
    score: int
    hits: int
    fails: int

class ScoreResponse(BaseModel):
    id: int
    player_id: str
    score: int
    hits: int
    fails: int
    timestamp: datetime

    class Config:
        orm_mode = True
