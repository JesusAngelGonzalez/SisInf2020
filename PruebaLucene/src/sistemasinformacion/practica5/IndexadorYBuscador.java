package sistemasinformacion.practica5;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.document.TextField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.Field;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.es.SpanishAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.MMapDirectory;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.BufferedReader;
import java.io.File;


/**
 * Clase de ejemplo de un indexador y buscador usando Lucene
 * @author sisinf
 *
 */
public class IndexadorYBuscador{

	private static final String menu = "1 - Indexar un directorio\n" + 
			"2 - Añadir un documento al índice\n" + 
			"3 - Buscar un término\n" + 
			"4 - Salir\n" + 
			"Introduzca un número para ejecutar una acción:";
	
	/**
	 * Analizar utilizado por el indexador / buscador 
	 */
	private Analyzer analizador;
	
	private final static String INDEXDIR = "./ficheros/indice";
	private List<String> ficherosIndexados = new ArrayList<String>();
	private Directory directorio;

	/**
	 * Constructor parametrizado
	 * @param ficherosAIndexar Colección de ficheros a indexar
	 * @param queries Colección de palabras a buscar
	 */
	public IndexadorYBuscador(){
		analizador = new SpanishAnalyzer();

	
	}
	
	
	
	/**
	 * Añade un fichero al índice
	 * @param indice Indice que estamos construyendo
	 * @param path ruta del fichero a indexar
	 * @throws IOException
	 */
	private void anhadirFichero(IndexWriter indice, String path) 
	throws IOException {
		InputStream inputStream = new FileInputStream(path);
		BufferedReader inputStreamReader = new BufferedReader(
				new InputStreamReader(inputStream,"UTF-8"));
		
		Document doc = new Document();   
		doc.add(new TextField("contenido", inputStreamReader));
		doc.add(new StringField("path", path, Field.Store.YES));
		indice.addDocument(doc);
		ficherosIndexados.add(path);
	}
	
	
	
	/**
	 * Indexa los ficheros incluidos en "ficherosAIndexar"
	 * @return un índice (Directory) en memoria, con los índices de los ficheros
	 * @throws IOException
	 */
	private Directory crearIndiceDeUnDirectorio(String dir) throws IOException{
		IndexWriter indice = null;
		Directory directorioAlmacenarIndice = new MMapDirectory(Paths.get(INDEXDIR));

		IndexWriterConfig configuracionIndice = new IndexWriterConfig(analizador);

		indice = new IndexWriter(directorioAlmacenarIndice, configuracionIndice);
		
		File myDir = new File(dir);
		List<File> lFiles = new ArrayList<File>();
		List<String> ficherosAIndexar = new ArrayList<String>();
		
		lFiles.addAll(Arrays.asList(myDir.listFiles()));
		for(File file : lFiles) {
			if(file.isFile()) {
				ficherosAIndexar.add(file.getAbsolutePath());
			}else if(file.isDirectory()) {
				lFiles.addAll(Arrays.asList(file.listFiles()));
			}
		}
		
		for (String fichero : ficherosAIndexar) {
			if(!ficherosIndexados.contains(fichero)) {
				anhadirFichero(indice, fichero);
			}
		}
		
		indice.close();
		this.directorio = directorioAlmacenarIndice;
	}
	
	
	
	/**
	 * Busca la palabra indicada en queryAsString en el directorioDelIndice.
	 * @param directorioDelIndice
	 * @param paginas
	 * @param hitsPorPagina
	 * @param queryAsString
	 * @throws IOException
	 */
	private void buscarQueryEnIndice(Directory directorioDelIndice, 
										int paginas, 
										int hitsPorPagina, 
										String queryAsString)
	throws IOException{

		DirectoryReader directoryReader = DirectoryReader.open(directorioDelIndice);
		IndexSearcher buscador = new IndexSearcher(directoryReader);
		
		QueryParser queryParser = new QueryParser("contenido", analizador); 
		Query query = null;
		try{
			query = queryParser.parse(queryAsString);
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
			throw new IOException(e);
		}	
	}
	
	
	
	/**
	 * Ejecuta en el índice una búsqueda por cada una de las palabras clave solicitadas. <p>
	 * Las palabras clave solicitadas están en la propiedad global "queries". 
	 * @param directorioDelIndice índice
	 * @param paginas 
	 * @param hitsPorPagina
	 * @throws IOException
	 */
	private void buscarQueries(Directory directorioDelIndice, int paginas, int hitsPorPagina)
	throws IOException{
		for (String palabra : queries) {
			buscarQueryEnIndice(directorioDelIndice, 
								paginas, 
								hitsPorPagina, 
								palabra);			
		}
	}
	
	
	
	/**
	 * Programa principal de prueba. Rellena las colecciones "ficheros" y "queries"
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[]args) throws IOException{
		System.out.println(menu);
		Scanner inputText = new Scanner(System.in);
		int action = 0;
		String peticion = "";
		IndexadorYBuscador ejemplo = new IndexadorYBuscador();
		while(action != 4) {
			try{
				action = Integer.parseInt(inputText.nextLine());
				switch(action) {
				case 1:
					System.out.println("Introduzca un directorio: ");
					peticion = inputText.nextLine();
					ejemplo.crearIndiceDeUnDirectorio(peticion);
					break;
				case 2:
					System.out.println("Introduzca un fichero: ");
					peticion = inputText.nextLine();
					parseFile(peticion);
					break;
				case 3:
					System.out.println("Introduzca una búsqueda: ");
					peticion = inputText.nextLine();
					ejemplo.buscarQueries(peticion, 1);
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
		
		
		
		
		
		
		
		
		
		// Establecemos la lista de ficheros a indexar
		Collection <String> ficheros = new ArrayList <String>();
		ficheros.add("./ficheros/uno.txt");
		ficheros.add("./ficheros/dos.txt");
		ficheros.add("./ficheros/tres.txt");
		ficheros.add("./ficheros/cuatro.txt");

		// Establecemos las palabras clave a utilizar en la búsqueda
		Collection <String> queries = new ArrayList <String>();
		queries.add("Contaminación");
		queries.add("Contaminacion");
		queries.add("cambio climatico");
		queries.add("cambio climático");
		queries.add("cambio");
		queries.add("climatico");
		queries.add("climático");
		queries.add("por");
		queries.add("aeropuerto");

		
		
		// Abrimos un ficher indexado previamente
		//Directory directorioDelIndiceCreado = MMapDirectory.open(Paths.get(INDEXDIR));
		
		// Ejecutamos la búsqueda de las palabras clave
		ejemplo.buscarQueries(directorioDelIndiceCreado, ficheros.size(), 1);
	}
	
}


