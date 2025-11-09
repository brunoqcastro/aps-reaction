from pydantic import BaseModel
from datetime import datetime

class ReactionBase(BaseModel):
    reaction_time: float

class ReactionCreate(ReactionBase):
    pass

class ReactionOut(ReactionBase):
    id: int
    timestamp: datetime

    class Config:
        orm_mode = True


class ConfigBase(BaseModel):
    min_ms: int
    max_ms: int

class ConfigOut(ConfigBase):
    id: int

    class Config:
        orm_mode = True
