from locust import HttpUser, task, between
import json
import random

API_KEY = "sk-biblioteca-key-12345"

class LivroUserCaio(HttpUser):
    # Tempo de espera entre cada tarefa: entre 1 e 3 segundos
    wait_time = between(1, 3)

    # Headers enviados em todas as requisicoes
    headers = {
        "X-API-Key": API_KEY,
        "Content-Type": "application/json"
    }

    @task(2)  # peso 2: executa 2x mais que o GET
    def criar_livro(self):
        payload = {
            "titulo": f"Livro Teste {random.randint(1, 9999)}",
            "ano": random.randint(1900, 2024),
            "autor": {"id": 1}
        }
        with self.client.post(
            "/livros",
            headers=self.headers,
            data=json.dumps(payload),
            catch_response=True
        ) as response:
            if response.status_code == 201:
                response.success()
                # Salva o ID do livro criado para o GET
                livro = response.json()
                self.ultimo_id = livro.get("id", 1)
            else:
                response.failure(f"POST /livros falhou: {response.status_code}")

    @task(1)  # peso 1
    def ler_livro(self):
        livro_id = getattr(self, 'ultimo_id', 1)
        with self.client.get(
            f"/livros/{livro_id}",
            headers=self.headers,
            catch_response=True
        ) as response:
            if response.status_code == 200:
                response.success()
            else:
                response.failure(f"GET /livros/{livro_id} falhou: {response.status_code}")
