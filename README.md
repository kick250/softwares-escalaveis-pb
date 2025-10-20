# NexoERP

**NexoERP** é um sistema ERP para gestão de **estoques, produtos e pedidos**, com duas interfaces: uma **administrativa** e um **portal para clientes**.
Desenvolvido com **Java (Spring Boot)** no backend, **React** no frontend e arquitetura de microserviços.

---

## Funcionalidades Principais

* Gestão de estoques e produtos (criação, edição, exclusão).
* Controle de quantidades, preços e disponibilidade.
* Sistema de pedidos com aprovação, cancelamento e acompanhamento.
* Autenticação e autorização com JWT, perfis ERP_USER e PORTAL_USER.

---

## Tecnologias

* **Backend:** Java 21, Spring Boot 3.5.4, Spring Security, Spring Data JPA, PostgreSQL, MongoDB, Redis.
* **Frontend:** React 19, Vite, Bootstrap 5, Zustand, Axios.
* **Infraestrutura:** Docker, Docker Compose, Kubernetes, Nginx.

---

## Usuários Padrão

Para testes, o sistema já possui alguns usuários pré-cadastrados (senha: `123456`):

| Usuário      | Email                                                           | Perfil                         | Senha  |
| ------------ | --------------------------------------------------------------- | ------------------------------ | ------ |
| ERP USER     | [default@erp.com.br](mailto:default@erp.com.br)                 | ERP_USER                       | 123456 |
| PORTAL USER  | [default@portal.com.br](mailto:default@portal.com.br)           | PORTAL_USER                    | 123456 |
| PORTAL ADMIN | [defaul.admin@portal.com.br](mailto:defaul.admin@portal.com.br) | PORTAL_USER + ADMIN_PERMISSION | 123456 |

---

## Executando o Sistema

### Com Kubernetes

```bash
kubectl apply -f ./k8s/
```

> Aplica todos os manifests e sobe o sistema em ambiente de produção/local Kubernetes.

### Com Docker Compose (Desenvolvimento Local)

1. Copie o arquivo de configuração:

```bash
cp ./backend/src/main/resources/application.example.yml ./backend/src/main/resources/application.yml
```

2. Suba todos os serviços:

```bash
docker-compose -f docker-compose.all.yml up -d
```

O sistema ficará disponível nas seguintes portas:

* **Administrativo:** `http://localhost:8081`
* **Portal do Cliente:** `http://localhost:8082`

---

## Observações

* Certifique-se de ter **Docker**, **Docker Compose** e/ou **Kubernetes (kubectl)** instalados.
* A configuração de exemplo (`application.example.yml`) deve ser adaptada conforme seu ambiente.

---

## Vídeos

Aqui serão adicionados vídeos demonstrativos do sistema.


https://github.com/user-attachments/assets/3bcc13cc-d8fe-4684-8897-27d42a6c613c

