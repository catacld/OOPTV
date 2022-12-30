package strategy;

import com.fasterxml.jackson.databind.node.ObjectNode;
import pages.Page;

public class ProcessAction {

    private final Action action;

    public ProcessAction(final Action strategy) {
        action = strategy;
    }

    /**
     * perform the action given as parameter
     * @param actionDetails the details of the
     *                      action to be performed
     * @return the page the user is viewing,
     *          after performing the given
     *          action
     */
    public Page executeAction(final ObjectNode actionDetails) {
        return action.performAction(actionDetails);
    }
}
