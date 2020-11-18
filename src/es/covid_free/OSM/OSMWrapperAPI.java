package es.covid_free.OSM;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class OSMWrapperAPI {
	private static final String NOMINATIM_API = "https://nominatim.openstreetmap.org/search?q=";
	
	public static String getCorrectaDireccion(String ubicacion, String lugar) throws IOException, ParserConfigurationException, SAXException {
		String query = NOMINATIM_API + ubicacion + ", " + lugar + "&format=xml&addressdetails=1&namedetails=1";
		
		URL osm = new URL(query);
		HttpURLConnection conn = (HttpURLConnection) osm.openConnection();
		
		DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
		Document document = docBuilder.parse(conn.getInputStream());
		
		NodeList nodos = document.getElementsByTagName("place");
		
		if( nodos.getLength() == 0 ) {
			System.out.println(nodos);
			return "";
		}
		
		Node info = nodos.item(0);
		String dir = "";
		
		for(  int i = 0; i < info.getChildNodes().getLength(); i++) {
			Node child = info.getChildNodes().item(i);
			if(child.getNodeName().equals("namedetails")) {
				//System.out.println("Name: " + child.getFirstChild().getNodeValue().getClass());
				dir += child.getFirstChild().getNodeValue();
			}else if(child.getNodeName().equals("road")) {
				dir += child.getNodeValue();
			}else  if(child.getNodeName().equals("city")) {
				dir += child.getNodeValue();
			}
		}
				
		return dir;
	}
	
	public static void main(String[] args) {
		try {
			System.out.println(getCorrectaDireccion("Juncos", "Zaragoza"));	
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
