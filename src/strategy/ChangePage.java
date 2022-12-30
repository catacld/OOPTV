package strategy;

import com.fasterxml.jackson.databind.node.ObjectNode;
import pages.Page;
import platformlogic.ActionsManager;

public class ChangePage implements Action {

    /**
     * perform the action given as parameter
     * on the page the user is viewing
     * @param action the action to be performed
     * @return the new page if the action can be done
     *          the same page otherwise
     */
    @Override
    public Page performAction(final ObjectNode action) {
        return ActionsManager.getCurrentPage().changePage(action);
    }
}
