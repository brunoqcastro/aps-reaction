#!/usr/bin/env python
"""Script para executar o servidor FastAPI"""
import uvicorn

if __name__ == "__main__":
    print("=" * 50)
    print("🚀 Iniciando Reaction Game API...")
    print("=" * 50)
    print("\n📡 Servidor disponível em:")
    print("   - Local: http://localhost:5000")
    print("   - Rede: http://0.0.0.0:5000")
    print("\n📚 Documentação disponível em:")
    print("   - Swagger UI: http://localhost:5000/docs")
    print("   - ReDoc: http://localhost:5000/redoc")
    print("\n" + "=" * 50 + "\n")
    
    uvicorn.run(
        "api.main:app",
        host="0.0.0.0",
        port=5000,
        reload=True,
        log_level="info"
    )

