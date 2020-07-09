// plik jest zakodowany w systemie UTF-8
// to mogłoby być częścią programu wikiImporter, użytkowanik definiuje jaka ma być strona

import java.util.*;
// Page może być parametryzowane Page<T> np. Page<Integer> strony zawierające liczbę jako tytuł
public class Page /*implements Comparable<Integer>*/{ // strona - pojedyncza encja odpowiadająca jednemu artykułowi
	// można dodać inne dane
	private ArrayList<String> linkList; // lista odnośników do innych stron w danej stronie
	private ArrayList<String> templateList; // lista szablonów w danej stronie
	private Integer number; // można usunąć tą zmienną
	private String title;
	private String Persondata;
	private String categories;
	
	public Page(int number){	
		this.linkList = new ArrayList<String>();
		this.templateList = new ArrayList<String>();
		this.number = number;
		this.title = "";
		this.Persondata = "";
		this.categories = "";
	}
	
	//public int compareTo(Integer i){
	//	return this.number.compareTo(i);
	//}
	
	@Override public String toString(){
		return this.title;
	}
	
	public void setTitle(String s){
		this.title = s;
	}
	
	public void setPersondata(String s){
		this.Persondata = s;
	}
	
	public void setCategories(String s){
		this.categories = s;
	}
	
	public void setTemplates(ArrayList<String> list){
		this.templateList = list;
	}
	
	public void setLinks(ArrayList<String> list){
		this.linkList = list;
	}
	
	public int getNumber(){
		return this.number;
	}
	
	public String getTitle(){
		return this.title;
	}
	
	public String getPersondata(){
		return this.Persondata;
	}
	
	public String getCategories(){
		return this.categories;
	}
	
	public ArrayList<String> getTemplates(){
		return this.templateList;
	}
	
	public ArrayList<String> getLinks(){
		return this.linkList;
	}
}
