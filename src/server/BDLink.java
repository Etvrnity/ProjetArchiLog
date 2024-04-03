package server;

import io.github.cdimascio.dotenv.Dotenv;
import server.documents.Document;
import server.documents.types.Book;
import server.documents.types.DVD;
import server.exceptions.SubscriberNotFoundException;
import server.subscribers.Subscriber;

import java.sql.*;

public class BDLink {
    private Connection connection;
    public BDLink() {
        Dotenv dotenv = Dotenv.load();
        try {
            Class.forName("com.mysql.jdbc.Driver");
        }
        catch (ClassNotFoundException e1) {
            System.err.print("ClassNotFoundException: ");
            System.err.println(e1.getMessage());
        }
        try{
            connection = DriverManager.getConnection("jdbc:mysql://" + dotenv.get("DB_ADDR") + ":3306/" +
                    dotenv.get("DB_NAME"), dotenv.get("DB_USER"), dotenv.get("DB_PASSW"));
        }
        catch (SQLException e) {
            System.err.println("SQLException: " + e.getMessage());
        }
        Runtime.getRuntime().addShutdownHook(new Thread(this::closeDatabaseConnection));
    }

    private void closeDatabaseConnection() {
        try {
            connection.close();
            System.out.println("connection to BD closed");
        } catch (SQLException ignored) { }
    }

    public void init_local_from_bd(Library library) {
        try {
            Statement stmtSub = connection.createStatement();
            String querySub = "SELECT `id_subscriber`, `name`, `date_naissance` FROM `Subscriber`";
            ResultSet rsSub = stmtSub.executeQuery(querySub);

            Subscriber sub;

            while (rsSub.next()) {
                int id_subscriber= rsSub.getInt("id_subscriber") ;
                String name = rsSub.getString("name");
                Date date = rsSub.getDate("date_naissance");

                sub = new Subscriber(id_subscriber, name, date);
                library.addSubsciber(sub);
            }
            rsSub.close();


            Statement stmtDoc = connection.createStatement();
            String queryDoc = "SELECT `id_document`, `title`, `is_book`, `number_pages`, `pegi_16`, `id_subsciber` FROM `Document`";
            ResultSet rsDoc = stmtDoc.executeQuery(queryDoc);

            Document doc;

            while (rsDoc.next()) {
                int id_doc = rsDoc.getInt("id_document");
                String title = rsDoc.getString("title");

                Subscriber subscriber = null;
                int idSub = rsDoc.getInt("id_subsciber");
                if(!(rsDoc.wasNull())){
                    subscriber = library.findSubsciberFromID(idSub);
                }

                boolean borrowed = !(subscriber == null);
                boolean pegi_16 = rsDoc.getInt("pegi_16") == 0;
                int number_pages = rsDoc.getInt("number_pages");

                if(rsDoc.getString("is_book").equals("dvd")){
                    doc = new DVD(id_doc, title, subscriber, borrowed, pegi_16);
                    library.addDocument(doc);
                }
                else if(rsDoc.getString("is_book").equals("book")){
                    doc = new Book(id_doc, title, number_pages, subscriber, borrowed);
                    library.addDocument(doc);
                } else {
                    System.err.println("Error : the local library could not be initialized correctly");
                }
            }
            rsDoc.close();
        }
        catch (SQLException e) {
            System.err.println("SQLException: " + e.getLocalizedMessage());
        } catch (SubscriberNotFoundException e) {
            System.err.println("SubscriberNotFoundException: " + e.getLocalizedMessage());
        }
    }
}
