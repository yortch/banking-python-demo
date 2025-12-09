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
│   │   │   │       ├── dto/
│   │   │   │       ├── exception/
│   │   │   │       ├── model/
│   │   │   │       ├── repository/
│   │   │   │       └── service/
│   │   │   │           ├── AccountService.java
│   │   │   │           ├── TransactionService.java
│   │   │   │           └── TransferService.java
│   │   │   └── resources/
│   │   │       └── application.properties
│   │   └── test/
│   │       └── java/
│   │           └── com/threeriversbank/banking/
│   │               ├── controller/
│   │               └── service/
│   │                   ├── AccountServiceTest.java
│   │                   ├── TransactionServiceTest.java
│   │                   └── TransferServiceTest.java
│   └── pom.xml
└── frontend/                   # Python Streamlit frontend
    ├── app.py                  # Main application entry point
    ├── requirements.txt
    ├── config/                 # Configuration module
    │   ├── __init__.py
    │   └── settings.py         # Application settings and constants
    ├── services/               # Service layer module
    │   ├── __init__.py
    │   └── api_client.py       # API communication logic
    ├── components/             # UI components module
    │   ├── __init__.py
    │   ├── styles.py           # CSS styling definitions
    │   └── header.py           # Header and navigation components
    ├── views/                  # View modules (renamed from pages to avoid Streamlit auto-navigation)
    │   ├── __init__.py
    │   ├── dashboard.py        # Dashboard view
    │   ├── account_details.py  # Account details view
    │   ├── transactions.py     # Transaction history view
    │   └── transfer.py         # Transfer funds view
    └── utils/                  # Utility functions module
        ├── __init__.py
        └── formatters.py       # Data formatting utilities
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

## Architecture

### Backend Service Layer Design

The backend follows a **domain-driven design** with modular services organized by business domain:

#### **AccountService**
- Manages account-related operations
- Handles account retrieval (all accounts, single account)
- Manages account balance updates
- Ensures account business rules are enforced

#### **TransactionService**
- Manages transaction-related operations
- Records new transactions
- Retrieves transaction history
- Maintains transaction audit trail

#### **TransferService**
- Orchestrates fund transfer operations
- Coordinates between `AccountService` and `TransactionService`
- Ensures transactional integrity (ACID properties)
- Validates transfer business rules:
  - Positive amount validation
  - Sufficient funds verification
  - Account existence validation

This modular architecture provides:
- ✅ **Separation of Concerns**: Each service has a single, well-defined responsibility
- ✅ **Testability**: Services can be independently unit tested with mocked dependencies
- ✅ **Maintainability**: Changes to one domain don't affect others
- ✅ **Scalability**: Services can be easily extended or modified
- ✅ **Reusability**: Services can be composed in different ways

### Frontend Modular Design

The frontend follows a **modular architecture** organized by functionality and domain:

#### **Configuration Layer** (`config/`)
- **settings.py**: Centralized application configuration
  - API endpoints
  - User settings
  - Application constants

#### **Service Layer** (`services/`)
- **api_client.py**: Backend API communication
  - Account retrieval
  - Transaction queries
  - Fund transfers
  - Error handling and response parsing

#### **Component Layer** (`components/`)
- **styles.py**: CSS styling definitions and application logic
- **header.py**: Reusable UI components (header, user info, sidebar footer)

#### **View Layer** (`views/`)
- **dashboard.py**: Account overview and total balance
- **account_details.py**: Detailed account information
- **transactions.py**: Transaction history display
- **transfer.py**: Inter-account fund transfers
- Note: Renamed from `pages/` to avoid Streamlit's automatic page navigation feature

#### **Utility Layer** (`utils/`)
- **formatters.py**: Data transformation utilities
  - Currency formatting
  - Date/time formatting
  - Transaction data processing

#### **Application Entry Point** (`app.py`)
- Page configuration and initialization
- Navigation routing
- Component orchestration

This modular frontend architecture provides:
- ✅ **Separation of Concerns**: UI logic separated from business logic and API communication
- ✅ **Reusability**: Components and utilities can be shared across pages
- ✅ **Maintainability**: Easy to locate and modify specific functionality
- ✅ **Testability**: Each module can be tested independently
- ✅ **Scalability**: New features can be added as new modules without affecting existing code
- ✅ **Consistency**: Mirrors the modular pattern of the backend architecture

## Development Notes

- The application simulates a logged-in user (John Doe) without requiring authentication
- All data is stored in an H2 in-memory database and will be reset when the backend restarts
- CORS is enabled on the backend to allow frontend communication
- The service layer uses Spring's `@Transactional` annotation to ensure data consistency

## Future Enhancements

- User authentication and authorization
- External account transfers
- Bill payments
- Account statements and reports
- Mobile responsive design
- Email notifications

## License

This is a demonstration project for educational purposes.
