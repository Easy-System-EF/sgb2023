package gui.sgcpmodel.entites;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import gui.sgcpmodel.entites.consulta.ParPeriodo;

 
public class Compromisso implements Serializable {

 	private static final long serialVersionUID = 1L;

 	private Integer idCom;
 	private Integer codigoFornecedorCom;
 	private String nomeFornecedorCom;
   	private Integer nnfCom;
  	private Date dataCom;
  	private Date dataVencimentoCom;
 	private Double valorCom;
 	private Integer parcelaCom;
 	private Integer prazoCom;
 	
  	public Fornecedor fornecedor;
 	public TipoConsumo tipoFornecedor;
 	public ParPeriodo periodo;
 	
   	public Compromisso() {
	}

	public Compromisso(Integer idCom, Integer codigoFornecedorCom, String nomeFornecedorCom, Integer nnfCom, Date dataCom, 
			Date dataVencimentoCom, Double valorCom, Integer parcelaCom, Integer prazoCom,
			Fornecedor fornecedor, TipoConsumo tipoFornecedor, ParPeriodo periodo) {
		this.idCom = idCom;
		this.codigoFornecedorCom = codigoFornecedorCom; 
		this.nomeFornecedorCom = nomeFornecedorCom;
   		this.nnfCom = nnfCom;
		this.dataCom = dataCom;
		this.dataVencimentoCom = dataVencimentoCom;
		this.valorCom = valorCom;
		this.parcelaCom = parcelaCom;
		this.prazoCom = prazoCom;
		this.fornecedor = fornecedor;
		this.tipoFornecedor = tipoFornecedor;  
		this.periodo = periodo;
  	}
 
	public Integer getIdCom() {
		return idCom;
	}

	public void setIdCom(Integer idCom) {
		this.idCom = idCom;
	}

	public Integer getCodigoFornecedorCom() {
		return codigoFornecedorCom;
	}

	public void setCodigoFornecedorCom(Integer codigoFornecedorCom) {
		this.codigoFornecedorCom = codigoFornecedorCom;
	}

 	public String getNomeFornecedorCom() {
		return nomeFornecedorCom;
	}

	public void setNomeFornecedorCom(String nomeFornecedorCom) {
		this.nomeFornecedorCom = nomeFornecedorCom;
	}

	public Integer getNnfCom() {
		return nnfCom;
	}

	public void setNnfCom(Integer nnfCom) {
		this.nnfCom = nnfCom;
	}

	public Date getDataCom() {
		return dataCom;
	}

	public void setDataCom(Date dataCom) {
		this.dataCom = dataCom;
	}

	public Date getDataVencimentoCom() {
		return dataVencimentoCom;
	}

	public void setDataVencimentoCom(Date dataVencimentoCom) {
		this.dataVencimentoCom = dataVencimentoCom;
	}

	public Double getValorCom() {
		return valorCom;
	}

	public void setValorCom(Double valorCom) {
		this.valorCom = valorCom;
	}

	public Integer getParcelaCom() {
		return parcelaCom;
	}

	public void setParcelaCom(Integer parcelaCom) {
		this.parcelaCom = parcelaCom;
	}

	public Integer getPrazoCom() {
		return prazoCom;
	}

	public void setPrazoCom(Integer prazoCom) {
		this.prazoCom = prazoCom;
	}

 	public Fornecedor getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(Fornecedor fornecedor) {
		this.fornecedor = fornecedor;
	}

 	public TipoConsumo getTipoFornecedor() {
		return tipoFornecedor;
	}

	public void setTipoFornecedor(TipoConsumo tipoFornecedor) {
		this.tipoFornecedor = tipoFornecedor;
	}

 	public ParPeriodo getParPeriodo() {
		return periodo;
	}

	public void setParPeriodo(ParPeriodo periodo) {
		this.periodo = periodo;
	}

	@Override
	public int hashCode() {
		return Objects.hash(codigoFornecedorCom, nnfCom);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Compromisso other = (Compromisso) obj;
		return Objects.equals(codigoFornecedorCom, other.codigoFornecedorCom) && Objects.equals(nnfCom, other.nnfCom);
	}

	@Override
	public String toString() {
		return "Compromisso [idCom=" + idCom + ", codigoFornecedorCom=" + codigoFornecedorCom + ", nomeFornecedorCom="
				+ nomeFornecedorCom + ", nnfCom=" + nnfCom + ", dataCom=" + dataCom + ", dataVencimentoCom="
				+ dataVencimentoCom + ", valorCom=" + valorCom + ", parcelaCom=" + parcelaCom + ", prazoCom=" + prazoCom
				+ ", fornecedor=" + fornecedor + ", tipoFornecedor=" + tipoFornecedor + ", periodo=" + periodo + "]";
	}
 }	