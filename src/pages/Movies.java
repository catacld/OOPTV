package pages;

import classes.Movie;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import data.Database;
import factory.Factory;
import ioclasses.Writer;
import utilities.CheckAction;
import utilities.FilterMovies;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Movies implements Page {

    private final ArrayList<String> destinationPages;

    private final ArrayList<String> onPageActions;

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

    /**
     * Change the page from the current page to
     * another page
     * @param actionDetails node where the details of the
     *                      action are stored
     * @return the destination page
     */
    @Override
    public final Page changePage(final ObjectNode actionDetails) {
        String destinationPage = actionDetails.get("page").asText();

        // check if the action can be executed
        // while on this page
        boolean valid = CheckAction.canChangePage(destinationPage, destinationPages);

        if (!valid) {

            // the destination is not reachable
            return this;

        } else {
            // the destination is reachable

            // this case will be treated separately
            // since it needs one more parameter
            if ("see details".equals(destinationPage)) {
                String movie = actionDetails.get("movie").asText();
                return Factory.newSeeDetailsPage(movie);
            } else {
                return Factory.newPage(destinationPage);
            }
        }
    }


    /**
     * Execute an "on page" action
     * @param actionDetails node where the details of the
     *                      action are stored
     * @return the same page
     */
    @Override
    public final Page onPage(final ObjectNode actionDetails) {
        String action = actionDetails.get("feature").asText();

        // check if the action can be executed
        // while on this page
        boolean valid = CheckAction.canExecuteAction(action, onPageActions);


        if (!valid) {
            // the action can not be executed
            return this;
        } else {
            // the action can be executed
            Database.getInstance().deepCopyFilteredMovies();
            List<Movie> moviesList = Database.getInstance().getFilteredMovies();
            switch (action) {

                case "search" -> {
                    String startsWith = actionDetails.get("startsWith").asText();

                    // filter the list of movies by title
                    moviesList = FilterMovies.search(moviesList, startsWith);
                    Writer.getInstance().addOutput(null,
                            moviesList,
                            Database.getInstance().getCurrentUser());

                    // reset the original list in the database
                    // since the filter messes it up
                    Database.getInstance().deepCopyFilteredMovies();
                    return this;
                }
                case "filter" -> {
                    if (actionDetails.get("filters").get("sort") != null) {

                        String ratingOrder = null;
                        String durationOrder = null;

                        if (actionDetails.get("filters").get("sort").get("rating") != null) {
                            ratingOrder = actionDetails.get("filters").
                                          get("sort").get("rating").asText();
                        }
                        if (actionDetails.get("filters").get("sort").get("duration") != null) {
                            durationOrder = actionDetails.get("filters").get("sort").
                                            get("duration").asText();
                        }


                        // filter the list of movies by both
                        // rating and duration
                        if (ratingOrder != null && durationOrder != null) {
                            moviesList = FilterMovies.sortByBoth(moviesList,
                                                      durationOrder, ratingOrder);
                        } else if (ratingOrder != null) {
                             // filter the list of movies only by rating
                            moviesList = FilterMovies.sortByRating(moviesList, ratingOrder);
                        } else if (durationOrder != null) {
                             // filter the list of movies only by duration
                            moviesList = FilterMovies.sortByDuration(moviesList, durationOrder);
                        }
                    }
                    if (actionDetails.get("filters").get("contains") != null) {
                        ObjectMapper objectMapper = new ObjectMapper();
                        TypeReference<List<String>> refList = new TypeReference<>() {
                        };

                        if (actionDetails.get("filters").get("contains").get("actors") != null) {
                            // get the list of actors given in the input
                            List<String> actors;
                            try {
                                actors = objectMapper.readValue(
                                        actionDetails.get("filters").get("contains").
                                                      get("actors").traverse(), refList);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }

                            // filter by each actor given
                            for (String actor : actors) {
                                moviesList = FilterMovies.filterByActor(moviesList, actor);
                            }
                        }

                        if (actionDetails.get("filters").get("contains").get("genre") != null) {

                            // get the list of genres
                            List<String> genres;
                            try {
                                genres = objectMapper.readValue(
                                        actionDetails.get("filters").get("contains").
                                                      get("genre").traverse(), refList);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }

                            // filter by each genre given
                            for (String genre : genres) {
                                moviesList = FilterMovies.filterByGenre(moviesList, genre);
                            }
                        }
                    }

                    // write the output of the command
                    Writer.getInstance().addOutput(null,
                            moviesList, Database.getInstance().getCurrentUser());
                    return this;
                }
                default -> {
                    return this;
                }
            }
        }
    }


    /**
     * Will print the output of the action based on the
     * parameter given
     * @param code 0 - prints nothing
     *             1 - prints normal output
     *             2 - prints error
     */
    @Override
    public void printMessage(final int code) {

        if (code == 1) {
            Writer.getInstance().addOutput(null, Database.getInstance().getFilteredMovies(),
                        Database.getInstance().getCurrentUser());
        } else if (code == 2) {
            Writer.getInstance().addOutput("Error", new ArrayList<>(), null);
        }


    }

}
