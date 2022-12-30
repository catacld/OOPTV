package strategy;

import com.fasterxml.jackson.databind.node.ObjectNode;
import pages.Page;

public interface Action {

    /**
     * perform the action given as parameter
     * on the page the user is viewing
     * @param action the action to be performed
     * @return the page the user is viewing,
     *         after performing the given
     *         action
     */
    Page performAction(ObjectNode action);
}
