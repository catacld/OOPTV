package pages;

import classes.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import data.Database;
import ioclasses.Writer;
import utilities.CheckAction;

import java.util.ArrayList;

public final class RegisterPage implements Page {

    private static RegisterPage instance = null;

    private final ArrayList<String> destinationPages;

    private final ArrayList<String> onPageActions;

    private RegisterPage() {

        this.destinationPages = new ArrayList<>();
        destinationPages.add("register");

        this.onPageActions = new ArrayList<>();
        onPageActions.add("register");

    }

    /**
     * Get the instance
     */
    public static RegisterPage getInstance() {
        if (instance == null) {
            instance = new RegisterPage();
        }

        return instance;
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
        // on the register page
        boolean valid = CheckAction.canExecuteAction(action, onPageActions);

        if (!valid) {
            return this;
        } else {

            // the action can be executed

            // extract the credentials of the new user
            ObjectMapper mapper = new ObjectMapper();
            Credentials userCredentials = mapper.convertValue(actionDetails.get("credentials"),
                                          Credentials.class);

            // check if the username is available
            boolean available = Database.getInstance().checkUsernameAvailable(
                                userCredentials.getName());

            // write the output based on the action's result
            if (available) {
                User userToAdd = new User(userCredentials);

                // add a new user to the database
                Database.getInstance().getUsers().add(userToAdd);

                // update the logged-in user
                Database.getInstance().setCurrentUser(userToAdd);
                Database.getInstance().deepCopyFilteredMovies(userToAdd);

                // empty the history when the user changes
                Database.getInstance().getHistory().clear();

                // write the output of the action
                Writer.getInstance().addOutput(
                        null, new ArrayList<>(), Database.getInstance().getCurrentUser());
                return new AuthenticatedHome();
            } else {
                Writer.getInstance().addOutput("Error", new ArrayList<>(), null);
                return UnauthenticatedHome.getInstance();
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
    public void printMessage(int code) {

        if (code == 2) {
            Writer.getInstance().addOutput("Error", new ArrayList<>(), null);
        }
    }
}
