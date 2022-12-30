package classes;

import java.util.Objects;

public final class Notification {

    private final String movieName;

    private final String message;

    private Notification(final NotificationBuilder builder) {
        this.movieName = builder.movieName;
        this.message = builder.message;
    }

    public String getMovieName() {
        return movieName;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Notification that = (Notification) o;
        return Objects.equals(movieName, that.movieName) && Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(movieName, message);
    }


    // implement builder to be easy scalable in the
    // future if more fields are implemented
    // inside a notification
    public static class NotificationBuilder {

        //mandatory
        private final String message;

        // optional
        private String movieName = "No recommendation";

        public NotificationBuilder(final String message) {
            this.message = message;
        }

        /**
         * add a movie name inside the notification
         * @param movieName the movie to be added
         * @return notificationBuilder with the field
         *          "movieName" set
         */
        public NotificationBuilder movieName(final String movieName) {
            this.movieName = movieName;
            return this;
        }

        /**
         * build a notification
         * @return the built notification
         */
        public Notification build() {
            return new Notification(this);
        }
    }
}
