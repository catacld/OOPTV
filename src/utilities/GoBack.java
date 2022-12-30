package utilities;

import data.Database;
import ioclasses.Writer;
import pages.Page;

import java.util.ArrayList;

public final class GoBack {

    private GoBack() {
    }

    /**
     * goes back on the last visited page
     * @return the last page that
     *          the user visited
     */
    public static Page back() {

        // check if the user can go back
        if (CheckAction.canGoBack()) {
            return Database.getInstance().getHistory().removeLast();
        } else {
            Writer.getInstance().addOutput("Error", new ArrayList<>(),
                    null);
            return null;
        }
    }
}
