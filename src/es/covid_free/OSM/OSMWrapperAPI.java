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


/**
 * Clase para interactuar con OSM
 * @author covid_free
 *
 */

public class OSMWrapperAPI {
	private static final String NOMINATIM_API = "https://nominatim.openstreetmap.org/search?q=";
	
	
	/**
	 * Dado el nombre de un lugar y su dirección, devolverá en un string una forma única
	 * de representar a dicho lugar según OpenStreetMap. Ese string será vacio si no hay
	 * ningún lugar o tres campos separados por comas siendo el primero el nombre del
	 * sitio, el segundo la calle y el tercero la ciudad.
	 * 
	 * 
	 * @param ubicacion Nombre del lugar a buscar
	 * @param lugar 	Calle, ciudad u otro parametro para identificar el lugar
	 * @return			Devuelve la dirección o vacio si no se ha encontrado
	 * @throws IOException Lanzada al establecer una HttpURLConnection por la clase URL o el parser de DocumentBuilder
	 * @throws ParserConfigurationException Lanzado por newDocumentBuilder() del Factory
	 * @throws SAXException Lanzada por el parser 
	 */
	public static String getCorrectaDireccion(String ubicacion, String lugar) throws IOException, ParserConfigurationException, SAXException {
		//Se genera la URL a consultar
		String query = NOMINATIM_API + ubicacion + ", " + lugar + "&format=xml&addressdetails=1&namedetails=1";
		
		//Se establece una conexión URL
		URL osm = new URL(query);
		HttpURLConnection conn = (HttpURLConnection) osm.openConnection();
		
		//Se obtiene el documento de respuesta de la consulta
		DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
		Document document = docBuilder.parse(conn.getInputStream());
		
		//Se extrae el nodo del XML con nombre "place"
		NodeList nodos = document.getElementsByTagName("place");
		
		//En caso de que no haya ningún nodo se devuelve la cadena vacía
		if( nodos.getLength() == 0 ) {
			System.out.println(nodos);
			return "";
		}
		
		//Se obtiene la información del primer nodo
		Node info = nodos.item(0);
		String dir = "";
		
		//Se itera por los hijos del nodo para agregar su información a devolver
		for(  int i = 0; i < info.getChildNodes().getLength(); i++) {
			Node child = info.getChildNodes().item(i);
			if(child.getNodeName().equals("namedetails")) {
				dir += child.getFirstChild().getTextContent();
			}else if(child.getNodeName().equals("road")) {
				dir += "," + child.getTextContent();
			}else  if(child.getNodeName().equals("city")) {
				dir += "," + child.getTextContent();
			}
		}
				
		return dir;
	}
	
	/**
	 * Ejecución de ejemplo del OSMWrapperAPI
	 */
	
	public static void main(String[] args) {
		try {
			System.out.println(getCorrectaDireccion("Juncos", "Zaragoza"));	
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}