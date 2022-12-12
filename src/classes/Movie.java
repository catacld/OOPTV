package classes;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.node.DecimalNode;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Formatter;

import static java.lang.Math.round;


public class Movie {

    private final DecimalFormat formatter = new DecimalFormat("#0.00");
    private String name;
    private int year;
    private int duration;
    private ArrayList<String> genres;
    private ArrayList<String> actors;
    private ArrayList<String> countriesBanned;

    private int numLikes;

    private Double rating;

    private int numRatings;

    public Movie() {
        rating = 0.00;
    }

    // deep-copy constructor
    public Movie(Movie movie) {
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

    public String toString() {
        return "classes.Movie{name='" + this.name + "', year=" + this.year + ", duration=" + this.duration + ", genres=" + this.genres + ", actors=" + this.actors + ", countriesBanned=" + this.countriesBanned + "}";
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getYear() {
        return this.year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getDuration() {
        return this.duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public ArrayList<String> getGenres() {
        return this.genres;
    }

    public void setGenres(ArrayList<String> genres) {
        this.genres = genres;
    }

    public ArrayList<String> getActors() {
        return this.actors;
    }

    public void setActors(ArrayList<String> actors) {
        this.actors = actors;
    }

    public ArrayList<String> getCountriesBanned() {
        return this.countriesBanned;
    }

    public void setCountriesBanned(ArrayList<String> countriesBanned) {
        this.countriesBanned = countriesBanned;
    }

    public int getNumLikes() {
        return numLikes;
    }

    public void setNumLikes(int numLikes) {
        this.numLikes = numLikes;
    }

    public Double getRating() {
        Double d = 0.00;
        String str = String.format("%.2f",d);
        //Double newD = BigDecimal.valueOf(rating).setScale(2, RoundingMode.DOWN).doubleValue();
        return Double.valueOf(str);


    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public int getNumRatings() {
        return numRatings;
    }

    public void setNumRatings(int numRatings) {
        this.numRatings = numRatings;
    }

    // check if the movies is banned for a
    // specific user
    public boolean isBannedForTheUser(String userCountry) {
        for (String country : this.countriesBanned ) {
            if (country.equals(userCountry)) {
                return true;
            }
        }

        return false;
    }

}
