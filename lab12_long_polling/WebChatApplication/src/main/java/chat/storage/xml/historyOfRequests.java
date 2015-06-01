package chat.storage.xml;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import chat.model.Message;
import org.json.simple.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class historyOfRequests {
    private static final String STORAGE_LOCATION = "D:\\" + "historyOfRequests.xml"; // history.xml will be located in the home directory
    private static final String REQUESTS = "requests";
    private static final String REQUEST = "request";
    private static final String POST = "post";
    private static final String DELETE = "delete";
    private static final String PUT = "put";
    private static final String ID = "id";
    private static int counter = 0;

    historyOfRequests(){}

    public static synchronized void createRequestsStorage() throws ParserConfigurationException, TransformerException {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        Document doc = docBuilder.newDocument();
        Element rootElement = doc.createElement(REQUESTS);
        doc.appendChild(rootElement);
        Transformer transformer = getTransformer();

        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File(STORAGE_LOCATION));
        transformer.transform(source, result);
    }

    public static synchronized void addRequest(String method, String id) throws ParserConfigurationException, SAXException, IOException, TransformerException {

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(STORAGE_LOCATION);
        document.getDocumentElement().normalize();

        Element root = document.getDocumentElement();

        Element request = document.createElement(REQUEST);
        request.setAttribute("method", method);
        request.appendChild(document.createTextNode(id));
        root.appendChild(request);

        DOMSource source = new DOMSource(document);
        counter++;
        Transformer transformer = getTransformer();

        StreamResult result = new StreamResult(STORAGE_LOCATION);
        transformer.transform(source, result);
    }

    public static synchronized JSONObject getRequestByIndex(int index) throws ParserConfigurationException, SAXException, IOException {
        JSONObject jsonObject = new JSONObject();
        //System.out.println("INDEX " + index +", COUNTER " + counter);
        if(index < counter) {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(STORAGE_LOCATION);
            document.getDocumentElement().normalize();
            Element root = document.getDocumentElement(); // Root <tasks> element
            NodeList requestList = root.getElementsByTagName(REQUEST);
            Element request = (Element) requestList.item(index);
            //System.out.println(request);
            String method = request.getAttribute("method");
            String id = request.getTextContent();
            jsonObject.put("method", method);
            jsonObject.put("id", id);
        }
        else {
            jsonObject.put("method", "");
        }

        return  jsonObject;
    }

    public static synchronized int getStorageSize() throws SAXException, IOException, ParserConfigurationException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(STORAGE_LOCATION);
        document.getDocumentElement().normalize();
        Element root = document.getDocumentElement(); // Root <tasks> element
        return root.getElementsByTagName(REQUEST).getLength();
    }

    private static Transformer getTransformer() throws TransformerConfigurationException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        // Formatting XML properly
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        return transformer;
    }
    
    public static int getCounter(){
        return counter;
    }
}
