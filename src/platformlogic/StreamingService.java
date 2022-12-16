package platformlogic;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import data.Database;
import ioclasses.Reader;
import ioclasses.Writer;

public class StreamingService {
    private String path;
    private ObjectMapper objectMapper;
    private ArrayNode output;
    private ObjectWriter objectWriter;
    public StreamingService(final String path) {
        this.path = path;
        this.objectMapper = new ObjectMapper();
        this.output = this.objectMapper.createArrayNode();
        this.objectWriter = this.objectMapper.writerWithDefaultPrettyPrinter();
    }


    /**
     * Start the program
     */
    public void start() {

        Reader readData = new Reader(this.path);
        Database database = Database.getInstance();

        // read and add the list of users and movies to
        // the database
        database.setUsers(readData.readUsers());
        database.setMovies(readData.readMovies());

        // read the array of actions and execute them
        ActionsManager actionsManager = new ActionsManager(readData.readActions());
        actionsManager.manageActions();

        //write the output to the file
        Writer.getInstance().writeOutput();
    }
}
