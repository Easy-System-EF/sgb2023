package gui.copia;

import java.io.Serializable;

public class Unidade implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer IdUnid;
	private String letraUnid;
	
	public Unidade() {
	}

	public Unidade(Integer idUnid, String letraUnid) {
		super();
		IdUnid = idUnid;
		this.letraUnid = letraUnid;
	}

	public Integer getIdUnid() {
		return IdUnid;
	}

	public void setIdUnid(Integer idUnid) {
		IdUnid = idUnid;
	}

	public String getLetraUnid() {
		return letraUnid;
	}

	public void setLetraUnid(String letraUnid) {
		this.letraUnid = letraUnid;
	}

	@Override
	public String toString() {
		return "Unidade [IdUnid=" + IdUnid + ", letraUnid=" + letraUnid + "]";
	}
}
