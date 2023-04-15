# Spring Boot Application with Custom Hibernate Configuration, H2 Database, and Swagger

This is a Spring Boot application that uses a custom Hibernate configuration to connect to an H2 in-memory database and perform CRUD (Create, Read, Update, Delete) operations. It also includes Swagger documentation to easily view and test the API endpoints.

## Getting Started

To get started with this project, you'll need to clone the repository to your local machine and open it in your preferred IDE.

### Prerequisites

- JDK 8 or higher
- Spring Boot 2.5.1 or higher

### Installing
1. Clone the repository to your local machine:
git clone https://github.com/your-username/your-repository.git

2. Open the project in your preferred IDE.

3. Run the application using the following command:
./mvnw spring-boot:run

4. Access the Swagger documentation by navigating to `http://localhost:8080/swagger-ui` in your web browser.

## Custom Hibernate Configuration

This project uses a custom Hibernate configuration to connect to the H2 in-memory database and perform CRUD operations. The custom configuration is defined in the `HibernateConfig.java` file, which is located in the `com.example.demo.config` package.

In this configuration, we define the following Hibernate properties:

- `hibernate.hbm2ddl.auto`: Automatically create, update, or validate the schema based on the Hibernate mappings.

We also define the `DataSource` bean that provides the database connection properties.

## H2 Database Configuration

This project uses an H2 in-memory database with a `schema.sql` and `data.sql` file to create and populate the database. The `schema.sql` file is used to create the database schema, while the `data.sql` file is used to insert data into the database.

In this example, we're using the H2 console to interact with the database. You can access the console by navigating to `http://localhost:8080/h2-console` in your web browser. Make sure to enter the following settings:

- `JDBC URL`: `jdbc:h2:mem:testdb`
- `Username`: `sa`
- `Password`: ``

Once you're logged in, you can execute SQL queries and view the contents of the database.

## Swagger Documentation

This project includes Swagger documentation that describes the available endpoints and allows you to test them directly from the browser. You can access the Swagger documentation by navigating to `http://localhost:8080/swagger-ui.html` in your web browser.

The following endpoints are available:

- `GET /api/users`: Get a list of all employees.
- `POST /api/add`: Create a new employee.
- `DELETE /api/user/{id}`: Delete an employee by ID.

## Contributing

If you find any issues or have suggestions for improving the project, feel free to submit a pull request or open an issue.

## License

This project is licensed under the MIT License - see the `LICENSE` file for details.
