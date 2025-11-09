from sqlalchemy import Column, Integer, Float, DateTime
from datetime import datetime
from .database import Base

class Reaction(Base):
    __tablename__ = "reactions"

    id = Column(Integer, primary_key=True, index=True)
    timestamp = Column(DateTime, default=datetime.utcnow)
    reaction_time = Column(Float, nullable=False)

class Config(Base):
    __tablename__ = "config"

    id = Column(Integer, primary_key=True, index=True)
    min_ms = Column(Integer, default=500)
    max_ms = Column(Integer, default=5000)
