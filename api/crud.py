from sqlalchemy.orm import Session
from . import models, schemas

def create_reaction(db: Session, reaction: schemas.ReactionCreate):
    db_reaction = models.Reaction(reaction_time=reaction.reaction_time)
    db.add(db_reaction)
    db.commit()
    db.refresh(db_reaction)
    return db_reaction

def get_top_reactions(db: Session, limit: int = 10):
    return db.query(models.Reaction).order_by(models.Reaction.reaction_time.asc()).limit(limit).all()

def get_last_reaction(db: Session):
    return db.query(models.Reaction).order_by(models.Reaction.timestamp.desc()).first()

def get_config(db: Session):
    config = db.query(models.Config).first()
    if not config:
        config = models.Config()
        db.add(config)
        db.commit()
        db.refresh(config)
    return config

def update_config(db: Session, new_config: schemas.ConfigBase):
    config = get_config(db)
    config.min_ms = new_config.min_ms
    config.max_ms = new_config.max_ms
    db.commit()
    db.refresh(config)
    return config
