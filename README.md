# Event-Ticket-Management-App-Rancard

A robust event ticketing backend system built with **Spring Boot**, supporting:

- User authentication with JWT
- Event creation and management
- Ticket generation and purchase
- H2 in-memory database
- RESTful API endpoints
- Spring Security integration

---

## Features

| Feature      | Description                                     |
| ------------ | ----------------------------------------------- |
| **Auth**     | Register & Login with JWT                       |
| **Events**   | Create, update, delete, view all                |
| **Tickets**  | Generate tickets, purchase, scan attendance     |
| **Security** | JWT-based authentication and authorization      |
| **Database** | H2 in-memory database with JPA                  |
| **API**      | RESTful endpoints with proper HTTP status codes |

---

## Technologies

- Java 17
- Spring Boot 3.5.3
- Spring Security
- JWT Authentication (JJWT 0.12.5)
- H2 Database
- JPA/Hibernate
- Maven
- Lombok

---

## API Endpoints

### Authentication Endpoints

#### Register User

```http
POST /auth/register
Content-Type: application/json

{
    "username": "john_doe",
    "password": "securePassword123"
}
```

**Response:**

```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqb2huX2RvZSI..."
}
```

#### Login User

```http
POST /auth/login
Content-Type: application/json

{
    "username": "john_doe",
    "password": "securePassword123"
}
```

**Response:**

```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqb2huX2RvZSI..."
}
```

---

### Event Management Endpoints

#### Create Event

```http
POST /events
Content-Type: application/json
Authorization: Bearer <JWT_TOKEN>

{
    "title": "Tech Conference 2025",
    "description": "Annual technology conference showcasing latest innovations",
    "startDate": "2025-09-15T10:00:00",
    "endDate": "2025-09-15T18:00:00"
}
```

#### Get All Events

```http
GET /events
```

**Response:**

```json
[
  {
    "id": 1,
    "title": "Tech Conference 2025",
    "description": "Annual technology conference",
    "startDate": "2025-09-15T10:00:00",
    "endDate": "2025-09-15T18:00:00",
    "ownerUsername": "john_doe"
  }
]
```

#### Update Event

```http
PUT /events/{id}
Content-Type: application/json
Authorization: Bearer <JWT_TOKEN>

{
    "title": "Updated Tech Conference 2025",
    "description": "Updated description",
    "startDate": "2025-09-15T09:00:00",
    "endDate": "2025-09-15T19:00:00"
}
```

#### Delete Event

```http
DELETE /events/{id}
Authorization: Bearer <JWT_TOKEN>
```

---

### Ticket Management Endpoints

#### Purchase Ticket

```http
POST /tickets/purchase
Content-Type: application/json
Authorization: Bearer <JWT_TOKEN>

{
    "code": "TICKET-001",
    "eventId": 1
}
```

#### Get Tickets for Event

```http
GET /tickets/event/{eventId}
Authorization: Bearer <JWT_TOKEN>
```

#### Scan Ticket

```http
POST /tickets/{ticketId}/scan
Authorization: Bearer <JWT_TOKEN>
```

**Response:**

```json
{
  "id": 1,
  "code": "TICKET-001",
  "scanned": true,
  "purchaseDate": "2025-08-03T16:30:00",
  "purchaserUsername": "john_doe"
}
```

---

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6+

### Running the Application

1. **Clone the repository:**

```bash
git clone https://github.com/Weber-droid/Event-Ticket-Management-App-Rancard.git

cd Event-Ticket-Management-App-Rancard/app
```

2. **Build the application:**

```bash
./mvnw clean install
```

3. **Run the application:**

```bash
./mvnw spring-boot:run
```

The application will start on `http://localhost:8080`

### Database Console

Access the H2 database console at: `http://localhost:8080/h2-console`

**Connection Settings:**

- JDBC URL: `jdbc:h2:mem:eventdb`
- Username: `sa`
- Password: (leave empty)

---

## Authentication Flow

1. **Register** a new user via `/auth/register`
2. **Login** to get JWT token via `/auth/login`
3. **Include token** in Authorization header for protected endpoints:
   ```
   Authorization: Bearer <your-jwt-token>
   ```

---

## Error Responses

The API returns appropriate HTTP status codes:

- `200` - Success
- `201` - Created
- `400` - Bad Request
- `401` - Unauthorized
- `403` - Forbidden
- `404` - Not Found
- `500` - Internal Server Error

**Error Response Format:**

```json
{
  "error": "Error message description"
}
```

---

## User Roles

- **ORGANIZER**: Can create, update, and delete events
- **CUSTOMER**: Can purchase tickets and view events

---

## Security Features

- JWT-based stateless authentication
- Password encryption with BCrypt

---

## Development

### Project Structure

```
src/
├── main/java/com/rancard/eventmanagement/app/
│   ├── controller/     # REST controllers
│   ├── config/         # Config Files
│   ├── service/        # Business logic
│   ├── repository/     # Data access
│   ├── model/          # Entity classes
│   ├── dto/            # Data transfer objects
│   └── security/       # Security configuration
└── resources/
    └── application.properties
```

### Testing the API

You can test the API using:

- **Postman** or **Insomnia**
- **curl** commands
- **Browser** (for GET endpoints)

---

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

---

## License

- Emmanuel Tetteh Totimeh
- Rancard NSS Assignment
