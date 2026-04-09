package libraryManagementSystem;

import core.LibraryManagementSystem;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class LibraryManagementInterface {

    private final LibraryManagementSystem libraryManagementSystem;

    public LibraryManagementInterface(String libraryId, Integer totalRacks) {
        this.libraryManagementSystem = new LibraryManagementSystem(libraryId, totalRacks);
        System.out.println("[INFO]    | Created library with " + totalRacks + " racks");
    }

    public void addBook(String bookId, String title, String authors, String publishingCompanies, String bookCopyIds){
        List<String> authorList = Arrays.asList(authors.split(","));
        List<String> publishingCompanyList = Arrays.asList(publishingCompanies.split(","));
        List<String> bookCopyIdList = Arrays.asList(bookCopyIds.split(","));

        libraryManagementSystem.addBook(bookId, title, authorList, publishingCompanyList, bookCopyIdList);
    }

    public void getBorrowLimitPerUser(){
        System.out.println("[INFO]    | Fetching borrow limit per user...");
        System.out.println("[DETAILS] | Borrow Limit Per User: " + libraryManagementSystem.getBorrowLimitPerUser());
    }

    public void displayRacks() {
        libraryManagementSystem.displayRacks();
    }

    public void removeBookCopy(String copyId) {
        libraryManagementSystem.removeBookCopy(copyId);
    }

    public void addUser(String userId, String name) {
        libraryManagementSystem.addUser(userId,name);
    }

    public void borrowBook(String bookId, String userId, Date borrowDate) {
        libraryManagementSystem.borrowBook(bookId, userId, borrowDate);
    }

    public void borrowBookCopy(String bookCopyId, String userId, Date borrowDate) {
        libraryManagementSystem.borrowBookCopy(bookCopyId, userId, borrowDate);
    }

    public void returnBookCopy(String bookCopyId){
        libraryManagementSystem.returnBookCopy(bookCopyId);
    }

    public void printBorrowedBooks(String userId){
        libraryManagementSystem.printBorrowedBooks(userId);
    }

    public void searchBooks(String attribute, String value){
        libraryManagementSystem.searchBooks(attribute, value);
    }

}
