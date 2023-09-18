package gui.copia;

import java.io.Serializable;
import java.util.Objects;

public class Restaura implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer IdRestauraUp;
	private String fileUp;
	private String statusUp;
	private Integer countUp;

	public Restaura() {		
	}

	public Restaura(Integer idRestauraUp, String fileUp, String statusUp, Integer countUp) {
		IdRestauraUp = idRestauraUp;
		this.fileUp = fileUp;
		this.statusUp = statusUp;
		this.countUp = countUp;
	}

	public Integer getIdRestauraUp() {
		return IdRestauraUp;
	}

	public void setIdRestauraUp(Integer idRestauraUp) {
		IdRestauraUp = idRestauraUp;
	}

	public String getFileUp() {
		return fileUp;
	}

	public void setFileUp(String fileUp) {
		this.fileUp = fileUp;
	}

	public String getStatusUp() {
		return statusUp;
	}

	public void setStatusUp(String statusUp) {
		this.statusUp = statusUp;
	}

	public Integer getCountUp() {
		return countUp;
	}

	public void setCountUp(Integer countUp) {
		this.countUp = countUp;
	}

	@Override
	public int hashCode() {
		return Objects.hash(IdRestauraUp);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Restaura other = (Restaura) obj;
		return Objects.equals(IdRestauraUp, other.IdRestauraUp);
	}

	@Override
	public String toString() {
		return "Restaura [IdRestauraUp=" + IdRestauraUp + ", fileUp=" + fileUp + ", statusUp=" + statusUp + ", countUp="
				+ countUp + "]";
	}	
}
