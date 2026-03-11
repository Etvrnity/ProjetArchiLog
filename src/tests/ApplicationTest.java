package tests;

import org.junit.jupiter.api.Test;
import server.Library;
import server.exceptions.DocumentNotFoundException;
import server.exceptions.SubscriberNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

class ApplicationTest {

    @Test
    void findAbonneFromID() {
        Library lib = new Library();
        lib.initData();
        assertThrows(SubscriberNotFoundException.class, () -> {
            lib.findAbonneFromID(0);
        });
    }

    @Test
    void findDocumentFromID() {
        Library lib = new Library();
        lib.initData();
        assertThrows(DocumentNotFoundException.class, () -> {
            lib.findDocumentFromID(0);
        });
    }
}