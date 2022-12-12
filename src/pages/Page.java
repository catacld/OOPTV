package pages;

import com.fasterxml.jackson.databind.node.ObjectNode;

public interface Page {

    public Page changePage(ObjectNode actionDetails);

    public Page onPage(ObjectNode actionDetails);
}
