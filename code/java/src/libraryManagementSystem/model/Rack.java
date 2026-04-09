package libraryManagementSystem.model;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
public class Rack {
    private final Integer rackNumber;
    private final List<BookCopy> bookCopyList; //each rack can hold at most one book copy
    private final Set<String> bookIdsInRack; //to ensure no duplicate books in the same rack

    public Rack(Integer rackNumber) {
        this.rackNumber = rackNumber;
        this.bookCopyList = new ArrayList<>();
        this.bookIdsInRack = new HashSet<>();
    }

    public void addBookCopy(BookCopy bookCopy) {
        if(checkAvailabilityForBook(bookCopy)) {
            this.bookCopyList.add(bookCopy);
            this.bookIdsInRack.add(bookCopy.getBookId());
        }else {
            System.out.println("[WARN]    | Skipping addition of duplicate book ID: " + bookCopy.getBookId());
        }
    }

    public void removeBookCopy(String copyId) {
        BookCopy toRemove = null;

        // Find the BookCopy by copyId
        for (BookCopy copy : bookCopyList) {
            if (copy.getCopyId().equals(copyId)) {
                toRemove = copy;
                break;
            }
        }

        // Remove if found
        if (toRemove != null) {
            bookCopyList.remove(toRemove);
            bookIdsInRack.remove(toRemove.getBookId()); // Update book tracking
        } else {
            System.out.println("[WARN]    | Book copy ID " + copyId + " not found in rack " + rackNumber);
        }
    }


    public boolean checkAvailabilityForBook(BookCopy bookCopy) {
        return !bookIdsInRack.contains(bookCopy.getBookId());
    }

    public void displayBookCopies() {
        System.out.println("Rack Number: " + rackNumber);
        if(bookCopyList.isEmpty()) {
            System.out.println(" - No book copies in this rack.");
            return;
        }
        for (BookCopy copy : bookCopyList) {
            System.out.println(" - Copy ID: " + copy.getCopyId() + ", Title: " + copy.getTitle());
        }

    }
}
