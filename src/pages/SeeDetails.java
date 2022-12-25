package pages;

import classes.User;
import utilities.CheckAction;
import data.Database;
import classes.Movie;
import ioclasses.Writer;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;


public class SeeDetails implements Page {

    private final Movie movie;

    private final ArrayList<String> destinationPages;

    private final ArrayList<String> onPageActions;



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

            // get the user that is currently
            // logged in
            User currentUser = Database.getInstance().getCurrentUser();

            switch (action) {
                case "purchase" -> {

                    //purchase the movie
                    currentUser.purchase(movie);

                    return this;
                }
                case "watch" -> {

                    // watch the movie
                    currentUser.watch(movie);

                    return this;
                }
                case "like" -> {

                    //like the movie
                    currentUser.like(movie);

                    return this;
                }
                case "rate" -> {

                    String rating = actionDetails.get("rate").asText();

                    // rate the movie
                    currentUser.rate(movie, Double.parseDouble(rating));

                    return this;
                }
                default -> {
                    return this;
                }
            }
        }
    }



}
