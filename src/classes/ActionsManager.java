package classes;

import com.fasterxml.jackson.databind.node.ObjectNode;
import pages.Page;
import pages.UnauthenticatedHome;

import java.util.List;

public class ActionsManager {

    private List<ObjectNode> actions;
    private Page currentPage;

    public ActionsManager(List<ObjectNode> actions) {
        this.actions = actions;
        this.currentPage = UnauthenticatedHome.getInstance();
    }

    // execute the actions given as input
    public void manageActions() {
        // traverse the array of actions
        for (ObjectNode action : actions) {
            // get the type of the action
            String type = action.get("type").asText();
            switch (type) {
                // a "change page" action
                case "change page" -> {
                    currentPage = currentPage.changePage(action);
                }
                // an "on page" action
                case "on page" -> {
                    currentPage = currentPage.onPage(action);
                }
            }
        }
    }

}
