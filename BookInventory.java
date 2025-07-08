import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class BookInventory {
    private static final String STORE_PREFIX = "Quantum book store | ";
    private static final int MAX_QUANTITY = 100;
    
    private final Map<String, Book> books = new ConcurrentHashMap<>();
    private final ShippingService shippingService;
    private final MailService mailService;

    public BookInventory(ShippingService shippingService, MailService mailService) {
        if (shippingService == null) {
            throw new IllegalArgumentException("Shipping service cannot be null");
        }
        if (mailService == null) {
            throw new IllegalArgumentException("Mail service cannot be null");
        }
        this.shippingService = shippingService;
        this.mailService = mailService;
    }

    public void addBook(Book book) {
        if (book == null) {
            throw new IllegalArgumentException("Book cannot be null");
        }
        books.put(book.getIsbn(), book);
        System.out.println(STORE_PREFIX + "Book added: " + book);
    }

    public List<Book> removeOutdatedBooks(int maxAge, int currentYear) {
        if (maxAge < 0) {
            throw new IllegalArgumentException("Max age cannot be negative");
        }
        if (currentYear < 1900 || currentYear > 2100) {
            throw new IllegalArgumentException("Invalid current year: " + currentYear);
        }
        
        List<Book> removed = new ArrayList<>();
        Iterator<Map.Entry<String, Book>> it = books.entrySet().iterator();
        while (it.hasNext()) {
            Book book = it.next().getValue();
            if (currentYear - book.getYear() > maxAge) {
                removed.add(book);
                it.remove();
                System.out.println(STORE_PREFIX + "Removed outdated book: " + book);
            }
        }
        return removed;
    }

    public double buyBook(String isbn, int quantity, String email, String address) {
        if (isbn == null || isbn.trim().isEmpty()) {
            throw new IllegalArgumentException("ISBN cannot be null or empty");
        }
        if (quantity <= 0 || quantity > MAX_QUANTITY) {
            throw new IllegalArgumentException("Invalid quantity: " + quantity);
        }
        
        Book book = books.get(isbn);
        if (book == null) {
            throw new IllegalArgumentException(STORE_PREFIX + "Book not found: " + isbn);
        }
        if (!book.isForSale()) {
            throw new IllegalArgumentException(STORE_PREFIX + "Book is not for sale: " + isbn);
        }
        
        if (book instanceof PaperBook) {
            return processPaperBookPurchase((PaperBook) book, quantity, address);
        } else if (book instanceof EBook) {
            return processEBookPurchase((EBook) book, quantity, email);
        } else {
            throw new IllegalArgumentException(STORE_PREFIX + "Book type not supported for purchase: " + book.getClass().getSimpleName());
        }
    }

    private double processPaperBookPurchase(PaperBook paperBook, int quantity, String address) {
        if (address == null || address.trim().isEmpty()) {
            throw new IllegalArgumentException("Address is required for paper book purchase");
        }
        
        paperBook.reduceStock(quantity);
        shippingService.shipPaperBook(paperBook, address);
        System.out.println(STORE_PREFIX + "Shipped " + quantity + " paper book(s) to " + address);
        return paperBook.getPrice() * quantity;
    }

    private double processEBookPurchase(EBook eBook, int quantity, String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email is required for ebook purchase");
        }
        if (quantity != 1) {
            throw new IllegalArgumentException(STORE_PREFIX + "Can only buy one copy of an EBook at a time");
        }
        
        mailService.sendEBook(eBook, email);
        System.out.println(STORE_PREFIX + "Sent ebook to " + email);
        return eBook.getPrice();
    }

    public Book getBook(String isbn) {
        if (isbn == null || isbn.trim().isEmpty()) {
            throw new IllegalArgumentException("ISBN cannot be null or empty");
        }
        return books.get(isbn);
    }

    public Collection<Book> getAllBooks() {
        return new ArrayList<>(books.values());
    }

    public boolean hasBook(String isbn) {
        return books.containsKey(isbn);
    }

    public int getInventorySize() {
        return books.size();
    }
} 