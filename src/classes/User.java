package classes;

import data.Database;
import ioclasses.Writer;

import java.util.ArrayList;
import java.util.List;



public class User {
    private Credentials credentials;

    private int tokensCount;

    private int numFreePremiumMovies;

    private List<Movie> purchasedMovies;

    private List<Movie> watchedMovies;

    private List<Movie> likedMovies;

    private List<Movie> ratedMovies;

    public User(final Credentials credentials) {
        this.credentials = credentials;
        purchasedMovies = new ArrayList<>();
        watchedMovies = new ArrayList<>();
        likedMovies = new ArrayList<>();
        ratedMovies = new ArrayList<>();
        numFreePremiumMovies = 15;
    }

    public User() {
        purchasedMovies = new ArrayList<>();
        watchedMovies = new ArrayList<>();
        likedMovies = new ArrayList<>();
        ratedMovies = new ArrayList<>();
        numFreePremiumMovies = 15;
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



    /**
     * Purchase the movie given as a parameter
     * @param movie the movie to be purchased
     */
    public final void purchase(final Movie movie) {

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

    /**
     * Watch the movie given as a parameter
     * @param movie the movie to be watched
     */
    public final void watch(final Movie movie) {

        // check if the movie has been purchased
        if (this.purchasedMovies.contains(movie)) {

            // add the movie to the user's watched list
            this.watchedMovies.add(movie);

            // write the output
            List<Movie> movieToPrint = new ArrayList<>();
            movieToPrint.add(movie);
            Writer.getInstance().addOutput(null,
                    movieToPrint, Database.getInstance().getCurrentUser());
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


            // update the movie's number of likes in the global list
            Movie universalMovie = Database.getInstance().getMovie(Database.
                    getInstance().getMovies(), movie.getName());
            assert universalMovie != null;
            universalMovie.setNumLikes(universalMovie.getNumLikes() + 1);

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
            if (rating > 5 || rating < 1) {
                Writer.getInstance().addOutput("Error", new ArrayList<>(), null);
            } else {
                // add the movie to the user's liked list
                this.ratedMovies.add(movie);

                // rate the movie in the user's list
                movie.rate(rating);

                // rate the movie in the global list
                Movie universalMovie = Database.getInstance().getMovie(
                        Database.getInstance().getMovies(), movie.getName());
                assert universalMovie != null;
                universalMovie.rate(rating);

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
}
