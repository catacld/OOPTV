package pages;

import classes.Credentials;
import classes.Database;
import classes.User;
import classes.Writer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import javax.xml.crypto.Data;
import java.util.ArrayList;

public class RegisterPage implements Page{

    private static RegisterPage instance = null;

    private ArrayList<String> destinationPages;

    private ArrayList<String> onPageActions;

    private RegisterPage() {

        this.destinationPages = new ArrayList<>();

        this.onPageActions = new ArrayList<>();
        onPageActions.add("register");

    }

    public static RegisterPage getInstance() {
        if (instance == null) {
            instance = new RegisterPage();
        }

        return instance;
    }

    @Override
    public Page changePage(ObjectNode actionDetails) {
        // since there are no "change page" actions that can be done
        // always write an error
        Writer.getInstance().addOutput("Error", new ArrayList<>(), null);
        return this;
    }

    @Override
    public Page onPage(ObjectNode actionDetails) {

        String action = actionDetails.get("feature").asText();

        // check if the action can be executed while
        // on the register page
        boolean valid = canExecuteAction(action);

        if (!valid) {
            // the action can not be executed
            //TODO add error output
            Writer.getInstance().addOutput("Error", new ArrayList<>(), null);
        } else {
            // the action can be executed
            // extract the credentials of the new user
            ObjectMapper mapper = new ObjectMapper();
            Credentials userCredentials = mapper.convertValue(actionDetails.get("credentials"),Credentials.class);



            // check if the username is available
            boolean available = Database.getInstance().checkUsernameAvailable(userCredentials.getName());
            // write the output based on the action's result
            if (available) {
                User userToAdd = new User(userCredentials);
                Database.getInstance().getUsers().add(userToAdd);
                Database.getInstance().setCurrentUser(userToAdd);
                Database.getInstance().deepCopyFilteredMovies(userToAdd);
                Writer.getInstance().addOutput(null, new ArrayList<>(), Database.getInstance().getCurrentUser());
                return new AuthenticatedHome();
            } else {
                Writer.getInstance().addOutput("Error", new ArrayList<>(), null);
                return UnauthenticatedHome.getInstance();
            }
        }
        return this;
    }

    // check if we can execute the
    // "on page" auction
    private boolean canExecuteAction(String actionToExecute) {
        for (String action : onPageActions) {
            if (action.equals(actionToExecute)) {
                return true;
            }
        }
        return false;
    }
}
