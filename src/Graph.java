// plik jest zakodowany w systemie UTF-8
// algorytm przeszukiwania grafu powinien uwzględniać obecność odnośników do stron nie zawartych w liście artykułów

import java.util.*;

public class Graph<T extends Page> { // tu można dać interfejs zamiast page
	private Map<T, ArrayList<T>> graph; // zbiór wszystkich wierzchołków grafów oraz ich list połączeń z pozostałymi wierzchołkami
	//private ArrayList<T> zbiórWierzchołków;
	//private ArrayList<Wierzchołek<T>> listaSąsiedztwa;
	
	public Graph(){
		this.graph = new HashMap<T, ArrayList<T>>();
	}
	
	public Graph(ArrayList<T> list){
		this.graph = new HashMap<T, ArrayList<T>>();
		ArrayList<T> linklist;
		for(T t : list){
			linklist = new ArrayList<T>();
			for(String s : t.getLinks()){ // pętla ta przetwarza listę odnośników danej strony (które są typu String) na listę stron z którymi dana strona jest połączona
				for(T t2 : list){
					if(s.equalsIgnoreCase(t2.getTitle())) linklist.add(t2);
				}
			}
			this.graph.put(t, linklist);
		}
		//Set<T> nodeSet = this.graph.keySet();
		//for(T node : nodeSet){
		//	System.out.println(this.graph.get(node));
		//}
		
	}

	/*public Graph(TreeMap<T, ArrayList<T>> graph){
		this.graph = graph;
	}*/
	
	/*public void add(T t){
		if (graph.containsKey(t)) throw new UnsupportedOperationException(); // tutaj można zrobić jakąś obsługę wyjątków
		else this.graph.put(t, new ArrayList<T>());
	}*/	
	
	/*public void linkNodes(T t1, T t2){
		ArrayList<T> lista = this.graph.get(t1);
		lista.add(t2);
		this.graph.remove(t1); 
		this.graph.put(t1, lista);
		lista = this.graph.get(t2);
		lista.add(t1);
		this.graph.put(t2, lista);
	}*/
	
	// algorytm Breadth-first search w zmofyfikowanej postaci, t1 - wierzchołek z którego rozpoczynamy poszukiwania, t2 - wierzchołek poszukiwany, w liście list będą zapisane wierzchołki (łącznie z t1 i t2) znajdujące się pomiędzy wierzchołkami t1 i t2
	public ArrayList<T> BFS(T t1, T t2){ // funkcja ta zwraca długość najkrótszej ścieżki między wierzchołkami t1 i t2 lub wartość null gdy graf nie zawiera wierzchołka t1 lub t2
		System.out.println("buhahaha");
		if(!this.graph.containsKey(t1)) return null; // jeśli w grafie nie ma wierzchołka t1 to zwracamy null
		
		HashMap<T, ArrayList<T>> hm1 = new HashMap<T, ArrayList<T>>(); // tutaj będą przechowywane wszystkie wierzchołki grafu i ich ścieżki od wierzchołka t1 do danego wierzchołka
		HashMap<T, String> hm2 = new HashMap<T, String>(); // tutaj będą przechowywane wszystkie wierzchołki grafu i ich kolory
		Queue<T> queue = new LinkedList<T>();
		T u;
		Set<T> nodeSet = this.graph.keySet();
		ArrayList<T> list = new ArrayList<T>();
		ArrayList<T> list1 = new ArrayList<T>(); // tworzymy listę pomocniczą, ponieważ w mapie hm1 będzie zawarta lista - list
		
		for(T node : nodeSet){
			hm1.put(node, list);
			hm2.put(node, "white");
		}
		hm1.remove(t1);
		list1.add(t1);
		hm1.put(t1, list1);
		
		hm2.remove(t1);
		hm2.put(t1, "grey");
		queue.add(t1);
		while (!queue.isEmpty()){
			u = queue.remove();
			for(T v : this.graph.get(u)){ // pętla wykonująca się dla kazdego wierzchołka v z listy sąsiedztwa wierzchołka u
				if(hm2.get(v).equalsIgnoreCase("white")){
					
					// poniżej wstawiamy ścieżkę do wierzchołka v z wierzchołka t1
					list = hm1.get(u);
					hm1.remove(v);
					list1 = new ArrayList<T>();
					list1.addAll(list);
					list1.add(v);
					hm1.put(v, list1);
					
					if(t2.equals(v)) return hm1.get(v); // jeśli wierzchołek t2 został znaleziony to zwracamy jego ścieżkę do wierzchołka t1
					hm2.remove(v);
					hm2.put(v, "grey");
					queue.add(v);
				}
			}
			hm2.remove(u);
			hm2.put(u, "black");
		}
		return null; // jeśli wierzchołek t2 nie został znaleziony to zwracamy null
	}	
}
