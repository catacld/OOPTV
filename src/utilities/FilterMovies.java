package utilities;

import classes.Movie;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FilterMovies {


    // filter a list of movies by an actor
    public static List<Movie> filterByActor(List<Movie> moviesList, String actor) {
        for (int i = 0; i < moviesList.size(); i++) {
            if (!moviesList.get(i).getActors().contains(actor)) {
                moviesList.remove(i);
                i--;
            }
        }

        return moviesList;
    }

    // filter a list of movies by a genre
    public static List<Movie> filterByGenre(List<Movie> moviesList, String genre) {
        for (int i = 0; i < moviesList.size(); i++) {
            if (!moviesList.get(i).getGenres().contains(genre)) {
                moviesList.remove(i);
                i--;
            }
        }

        return moviesList;
    }

    // filter a list of movies by title
    public static List<Movie> search(List<Movie> moviesList, String startsWith) {

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
    public static List<Movie> sortByBoth(List<Movie> moviesList, String durationOrder, String ratingOrder) {

        // classic bubble-sort when sorting by both parameters

        for (int i = 0; i < moviesList.size() - 1; i++) {
            for (int j = i + 1; j < moviesList.size(); j++) {

                if (durationOrder.equals("decreasing")) {

                    if (moviesList.get(i).getDuration() <
                            moviesList.get(j).getDuration()) {
                        Collections.swap(moviesList, i, j);
                    } else if (moviesList.get(i).getDuration() == moviesList.get(j).getDuration()) {
                        if (ratingOrder.equals("increasing") &&
                                moviesList.get(i).getRating() > moviesList.get(j).getRating()) {
                            Collections.swap(moviesList, i, j);
                        } else if (ratingOrder.equals("decreasing") &&
                                moviesList.get(i).getRating() < moviesList.get(j).getRating()) {
                            Collections.swap(moviesList, i, j);
                        }
                    }
                } else if (durationOrder.equals("increasing")) {
                    if (moviesList.get(i).getDuration() >
                            moviesList.get(j).getDuration()) {
                        Collections.swap(moviesList, i, j);
                    } else if (moviesList.get(i).getDuration() == moviesList.get(j).getDuration()) {
                        if (ratingOrder.equals("increasing") &&
                                moviesList.get(i).getRating() > moviesList.get(j).getRating()) {
                            Collections.swap(moviesList, i, j);
                        } else if (ratingOrder.equals("decreasing") &&
                                moviesList.get(i).getRating() < moviesList.get(j).getRating()) {
                            Collections.swap(moviesList, i, j);
                        }
                    }
                }
            }
        }

        return moviesList;

    }

    // sort the list of movies
    // by rating
    public static List<Movie> sortByRating(List<Movie> moviesList, String ratingOrder) {

        Movie[] moviesArray = new Movie[moviesList.size()];

        for (int y = 0; y < moviesList.size(); y++) {
            moviesArray[y] = moviesList.get(y);
        }

        mergeSort(moviesArray, "rating", ratingOrder);

        return Arrays.asList(moviesArray);
    }

    // sort the list of movies
    // by duration
    public static List<Movie> sortByDuration(List<Movie> moviesList, String durationOrder) {
        Movie[] moviesArray = new Movie[moviesList.size()];

        for (int y = 0; y < moviesList.size(); y++) {
            moviesArray[y] = moviesList.get(y);
        }

        mergeSort(moviesArray, "duration", durationOrder);

        return Arrays.asList(moviesArray);

    }

    private static void mergeSort(Movie[] a, String sortBy, String order) {
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
            Movie[] a, Movie[] l, Movie[] r, int left, int right, String order) {

        int i = 0, j = 0, k = 0;

        while (i < left && j < right) {
            if (order.equals("increasing")) {
                if (l[i].getRating() <= r[j].getRating()) {
                    a[k++] = l[i++];
                }
                else {
                    a[k++] = r[j++];
                }
            } else {
                if (l[i].getRating() >= r[j].getRating()) {
                    a[k++] = l[i++];
                }
                else {
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
            Movie[] a, Movie[] l, Movie[] r, int left, int right, String order) {

        int i = 0, j = 0, k = 0;

        while (i < left && j < right) {
            if (order.equals("increasing")) {
                if (l[i].getDuration() <= r[j].getDuration()) {
                    a[k++] = l[i++];
                }
                else {
                    a[k++] = r[j++];
                }
            } else {
                if (l[i].getDuration() >= r[j].getDuration()) {
                    a[k++] = l[i++];
                }
                else {
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
