package classes;

import com.fasterxml.jackson.annotation.JsonIgnore;
import constants.Constants;
import data.Database;
import genres.Action;
import genres.Comedy;
import genres.Crime;
import genres.Drama;
import genres.Thriller;
import ioclasses.Writer;
import utilities.CheckAction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class User {
    private Credentials credentials;

    private int tokensCount;

    private int numFreePremiumMovies;

    private List<Movie> purchasedMovies;

    private List<Movie> watchedMovies;

    private List<Movie> likedMovies;

    private List<Movie> ratedMovies;

    private List<Notification> notifications;

    @JsonIgnore
    private List<String> subscribedGenres;

    @JsonIgnore
    // used for recommendations based on the
    // user's likes
    private HashMap<String, Integer> likedGenres;

    public User(final Credentials credentials) {
        this.credentials = credentials;
        purchasedMovies = new ArrayList<>();
        watchedMovies = new ArrayList<>();
        likedMovies = new ArrayList<>();
        ratedMovies = new ArrayList<>();
        notifications = new ArrayList<>();
        subscribedGenres = new ArrayList<>();
        likedGenres = new HashMap<>();
        numFreePremiumMovies = Constants.FIFTEEN;
    }

    public User() {
        purchasedMovies = new ArrayList<>();
        watchedMovies = new ArrayList<>();
        likedMovies = new ArrayList<>();
        ratedMovies = new ArrayList<>();
        notifications = new ArrayList<>();
        subscribedGenres = new ArrayList<>();
        likedGenres = new HashMap<>();
        numFreePremiumMovies = Constants.FIFTEEN;
    }

    public final Credentials getCredentials() {
        return this.credentials;
    }

    public final void setCredentials(final Credentials credentials) {
        this.credentials = credentials;
    }

    public final int getTokensCount() {
        return tokensCount;
    }

    public final void setTokensCount(final int tokensCount) {
        this.tokensCount = tokensCount;
    }

    public final int getNumFreePremiumMovies() {
        return numFreePremiumMovies;
    }

    public final void setNumFreePremiumMovies(final int numFreePremiumMovies) {
        this.numFreePremiumMovies = numFreePremiumMovies;
    }

    public final List<Movie> getPurchasedMovies() {
        return purchasedMovies;
    }

    public final void setPurchasedMovies(final List<Movie> purchasedMovies) {
        this.purchasedMovies = purchasedMovies;
    }

    public final List<Movie> getWatchedMovies() {
        return watchedMovies;
    }

    public final void setWatchedMovies(final List<Movie> watchedMovies) {
        this.watchedMovies = watchedMovies;
    }

    public final List<Movie> getLikedMovies() {
        return likedMovies;
    }

    public final void setLikedMovies(final List<Movie> likedMovies) {
        this.likedMovies = likedMovies;
    }

    public final List<Movie> getRatedMovies() {
        return ratedMovies;
    }

    public final void setRatedMovies(final List<Movie> ratedMovies) {
        this.ratedMovies = ratedMovies;
    }


    public final void setNotifications(final List<Notification> notifications) {
        this.notifications = notifications;
    }


    public final List<String> getSubscribedGenres() {
        return subscribedGenres;
    }


    public final List<Notification> getNotifications() {
        return notifications;
    }


    public final HashMap<String, Integer> getLikedGenres() {
        return likedGenres;
    }


    /**
     * Purchase the movie given as a parameter
     * @param movie the movie to be purchased
     */
    public final void purchase(final Movie movie) {


        // the user has already purchased the movie
        if (this.purchasedMovies.contains(movie)) {
            // write an error
            Writer.getInstance().addOutput("Error", new ArrayList<>(), null);
        } else {
            // add the movie to the user's purchased movies
            this.purchasedMovies.add(movie);

            // check if the user can get any free movies
            if (this.canGetAFreeMovie()) {
                this.numFreePremiumMovies--;
            } else {
                // if not then subtract 2 tokens for the movie
                this.tokensCount -= 2;
            }

            // write the output to the file
            List<Movie> movieToPrint = new ArrayList<>();
            movieToPrint.add(movie);
            Writer.getInstance().addOutput(null,
                    movieToPrint, Database.getInstance().getCurrentUser());
        }


    }

    /**
     * Watch the movie given as a parameter
     * @param movie the movie to be watched
     */
    public final void watch(final Movie movie) {

        // check if the movie has been purchased
        if (this.purchasedMovies.contains(movie)) {

            // the user watches the movie for the
            // first time
            if (!this.watchedMovies.contains(movie)) {
                //add it to the list of watched movies
                this.watchedMovies.add(movie);


                // write the output
                List<Movie> movieToPrint = new ArrayList<>();
                movieToPrint.add(movie);
                Writer.getInstance().addOutput(null,
                        movieToPrint, Database.getInstance().getCurrentUser());
            }
        } else {
            // the movie has not been purchased
            // write an error
            Writer.getInstance().addOutput("Error", new ArrayList<>(), null);
        }
    }

    /**
     * Like the movie given as a parameter
     * @param movie the movie to be liked
     */
    public final void like(final Movie movie) {

        // check if the movie has been watched
        if (this.watchedMovies.contains(movie)) {

            // add the movie to the user's liked list
            this.likedMovies.add(movie);

            // increase the movie's number of likes
            movie.setNumLikes(movie.getNumLikes() + 1);


            // update the likes of the movie's genres
            // for the user
            for (String genre : movie.getGenres()) {
                likedGenres.put(genre, likedGenres.getOrDefault(genre, 0) + 1);
            }

            // write the output
            List<Movie> movieToPrint = new ArrayList<>();
            movieToPrint.add(movie);
            Writer.getInstance().addOutput(null,
                    movieToPrint, Database.getInstance().getCurrentUser());

        } else {
            // the movie has not been watched
            // write an error
            Writer.getInstance().addOutput("Error", new ArrayList<>(), null);
        }
    }

    /**
     * Rate the movie given as a parameter
     * @param movie the movie to be rated
     */
    public final void rate(final Movie movie, final Double rating) {

        // check if the movie has been watched
        if (this.watchedMovies.contains(movie)) {


            // check if the rating is between 1 and 5
            if (rating > Constants.FIVE || rating < 1) {
                Writer.getInstance().addOutput("Error", new ArrayList<>(), null);
            } else {
                // add the movie to the user's rated list
                // if the user rates it for the first
                // time
                if (!this.ratedMovies.contains(movie)) {
                    this.ratedMovies.add(movie);
                }

                // rate the movie in the user's list
                movie.rate(this, rating);

                // write the output
                List<Movie> movieToPrint = new ArrayList<>();
                movieToPrint.add(movie);
                Writer.getInstance().addOutput(null,
                        movieToPrint, Database.getInstance().getCurrentUser());
            }
        } else {
            // the movie has not been watched
            Writer.getInstance().addOutput("Error", new ArrayList<>(), null);
        }
    }

    private boolean canGetAFreeMovie() {
        if (this.getCredentials().getAccountType().equals("standard")) {
            return false;
        } else {
            return (numFreePremiumMovies != 0);
        }
    }

    /**
     * subscribe, if possible, to a genre
     * of movies
     * @param genre the genre to subscribe to
     */
    public final void subscribe(final String genre) {


        if (!CheckAction.canSubscribe(this, Database.getInstance().getCurrentMovie(), genre)) {
            Writer.getInstance().addOutput("Error", new ArrayList<>(),
                    null);
        } else {
            this.subscribedGenres.add(genre);
            switch (genre) {
                case "Action" -> {
                    Action.addSubscriber(this);
                }
                case "Comedy" -> {
                    Comedy.addSubscriber(this);
                }
                case "Crime" -> {
                    Crime.addSubscriber(this);
                }
                case "Drama" -> {
                    Drama.addSubscriber(this);
                }
                case "Thriller" -> {
                    Thriller.addSubscriber(this);
                }
                default -> {

                }
            }
        }

    }

    /**
     * add a notification to the user's
     * notifications queue
     * @param notification the notification to
     *                     be added
     */
    public void addNotification(final Notification notification) {
        if (!this.alreadyNotified(notification)) {
            notifications.add(notification);
        }
    }




    /**
     * check if the user has already been notified
     * of the addition of a new movie
     * @param notification the notification to be searched for
     * @return true if the user has already been notified
     *          false otherwise
     */
    private boolean alreadyNotified(final Notification notification) {
        for (Notification n : notifications) {
            if (n.equals(notification)) {
                return true;
            }
        }
        return false;
    }

    /**
     * check if the user has purchased a movie
     * to refund it if it is removed
     * from the database
     * @param movie the movie to be searched for
     * @return true if the user owns the movie
     *          false otherwise
     */
    public boolean hasPurchasedMovie(final Movie movie) {
        return purchasedMovies.contains(movie);
    }

    /**
     * deletes a movie from the user's lists
     * @param movie the movie to be deleted
     */
    public final void deleteMovie(final Movie movie) {
        purchasedMovies.remove(movie);
        watchedMovies.remove(movie);
        likedMovies.remove(movie);
        ratedMovies.remove(movie);
    }

    /**
     * check if a user has a premium account
     * @return true if the user's account is premium
     *          false otherwise
     */
    @JsonIgnore
    public boolean isPremium() {
        return credentials.getAccountType().equals("premium");
    }
}
