package gui.sgbmodel.entities;

import java.io.Serializable;
import java.util.Date;

/*
 * or�amento virtual para mater pre�o n�mero de materiais
 */

public class CartelaPagante implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer numeroCartelaPag;
	private Integer paganteCartelaPag;
	private Date dataCartelaPag;
	private String localCartelaPag;
	private Double valorCartelaPag;
	private String formaCartelaPag;
	private String situacaoCartelaPag;
	private Integer cartelaIdOrigemPag;
	private Integer mesCartelaPag;
	private Integer anoCartelaPag;
	private int mesPagamentoPag;
	private int anoPagamentoPag;

//value "1" dinheiro, "2" pix, "3" debito, "4" credito;
	
	public CartelaPagante() {
	}

	public CartelaPagante(Integer numeroCartelaPag, Integer paganteCartelaPag, Date dataCartelaPag, 
			String localCartelaPag, Double valorCartelaPag, String formaCartelaPag, 
			String situacaoCartelaPag, Integer cartelaIdOrigemPag, Integer mesCartelaPag,
			Integer anoCartelaPag, int mesPagamentoPag, int anoPagamentoPag) {
		this.numeroCartelaPag = numeroCartelaPag;
		this.paganteCartelaPag = paganteCartelaPag;
		this.dataCartelaPag = dataCartelaPag;
		this.localCartelaPag = localCartelaPag;
		this.valorCartelaPag = valorCartelaPag;
		this.formaCartelaPag = formaCartelaPag;
		this.situacaoCartelaPag = situacaoCartelaPag;
		this.cartelaIdOrigemPag = cartelaIdOrigemPag;
		this.mesCartelaPag = mesCartelaPag;
		this.anoCartelaPag = anoCartelaPag;
		this.mesPagamentoPag = mesPagamentoPag;
		this.anoPagamentoPag = anoPagamentoPag;
	}

	public Integer getNumeroCartelaPag() {
		return numeroCartelaPag;
	}

	public void setNumeroCartelaPag(Integer numeroCartelaPag) {
		this.numeroCartelaPag = numeroCartelaPag;
	}

	public Integer getPaganteCartelaPag() {
		return paganteCartelaPag;
	}

	public void setPaganteCartelaPag(Integer paganteCartelaPag) {
		this.paganteCartelaPag = paganteCartelaPag;
	}

	public Date getDataCartelaPag() {
		return dataCartelaPag;
	}

	public void setDataCartelaPag(Date dataCartelaPag) {
		this.dataCartelaPag = dataCartelaPag;
	}

	public String getLocalCartelaPag() {
		return localCartelaPag;
	}

	public void setLocalCartelaPag(String localCartelaPag) {
		this.localCartelaPag = localCartelaPag;
	}

	public Double getValorCartelaPag() {
		return valorCartelaPag;
	}

	public void setValorCartelaPag(Double valorCartelaPag) {
		this.valorCartelaPag = valorCartelaPag;
	}

	public String getFormaCartelaPag() {
		return formaCartelaPag;
	}

	public void setFormaCartelaPag(String formaCartelaPag) {
		this.formaCartelaPag = formaCartelaPag;
	}

	public String getSituacaoCartelaPag() {
		return situacaoCartelaPag;
	}

	public void setSituacaoCartelaPag(String situacaoCartelaPag) {
		this.situacaoCartelaPag = situacaoCartelaPag;
	}

	public Integer getCartelaIdOrigemPag() {
		return cartelaIdOrigemPag;
	}

	public void setCartelaIdOrigemPag(Integer cartelaIdOrigemPag) {
		this.cartelaIdOrigemPag = cartelaIdOrigemPag;
	}

	public Integer getMesCartelaPag() {
		return mesCartelaPag;
	}

	public void setMesCartelaPag(Integer mesCartelaPag) {
		this.mesCartelaPag = mesCartelaPag;
	}

	public Integer getAnoCartelaPag() {
		return anoCartelaPag;
	}

	public void setAnoCartelaPag(Integer anoCartelaPag) {
		this.anoCartelaPag = anoCartelaPag;
	}

	public int getMesPagamentoPag() {
		return mesPagamentoPag;
	}

	public void setMesPagamentoPag(int mesPagamentoPag) {
		this.mesPagamentoPag = mesPagamentoPag;
	}

	public int getAnoPagamentoPag() {
		return anoPagamentoPag;
	}

	public void setAnoPagamentoPag(int anoPagamentoPag) {
		this.anoPagamentoPag = anoPagamentoPag;
	}

	@Override
	public String toString() {
		return "CartelaPagante [numeroCartelaPag=" + numeroCartelaPag + ", paganteCartelaPag=" + paganteCartelaPag
				+ ", dataCartelaPag=" + dataCartelaPag + ", localCartelaPag=" + localCartelaPag + ", valorCartelaPag="
				+ valorCartelaPag + ", formaCartelaPag=" + formaCartelaPag + ", situacaoCartelaPag="
				+ situacaoCartelaPag + ", cartelaIdOrigemPag=" + cartelaIdOrigemPag + ", mesCartelaPag=" + mesCartelaPag
				+ ", anoCartelaPag=" + anoCartelaPag + ", mesPagamentoPag=" + mesPagamentoPag + ", anoPagamentoPag="
				+ anoPagamentoPag + "]";
	}
}
