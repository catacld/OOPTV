package utilities;

import classes.Movie;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FilterMovies {



    /**
     * Filter a list of movies by an actor
     * @param moviesList the list of movies
     * @param actor the name of the actor
     * @return the filtered list
     */
    public static List<Movie> filterByActor(final List<Movie> moviesList,
                                            final String actor) {
        for (int i = 0; i < moviesList.size(); i++) {
            if (!moviesList.get(i).getActors().contains(actor)) {
                moviesList.remove(i);
                i--;
            }
        }

        return moviesList;
    }

    /**
     * Filter a list of movies by a genre
     * @param moviesList the list of movies
     * @param genre the genre
     * @return the filtered list
     */
    public static List<Movie> filterByGenre(final List<Movie> moviesList,
                                            final String genre) {
        for (int i = 0; i < moviesList.size(); i++) {
            if (!moviesList.get(i).getGenres().contains(genre)) {
                moviesList.remove(i);
                i--;
            }
        }

        return moviesList;
    }

    // filter a list of movies by title
    /**
     * Filter a list of movies by title
     * @param moviesList the list of movies
     * @param startsWith the sequence that the title of
     *                   the movie must start with
     * @return the filtered list
     */
    public static List<Movie> search(final List<Movie> moviesList,
                                     final String startsWith) {

        for (int i = 0; i < moviesList.size(); i++) {
            if (!moviesList.get(i).getName().startsWith(startsWith)) {
                moviesList.remove(i);
                i--;
            }
        }

        return moviesList;
    }


    // sort the list of movies by both
    // duration and rating
    /**
     * Sort a list of movies by duration
     * and rating
     * @param moviesList the list of movies
     * @param durationOrder the order in which the list will
     *                      be sorted by duration
     * @param ratingOrder the order in which the list will
     *                    be sorted by rating
     * @return the filtered list
     */
    public static List<Movie> sortByBoth(final List<Movie> moviesList,
                                         final String durationOrder,
                                         final String ratingOrder) {

        // sort by increasing duration
        if (durationOrder.equals("increasing")) {
            moviesList.sort((o1, o2) -> {
                // if the movies are equal by duration
                // sort by rating
                if (o1.getDuration() == o2.getDuration()) {
                    if (ratingOrder.equals("increasing")) {
                        // sort by increasing rating
                        return (int) (o1.getRating() - o2.getRating());
                    } else {
                        // sort by decreasing rating
                        return (int) (-1 * (o1.getRating() - o2.getRating()));
                    }
                } else {
                    return o1.getDuration() - o2.getDuration();
                }
            });
        } else {
            // sort by decreasing duration
            moviesList.sort((o1, o2) -> {
                // if the movies are equal by duration
                // sort by rating
                if (o1.getDuration() == o2.getDuration()) {
                    if (ratingOrder.equals("increasing")) {
                        // sort by increasing rating
                        return (int) (o1.getRating() - o2.getRating());
                    } else {
                        // sort by decreasing rating
                        return (int) (-1 * (o1.getRating() - o2.getRating()));
                    }
                } else {
                    return (-1) * (o1.getDuration() - o2.getDuration());
                }
            });
        }

        return moviesList;

    }


    /**
     * Sort a list of movies by rating
     * @param moviesList the list of movies
     * @param ratingOrder the order in which the list
     *                    will be sorted by rating
     * @return the filtered list
     */
    public static List<Movie> sortByRating(final List<Movie> moviesList,
                                           final String ratingOrder) {


        moviesList.sort((o1, o2) -> (int) (o1.getRating() - o2.getRating()));

        if (ratingOrder.equals("decreasing")) {
            Collections.reverse(moviesList);
        }

        return moviesList;

    }


    /**
     * Sort a list of movies by duration
     * @param moviesList the list of movies
     * @param durationOrder the order in which the list
     *                    will be sorted by duration
     * @return the filtered list
     */
    public static List<Movie> sortByDuration(final List<Movie> moviesList,
                                             final String durationOrder) {


        moviesList.sort(Comparator.comparingInt(Movie::getDuration));

        if (durationOrder.equals("decreasing")) {
            Collections.reverse(moviesList);
        }

        return moviesList;

    }


}
