package ioclasses;

import classes.Movie;
import classes.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Reader {

    private final String path;

    private final ObjectMapper objectMapper;

    private JsonNode node;

    public Reader(final String path) {
        this.path = path;
        this.objectMapper = new ObjectMapper();
    }


    /**
     * Read the list of users from the input file
     */
    public List<User> readUsers() {

        List<User> users;

        try {
            this.node = this.objectMapper.readTree(new File(this.path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        this.node = this.node.get("users");
        TypeReference<List<User>> refList = new TypeReference<>() {

        };

        try {
            users = this.objectMapper.readValue(this.node.traverse(), refList);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return users;
    }


    /**
     * Read the list of movies from the input file
     */
    public List<Movie> readMovies() {

        List<Movie> movies;

        try {
            this.node = this.objectMapper.readTree(new File(this.path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        this.node = this.node.get("movies");
        TypeReference<List<Movie>> refList = new TypeReference<>() {
        };

        try {
            movies = this.objectMapper.readValue(this.node.traverse(), refList);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return movies;
    }


    /**
     * Read the list of actions from the input file
     */
    public List<ObjectNode> readActions() {

        List<ObjectNode> actions;

        try {
            this.node = this.objectMapper.readTree(new File(this.path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        this.node = this.node.get("actions");
        TypeReference<List<ObjectNode>> refList = new TypeReference<>() {

        };

        try {
            actions = this.objectMapper.readValue(this.node.traverse(), refList);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return actions;

    }



}
