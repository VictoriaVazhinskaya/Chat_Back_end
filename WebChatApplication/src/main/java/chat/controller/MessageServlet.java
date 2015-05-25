package chat.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import static chat.util.MessageUtil.MESSAGES;
import static chat.util.MessageUtil.TOKEN;
import static chat.util.MessageUtil.getIndex;
import static chat.util.MessageUtil.getToken;
import static chat.util.MessageUtil.jsonToMessage;
import static chat.util.MessageUtil.stringToJson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;


import chat.model.Message;

import org.apache.log4j.Logger;
import java.util.Date;
import java.text.SimpleDateFormat;
import chat.model.Message;
import chat.storage.xml.XMLHistoryUtil;
import chat.util.ServletUtil;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.xml.sax.SAXException;

@WebServlet("/chat")
public class MessageServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static Logger logger = Logger.getLogger(MessageServlet.class.getName());

    @Override
    public void init() throws ServletException {
        try {
            loadHistory();
        } catch (SAXException | IOException | ParserConfigurationException | TransformerException e) {
            logger.error(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("doGet");
        String token = request.getParameter(TOKEN);
        logger.info("Token " + token);

        try {
            if (token != null && !"".equals(token)) {
                int index = getIndex(token);
                logger.info("Index " + index);
                String messages;
                messages = formResponse(index);
                //System.out.println(messages);
                response.setContentType(ServletUtil.APPLICATION_JSON);
                PrintWriter out = response.getWriter();
                out.print(messages);
                out.flush();
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "'token' parameter needed");
            }
        } catch (SAXException | ParserConfigurationException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("doPost");
        String data = ServletUtil.getMessageBody(request);
        logger.info(data);
        try {
            JSONObject json = stringToJson(data);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yy-MM-dd  HH:mm");
            Message message = jsonToMessage(json);
            String date =  dateFormat.format(new Date());
            message.setDate(date);
            System.out.println(date + " " + message.getAuthor() + " : " + message.getText());
            XMLHistoryUtil.addData(message);
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (ParseException | ParserConfigurationException | SAXException | TransformerException e) {
            logger.error(e);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    /*@Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("doPut");
        String data = ServletUtil.getMessageBody(request);
        logger.info(data);
        try {
            JSONObject json = stringToJson(data);
            Task task = jsonToTask(json);
            String id = task.getId();
            Task taskToUpdate = TaskStorage.getTaskById(id);
            if (taskToUpdate != null) {
                taskToUpdate.setDescription(task.getDescription());
                taskToUpdate.setDone(task.isDone());
                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Task does not exist");
            }
        } catch (ParseException e) {
            logger.error(e);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }*/

    @SuppressWarnings("unchecked")
    private String formResponse(int index) throws SAXException, IOException, ParserConfigurationException {
        JSONObject jsonObject = new JSONObject();
        List <Message> messages = new ArrayList<Message>(XMLHistoryUtil.getSubMessagesByIndex(index));
        int size = messages.size();
        for(int i=0; i<size; i++)
            System.out.println(messages.get(i).onConsole());
        jsonObject.put(MESSAGES, messages);
        jsonObject.put(TOKEN, getToken(XMLHistoryUtil.getStorageSize()));
        return jsonObject.toJSONString();
    }

    private void loadHistory() throws SAXException, IOException, ParserConfigurationException, TransformerException {
        if (!XMLHistoryUtil.doesStorageExist()) { // creating storage and history if not exist
            XMLHistoryUtil.createStorage();
            //addStubData();
        }
    }

    /*private void addStubData() throws ParserConfigurationException, TransformerException {
        Message[] stubMessages = {
                new Message("Victoria", "restructuring")};
        for (Message message : stubMessages) {
            try {
                XMLHistoryUtil.addData(message);
            } catch (ParserConfigurationException | SAXException | IOException | TransformerException e) {
                logger.error(e);
            }
        }
    }*/

}


