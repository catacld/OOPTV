package pages;

import utilities.CheckAction;
import data.Database;
import classes.Movie;
import ioclasses.Writer;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;
import java.util.List;

public class SeeDetails implements Page {

    private final Movie movie;

    private ArrayList<String> destinationPages;

    private ArrayList<String> onPageActions;



    public SeeDetails(final Movie movie) {
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

    /**
     * Change the page from the current page to
     * another page
     * @param actionDetails node where the details of the
     *                      action are stored
     * @return the destination page
     */
    @Override
    public Page changePage(final ObjectNode actionDetails) {
        String destinationPage = actionDetails.get("page").asText();

        // check if the action can be executed while
        // on this page
        boolean valid = CheckAction.canChangePage(destinationPage, destinationPages);

        if (!valid) {
            // the destination is not reachable
            return this;
        } else {
            // the destination is reachable

            switch (destinationPage) {
                case "movies" -> {

                    // reset the filtered list when the page changes
                    Database.getInstance().deepCopyFilteredMovies(
                            Database.getInstance().getCurrentUser());

                    Writer.getInstance().addOutput(null, Database.getInstance().getFilteredMovies(),
                            Database.getInstance().getCurrentUser());
                    return new Movies();
                }
                case "upgrades" -> {
                    return new Upgrades();
                }
                case "homepage autentificat" -> {
                    Writer.getInstance().addOutput(null, new ArrayList<>(),
                                                   Database.getInstance().getCurrentUser());
                    return new AuthenticatedHome();
                }
                case "logout" -> {
                    Database.getInstance().setCurrentUser(null);
                    Database.getInstance().setFilteredMovies(null);
                    return UnauthenticatedHome.getInstance();
                }
                default -> {
                    return this;
                }
            }
        }
    }

    /**
     * Execute an "on page" action
     * @param actionDetails node where the details of the
     *                      action are stored
     * @return the same page
     */
    @Override
    public Page onPage(final ObjectNode actionDetails) {
        String action = actionDetails.get("feature").asText();

        // check if the action can be executed while
        // on this page
        boolean valid = CheckAction.canExecuteAction(action, onPageActions);


        if (!valid) {
            // the action can not be executed
            return this;
        } else {
            // the action can be executed

            switch (action) {
                case "purchase" -> {
                    // add the movie to the user's purchased movied
                    Database.getInstance().getCurrentUser().getPurchasedMovies().add(this.movie);

                    // check if the user is premium and has any free movies left
                    if (Database.getInstance().getCurrentUser().
                            getCredentials().getAccountType().equals("premium")
                            && Database.getInstance().getCurrentUser().
                            getNumFreePremiumMovies() != 0) {
                        Database.getInstance().getCurrentUser().setNumFreePremiumMovies(
                                Database.getInstance().getCurrentUser().
                                        getNumFreePremiumMovies() - 1);
                    } else {
                        // if not then substract 2 tokens for the movie
                        Database.getInstance().getCurrentUser().setTokensCount(
                                Database.getInstance().getCurrentUser().getTokensCount() - 2);
                    }

                    // write the output to the file
                    List<Movie> movieToPrint = new ArrayList<>();
                    movieToPrint.add(this.movie);
                    Writer.getInstance().addOutput(null,
                            movieToPrint, Database.getInstance().getCurrentUser());

                    return this;
                }
                case "watch" -> {

                    // check if the movie has been purchased
                    if (Database.getInstance().getMovie(Database.getInstance().getCurrentUser().
                            getPurchasedMovies(), this.movie.getName()) != null) {
                        // add the movie to the user's watched list
                        Database.getInstance().getCurrentUser().getWatchedMovies().add(this.movie);

                        // write the output
                        List<Movie> movieToPrint = new ArrayList<>();
                        movieToPrint.add(this.movie);
                        Writer.getInstance().addOutput(null,
                                movieToPrint, Database.getInstance().getCurrentUser());
                    } else {
                        // the movie has not been purchased
                        // write an error
                        Writer.getInstance().addOutput("Error", new ArrayList<>(), null);
                    }
                    return this;
                }
                case "like" -> {
                    // check if the movie has been watched
                    if (Database.getInstance().getMovie(Database.getInstance().getCurrentUser().
                            getWatchedMovies(), this.movie.getName()) != null) {

                        // add the movie to the user's liked list
                        Database.getInstance().getCurrentUser().getLikedMovies().add(this.movie);

                        // increase the movie's number of likes
                        this.movie.setNumLikes(this.movie.getNumLikes() + 1);


                        // update the movie's number of likes in the global list
                        Movie universalMovie = Database.getInstance().getMovie(Database.
                                getInstance().getMovies(), this.movie.getName());
                        universalMovie.setNumLikes(universalMovie.getNumLikes() + 1);

                        // write the output
                        List<Movie> movieToPrint = new ArrayList<>();
                        movieToPrint.add(this.movie);
                        Writer.getInstance().addOutput(null,
                                movieToPrint, Database.getInstance().getCurrentUser());

                    } else {
                        // the movie has not been watched
                        // write an error
                        Writer.getInstance().addOutput("Error", new ArrayList<>(), null);
                    }
                    return this;
                }
                case "rate" -> {

                    // check if the movie has been watched
                    if (Database.getInstance().getMovie(Database.getInstance().getCurrentUser().
                            getWatchedMovies(), this.movie.getName()) != null) {

                        String rating = actionDetails.get("rate").asText();

                        // check if the rating is between 1 and 5
                        if (Double.parseDouble(rating) > 5 || Double.parseDouble(rating) < 1) {
                            Writer.getInstance().addOutput("Error", new ArrayList<>(), null);
                        } else {
                            // add the movie to the user's liked list
                            Database.getInstance().getCurrentUser().getRatedMovies().
                                    add(this.movie);

                            // rate the movie in the user's list
                            this.movie.rate(Double.valueOf(rating));

                            // rate the movie in the global list
                            Movie universalMovie = Database.getInstance().getMovie(
                                    Database.getInstance().getMovies(), this.movie.getName());
                            universalMovie.rate(Double.valueOf(rating));

                            // write the output
                            List<Movie> movieToPrint = new ArrayList<>();
                            movieToPrint.add(this.movie);
                            Writer.getInstance().addOutput(null,
                                    movieToPrint, Database.getInstance().getCurrentUser());
                        }
                    } else {
                        // the movie has not been watched
                        Writer.getInstance().addOutput("Error", new ArrayList<>(), null);
                    }
                    return this;
                }
                default -> {
                    return this;
                }
            }
        }
    }



}
