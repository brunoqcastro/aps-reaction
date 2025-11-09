from fastapi import FastAPI
from .database import Base, engine
from .routers import reactions, config

Base.metadata.create_all(bind=engine)

app = FastAPI(title="Reaction Game API")

app.include_router(reactions.router)
app.include_router(config.router)

@app.get("/")
def root():
    return {"message": "Reaction Game API está no ar!"}
