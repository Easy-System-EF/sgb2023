package gui.sgbmodel.entities;

import java.io.Serializable;
import java.util.Objects;

public class FolhaMes implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer numeroFolha;
	private String funcionarioFolha;
	private String cargoFolha;
	private String situacaoFolha;
	private String salarioFolha;
	private String comissaoFolha;
	private String valeFolha;
	private String receberFolha;
	private String totalFolha;
	private Integer mesFolha;
	private Integer anoFolha;
	
	public FolhaMes() {		
	}

	public FolhaMes(Integer numeroFolha, String funcionarioFolha, String cargoFolha, String situacaoFolha, String salarioFolha,
			String comissaoFolha, String valeFolha, String receberFolha, String totalFolha, Integer mesFolha, 
			Integer anoFolha) {
		this.numeroFolha = numeroFolha;
		this.funcionarioFolha = funcionarioFolha;
		this.cargoFolha = cargoFolha;
		this.situacaoFolha = situacaoFolha;
		this.salarioFolha = salarioFolha;
		this.comissaoFolha = comissaoFolha;
		this.valeFolha = valeFolha;
		this.receberFolha = receberFolha;
		this.totalFolha = totalFolha;
		this.mesFolha = mesFolha;
		this.anoFolha = anoFolha;
	}

	public Integer getNumeroFolha() {
		return numeroFolha;
	}

	public void setNumeroFolha(Integer numeroFolha) {
		this.numeroFolha = numeroFolha;
	}

	public String getFuncionarioFolha() {
		return funcionarioFolha;
	}

	public void setFuncionarioFolha(String funcionarioFolha) {
		this.funcionarioFolha = funcionarioFolha;
	}

	public String getCargoFolha() {
		return cargoFolha;
	}

	public void setCargoFolha(String cargoFolha) {
		this.cargoFolha = cargoFolha;
	}

	public String getSituacaoFolha() {
		return situacaoFolha;
	}

	public void setSituacaoFolha(String situacaoFolha) {
		this.situacaoFolha = situacaoFolha;
	}

	public String getSalarioFolha() {
		return salarioFolha;
	}

	public void setSalarioFolha(String salarioFolha) {
		this.salarioFolha = salarioFolha;
	}

	public String getComissaoFolha() {
		return comissaoFolha;
	}

	public void setComissaoFolha(String comissaoFolha) {
		this.comissaoFolha = comissaoFolha;
	}

	public String getValeFolha() {
		return valeFolha;
	}

	public void setValeFolha(String valeFolha) {
		this.valeFolha = valeFolha;
	}

	public String getReceberFolha() {
		return receberFolha;
	}

	public void setReceberFolha(String receberFolha) {
		this.receberFolha = receberFolha;
	}

	public String getTotalFolha() {
		return totalFolha;
	}

	public void setTotalFolha(String totalFolha) {
		this.totalFolha = totalFolha;
	}

	public Integer getMesFolha() {
		return mesFolha;
	}

	public void setMesFolha(Integer mesFolha) {
		this.mesFolha = mesFolha;
	}

	public Integer getAnoFolha() {
		return anoFolha;
	}

	public void setAnoFolha(Integer anoFolha) {
		this.anoFolha = anoFolha;
	}

	@Override
	public int hashCode() {
		return Objects.hash(numeroFolha);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FolhaMes other = (FolhaMes) obj;
		return Objects.equals(numeroFolha, other.numeroFolha);
	}

	@Override
	public String toString() {
		return "FolhaMes [numeroFolha=" + numeroFolha + ", funcionarioFolha=" + funcionarioFolha + ", cargoFolha="
				+ cargoFolha + ", situacaoFolha=" + situacaoFolha + ", salarioFolha=" + salarioFolha
				+ ", comissaoFolha=" + comissaoFolha + ", valeFolha=" + valeFolha + ", receberFolha=" + receberFolha
				+ ", totalFolha=" + totalFolha + ", mesFolha=" + mesFolha + ", anoFolha=" + anoFolha + "]";
	}

}
