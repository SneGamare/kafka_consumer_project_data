# Kafka Avro Example

This project demonstrates how to use Apache Kafka with Avro serialization in a Spring Boot application.

## Prerequisites

- Java 17 or higher
- Maven
- Docker and Docker Compose
- Apache Kafka
- Confluent Schema Registry

## Project Structure

```
kafka-avro-full-project/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── example/
│   │   │           └── kafkaavro/
│   │   │               ├── KafkaAvroApplication.java
│   │   │               ├── config/
│   │   │               │   └── KafkaConfig.java
│   │   │               ├── controller/
│   │   │               │   └── KafkaController.java
│   │   │               ├── model/
│   │   │               │   └── PlutusFinacleData.java
│   │   │               └── service/
│   │   │                   ├── ConsumerService.java
│   │   │                   └── ProducerService.java
│   │   └── resources/
│   │       ├── application.properties
│   │       └── plutus-finacle-data.avsc
│   └── test/
│       └── java/
│           └── com/
│               └── example/
│                   └── kafkaavro/
│                       └── KafkaAvroApplicationTests.java
├── docker-compose.yml
├── pom.xml
└── README.md
```

## Getting Started

1. Start the required services using Docker Compose:
   ```bash
   docker-compose up -d
   ```

2. Build the project:
   ```bash
   mvn clean package
   ```

3. Run the application:
   ```bash
   java -jar target/kafka-avro-1.0.0.jar
   ```

## API Endpoints

### Producer Endpoint
- **URL**: `/api/messages`
- **Method**: `POST`
- **Content-Type**: `application/json`
- **Description**: Publishes a message to Kafka topic

### Consumer Endpoint
- **URL**: `/api/messages`
- **Method**: `GET`
- **Description**: Retrieves all received messages

### Clear Messages Endpoint
- **URL**: `/api/messages`
- **Method**: `DELETE`
- **Description**: Clears all received messages

## Testing with cURL

### 1. Send a Message (Producer)
```bash
curl -X POST http://localhost:8080/api/messages \
-H "Content-Type: application/json" \
-d '{
    "foracid": "123456",
    "acctName": "Test Account",
    "tranId": "T123",
    "tranAmt": 100.00,
    "tranDate": "2025-04-04",
    "tranType": "CREDIT",
    "tranParticular": "Test Transaction"
}'
```

### 2. Get Messages (Consumer)
```bash
curl -X GET http://localhost:8080/api/messages
```

### 3. Clear Messages
```bash
curl -X DELETE http://localhost:8080/api/messages
```

### 4. Send Multiple Messages
```bash
# Message 1
curl -X POST http://localhost:8080/api/messages \
-H "Content-Type: application/json" \
-d '{
    "foracid": "123457",
    "acctName": "Test Account 2",
    "tranId": "T124",
    "tranAmt": 200.00,
    "tranDate": "2025-04-04",
    "tranType": "DEBIT",
    "tranParticular": "Test Transaction 2"
}'

# Message 2
curl -X POST http://localhost:8080/api/messages \
-H "Content-Type: application/json" \
-d '{
    "foracid": "123458",
    "acctName": "Test Account 3",
    "tranId": "T125",
    "tranAmt": 300.00,
    "tranDate": "2025-04-04",
    "tranType": "CREDIT",
    "tranParticular": "Test Transaction 3"
}'
```

### 5. PowerShell Commands (Windows)
```powershell
# Send a message
Invoke-WebRequest -Uri 'http://localhost:8080/api/messages' -Method Post -ContentType 'application/json' -Body '{
    "foracid": "123459",
    "acctName": "Test Account 4",
    "tranId": "T126",
    "tranAmt": 400.00,
    "tranDate": "2025-04-04",
    "tranType": "DEBIT",
    "tranParticular": "Test Transaction 4"
}'

# Get messages
Invoke-WebRequest -Uri 'http://localhost:8080/api/messages' -Method Get

# Clear messages
Invoke-WebRequest -Uri 'http://localhost:8080/api/messages' -Method Delete
```

## Expected Responses

### Successful Message Publication
```json
{
    "message": "Message published successfully"
}
```

### Retrieved Messages
```json
[
    {
        "foracid": "123456",
        "acctName": "Test Account",
        "tranId": "T123",
        "tranAmt": 100.00,
        "tranDate": "2025-04-04",
        "tranType": "CREDIT",
        "tranParticular": "Test Transaction"
    }
]
```

### Successful Message Clear
```json
{
    "message": "Messages cleared successfully"
}
```

## Error Handling

The application includes error handling for:
- Invalid message format
- Kafka connection issues
- Schema Registry errors
- Serialization/Deserialization errors

## Contributing

1. Fork the repository
2. Create your feature branch
3. Commit your changes
4. Push to the branch
5. Create a new Pull Request
