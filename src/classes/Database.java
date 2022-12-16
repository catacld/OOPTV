package classes;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Database {

    private static Database instance = null;

    private List<User> users;

    // all the movies from the database
    private List<Movie> movies;

    // a list on which filters and restrictions
    // will be applied
    private List<Movie> movieList;

    private User currentUser;

    private Database() {
    }

    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }

        return instance;
    }

    // check if the given credentials for login
    // are valid
    public User validLogin(String username, String password) {

        for (User user : users) {
            if (user.getCredentials().getPassword().equals(password) &&
                    user.getCredentials().getName().equals(username)) {
                currentUser = user;
                return user;
            }
        }

        return null;
    }

    // check if there is already a user registered
    // with a username
    public boolean checkUsernameAvailable(String username) {
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

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public List<Movie> getMovieList() {
        return movieList;
    }

    public void setMovieList(List<Movie> movieList) {
        this.movieList = movieList;
    }

    // the filtered movies list will be a deep copy
    // of the original list
    public void deepCopyFilteredMovies(User user) {
        this.movieList = new ArrayList<>();
        for (Movie movie : this.movies) {
            if (!movie.isBannedForTheUser(currentUser.getCredentials().getCountry())) {
                movieList.add(new Movie(movie));
            }

        }
    }

    // check if a movie is available for a user
    public Movie getMovie(List<Movie> moviesList, String movieName) {
        for (Movie movie : moviesList) {
            if (movie.getName().equals(movieName)) {
                return movie;
            }
        }

        return null;
    }

    public List<Movie> search(List<Movie> moviesList, String startsWith) {

        for (int i = 0; i < moviesList.size(); i++) {
            if (!moviesList.get(i).getName().startsWith(startsWith)) {
                moviesList.remove(i);
                i--;
            }
        }

        return moviesList;
    }


    public List<Movie> sortByRating(List<Movie> moviesList, String ratingOrder) {
            // classic bubble sort

            // convert the list to an array
            // so it can be sorted in place

        Movie[] moviesArray = new Movie[moviesList.size()];

        for (int y = 0; y < moviesList.size(); y++) {
            moviesArray[y] = moviesList.get(y);
        }

        mergeSort(moviesArray, "rating", ratingOrder);

        return Arrays.asList(moviesArray);
    }

    public List<Movie> sortByDuration(List<Movie> moviesList, String durationOrder) {
        Movie[] moviesArray = new Movie[moviesList.size()];

        for (int y = 0; y < moviesList.size(); y++) {
            moviesArray[y] = moviesList.get(y);
        }

        mergeSort(moviesArray, "duration", durationOrder);

        return Arrays.asList(moviesArray);

    }


    public List<Movie> sortByBoth(List<Movie> moviesList, String durationOrder, String ratingOrder) {

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

    public List<Movie> filterByActor(List<Movie> moviesList, String actor) {
        for (int i = 0; i < moviesList.size(); i++) {
            if (!moviesList.get(i).getActors().contains(actor)) {
                movieList.remove(i);
                i--;
            }
        }

        return moviesList;
    }

    public List<Movie> filterByGenre(List<Movie> moviesList, String genre) {
        for (int i = 0; i < moviesList.size(); i++) {
            if (!moviesList.get(i).getGenres().contains(genre)) {
                movieList.remove(i);
                i--;
            }
        }

        return moviesList;
    }


    public static void mergeSort(Movie[] a, String sortBy, String order) {
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

    public static void mergeSortRating(
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

    public static void mergeSortDuration(
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
