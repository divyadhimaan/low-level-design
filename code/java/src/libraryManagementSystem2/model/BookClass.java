package libraryManagementSystem2.model;

import java.util.List;

public class BookClass {
    private String bookId;
    private String title;
    private List<String> authors;
    private String ISBN;
    private String publishingYear;


    public static class BookBuilder{
        private String bookId;
        private String title;
        private List<String> authors;
        private String ISBN;
        private String publishingYear;
    }
}
