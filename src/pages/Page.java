package pages;

import com.fasterxml.jackson.databind.node.ObjectNode;

public interface Page {

    /**
     * Change the page from the current page to
     * another page
     * @param actionDetails node where the details of the
     *                      action are stored
     * @return the destination page
     */
    Page changePage(ObjectNode actionDetails);

    /**
     * Execute an "on page" action
     * @param actionDetails node where the details of the
     *                      action are stored
     * @return the same page
     */
    Page onPage(ObjectNode actionDetails);
}
