package classes;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.HashMap;

public class Movie {

    private String name;

    private String year;
    private int duration;
    private ArrayList<String> genres;
    private ArrayList<String> actors;
    private ArrayList<String> countriesBanned;

    @JsonIgnore
    private HashMap<User, Double> ratings;

    private int numLikes;

    private Double rating;

    @JsonIgnore
    private double ratingSum;

    private int numRatings;

    public Movie() {
        rating = 0.00;
        this.ratingSum = 0.00;
        this.ratings = new HashMap<>();
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
        this.ratingSum = movie.getRatingSum();
        this.ratings = new HashMap<>();
    }

    public final String getName() {
        return this.name;
    }

    public final void setName(final String name) {
        this.name = name;
    }

    public final String getYear() {
        return this.year;
    }

    public final void setYear(final String year) {
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

    public final double getRatingSum() {
        return ratingSum;
    }

    public final void setRatingSum(final double ratingSum) {
        this.ratingSum = ratingSum;
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
    public final void rate(final User user, final Double givenRating) {
        // rate the movie as the user
        ratings.put(user, givenRating);

        // calculate the rating
        ratingSum = 0;
        ratings.forEach(
                (key, value) -> ratingSum += value);

        this.numRatings = ratings.size();
        this.rating = ratingSum / numRatings;
    }





}
