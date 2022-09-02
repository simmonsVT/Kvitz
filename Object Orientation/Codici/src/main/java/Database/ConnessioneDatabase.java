package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class ConnessioneDatabase {

        private static String jdbcURL = "jdbc:postgresql://localhost:5432/progetto2022";
        private static String username = "postgres";
        private static String password = "admin";

        public static String studenti_table = "studenti";
        public static String docenti_table = "docenti";
        public static String test_table = "test";
        public static String test_svolti_table = "test_svolti";
        public static String quiz_multiplo_table = "quiz_multiplo";
        public static String quiz_aperto_table = "quiz_aperto";
        public static String quiz_svolti_aperti_table = "quiz_svolti_aperti";
        public static String quiz_svolti_multipli_table = "quiz_svolti_multipli";





    private static Connection connection = null;
        public static Connection connect_db() {
            try {
                Class.forName("org.postgresql.Driver");
                connection = DriverManager.getConnection(jdbcURL, username, password);
                System.out.println("Connected to PostgreSQL server");
                return connection;
            } catch (SQLException  | ClassNotFoundException e) {
                System.out.println("Error in connecting to PostgreSQL server");
                e.printStackTrace();
                return null;
            }
        }



}