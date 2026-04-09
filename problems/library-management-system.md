# Library Management System

A library management system is an automation system used to manage a library and the different resource management required in it like cataloging of books, allowing check out and return of books, invoicing, user management, etc.

For this problem, we have to design a library management system that can do a few of the above functionalities.


## Requirements

### The Library
- The library will have one or more copies of multiple books
- The library will have multiple racks and each rack can contain at most one copy of any book.

### Book

Each book in the library has the following properties:

- **Book ID**: Unique identifier for the book.
- **Title**: Name of the book.
- **Authors**: List of authors.
- **Publisher**: Publishing company.

### **Book Copy**

There could be multiple copies of the same book. Each copy of a book has:

* **Copy ID**: Unique identifier for that specific copy.
* **Book Reference**: Points to the book it belongs to.
* **Rack Number**: The rack where it is stored.
* **Borrowed By**: The user who borrowed the book (if any).
* **Due Date**: Date when the book must be returned.

### **Rack**

Each rack can hold at most one book copy.

* **Rack Number**: Unique identifier for each rack.
* **Book Copy**: The copy currently placed in the rack.
* **is_empty()**: Method to check if the rack is vacant.

### **User**

Represents a library user.

* **User ID**: Unique ID for the user.
* **Name**: Name of the user.
* **Borrow Limit**: Can borrow up to 5 books.
* **Borrowed Books**: List of currently borrowed book copies.

---

## ⚙️ Functional Requirements

### 1. **Create Library**

Initialize a library with a specified number of racks.

### 2. **Add Book to Library**

Add a new book copy to the first available rack.

### 3. **Remove Book Copy**

Remove a book copy from the library and free the rack.

### 4. **Borrow Book by Book ID**

Allow a user to borrow the first available copy of a given book (lowest rack number preferred).

### 5. **Borrow Book by Copy ID**

Allow a user to borrow a specific copy of a book.

### 6. **Return Book Copy**

Allow a user to return a borrowed copy and place it in the first available rack.

### 7. **Print Borrowed Books**

Print all book copy IDs borrowed by a given user.

### 8. **Search Books**

Search for books based on one or more properties:

* Book ID
* Title
* Author
* Publisher

Returns all matching book copies with their details.

---


[Java Implementation](../code/java/src/libraryManagementSystem/LibraryManagementSystemSimulation.java) | [Design Explanation](../code/java/src/libraryManagementSystem/LibraryManagmentSystem.md)