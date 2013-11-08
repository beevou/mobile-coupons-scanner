package libraries;

public abstract class NotificationFactory {
    /**
     * Pops a message up for the user.
     * 
     * @param msg
     */
    public abstract void newToast(String msg);

    // other notifications can go here
}