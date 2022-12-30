package factory;


import classes.Movie;
import data.Database;
import ioclasses.Writer;
import pages.AuthenticatedHome;
import pages.LoginPage;
import pages.Movies;
import pages.Page;
import pages.RegisterPage;
import pages.SeeDetails;
import pages.UnauthenticatedHome;
import pages.Upgrades;
import platformlogic.ActionsManager;

import java.util.ArrayList;



public final class Factory {

    private Factory() {
    }

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

        // the user is not viewing the details of any movie
        // on any of these pages
        Database.getInstance().setCurrentMovie(null);

        switch (page) {
            case "movies":
                // reset any filters applied to the list
                // of movies
                Database.getInstance().deepCopyFilteredMovies();
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

    /**
     * returns a new "See details" page
     * @param movie the movie which will be on
     *              the page
     * @return the new page
     */
    public static Page newSeeDetailsPage(final String movie) {

        // the movie whose details will be shown
        Movie detailedMovie = Database.getInstance().getMovie(Database.getInstance().getMovies(),
                movie);

        // check if the movie is available to the user
        Movie filteredMovie = Database.getInstance().getMovie(
                Database.getInstance().getFilteredMovies(), movie);

        if (filteredMovie == null) {
            // the movie is not available to the current user
            Writer.getInstance().addOutput("Error", new ArrayList<>(), null);
            return new Movies();
        } else {
            // write the output of the action
            SeeDetails seeDetailsPage = new SeeDetails(detailedMovie);
            Database.getInstance().setCurrentMovie(detailedMovie);
            seeDetailsPage.printMessage(1);

            // the current page will become
            // the previous page
            Database.getInstance().getHistory().add(ActionsManager.getCurrentPage());
            return seeDetailsPage;
        }
    }
}
