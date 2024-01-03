package it.polito.tdp.itunes.model;

import java.util.ArrayList;
import java.util.List;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.itunes.db.ItunesDAO;

public class Model {
	private ItunesDAO dao;
	private List<Album> allAlbums;
	private SimpleDirectedWeightedGraph<Album, DefaultWeightedEdge> grafo;
	
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
		System.out.println(this.grafo.vertexSet().size());
		System.out.println(this.grafo.edgeSet().size());
		
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
	
}
