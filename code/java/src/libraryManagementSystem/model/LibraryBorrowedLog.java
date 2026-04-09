package libraryManagementSystem.model;

import lombok.Getter;

import java.util.Date;

@Getter
public class LibraryBorrowedLog {
    private final String logId;
    private final BookCopy bookCopy;
    private final LibraryUser user;
    private final Date borrowDate;
    private final Date dueDate;
    private Date returnDate;

    public LibraryBorrowedLog(String logId, BookCopy bookCopy, LibraryUser user, Date borrowDate, Date dueDate) {
        this.logId = logId;
        this.bookCopy = bookCopy;
        this.user = user;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.returnDate = null;
    }

    public void markAsReturned(Date returnDate) {
        this.returnDate = returnDate;
    }
}
