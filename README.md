# Specmatic Sample Client Application

![HTML client talks to client API which talks to backend api](specmatic-sample-architecture.svg)

BFF = Backend For Frontend, the API invoked by the HTTP calls in the client HTML page (Website UI).

This project contains the product API, which is used by a small [ecommerce client application]((https://github.com/znsio/specmatic-order-ui).

Here is the [contract](https://github.com/znsio/specmatic-order-contracts/blob/main/in/specmatic/examples/store/api_order_v1.yaml) governing the interaction of the client with the product API.

The architecture diagram was created using the amazing free online SVG editor at [Vectr](https://vectr.com).

```shell
docker compose up
```

Start the application and send the request through Postman,
In Postman request under authorization tab select type as OAuth 2.0 and click on Get New Access Token button with below details
```
auth-url = http://localhost:8083/realms/specmatic/protocol/openid-connect/auth
access-token-url = http://localhost:8083/realms/specmatic/protocol/openid-connect/token
client-id = order-api

username: user1
password: password
```
And select "Authorize using browser" and click "Get New Access Token"

When redirected to browser enter username as "user1" and password as "password" and click login which will re-open postman with access token.