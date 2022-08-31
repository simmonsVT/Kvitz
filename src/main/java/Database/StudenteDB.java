package Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import Model.*;
import PostgresDAO.StudenteDB_DAO;

import static Database.ConnessioneDatabase.docenti_table;
import static Database.ConnessioneDatabase.studenti_table;

public class StudenteDB implements StudenteDB_DAO {

    @Override
    public ArrayList<Studente> elenco_studenti(Connection connection) {

        try {
            if (connection != null && !connection.isClosed() ){
                String  query = "SELECT * FROM "+studenti_table ;

                PreparedStatement query_obj = connection.prepareStatement(query);

                ResultSet ris = query_obj.executeQuery();
                Studente s = new Studente();
                ArrayList<Studente> studenti_list = new ArrayList();
                while (ris.next()) {
                    s.setNome(ris.getString("nome"));
                    s.setCognome(ris.getString("cognome"));
                    s.setUsername(ris.getString("username"));
                    s.setPassword(ris.getString("password"));

                    studenti_list.add(s);

                }
                return studenti_list;
            }
            else
                return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }


    }

    public Studente get_studente(Connection c, String username) {
        try {
            if (c != null && !c.isClosed()) {
                String query = "SELECT * FROM " + studenti_table + " WHERE username LIKE '%" + username + "%'";

                PreparedStatement query_obj = c.prepareStatement(query);

                ResultSet ris = query_obj.executeQuery();
                Studente s = new Studente();
                while (ris.next()) {
                    s.setNome(ris.getString("nome"));
                    s.setCognome(ris.getString("cognome"));
                    s.setUsername(ris.getString("username"));
                    s.setPassword(ris.getString("password"));

                    return s;
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
