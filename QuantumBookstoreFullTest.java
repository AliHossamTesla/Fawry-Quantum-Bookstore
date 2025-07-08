public class QuantumBookstoreFullTest {
    public static void main(String[] args) {
        ShippingService shippingService = new ShippingService() {
            @Override
            public void shipPaperBook(PaperBook book, String address) {
                System.out.println("Quantum book store | (Mock) Shipping paper book '" + book.getTitle() + "' to " + address);
            }
        };
        
        MailService mailService = new MailService() {
            @Override
            public void sendEBook(EBook book, String email) {
                System.out.println("Quantum book store | (Mock) Sending ebook '" + book.getTitle() + "' to " + email);
            }
        };

        BookInventory inventory = new BookInventory(shippingService, mailService);

        PaperBook paperBook = new PaperBook("ISBN001", "Java for Humans", 2015, 150.0, "Alice Smith", 5);
        EBook eBook = new EBook("ISBN002", "Quantum Computing 101", 2020, 80.0, "Bob Jones", "PDF");
        ShowcaseBook showcaseBook = new ShowcaseBook("ISBN003", "Rare Manuscript", 1950, 0.0, "Unknown Author");

        inventory.addBook(paperBook);
        inventory.addBook(eBook);
        inventory.addBook(showcaseBook);

        testPaperBookPurchase(inventory);
        testEBookPurchase(inventory);
        testShowcaseBookPurchase(inventory);
        testInvalidPurchases(inventory);
        testOutdatedBookRemoval(inventory);
    }

    private static void testPaperBookPurchase(BookInventory inventory) {
        try {
            double paid = inventory.buyBook("ISBN001", 2, "buyer@example.com", "123 Main St");
            System.out.println("Quantum book store | Paid amount: " + paid);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void testEBookPurchase(BookInventory inventory) {
        try {
            double paid = inventory.buyBook("ISBN002", 1, "reader@example.com", "");
            System.out.println("Quantum book store | Paid amount: " + paid);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void testShowcaseBookPurchase(BookInventory inventory) {
        try {
            inventory.buyBook("ISBN003", 1, "", "");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void testInvalidPurchases(BookInventory inventory) {
        try {
            inventory.buyBook("NONEXISTENT", 1, "test@example.com", "Test Address");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            inventory.buyBook("ISBN002", 2, "test@example.com", "");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            inventory.buyBook("ISBN001", 10, "test@example.com", "");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void testOutdatedBookRemoval(BookInventory inventory) {
        inventory.removeOutdatedBooks(10, 2025);
    }
} 