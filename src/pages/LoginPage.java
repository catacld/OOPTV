package pages;

import classes.Database;
import classes.User;
import classes.Writer;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;

public class LoginPage implements Page{

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

    public static LoginPage getInstance() {
        if (instance == null) {
            instance =  new LoginPage();
        }

        return instance;
    }

    public static void setInstance(final LoginPage instance) {
        LoginPage.instance = instance;
    }

    // execute a "change page" action
    // while on the login page
    @Override
    public Page changePage(ObjectNode actionDetails) {
        // since there are no "change page" actions that can be done
        // always write an error
        Writer.getInstance().addOutput("Error", new ArrayList<>(), null);
        return this;
    }

    // execute an "on page" action
    // while on the login page
    @Override
    public Page onPage(ObjectNode actionDetails) {

        String action = actionDetails.get("feature").asText();

        // check if the action can be executed while
        // on the login page
        boolean valid = canExecuteAction(action);


        if (!valid) {
            // the action can not be executed
            //TODO add error output
            Writer.getInstance().addOutput("Error", new ArrayList<>(), null);
        } else {
            // the action can be executed

            // extract the username and password
            String username = actionDetails.get("credentials").get("name").asText();
            String password = actionDetails.get("credentials").get("password").asText();
            // check if the login was successful
            User loggedUser = Database.getInstance().validLogin(username,password);
            // write the output based on the action's result
            if (loggedUser != null) {
                Database.getInstance().setCurrentUser(loggedUser);
                Database.getInstance().deepCopyFilteredMovies(loggedUser);
                Writer.getInstance().addOutput(null, new ArrayList<>(), Database.getInstance().getCurrentUser());
                return new AuthenticatedHome();
            } else {
                Writer.getInstance().addOutput("Error", new ArrayList<>(), null);
                return UnauthenticatedHome.getInstance();
            }
        }
        return this;
    }


    // check if the "on page" action
    // can be executed
    private boolean canExecuteAction(String actionToExecute) {
        for (String action : onPageActions) {
            if (action.equals(actionToExecute)) {
                return true;
            }
        }
        return false;
    }
}
