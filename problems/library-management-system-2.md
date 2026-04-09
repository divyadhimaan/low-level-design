# Library Management System 2

## Requirements
1. The library management system should allow librarians to manage books, members, and borrowing activities.
2. The system should support adding, updating, and removing books from the library catalog.
3. Each book should have details such as title, author, ISBN, publication year, and availability status.
4. The system should allow members to borrow and return books.
5. Each member should have details such as name, member ID, contact information, and borrowing history.
6. The system should enforce borrowing rules, such as a maximum number of books that can be borrowed at a time and loan duration.
7. The system should handle concurrent access to the library catalog and member records.
8. The system should be extensible to accommodate future enhancements and new features.


## Entities

- User
- Admin (Librarians)
- Library Catalog
- Borrowing Rules

User
- Attributes
  - Name
  - Member ID
  - Contact info (Mobile/email)
  - borrowing history (logs)
- Functions can do
  - Borrow book
  - return book

Admin
- Functions can do
  - Manage Books (add/update/remove book from catalog)
  - Manage Members
  - Borrowing activity (borrow allowed/rejected)

Catalog
- Collection of books -> (can have multiple books) 
  - Need Each book obj
  - Map of bookId -> location
  - Map (book title + author) -> availability? quantity?

- Book
  - title
  - author 
  - ISBN
  - publication year
  - availability status