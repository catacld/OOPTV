package utilities;

import classes.Movie;
import classes.User;
import data.Database;
import ioclasses.Writer;

import java.util.ArrayList;


public final class CheckAction {

    private CheckAction() {
    }

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

    /**
     * check if a user can go on a previous page
     * by checking if the stack of previous page
     * is not empty
     * @return true if the user can go on a previous page
     *          false otherwise
     */
    public static boolean canGoBack() {

        return (Database.getInstance().getCurrentUser() != null
                && !Database.getInstance().getHistory().isEmpty());
    }

    /**
     * check if a user can subscribe to a genre
     * while being on a movie's page
     * @param user the user to subscribe
     * @param movie the movie's page the user is on
     * @param genre the genre to subscribe to
     * @return true if the user can subscribe
     *          false otherwise
     */
    public static boolean canSubscribe(final User user, final Movie movie, final String genre) {
        return !(user.getSubscribedGenres().contains(genre)
                || Database.getInstance().getCurrentMovie() == null
                || !movie.getGenres().contains(genre));
    }

    /**
     * checks if a recommendation should be made
     * @return true if a recommendation should be made
     *          false otherwise
     */
    public static boolean shouldRecommend() {
        return (Database.getInstance().getCurrentUser() != null
                && Database.getInstance().getCurrentUser().isPremium());
    }
}
