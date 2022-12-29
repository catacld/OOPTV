package factory;


import classes.Movie;
import data.Database;
import ioclasses.Writer;
import pages.*;
import platformlogic.ActionsManager;

import java.util.ArrayList;



public class Factory {

    /**
     * Return an instance of a new page
     * @param page the page whose instance
     *             will be returned
     * @return an instance of "page"
     */
    public static Page newPage(final String page) {

        // the current page will become
        // the previous page

        Database.getInstance().getHistory().add(ActionsManager.getCurrentPage());

        switch (page) {
            case "movies":
                // reset any filters applied to the list
                // of movies
                Database.getInstance().deepCopyFilteredMovies(
                        Database.getInstance().getCurrentUser());
//                Writer.getInstance().addOutput(null, Database.getInstance().getFilteredMovies(),
//                        Database.getInstance().getCurrentUser());
                Movies moviesPage =  new Movies();
                moviesPage.printMessage(1);
                return moviesPage;
            case "upgrades":
                return new Upgrades();
            case "logout":
                Database.getInstance().setCurrentUser(null);
                Database.getInstance().setFilteredMovies(null);
                // empty the history of pages when the user
                // disconnects
                Database.getInstance().getHistory().clear();
                return UnauthenticatedHome.getInstance();
            case "homepage autentificat" :
                Writer.getInstance().addOutput(null, new ArrayList<>(),
                        Database.getInstance().getCurrentUser());
                return new AuthenticatedHome();
            case "register" :
                return RegisterPage.getInstance();
            case "login" :
                return LoginPage.getInstance();
            default:
                return null;
        }
    }

    public static Page newSeeDetailsPage(final String movie) {

        // the movie whose details will be shown
        Movie detailedMovie = Database.getInstance().getMovie(Database.getInstance().getFilteredMovies(),
                movie);

        if (detailedMovie == null) {
            // the movie is not available for the current user
            Writer.getInstance().addOutput("Error", new ArrayList<>(), null);
            return new Movies();
        } else {
            // write the output of the action
            SeeDetails seeDetailsPage = new SeeDetails(detailedMovie);
            seeDetailsPage.printMessage(1);

            // the current page will become
            // the previous page
            Database.getInstance().getHistory().add(ActionsManager.getCurrentPage());
            return seeDetailsPage;
        }
    }
}
