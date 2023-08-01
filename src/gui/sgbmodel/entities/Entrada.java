package gui.sgbmodel.entities;

import java.io.Serializable;
import java.util.Date;

import gui.sgcpmodel.entites.Fornecedor;

public class Entrada implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer numeroEnt;
	private Integer nnfEnt;
	private Date dataEnt;
	private String nomeFornEnt;
	private String nomeProdEnt;
	private Double quantidadeProdEnt;
	private Double valorProdEnt;
	
	Fornecedor fornecedor;
	Produto produto;
	
	public Entrada() {
	}

	public Entrada(Integer numeroEnt, Integer nnfEnt, Date dataEnt, String nomeFornEnt, String nomeProdEnt, 
			Double quantidadeProdEnt, Double valorProdEnt, Fornecedor fornecedor, Produto produto) {
		this.numeroEnt = numeroEnt;
		this.nnfEnt = nnfEnt;
		this.dataEnt = dataEnt;
		this.nomeFornEnt = nomeFornEnt;
		this.nomeProdEnt = nomeProdEnt;
		this.quantidadeProdEnt = quantidadeProdEnt;
		this.valorProdEnt = valorProdEnt;
		this.fornecedor = fornecedor;
		this.produto = produto;
	}

	public Integer getNumeroEnt() {
		return numeroEnt;
	}

	public void setNumeroEnt(Integer numeroEnt) {
		this.numeroEnt = numeroEnt;
	}

	public Integer getNnfEnt() {
		return nnfEnt;
	}

	public void setNnfEnt(Integer nnfEnt) {
		this.nnfEnt = nnfEnt;
	}

	public Date getDataEnt() {
		return dataEnt;
	}

	public void setDataEnt(Date dataEnt) {
		this.dataEnt = dataEnt;
	}

	public String getNomeFornEnt() {
		return nomeFornEnt;
	}

	public void setNomeFornEnt(String nomeFornEnt) {
		this.nomeFornEnt = nomeFornEnt;
	}

	public String getNomeProdEnt() {
		return nomeProdEnt;
	}

	public void setNomeProdEnt(String nomeProdEnt) {
		this.nomeProdEnt = nomeProdEnt;
	}

	public Double getQuantidadeProdEnt() {
		return quantidadeProdEnt;
	}

	public void setQuantidadeProdEnt(Double quantidadeProdEnt) {
		this.quantidadeProdEnt = quantidadeProdEnt;
	}

	public Double getValorProdEnt() {
		return valorProdEnt;
	}

	public void setValorProdEnt(Double valorProdEnt) {
		this.valorProdEnt = valorProdEnt;
	}

	public Fornecedor getForn() {
		return fornecedor;
	}

	public void setForn(Fornecedor forn) {
		this.fornecedor = forn;
	}

	public Produto getProd() {
		return produto;
	}

	public void setProd(Produto prod) {
		this.produto = prod;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((numeroEnt == null) ? 0 : numeroEnt.hashCode());
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
		Entrada other = (Entrada) obj;
		if (numeroEnt == null) {
			if (other.numeroEnt != null)
				return false;
		} else if (!numeroEnt.equals(other.numeroEnt))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Entrada [numeroEnt=" + numeroEnt + ", nnfEnt=" + nnfEnt + ", dataEnt=" + dataEnt + ", nomeFornEnt="
				+ nomeFornEnt + ", quantidadeProdEnt=" + quantidadeProdEnt
				+ ", valorProdEnt=" + valorProdEnt + ", forn=" + fornecedor + "]";
	}
 }
