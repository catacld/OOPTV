package genres;

import classes.Notification;
import classes.User;

import java.util.ArrayList;
import java.util.List;

public class Drama {

    private final static List<User> subscribers = new ArrayList<>();

    public static void addSubscriber(User user) {
        subscribers.add(user);
    }


    public static void notify(String movieName) {

        Notification notification = new Notification(movieName, "ADD");

        for (User user : subscribers) {
            user.addNotification(notification);
        }
    }
}
