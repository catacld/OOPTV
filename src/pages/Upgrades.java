package pages;

import com.fasterxml.jackson.databind.node.ObjectNode;

public class Upgrades implements Page{

    public Upgrades() {
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
