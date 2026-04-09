package libraryManagementSystem.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
public class BookCopy extends Book {
    private final String copyId;
    private final Book book;
    @Setter
    private Integer rackNumber;
    private LibraryUser borrowedBy;
    private Date dueDate;

    public BookCopy(String copyId,Book book, Integer rackNumber) {
        super(book.getBookId(), book.getTitle(), book.getAuthors(), book.getPublishingCompany(), book.getBookCopyIds());
        this.copyId = copyId;
        this.book = book;
        this.rackNumber = rackNumber;
        this.borrowedBy = null;
        this.dueDate = null;
    }
    public Book getBookDetails() {
        return this.book;
    }

    public void setBorrowedDetails(LibraryUser user, Date dueDate) {
        this.borrowedBy = user;
        this.dueDate = dueDate;
    }

    public boolean isAvailable() {
        return this.borrowedBy == null;
    }

}
