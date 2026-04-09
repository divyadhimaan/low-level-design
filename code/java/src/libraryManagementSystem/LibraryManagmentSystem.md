# Library Management System


## Design Patterns Used
| Pattern                 | Used Where                            | Purpose                                       |
|-------------------------|---------------------------------------|-----------------------------------------------|
| Singleton               | `LibraryManagementInterface` instance | Ensure single library instance                |
| Factory                 | `addBook()` creating `BookCopy`       | Encapsulate object creation                   |
| Composition/Aggregation | Library -> Racks, BookCopies, Users   | Manage ownership & modularity                 |
| Strategy                | `searchBooks()` by attribute          | Allow flexible search algorithms              |
| Template Method         | `borrowBook()` vs `borrowBookCopy()`  | Reuse common steps, vary selection            |


## UML Class Diagram

```mermaid

classDiagram
    LibraryManagementInterface --> LibraryManagementSystem : uses
    LibraryManagementSystem --> Rack : has 1..*
    LibraryManagementSystem --> BookCopy : manages
    LibraryManagementSystem --> LibraryUser : manages
    LibraryManagementSystem --> Book : manages
    LibraryManagementSystem --> LibraryBorrowedLog : keeps track of
    Rack --> BookCopy : contains 0..*
    LibraryUser --> BookCopy : borrows 0..*
    LibraryBorrowedLog --> BookCopy : logs
    LibraryBorrowedLog --> LibraryUser : logs
    BookCopy --> Book : is a copy of

    class LibraryManagementInterface {
        -LibraryManagementSystem libraryManagementSystem
        +addBook(bookId, title, authors, publishingCompanies, bookCopyIds)
        +removeBookCopy(copyId)
        +addUser(userId, name)
        +borrowBook(bookId, userId, borrowDate)
        +borrowBookCopy(bookCopyId, userId, borrowDate)
        +returnBookCopy(bookCopyId)
        +printBorrowedBooks(userId)
        +searchBooks(attribute, value)
        +displayRacks()
    }

    class LibraryManagementSystem {
        -String libraryId
        -Integer borrowLimitPerUser
        -Integer totalRacks
        -Map<Integer,Rack> racks
        -Map<String,BookCopy> copyIdToBookCopy
        -Map<String,LibraryUser> userIdToLibraryUser
        -Map<String,Book> bookIdToBook
        -List<LibraryBorrowedLog> borrowedLogs
        +addBook(bookId, title, authorList, publishingCompanyList, bookCopyIdList)
        +removeBookCopy(copyId)
        +addUser(userId, name)
        +borrowBook(bookId, userId, borrowDate)
        +borrowBookCopy(bookCopyId, userId, borrowDate)
        +returnBookCopy(copyId)
        +printBorrowedBooks(userId)
        +searchBooks(attribute, value)
        +displayRacks()
    }

    class Rack {
        -Integer rackNumber
        -List<BookCopy> bookCopyList
        -Set<String> bookIdsInRack
        +addBookCopy(bookCopy)
        +removeBookCopy(copyId)
        +checkAvailabilityForBook(bookCopy)
        +displayBookCopies()
    }

    class Book {
        -String bookId
        -String title
        -List<String> authors
        -List<String> publishingCompany
        -List<String> bookCopyIds
    }

    class BookCopy {
        -String copyId
        -Book book
        -Integer rackNumber
        -LibraryUser borrowedBy
        -Date dueDate
        +setBorrowedDetails(user, dueDate)
        +isAvailable()
        +getBookDetails()
    }

    class LibraryUser {
        -String userId
        -String name
        -Integer borrowLimit
        -List<BookCopy> borrowedBooks
        +addBorrowedBook(bookCopy)
    }

    class LibraryBorrowedLog {
        -String logId
        -BookCopy bookCopy
        -LibraryUser user
        -Date borrowDate
        -Date dueDate
        -Date returnDate
        +markAsReturned(returnDate)
    }

```


