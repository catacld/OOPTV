package utilities;

import classes.Movie;

import java.util.Arrays;
import java.util.Collections;
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

        // classic bubble-sort when sorting by both parameters

        for (int i = 0; i < moviesList.size() - 1; i++) {
            for (int j = i + 1; j < moviesList.size(); j++) {

                if (durationOrder.equals("decreasing")) {

                    if (moviesList.get(i).getDuration()
                            < moviesList.get(j).getDuration()) {
                        Collections.swap(moviesList, i, j);
                    } else if (moviesList.get(i).getDuration() == moviesList.get(j).getDuration()) {
                        if (ratingOrder.equals("increasing")
                                && moviesList.get(i).getRating()
                                > moviesList.get(j).getRating()) {
                            Collections.swap(moviesList, i, j);
                        } else if (ratingOrder.equals("decreasing")
                                && moviesList.get(i).getRating()
                                < moviesList.get(j).getRating()) {
                            Collections.swap(moviesList, i, j);
                        }
                    }
                } else if (durationOrder.equals("increasing")) {
                    if (moviesList.get(i).getDuration()
                            > moviesList.get(j).getDuration()) {
                        Collections.swap(moviesList, i, j);
                    } else if (moviesList.get(i).getDuration() == moviesList.get(j).getDuration()) {
                        if (ratingOrder.equals("increasing")
                                && moviesList.get(i).getRating()
                                > moviesList.get(j).getRating()) {
                            Collections.swap(moviesList, i, j);
                        } else if (ratingOrder.equals("decreasing")
                                && moviesList.get(i).getRating()
                                < moviesList.get(j).getRating()) {
                            Collections.swap(moviesList, i, j);
                        }
                    }
                }
            }
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

        Movie[] moviesArray = new Movie[moviesList.size()];

        for (int y = 0; y < moviesList.size(); y++) {
            moviesArray[y] = moviesList.get(y);
        }

        mergeSort(moviesArray, "rating", ratingOrder);

        return Arrays.asList(moviesArray);
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
        Movie[] moviesArray = new Movie[moviesList.size()];

        for (int y = 0; y < moviesList.size(); y++) {
            moviesArray[y] = moviesList.get(y);
        }

        mergeSort(moviesArray, "duration", durationOrder);

        return Arrays.asList(moviesArray);

    }

    private static void mergeSort(final Movie[] a,
                                  final String sortBy,
                                  final String order) {
        if (a.length < 2) {
            return;
        }
        int mid = a.length / 2;
        Movie[] l = new Movie[mid];
        Movie[] r = new Movie[a.length - mid];

        for (int i = 0; i < mid; i++) {
            l[i] = a[i];
        }
        for (int i = mid; i < a.length; i++) {
            r[i - mid] = a[i];
        }

        mergeSort(l, sortBy, order);
        mergeSort(r, sortBy, order);

        if (sortBy.equals("duration")) {
            mergeSortDuration(a, l, r, mid, a.length - mid, order);
        } else {
            mergeSortRating(a, l, r, mid, a.length - mid, order);
        }



    }

    private static void mergeSortRating(
            final Movie[] a, final Movie[] l, final Movie[] r,
            final int left, final int right, final String order) {

        int i = 0, j = 0, k = 0;

        while (i < left && j < right) {
            if (order.equals("increasing")) {
                if (l[i].getRating() <= r[j].getRating()) {
                    a[k++] = l[i++];
                } else {
                    a[k++] = r[j++];
                }
            } else {
                if (l[i].getRating() >= r[j].getRating()) {
                    a[k++] = l[i++];
                } else {
                    a[k++] = r[j++];
                }
            }

        }
        while (i < left) {
            a[k++] = l[i++];
        }
        while (j < right) {
            a[k++] = r[j++];
        }
    }

    private static void mergeSortDuration(
            final Movie[] a, final Movie[] l, final Movie[] r,
            final int left, final int right, final String order) {

        int i = 0, j = 0, k = 0;

        while (i < left && j < right) {
            if (order.equals("increasing")) {
                if (l[i].getDuration() <= r[j].getDuration()) {
                    a[k++] = l[i++];
                } else {
                    a[k++] = r[j++];
                }
            } else {
                if (l[i].getDuration() >= r[j].getDuration()) {
                    a[k++] = l[i++];
                } else {
                    a[k++] = r[j++];
                }
            }

        }
        while (i < left) {
            a[k++] = l[i++];
        }
        while (j < right) {
            a[k++] = r[j++];
        }
    }


}
