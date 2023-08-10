package gui.copia;

public class Unidade {

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
