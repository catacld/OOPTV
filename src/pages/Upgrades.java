package pages;

import constants.Constants;
import factory.Factory;
import ioclasses.Writer;
import utilities.CheckAction;
import data.Database;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;

public class Upgrades implements Page {

    private final ArrayList<String> destinationPages;

    private final ArrayList<String> onPageActions;

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

                return Factory.newPage(destinationPage);
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
                case "buy tokens" -> {

                    // extract the amount of tokens desired
                    String amount = actionDetails.get("count").asText();

                    // update the user's tokens balance
                    Database.getInstance().getCurrentUser().setTokensCount(
                            Database.getInstance().getCurrentUser().getTokensCount()
                                    + Integer.parseInt(amount));

                    // update the user's balance after buying
                    // the tokens
                    Database.getInstance().getCurrentUser().getCredentials().setBalance(
                            Integer.toString(Integer.parseInt(Database.getInstance().
                                    getCurrentUser().getCredentials().getBalance())
                                    - Integer.parseInt(amount)));
                    return this;
                }
                case "buy premium account" -> {

                    // update the status of the user's account
                    Database.getInstance().getCurrentUser().getCredentials().
                            setAccountType("premium");

                    // substract the price of the premium account
                    Database.getInstance().getCurrentUser().setTokensCount(Database.getInstance().
                            getCurrentUser().getTokensCount() - Constants.TEN);

                    return this;
                }
                default -> {
                    return this;
                }
            }
        }
    }

    /**
     * Will print the output of the action based on the
     * parameter given
     * @param code 0 - prints nothing
     *             1 - prints normal output
     *             2 - prints error
     */

    @Override
    public void printMessage(final int code) {

        if (code == 2) {
            Writer.getInstance().addOutput("Error", new ArrayList<>(), null);
        }
    }



}
