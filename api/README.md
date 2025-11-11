# ⚡ API de Reações — Projeto Arduino + FastAPI

Esta API foi desenvolvida para armazenar os tempos de reação obtidos em um jogo físico com **Arduino** (ou ESP8266/ESP32).  
O sistema permite registrar os resultados, ajustar o tempo aleatório de reação e consultar o ranking com os melhores tempos.

---

## 🚀 Tecnologias Utilizadas

- **Python 3.10+**
- **FastAPI** — Framework principal para a API REST
- **Uvicorn** — Servidor ASGI
- **SQLAlchemy** — ORM para banco de dados
- **SQLite** — Banco de dados local (simples e leve)

---

## 📁 Estrutura do Projeto

```

📦 api_reaction_game
├── main.py               # Ponto de entrada da aplicação
├── database.py           # Configuração do banco de dados
├── models.py             # Modelos do SQLAlchemy
├── schemas.py            # Schemas do Pydantic
├── crud.py               # Funções CRUD
└── requirements.txt      # Dependências do projeto

````

---

## ⚙️ Instalação e Execução

### 1️⃣ Clone o repositório
```bash
git clone https://github.com/brunoqcastro/aps-reaction.git
cd api-reaction-game
````

### 2️⃣ Crie e ative um ambiente virtual (opcional, mas recomendado)

```bash
python -m venv venv
source venv/bin/activate     # Linux/Mac
venv\Scripts\activate        # Windows
```

### 3️⃣ Instale as dependências

```bash
pip install -r requirements.txt
```

### 4️⃣ Execute a aplicação

```bash
uvicorn main:app --host 0.0.0.0 --port 5000 --reload
```

> ⚠️ O parâmetro `--host 0.0.0.0` permite que o Arduino (ou qualquer dispositivo na mesma rede) acesse a API.

---

## 🧠 Banco de Dados

A aplicação utiliza **SQLite**, e o arquivo `reaction.db` é criado automaticamente na primeira execução.

Para resetar o banco:

```bash
rm reaction.db
```

ou no Windows:

```bash
del reaction.db
```

---

## 📡 Endpoints Disponíveis

### 🔹 `GET /config`

Retorna os valores atuais de tempo **mínimo** e **máximo** usados pelo Arduino para determinar o momento em que o LED verde acende.

#### Exemplo de resposta:

```json
{
  "min_ms": 500,
  "max_ms": 5000
}
```

---

### 🔹 `PUT /config`

Atualiza os tempos **mínimo** e **máximo** de reação (em milissegundos).

#### Corpo da requisição:

```json
{
  "min_ms": 1000,
  "max_ms": 3000
}
```

#### Exemplo de resposta:

```json
{
  "message": "Configuração atualizada com sucesso"
}
```

---

### 🔹 `POST /reactions`

Registra um novo tempo de reação medido pelo Arduino.

#### Corpo da requisição:

```json
{
  "reaction_time": 428
}
```

#### Exemplo de resposta:

```json
{
  "id": 1,
  "reaction_time": 428,
  "created_at": "2025-11-08T12:31:20"
}
```

---

### 🔹 `GET /reactions/top`

Retorna os **10 melhores tempos** de reação registrados.

#### Exemplo de resposta:

```json
[
  { "reaction_time": 312, "created_at": "2025-11-08T12:30:01" },
  { "reaction_time": 335, "created_at": "2025-11-08T12:32:45" },
  ...
]
```

---

## 🧩 Integração com o Arduino

O Arduino envia e consome dados dessa API.
Para funcionar corretamente:

* O Arduino e o computador devem estar **na mesma rede Wi-Fi**
* A URL da API deve seguir o formato:

  ```
  const char* serverUrl = "http://192.168.0.40:5000";
  ```

  *(substitua `192.168.0.40` pelo seu IPv4 local)*

---

## 🧪 Testando via navegador ou curl

### Testar `/config`

```bash
curl http://localhost:5000/config
```

### Atualizar `/config`

```bash
curl -X PUT http://localhost:5000/config \
     -H "Content-Type: application/json" \
     -d '{"min_time": 800, "max_time": 4000}'
```

### Registrar reação

```bash
curl -X POST http://localhost:5000/reactions \
     -H "Content-Type: application/json" \
     -d '{"reaction_time": 512}'
```

### Consultar ranking

```bash
curl http://localhost:5000/reactions/top
```

---

## 🧰 Dependências

Arquivo `requirements.txt`:

```
fastapi
uvicorn
sqlalchemy
pydantic
```

---
