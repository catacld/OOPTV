package classes;

import java.util.ArrayList;
import java.util.List;

public class Database {

    private static Database instance = null;

    private List<User> users;

    // all the movies from the database
    private List<Movie> movies;

    // a list on which filters and restrictions
    // will be applied
    private List<Movie> filteredMovies;

    private User currentUser;

    private Database() {
    }

    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }

        return instance;
    }

    // check if the given credentials for login
    // are valid
    public User validLogin(String username, String password) {

        for (User user : users) {
            if (user.getCredentials().getPassword().equals(password) &&
                    user.getCredentials().getName().equals(username)) {
                currentUser = user;
                return user;
            }
        }

        return null;
    }

    // check if there is already a user registered
    // with a username
    public boolean checkUsernameAvailable(String username) {
        for (User user : users) {
            if (user.getCredentials().getName().equals(username)) {
                // the username is already taken
                return false;
            }
        }
        return true;
    }

    public static void setInstance(final Database instance) {
        Database.instance = instance;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public List<Movie> getFilteredMovies() {
        return filteredMovies;
    }

    public void setFilteredMovies(List<Movie> filteredMovies) {
        this.filteredMovies = filteredMovies;
    }

    // the filtered movies list will be a deep copy
    // of the original list
    public void deepCopyFilteredMovies(User user) {
        this.filteredMovies = new ArrayList<>();
        for (Movie movie : this.movies) {
            if (!movie.isBannedForTheUser(currentUser.getCredentials().getCountry())) {
                filteredMovies.add(new Movie(movie));
            }

        }
    }

    // check if a movie is available for a user
    public Movie getMovie(String movieName) {
        for (Movie movie : filteredMovies) {
            if (movie.getName().equals(movieName)) {
                return movie;
            }
        }

        return null;
    }
}
