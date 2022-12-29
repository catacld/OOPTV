package pages;

import factory.Factory;
import utilities.CheckAction;
import ioclasses.Writer;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;

public class AuthenticatedHome implements Page {

    private final ArrayList<String> destinationPages;

    private final ArrayList<String> onPageActions;




    public AuthenticatedHome() {

        destinationPages = new ArrayList<>();
        destinationPages.add("movies");
        destinationPages.add("upgrades");
        destinationPages.add("logout");
        destinationPages.add("homepage autentificat");

        onPageActions = new ArrayList<>();

    }


    /**
     * Change the page from the current page to
     * another page
     * @param actionDetails node where the details of the
     *                      action are stored
     * @return the destination page
     */
    @Override
    public final Page changePage(final ObjectNode actionDetails) {

        String destinationPage = actionDetails.get("page").asText();

        // check if the action can be executed while
        // on this page
        boolean valid = CheckAction.canChangePage(destinationPage, destinationPages);

        // the action can not be executed
        if (!valid) {
            return this;
        } else {
            // else change the page
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
    public final Page onPage(final ObjectNode actionDetails) {
        // since there are no "on page" actions that can be executed
        // while on this page, always write an error
        Writer.getInstance().addOutput("Error", new ArrayList<>(), null);
        return this;
    }

    /**
     * Will print the output of the action based on the
     * parameter given
     * @param code 0 - prints nothing
     *             1 - prints normal output
     *             2 - prints error
     */

    @Override
    public void printMessage(int code) {

        if (code == 2) {
            Writer.getInstance().addOutput("Error", new ArrayList<>(), null);
        }
    }
}
