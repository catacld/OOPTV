package platformlogic;



import classes.Movie;
import classes.Notification;
import classes.User;
import com.fasterxml.jackson.databind.node.ObjectNode;
import data.Database;
import ioclasses.Writer;
import pages.Page;
import pages.UnauthenticatedHome;
import strategy.ChangePage;
import strategy.OnPage;
import strategy.ProcessAction;
import utilities.CheckAction;
import utilities.GoBack;


import java.util.List;

public class ActionsManager {

    private final List<ObjectNode> actions;
    private static Page currentPage;

    public ActionsManager(final List<ObjectNode> actions) {
        this.actions = actions;
        currentPage = UnauthenticatedHome.getInstance();
    }

    public static Page getCurrentPage() {
        return currentPage;
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
                    ProcessAction processAction = new ProcessAction(new ChangePage());
                    currentPage = processAction.executeAction(action);
                }
                // execute an "on page" action
                case "on page" -> {
                    ProcessAction processAction = new ProcessAction(new OnPage());
                    currentPage = processAction.executeAction(action);
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
                case "back" -> {
                    Page previousPage = GoBack.back();

                    if (previousPage != null) {
                        currentPage = previousPage;
                        currentPage.printMessage(1);
                    }
                }
                default -> {
                    return;
                }
            }
        }
    }

    /**
     * recommend a movie to the logged in
     * premium user
     */
    public void recommend() {

        if (CheckAction.shouldRecommend()) {
            User currentUser = Database.getInstance().getCurrentUser();

            //Movie recommendedMovie = Database.getInstance().recommendMovie();
            Movie recommendedMovie = Database.getInstance().recommendMovie();
            Notification recommendation;

            // create the recommendation
            if (recommendedMovie == null) {
                recommendation = new Notification.NotificationBuilder("Recommendation").build();
            } else {
                recommendation = new Notification.NotificationBuilder("Recommendation")
                                    .movieName(recommendedMovie.getName()).build();
            }


            // send the recommendation as
            // a notification to the user
            currentUser.addNotification(recommendation);

            // write the output of the command
            Writer.getInstance().addOutput(null, null,
                    Database.getInstance().getCurrentUser());

        }
    }

}
