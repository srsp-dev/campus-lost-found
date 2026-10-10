
package com.campus.lostfound;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final LostFoundService service = new LostFoundService();

    public static void main(String[] args) {
        System.out.println("====================================");
        System.out.println(" CAMPUS LOST & FOUND MANAGEMENT");
        System.out.println("====================================");

        boolean running = true;

        while (running) {
            printMenu();
            String choice = scanner.nextLine().trim();

            try {
                switch (choice) {
                    case "1" -> reportItem(ItemStatus.LOST);
                    case "2" -> reportItem(ItemStatus.FOUND);
                    case "3" -> viewAllItems();
                    case "4" -> searchItems();
                    case "5" -> claimItem();
                    case "6" -> returnItem();
                    case "0" -> {
                        running = false;
                        System.out.println("Thank you for using Campus Lost & Found!");
                    }
                    default -> System.out.println("Invalid choice. Please try again.");
                }
            } catch (IllegalArgumentException | IllegalStateException |
                     java.util.NoSuchElementException exception) {
                System.out.println("Operation failed: " + exception.getMessage());
            }

            System.out.println();
        }

        scanner.close();
    }

    private static void printMenu() {
        System.out.println("1. Report a LOST item");
        System.out.println("2. Report a FOUND item");
        System.out.println("3. View all items");
        System.out.println("4. Search items");
        System.out.println("5. Claim a found item");
        System.out.println("6. Mark a claimed item as returned");
        System.out.println("0. Exit");
        System.out.print("Choose an option: ");
    }

    private static void reportItem(ItemStatus status) {
        System.out.print("Item name: ");
        String name = scanner.nextLine();

        System.out.print("Category (Electronics, Stationery, etc.): ");
        String category = scanner.nextLine();

        System.out.print("Description: ");
        String description = scanner.nextLine();

        System.out.print("Location: ");
        String location = scanner.nextLine();

        System.out.print("Date (YYYY-MM-DD): ");
        LocalDate date = LocalDate.parse(scanner.nextLine().trim());

        LostFoundItem item = service.reportItem(
                name, category, description, location, date, status);

        System.out.println("Item reported successfully!");
        System.out.println("Item ID: " + item.getId());
        System.out.println("Status: " + item.getStatus());
    }

    private static void viewAllItems() {
        List<LostFoundItem> items = service.getAllItems();

        if (items.isEmpty()) {
            System.out.println("No items have been reported yet.");
            return;
        }

        for (LostFoundItem item : items) {
            System.out.println(item);
        }
    }

    private static void searchItems() {
        System.out.print("Enter item name, category, description, or location: ");
        String keyword = scanner.nextLine();

        List<LostFoundItem> results = service.searchItems(keyword);

        if (results.isEmpty()) {
            System.out.println("No matching items found.");
            return;
        }

        results.forEach(System.out::println);
    }

    private static void claimItem() {
        System.out.print("Enter the found item's ID: ");
        String id = scanner.nextLine().trim();

        LostFoundItem item = service.claimItem(id);
        System.out.println("Item claimed successfully!");
        System.out.println(item);
    }

    private static void returnItem() {
        System.out.print("Enter the claimed item's ID: ");
        String id = scanner.nextLine().trim();

        LostFoundItem item = service.returnItem(id);
        System.out.println("Item marked as returned!");
        System.out.println(item);
    }
}
