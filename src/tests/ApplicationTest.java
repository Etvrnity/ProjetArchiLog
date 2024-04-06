package tests;

import org.junit.jupiter.api.Test;
import server.BDLink;
import server.Library;
import server.exceptions.DocumentNotFoundException;
import server.exceptions.SubscriberNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

class ApplicationTest {

    @Test
    void findSubsciberFromID() {
        BDLink bdLink = new BDLink();
        Library lib = new Library(bdLink);
        lib.init_local_from_bd();
        assertThrows(SubscriberNotFoundException.class, () -> {lib.findSubsciberFromID(0);});
    }

    @Test
    void findDocumentFromID() {
        BDLink bdLink = new BDLink();
        Library lib = new Library(bdLink);
        lib.init_local_from_bd();
        assertThrows(DocumentNotFoundException.class, () -> {lib.findDocumentFromID(0);});
    }
}