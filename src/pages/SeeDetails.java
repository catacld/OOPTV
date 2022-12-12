package pages;

import classes.Movie;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class SeeDetails implements Page{

    private final Movie detailsOfMovie;

    public SeeDetails(Movie detailsOfMovie) {
        this.detailsOfMovie = detailsOfMovie;
    }

    @Override
    public Page changePage(ObjectNode actionDetails) {
        return this;
    }

    @Override
    public Page onPage(ObjectNode actionDetails) {
        return this;
    }
}
