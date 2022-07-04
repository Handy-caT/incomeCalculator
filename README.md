# Income Calculator REST API

[![GitHub commits](https://badgen.net/github/commits/Handy-caT/incomeCalculator)](https://github.com/Handy-caT/incomeCalculator/commit/) [![Docker](https://badgen.net/badge/icon/docker?icon=docker&label)](https://https://docker.com/) [![Maven](https://badgen.net/badge/icon/maven?icon=maven&label)](https://https://maven.apache.org/)

- **[Setup Guide](#setup-guide)**

- [**Using REST API**](#usage)
  
  - [**Authentication**](#authentication)
  
  - [**Users**](#users)
  
  - [**Currency Units**](#currencyUnits)
  
  - [**Ratios**](#ratios)
  
  - [**Cards**](#cards)

- [**Valid JSON Samples**](#samples)

- [**Unit Testing**](#testing)

- [**Scripts**](#scripts)

- [**Database Schema**](#dbschema)

## <a id="setup">Setup Guide</a>

First clone repository

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
  
  If you want to know more about **[run](./run)** script usage, read [here](#runScript) 

- Manually using **Docker**
  
  ```bash
  docker-compose up
  ```

The application will start running at  [http://localhost:8080/]()

## <a id="usage">Using REST API</a>

### <a id="authentication">Authentication</a>

You need to be authenticated to use APIs but some features is available for not authenticated users. Let's start from authentication

| **Method** | **Url**   | **Description**            | **Request Sample**   |
| ---------- | --------- | -------------------------- | -------------------- |
| POST       | /auth     | Authentication             | [JSON](#authRequest) |
| POST       | /register | Registration for new users | [JSON](#authRequest) |

After registration/authentication you will receive a token for further usage of API.

Token is used as a bearer token, so you need to add **Authentication header** to all requests, you will get new token in response headers. So your token is updated after all request. Token also has **1 day** expiration limit.

### <a id="users">Users</a>

| **Method** | **Url**               | **Description**                                          | **Request Sample**      |
| ---------- | --------------------- | -------------------------------------------------------- | ----------------------- |
| GET        | /users                | All users list. Only for admin users                     |                         |
| GET        | /users/me             | Get authenticated user info                              |                         |
| GET        | /users/{id}           | Get user by id, if user is same to authenticated user    |                         |
| DELETE     | /users/{id}           | Delete user by id, if user is same to authenticated user |                         |
| PATCH      | /users/{id}/password  | Password update                                          | [JSON](#passwordChange) |
| PATCH      | /users/{id}/makeAdmin | Only for admins                                          |                         |
| PATCH      | /users/{id}/makeUser  | Only for admins                                          |                         |

### <a id="currencyUnits">Currency Units</a>

| **Method** | **Url**                                   | **Description**                                                 | **Request Sample**      |
| ---------- |:----------------------------------------- | --------------------------------------------------------------- | ----------------------- |
| GET        | /currencyUnits                            | Currency units list. Available even for not authenticated users |                         |
| GET        | /currencyUnits/{id}                       | Currency unit by id                                             |                         |
| GET        | /currencyUnits/{currencyName}?parammode=1 | Currency unit by currency name                                  |                         |
| GET        | /currencyUnits/{currencyId}?parammode=2   | Currency unit by currency id                                    |                         |
| POST       | /currencyUnit/{id}                        | Add currency unit. Admin tool                                   | [JSON](#currencySample) |
| DELETE     | /currencyUnit/{id}                        | Delete currency unit. Admin tool                                |                         |
| PUT        | /currencyUnits/{id}                       | Update currency unit. Admin tool                                | [JSON](#currencySample) |

### <a id="ratios">Ratios</a>

| **Method** | **Url**                            | **Description**                        | **Request Sample**   |
| ---------- | ---------------------------------- | -------------------------------------- | -------------------- |
| GET        | /ratios                            | Get todays ratios                      |                      |
| GET        | /ratios?ondate=21_05_2022          | Get ratio on date in format dd_MM_yyyy |                      |
| GET        | /ratios/{id}                       | Get ratio by id                        |                      |
| GET        | /ratios/{currencyName}?parammode=1 | Get todays ratio for currency          |                      |
| DELETE     | /ratios/{id}                       | Delete ratio. Admin tool               |                      |
| DELETE     | /ratios?ondate=21_05_2022          | Delete ratio on date. Admin tool       |                      |
| PUT        | /ratios/{id}                       | Update ratio. Admin tool               | [JSON](#ratioSample) |
| POST       | /ratios/{id}                       | Add ratio. Admin tool                  | [JSON](#ratioSample) |

### <a id="cards">Cards</a>

| **Method** | **Url**                                 | **Description**                                            | **Request Simple**  |
| ---------- | --------------------------------------- | ---------------------------------------------------------- | ------------------- |
| GET        | /cards                                  | Get all your cards if user, or all existing cards if admin |                     |
| GET        | /cards/{id}                             | Get card by id if card belongs to you                      |                     |
| GET        | /cards/{id}/transactions                | Get all transactions for card, if card belong to you       |                     |
| POST       | /cards                                  | Create new card                                            | [JSON](#cardSample) |
| POST       | /cards/{id}/transactions                | Create new transaction to card                             |                     |
| DELETE     | /cards/{id}/transaction/{transactionId} | Delete  transaction for card by id and transaction id      |                     |
| GET        | /cards/{id}/transaction/{transactionId} | Get transaction for card by id and transaction id          |                     |
| PATCH      | /cards/{id}                             | Rename card if it belongs to you                           | [JSON](#cardPatch)  |
| DELETE     | /cards/{id}                             | Delete card if it belongs to you                           |                     |

## <a id="samples">Valid JSON Samples</a>

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

<a id="ratioSample">**Ratio sample** (/ratios/{id})</a>

```json
{
    "currencyName": "USD",
    "ratio": 2.3456,
    "dateString": "21_05_2022"
}
```

<a id="cardSample">**Card sample (/cards)**</a>

```json
{
    "cardName": "card",
    "currencyName": "USD"
}
```

<a id="cardPatch">**Card patch** (/cards)</a>

```json
{
    "cardName": "newCard"
}
```

<a id="authRequest">**Auth request** (/auth,/register)</a>

```json
{
    "login": "zuka",
    "password": "pa$$W0rd"
}
```

## <a id = "testing">Unit testing</a>

1. **Card controller**
   
   Tests: **32**,
   
   Controller coverage: **100%**

2. **Currency Unit controller**
   
   Tests: **13**,
   
   Controller coverage: **100%**

3. **Ratio controller**
   
   Tests: **12**,
   
   Controller coverage: **85%**

4. **Auth controller**
   
   Tests: **3**,
   
   Controller coverage: **95%**

## <a id="scripts">Scripts</a>

### <a id="runScript">Run</a>

**[run](./run)** script is made to easier run application

| **Flag** | **Description**                        |
| -------- | -------------------------------------- |
| d        | Run application via **docker-compose** |
| m        | Run application locally via **Maven**  |

### Build

**[build](./build)** script is made to build application

| **Flag** | **Description**                     |
| -------- | ----------------------------------- |
| d        | Pull docker images  from docker hub |
| C        | Compile **Maven** project           |
| A        | Build all docker images locally     |
| g        | Build **Gateway** docker image      |
| c        | Build **Card API** docker image     |
| u        | Build **User API** docker image     |
| p        | Push created images to docker hub   |

## <a id="dbschema">Database tables schema</a>

![schema](/incomeCalculatorDBSchema.png)
