package es.covid_free.OSM;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class OSMWrapperAPI {
	private static final String NOMINATIM_API = "https://nominatim.openstreetmap.org/search?q=";
	
	public static String getCorrectUbicacion(String name) throws IOException, ParserConfigurationException, SAXException {
		String query = NOMINATIM_API + name + "&format=xml&addressdetails=1&namedetails=1";
		
		URL osm = new URL(query);
		HttpURLConnection conn = (HttpURLConnection) osm.openConnection();
		
		DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
		Document document = docBuilder.parse(conn.getInputStream());
		
		return document.
	}
}
