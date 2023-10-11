package gui.sgbmodel.entities;

import java.io.Serializable;
import java.util.Date;

public class Cartela implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer numeroCar;
	private Date dataCar;
	private String localCar;
	private Double descontoCar;
	private Double totalCar;
	private String situacaoCar;
	private Integer numeroPaganteCar;
	private Double valorPaganteCar;
	private Integer mesCar;
	private Integer anoCar;
	private String obsCar;
	private String servicoCar;
	private Double valorServicoCar;	
	private Double subTotalCar;
	private String nomeSituacaoCar;
	private Integer mesPagCar;
	private Integer anoPagCar;
	private String clienteCar;

/*
 * cliente só aqui n classe, não esta associado - não no mysql	
 */
	private Cliente cliente;
	
	public Cartela() {
	}

	public Cartela(Integer numeroCar, Date dataCar, String localCar, Double descontoCar, Double totalCar,
			String situacaoCar, Integer numeroPaganteCar, Double valorPaganteCar, Integer mesCar, 
			Integer anoCar, String obsCar, String servicoCar, Double valorServicoCar, 
			Double subTotalCar, String nomeSituacaoCar,Integer mesPagCar, Integer anoPagCar, String clienteCar,
			Cliente cliente) {
		this.numeroCar = numeroCar;
		this.dataCar = dataCar;
		this.localCar = localCar;
		this.descontoCar = descontoCar;
		this.totalCar = totalCar;
		this.situacaoCar = situacaoCar;
		this.numeroPaganteCar = numeroPaganteCar;
		this.valorPaganteCar = valorPaganteCar;
		this.mesCar = mesCar;
		this.anoCar = anoCar;
		this.obsCar = obsCar;
		this.servicoCar = servicoCar;
		this.valorServicoCar = valorServicoCar;
		this.subTotalCar = subTotalCar;
		this.nomeSituacaoCar = nomeSituacaoCar;
		this.mesPagCar = mesPagCar;
		this.anoPagCar = anoPagCar;
		this.clienteCar = clienteCar;
		this.cliente = cliente;
	}

	public Integer getNumeroCar() {
		return numeroCar;
	}

	public void setNumeroCar(Integer numeroCar) {
		this.numeroCar = numeroCar;
	}

	public Date getDataCar() {
		return dataCar;
	}

	public void setDataCar(Date dataCar) {
		this.dataCar = dataCar;
	}

	public String getLocalCar() {
		return localCar;
	}

	public void setLocalCar(String localCar) {
		this.localCar = localCar;
	}

	public Double getDescontoCar() {
		return descontoCar;
	}

	public void setDescontoCar(Double descontoCar) {
		this.descontoCar = descontoCar;
	}

	public Double getTotalCar() {
		return totalCar;
	}

	public void setTotalCar(Double totalCar) {
		this.totalCar = totalCar;
	}

	public String getSituacaoCar() {
		return situacaoCar;
	}

	public void setSituacaoCar(String situacaoCar) {
		this.situacaoCar = situacaoCar;
	}

	public Integer getNumeroPaganteCar() {
		return numeroPaganteCar;
	}

	public void setNumeroPaganteCar(Integer numeroPaganteCar) {
		this.numeroPaganteCar = numeroPaganteCar;
	}

	public Double getValorPaganteCar() {
		return valorPaganteCar;
	}

	public void setValorPaganteCar(Double valorPaganteCar) {
		this.valorPaganteCar = valorPaganteCar;
	}

	public Integer getMesCar() {
		return mesCar;
	}

	public void setMesCar(Integer mesCar) {
		this.mesCar = mesCar;
	}

	public Integer getAnoCar() {
		return anoCar;
	}

	public void setAnoCar(Integer anoCar) {
		this.anoCar = anoCar;
	}

	public String getObsCar() {
		return obsCar;
	}

	public void setObsCar(String obsCar) {
		this.obsCar = obsCar;
	}

	public String getServicoCar() {
		return servicoCar;
	}

	public void setServicoCar(String servicoCar) {
		this.servicoCar = servicoCar;
	}

	public Double getValorServicoCar() {
		return valorServicoCar;
	}

	public void setValorServicoCar(Double valorServicoCar) {
		this.valorServicoCar = valorServicoCar;
	}

	public Double getSubTotalCar() {
		return subTotalCar;
	}

	public void setSubTotalCar(Double subTotalCar) {
		this.subTotalCar = subTotalCar;
	}

	public String getNomeSituacaoCar() {
		if (situacaoCar == "A") {
			nomeSituacaoCar = "Aberto";
		} 
		if (situacaoCar == "C") {
			nomeSituacaoCar = "Calote";
		}
		return nomeSituacaoCar;
	}

	public void setNomeSituacaoCar(String nomeSituacaoCar) {
		this.nomeSituacaoCar = nomeSituacaoCar;
	}

	public Integer getMesPagCar() {
		if (mesPagCar == null) {
			mesPagCar = 0;
		}
		return mesPagCar;
	}

	public void setMesPagCar(Integer mesPagCar) {
		this.mesPagCar = mesPagCar;
	}

	public Integer getAnoPagCar() {
		if (anoPagCar == null) {
			anoPagCar = 0;
		}
		return anoPagCar;
	}

	public void setAnoPagCar(Integer anoPagCar) {
		this.anoPagCar = anoPagCar;
	}
	
	public String getClienteCar() {
		return clienteCar;
	}

	public void setClienteCar(String clienteCar) {
		this.clienteCar = clienteCar;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public void calculaValorPagante() {
		valorPaganteCar = 0.00;
		if (numeroPaganteCar == 0) {
			numeroPaganteCar = 1;
		}
		valorPaganteCar = totalCar / numeroPaganteCar;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((localCar == null) ? 0 : localCar.hashCode());
		result = prime * result + ((situacaoCar == null) ? 0 : situacaoCar.hashCode());
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
		Cartela other = (Cartela) obj;
		if (localCar == null) {
			if (other.localCar != null)
				return false;
		} else if (!localCar.equals(other.localCar))
			return false;
		if (situacaoCar == null) {
			if (other.situacaoCar != null)
				return false;
		} else if (!situacaoCar.equals(other.situacaoCar))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Cartela [numeroCar=" + numeroCar + ", dataCar=" + dataCar + ", localCar=" + localCar + ", descontoCar="
				+ descontoCar + ", totalCar=" + totalCar + ", situacaoCar=" + situacaoCar + ", numeroPaganteCar="
				+ numeroPaganteCar + ", valorPaganteCar=" + valorPaganteCar + ", mesCar=" + mesCar + ", anoCar="
				+ anoCar + ", obsCar=" + obsCar + ", servicoCar=" + servicoCar + ", valorServicoCar=" + valorServicoCar
				+ ", subTotalCar=" + subTotalCar + ", nomeSituacaoCar=" + nomeSituacaoCar + ", mesPagCar=" + mesPagCar
				+ ", anoPagCar=" + anoPagCar + ", clienteCar=" + clienteCar + ", cliente=" + cliente + "]";
	}
}
