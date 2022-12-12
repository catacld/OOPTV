package pages;

import classes.Database;
import classes.Movie;
import classes.Writer;
import com.fasterxml.jackson.databind.node.ObjectNode;

import javax.xml.crypto.Data;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class Movies implements Page{

    private ArrayList<String> destinationPages;

    private ArrayList<String> onPageActions;

    public Movies() {

        destinationPages = new ArrayList<>();
        destinationPages.add("homepage autentificat");
        destinationPages.add("see details");
        destinationPages.add("logout");

        onPageActions = new ArrayList<>();
        onPageActions.add("search");
        onPageActions.add("filter");
    }

    @Override
    public Page changePage(ObjectNode actionDetails) {
        String destinationPage = actionDetails.get("page").asText();

        // check if the destination page is reachable
        // while on the "movies" page
        boolean valid = canExecuteAction(destinationPage,destinationPages);

        if (!valid) {
            // the destination is not reachable
            Writer.getInstance().addOutput("Error", new ArrayList<>(), null);
        } else {
            // the destination is reachable

            switch (destinationPage) {
                case "see details" -> {
                    String movie = actionDetails.get("movie").asText();
                    if (Database.getInstance().getMovie(movie) == null) {
                        // the movie is not available for the current user
                        Writer.getInstance().addOutput("Error", new ArrayList<>(), null);
                        return this;
                    } else {
                        return new SeeDetails(Database.getInstance().getMovie(movie));
                    }
                }
                case "homepage autentificat" -> {
                    Writer.getInstance().addOutput(null, new ArrayList<>(), Database.getInstance().getCurrentUser());
                    return new AuthenticatedHome();
                }
                case "logout" -> {
                    Database.getInstance().setCurrentUser(null);
                    Database.getInstance().setFilteredMovies(null);
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
        // on the "movies" page
        boolean valid = canExecuteAction(action,onPageActions);


        if (!valid) {
            // the action can not be executed
            Writer.getInstance().addOutput("Error", new ArrayList<>(), null);
        } else {
            // the action can be executed

            switch(action){
                case "search":
                    String startsWith = actionDetails.get("startsWith").asText();
                    // will make a copy of the original list with all the movies
                    Database.getInstance().deepCopyFilteredMovies(Database.getInstance().getCurrentUser());
                    // will apply the search operation to filter the list
                    // deleting all the movies that do not qualify
                    Database.getInstance().getFilteredMovies().removeIf(
                            movie -> !movie.getName().startsWith(startsWith));
                    Writer.getInstance().addOutput(null,
                            Database.getInstance().getFilteredMovies(),
                            Database.getInstance().getCurrentUser());
                    return this;
                case "filter":
                    Writer.getInstance().addOutput(null,
                            new ArrayList<>(), Database.getInstance().getCurrentUser());
                    return this;
            }
        }
        return this;
    }

    // check if the "on page" action
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
