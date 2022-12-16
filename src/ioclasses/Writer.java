package ioclasses;


import classes.Movie;
import classes.User;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Writer {

    private static Writer instance = null;

    private ObjectMapper objectMapper = new ObjectMapper();

    private ArrayNode output = objectMapper.createArrayNode();

    private ObjectWriter objectWriter = this.objectMapper.writerWithDefaultPrettyPrinter();

    private String error;

    private List<Movie> currentMoviesList;

    private User currentUser;

    public Writer(final String error, final ArrayList<Movie> currentMoviesList,
                  final User currentUser) {
        this.error = error;
        this.currentMoviesList = currentMoviesList;
        this.currentUser = currentUser;
    }

    private Writer() {
    }

    /**
     * Get the instance of the writer
     */
    public static Writer getInstance() {
        if (instance == null) {
            instance = new Writer();
        }

        return instance;
    }

    /**
     * Add the result of an action to the output node
     * @param outputError the error to be written
     * @param moviesList the list of movies to be written
     * @param loggedUser the logged-in user
     */
    public void addOutput(final String outputError, final List<Movie> moviesList,
                          final User loggedUser) {
        this.error = outputError;
        this.currentMoviesList = moviesList;
        this.currentUser = loggedUser;
        output.add(objectMapper.convertValue(this, JsonNode.class));
    }


    /**
     * Write the output to the file
     */
    public void writeOutput() {
        try {
            this.objectWriter.writeValue(new File("results.out"), this.output);
            output = objectMapper.createArrayNode();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public final String getError() {
        return error;
    }

    public final void setError(final String error) {
        this.error = error;
    }

    public final List<Movie> getCurrentMoviesList() {
        return currentMoviesList;
    }

    public final void setCurrentMoviesList(final List<Movie> currentMoviesList) {
        this.currentMoviesList = currentMoviesList;
    }

    public final User getCurrentUser() {
        return currentUser;
    }

    public final void setCurrentUser(final User currentUser) {
        this.currentUser = currentUser;
    }
}
