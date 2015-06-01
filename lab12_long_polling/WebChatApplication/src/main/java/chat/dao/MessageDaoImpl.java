package chat.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import chat.db.ConnectionPool;
import chat.model.Message;

public class MessageDaoImpl implements MessageDao {
    private static Logger logger = Logger.getLogger(MessageDaoImpl.class.getName());

    @Override
    public void add(Message message) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        String user_id = getIdByAuthor(message.getAuthor());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if(user_id.equals("")) {
            user_id = String.valueOf(uniqueId());
            addUser(user_id, message.getAuthor());
        }
        try {
            connection = ConnectionPool.getConnection();
            preparedStatement = connection.prepareStatement("INSERT INTO messages  VALUES (?, ?, ?, ?)");
            preparedStatement.setString(1, message.getId());
            preparedStatement.setString(2, message.getText());
            preparedStatement.setTimestamp(3, java.sql.Timestamp.valueOf(message.getDate()));
            preparedStatement.setString(4, user_id);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            logger.error(e);
        } finally
           {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    logger.error(e);
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    logger.error(e);
                }
            }
        }
    }

    @Override
    public void addUser(String id, String name){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try{
            connection = ConnectionPool.getConnection();
            preparedStatement = connection.prepareStatement("INSERT INTO users  VALUES (?, ?)");
            preparedStatement.setString(1, id);
            preparedStatement.setString(2, name);
            preparedStatement.executeUpdate();
        }
        catch (SQLException e) {
            logger.error(e);
        } finally
        {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    logger.error(e);
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    logger.error(e);
                }
            }
        }
    }

    @Override
    public void update(Message message) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = ConnectionPool.getConnection();
            preparedStatement = connection.prepareStatement("Update messages SET  text = ?  WHERE id = ?");
            preparedStatement.setString(1, message.getText());
            preparedStatement.setString(2, message.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.error(e);
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    logger.error(e);
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    logger.error(e);
                }
            }
        }
    }

    @Override
    public Message selectById(String id) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Message message = new Message("", "", "", "");
        try {
            connection = ConnectionPool.getConnection();
            preparedStatement = connection.prepareStatement("SELECT * FROM messages WHERE id = ?");
            preparedStatement.setString(1, id);
            resultSet = preparedStatement.executeQuery();
            resultSet.next();
            String text = resultSet.getString("text");
            String date = dateFormat.format(resultSet.getDate("date"));
            String user_id = resultSet.getString("user_id");
            String author = getAuthorById(user_id);
            message.setAuthor(author);
            message.setText(text);
            message.setDate(date);
            message.setId(id);
        } catch (SQLException e) {
            logger.error(e);
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    logger.error(e);
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    logger.error(e);
                }
            }
        }
        return message;
    }

    @Override
    public String getAuthorById(String id) {
        String author = "";
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = ConnectionPool.getConnection();
            preparedStatement = connection.prepareStatement("SELECT name FROM users WHERE id = ?");
            preparedStatement.setString(1, id);
            resultSet = preparedStatement.executeQuery();
            resultSet.next();
            author = resultSet.getString("name");

        } catch (SQLException e) {
            logger.error(e);
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    logger.error(e);
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    logger.error(e);
                }
            }
        }

        return author;
    }

    @Override
    public String getIdByAuthor(String author) {
        String id = "";
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = ConnectionPool.getConnection();
            preparedStatement = connection.prepareStatement("SELECT id FROM users WHERE name = ?");
            preparedStatement.setString(1, author);
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next())
            id = resultSet.getString("id");

        } catch (SQLException e) {
            logger.error(e);
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    logger.error(e);
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    logger.error(e);
                }
            }
        }

        return id;
    }

    @Override
    public List<Message> selectAll() {
        List<Message> messages = new ArrayList<>();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            connection = ConnectionPool.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM messages ORDER  BY date");
            while (resultSet.next()) {
                String id = resultSet.getString("id");
                String text = resultSet.getString("text");
                String date = dateFormat.format(resultSet.getDate("date"));
                String user_id =  resultSet.getString("user_id");
                String author = getAuthorById(user_id);
                messages.add(new Message(author, text, date, id));
            }
        } catch (SQLException e) {
            logger.error(e);
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    logger.error(e);
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    logger.error(e);
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    logger.error(e);
                }
            }
        }
        return messages;
    }

    @Override
    public void delete(String id) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = ConnectionPool.getConnection();
            preparedStatement = connection.prepareStatement("DELETE  FROM  messages  WHERE id = ?");
            preparedStatement.setString(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.error(e);
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    logger.error(e);
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    logger.error(e);
                }
            }
        }
    }

    @Override
    public int getSizeOfMessages(){
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        int size = 0;
        try {
            connection = ConnectionPool.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT COUNT(*) AS total FROM messages");
            resultSet.next();
            size = resultSet.getInt("total");
        } catch (SQLException e) {
            logger.error(e);
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    logger.error(e);
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    logger.error(e);
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    logger.error(e);
                }
            }
        }
        return size;
    }

    private long uniqueId(){
        Date date = new Date();
        double rand = Math.random() * Math.random();
        return Math.round(date.getTime()*rand);
    }

}



