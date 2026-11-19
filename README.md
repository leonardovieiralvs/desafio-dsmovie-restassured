# Desafio DSMovie: Testes de API com Rest Assured

Testes automatizados de integração para a API REST do **DSMovie**, feitos com **Rest Assured** e **JUnit 5**. O projeto faz parte do curso Java Spring Professional da [DevSuperior](https://devsuperior.com.br) e cobre os endpoints de filmes e avaliações, incluindo autenticação, autorização e validação de dados.

## O que é testado

Os testes validam o status HTTP e também o corpo da resposta (`status`, `error`, `path`, `errors.fieldName` e `errors.message`).

### `MovieControllerRA`

| Cenário | Requisição | Resultado esperado |
|---|---|---|
| Listar filmes sem argumentos | `GET /movies` | 200 e lista não vazia |
| Listar filmes filtrando por título | `GET /movies?title=...` | 200 e página com o filme filtrado |
| Buscar filme por id existente | `GET /movies/{id}` | 200 e filme correspondente |
| Buscar filme por id inexistente | `GET /movies/{id}` | 404 |
| Inserir filme com dados válidos (admin) | `POST /movies` | 201 |
| Inserir filme com título em branco (admin) | `POST /movies` | 422 com detalhe do campo `title` |
| Inserir filme logado como cliente | `POST /movies` | 403 |
| Inserir filme com token inválido | `POST /movies` | 401 |

### `ScoreControllerRA`

| Cenário | Requisição | Resultado esperado |
|---|---|---|
| Salvar avaliação válida (cliente) | `PUT /scores` | 200 e filme atualizado |
| Salvar avaliação de filme inexistente | `PUT /scores` | 404 |
| Salvar avaliação sem `movieId` | `PUT /scores` | 422 com detalhe do campo `movieId` |
| Salvar avaliação com nota menor que zero | `PUT /scores` | 422 com detalhe do campo `score` |

## Tecnologias

- Java
- Spring Boot (aplicação testada)
- Rest Assured
- JUnit 5
- Hamcrest
- json-simple
- Maven

## Como executar

1. Clone o repositório:

```bash
git clone https://github.com/leonardovieiralvs/desafio-dsmovie-restassured.git
cd desafio-dsmovie-restassured
```

2. Suba a aplicação em `http://localhost:8080` (pela IDE ou pelo Maven):

```bash
mvn spring-boot:run
```

3. Com a aplicação no ar, execute os testes pela IDE ou pelo Maven:

```bash
mvn test -Dtest="MovieControllerRA,ScoreControllerRA"
```

> As classes terminam em `RA` e dependem da aplicação rodando, por isso não entram no padrão de nomes que o Maven executa sozinho no `mvn test`. É necessário indicá-las com `-Dtest`.

## Observações

- Os testes dependem dos dados iniciais (seed) da aplicação, como o filme de id 1 (*The Witcher*) e os usuários de teste, usados para obter os tokens de admin e de cliente por meio da classe `TokenUtil`.
- Os testes de inserção (`POST /movies`) e de avaliação (`PUT /scores`) gravam dados no banco. Com o banco H2 do perfil de teste, os dados são recriados a cada inicialização da aplicação.

## Autor

**Leonardo Vieira de Souza**
[github.com/leonardovieiralvs](https://github.com/leonardovieiralvs)
