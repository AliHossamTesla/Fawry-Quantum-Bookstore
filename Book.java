public abstract class Book {
    private final String isbn;
    private final String title;
    private final int year;
    private final double price;
    private final String author;

    public Book(String isbn, String title, int year, double price, String author) {
        if (isbn == null || isbn.trim().isEmpty()) {
            throw new IllegalArgumentException("ISBN cannot be null or empty");
        }
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty");
        }
        if (year < 0 || year > 2100) {
            throw new IllegalArgumentException("Invalid year: " + year);
        }
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
        if (author == null || author.trim().isEmpty()) {
            throw new IllegalArgumentException("Author cannot be null or empty");
        }
        
        this.isbn = isbn.trim();
        this.title = title.trim();
        this.year = year;
        this.price = price;
        this.author = author.trim();
    }

    public String getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }

    public int getYear() {
        return year;
    }

    public double getPrice() {
        return price;
    }

    public String getAuthor() {
        return author;
    }

    public abstract boolean isForSale();

    @Override
    public String toString() {
        return String.format("Quantum book store | ISBN: %s | Title: %s | Author: %s | Year: %d | Price: %.2f", 
                           isbn, title, author, year, price);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Book book = (Book) obj;
        return isbn.equals(book.isbn);
    }

    @Override
    public int hashCode() {
        return isbn.hashCode();
    }
} 