import java.util.*;

abstract class Book {
    protected String isbn;
    protected String title;
    protected String author;
    protected int year;
    protected double price;

    public Book(String isbn, String title, String author, int year, double price) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.year = year;
        this.price = price;
    }

    public String getIsbn() { return isbn; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public int getYear() { return year; }
    public double getPrice() { return price; }

    public abstract boolean isForSale();
    public abstract double buy(int quantity, String target);
}

class PaperBook extends Book {
    private int stock;

    public PaperBook(String isbn, String title, String author, int year, double price, int stock) {
        super(isbn, title, author, year, price);
        this.stock = stock;
    }

    public boolean isForSale() {
        return true;
    }

    public int getStock() {
        return stock;
    }

    public void reduceStock(int qty) {
        stock -= qty;
    }

    public double buy(int quantity, String address) {
        if (stock < quantity) {
            System.out.println("Quantum book store: Not enough stock.");
            return 0;
        }
        reduceStock(quantity);
        System.out.println("Quantum book store: ShippingService shipping to " + address);
        double total = price * quantity;
        System.out.println("Quantum book store: Paid: " + (int) total);
        return total;
    }
}

class EBook extends Book {
    private String fileType;

    public EBook(String isbn, String title, String author, int year, double price, String fileType) {
        super(isbn, title, author, year, price);
        this.fileType = fileType;
    }

    public boolean isForSale() {
        return true;
    }

    public double buy(int quantity, String email) {
        System.out.println("Quantum book store: MailService sending to " + email);
        double total = price * quantity;
        System.out.println("Quantum book store: Paid: " + (int) total);
        return total;
    }
}

class DemoBook extends Book {
    public DemoBook(String isbn, String title, String author, int year) {
        super(isbn, title, author, year, 0);
    }

    public boolean isForSale() {
        return false;
    }

    public double buy(int quantity, String target) {
        System.out.println("Quantum book store: This book is not for sale.");
        return 0;
    }
}

class Inventory {
    private List<Book> books = new ArrayList<>();

    public void addBook(Book book) {
        books.add(book);
    }

    public Book getBookByISBN(String isbn) {
        for (Book b : books) {
            if (b.getIsbn().equals(isbn)) {
                return b;
            }
        }
        return null;
    }

    public void removeOutdatedBooks(int maxAge, int currentYear) {
        books.removeIf(book -> {
            if (currentYear - book.getYear() > maxAge) {
                System.out.println("Quantum book store: Removed outdated book: " + book.getTitle());
                return true;
            }
            return false;
        });
    }
}

class Bookstore {
    private Inventory inventory;

    public Bookstore(Inventory inventory) {
        this.inventory = inventory;
    }

    public double buy(String isbn, int quantity, String target) {
        Book selectedBook = inventory.getBookByISBN(isbn);
        if (selectedBook == null) {
            System.out.println("Quantum book store: Book not found.");
            return 0;
        }

        if (!selectedBook.isForSale()) {
            System.out.println("Quantum book store: Book is not for sale.");
            return 0;
        }

        return selectedBook.buy(quantity, target);
    }
}

public class QuantumBookstoreFullTest {
    public static void main(String[] args) {
        Inventory inventory = new Inventory();

        Book paperBook = new PaperBook("111", "good to great", "Ahmed Samy", 2015, 200, 10);
        Book ebook = new EBook("222", "rich man", "Fatma Tarek", 2022, 150, "PDF");
        Book demo = new DemoBook("333", "Free Sample", "Anonymous", 2010);

        inventory.addBook(paperBook);
        inventory.addBook(ebook);
        inventory.addBook(demo);

        Bookstore store = new Bookstore(inventory);

        System.out.println("Quantum book store: ---- Buying Paper Book ----");
        store.buy("111", 2, "Cairo, Egypt");

        System.out.println("\nQuantum book store: ---- Buying EBook ----");
        store.buy("222", 1, "ahmed@email.com");

        System.out.println("\nQuantum book store: ---- Removing outdated books ----");
        inventory.removeOutdatedBooks(10, 2025);

        System.out.println("\nQuantum book store: ---- Trying to buy DemoBook ----");
        store.buy("333", 1, "anywhere");
    }
}
