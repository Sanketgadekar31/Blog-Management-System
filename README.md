# Blog-Management-System
Java-based console application for Blog Management

## Overview

The Blog Management System is a Java-based console application designed to manage blogs and comments. It allows users to perform various operations such as adding, viewing, updating, and deleting blogs and comments. The application interacts with a MySQL database to store and retrieve data.

## Features

- Add a new blog
- View blog details
- Update blog details
- Delete a blog
- Add a comment to a blog
- View comments
- Update comments
- Delete a comment

## Prerequisites

- Java Development Kit (JDK) 8 or higher
- MySQL Server
- MySQL Connector/J (JDBC driver)
- Maven (optional, if you want to build with Maven)

## Setup

1. **Clone the Repository**

   ```bash
   git clone https://github.com/yourusername/blog-management-system.git
   cd blog-management-system


2. **Setup MySQL Database**

    Create a database named blog_db in MySQL.

    Create the necessary tables for blogs and comments using the following SQL scripts:
    1. blog Table Structure:
    ```bash
    CREATE TABLE blog (
    blog_id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    creation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

    2. comment Table Structure:
    ```bash
    CREATE TABLE comment (
    comment_id INT AUTO_INCREMENT PRIMARY KEY,
    content VARCHAR(50) NOT NULL,
    blog_id INT,
    FOREIGN KEY (blog_id) REFERENCES blog(blog_id)
    );

## Usage

1. Start the Application

2. Run the application using the instructions provided above.
3. Choose an Option:
The console will display a menu with the following options:

- Add a new Blog
- View blog details
- Update blog details
- Delete a blog
- Add comment on blog
- View comment
- Update comment
- Delete a comment
- EXIT

Enter the number corresponding to the option you want to choose and follow the prompts.