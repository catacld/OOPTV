package classes;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;


import javax.xml.crypto.Data;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class StreamingService {
    private String path;
    private ObjectMapper objectMapper;
    private ArrayNode output;
    private ObjectWriter objectWriter;
    public StreamingService(String path) {
        this.path = path;
        this.objectMapper = new ObjectMapper();
        this.output = this.objectMapper.createArrayNode();
        this.objectWriter = this.objectMapper.writerWithDefaultPrettyPrinter();
    }


    public void start() {

        Reader readData = new Reader(this.path);
        Database database = Database.getInstance();

        // add the list of users and movies in
        // the database
        database.setUsers(readData.readUsers());
        database.setMovies(readData.readMovies());

        // read the array of actions and execute them
        ActionsManager actionsManager = new ActionsManager(readData.readActions());
        actionsManager.manageActions();

        //output.add(objectMapper.convertValue(database.getUsers(), JsonNode.class));

        //write the output to the file
        Writer.getInstance().writeOutput();
    }
}