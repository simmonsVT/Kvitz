package Database;

import Model.Docente;
import PostgresDAO.DocenteDBDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static Database.ConnessioneDatabase.docenti_table;

public class DocenteDB implements DocenteDBDAO {


    public ArrayList<Docente> elenco_docenti(Connection connection) {
        try {
            if (connection != null && !connection.isClosed()) {
                String query = "SELECT * FROM " + docenti_table;

                PreparedStatement query_obj = connection.prepareStatement(query);

                ResultSet ris = query_obj.executeQuery();
                Docente d = new Docente();
                ArrayList<Docente> docenti_list = new ArrayList();
                while (ris.next()) {
                    d.setNome(ris.getString("nome"));
                    d.setCognome(ris.getString("cognome"));
                    d.setUsername(ris.getString("username"));
                    d.setPassword(ris.getString("password"));

                    docenti_list.add(d);

                }
                return docenti_list;
            } else
                return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }


    }

    public Docente get_docente(Connection c, String username) {
        try {
            if (c != null && !c.isClosed()) {
                String query = "SELECT * FROM " + docenti_table + " WHERE username LIKE '" + username+"'";

                PreparedStatement query_obj = c.prepareStatement(query);

                ResultSet ris = query_obj.executeQuery();
                Docente d = new Docente();
                while (ris.next()) {
                    d.setNome(ris.getString("nome"));
                    d.setCognome(ris.getString("cognome"));
                    d.setUsername(ris.getString("username"));
                    d.setPassword(ris.getString("password"));

                    return d;
                }
            } else
                return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }
}