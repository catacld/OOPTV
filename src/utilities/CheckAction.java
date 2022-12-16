package utilities;

import ioclasses.Writer;

import java.util.ArrayList;

public class CheckAction {

    // check if a "change page" action can be
    // executed while on a certain page
    public static boolean canChangePage(String page, ArrayList<String> destinationPages) {
        for(String destination : destinationPages) {
            if (destination.equals(page)) {
                return true;
            }
        }
        // the action can not be executed, print an error
        // and return false
        Writer.getInstance().addOutput("Error", new ArrayList<>(), null);
        return false;
    }

    public static boolean canExecuteAction(String actionToExecute, ArrayList<String> onPageActions) {
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
