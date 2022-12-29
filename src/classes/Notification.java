package classes;

import java.util.Objects;

public class Notification {

    private String movieName;

    private String message;

    public Notification(String movieName, String message) {
        this.movieName = movieName;
        this.message = message;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Notification that = (Notification) o;
        return Objects.equals(movieName, that.movieName) && Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(movieName, message);
    }
}
