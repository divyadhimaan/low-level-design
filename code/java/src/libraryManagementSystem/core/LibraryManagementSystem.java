package libraryManagementSystem.core;

import lombok.Getter;
import model.*;

import java.util.*;

@Getter
public class LibraryManagementSystem {
    private final String libraryId;
    private final Integer borrowLimitPerUser = 5;
    private final Integer totalRacks;
    private final Map<Integer, Rack> racks; //rackNumber -> Rack
    private final Map<String, BookCopy> copyIdToBookCopy; //copyId -> BookCopy
    private final Map<String, LibraryUser> userIdToLibraryUser; //userId -> LibraryUser
    private final Map<String, Book> bookIdToBook; //bookId -> Book
    private final List<LibraryBorrowedLog> borrowedLogs;


    public LibraryManagementSystem(String libraryId, Integer totalRacks) {
        this.libraryId = libraryId;
        this.totalRacks = totalRacks;
        this.racks = new HashMap<>();
        for(int i=1; i<=totalRacks; i++){
            racks.put(i, new Rack(i)); //initially all racks are empty
        }
        this.copyIdToBookCopy = new HashMap<>();
        this.userIdToLibraryUser = new HashMap<>();
        this.bookIdToBook = new HashMap<>();
        this.borrowedLogs = new ArrayList<>();
    }

    public void addBook(String bookId, String title, List<String> authorList, List<String> publishingCompanyList, List<String> bookCopyIdList)
    {

        Book book = new Book(bookId, title, authorList, publishingCompanyList, bookCopyIdList);
        int availableRackCount = 0;

        // Step 1: Count racks available for this book (each rack can hold 1 copy)
        for (Rack rack : racks.values()) {
            if (rack.checkAvailabilityForBook(new BookCopy("temp", book, rack.getRackNumber()))) {
                availableRackCount++;
            }
        }

        // Step 2: Check if enough racks exist
        if (availableRackCount < bookCopyIdList.size()) {
            System.out.println("[ERROR]   | Rack not available for book ID: " + bookId);
            return ; // Reject this add request entirely
        }

        List<Integer> rackNumbers = new ArrayList<>();

        for (String copyId : bookCopyIdList) {
            for (int i = 1; i <= totalRacks; i++) { // ascending order
                Rack rack = racks.get(i);
                BookCopy newBookCopy = new BookCopy(copyId, book, rack.getRackNumber());
                if (rack.checkAvailabilityForBook(newBookCopy)) {
                    rack.addBookCopy(newBookCopy);
                    copyIdToBookCopy.put(copyId, newBookCopy);
                    rackNumbers.add(rack.getRackNumber());
                    break;
                }
            }
        }
        bookIdToBook.put(bookId, book);
        System.out.println("[INFO]    | Added Book to racks: " + rackNumbers);
    }

    public void removeBookCopy(String copyId) {
        BookCopy bookCopy = copyIdToBookCopy.get(copyId);
        if(bookCopy == null){
            System.out.println("[ERROR]   | Invalid Book Copy ID");
            return;
        }
        Rack rack = racks.get(bookCopy.getRackNumber());
        if (rack != null) {
            rack.removeBookCopy(copyId);
            copyIdToBookCopy.remove(copyId);
            System.out.println("[INFO]    | Removed book copy: "+ copyId + " from rack: " + rack.getRackNumber());


            boolean anyCopyLeft = copyIdToBookCopy.values().stream()
                    .anyMatch(c -> c.getBookId().equals(bookCopy.getBookId()));
            if (!anyCopyLeft) {
                bookIdToBook.remove(bookCopy.getBookId());
                System.out.println("[INFO]    | No more copies left for Book ID: " + bookCopy.getBookId() +
                        ". Book removed from library.");
            }
        }


    }

    public void addUser(String userId,String name) {
        LibraryUser user = new LibraryUser(userId, name, borrowLimitPerUser);
        userIdToLibraryUser.put(user.getUserId(), user);
        System.out.println("[INFO]    | Added user: " + name + " with User ID: " + user.getUserId());
    }

    public void borrowBook(String bookId, String userId, Date borrowDate) {
        LibraryUser user = userIdToLibraryUser.get(userId);
        if(!verifyUser(user, true))
            return;

        if(!bookIdToBook.containsKey(bookId)){
            System.out.println("[ERROR]   | Invalid Book ID");
            return;
        }

        // Find the first available book copy based on rack number
        BookCopy availableCopy = null;
        for (int i = 1; i <= totalRacks; i++) { // ascending rack order
            Rack rack = racks.get(i);
            for (BookCopy copy : rack.getBookCopyList()) {
                if (copy.getBookId().equals(bookId) && copy.isAvailable()) {
                    availableCopy = copy;
                    break;
                }
            }
            if (availableCopy != null) break;
        }

        if (availableCopy == null) {
            System.out.println("[ERROR]   | No available copies for Book ID: " + bookId);
            return;
        }


        Date dueDate = new Date(borrowDate.getTime() + (14L * 24 * 60 * 60 * 1000)); // 2 weeks from borrow date
        LibraryBorrowedLog log = new LibraryBorrowedLog(UUID.randomUUID().toString(), availableCopy, user, borrowDate, dueDate);
        borrowedLogs.add(log);

        // Mark the book copy as borrowed
        availableCopy.setBorrowedDetails(user, dueDate);
        user.addBorrowedBook(availableCopy);

        System.out.println("[INFO]    | User " + user.getName() + " borrowed book copy ID: " + availableCopy.getCopyId() + ", Due Date: " + dueDate + " from Rack: " + availableCopy.getRackNumber());
    }

    public void borrowBookCopy(String bookCopyId, String userId, Date borrowDate) {
        LibraryUser user = userIdToLibraryUser.get(userId);
        if(!verifyUser(user, true))
            return;

        BookCopy bookCopy = copyIdToBookCopy.get(bookCopyId);
        if(bookCopy == null){
            System.out.println("[ERROR]   | Invalid Book Copy ID");
            return;
        }
        if(!bookCopy.isAvailable()){
            System.out.println("[ERROR]   | Book Copy ID: " + bookCopyId + " is currently not available");
            return;
        }

        Date dueDate = new Date(borrowDate.getTime() + (14L * 24 * 60 * 60 * 1000)); // 2 weeks from borrow date
        LibraryBorrowedLog log = new LibraryBorrowedLog(UUID.randomUUID().toString(), bookCopy, user, borrowDate, dueDate);
        borrowedLogs.add(log);

        // Mark the book copy as borrowed
        bookCopy.setBorrowedDetails(user, dueDate);
        user.addBorrowedBook(bookCopy);

        System.out.println("[INFO]    | User " + user.getName() + " borrowed book copy ID: " + bookCopy.getCopyId() + ", Due Date: " + dueDate + " from Rack: " + bookCopy.getRackNumber());
    }

    private void returnBookCopyToRack(BookCopy bookCopy) {
        for (int i = 1; i <= totalRacks; i++) {
            Rack rack = racks.get(i);
            if (rack.checkAvailabilityForBook(bookCopy)) {
                rack.addBookCopy(bookCopy);
                bookCopy.setRackNumber(i); // if rackNumber is non-final
                return;
            }
        }
        System.out.println("[WARN] | No racks available to place returned book copy: " + bookCopy.getCopyId());
    }

    public void returnBookCopy(String bookCopyId){
        BookCopy bookCopy = copyIdToBookCopy.get(bookCopyId);
        if(bookCopy == null){
            System.out.println("[ERROR]   | Invalid Book Copy ID");
            return;
        }

        // Find the corresponding log
        for (LibraryBorrowedLog log : borrowedLogs) {
            if (log.getBookCopy().getCopyId().equals(bookCopyId) && log.getReturnDate() == null) {
                log.markAsReturned(new Date());
                break;
            }
        }

        // Mark the book copy as returned
        LibraryUser user = bookCopy.getBorrowedBy();
        bookCopy.setBorrowedDetails(null, null);
        if(user != null) {
            user.getBorrowedBooks().remove(bookCopy);
        }

        returnBookCopyToRack(bookCopy);

        System.out.println("[INFO]    | Book copy ID: " + bookCopyId + " has been returned and is now available in Rack: " + bookCopy.getRackNumber());
    }

    public void printBorrowedBooks(String userId){
        if(!verifyUser(userIdToLibraryUser.get(userId))){
            return;
        }

        LibraryUser user = userIdToLibraryUser.get(userId);
        List<BookCopy> borrowedBooks = user.getBorrowedBooks();
        if(borrowedBooks.isEmpty()) {
            System.out.println("[INFO]    | User " + user.getName() + " has no borrowed books.");
            return;
        }
        System.out.println("[INFO]    | User " + user.getName() + " has borrowed the following books:");

        for(BookCopy bookCopy: borrowedBooks){
            System.out.println("[DETAILS] | Book Copy: "+ bookCopy.getCopyId()+ " "+ " Title: "+bookCopy.getTitle()+ " Due Date: "+ bookCopy.getDueDate());
        }


    }

    public void searchBooks(String attribute, String value){
        if(!checkInputSearchParam(attribute, value)){
            return;
        }

        switch(attribute){
            case "book_id":
                searchByBookId(value);
                break;
            case "author":
                searchByAuthor(value);
                break;
            case "publisher":
                searchByPublisher(value);
                break;
            case "title":
                searchByTitle(value);
                break;
            default:
                System.out.println("[ERROR]   | Invalid attribute. Allowed: book_id, author, publisher, title");
        }
    }

    private void searchByBookId(String bookId) {
        boolean found = false;

        for(BookCopy bookCopy: copyIdToBookCopy.values()){
            if(bookCopy.getBookId().equalsIgnoreCase(bookId)){
                printBookCopyDetails(bookCopy);
                found=true;
            }
        }

        if(!found){
            System.out.println("[ERROR]    | No books found for Book ID: " + bookId);
        }
    }

    private void searchByTitle(String title){
        boolean found = false;

        for(BookCopy bookCopy: copyIdToBookCopy.values()){
            if(bookCopy.getTitle().equalsIgnoreCase(title)){
                printBookCopyDetails(bookCopy);
                found=true;
            }
        }

        if(!found){
            System.out.println("[ERROR]    | No books found for title: " + title);
        }
    }

    private void searchByAuthor(String author){
        boolean found = false;

        for(BookCopy bookCopy: copyIdToBookCopy.values()){
            if(bookCopy.getAuthors() != null && bookCopy.getAuthors().stream().anyMatch(a->a.equalsIgnoreCase(author))){
                printBookCopyDetails(bookCopy);
                found=true;
            }
        }
        if(!found){
            System.out.println("[ERROR]    | No books found for Author: " + author);
        }
    }

    private void searchByPublisher(String publisher){
        boolean found = false;

        for(BookCopy bookCopy: copyIdToBookCopy.values()){
            if(bookCopy.getPublishingCompany() != null && bookCopy.getPublishingCompany().stream().anyMatch(p->p.equalsIgnoreCase(publisher))){
                printBookCopyDetails(bookCopy);
                found=true;
            }
        }
        if(!found){
            System.out.println("[ERROR]    | No books found for Publisher: " + publisher);
        }
    }

    private void printBookCopyDetails(BookCopy copy) {
        Book book = copy.getBook();
        String authors = String.join(",", book.getAuthors());
        String publishers = String.join(",", book.getPublishingCompany());
        String borrowedById = (copy.getBorrowedBy() != null) ? copy.getBorrowedBy().getUserId() : "N/A";
        String dueDate = "N/A";

        for (LibraryBorrowedLog log : borrowedLogs) {
            if (log.getBookCopy().equals(copy) && log.getReturnDate() == null) {
                dueDate = log.getDueDate().toString();
                break;
            }
        }

        System.out.println("Book Copy: " +
                copy.getCopyId() + " " +
                book.getBookId() + " " +
                book.getTitle() + " " +
                authors + " " +
                publishers + " " +
                copy.getRackNumber() + " " +
                borrowedById + " " +
                dueDate);
    }

    private boolean checkInputSearchParam(String attribute, String value) {
        if (attribute == null || value == null || value.trim().isEmpty()) {
            System.out.println("[ERROR]   | Please provide a valid attribute and value");
            return false;
        }
        return true;
    }

    private boolean verifyUser(LibraryUser user) {
        return verifyUser(user, false);
    }

    private boolean verifyUser(LibraryUser user, Boolean checkLimit) {
        if(user == null) {
            System.out.println("[ERROR]   | Invalid User ID");
            return false;
        }
        if(checkLimit && user.getBorrowedBooks() != null && user.getBorrowedBooks().size() >= borrowLimitPerUser) {
            System.out.println("[ERROR]   | Overlimit: User has reached the borrow limit");
            return false;
        }

        return true;
    }

    public void displayRacks() {
        System.out.println("Current state of racks:");
        for (Rack rack : racks.values()) {
            rack.displayBookCopies();
        }
    }
}


