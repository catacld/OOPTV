package pages;

import classes.Database;
import classes.Writer;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;

public class AuthenticatedHome implements Page{

    private ArrayList<String> destinationPages;

    private ArrayList<String> onPageActions;




    public AuthenticatedHome() {

        destinationPages = new ArrayList<>();
        destinationPages.add("movies");
        destinationPages.add("upgrades");
        destinationPages.add("logout");
        destinationPages.add("homepage autentificat");

        onPageActions = new ArrayList<>();

    }



    @Override
    public Page changePage(ObjectNode actionDetails) {

        String destinationPage = actionDetails.get("page").asText();

        // check if the page is reachable
        boolean valid = canChangePage(destinationPage);

        // if it is not print an error
        // and stay on the same page
        if (!valid) {
            Writer.getInstance().addOutput("Error", new ArrayList<>(), null);
            return this;
        } else {
            // else change the page
            switch (destinationPage) {
                case "movies":
                    Writer.getInstance().addOutput(null, Database.getInstance().getMovieList(),
                            Database.getInstance().getCurrentUser());
                    return new Movies();
                case "upgrades":
                    return new Upgrades();
                case "logout":
                    Database.getInstance().setCurrentUser(null);
                    Database.getInstance().setMovieList(null);
                    return UnauthenticatedHome.getInstance();
                default:
                    return this;
            }

        }
    }

    @Override
    public Page onPage(ObjectNode actionDetails) {
        // since there are no "on page" actions that can be done
        // always write an error
        Writer.getInstance().addOutput("Error", new ArrayList<>(), null);
        return this;
    }

    // check if the destination "page" is reachable
    // from the current page
    private boolean canChangePage(String page) {
        for(String destination : destinationPages) {
            if (destination.equals(page)) {
                return true;
            }
        }
        return false;
    }
}
