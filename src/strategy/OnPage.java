package strategy;

import com.fasterxml.jackson.databind.node.ObjectNode;
import pages.Page;
import platformlogic.ActionsManager;

public class OnPage implements Action {


    /**
     * perform the action given as parameter
     * on the page the user is viewing
     * @param action the action to be performed
     * @return the same page since the user
     *          does not leave the page
     */
    @Override
    public Page performAction(final ObjectNode action) {
        return ActionsManager.getCurrentPage().onPage(action);
    }
}
