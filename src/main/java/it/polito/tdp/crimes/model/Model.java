package it.polito.tdp.crimes.model;

import java.util.LinkedList;
import java.util.List;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.crimes.db.EventsDao;

public class Model {
	
	EventsDao dao;
	SimpleWeightedGraph<String, DefaultWeightedEdge> grafo;
	List<Adiacenza> adiacenze;
	List<String> soluzione;
	List<String> vertici;
	int pesoMin;
	
	
	public Model() {
		dao= new EventsDao();
		adiacenze= new LinkedList<>();
		this.vertici= new LinkedList<>();
	}
	public List<String> getCategorie(){
		return dao.getCategorie();
	}
	public List<Integer> getGiorni(){
		return dao.getAnni();
	}
	public void creaGrafo(String categoria, int giorno) {
		this.grafo= new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		vertici= dao.getVertici(categoria, giorno);
		Graphs.addAllVertices(grafo,vertici);
		
		adiacenze=dao.getAdiacenze(categoria, giorno);
		for(Adiacenza a: adiacenze) {
			if(grafo.vertexSet().contains(a.getTipo1())&& grafo.vertexSet().contains(a.tipo2)) {
				Graphs.addEdge(grafo, a.getTipo1(), a.getTipo2(), a.getPeso());
			}
		}
	}
	
	public int numVertici() {
		// TODO Auto-generated method stub
		return this.grafo.vertexSet().size();
	}

	public int numArchi() {
		// TODO Auto-generated method stub
		return this.grafo.edgeSet().size();
	}
	public int pesoMassimo() {
		int peso=0;
		for(DefaultWeightedEdge edge: grafo.edgeSet()) {
			int p= (int) grafo.getEdgeWeight(edge);
			if(p>peso)
				peso=p;
		}
		return peso;
	}
	public List<Adiacenza> getArchiPesoMassimo(){
		List<Adiacenza> result= new LinkedList<>();
		for(Adiacenza a: adiacenze) {
			if(a.getPeso()==this.pesoMassimo())
				result.add(a);
		}
		return result;
	}
	public List<Adiacenza> getAdiacenze(){
		return adiacenze;
	}
	public List<String> ricorsione(String partenza, String arrivo){
		this.soluzione= new LinkedList<>();
		List<String> parziale= new LinkedList<>();
		this.pesoMin=Integer.MAX_VALUE;
		parziale.add(partenza);
		cerca(parziale, 1, arrivo, 0);
		
		return soluzione;
		
	}
	private void cerca(List<String> parziale, int livello, String arrivo, int peso) {
		// TODO Auto-generated method stub
		if(parziale.size()>vertici.size())
			return;
		
		String ultimo= parziale.get(parziale.size()-1);
		if(parziale.size()==vertici.size()&& peso<this.pesoMin && ultimo.equals(arrivo)) {
			this.soluzione= new LinkedList<>(parziale);
			this.pesoMin=peso;
			return;
		}
		for(String s: Graphs.neighborListOf(grafo, ultimo)) {
			if(!parziale.contains(s)) {
				int p= (int) grafo.getEdgeWeight(grafo.getEdge(s, ultimo));
				parziale.add(s);
				peso+=p;
				cerca(parziale,livello+1,arrivo,peso);
				parziale.remove(s);
				peso-=p;
			}
		}
	}
	public boolean grafoCreato() {
		if(grafo==null)
			return false;
		return true;
	}
	public int getPesoMin() {
		return pesoMin;
	}
	
	public List<String> doRicorsione2(String partenza, String arrivo){
		this.soluzione= new LinkedList<>();
		List<String> parziale= new LinkedList<>();
		
		parziale.add(partenza);
		cerca1(parziale, 1, arrivo);
		
		return soluzione;
	}
	private void cerca1(List<String> parziale, int i, String arrivo) {
		// TODO Auto-generated method stub
		String ultimo= parziale.get(parziale.size()-1);
		if(parziale.size()>soluzione.size() && ultimo.equals(arrivo) ) {
			soluzione= new LinkedList<>(parziale);
		}
		for(String s: Graphs.neighborListOf(grafo, ultimo)) {
			if(!parziale.contains(s)) {
				parziale.add(s);
				cerca1(parziale,i+1,arrivo);
				parziale.remove(s);
			}
		}
	}
}
