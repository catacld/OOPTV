package pages;

import factory.Factory;
import utilities.CheckAction;
import ioclasses.Writer;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;

public final class UnauthenticatedHome implements Page {

    private static UnauthenticatedHome  instance = null;

    private ArrayList<String> destinationPages;

    private ArrayList<String> onPageActions;

    // singleton since it is always the same for
    // anyone using the platform
    private UnauthenticatedHome() {

        this.destinationPages = new ArrayList<>();
        destinationPages.add("login");
        destinationPages.add("register");
        destinationPages.add("homepage neautentificat");

        this.onPageActions = new ArrayList<>();
    }

    /**
     * Get the instance
     */
    public static UnauthenticatedHome getInstance() {
        if (instance == null) {
            instance =  new UnauthenticatedHome();
        }

        return instance;
    }

    public static void setInstance(final UnauthenticatedHome instance) {
        UnauthenticatedHome.instance = instance;
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

        // check if the destination page is reachable
        // from the current page
        boolean valid = CheckAction.canChangePage(destinationPage, destinationPages);

        // if it is not stay on the
        // same page
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
    public Page onPage(final ObjectNode actionDetails) {

        // since there are no "on page" actions that can be executed
        // while on this page, always write an error
        Writer.getInstance().addOutput("Error", new ArrayList<>(), null);
        return this;

    }
}
