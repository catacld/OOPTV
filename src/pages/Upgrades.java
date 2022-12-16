package pages;

import classes.Database;
import classes.User;
import classes.Writer;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;

public class Upgrades implements Page{

    private ArrayList<String> destinationPages;

    private ArrayList<String> onPageActions;

    public Upgrades() {

        destinationPages = new ArrayList<>();
        destinationPages.add("homepage autentificat");
        destinationPages.add("movies");
        destinationPages.add("logout");
        destinationPages.add("upgrades");

        onPageActions = new ArrayList<>();
        onPageActions.add("buy premium account");
        onPageActions.add("buy tokens");
    }

    @Override
    public Page changePage(ObjectNode actionDetails) {

            //String destinationPage = actionDetails.get("page").asText();
              String destinationPage = "movies";


            // check if the destination page is reachable
            // while on the "upgrades" page
            boolean valid = canExecuteAction(destinationPage, destinationPages);

            if (!valid) {
                // the destination is not reachable
                Writer.getInstance().addOutput("Error", new ArrayList<>(), null);
            } else {
                // the destination is reachable

                switch (destinationPage) {
                    case "movies" -> {
                        Writer.getInstance().addOutput(null, Database.getInstance().getMovieList(),
                                Database.getInstance().getCurrentUser());
                        return new Movies();
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
                case "buy tokens" -> {
                    String amount = actionDetails.get("count").asText();
                    Database.getInstance().getCurrentUser().setTokensCount(
                            Database.getInstance().getCurrentUser().getTokensCount() + Integer.parseInt(amount));
                    Database.getInstance().getCurrentUser().getCredentials().setBalance(
                            Integer.toString(Integer.parseInt(Database.getInstance().getCurrentUser().getCredentials().getBalance())  -
                                   Integer.parseInt(amount)));
                    return this;
                }
                case "buy premium account" -> {
                    Database.getInstance().getCurrentUser().getCredentials().setAccountType("premium");
                    Database.getInstance().getCurrentUser().setTokensCount(Database.getInstance().getCurrentUser().getTokensCount() - 10);
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
