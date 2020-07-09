import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import java.util.*;
// skopiować zawartość tego pliku do wikiImporter
// sprawdzić jak program jest wywoływany z konsoli z argumentem
// można zrobić obsługę wyjątków dla każdeogo wyrażenia typu throws exception
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Klasyfikator extends Classifier{
	private String [] tablicaKategorii; // tablica własnych kategori (kryteriów), które będą wyszukiwane w tekście zawierającym kategorie
	//private ArrayList<Page> listaOsób;
	
	//private String tekst; // tekst zawierający wszystkie kategorie z danego artykułu
	//private boolean czyOpisujeOsobę; 
	
	public Klasyfikator(String [] array){
		this.tablicaKategorii = array;
		//this.listaOsób = new ArrayList<Page>();
	}
	
	public boolean sprawdźArtykuł(Page p){
		if (this.containsPersondata(p)) return true;
		else{
			for(String kategoria: this.tablicaKategorii){
				if(this.containsCategory(p.getCategories(), kategoria)) return true; // p.getCategories() - metoda, która zwraca tekst zawierający wszystkie kategorie danego artykułu
			}
		}
		return false;
	}
	
	public ArrayList<Page> dajListęOsób(ArrayList<Page> lista){ // funkcja która przyjmuje jako argument listę wszystkich artykułów (łącznie z tymi które nie opisują osób)
		ArrayList<Page> listaOsób = new ArrayList<Page>(); // funkcja zwraca listę tych artykułów które opisują osoby
		for(Page p : lista){
			if(sprawdźArtykuł(p) == true) listaOsób.add(p);
		}	
		return listaOsób;
	}
	
}

public class Main {
	private static String nazwaPliku;
	private static ArrayList<Page> lista; // lista wszystkich artykułów
	private static ArrayList<Page> listaOsób; // lista zawierająca tylko artykuły opisujące osoby
	
	
	// tą funkcję można dodać do klasy - funkcjonalności
	private static void usuwanieZbędnychOdnośników(ArrayList<Page> lista){  // funkcja która przyjmuje jako argument listę tych artykułów, które opisują osoby
		ArrayList<String> listaOdnośników; // funkcja zwraca listę artykułów opisujących osoby, które zawierają odnośniki tylko do artykułów z tej listy (usuwane są też odnośniki, które się powtarzają w danym artykule)
		
		// tworzymy listę nazw wszystkich artykułów z listy danej jako argument
		ArrayList<String> listaTytułów = new ArrayList<String>(); 
		for(Page p : lista){
			listaTytułów.add(p.getTitle());
		}
		
		for(Page p : lista){
			listaOdnośników = new ArrayList<String>();
			for(String s : p.getLinks()){
				if(listaTytułów.contains(s)) if(!listaOdnośników.contains(s)) listaOdnośników.add(s);
			}
			p.setLinks(listaOdnośników);
		}
	}
	
	public static void pierwszaFaza() throws IOException, SAXException, ParserConfigurationException {
		XMLImporter imp;
		String [] tablicaKategorii = {"<dowolny rok> births", "<dowolny rok> deaths", "<dowolny tekst> Physicists",
				"Philosophers of <dowolna dziedzina>", "<dowolny tekst> theorists", "Nobel laureates <dowolny tekst>",
				"<dowolny tekst> actors", "<dowolny tekst> alumni"};
		
		System.out.println("Program - Historyczny Facebook (Proszę czekać - program importuje dane z pliku xml)");
		imp = new XMLImporter(nazwaPliku);
		imp.initiate(); // sparsowanie całego pliku xml może zająć kilka minut
		lista = imp.getList();
		//System.out.println("Article names:\n" + imp.showTitles());
		
		Klasyfikator k = new Klasyfikator(tablicaKategorii);
		listaOsób = k.dajListęOsób(lista);
		usuwanieZbędnychOdnośników(listaOsób);
		System.out.println("Z pliku podanego jako argument zostało zaimportowane " + listaOsób.size() + " artykułów odpowiadających osobom");
		for(int i = 1; i <= listaOsób.size(); i++){
		System.out.println("" + listaOsób.get(i-1).getNumber() + listaOsób.get(i-1).getTitle());
		//System.out.println(x.getPersondata(i));
		//System.out.println(x.getCategories(i));
		System.out.println(listaOsób.get(i-1).getLinks());
		//System.out.println(x.getTemplates(i));
		}
	}
	
	public static void drugaFaza() throws IOException{
		BufferedReader wejscie = new BufferedReader(new InputStreamReader(System.in));
		String s, filtr;
		Graph<Page> graf = new Graph<Page>(listaOsób);
		//List<Page> lista = new LinkedList<Page>();
		Page strona1 = new Page(1);
		Page strona2 = new Page(2);
		Pattern pattern = Pattern.compile("<([^>]*)>");
		Matcher matcher;
		System.out.println("Podaj zapytanie w formacie \"<filtr> <osoba_1> <osoba_2>\". Aby zakończyć nacisnij \"q\""); // powtarzanie tej instrukcji w pętli. znacznik konca pliku - koniec pracy programu albo odpowiedni przycisk
		s = wejscie.readLine();
		if(s == null) System.exit(0);
		
		while (s.charAt(0) != 'q'){
		matcher = pattern.matcher(s);
		
		matcher.find();		
		filtr = matcher.group(1);
		matcher.find();		
		for(Page p : listaOsób){
			if(matcher.group(1).equalsIgnoreCase(p.getTitle())) strona1 = p;
		}
		matcher.find();		
		for(Page p : listaOsób){
			if(matcher.group(1).equalsIgnoreCase(p.getTitle())) strona2 = p;
		}
		System.out.println(filtr + " " + strona1 + " " + strona2);
		lista = graf.BFS(strona1, strona2);
		System.out.println("***");
		if(lista == null) System.out.println("Zapytanie zawiera nazwę artykułu który nie występuje w grafie");
		else{
			System.out.println("Path length: " + (lista.size()-1));
			for(Page p : lista)	System.out.println(p);
		}
		System.out.println("***");

		// nie warto dodawać poniższej linijki bo dialog z programem powinien być zgodny z tym ze specyfikacji
		//System.out.println("Podaj zapytanie w formacie \"<filtr> <osoba_1> <osoba_2>\". Aby zakończyć nacisnij \"q\""); // powtarzanie tej instrukcji w pętli. znacznik konca pliku - koniec pracy programu albo odpowiedni przycisk
		s = wejscie.readLine();
		if(s == null) System.exit(0);
		}
				
	}
	
	public static void main(String[] args) throws IOException, SAXException, ParserConfigurationException {
		nazwaPliku = args[0]; // zabezpieczyć się przed wyjątkami
		
		pierwszaFaza();
		drugaFaza();
		/*graf.dodaj("Arystoteles");
		graf.dodaj("Albert Einstein");
		graf.dodaj("Immanuel Kant");
		graf.dodaj("Platon");
		graf.dodaj("Euklides");
		graf.dodaj("Sokrates");
		graf.połącz("Arystoteles", "Platon");
		graf.połącz("Sokrates", "Platon");
		graf.połącz("Arystoteles", "Euklides");
		graf.połącz("Euklides", "Albert Einstein");
		graf.połącz("Arystoteles", "Immanuel Kant");
		System.out.println(graf.BFS(graf, "Arystoteles", "Sokrates"));*/
		
		
		
		
		
		//System.out.println(k.containsCategory(tekst, "Philosophers of <tekst2>"));

		//System.out.println(x.findPersondata());

	}
}
