package classes;

import java.util.ArrayList;

public class Movie {

    private String name;
    private int year;
    private int duration;
    private ArrayList<String> genres;
    private ArrayList<String> actors;
    private ArrayList<String> countriesBanned;

    private int numLikes;

    private Double rating;

    private double ratingSum;

    private int numRatings;

    public Movie() {
        rating = 0.00;
        this.ratingSum = 0.00;
    }

    // deep-copy constructor
    public Movie(final Movie movie) {
        this.name = movie.getName();
        this.year = movie.getYear();
        this.duration = movie.getDuration();
        this.genres = movie.getGenres();
        this.actors = movie.getActors();
        this.countriesBanned = movie.getCountriesBanned();
        this.numLikes = movie.getNumLikes();
        this.rating = movie.getRating();
        this.numRatings = movie.getNumRatings();
    }

    public final String getName() {
        return this.name;
    }

    public final void setName(final String name) {
        this.name = name;
    }

    public final int getYear() {
        return this.year;
    }

    public final void setYear(final int year) {
        this.year = year;
    }

    public final int getDuration() {
        return this.duration;
    }

    public final void setDuration(final int duration) {
        this.duration = duration;
    }

    public final ArrayList<String> getGenres() {
        return this.genres;
    }

    public final void setGenres(final ArrayList<String> genres) {
        this.genres = genres;
    }

    public final ArrayList<String> getActors() {
        return this.actors;
    }

    public final void setActors(final ArrayList<String> actors) {
        this.actors = actors;
    }

    public final ArrayList<String> getCountriesBanned() {
        return this.countriesBanned;
    }

    public final void setCountriesBanned(final ArrayList<String> countriesBanned) {
        this.countriesBanned = countriesBanned;
    }

    public final int getNumLikes() {
        return numLikes;
    }

    public final void setNumLikes(final int numLikes) {
        this.numLikes = numLikes;
    }

    public final Double getRating() {
        return this.rating;
    }

    public final void setRating(final Double rating) {
        this.rating = rating;
    }

    public final int getNumRatings() {
        return numRatings;
    }

    public final void setNumRatings(final int numRatings) {
        this.numRatings = numRatings;
    }

    /**
     * Check if the movie is banned for a specific suer
     * @param userCountry the user's country
     */
    public final boolean isBannedForTheUser(final String userCountry) {
        for (String country : this.countriesBanned) {
            if (country.equals(userCountry)) {
                return true;
            }
        }

        return false;
    }


    /**
     * Rate the movie
     * @param givenRating the rating given to the movie
     */
    public final void rate(final Double givenRating) {
        this.ratingSum += givenRating;
        this.numRatings++;
        this.rating = ratingSum / numRatings;
    }

}
