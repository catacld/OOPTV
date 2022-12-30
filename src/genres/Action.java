package genres;

import classes.Notification;
import classes.User;

import java.util.ArrayList;
import java.util.List;

public final class Action {

    private static final List<User> SUBSCRIBERS = new ArrayList<>();

    private Action() {
    }

    /**
     * add a user to the list of subscribers
     * @param user the user to be added
     */
    public static void addSubscriber(final User user) {
        SUBSCRIBERS.add(user);
    }


    /**
     * notify the users of adding a new movie
     * @param movieName the name of the added movie
     */
    public static void notify(final String movieName) {

        Notification notification = new Notification.NotificationBuilder("ADD")
                                        .movieName(movieName).build();

        for (User user : SUBSCRIBERS) {
            user.addNotification(notification);
        }
    }


}
