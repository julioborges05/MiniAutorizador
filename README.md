# MiniAutorizador

Este projeto consiste em um mini autorizador de cartão, desenvolvido com o objetivo de simular o processo
básico de autorização de transações financeiras. Nele, é possível realizar a criação de cartões e aplicar
regras de validação para transações, como limites, saldo disponível e critérios de aprovação ou recusa.

## 🔑 Arquitetura e Decisões Técnicas

A arquitetura MVC foi escolhida para este projeto por promover uma separação clara
de responsabilidades, facilitando a manutenção, a evolução do código e a legibilidade da aplicação. Utilizando
Java com Spring Framework.

O tratamento de erros é realizado por meio de um GlobalExceptionHandler, permitindo centralizar as exceções e
padronizar as respostas da API. Dessa forma, o código de negócio pode trabalhar predominantemente no “caminho feliz”,
tornando-se mais limpo, coeso e fácil de entender, sem a necessidade de múltiplos blocos de tratamento de exceção
espalhados pela aplicação.

Para o controle de concorrência, foi adotada a estratégia de Optimistic Lock na tabela de cartão, assegurando que
atualizações concorrentes sejam tratadas de forma segura e eficiente, sem bloqueios desnecessários. Já no processo de
realização de transações, foi utilizado o comando SELECT FOR UPDATE, garantindo o bloqueio do registro durante a
operação crítica e evitando inconsistências, como transações duplicadas ou saldo incorreto, em cenários de
acesso simultâneo.

As regras de autorização foram implementadas por meio da interface AuthorizationRule, permitindo uma abordagem
extensível e desacoplada. O Spring é responsável por instanciar automaticamente todos os componentes que
implementam essa interface, possibilitando que o método de realização de transações apenas itere sobre essas regras,
sem conhecer suas implementações específicas. Dessa forma, uma transação é aprovada caso nenhuma das regras aplicadas
retorne reprovação, facilitando a inclusão de novas validações sem impacto no fluxo principal.

Outra decisão importante de segurança foi nunca retornar senhas para o usuário, mesmo durante o processo de criação do
cartão. Essa abordagem reduz riscos de exposição de dados sensíveis, reforça boas práticas de segurança e garante que
informações críticas sejam tratadas exclusivamente de forma interna e protegida pela aplicação.

## 🚗 Execução do projeto com docker

Para executar o projeto localmente, foi disponibilizada uma configuração via Docker Compose, que orquestra os
serviços necessários para o funcionamento da aplicação. O ambiente é composto por um banco de dados MySQL e pela
aplicação do mini autorizador, já empacotada em uma imagem Docker. No projeto, foi utilizado o Flyway para o
controle de versionamento do banco de dados, portanto não é necessária a execução de nenhum script externo, pois as
migrações são aplicadas automaticamente na inicialização da aplicação.

## 🔐 Autenticação

Este projeto utiliza **Basic Authentication** para autenticação das requisições.

Todas as chamadas à API **devem** conter o header `Authorization` com as credenciais do usuário.

### Como autenticar

1. Concatene o usuário e a senha no formato:
    ```
    usuario:senha

2. Codifique o valor em **Base64**

3. Envie no header da requisição:
   Authorization: Basic <base64(usuario:senha)>


### Exemplo

```
Authorization: Basic bWV1X3VzdWFyaW86bWluaGFfc2VuaGE=
