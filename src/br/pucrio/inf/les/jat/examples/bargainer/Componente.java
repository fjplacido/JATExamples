package br.pucrio.inf.les.jat.examples.bargainer;

import java.io.Serializable;

public class Componente implements Serializable {
	
	private String nome;
	private int idComponente;
	private float preco;

	public int getIdComponente() {
		return idComponente;
	}

	public void setIdComponente(int idComponente) {
		this.idComponente = idComponente;
	}
	
	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
		
	public float getPreco() {
		return preco;
	}

	public void setPreco(float preco) {
		this.preco = preco;
	}

	@Override
	public boolean equals(Object obj) {
		
		if ( obj == null ) return false;
		if ( obj == this ) return true;
		if ( !(obj instanceof Componente) ) return false;
				
		Componente c = (Componente)obj;
			
		return (c.idComponente == this.idComponente);
	}
}
