// plik jest zakodowany w systemie UTF-8

public class Wierzchołek<T> {
	private T wartość;
	private String kolor; // zmienna potrzebna w przeszukiwaniu grafu	
	
	public Wierzchołek(){	
	}
	
	public Wierzchołek(T t, String kolor){
		this.wartość = t;
		this.kolor = kolor;
	}

	public void setWartość(T t){
		this.wartość = t;
	}
	
	//public void setLista(ArrayList<Wierzchołek<T>> lista){
	//	this.listaSąsiedztwa = lista;
	//}
	
	public void setKolor(String kolor){
		this.kolor = kolor;
	}

	public T getWartość(){
		return this.wartość;
	}
	
	//public ArrayList<Wierzchołek<T>> getLista(){
	//	return this.listaSąsiedztwa;
	//}
	
	public String getKolor(){
		return this.kolor;
	}
}
