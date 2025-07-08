public class EBook extends Book {
    private final String fileType;

    public EBook(String isbn, String title, int year, double price, String author, String fileType) {
        super(isbn, title, year, price, author);
        if (fileType == null || fileType.trim().isEmpty()) {
            throw new IllegalArgumentException("File type cannot be null or empty");
        }
        this.fileType = fileType.trim().toUpperCase();
    }

    public String getFileType() {
        return fileType;
    }

    @Override
    public boolean isForSale() {
        return true;
    }

    @Override
    public String toString() {
        return super.toString() + " | File Type: " + fileType;
    }
} 