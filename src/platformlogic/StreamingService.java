package platformlogic;


import data.Database;
import ioclasses.Reader;
import ioclasses.Writer;

public class StreamingService {
    private final String path;

    public StreamingService(final String path) {
        this.path = path;
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

        // after executing all the actions
        // recommend a movie if the
        // connected user is premium
        actionsManager.recommend();

        //write the output to the file
        Writer.getInstance().writeOutput();
    }
}
