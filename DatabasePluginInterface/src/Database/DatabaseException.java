package Database;

public class DatabaseException extends Exception {
    public DatabaseException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}