package libraryManagementSystem.model;

import lombok.Getter;

import java.util.List;

@Getter
public class Book {
    private final String bookId;
    private final String title;
    private final List<String> authors;
    private final List<String> publishingCompany;
    private final List<String> bookCopyIds;

    public Book(String bookId, String title, List<String> authors, List<String> publishingCompany, List<String> bookCopyIds) {
        this.bookId = bookId;
        this.title = title;
        this.authors = authors;
        this.publishingCompany = publishingCompany;
        this.bookCopyIds = bookCopyIds;
    }
}
