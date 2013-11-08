package libraries;


import android.content.Context;

public enum Library {
    Instance;

    private NotificationFactory notificationFactory;

    public NotificationFactory getNotificationFactory() {
        // nb: add synchronized if needed
        return notificationFactory;
    }

    public void setNotificationFactory(NotificationFactory notificationFactory) {
        // nb: add synchronized if needed
        this.notificationFactory = notificationFactory;
    }

    public void InitForAndroid(Context context) {
        setNotificationFactory(new AndroidNotificationFactory(context));
    }

    public void InitForJvm() {
        setNotificationFactory(new JvmNotificationFactory());
    }
}
