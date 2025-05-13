# Price Survey API

A Spring Boot REST API for managing price surveys of lubricant products across different stores.

## Features

- **User Management**: Admin (Garvey) can create and manage user accounts with role-based access
- **Store Management**: Add and manage stores with location data for mapping
- **Product Management**: Define and manage products with different volume ranges
- **Price Entry**: Users can input and track price and quantity data
- **Dashboard**: Data visualization with tables, graphs, and maps
- **Data Filtering**: Filter data by store, price, product, and location
- **Activity Tracking**: Track user login activities
- **JWT Authentication**: Secure API with token-based authentication

## Technical Stack

- **Framework**: Spring Boot 3.2.0
- **Language**: Kotlin
- **Database**: PostgreSQL
- **Migration**: Flyway
- **Security**: Spring Security + JWT
- **Build Tool**: Gradle with Kotlin DSL

## Getting Started

### Prerequisites

- Java 17 or higher
- PostgreSQL 12 or higher
- Gradle 7.x or higher

### Database Setup

1. Create a PostgreSQL database:
```sql
CREATE DATABASE pricesurveydb;
CREATE USER pricesurvey WITH PASSWORD 'password';
GRANT ALL PRIVILEGES ON DATABASE pricesurveydb TO pricesurvey;
```

2. Update `application.yml` with your database credentials if different from defaults.

### Running the Application

```bash
# Clone the repository
git clone <repository-url>
cd price-survey-app

# Run the application
./gradlew bootRun
```

The application will start on `http://localhost:8080`.

### Default Admin User

- **Email**: garvey@pricesurvey.com
- **Password**: admin123 (change this after first login)

## API Endpoints

### Authentication
- `POST /api/auth/login` - Login
- `GET /api/auth/me` - Get current user info

### Admin Endpoints (Admin only)
- `POST /api/admin/users` - Create user
- `GET /api/admin/users` - Get all users
- `PUT /api/admin/users/{id}` - Update user
- `DELETE /api/admin/users/{id}` - Delete user

### Stores
- `GET /api/stores` - Get all active stores
- `GET /api/stores/map` - Get stores for map visualization
- `POST /api/stores` - Create store (Admin only)
- `PUT /api/stores/{id}` - Update store (Admin only)
- `DELETE /api/stores/{id}` - Delete store (Admin only)

### Products
- `GET /api/products` - Get all active products
- `GET /api/products/categories` - Get product categories
- `GET /api/products/volumes` - Get available volumes
- `POST /api/products` - Create product (Admin only)
- `PUT /api/products/{id}` - Update product (Admin only)
- `DELETE /api/products/{id}` - Delete product (Admin only)

### Price Entries
- `POST /api/price-entries` - Create price entry
- `GET /api/price-entries` - Get price entries with filters
- `GET /api/price-entries/my-entries` - Get current user's entries

### Dashboard
- `GET /api/dashboard` - Get dashboard data with optional filters

## Configuration

Key configuration options in `application.yml`:

- Database connection settings
- JWT secret and expiration time
- Admin user credentials
- Server port

## Environment Variables

You can override default values using environment variables:
- `DB_USERNAME`: Database username
- `DB_PASSWORD`: Database password
- `JWT_SECRET`: JWT signing secret
- `JWT_EXPIRATION`: JWT expiration time in milliseconds
- `ADMIN_EMAIL`: Admin user email
- `ADMIN_PASSWORD`: Admin user password

## Development

### Running Tests

```bash
./gradlew test
```

### Building for Production

```bash
./gradlew build
```

## License

This project is proprietary and confidential.