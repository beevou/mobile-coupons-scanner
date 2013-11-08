package libraries;

import android.content.Context;

public final class AndroidNotificationFactory extends NotificationFactory {

    private final Context context;

    public AndroidNotificationFactory(Context context) {
        this.context = context;
    }

    @Override
    public void newToast(String msg) {
        // normal Android toast stuff using the context
    }

}
