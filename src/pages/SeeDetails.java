package pages;

import classes.Database;
import classes.Movie;
import classes.Writer;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;
import java.util.List;

public class SeeDetails implements Page{

    private final Movie movie;

    private ArrayList<String> destinationPages;

    private ArrayList<String> onPageActions;



    public SeeDetails(Movie movie) {
        this.movie = movie;

        destinationPages = new ArrayList<>();
        destinationPages.add("homepage autentificat");
        destinationPages.add("movies");
        destinationPages.add("upgrades");
        destinationPages.add("logout");
        destinationPages.add("see details");


        onPageActions = new ArrayList<>();
        onPageActions.add("purchase");
        onPageActions.add("watch");
        onPageActions.add("like");
        onPageActions.add("rate");

    }

    @Override
    public Page changePage(ObjectNode actionDetails) {
        String destinationPage = actionDetails.get("page").asText();

        // check if the destination page is reachable
        // while on the "see details" page
        boolean valid = canExecuteAction(destinationPage,destinationPages);

        if (!valid) {
            // the destination is not reachable
            Writer.getInstance().addOutput("Error", new ArrayList<>(), null);
        } else {
            // the destination is reachable

            switch (destinationPage) {
                case "movies" -> {
                    // reset the filtered list
                    Database.getInstance().deepCopyFilteredMovies(Database.getInstance().getCurrentUser());
                    Writer.getInstance().addOutput(null, Database.getInstance().getMovieList(),
                            Database.getInstance().getCurrentUser());
                    return new Movies();
                }
                case "upgrades" -> {
                    return new Upgrades();
                }
                case "homepage autentificat" -> {
                    Writer.getInstance().addOutput(null, new ArrayList<>(), Database.getInstance().getCurrentUser());
                    return new AuthenticatedHome();
                }
                case "logout" -> {
                    Database.getInstance().setCurrentUser(null);
                    Database.getInstance().setMovieList(null);
                    return UnauthenticatedHome.getInstance();
                }
            }
        }
        return this;
    }

    @Override
    public Page onPage(ObjectNode actionDetails) {
        String action = actionDetails.get("feature").asText();

        // check if the action can be executed while
        // on the login page
        boolean valid = canExecuteAction(action, onPageActions);


        if (!valid) {
            // the action can not be executed
            //TODO add error output
            Writer.getInstance().addOutput("Error", new ArrayList<>(),null);
        } else {
            // the action can be executed

            switch (action) {
                case "purchase" -> {
                    Database.getInstance().getCurrentUser().getPurchasedMovies().add(this.movie);
                    if (Database.getInstance().getCurrentUser().getCredentials().getAccountType().equals("premium") &&
                        Database.getInstance().getCurrentUser().getNumFreePremiumMovies() != 0) {
                        Database.getInstance().getCurrentUser().setNumFreePremiumMovies(
                                Database.getInstance().getCurrentUser().getNumFreePremiumMovies() - 1);
                    } else {
                        Database.getInstance().getCurrentUser().setTokensCount(
                                Database.getInstance().getCurrentUser().getTokensCount() - 2);
                    }
                    List<Movie> movieToPrint = new ArrayList<>();
                    movieToPrint.add(this.movie);
                    Writer.getInstance().addOutput(null,
                            movieToPrint, Database.getInstance().getCurrentUser());

                    return this;
                }
                case "watch" -> {
                    if (Database.getInstance().getMovie(Database.getInstance().getCurrentUser().getPurchasedMovies(), this.movie.getName()) != null) {
                        Database.getInstance().getCurrentUser().getWatchedMovies().add(this.movie);
                        List<Movie> movieToPrint = new ArrayList<>();
                        movieToPrint.add(this.movie);
                        Writer.getInstance().addOutput(null,
                                movieToPrint, Database.getInstance().getCurrentUser());
                    } else {
                        Writer.getInstance().addOutput("Error", new ArrayList<>(),null);
                    }
                    return this;
                }
                case "like" -> {
                    if (Database.getInstance().getMovie(Database.getInstance().getCurrentUser().getWatchedMovies(), this.movie.getName()) != null) {
                        Database.getInstance().getCurrentUser().getLikedMovies().add(this.movie);
                        this.movie.setNumLikes(this.movie.getNumLikes() + 1);
                        Movie universalMovie = Database.getInstance().getMovie(Database.getInstance().getMovies(), this.movie.getName());
                        universalMovie.setNumLikes(universalMovie.getNumLikes() + 1);
                        List<Movie> movieToPrint = new ArrayList<>();
                        movieToPrint.add(this.movie);
                        Writer.getInstance().addOutput(null,
                                movieToPrint, Database.getInstance().getCurrentUser());

                    } else {
                        Writer.getInstance().addOutput("Error", new ArrayList<>(),null);
                    }
                    return this;
                }
                case "rate" -> {
                    if (Database.getInstance().getMovie(Database.getInstance().getCurrentUser().getWatchedMovies(), this.movie.getName()) != null) {

                        String rating = actionDetails.get("rate").asText();

                        if (Double.parseDouble(rating) > 5 || Double.parseDouble(rating) < 1) {
                            Writer.getInstance().addOutput("Error", new ArrayList<>(),null);
                        } else {
                            Database.getInstance().getCurrentUser().getRatedMovies().add(this.movie);
                            //System.out.println(rating);
                            // TODO remove "if" when you found the bug
                            //if (rating != null) {
                            // modify the movie universally
                            // since i have two lists of movies
                            Movie universalMovie = Database.getInstance().getMovie(Database.getInstance().getMovies(), this.movie.getName());
                            universalMovie.rate(Double.valueOf(rating));
                            this.movie.rate(Double.valueOf(rating));
                            //}

                            List<Movie> movieToPrint = new ArrayList<>();
                            movieToPrint.add(this.movie);
                            Writer.getInstance().addOutput(null,
                                    movieToPrint, Database.getInstance().getCurrentUser());
                        }
                    } else {
                        Writer.getInstance().addOutput("Error", new ArrayList<>(),null);
                    }
                    return this;
                }
            }
        }
        return this;
    }


    // check if the action
    // can be executed
    private boolean canExecuteAction(String actionToExecute, ArrayList<String> possibleActions) {
        for (String action : possibleActions) {
            if (action.equals(actionToExecute)) {
                return true;
            }
        }
        return false;
    }
}
