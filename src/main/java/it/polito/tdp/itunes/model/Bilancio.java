package it.polito.tdp.itunes.model;

public class Bilancio implements Comparable <Bilancio>{
	
	private Album a;
	private Integer bilancio;
	
	
	public Bilancio(Album a, Integer bilancio) {
		super();
		this.a = a;
		this.bilancio = bilancio;
	}
	public Album getA() {
		return a;
	}
	public Integer getBilancio() {
		return bilancio;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((a == null) ? 0 : a.hashCode());
		result = prime * result + ((bilancio == null) ? 0 : bilancio.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Bilancio other = (Bilancio) obj;
		if (a == null) {
			if (other.a != null)
				return false;
		} else if (!a.equals(other.a))
			return false;
		if (bilancio == null) {
			if (other.bilancio != null)
				return false;
		} else if (!bilancio.equals(other.bilancio))
			return false;
		return true;
	}
	@Override
	public int compareTo(Bilancio b) {
		return b.getBilancio().compareTo(bilancio);
	}
	@Override
	public String toString() {
		return a + ", bilancio: " + bilancio;
	}
	
	
	

}
