# MiniAutorizador

Decisão: Não retornar a senha do cartão no endpoint de criação do cartão por questão de segurança

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
