package server;

import io.github.cdimascio.dotenv.Dotenv;
import server.documents.Document;
import server.documents.types.Book;
import server.documents.types.DVD;
import server.subscribers.Subscriber;

import java.sql.*;
import java.util.ArrayList;

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

    public void init_local_from_bd(ArrayList<Subscriber> subscribers, ArrayList<Document> documents) {
        try {
            Statement stmtSub = connection.createStatement();
            String querySub = "SELECT `id_subscriber`, `name`, `date_naissance` FROM `Subsciber`";
            ResultSet rsSub = stmtSub.executeQuery(querySub);

            Subscriber sub;

            while (rsSub.next()) {
                int id_subscriber= rsSub.getInt("id_subscriber") ;
                String name = rsSub.getString("name");
                Date date = rsSub.getDate("date_naissance");

                sub = new Subscriber(id_subscriber, name, date);
                subscribers.add(sub);
            }
            rsSub.close();


            Statement stmtDoc = connection.createStatement();
            String queryDoc = "SELECT `id_document`, `title`, `is_book`, `number_pages`, `pegi_16`, `id_subsciber` FROM `Document`";
            ResultSet rsDoc = stmtDoc.executeQuery(queryDoc);

            Document doc;

            while (rsDoc.next()) {
                int id_doc = rsDoc.getInt("id_document");
                String title = rsDoc.getString("title");
                Subscriber subscriber = null; //TODO
                boolean borrowed = false; //TODO
                boolean pegi_16 = rsDoc.getInt("pegi_16") == 0;
                int number_pages = rsDoc.getInt("number_pages");

                if(rsDoc.getString("is_book").equals("dvd")){
                    doc = new DVD(id_doc, title, subscriber, borrowed, pegi_16);
                    documents.add(doc);
                }
                else if(rsDoc.getString("is_book").equals("book")){
                    doc = new Book(id_doc, title, number_pages,subscriber, borrowed);
                    documents.add(doc);
                } else {
                    System.err.println("error");//TODO
                }
            }
            rsDoc.close();
        }
        catch (SQLException e) {
            System.err.println("SQLException: " + e.getMessage());
        }
    }
}
