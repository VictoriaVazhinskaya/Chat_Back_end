package chat.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

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
import chat.storage.xml.historyOfRequests;
import chat.util.ServletUtil;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.xml.sax.SAXException;

import static chat.util.MessageUtil.*;

@WebServlet("/chat")
public class MessageServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static Logger logger = Logger.getLogger(MessageServlet.class.getName());

    @Override
    public void init() throws ServletException {
        try {
            loadHistory();
            historyOfRequests.createRequestsStorage();
        } catch (SAXException | IOException | ParserConfigurationException | TransformerException e) {
            logger.error(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws  ServletException, IOException {
        logger.info("doGet");
        String token = request.getParameter(TOKEN);
        logger.info("Token " + token);

        try {
            if (token != null && !"".equals(token)) {
                int index = getIndex(token);
                logger.info("Index " + index);
                String messages = "";
                if(index < 0) {
                    messages = formResponse(0);
                }
                else {
                    //System.out.println("METHOD" + method);
                    JSONObject jsonRequest = historyOfRequests.getRequestByIndex(index);
                    String method = (String)jsonRequest.get("method");
                    String  id = "";
                    if(!method.equals(""))
                        id = (String) jsonRequest.get("id");
                    messages = formResponseWithRequest(method, id);
                }
                response.setContentType(ServletUtil.APPLICATION_JSON);
                PrintWriter out = response.getWriter();

                out.print(messages);
                out.flush();
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "'token' parameter needed");
            }
        } catch (SAXException | ParserConfigurationException|XPathExpressionException e) {
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
            historyOfRequests.addRequest("post", message.getId());
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (ParseException | ParserConfigurationException | SAXException | TransformerException e) {
            logger.error(e);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("doDelete");
        String id = request.getParameter("id");
        logger.info("id = " + id);
        try {
            Message message = new Message("", "", "", id);
            XMLHistoryUtil.updateData(message);
            historyOfRequests.addRequest("delete", message.getId());
        } catch ( ParserConfigurationException | SAXException | TransformerException | XPathExpressionException e) {
            logger.error(e);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }


    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("doPut");
        String data = ServletUtil.getMessageBody(request);
        logger.info(data);
        try {
            JSONObject json = stringToJson(data);
            Message message = jsonToDefectiveMessage(json);
            XMLHistoryUtil.updateData(message);
            historyOfRequests.addRequest("put", message.getId());
        } catch (ParseException | ParserConfigurationException | SAXException | TransformerException | XPathExpressionException e) {
            logger.error(e);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @SuppressWarnings("unchecked")
    private String formResponse(int index) throws SAXException, IOException, ParserConfigurationException {
        JSONObject jsonObject = new JSONObject();
        List <Message> messages = new ArrayList<Message>(XMLHistoryUtil.getSubMessagesByIndex(index));
        //int size = messages.size();
        /*for(int i=0; i<size; i++)
           System.out.println(messages.get(i).onConsole());*/
        jsonObject.put(MESSAGES, messages);
        jsonObject.put(TOKEN, getToken(XMLHistoryUtil.getStorageSize()));
        jsonObject.put("requests", String.valueOf(historyOfRequests.getStorageSize()));
        jsonObject.put("method", "get");
        return jsonObject.toJSONString();
    }

    private String formResponseWithRequest(String method, String id) throws XPathExpressionException, ParserConfigurationException, SAXException, IOException {
        JSONObject jsonObject = new JSONObject();
        List <Message> messages = new ArrayList<Message>();
        Message message;
        if(!method.equals("")) {
            if (method.equals("delete"))
                message = new Message("", "", "", id);
            else {
                message = XMLHistoryUtil.getMessagesById(id);
            }
            messages.add(message);
        }
        jsonObject.put(MESSAGES, messages);
        jsonObject.put(TOKEN, getToken(XMLHistoryUtil.getStorageSize()));
        jsonObject.put("method", method);
        return jsonObject.toJSONString();
    }


    private void loadHistory() throws SAXException, IOException, ParserConfigurationException, TransformerException {
        if (!XMLHistoryUtil.doesStorageExist()) { // creating storage and history if not exist
            XMLHistoryUtil.createStorage();
            //addStubData();
        }
    }



}


