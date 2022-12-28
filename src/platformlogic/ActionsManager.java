package platformlogic;


import com.fasterxml.jackson.databind.node.ObjectNode;
import data.Database;
import pages.Page;
import pages.UnauthenticatedHome;



import java.util.List;

public class ActionsManager {

    private List<ObjectNode> actions;
    private Page currentPage;

    public ActionsManager(final List<ObjectNode> actions) {
        this.actions = actions;
        this.currentPage = UnauthenticatedHome.getInstance();
    }

    /**
     * Execute the actions given as input
     */
    public void manageActions() {
        // traverse the array of actions
        for (ObjectNode action : actions) {
            // get the type of the action
            String type = action.get("type").asText();
            switch (type) {
                // execute a "change page" action
                case "change page" -> {
                    // add the page to the history of pages
                    currentPage = currentPage.changePage(action);
                }
                // execute an "on page" action
                case "on page" -> {
                    currentPage = currentPage.onPage(action);
                }
//                case "back" -> {
//                    if (GoBack.back() != null) {
//                        currentPage = GoBack.back();
//                    }
//                }
                // subscribe to a genre
                case "subscribe" -> {
                    String genre = action.get("subscribedGenre").asText();
                    Database.getInstance().getCurrentUser().subscribe(genre);
                }
                // execute a "database" action
                case "database" -> {
                    String feature = action.get("feature").asText();
                    if (feature.equals("add")) {
                        Database.getInstance().addMovie(action);
                    } else {
                        Database.getInstance().removeMovie(action);
                    }
                }
                default -> {
                    return;
                }
            }
        }
    }

}
