package data;

import classes.Movie;
import classes.User;

import java.util.ArrayList;
import java.util.List;

public final class Database {

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

    /**
     * Get the instance of the database
     */
    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }

        return instance;
    }


    /**
     * Execute the login action
     * @param username the username of the user
     * @param password the password of the user
     * @return in case of succes, the logged-in user,
     *          else null
     */
    public User login(final String username, final String password) {

        for (User user : users) {
            if (user.getCredentials().getPassword().equals(password)
                    && user.getCredentials().getName().equals(username)) {
                currentUser = user;
                return user;
            }
        }

        return null;
    }


    /**
     * Check if a given username is available
     * @param username the username to be checked
     */
    public boolean checkUsernameAvailable(final String username) {
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

    public void setUsers(final List<User> users) {
        this.users = users;
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public void setMovies(final List<Movie> movies) {
        this.movies = movies;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(final User currentUser) {
        this.currentUser = currentUser;
    }

    public List<Movie> getFilteredMovies() {
        return filteredMovies;
    }

    public void setFilteredMovies(final List<Movie> filteredMovies) {
        this.filteredMovies = filteredMovies;
    }


    /**
     * Deep-copy the list of available movies
     * to a user
     * @param user the user whose list will be deep-copied
     */
    public void deepCopyFilteredMovies(final User user) {
        this.filteredMovies = new ArrayList<>();
        for (Movie movie : this.movies) {
            if (!movie.isBannedForTheUser(currentUser.getCredentials().getCountry())) {
                filteredMovies.add(new Movie(movie));
            }

        }
    }

    /**
     * Return a movie from the list of movies available
     * to the user
     * @param moviesList the list of movies available to the user
     * @param movieName the name of the movie searched for
     */
    public Movie getMovie(final List<Movie> moviesList, final String movieName) {
        for (Movie movie : moviesList) {
            if (movie.getName().equals(movieName)) {
                return movie;
            }
        }

        return null;
    }





}
