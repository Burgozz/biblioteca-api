from locust import HttpUser, task, between
import json
import random

API_KEY = "sk-biblioteca-key-12345"

class AutorUserJoao(HttpUser):
    wait_time = between(1, 3)

    headers = {
        "X-API-Key": API_KEY,
        "Content-Type": "application/json"
    }

    @task(2)
    def criar_autor(self):
        nomes = ["Gabriel", "Lucas", "Maria", "Ana", "Carlos", "Julia"]
        nacionalidades = ["Brasileiro", "Argentino", "Portugues", "Frances"]
        payload = {
            "nome": f"{random.choice(nomes)} Autor {random.randint(1, 9999)}",
            "nacionalidade": random.choice(nacionalidades)
        }
        with self.client.post(
            "/autores",
            headers=self.headers,
            data=json.dumps(payload),
            catch_response=True
        ) as response:
            if response.status_code == 201:
                response.success()
                autor = response.json()
                self.ultimo_id = autor.get("id", 1)
            else:
                response.failure(f"POST /autores falhou: {response.status_code}")

    @task(1)
    def ler_autor(self):
        autor_id = getattr(self, 'ultimo_id', 1)
        with self.client.get(
            f"/autores/{autor_id}",
            headers=self.headers,
            catch_response=True
        ) as response:
            if response.status_code == 200:
                response.success()
            else:
                response.failure(f"GET /autores/{autor_id} falhou: {response.status_code}")
