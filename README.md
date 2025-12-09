# Three Rivers Bank - Online Banking Application

A demonstration online banking application for Three Rivers Bank featuring a Python frontend and Java Spring Boot backend.

## Features

- 👤 Simulated logged-in user (no authentication required)
- 💰 Multiple account overview (Checking, Savings, Credit Card)
- 📊 Detailed account information
- 📜 Transaction history
- 💸 Internal account transfers

## Technology Stack

### Backend
- **Framework**: Spring Boot 3.2.0
- **Database**: H2 (in-memory)
- **Build Tool**: Maven
- **Testing**: JUnit
- **Java Version**: 17

### Frontend
- **Framework**: Streamlit (Python)
- **Python Version**: 3.12+
- **HTTP Client**: Requests

## Project Structure

```
banking-python-demo/
├── backend/                    # Spring Boot backend
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/threeriversbank/banking/
│   │   │   │       ├── BankingApplication.java
│   │   │   │       ├── config/
│   │   │   │       ├── controller/
│   │   │   │       ├── model/
│   │   │   │       ├── repository/
│   │   │   │       └── service/
│   │   │   └── resources/
│   │   │       └── application.properties
│   │   └── test/
│   └── pom.xml
└── frontend/                   # Python Streamlit frontend
    ├── app.py
    └── requirements.txt
```

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6+
- Python 3.12+
- pip

### Backend Setup

1. Navigate to the backend directory:
   ```bash
   cd backend
   ```

2. Build the project:
   ```bash
   mvn clean install
   ```

3. Run the backend server:
   ```bash
   mvn spring-boot:run
   ```

   The backend will start on `http://localhost:8080`

4. Access H2 Console (optional):
   - URL: `http://localhost:8080/h2-console`
   - JDBC URL: `jdbc:h2:mem:bankingdb`
   - Username: `sa`
   - Password: (leave empty)

### Frontend Setup

1. Navigate to the frontend directory:
   ```bash
   cd frontend
   ```

2. Install dependencies:
   ```bash
   pip install -r requirements.txt
   ```

3. Run the Streamlit app:
   ```bash
   streamlit run app.py
   ```

   The frontend will open automatically in your browser at `http://localhost:8501`

## API Endpoints

### Accounts
- `GET /api/accounts` - Get all accounts
- `GET /api/accounts/{accountNumber}` - Get specific account details

### Transactions
- `GET /api/transactions/{accountNumber}` - Get transactions for an account

### Transfers
- `POST /api/transfer` - Transfer funds between accounts
  ```json
  {
    "fromAccount": "1001234567",
    "toAccount": "2001234567",
    "amount": "100.00",
    "description": "Transfer description"
  }
  ```

## Sample Data

The application comes pre-loaded with sample data for user "John Doe":

### Accounts
- **Checking Account**: 1001234567 - $5,250.00
- **Savings Account**: 2001234567 - $15,000.00
- **Credit Card**: 3001234567 - -$1,250.00

### Transactions
- Various sample transactions including deposits, withdrawals, and transfers

## Testing

### Backend Tests

Run the backend unit tests:
```bash
cd backend
mvn test
```

## Development Notes

- The application simulates a logged-in user (John Doe) without requiring authentication
- All data is stored in an H2 in-memory database and will be reset when the backend restarts
- CORS is enabled on the backend to allow frontend communication

## Future Enhancements

- User authentication and authorization
- External account transfers
- Bill payments
- Account statements and reports
- Mobile responsive design
- Email notifications

## License

This is a demonstration project for educational purposes.
