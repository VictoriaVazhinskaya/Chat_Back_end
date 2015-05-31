package chat.dao;

import java.util.List;
import chat.model.Message;


public interface MessageDao {
    void add(Message message);

    void addUser(String id, String name);

    void update(Message message);

    String getIdByAuthor(String author);

    String getAuthorById(String id);

    void delete(String id);

    Message selectById(String id);

    List<Message> selectAll();
}
