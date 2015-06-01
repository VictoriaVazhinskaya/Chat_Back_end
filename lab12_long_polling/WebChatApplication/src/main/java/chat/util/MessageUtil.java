package chat.util;

import chat.model.Message;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public final class MessageUtil {
    public static final String TOKEN = "token";
    public static final String MESSAGES = "messages";
    private static final String TN = "TN";
    private static final String EN = "EN";
    private static final String AUTHOR = "author";
    private static final String TEXT = "text";
    private static final String ID = "id";

    private MessageUtil() {
    }

    public static String getToken(int index) {
        Integer number = index * 8 + 11;
        return TN + number + EN;
    }

    public static int getIndex(String token) {
        int index = Integer.parseInt(token.substring(2, token.length() - 2));
        if (index == 10)
            return -1;
        else return (index - 11)/8;
    }

    public static JSONObject stringToJson(String data) throws ParseException {
        JSONParser parser = new JSONParser();
        return (JSONObject) parser.parse(data.trim());
    }

    public static Message jsonToMessage(JSONObject json) {
        Object author = json.get(AUTHOR);
        Object message = json.get(TEXT);
        Object id = json.get(ID);
        if (author != null && message != null) {
            return new Message((String) author, (String) message, "", (String)id);
        }
        return null;
    }

    public static Message jsonToDefectiveMessage(JSONObject json) {
       // Object author = json.get(AUTHOR);
        Object text = json.get(TEXT);
        Object id = json.get(ID);
        if (text != null) {
            return new Message("author", (String) text, "date", (String)id);
        }
        return null;
    }

}