//import java.util.*;
import java.util.regex.*;

public class Classifier {
	//private String [] categoryArray;
	//private ArrayList<Page> list;
	//private boolean aboutPerson; to będzie w programie wikiimporter
	
	//public Klasyfikator(ArrayList<Page> list, String [] array){
		//this.list = list;
		//this.categoryArray = array;
	//}
	
	
	public boolean containsPersondata(Page p){
		return !p.getPersondata().equalsIgnoreCase(""); 
	}
	
	public boolean containsCategory(String text, String category){ // napis category jest postaci  tekst1 <tekst> tekst3 np. <dowolny rok> births, Philosophers of <dowolna dziedzina> 
		String [] array;
		String left;  // left - napis znajdujący się na lewo od znaku '<'
		String right; // right - napis znajdujący się na prawo od znaku '>'
		String regex;
		array = category.split("<");
		left = array[0]; 
		array = array[1].split(">");
		if(array.length == 2) right = array[1]; // instrukcja warunkowa zabezpiecza przed błędem w przypadku gdy category jest postaci Philosophers of <dowolna dziedzina>
		else right = "";
		regex = "\\[\\[Category:" + left + "(.*)" + right + "\\]\\]";
		
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(text);
		return matcher.find();
	}
	
	
	
	
}
