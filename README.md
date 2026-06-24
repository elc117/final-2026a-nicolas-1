# FoodCheck - Gerenciamento de restaurantes

Nícolas Atkinson Ströher - Sistemas de Informação, 3° semestre <br>
Universidade Federal de Santa Maria, 2026<br>

Link do site: https://restaurant-frontend-0dk0.onrender.com 
[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/8MfjtJ-y)

## Descrição
FoodCheck é um projeto individual, iniciado como trabalho final da cadeira de Paradigmas de Programação, que tem como objetivo desenvolver um sistema integrado para fácil gerenciamento de restaurantes. A ideia do projeto foi inspirada por familiares presentes na área de público alvo, e pela vontade de aprofundar os conhecimentos em orientação a objetos e banco de dados. 
## Especificação

O aplicativo possui um backend Java gerenciado com Gradle, em comunicação com um frontend gerado pela Vercel v0 através de uma camada de requisições baseada na biblioteca Javalin. O sistema, até agora, conta com algumas funcionalidades básicas:

- Gerenciamento de estoque de ingredientes (manual);
- Gerenciamento de cadastro de funcionários (pelo administrador);
- Autenticação de login de usuário (implementado no backend, mas indisponível no front);
- Identificação de cargos e nível hierárquico para controle de acesso e funcionalidades.
- Gerenciamento de pedidos de compra de ingredientes


> PRÓXIMOS PASSOS - login efetivo / automatização de pedidos de compra

Por enquanto, a interface e funcionamento do aplicativo está limitada ao uso por um administrador da empresa, que pode gerenciar funcionários, o estoque de ingredientes e os pedidos de compra.
O sistema conta com testes unitários para as classes e repositórios do aplicativo, localizados em `src/test/`. Para executar os testes e verificar a integridade das classes e do banco de dados, basta acessar o backend e executar os testes com o Gradle:
```
cd backend
./gradlew test
```
## Relacionamento das Classes
As classes principais se relacionam de acordo com o diagrama a seguir:
<img width="1221" height="688" alt="image" src="https://github.com/user-attachments/assets/1cb09775-814f-4034-83d8-5f263612f1e1" />
No momento, o gerenciamento de funcionários e ingredientes/pedidos está separado, mas o plano é desenvolver o login, o que permite aos funcionários com perfil de administrador a editar ingredientes, e a funcionários da cozinha a criarem pedidos de compra que serão confirmados pelo chefe de cozinha ou pela administração.

# Demonstração
<img width="1834" height="908" alt="restaurante" src="https://github.com/user-attachments/assets/27d15691-f041-4b78-b9cc-0b8aedf8057f" />

## Histórico de Desenvolvimento

### 12/06/2026

**1. Classe e db de ingredientes implementados (nao testado)**
- Estrutura inicial criada com Gradle
- Fábrica de conexões - `config/ConnectionFactory.java`
- Classes base - `model/Ingredients.java, model/Employee.java`
- Enum para unidade de medida de ingrediente - `MeasurementUnit.java
- Repositório de ingredientes para comunicação com DB - `repository/IngredientRepository.java`
- Testes unitários - `model/IngredientTest.java, model/EmployeeTest.java`

### 13/06/2026

**2. Testes de integração para DB - ingredientes**
- Teste de integração - `test/repository/IngredientRepositoryIT.java`

**3. Classe e DB de usuário + testes**
- Classe de usuário - `model/User.java`
- Classes auxiliares - `model/WorkContract.java, model/WorkHour.java` 
	- *Somente declaração, talvez não sejam utilizadas em breve*
- Enum para tipo de contrato de emprego (CLT/PJ) - `model/enums/Employment.java`
- Repositório de usuários - `repository/UserRepository.java`
- Testes unitários - `model/EmployeeTest.java, model/UserTest.java`
- Teste de integração - `repository/UserRepositoryIT.java`
-  Banco de dados de teste criado para uso exclusivo em testes

**4. Conexao de funcionario com DB em implementação**
- Repositório de funcionários - `repository/EmployeeRepository.java`

### 14/06/2026


**5. Testes funcionario, nao funcional**
- A classe `Employee.java` agora tem um atributo `User user`, conectando um funcionário ao seu usuário
	- *A classe `User.java` representa somente o acesso ao sistema, enquanto `Employee.java` representa os dados reais do funcionário*
- Teste de integração - `repository/EmployeeRepositoryIT.java`

**6. EmployeeRepository consertado**
- Repositório de funcionário `EmployeeRepository.java` consertado
	- *Agora captura corretamente o ID fornecido pelo DB e trata o usuário relacionado*

**7. IngredientService e UserService - implementação parcial + criptografia de senha**
- Lógica de criptografia para senha - `config/CryptoUtils.java`
- Função `searchByLogin()` implementada em `UserRepository.java`
- Serviços para tratamento de entradas - `service/EmployeService.java, service/IngredientService.java, service/UserService.java`
	- *`EmployeeService.java`  - criado classe e construtores, mas não a lógica ainda*
- `UserRepository.java` atualizado para testar busca por login - `searchByLoginTest()`

### 16/06/2026
**8. Refatoração de testes**
- Enum para perfil de acesso ao sistema - `enums/AccessProfile.java`
- Testes refatorados e modularizados
	- *`asserts/` criado para modularizar e abstrair funções "assert" em `EmployeeAsserts.java, IngredientAsserts.java, UserAsserts.java`*
	- *`config/BaseIntegrationTest.java` criado para modularizar conexão e limpeza do DB de teste nos testes de integração (ITs herdam de `BaseIntegrationTest.java`)*
	- *`fixtures/` criado para modularizar e abstrair a criação de objetos de teste em `EmployeeFixtures.java, IngredientsFixtures.java, UserFixtures.java`*
- Organização de imports

**9. Refatoração para ID estático**
- O ID de todas as classes agora é final e definido pelo DB antes do objeto ser criado
	- *O objeto até já pode existir, mas o repositório o recebe como DTO, define o ID, armazena os dados e retorna uma cópia do objeto com o ID correto*
- DTOs para comunicação dos objetos com o DB
- Script de inicialização de tabelas do banco de dados - `resources/schema.sql`

### 19/06/2026
**10. Controle HTTP**
- Controllers criados em `controller/`para comunicação com frontend - `IngredientController.java, UserController.java, EmployeeController.java`
- Arquivos de `service/` finalizados
- Mapeamento de endpoints implementado em `main/.../App.java`
	- *O backend já pode receber requisições, falta comunicar com o front*
- Função `update` atualizada em todos os repositórios para retornar o objeto atualizado

### 20/06/2026
**11. Frontend adicionado**
- Adicionado projeto de frontend gerado com v0

**12. CRUD ingredientes conectado ao frontend**
- Operações de adicionar/editar/remover/buscar ingredientes implementados no frontend
	- *Funções assíncronas `fetch` definidas em `lib/api/ingredients.js`*
- `ingredientController.java` corrigido: agora trata corretamente as entradas de id do frontend
- Refatoração do frontend - nome de variáveis atualizado para condizer com o backend
	- *Frontend estava com variáveis `quantidadeAtual`, `unidadeMedida`... ao invés dos nomes definido no backend `currentAmount`, `measurementUnit`...*

### 23/06/2026
**13.CRUD de funcionários conectado ao frontend**
- Operações de adicionar/editar/remover/buscar ingredientes implementados no frontend
	- *Funções assíncronas `fetch` definidas em `lib/api/employees.js`*
- Classe `Employee` alterada para tratar novo atributo `hasAccess`
	- *Atributo utilizado para checar se um funcionário cadastrado tem acesso ao sistema*
- Validação de CPF e usuário corrigidas

**14. Classe de pedidos**
- Nova classe `model/Order.java` implementada para representar pedidos de integrediente.
- Camadas de repositório, serviço e controle para `model`
	- `repository/OrderRepository.java, service/OrderService.java, controller/OrderController.java`
- DTO `OrderDTO.java` para comunicação de objetos `Order` com o banco de dados


**15. CRUD de pedidos conectado ao frontend**
- Operações de CRUD da classe `Order.java` implementados no frontend
	- *Funções assíncronas `fetch` definidas em `/lib/api/orders.js`*
- Adicionada validação para atualizar estoque de ingrediente ao concluir pedido de compra

## Comentários e experiência
Como esse é meu primeiro projeto na linguagem Java e com o paradigma de orientação a objetos, descobri várias funcionalidades diferentes com a qual não estava acostumado.
Enquanto o código no geral não faz uso de elementos mais especializados da orientação a objetos e do Java, como herança, interfaces e etc., ao escrever o código para os testes me deparei com 2 elementos muito falados na orientação a objetos: a *classe abstrata* (herança) e a chamada *God Class*. <br><br>
Nos testes de integração do projeto, que testam o funcionamento dos repositórios e a conexão com o banco de dados, adotei a boa prática de iniciar o banco de dados vazio, realizar os testes, verificar (executar os *asserts*), e então limpar o banco de dados. No entanto, percebi que eu tinha a mesma função `clearDB()` em todos os arquivos, e quis modularizar. Com um pouco de pesquisa, decidi utilizar uma *classe abstrata* `BaseIntegrationTest.java`, em que defini a inicialização do banco de dados e todo esse processo. Assim, os testes de integração herdam dessa classe (com a palavra chave `extends`). O funcionamento fica o mesmo, mas o código fica mais limpo. <br><br>
Após isso, gostei do código mais limpo, e percebi que em todos os testes eu criava objetos de teste diretamente no teste de integração (como, por exemplo, instanciar um funcionário de forma *hardcoded* no próprio arquivo). Então, decidi modularizar isso também, e criei funções para criar objetos de teste, e coloquei na mesma classe abstrata `BaseIntegrationTest.java`. Então, eu precisava de funções de *assert* customizadas para avaliar os meus objetos de teste com os retornos do banco de dados: adicionei mais essas funções na classe abstrata. <br><br>
Um pouco depois, percebi que eu poderia utilizar essas funções de criação de objetos teste para os testes de classe e de outras partes do sistema também, mas me deparei com um problema: para testar as classes, eu não preciso acessar o banco de dados, mas isso está padronizado na classe abstrata. Diante disso, lembrei do termo *God Class* e pesquisei mais afundo, descobrindo na prática como a aglomeração de muitas responsabilidades em uma classe só pode ser prejudicial.
