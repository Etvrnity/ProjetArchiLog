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
        Library lib = new Library();
        BDLink bdLink = new BDLink();
        bdLink.init_local_from_bd(lib);
        assertThrows(SubscriberNotFoundException.class, () -> {lib.findSubsciberFromID(0);});
    }

    @Test
    void findDocumentFromID() {
        Library lib = new Library();
        BDLink bdLink = new BDLink();
        bdLink.init_local_from_bd(lib);
        assertThrows(DocumentNotFoundException.class, () -> {lib.findDocumentFromID(0);});
    }
}