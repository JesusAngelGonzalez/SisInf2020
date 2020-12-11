package sistemasinformacion.practica5;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.es.SpanishAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.MMapDirectory;

public class ProgramaInteractivo {
	
	private static final String menu = "1 - Indexar un directorio\n" + 
			"2 - Añadir un documento al índice\n" + 
			"3 - Buscar un término\n" + 
			"4 - Salir\n" + 
			"Introduzca un número para ejecutar una acción:";
	/**
	 * Relación de ficheros a indexar / buscar
	 */
	private Collection <String> ficherosAIndexar = new ArrayList<String>();
	/**
	 * Relación de palabras clave a buscar
	 */
	private Collection <String> queries = new ArrayList <String>();
	/**
	 * Analizar utilizado por el indexador / buscador 
	 */
	private static Analyzer analizador =  new SpanishAnalyzer();
	
	private final static  String INDEXDIR = "./indice";
	
	private static Directory directorioDelIndice = new MMapDirectory(Paths.get(INDEXDIR));

			

	public static void main(String[] args) {
		System.out.println(menu);
		Scanner inputText = new Scanner(System.in);
		int action = 0;
		String peticion = "";
		while(action != 4) {
			try{
				action = Integer.parseInt(inputText.nextLine());
				switch(action) {
				case 1:
					System.out.println("Introduzca un directorio: ");
					peticion = inputText.nextLine();
					parseDir(peticion);
					break;
				case 2:
					System.out.println("Introduzca un fichero: ");
					peticion = inputText.nextLine();
					parseFile(peticion);
					break;
				case 3:
					System.out.println("Introduzca una búsqueda: ");
					peticion = inputText.nextLine();
					executeQuery(peticion);
					break;
				case 4:
					break;
				default:
					System.out.println("Introduzca un número entre 1 y 4, no me seas gilipollas.");	
				}
			}catch(Exception e){
				System.out.println("Introduzca un número por favor.");
			}
			System.out.println(menu);
		}
	}


	private static void parseDir(String peticion) {
		// TODO Auto-generated method stub
		
	}


	private static void parseFile(String peticion) {
		// TODO Auto-generated method stub
		
	}
	private Directory crearIndiceEnUnDirectorio() throws IOException{
		directorioAlmacenarIndice = new MMapDirectory(Paths.get(INDEXDIR));
	}


	private static void executeQuery(String peticion) {		
		DirectoryReader directoryReader = DirectoryReader.open(directorioDelIndice);
		IndexSearcher buscador = new IndexSearcher(directoryReader);
		
		QueryParser queryParser = new QueryParser("contenido", analizador); 
		Query query = null;
		try{
			query = queryParser.parse(peticion);
			TopDocs resultado = buscador.search(query, paginas * hitsPorPagina);
			ScoreDoc[] hits = resultado.scoreDocs;
		      
			System.out.println("\nBuscando " + queryAsString + ": Encontrados " + hits.length + " hits.");
			int i = 0;
			for (ScoreDoc hit: hits) {
				int docId = hit.doc;
				
				Document doc = buscador.doc(docId);
				System.out.println((++i) + ". " + doc.get("path") + "\t" + hit.score);
			}
	
		}catch (ParseException e){
			System.out.println();
		}		
	}

}
