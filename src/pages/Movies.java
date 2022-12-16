package pages;

import classes.Database;
import classes.Movie;
import classes.Writer;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Movies implements Page{

    private ArrayList<String> destinationPages;

    private ArrayList<String> onPageActions;

    public Movies() {

        destinationPages = new ArrayList<>();
        destinationPages.add("homepage autentificat");
        destinationPages.add("see details");
        destinationPages.add("movies");
        destinationPages.add("logout");

        onPageActions = new ArrayList<>();
        onPageActions.add("search");
        onPageActions.add("filter");
    }

    @Override
    public Page changePage(ObjectNode actionDetails) {
        String destinationPage = actionDetails.get("page").asText();

        // check if the destination page is reachable
        // while on the "movies" page
        boolean valid = canExecuteAction(destinationPage,destinationPages);

        if (!valid) {
            // the destination is not reachable
            Writer.getInstance().addOutput("Error", new ArrayList<>(), null);
        } else {
            // the destination is reachable




            switch (destinationPage) {
                case "see details" -> {
                    String movie = actionDetails.get("movie").asText();
                    if (Database.getInstance().getMovie(Database.getInstance().getMovieList(),movie) == null) {
                        // the movie is not available for the current user
                        Writer.getInstance().addOutput("Error", new ArrayList<>(), null);
                        return this;
                    } else {
                        List<Movie> movieToPrint = new ArrayList<>();
                        movieToPrint.add(Database.getInstance().getMovie(Database.getInstance().getMovieList(),movie));
                        Writer.getInstance().addOutput(null,
                                movieToPrint, Database.getInstance().getCurrentUser());
                        return new SeeDetails(Database.getInstance().getMovie(Database.getInstance().getMovieList(), movie));
                    }
                }
                case "homepage autentificat" -> {
                    Writer.getInstance().addOutput(null, new ArrayList<>(), Database.getInstance().getCurrentUser());
                    return new AuthenticatedHome();
                }
                case "logout" -> {
                    Database.getInstance().setCurrentUser(null);
                    Database.getInstance().setMovieList(null);
                    return UnauthenticatedHome.getInstance();
                }
                case "movies" -> {
                    // reset the filtered list
                    Database.getInstance().deepCopyFilteredMovies(Database.getInstance().getCurrentUser());
                    Writer.getInstance().addOutput(null, Database.getInstance().getMovieList(),
                            Database.getInstance().getCurrentUser());
                    return this;
                }
            }
        }
        return this;
    }

    @Override
    public Page onPage(ObjectNode actionDetails) {
        String action = actionDetails.get("feature").asText();

        // check if the action can be executed while
        // on the "movies" page
        boolean valid = canExecuteAction(action,onPageActions);


        if (!valid) {
            // the action can not be executed
            Writer.getInstance().addOutput("Error", new ArrayList<>(), null);
        } else {
            // the action can be executed
            Database.getInstance().deepCopyFilteredMovies(Database.getInstance().getCurrentUser());
            List<Movie> moviesList = Database.getInstance().getMovieList();
            switch (action) {

                case "search" -> {
                    String startsWith = actionDetails.get("startsWith").asText();
                    // will apply the search operation to filter the list
                    // deleting all the movies that do not qualify
                    moviesList = Database.getInstance().search(moviesList, startsWith);
                    Writer.getInstance().addOutput(null,
                            moviesList,
                            Database.getInstance().getCurrentUser());
                    // reset the list
                    Database.getInstance().deepCopyFilteredMovies(Database.getInstance().getCurrentUser());
                    return this;
                }
                case "filter" -> {
                    // get the movies available to the user
                    if (actionDetails.get("filters").get("sort") != null) {

                        String ratingOrder = null;
                        String durationOrder = null;

                        if (actionDetails.get("filters").get("sort").get("rating") != null) {
                            ratingOrder = actionDetails.get("filters").get("sort").get("rating").asText();
                        }
                        if (actionDetails.get("filters").get("sort").get("duration") != null) {
                            durationOrder = actionDetails.get("filters").get("sort").get("duration").asText();
                        }


                        // both are mentioned
                        if (ratingOrder != null && durationOrder != null) {
                            moviesList = Database.getInstance().sortByBoth(moviesList,durationOrder,ratingOrder);
                        }
                         else if (ratingOrder != null) {
                             // if rating is equal sort by
                            // duration increasing
                            moviesList = Database.getInstance().sortByRating(moviesList,ratingOrder);
                        } else if (durationOrder != null) {
                            moviesList = Database.getInstance().sortByDuration(moviesList, durationOrder);
                        }
                    }
                    if (actionDetails.get("filters").get("contains") != null) {
                        ObjectMapper objectMapper = new ObjectMapper();
                        TypeReference<List<String>> refList = new TypeReference<>() {
                        };

                        if (actionDetails.get("filters").get("contains").get("actors") != null) {
                            // get the list of actors
                            List<String> actors;
                            try {
                                actors = objectMapper.readValue(
                                        actionDetails.get("filters").get("contains").get("actors").traverse(), refList);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            for (String actor : actors) {
                                moviesList = Database.getInstance().filterByActor(moviesList, actor);
                            }
                        }

                        if (actionDetails.get("filters").get("contains").get("genre") != null) {
                            // get the list of actors
                            List<String> genres;
                            try {
                                genres = objectMapper.readValue(
                                        actionDetails.get("filters").get("contains").get("genre").traverse(), refList);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            for (String genre : genres) {
                                moviesList = Database.getInstance().filterByGenre(moviesList, genre);
                            }
                        }
                    }
                    //Database.getInstance().deepCopyFilteredMovies(Database.getInstance().getCurrentUser());
                    Writer.getInstance().addOutput(null,
                            moviesList, Database.getInstance().getCurrentUser());
                    return this;
                }
            }
        }
        return this;
    }

    // check if the action
    // can be executed
    private boolean canExecuteAction(String actionToExecute, ArrayList<String> possibleActions) {
        for (String action : possibleActions) {
            if (action.equals(actionToExecute)) {
                return true;
            }
        }
        return false;
    }
}
