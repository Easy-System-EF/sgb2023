package gui.sgbmodel.entities;

import java.io.Serializable;

public class FechamentoAno implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer numeroFechamentoAno;
	private String cartelaFechamentoAno;
	private String dataFechamentoAno;
	private String situacaoFechamentoAno;
	private String valorCartelaFechamentoAno;
	private String valorProdutoFechamentoAno;
	private String valorComissaoFechamentoAno;
	private String valorResultadoFechamentoAno;
	private String valorAcumuladoFechamentoAno;

	public FechamentoAno() {
	}

	public FechamentoAno(Integer numeroFechamentoAno, String cartelaFechamentoAno, 
			String dataFechamentoAno, String situacaoFechamentoAno, 
			String valorCartelaFechamentoAno, String valorProdutoFechamentoAno, 
			String valorComissaoFechamentoAno, 	String valorResultadoFechamentoAno, 
			String valorAcumuladoFechamentoAno) {
		this.numeroFechamentoAno = numeroFechamentoAno;
		this.cartelaFechamentoAno = cartelaFechamentoAno;
		this.dataFechamentoAno = dataFechamentoAno;
		this.situacaoFechamentoAno = situacaoFechamentoAno;
		this.valorCartelaFechamentoAno = valorCartelaFechamentoAno;
		this.valorProdutoFechamentoAno = valorProdutoFechamentoAno;
		this.valorComissaoFechamentoAno = valorComissaoFechamentoAno;
		this.valorResultadoFechamentoAno = valorResultadoFechamentoAno;
		this.valorAcumuladoFechamentoAno = valorAcumuladoFechamentoAno;
	}

	public Integer getNumeroFechamentoAno() {
		return numeroFechamentoAno;
	}

	public void setNumeroFechamentoAno(Integer numeroFechamentoAno) {
		this.numeroFechamentoAno = numeroFechamentoAno;
	}

	public String getCartelaFechamentoAno() {
		return cartelaFechamentoAno;
	}

	public void setCartelaFechamentoAno(String cartelaFechamentoAno) {
		this.cartelaFechamentoAno = cartelaFechamentoAno;
	}

	public String getDataFechamentoAno() {
		return dataFechamentoAno;
	}

	public void setDataFechamentoAno(String dataFechamentoAno) {
		this.dataFechamentoAno = dataFechamentoAno;
	}

	public String getSituacaoFechamentoAno() {
		return situacaoFechamentoAno;
	}

	public void setSituacaoFechamentoAno(String situacaoFechamentoAno) {
		this.situacaoFechamentoAno = situacaoFechamentoAno;
	}

	public String getValorCartelaFechamentoAno() {
		return valorCartelaFechamentoAno;
	}

	public void setValorCartelaFechamentoAno(String valorCartelaFechamentoAno) {
		this.valorCartelaFechamentoAno = valorCartelaFechamentoAno;
	}

	public String getValorProdutoFechamentoAno() {
		return valorProdutoFechamentoAno;
	}

	public void setValorProdutoFechamentoAno(String valorProdutoFechamentoAno) {
		this.valorProdutoFechamentoAno = valorProdutoFechamentoAno;
	}

	public String getValorComissaoFechamentoAno() {
		return valorComissaoFechamentoAno;
	}

	public void setValorComissaoFechamentoAno(String valorComissaoFechamentoAno) {
		this.valorComissaoFechamentoAno = valorComissaoFechamentoAno;
	}

	public String getValorResultadoFechamentoAno() {
		return valorResultadoFechamentoAno;
	}

	public void setValorResultadoFechamentoAno(String valorResultadoFechamentoAno) {
		this.valorResultadoFechamentoAno = valorResultadoFechamentoAno;
	}

	public String getValorAcumuladoFechamentoAno() {
		return valorAcumuladoFechamentoAno;
	}

	public void setValorAcumuladoFechamentoAno(String valorAcumuladoFechamentoAno) {
		this.valorAcumuladoFechamentoAno = valorAcumuladoFechamentoAno;
	}

	@Override
	public String toString() {
		return "FechamentoAno [numeroFechamentoAno=" + numeroFechamentoAno + ", cartelaFechamentoAno="
				+ cartelaFechamentoAno + ", dataFechamentoAno=" + dataFechamentoAno + ", situacaoFechamentoAno="
				+ situacaoFechamentoAno + ", valorCartelaFechamentoAno=" + valorCartelaFechamentoAno
				+ ", valorProdutoFechamentoAno=" + valorProdutoFechamentoAno + ", valorComissaoFechamentoAno="
				+ valorComissaoFechamentoAno + ", valorResultadoFechamentoAno=" + valorResultadoFechamentoAno
				+ ", valorAcumuladoFechamentoAno=" + valorAcumuladoFechamentoAno + "]";
	}
}
