package org.voc5.backend;

import org.voc5.backend.data.Vocabulary;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JDBCWrapper {
    private static JDBCWrapper JDBC_WRAPPER = new JDBCWrapper();
    private Connection dbConnection;

    private JDBCWrapper() {
        String postgresIp = System.getenv("POSTGRES_IP");
        String postgresPassword = System.getenv("POSTGRES_PASSWORD");
        String postgresPort = System.getenv("POSTGRES_PORT");
        String postgresDb = System.getenv("POSTGRES_DB");
        String postgresSchema = System.getenv("POSTGRES_SCHEMA");
        postgresIp = postgresIp == null ? "localhost" : postgresIp;
        postgresPort = postgresPort == null ? "5432" : postgresPort;
        postgresDb = postgresDb == null ? "postgres" : postgresDb;
        postgresSchema = postgresSchema == null ? "public" : postgresSchema;
        postgresPassword = postgresPassword == null ? "postgres" : postgresPassword;
        // Create a URL that identifies database
        String url = "jdbc:postgresql://" + postgresIp + ":" + postgresPort + "/" + postgresDb + "?currentSchema=" + postgresSchema;

        // Now attempt to create a database connection
        try {
            dbConnection =
                    DriverManager.getConnection(url, "postgres", postgresPassword);
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static JDBCWrapper getInstance() {
        return JDBC_WRAPPER;
    }


    public IdAndPassword getPasswordHashFromUserAndId(String email) {
        PreparedStatement preparedStatement;
        try {
            preparedStatement = dbConnection.prepareStatement("Select id, password from account where email = ?;", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);

            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            boolean first = resultSet.first();
            if (!first) {
                return null;
            }
            int id = resultSet.getInt(1);
            String passwordHash = resultSet.getString(2);
            return new IdAndPassword(id, passwordHash);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void register(String email, String passwordHash) {
        try {
            PreparedStatement preparedStatement = dbConnection.prepareStatement("INSERT into account(email, password) VALUES(?,?);");

            preparedStatement.setString(1, email);
            preparedStatement.setString(2, passwordHash);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void connect() {
        //Will trigger constructor
    }

    public List<Vocabulary> getAllVocabulary(int userId) {
        try {
            PreparedStatement preparedStatement = dbConnection.prepareStatement("Select * from vocabulary where owner = ?;", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);

            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<Vocabulary> vocs = new ArrayList<>();
            while (resultSet.next()) {
                Vocabulary vocabulary = new Vocabulary(resultSet.getInt("id"), resultSet.getString("answer"), resultSet.getString("question"), resultSet.getString("language"), resultSet.getInt("phase"));
                vocs.add(vocabulary);
            }
            return vocs;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Vocabulary getRandomVoc(int userId) {
        try {
            PreparedStatement preparedStatement = dbConnection.prepareStatement("Select * from vocabulary where owner = ? order by random() limit 1;", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            boolean first = resultSet.first();
            if (!first) {
                return null;
            }
            return new Vocabulary(resultSet.getInt("id"), resultSet.getString("answer"), resultSet.getString("question"), resultSet.getString("language"), resultSet.getInt("phase"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean deleteVocabulary(int userId, int vocId) {
        try {
            PreparedStatement preparedStatement = dbConnection.prepareStatement("DELETE from vocabulary where owner = ? and id = ?;", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, vocId);
            return preparedStatement.executeUpdate() == 1;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Object createVoc(int userId, Vocabulary vocabulary) {
        try {
            PreparedStatement preparedStatement = dbConnection.prepareStatement("INSERT into vocabulary(owner,answer,language,question) VALUES(?,?,?,?);");

            preparedStatement.setInt(1, userId);
            preparedStatement.setString(2, vocabulary.getAnswer());
            preparedStatement.setString(3, vocabulary.getLanguage());
            preparedStatement.setString(4, vocabulary.getQuestion());
            return preparedStatement.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void patchVocabulary(int userId, Vocabulary vocabulary) {
        try {
            if (vocabulary.getAnswer() != null) {
                PreparedStatement preparedStatement = dbConnection.prepareStatement("UPDATE  vocabulary set answer = ? where owner = ? and id= ?;");

                preparedStatement.setInt(2, userId);
                preparedStatement.setInt(3, vocabulary.getId());
                preparedStatement.setString(1, vocabulary.getAnswer());
                preparedStatement.executeUpdate();
            }
            if (vocabulary.getQuestion() != null) {
                PreparedStatement preparedStatement = dbConnection.prepareStatement("UPDATE  vocabulary set question = ? where owner = ? and id= ?;");

                preparedStatement.setInt(2, userId);
                preparedStatement.setInt(3, vocabulary.getId());
                preparedStatement.setString(1, vocabulary.getQuestion());
                preparedStatement.executeUpdate();
            }
            if (vocabulary.getLanguage() != null) {
                PreparedStatement preparedStatement = dbConnection.prepareStatement("UPDATE  vocabulary set language = ? where owner = ? and id= ?;");

                preparedStatement.setInt(2, userId);
                preparedStatement.setInt(3, vocabulary.getId());
                preparedStatement.setString(1, vocabulary.getLanguage());
                preparedStatement.executeUpdate();
            }
            if (vocabulary.getPhase() != null) {
                PreparedStatement preparedStatement = dbConnection.prepareStatement("UPDATE  vocabulary set phase = ? where owner = ? and id= ?;");

                preparedStatement.setInt(2, userId);
                preparedStatement.setInt(3, vocabulary.getId());
                preparedStatement.setInt(1, vocabulary.getPhase());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static class IdAndPassword {
        private final int id;
        private final String passwordHash;

        public IdAndPassword(int id, String passwordHash) {
            this.id = id;
            this.passwordHash = passwordHash;
        }

        public int getId() {
            return id;
        }

        public String getPasswordHash() {
            return passwordHash;
        }
    }
}
