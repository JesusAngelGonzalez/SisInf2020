package sistemasinformacion.practica5;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.es.SpanishAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
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
	
	
	private static Directory directorioDelIndice;
	
	public ProgramaInteractivo(Collection<String> ficherosAIndexar) {
		this.ficherosAIndexar = ficherosAIndexar;
	}

			

	public static void main(String[] args) {
		Collection <String> ficheros = new ArrayList <String>();
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
					File carpeta = new File(peticion);
					if(carpeta.exists() && carpeta.isDirectory()) {
						parseDir(carpeta, ficheros);
					}
					else {
						System.out.println(peticion + " no existe o no es un directorio");
					}
					break;
				case 2:
					System.out.println("Introduzca un fichero: ");
					peticion = inputText.nextLine();
					
					File archivo = new File(peticion);
					
					if(archivo.exists() && !archivo.isDirectory()) {
						ficheros.add(peticion);
					}
					else {
						System.out.println(peticion + " es un directorio, por favor introduce un documento");
					}
					break;
				case 3:
					System.out.println("Introduzca una búsqueda: ");
					peticion = inputText.nextLine();
					ProgramaInteractivo ejecutar = new ProgramaInteractivo(ficheros);
					Directory directorioDelIndiceCreado = ejecutar.crearIndiceEnUnDirectorio();
					ejecutar.executeQuery(directorioDelIndiceCreado, ficheros.size(), 1, peticion);
					break;
				case 4:
					break;
				default:
					System.out.println("Introduzca un número entre 1 y 4.");	
				}
			}catch(Exception e){
				System.out.println("Introduzca un número por favor.");
			}
			System.out.println(menu);
		}
	}
	
	private static void parseDir(File directorio, Collection<String> ficherosIndexar) {
		for (File fichero : directorio.listFiles()) {
			if(fichero.isDirectory()) {
				parseDir(fichero,ficherosIndexar);
			}
			else {
				ficherosIndexar.add(fichero.getAbsolutePath());
			}
		}
	}

	private Directory crearIndiceEnUnDirectorio() throws IOException{
		IndexWriter indice = null;
		Directory directorioAlmacenarIndice = new MMapDirectory(Paths.get(INDEXDIR));

		IndexWriterConfig configuracionIndice = new IndexWriterConfig(analizador);

		indice = new IndexWriter(directorioAlmacenarIndice, configuracionIndice);
		
		for (String fichero : ficherosAIndexar) {
			anhadirFichero(indice, fichero);
		}
		
		indice.close();
		return directorioAlmacenarIndice;
	}
	
	private void anhadirFichero(IndexWriter indice, String path) 
			throws IOException {
				InputStream inputStream = new FileInputStream(path);
				BufferedReader inputStreamReader = new BufferedReader(
						new InputStreamReader(inputStream,"UTF-8"));
				
				Document doc = new Document();   
				doc.add(new TextField("contenido", inputStreamReader));
				doc.add(new StringField("path", path, Field.Store.YES));
				indice.addDocument(doc);
	}


	private static void executeQuery(Directory directorioDelIndice, int paginas, int hitsPorPagina, 
			String peticion) throws IOException {		
		DirectoryReader directoryReader = DirectoryReader.open(directorioDelIndice);
		IndexSearcher buscador = new IndexSearcher(directoryReader);
		
		QueryParser queryParser = new QueryParser("contenido", analizador); 
		Query query = null;
		try{
			query = queryParser.parse(peticion);
			TopDocs resultado = buscador.search(query, paginas * hitsPorPagina);
			ScoreDoc[] hits = resultado.scoreDocs;
		      
			System.out.println("\nBuscando " + peticion + ": Encontrados " + hits.length + " hits.");
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
