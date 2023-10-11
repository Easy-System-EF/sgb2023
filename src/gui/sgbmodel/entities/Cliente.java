package gui.sgbmodel.entities;

import java.io.Serializable;
import java.util.Objects;

public class Cliente implements Serializable{

	private static final long serialVersionUID = 1L;

	private Integer codigoCli;
	private String nomeCli;
	private Integer dddCli;
	private Integer telefoneCli;
	private String convenioCli;
	
	public Cliente () {
		
	}

	public Cliente(Integer codigoCli ,String nomeCli, Integer dddCli, Integer telefoneCli, String convenioCli) {
		this.codigoCli = codigoCli;
		this.nomeCli = nomeCli;
		this.dddCli = dddCli;
		this.telefoneCli = telefoneCli;
		this.convenioCli = convenioCli;
	}

	public Integer getCodigoCli() {
		return codigoCli;
	}

	public void setCodigoCli(Integer codigoCli) {
		this.codigoCli = codigoCli;
	}

	public String getNomeCli() {
		return nomeCli;
	}

	public void setNomeCli(String nomeCli) {
		this.nomeCli = nomeCli;
	}

	public Integer getDddCli() {
		return dddCli;
	}

	public void setDddCli(Integer dddCli) {
		this.dddCli = dddCli;
	}

	public Integer getTelefoneCli() {
		return telefoneCli;
	}

	public void setTelefoneCli(Integer telefoneCli) {
		this.telefoneCli = telefoneCli;
	}

	public String getConvenioCli() {
		return convenioCli;
	}

	public void setConvenioCli(String convenioCli) {
		this.convenioCli = convenioCli;
	}

	@Override
	public int hashCode() {
		return Objects.hash(dddCli, telefoneCli);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Cliente other = (Cliente) obj;
		return Objects.equals(dddCli, other.dddCli) && Objects.equals(telefoneCli, other.telefoneCli);
	}

	@Override
	public String toString() {
		return "Cliente [codigoCli=" + codigoCli + ", nomeCli=" + nomeCli + ", dddCli=" + dddCli + ", telefoneCli="
				+ telefoneCli + ", convenioCli=" + convenioCli + "]";
	}
}
