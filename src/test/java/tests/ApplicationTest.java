package tests;

import org.junit.jupiter.api.Test;
import server.Library;
import server.exceptions.DocumentNotFoundException;
import server.exceptions.SubscriberNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

class ApplicationTest {

    @Test
    void findAbonneByID() {
        Library lib = new Library();
        lib.initData();
        assertThrows(SubscriberNotFoundException.class, () -> {
            lib.findAbonneByID(0);
        });
    }

    @Test
    void findDocumentByID() {
        Library lib = new Library();
        lib.initData();
        assertThrows(DocumentNotFoundException.class, () -> {
            lib.findDocumentByID(0);
        });
    }
}