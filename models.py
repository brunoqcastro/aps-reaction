from sqlalchemy import Column, Integer, String, DateTime
from datetime import datetime
from database import Base

class Score(Base):
    __tablename__ = "scores"

    id = Column(Integer, primary_key=True, index=True)
    player_id = Column(String, index=True)
    score = Column(Integer)
    hits = Column(Integer)
    fails = Column(Integer)
    timestamp = Column(DateTime, default=datetime.utcnow)
