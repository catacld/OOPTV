package utilities;

import ioclasses.Writer;

import java.util.ArrayList;

public class CheckAction {

    // check if a "change page" action can be
    // executed while on a certain page
    /**
     * Check if a "change page" action can be
     * executed while on a certain page
     * @param page the destination page
     * @param destinationPages the list of possible
     *                         destination pages
     * @return true if possible, else false
     */
        public static boolean canChangePage(final String page,
                                            final ArrayList<String> destinationPages) {
        for (String destination : destinationPages) {
            if (destination.equals(page)) {
                return true;
            }
        }
        // the action can not be executed, print an error
        // and return false
        Writer.getInstance().addOutput("Error", new ArrayList<>(), null);
        return false;
    }


    /**
     * Check if an "on page" action can be
     * executed while on a certain page
     * @param actionToExecute the action to be executed
     * @param onPageActions the list of possible actions
     *                      to be executed while on the
     *                      current page
     * @return true if possible, else false
     */
    public static boolean canExecuteAction(final String actionToExecute,
                                           final ArrayList<String> onPageActions) {
        for (String action : onPageActions) {
            if (action.equals(actionToExecute)) {
                return true;
            }
        }

        // the action can not be executed, print an error
        // and return false
        Writer.getInstance().addOutput("Error", new ArrayList<>(), null);
        return false;
    }
}
