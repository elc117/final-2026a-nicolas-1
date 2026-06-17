# FoodCheck - Gerenciamento de restaurantes

[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/8MfjtJ-y)

## Descrição
FoodCheck é um projeto individual, iniciado como trabalho final da cadeira de Paradigmas de Programação, que tem como objetivo desenvolver um sistema integrado para fácil gerenciamento de restaurantes. A ideia do projeto foi inspirada por familiares presentes na área de público alvo, e pela vontade de aprofundar os conhecimentos em orientação a objetos e banco de dados. 
## Especificação
> OBS: O backend ainda está sendo integrado ao frontend; enquanto isso não finaliza, as funcionalidades abaixo existem no backend, mas estão indisponíveis para uso oficial. Para verificar o funcionamento, utilize os testes como apresentado abaixo.

O aplicativo possui um backend Java gerenciado com Gradle, em comunicação com um frontend gerado pela Vercel v0 através de uma camada de requisições baseada na biblioteca Javalin. O sistema, até agora, conta com algumas funcionalidades básicas:

- Gerenciamento de estoque de ingredientes (manual);
-  Gerenciamento de cadastro de funcionários (pelo administrador);
- Autenticação de login de usuário (implementado no backend, mas indisponível no front);
- Identificação de cargos e nível hierárquico para controle de acesso e funcionalidades.


> PRÓXIMOS PASSOS - contabilização de consumo de ingredientes


Por enquanto, a interface e funcionamento do aplicativo está limitada ao uso por um administrador da empresa, que pode gerenciar funcionários e o estoque de ingredientes.
O sistema conta com testes unitários para as classes e repositórios do aplicativo, localizados em `src/test/`. Para executar os testes e verificar a integridade das classes e do banco de dados, basta acessar o backend e executar os testes com o Gradle:
```
cd backend
./gradlew test
```
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

---

<div align="center">
 Nícolas Atkinson Ströher - Sistemas de Informação, 3° semestre <br>
Universidade Federal de Santa Maria, 2026
<div>