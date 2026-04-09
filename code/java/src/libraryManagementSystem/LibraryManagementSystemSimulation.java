package libraryManagementSystem;

import java.text.SimpleDateFormat;
import java.util.*;

public class LibraryManagementSystemSimulation {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        LibraryManagementInterface library = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

        while (true) {
            String input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("exit")) break;
            if (input.isEmpty()) continue;

            String[] parts = input.split(" ", 2);
            String command = parts[0];
            String[] params = (parts.length > 1) ? parts[1].split(" ") : new String[0];

            try {
                switch (command) {
                    case "create_library":
                        library = new LibraryManagementInterface(params[0], Integer.parseInt(params[1]));
                        break;

                    case "add_book":
                        if(library == null) {
                            System.out.println("[ERROR]   | Library not created. Use create_library command first.");
                            break;
                        }
                        library.addBook(params[0], params[1], params[2], params[3], params[4]);
                        break;

                    case "remove_book_copy":
                        if(library == null) {
                            System.out.println("[ERROR]   | Library not created. Use create_library command first.");
                            break;
                        }
                        library.removeBookCopy(params[0]);
                        break;

                    case "add_user":
                        if(library == null) {
                            System.out.println("[ERROR]   | Library not created. Use create_library command first.");
                            break;
                        }
                        library.addUser(params[0], params[1]);
                        break;

                    case "borrow_book":
                        if(library == null) {
                            System.out.println("[ERROR]   | Library not created. Use create_library command first.");
                            break;
                        }
                        library.borrowBook(params[0], params[1], sdf.parse(params[2]));
                        break;

                    case "borrow_book_copy":
                        if(library == null) {
                            System.out.println("[ERROR]   | Library not created. Use create_library command first.");
                            break;
                        }
                        library.borrowBookCopy(params[0], params[1], sdf.parse(params[2]));
                        break;

                    case "return_book_copy":
                        if(library == null) {
                            System.out.println("[ERROR]   | Library not created. Use create_library command first.");
                            break;
                        }
                        library.returnBookCopy(params[0]);
                        break;

                    case "print_borrowed":
                        if(library == null) {
                            System.out.println("[ERROR]   | Library not created. Use create_library command first.");
                            break;
                        }
                        library.printBorrowedBooks(params[0]);
                        break;

                    case "search":
                        if(library == null) {
                            System.out.println("[ERROR]   | Library not created. Use create_library command first.");
                            break;
                        }
                        library.searchBooks(params[0], params[1]);
                        break;

                    case "display_racks":
                        if(library == null) {
                            System.out.println("[ERROR]   | Library not created. Use create_library command first.");
                            break;
                        }
                        library.displayRacks();
                        break;

                    default:
                        System.out.println("[ERROR]   | Unknown command: " + command);
                }
            } catch (Exception e) {
                System.out.println("[ERROR]   | Invalid parameters for command: " + command);
            }
        }

        System.out.println("Exiting...");
    }
}
