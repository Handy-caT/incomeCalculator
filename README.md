# Income Calculator REST API

..description..

- **[Setup Guide](#setup-guide)**

## <a id="setup">Setup Guide</a>

First of all clone repository

```bash
git clone https://github.com/Handy-caT/incomeCalculator.git
```

You can choose a way to start up the application

- Using **[run](./run)** bash script 
  
  **[run](./run)** was created as a single entry point to run the application, so you can use it to start application in all possible ways: 
  
  - Using **Docker**
    
    ```bash
    ./run -d
    ```
  
  - Using **Maven**
    
    ```bash
    ./run -m
    ```
  
  If you want to know more about **[run](./run)** script usage, read here 

- Manually using **Docker**
  
  ```bash
  docker-compose up
  ```

The application will start running at  [http://localhost:8080/]()

## Using REST API

### Authentication

You need to be authenticated to use API's but some features is avaolable for not authenticated users. Let's start from authentication

| **Method** | **Url**   | **Description**            | **Request Sample** |
| ---------- | --------- | -------------------------- | ------------------ |
| POST       | /auth     | Authentication             |                    |
| POST       | /register | Registration for new users |                    |

After registration/authentication you will recieve a token for further usage of API.

Token is used as a bearer token, so you need to add **Authentication header** to all requests, you will get new token in response headers. So your token is uptadet after all request. Token also has **1 day** expiration limit.

### Users

| **Metoh** | **Url**               | **Description**                                          | **Request Sample**      |
| --------- | --------------------- | -------------------------------------------------------- | ----------------------- |
| GET       | /users                | All users list. Only for admin users                     |                         |
| GET       | /users/me             | Get authenticated user info                              |                         |
| GET       | /users/{id}           | Get user by id, if user is same to authenticated user    |                         |
| DELETE    | /users/{id}           | Delete user by id, if user is same to authenticated user |                         |
| PATCH     | /users/{id}/password  | Password update                                          | [JSON](#passwordChange) |
| PATCH     | /users/{id}/makeAdmin | Only for admins                                          |                         |
| PATCH     | /users/{id}/makeUser  | Only for admins                                          |                         |

### Currency Units

| **Method** | **Url**                                   | **Description**                                                 | **Request Sample**      |
| ---------- | ----------------------------------------- | --------------------------------------------------------------- | ----------------------- |
| GET        | /currencyUnits                            | Currency units list. Available even for not authenticated users |                         |
| GET        | /currencyUnits/{id}                       | Currency unit by id                                             |                         |
| GET        | /currencyUnits/{currencyName}?parammode=1 | Currency unit by currency name                                  |                         |
| GET        | /currencyUnits/{currencyId}?parammode=2   | Currency unit by currency id                                    |                         |
| POST       | /currencyUnit/{id}                        | Add currency unit. Admin tool                                   | [JSON](#currencySample) |
| DELETE     | /currencyUnit/{id}                        | Delete currency unit. Admin tool                                |                         |
| PUT        | /currencyUnits/{id}                       | Update currency unit. Admin tool                                | [JSON](#currencySample) |





## Valid JSON Samples

<a id="passwordChange">**Password change** (/users/{id}/password)</a>

```json
{
    "login": "login",
    "oldPassword": "oldPa$$W0rd",
    "newPassword": "nEwPa$Sw0r6"
}
```

<a id="currencySample">**Currency Unit sample** (/currencyUnit/{id})</a>

```json
{
    "currencyName": "USD",
    "currencyId": 432,
    "currencyScale": 1
}
```
