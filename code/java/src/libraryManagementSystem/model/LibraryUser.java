package libraryManagementSystem.model;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class LibraryUser {
    private final String userId;
    private final String name;
    private final Integer borrowLimit;
    private final List<BookCopy> borrowedBooks;

    public LibraryUser(String userId, String name, Integer borrowLimit){
        this.userId = userId;
        this.name = name;
        this.borrowLimit = borrowLimit;
        this.borrowedBooks = new ArrayList<>();
    }
    public void addBorrowedBook(BookCopy bookCopy) {
        if (borrowedBooks.size() < borrowLimit) {
            borrowedBooks.add(bookCopy);
        } else {
            System.out.println("[ERROR]   | User " + name + " has reached the borrow limit.");
        }
    }
}
