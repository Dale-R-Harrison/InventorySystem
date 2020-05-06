package inventorySystem;

public class PartsTooHighException extends Exception {
    public PartsTooHighException(String message) {
        super(message);
    }
}
