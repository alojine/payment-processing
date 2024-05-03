# payment-processing
### A RESTful web service for payment processing build with SpringBoot

## Solution
This solution contains a tightly structured payment processing RESTful api, operates by making CRUD operations on
Payment model. The primary focus was crafting clean, maintainable code while leveraging design patterns to facilitate the
seamless integration of a new payment types:
* **PaymentService**: This component orchestrates operations related to Payments.
* **PaymentProcessor**: Utilizing the strategy pattern interface, this interface serves as a blueprint for implementing 
various **PaymentType** strategies.
* **PaymentRequestFilter**: Logs all api requests with **clientIp**, **methodType** and **requestURI**.

## Requirements
* [JDK 21](https://www.oracle.com/java/technologies/downloads/#java21)
* [Maven](https://maven.apache.org/download.cgi)

## Dependencies
* spring-boot-starter-jpa
* h2
* lambok
* exec-maven-plugin

## Endpoints
For testing purposes I added **postman collections** inside the repo.
* `GET /api/v1/payments/{id]` Retrieve a PaymentResponseDTO by providing it's Id.
* `GET /api/v1/payments` Retrieve filtered payment Id's with parameters **minAmount** and **maxAmount** which are optional.
* `PUT /api/v1/payments/{id]` Cancel payment by providing it's Id.
* `POST /api/v1/payments` Payment creation by providing PaymentRequestDTO as request body shown below.
```
{
    "paymentType": "TYPE1",
    "amount": 10,
    "currency": "EUR",
    "debtOrIban": "IE29 AIBK 9311 5212 3456 78",
    "creditOrIban": "IE29 AIBK 9311 5212 3456 78",
    "details": "Payment for car repair",
    "bicCode": ""
}
```

## Running and testing the project
* Build and Run the project
```
mvn install && mvn exec:java
```
* Run all tests
```
mvn test
```
