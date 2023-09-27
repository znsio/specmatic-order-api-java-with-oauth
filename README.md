# Specmatic Sample Client Application

![HTML client talks to client API which talks to backend api](specmatic-sample-architecture.svg)

BFF = Backend For Frontend, the API invoked by the HTTP calls in the client HTML page (Website UI).

This project contains the product API, which is used by a small [ecommerce client application]((https://github.com/znsio/specmatic-order-ui).

Here is the [contract](https://github.com/znsio/specmatic-order-contracts/blob/main/in/specmatic/examples/store/api_order_v1.yaml) governing the interaction of the client with the product API.

The architecture diagram was created using the amazing free online SVG editor at [Vectr](https://vectr.com).

This project demonstrates how to run contract tests using Specmatic against an API Resource Server.
A resource server protects its resources by requiring all clients to supply a valid OAuth token in the Authorization header in the request. 
We have used a Spring Boot app, Keycloak and Spring security to explain how to run the application in prod/dev and test modes.

## Dev Mode:
In this mode, we'll run Keycloak locally use it as our OAuth authorization server.  
We use a Security Configuration (refer ```src/main/java/com/store/config/SecurityConfig.kt```) which secures every endpoint with scope : 'email'.  
This means, to access any endpoint, the request must contain a token with the 'email' scope in the Authorization header.  
The application validates the token received in every request by calling the ```spring.security.oauth2.resourceserver.jwt.issuer-uri``` url defined in the ```application.properties``` file.  

#### Start Keycloak
From the project root folder, run:

```shell
docker compose up
```
This will start the Keycloak server on localhost:8083  

#### Start the application
From the project root folder, run:

```shell
mvn clean spring-boot:run -Dspring-boot.run.profiles=dev
```
This will start the application on localhost:8080

#### Generate OAuth token
Open Postman.  
Creat a new request with method:  ```GET``` and url: ```http://localhost:8080/products/10```  
Under the authorization tab select type as OAuth 2.0 and click on Get New Access Token button with below details
```
Auth URL = http://localhost:8083/realms/specmatic/protocol/openid-connect/auth
Access Token URL = http://localhost:8083/realms/specmatic/protocol/openid-connect/token
CLient ID = order-api
Client Secret = <blank>
Scope = profile email
```
And select "Authorize using browser" and click "Get New Access Token"  
When redirected to browser enter the following user credentials:  
```
username: user1
password: password
```
Click the Signin button.  
You should then be able to see the generated access token with a ```Use Token``` button on top right.  
Click the ```Use Token``` button.  
This will take you back to your Postman request tab with the oauth token already added to the Authorization header.  
(The Authorization header might be hidden in the default view, but you can click on the hidden headers to see it)


#### Make request with OAuth token
Click on the ```Send``` button in Postman.  
You should see the following response:  
```json
{
"name": "XYZ Phone",
"type": "gadget",
"inventory": 6,
"id": 10
}
```


## Test Mode:
In test mode, we don't need the Keycloak server running (we can shut it down).
The application uses a dummy security configuration ((refer ```src/main/java/com/store/config/DummySecurityConfig.kt```)) which only checks if the Authorization header contains a string which starts with "Bearer ".
The actual token is not validated.

Specmatic will use the Order API's open api specification, read the security scheme, generate a token and send it in the appropriate header while making a request.
From the open api spec we can see that the service uses the ```oAuth2AuthCode``` security scheme, which is defined as follows:
```yaml
  securitySchemes:
    oAuth2AuthCode:
      type: oauth2
      description: keycloak based oauth security example
      flows:
        authorizationCode:
          authorizationUrl: http://localhost:8083/realms/specmatic/protocol/openid-connect/auth
          tokenUrl: http://localhost:8083/realms/specmatic/protocol/openid-connect/token

security:
  - oAuth2AuthCode: []
```

Specmatic will check if an Open API security scheme named ```oAuth2AuthCode``` is defined in the ```security``` section in specmatic.json.  
If it finds it, it will pick up the token defined for the theme and use it to run tests.  
If no such theme is defined in specmatic.json, Specmatic will auto-generate a random string to be sent as the token.  

We can see that we do have the ```oAuth2AuthCode``` security scheme defined in specmatic.json:
```json
{
  "security": {
    "OpenAPI": {
      "securitySchemes": {
        "oAuth2AuthCode": {
          "type": "oauth2",
          "token": "OAUTH1234"
        }
      }
    }
  }
}
```

#### Running Tests
Go to the ContractTest class at ```src/test/java/com/store/ContractTest```  
Run the contract tests (Ctrl+Shift+R)  
All tests should pass.  
You should see the Authorization header set for every test as:
```
Request to http://localhost:8080 at 2023-9-27 6:59:20.607
    GET /products/10
    Authorization: Bearer OAUTH1234
    Accept-Charset: UTF-8
    Accept: */*
```
