// plik jest zakodowany w systemie UTF-8

//usunąć komentarze
//uwaga: linki mogą się powtarzać

import java.util.regex.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


public class XMLImporter extends DefaultHandler{
	private String fileName;
	private SAXParserFactory spfac;
	private SAXParser sp;
    private Page page;
    private String temp;
	private String text;
    private ArrayList<Page> pageList = new ArrayList<Page>();
	
	public XMLImporter(String fileName) {
		this.fileName = fileName;
		this.spfac = SAXParserFactory.newInstance();
	}
	
	public void initiate() throws IOException, SAXException, ParserConfigurationException {
		this.sp = this.spfac.newSAXParser();
		//this.text = "";
		sp.parse(this.fileName, this);
		//System.out.println(text);
	}
	
	protected ArrayList<Page> getList(){
		return this.pageList;
	}
	
	public String showTitles(){ // ta funkcja chyba nie będzie potrzebna
		String s = "";
		for(Page p : this.pageList){
			s = s + p.getNumber() + " " + p.getTitle() + "\n";
		}
		return s;
	}
	
	public String getTitle(int number){
		if(number > 0 && number <= this.pageList.size()) 
			return this.pageList.get(number-1).getTitle();
		else return null;
	}
	
	public String getPersondata(int number) {
		if(number > 0 && number <= this.pageList.size()){
			if(this.pageList.get(number-1).getPersondata().equals("")) return null; // jeśli w danym artykule nie ma szablonu Persondata to zwracana jest wartość mull
			else return "|Persondata" + this.pageList.get(number-1).getPersondata();
			
		} 
		else return null;	
	}
	
	public String getLinks(int number){
		String text = "";
		if(number > 0 && number <= this.pageList.size()){
			for(String s : this.pageList.get(number-1).getLinks()){
				text = text + s + "\n";
			}
			return text;
		} 
		else return null;
	}
	
	public String getTemplates(int number){
		String text = "";
		if(number > 0 && number <= this.pageList.size()){
			for(String s : this.pageList.get(number-1).getTemplates()){
				text = text + s + "\n";
			}
			return text;
		} 
		else return null;
	}
	
	public String getCategories(int number){
		if(number > 0 && number <= this.pageList.size()) 
			return this.pageList.get(number-1).getCategories();
		else return null;
	}
	
	
 /*
  * When the parser encounters plain text (not XML elements),
  * it calls(this method, which accumulates them in a string buffer
  */	
	public void characters(char[] buffer, int start, int length) {
        temp = new String(buffer, start, length);
        this.text = this.text + temp;
    }


 /*
  * Every time the parser encounters the beginning of a new element,
  * it calls this method, which resets the string buffer
  */ 
	public void startElement(String uri, String localName,
               String qName, Attributes attributes) throws SAXException {
        this.temp = "";
        if (qName.equalsIgnoreCase("page")) {
               this.page = new Page(this.pageList.size() + 1);
               this.text = "";
               //page.setType(attributes.getValue("type"));

        }
	}

 /*
  * When the parser encounters the end of an element, it calls this method
  */
	public void endElement(String uri, String localName, String qName)
               throws SAXException {
	 	ArrayList<String> list = new ArrayList<String>();
	 	String links = "";
	 	
        if (qName.equalsIgnoreCase("page")) {
               // add it to the list
            this.pageList.add(page);

        } else if (qName.equalsIgnoreCase("title")) {
            this.page.setTitle(temp);
        } else if (qName.equalsIgnoreCase("text")) {
			Pattern pattern = Pattern.compile("\\{\\{Persondata([^\\}]*)\\}\\}");
			Matcher matcher = pattern.matcher(text);
			while (matcher.find()) {
				this.page.setPersondata(this.page.getPersondata() + matcher.group(1));
			}
			
			pattern = Pattern.compile("\\[\\[Category:([^\\]]*)\\]\\]");
			matcher = pattern.matcher(text);
			while (matcher.find()) {
				this.page.setCategories(this.page.getCategories() + "[[Category:" + matcher.group(1) + "]]\n");
			}
			
			// wyszukiwanie linków podzielone jest na dwie części. W pierwszej wyodrębniamy wszystkie odnośniki do innych artykułów poza odnośnikami do plików jpg i kategorii
			// a w drugiej wyodrębniamy nazwę artykułu z odnośnika np. Albert Einstein|Einstein  przekształcone jest na Albert Einstein
			// część I
			pattern = Pattern.compile("\\[\\[([^\\]&&[^:]]*)\\]\\]"); // wyrażenie &&[^:] pozwala pominąć te napisy, które zawierają znak ':'
			// Dzięki temu w liście linków nie znajdą się napisy typu: File:Arabic aristotle.jpg lub Category:Ancient Greek philosophers
			matcher = pattern.matcher(text);
			while (matcher.find()) {
				links = links + "[" + matcher.group(1) + "|"; // dla wygody wstawiamy znaczniki z lewej i prawej strony
			}
			//część II
			pattern = Pattern.compile("\\[([^\\|]*)\\|");
			matcher = pattern.matcher(links);
			while (matcher.find()) {
				list.add(matcher.group(1));
			}
			this.page.setLinks(list); // tutaj jest tworzona lista odnośników dla danej strony
			
			list = new ArrayList<String>();
			pattern = Pattern.compile("(\\{\\{[^\\}&&[^:]]*\\}\\})");
			matcher = pattern.matcher(text);
			while (matcher.find()) {
				list.add(matcher.group(1));
			}
			this.page.setTemplates(list);
               //acct.setId(Integer.parseInt(temp));
        }/* else if (qName.equalsIgnoreCase("Amt")) {
               acct.setAmt(Integer.parseInt(temp));
        }*/
	}
}
