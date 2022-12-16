package pages;

import utilities.CheckAction;
import data.Database;
import classes.User;
import ioclasses.Writer;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;

public final class LoginPage implements Page {

    private static LoginPage instance = null;

    private ArrayList<String> destinationPages;

    private ArrayList<String> onPageActions;

    // singleton since it is always the same
    // for anyone using the platform
    private LoginPage() {
        this.destinationPages = new ArrayList<>();
        destinationPages.add("login");

        this.onPageActions = new ArrayList<>();
        onPageActions.add("login");
    }

    /**
     * Get the instance of the page
     */
    public static LoginPage getInstance() {
        if (instance == null) {
            instance =  new LoginPage();
        }

        return instance;
    }

    public static void setInstance(final LoginPage instance) {
        LoginPage.instance = instance;
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
        // since there are no "change page" actions that can be executed
        // while on this page, always write an error
        Writer.getInstance().addOutput("Error", new ArrayList<>(), null);
        return this;
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
        // on the login page
        boolean valid = CheckAction.canExecuteAction(action, onPageActions);


        if (!valid) {
            // the action can not be executed
            return this;
        } else {
            // the action can be executed

            // extract the login credentials
            String username = actionDetails.get("credentials").get("name").asText();
            String password = actionDetails.get("credentials").get("password").asText();

            // check if the login was successful
            User loggedUser = Database.getInstance().login(username, password);

            // write the output based on the action's result
            if (loggedUser != null) {

                // update the logged-in user and the list of
                // available movies to the user in the database
                Database.getInstance().setCurrentUser(loggedUser);
                Database.getInstance().deepCopyFilteredMovies(loggedUser);
                Writer.getInstance().addOutput(null, new ArrayList<>(),
                                                Database.getInstance().getCurrentUser());
                return new AuthenticatedHome();
            } else {
                // the login failed
                Writer.getInstance().addOutput("Error", new ArrayList<>(), null);
                return UnauthenticatedHome.getInstance();
            }
        }
    }



}
