package pages;

import utilities.CheckAction;
import data.Database;
import ioclasses.Writer;
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
                        Writer.getInstance().addOutput(null, Database.getInstance().getFilteredMovies(),
                                Database.getInstance().getCurrentUser());
                        return new Movies();
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
        // on this page
        boolean valid = CheckAction.canExecuteAction(action, onPageActions);


        if (!valid) {
            // the action can not be executed
            return this;
        } else {
            // the action can be executed

            switch (action) {
                case "buy tokens" -> {

                    // extract the amount of tokens desired
                    String amount = actionDetails.get("count").asText();

                    // update the user's tokens balance
                    Database.getInstance().getCurrentUser().setTokensCount(
                            Database.getInstance().getCurrentUser().getTokensCount() + Integer.parseInt(amount));

                    // update the user's balance after buying
                    // the tokens
                    Database.getInstance().getCurrentUser().getCredentials().setBalance(
                            Integer.toString(Integer.parseInt(Database.getInstance().getCurrentUser().getCredentials().getBalance())  -
                                   Integer.parseInt(amount)));
                    return this;
                }
                case "buy premium account" -> {

                    // update the status of the user's account
                    Database.getInstance().getCurrentUser().getCredentials().setAccountType("premium");

                    // substract the price of the premium account
                    Database.getInstance().getCurrentUser().setTokensCount(Database.getInstance().getCurrentUser().getTokensCount() - 10);

                    return this;
                }
            }
        }
        return this;
    }



}
