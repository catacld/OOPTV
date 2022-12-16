package data;

import classes.Movie;
import classes.User;

import java.util.ArrayList;
import java.util.List;

public class Database {

    private static Database instance = null;

    private List<User> users;

    // all the movies from the database
    private List<Movie> movies;

    // the list of movies available to the user
    // after country restrictions and, eventually,
    // filters are applied
    private List<Movie> filteredMovies;

    // the logged-in user
    private User currentUser;

    private Database() {
    }

    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }

        return instance;
    }

    // try to execute the login action
    // in case of success, the logged-in user will be returned
    // else, null will be returned
    public User login(String username, String password) {

        for (User user : users) {
            if (user.getCredentials().getPassword().equals(password) &&
                    user.getCredentials().getName().equals(username)) {
                currentUser = user;
                return user;
            }
        }

        return null;
    }

    // check if a given username is available
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

    // deep-copy the list of available movies
    // to the user
    public void deepCopyFilteredMovies(User user) {
        this.filteredMovies = new ArrayList<>();
        for (Movie movie : this.movies) {
            if (!movie.isBannedForTheUser(currentUser.getCredentials().getCountry())) {
                filteredMovies.add(new Movie(movie));
            }

        }
    }

    // get a movie from the list of movies available
    // to the user
    public Movie getMovie(List<Movie> moviesList, String movieName) {
        for (Movie movie : moviesList) {
            if (movie.getName().equals(movieName)) {
                return movie;
            }
        }

        return null;
    }





}
