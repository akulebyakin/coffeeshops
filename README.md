# Coffeeshops

EHU student Java Spring project

## Project Overview

This project is a web application for managing a list of coffee shops. Users can register, log in, view the list of coffee shops, and log out. Admin users have additional privileges to edit or delete any coffee shop by its ID.

## Features

- **User Registration**: Users can register by providing their name, email, login, and password.
- **User Login**: Registered users can log in to the system.
- **View Coffee Shops**: Users can view a list of coffee shops with their name, address, contact data, and rating.
- **Admin Privileges**: Admin users can edit or delete any coffee shop by its ID.
- **Error Handling**: The application displays error messages for various scenarios, such as invalid login credentials or registration errors.
- **Validation**: Registration fields are validated to ensure data integrity.
- **Security**: Passwords are stored securely using the BCrypt hashing algorithm.
- **Database Integration**: The application uses PostgreSQL as the database backend. Connection pooling is implemented.
