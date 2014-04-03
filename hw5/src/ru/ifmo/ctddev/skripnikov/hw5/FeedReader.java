package ru.ifmo.ctddev.skripnikov.hw5;

import android.os.AsyncTask;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

public class FeedReader extends AsyncTask<String, Void, Void> {

    protected ArrayList<FeedItem> feed;

    @Override
    protected Void doInBackground(String... params) {
        try {
            setFeedByURL(params[0]);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void setFeedByURL(String stringUrl) throws IOException {
        URL url = new URL(stringUrl);
        HttpURLConnection connection = null;
        InputStream is = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.connect();
            is = connection.getInputStream();
            setFeedFromInputStream(is);
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } finally {
            if (is != null)
                is.close();
            if (connection != null)
                connection.disconnect();
        }
    }

    private void setFeedFromInputStream(InputStream is) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(is);
        if (doc.getElementsByTagName("channel").getLength() > 0) {
            setFeedFromDocument(doc, null, "item", "description");
        } else if (doc.getElementsByTagName("feed").getLength() > 0) {
            setFeedFromDocument(doc, "href", "entry", "summary");
        }
    }

    private void setFeedFromDocument(Document doc, String linkName, String itemName, String descriptionName) {
        feed = new ArrayList<FeedItem>();
        NodeList list = doc.getElementsByTagName(itemName);
        for (int i = 0; i < list.getLength(); i++) {
            NodeList children = list.item(i).getChildNodes();
            String link = null;
            String title = null;
            String description = null;
            for (int j = 0; j < children.getLength(); j++) {
                Node child = children.item(j);
                if ("link".equals(child.getNodeName())) {
                    if(linkName == null)
                        link = child.getTextContent();
                    else
                        link = child.getAttributes().getNamedItem(linkName).getNodeValue();
                } else if ("title".equals(child.getNodeName())) {
                    title = child.getTextContent();
                } else if (descriptionName.equals(child.getNodeName())) {
                    description = child.getTextContent();
                }
            }
            if (link != null && title != null && description != null)
                feed.add(new FeedItem(link, title, description));
        }
    }
}
