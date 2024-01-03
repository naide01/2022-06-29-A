package it.polito.tdp.itunes.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.itunes.db.ItunesDAO;

public class Model {
	private ItunesDAO dao;
	private List<Album> allAlbums;
	private SimpleDirectedWeightedGraph<Album, DefaultWeightedEdge> grafo;
	private List<Album> bestPath;
	private int bestScore;
	
	public Model() {
		this.allAlbums= new ArrayList<>();
		this.grafo = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		this.dao = new ItunesDAO();
	}
	
	public void creaGrafo(int n){
		clearGraph();
		loadNodes(n);
		
		Graphs.addAllVertices(this.grafo, this.allAlbums);
		
		for (Album a1 : this.allAlbums) {
			for (Album a2 : this.allAlbums) {
				int peso = a1.getNumSongs() - a2.getNumSongs();
				
				if (peso > 0) {	//SOLO GLI ARCHI CHE ENTRANO IN A1
					Graphs.addEdgeWithVertices(this.grafo, a2, a1, peso);
				}
			}
		}
//		System.out.println(this.grafo.vertexSet().size());
//		System.out.println(this.grafo.edgeSet().size());
		
	}

	private void clearGraph() {
		this.grafo = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		this.allAlbums= new ArrayList<>();
		
		
	}

	private void loadNodes(int n) {
		if (this.allAlbums.isEmpty()) {
			this.allAlbums = this.dao.getFilteredAlbums(n);
		}
	}

	public int getNumVertici() {
		return this.grafo.vertexSet().size();
	}
	public int getNumArchi() {
		return this.grafo.edgeSet().size();
	}

	private int getBilancio(Album a) {
		int bilancio = 0;
		
		List<DefaultWeightedEdge> edgeIN = new ArrayList <>(this.grafo.incomingEdgesOf(a));
		List<DefaultWeightedEdge> edgeOUT = new ArrayList <>(this.grafo.outgoingEdgesOf(a));
		
		for (DefaultWeightedEdge edge : edgeIN) {
			bilancio += this.grafo.getEdgeWeight(edge);
			for (DefaultWeightedEdge edge2 : edgeOUT) {
				bilancio -= this.grafo.getEdgeWeight(edge2);
			}
		}
		return bilancio;
	}
	
  /**
   * TUTTI GLI ALBUM ORDINATI PER TITOLO
   * @return
   */
	public List<Album> getVertici(){
		List<Album> allVertices = new ArrayList<>(this.grafo.vertexSet());
		Collections.sort(allVertices);
		return allVertices;

	}
	
	public List<Bilancio> getAdiacenti (Album a){
		List<Album> successori = Graphs.successorListOf(this.grafo, a);
		List<Bilancio> bilancioSuccessori = new ArrayList<>();
		for (Album al : successori) {
			bilancioSuccessori.add(new Bilancio(al, getBilancio(al)));
			
		}
		Collections.sort(bilancioSuccessori);
		return bilancioSuccessori;
		
	}
	
	
	
	/**
	 * ES.2 RICORSIONE
	 * 
	 * METODO CHE CHIAMERA' IL CONTROLLORE PER TROVARE IL PERCORSO
	 * @return
	 */
	public List<Album> getPath(Album source, Album target, int threshold){
		List<Album> parziale = new ArrayList<>();
		this.bestPath = new ArrayList<>();
		this.bestScore = 0;
		parziale.add(source);
		
		ricorsione(parziale,target,threshold);
		
		return this.bestPath;
		
	}

	private void ricorsione(List<Album> parziale, Album target, int threshold) {
		Album current = parziale.get(parziale.size()-1);
		
		//condizione di uscita
		if (current.equals(target)) {
			//controllo se la soluzione è migliore della best
			if (getScore(parziale)>this.bestScore) {
				this.bestScore = getScore(parziale);
				this.bestPath = new ArrayList<>(parziale); //NO = PARZIALE MA FAI NEW
			}
			return;
			
		}
		//continuo ad aggiungere gli elementi in parziale
		List<Album> successori = Graphs.successorListOf(this.grafo, current);
		for (Album a: successori) {
			/**
			 * PRENDO L'ARCO DA CURRENT AL SUCCESSORE E CALCOLO IL PESO, 
			 * SE E' >= DELLA SOGLIA (THRESHOLD) ALLORA LO AGGIUNGO AL PERCORSO
			 */
			if(this.grafo.getEdgeWeight(this.grafo.getEdge(current, a)) >= threshold && !parziale.contains(a));
				parziale.add(a);
				ricorsione(parziale, target, threshold);
				parziale.remove(a); //BACKTRAKING
		}
		
	}
	/**
	 * TOCCA IL MAGGIOR NUMERO DI VERTICI CHE HANNO
	 * BILANCIO MAGGIORE DI QUELLO DI PARTENZA
	 * @param parziale
	 * @return
	 */
	private int getScore(List<Album> parziale) {
		Album source = parziale.get(0);
		int score = 0;
		for (Album a : parziale.subList(1, parziale.size()-1)) {
			if (getBilancio(a)>getBilancio(source)) {
				score +=1;
			}
		}
		return score;
	}
	
	
}
