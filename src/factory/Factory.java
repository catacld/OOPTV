package factory;


import data.Database;
import ioclasses.Writer;
import pages.*;

import java.util.ArrayList;


public class Factory {

    /**
     * Return an instance of a new page
     * @param page the page whose instance
     *             will be returned
     * @return an instance of "page"
     */
    public static Page newPage(final String page) {
        switch (page) {
            case "movies":
                // reset any filters applied to the list
                // of movies
                Database.getInstance().deepCopyFilteredMovies(
                        Database.getInstance().getCurrentUser());
                Writer.getInstance().addOutput(null, Database.getInstance().getFilteredMovies(),
                        Database.getInstance().getCurrentUser());
                return new Movies();
            case "upgrades":
                return new Upgrades();
            case "logout":
                Database.getInstance().setCurrentUser(null);
                Database.getInstance().setFilteredMovies(null);
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
}
