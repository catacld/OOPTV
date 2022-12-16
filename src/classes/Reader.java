package classes;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Reader {

    private String path;

    private ObjectMapper objectMapper;

    private JsonNode node;

    public Reader(String path) {
        this.path = path;
        this.objectMapper = new ObjectMapper();
    }

    public List<User> readUsers() {

        List<User> users;

        try {
            this.node = this.objectMapper.readTree(new File(this.path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        this.node = this.node.get("users");
        TypeReference<List<User>> refList = new TypeReference<>() {};

        try {
            users = this.objectMapper.readValue(this.node.traverse(), refList);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return users;
    }

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

    public List<ObjectNode> readActions() {

        List<ObjectNode> actions;

        try {
            this.node = this.objectMapper.readTree(new File(this.path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        this.node = this.node.get("actions");
        TypeReference<List<ObjectNode>> refList = new TypeReference<>() {};

        try {
            actions = this.objectMapper.readValue(this.node.traverse(), refList);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return actions;

    }



}
