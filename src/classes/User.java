package classes;

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
}
