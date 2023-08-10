package gui.copia;

import java.util.Objects;

public class Copia {

	private Integer IdBackUp;
	private String dataIBackUp;
	private String userBackUp;
	private String dataFBackUp;
	
	public Copia() {		
	}

	public Copia(Integer IdBackUp, String dataIBackUp, String userBackUp, String dataFBackUp) {
		this.IdBackUp = IdBackUp;
		this.dataIBackUp = dataIBackUp;
		this.userBackUp = userBackUp;
		this.dataFBackUp = dataFBackUp;
	}

	public Integer getIdBackUp() {
		return IdBackUp;
	}

	public void setIdBackUp(Integer idBackUp) {
		IdBackUp = idBackUp;
	}

	public String getDataIBackUp() {
		return dataIBackUp;
	}

	public void setDataIBackUp(String dataIBackUp) {
		this.dataIBackUp = dataIBackUp;
	}

	public String getUserBackUp() {
		return userBackUp;
	}

	public void setUserBackUp(String userBackUp) {
		this.userBackUp = userBackUp;
	}

	public String getDataFBackUp() {
		return dataFBackUp;
	}

	public void setDataFBackUp(String dataFBackUp) {
		this.dataFBackUp = dataFBackUp;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(IdBackUp);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Copia other = (Copia) obj;
		return Objects.equals(IdBackUp, other.IdBackUp);
	}

	@Override
	public String toString() {
		return "BackUp [IdBackUp=" + IdBackUp + ", dataIBackUp=" + dataIBackUp + ", userBackUp=" + userBackUp
				+ ", dataFBackUp=" + dataFBackUp + "]";
	}
}
