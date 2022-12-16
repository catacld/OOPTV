package pages;

import utilities.CheckAction;
import ioclasses.Writer;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;

public class UnauthenticatedHome implements Page{

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

    public static UnauthenticatedHome getInstance() {
        if (instance == null) {
            instance =  new UnauthenticatedHome();
        }

        return instance;
    }

    public static void setInstance(final UnauthenticatedHome instance) {
        UnauthenticatedHome.instance = instance;
    }

    @Override
    public Page changePage(ObjectNode actionDetails) {

        String destinationPage = actionDetails.get("page").asText();

        // check if the destination page is reachable
        // from the current page
        boolean valid = CheckAction.canChangePage(destinationPage,destinationPages);

        // if it is not print an error
        // stay on the same page
        if (!valid) {
            return this;
        } else {
            // else change the page
            return switch (destinationPage) {
                case "login" -> LoginPage.getInstance();
                case "register" -> RegisterPage.getInstance();
                default -> this;
            };
        }

    }

    @Override
    public Page onPage(ObjectNode actionDetails) {

        // since there are no "on page" actions that can be executed
        // while on this page, always write an error
        Writer.getInstance().addOutput("Error", new ArrayList<>(), null);
        return this;

    }
}
