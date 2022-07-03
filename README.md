# Income Calculator REST API

- **[Setup Guide](#setup-guide)**

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
  
  If you want to know more about **[run](./run)** script usage, read here 

- Manually using **Docker**
  
  ```bash
  docker-compose up
  ```

The application will start running at  [http://localhost:8080/]()

## Using REST API

### Authentication

You need to be authenticated to use APIs but some features is available for not authenticated users. Let's start from authentication

| **Method** | **Url**   | **Description**            | **Request Sample** |
| ---------- | --------- | -------------------------- | ------------------ |
| POST       | /auth     | Authentication             |                    |
| POST       | /register | Registration for new users |                    |

After registration/authentication you will receive a token for further usage of API.

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
| ---------- |:----------------------------------------- | --------------------------------------------------------------- | ----------------------- |
| GET        | /currencyUnits                            | Currency units list. Available even for not authenticated users |                         |
| GET        | /currencyUnits/{id}                       | Currency unit by id                                             |                         |
| GET        | /currencyUnits/{currencyName}?parammode=1 | Currency unit by currency name                                  |                         |
| GET        | /currencyUnits/{currencyId}?parammode=2   | Currency unit by currency id                                    |                         |
| POST       | /currencyUnit/{id}                        | Add currency unit. Admin tool                                   | [JSON](#currencySample) |
| DELETE     | /currencyUnit/{id}                        | Delete currency unit. Admin tool                                |                         |
| PUT        | /currencyUnits/{id}                       | Update currency unit. Admin tool                                | [JSON](#currencySample) |

### Ratios

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

<a id="ratioSample">**Ratio sample** (/ratios/{id})</a>

```json
{
    "currencyName": "USD",
    "ratio": 2.3456,
    "dateString": "21_05_2022"
}
```



## Unit testing

### Card API

1. **Card controller**

2. **Currency Unit controller**

3. **Ratio controller**

4. **Auth controller**

5. **User controller**