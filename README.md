# Cinema Room REST API

A RESTful web service that simulates a backend for a cinema ticket booking system. This project was built to demonstrate fundamental backend concepts such as endpoint mapping, JSON serialization/deserialization, state management, and exception handling using **Java** and **Spring Boot**.

## Technologies Used

* **Java 17+**
* **Spring Boot** (Spring Web)
* **Jackson** (JSON processing)
* **Collections API** (In-memory state management using `HashMap`)

## Features

* Fetch the layout and prices of all seats in the cinema (9x9 grid).
* Purchase tickets with boundary and availability validation.
* Generate unique `UUID` tokens for secure ticket returns.
* View restricted cinema statistics (income, available seats) protected by a query parameter password.
* Optimized data retrieval using O(1) complexity structures (`Map<UUID, Seat>`).

---

# API Endpoints Documentation

## 1. Get all seats

Retrieves the layout of the cinema, including total rows, total columns, and a list of all seats.

**URL:** `GET /seats`

### Success Response (200 OK)

```json
{
  "total_rows": 9,
  "total_columns": 9,
  "seats": [
    {
      "row": 1,
      "column": 1,
      "price": 10
    }
  ]
}
```

---

## 2. Purchase a Ticket

Reserves a specific seat in the cinema and generates a unique return token.

**URL:** `POST /purchase`

### Request Body

```json
{
  "row": 3,
  "column": 4
}
```

### Success Response (200 OK)

```json
{
  "token": "e739267a-7031-4eed-a49c-65d8ac11f556",
  "ticket": {
    "row": 3,
    "column": 4,
    "price": 10
  }
}
```

### Error Responses (400 Bad Request)

Invalid coordinates:

```json
{
  "error": "The number of a row or a column is out of bounds!"
}
```

Seat already purchased:

```json
{
  "error": "The ticket has been already purchased!"
}
```

---

## 3. Return a Ticket

Refunds a ticket using the unique token generated during purchase and makes the seat available again.

**URL:** `POST /return`

### Request Body

```json
{
  "token": "e739267a-7031-4eed-a49c-65d8ac11f556"
}
```

### Success Response (200 OK)

```json
{
  "ticket": {
    "row": 3,
    "column": 4,
    "price": 10
  }
}
```

### Error Response (400 Bad Request)

Wrong token:

```json
{
  "error": "Wrong token!"
}
```

---

## 4. Cinema Statistics

Retrieves current business metrics (income, available seats, purchased tickets). This endpoint is protected.

**URL:** `GET /stats?password=super_secret`

### Success Response (200 OK)

```json
{
  "current_income": 30,
  "number_of_available_seats": 78,
  "number_of_purchased_tickets": 3
}
```

### Error Response (401 Unauthorized)

Wrong or missing password:

```json
{
  "error": "Wrong password!"
}
```

---

# How to Run Locally

## Clone the repository

```bash
git clone https://github.com/JakubSwid/java-spring-boot-cinema.git
```

## Navigate to the project directory

```bash
cd java-spring-boot-cinema
```

## Run the application using the Maven wrapper

```bash
./mvnw spring-boot:run
```

The API will be available at:

```text
http://localhost:28852
```
