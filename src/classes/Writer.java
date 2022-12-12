package classes;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
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

    public Writer(String error, ArrayList<Movie> currentMoviesList, User currentUser) {
        this.error = error;
        this.currentMoviesList = currentMoviesList;
        this.currentUser = currentUser;
    }

    private Writer() {
        //output.add(objectMapper.convertValue(Database.getInstance().getUsers(), JsonNode.class));
    }

    public static Writer getInstance() {
        if (instance == null) {
            instance = new Writer();
        }

        return instance;
    }

    // add the result of an action to the output
    public void addOutput(String error, List<Movie> MoviesList, User currentUser) {
        this.error = error;
        this.currentMoviesList = MoviesList;
        this.currentUser = currentUser;
        output.add(objectMapper.convertValue(this, JsonNode.class));
    }

    // write the output to a file
    public void writeOutput() {
        // TODO remove this line after you're done
        //output.add(objectMapper.convertValue("END OF TEST", JsonNode.class));
        try {
            this.objectWriter.writeValue(new File("results.out"), this.output);
            output = objectMapper.createArrayNode();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public List<Movie> getCurrentMoviesList() {
        return currentMoviesList;
    }

    public void setCurrentMoviesList(List<Movie> currentMoviesList) {
        this.currentMoviesList = currentMoviesList;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
}
