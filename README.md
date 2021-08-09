## Desafio Automação de Testes - API
Este é o documento com as orientações do desafio de automação da API que
está neste repositório.

### O que você deve fazer:
- Primeiro você deverá subir a aplicação back-end contida nesse repositório, conforme
orientações abaixo, pois utilizaremos ela como alvo dos nossos testes. (tópico “Como executar a aplicação”)
- Segundo você deverá criar um projeto baseado em Maven na sua IDE desenvolvimento favorita utilizando a linguagem de programação Java.
- Terceiro você deverá realizar a leitura das regras listadas abaixo e elaborar seus cenários de testes. (tópico “Regras”)
- Quarto você deverá realizar a automação dos testes dos cenários a serem cobertos conforme sua escolha.

### Forma de Entrega
Você deverá comitar em seu Gitlab pessoal o projeto de teste automatizado em um projeto Privado adicionando o usuário “sicredi_user” como “developer. (Para realizar isso basta acessar o menu Settings -> Members, procurar por “sicredi_owner” e adicionar ao projeto)

### Requisitos
- Java 8+ JDK deve estar instalado
- Maven deve estar instalado e configurado no path da aplicação

- Como executar a aplicação:
Na raiz do projeto, através de seu Prompt de Commando/Terminal/Console execute o
comando
`mvn clean spring-boot:run`
A aplicação estará disponível através da URL http://localhost:8080
Você pode trocar a porta da aplicação, caso a 8080 já estiver em uso, adicionando a
propriedade JVM server.port.
`mvn clean spring-boot:run -Dserver.port=8888`

#### Documentacão técnica da aplicação
A documentação técnica da API está disponível através do OpenAPI/Swagger em
http://localhost:8080/swagger-ui.html

### Regras
#### Restrições

##### Consultar uma restrição pelo CPF
`GET <host>/api/v1/restricoes/{cpf}`

O endpoint de Restrições tem a finalidade de consultar o CPF informando, retornando se ele possui ou não uma restrição.

- Se não possui restrição do HTTP Status 204 é retornado
- Se possui restrição o HTTP Status 200 é retornado com a mensagem "O CPF 99999999999 possui restrição"

CPFs com restrição
| CPF         |
|-------------|
| 97093236014 |
| 60094146012 |
| 84809766080 |
| 62648716050 |
| 26276298085 |
| 01317496094 |
| 55856777050 |
| 19626829001 |
| 24094592008 |
| 58063164083 |

### Simulações
A simulação é um cadastro que ficará registrado informações importantes sobre o crédito como valor, parcelas, dados de contato, etc...

### Criar uma simulação

`POST <host>/api/v1/simulacoes`

Este endpoint é responsável por inserir uma nova simulação.

Existem os seguintes atributos a serem informados, com suas respectivas regras:

| Atributo | Obrigatório | Regra |
|----------|-----------------|----|
| cpf | sim | texto informando o CPF não no formato 999.999.999-99 |
| nome | sim | texto informando o nome da pessoa |
| email | sim | texto informado um e-mail válido |
| valor | sim | valor da simulação que deve ser igual ou maior que R$ 1.000 e menor ou igual que R$ 40.000 |
| parcela | sim | número de parcelas para pagamento que deve ser igual ou maior que 2 e menor ou igual a 48 |
| seguro | sim | booleano true se com seguro e false se sem seguro |

- Uma simulação cadastrada com sucesso retorna o HTTP Status 201 e os dados inseridos como retorno
- Uma simulação com problema em alguma regra retorna o HTTP Status 400 com a lista de erros
- Uma simulação para um mesmo CPF retorna um HTTP Status 409 com a mensagem "CPF já existente"

### Alterar uma simulação
`PUT <host>/api/v1/simulacoes/{cpf}`

Altera uma simulação já existente, onde o CPF deve ser informado para que a alteração possa ser efetuada.

- A alteração pode ser feita em qualquer atributo da simulação
- As mesmas regras se mantém
- Se o CPF não possuir uma simulação o HTTP Status 404 é retornado com a
mensagem "CPF não encontrado"

### Consultar todas a simulações cadastradas

`GET <host>/api/v1/simulacoes`

Lista as simulações cadastradas.

- Retorna a lista de simulações cadastradas e existir uma ou mais
- Retorna HTTP Status 204 se não existir simulações cadastradas

### Consultar uma simulação pelo CPF

`GET <host>/api/v1/simulacoes/{cpf}`

Retorna a simulação previamente cadastrada pelo CPF.

- Retorna a simulação cadastrada
- Se o CPF não possuir uma simulação o HTTP Status 404 é retornado

### Remover uma simulação

`DELETE <host>/api/v1/simulacoes/{id}`

Remove uma simulação previamente cadastrada pelo seu ID.

- Retorna o HTTP Status 204 se simulação for removida com sucesso
- Retorna o HTTP Status 404 com a mensagem "Simulação não encontrada" se não existir a simulação pelo ID informado