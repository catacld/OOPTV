package data;

import classes.Movie;
import classes.Notification;
import classes.User;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import genres.Action;
import genres.Comedy;
import genres.Crime;
import genres.Drama;
import genres.Thriller;
import ioclasses.Writer;
import pages.Page;


import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

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

    // the movie whose "see details"
    // page the user is currently
    // viewing
    private Movie currentMovie;

    // the list of pages the user has
    // navigated on
    private ArrayDeque<Page> history;

    // auxiliary list for when the user goes
    // back to a "see details" page
    private ArrayDeque<String> movieTitles;

    private Database() {
        history = new ArrayDeque<>();
        movieTitles = new ArrayDeque<>();
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
     * @return the logged-in user, in case of success,
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

    public ArrayDeque<Page> getHistory() {
        return history;
    }

    public void setHistory(final ArrayDeque<Page> history) {
        this.history = history;
    }

    public ArrayDeque<String> getMovieTitles() {
        return movieTitles;
    }

    public void setMovieTitles(final ArrayDeque<String> movieTitles) {
        this.movieTitles = movieTitles;
    }

    public Movie getCurrentMovie() {
        return currentMovie;
    }

    public void setCurrentMovie(final Movie currentMovie) {
        this.currentMovie = currentMovie;
    }

    /**
     * Deep-copy the list of available movies
     * to the current user
     */
    public void deepCopyFilteredMovies() {
        this.filteredMovies = new ArrayList<>();
        for (Movie movie : this.movies) {
            if (!movie.isBannedForTheUser(currentUser.getCredentials().getCountry())) {
                filteredMovies.add(movie);
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

    /**
     * add a movie to the database
     * @param addedMovie the movie to be added
     */
    public void addMovie(final JsonNode addedMovie) {

        JsonNode movieData = addedMovie.get("addedMovie");
        ObjectMapper mapper = new ObjectMapper();
        Movie movie = mapper.convertValue(movieData, Movie.class);

        // the movie already exists in the database
        if (this.getMovie(this.movies, movie.getName()) != null) {


            Writer.getInstance().addOutput("Error", new ArrayList<>(),
                    null);
        } else {
            // the movie does not exist in the database
            // add the movie to the database
            movies.add(movie);


            // notify the users

            for (String genre: movie.getGenres()) {
                switch (genre) {
                    case "Action" -> {
                        Action.notify(movie.getName());
                    }
                    case "Comedy" -> {
                        Comedy.notify(movie.getName());
                    }
                    case "Crime" -> {
                        Crime.notify(movie.getName());
                    }
                    case "Drama" -> {
                        Drama.notify(movie.getName());
                    }
                    case "Thriller" -> {
                        Thriller.notify(movie.getName());
                    }
                    default -> {

                    }
                }
            }
        }

    }

    /**
     * remove a movie from the database
     * @param removedMovie the movie to be removed
     */
    public void removeMovie(final ObjectNode removedMovie) {

        String movieTitle = removedMovie.get("deletedMovie").asText();
        Movie movie = getMovie(movies, movieTitle);

        // the movie does not exist in the database
        if (movie == null) {
            Writer.getInstance().addOutput("Error", new ArrayList<>(),
                    null);
        } else {
            for (User user : users) {
                if (user.hasPurchasedMovie(movie)) {
                    // delete the movie from the database
                    movies.remove(movie);
                    // refund the user
                    refund(user);
                    // delete the movie from the user's
                    // list of movies
                    user.deleteMovie(movie);
                    // notify the user of the deletion
                    // of the movie
                    Notification notification = new Notification.NotificationBuilder("DELETE").
                                                    movieName(movie.getName()).build();
                    user.addNotification(notification);
                }
            }
            movies.remove(movie);
        }


    }

    private void refund(final User user) {
        if (user.getCredentials().getAccountType().equals("standard")) {
            user.setTokensCount(user.getTokensCount() + 2);
        } else {
            user.setNumFreePremiumMovies(user.getNumFreePremiumMovies() + 1);
        }
    }

    /**
     * recommend a movie to the logged in
     * premium user
     * @return the recommended movie
     */
    public Movie recommendMovie() {


        // sort the genres by likes then lexicographically
        Comparator<Map.Entry<String, Integer>>  comparator = (a, b) -> {
            if (a.getValue().compareTo(b.getValue()) == 0) {
                return a.getKey().compareTo(b.getKey());
            } else {
                return a.getValue().compareTo(b.getValue());
            }
        };

        List<Map.Entry<String, Integer>> sortedGenres = currentUser.getLikedGenres().entrySet()
                .stream().sorted(comparator).toList();


        // iterate through all the genres
        // until a  movie to be recommended
        // is found
        for (Map.Entry<String, Integer> sortedGenre : sortedGenres) {

            // get all the movies of a certain genre

            String genre = sortedGenre.getKey();
            List<Movie> genreFilteredMovies = this.filterByGenre(genre);

            // there are movies that have a specific genre
            // and have not been watched by the user
            if (!genreFilteredMovies.isEmpty()) {
                // sort the movies descending
                // by the number of likes
                genreFilteredMovies.sort((o1, o2) -> (o2.getNumLikes() - o1.getNumLikes()));

                return genreFilteredMovies.get(0);
            }


        }

        // no movie has been found
        return null;

    }



    private List<Movie> filterByGenre(final String genre) {

        List<Movie> genreFiltered = new ArrayList<>();

        for (Movie movie : filteredMovies) {

            // the movie has a specific genre and
            // has not been watched by the user
            if (movie.getGenres().contains(genre)
                && !currentUser.getWatchedMovies().contains(movie)) {
                genreFiltered.add(movie);
            }
        }

        return genreFiltered;
    }



}
