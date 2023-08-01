package gui.sgbmodel.entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

import gui.util.DataStaticSGB;

public class Produto implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer codigoProd;
	private Integer grupoProd;
	private String nomeProd;
	private Double saldoProd;
	private Double estMinProd;
	private Double precoProd;
	private Double vendaProd;
	private Double cmmProd;
	private Double saidaCmmProd;
	private Date dataCadastroProd;
	private Double percentualProd;
	private char letraProd;

  	private Grupo grupo;

	public Produto() {
	}

	public Produto(Integer codigoProd, Integer grupoProd, String nomeProd, Double saldoProd, Double estMinProd, 
			Double precoProd, Double vendaProd, Double cmmProd, Double saidaCmmProd, Date dataCadastroProd, Grupo grupo,
			Double percentualProd, char letraProd) {
		this.codigoProd = codigoProd;
		this.grupoProd = grupoProd;
		this.nomeProd = nomeProd;
		this.saldoProd = saldoProd;
		this.estMinProd = estMinProd;
		this.precoProd = precoProd;
		this.vendaProd = vendaProd;
		this.cmmProd = cmmProd;
		this.saidaCmmProd = saidaCmmProd;
		this.dataCadastroProd = dataCadastroProd;
		this.grupo = grupo;
		this.percentualProd = percentualProd;
		this.letraProd = letraProd;
	}

	public Integer getCodigoProd() {
		return codigoProd;
	}

	public void setCodigoProd(Integer codigoProd) {
		this.codigoProd = codigoProd;
	}

	public Integer getGrupoProd() {
		return grupoProd;
	}

	public void setGrupoProd(Integer grupoProd) {
		this.grupoProd = grupoProd;
	}

	public String getNomeProd() {
		return nomeProd;
	}

	public void setNomeProd(String nomeProd) {
		this.nomeProd = nomeProd;
	}

	public void setSaldoProd(Double saldoProd) {
		this.saldoProd = saldoProd;
	}

	public Double getSaldoProd() {
		return saldoProd;
	}
	
	public void saidaSaldo(Double qtdS) {
		saldoProd -= qtdS;
	}
	
	public void entraSaldo(Double qtdE) {
		saldoProd += qtdE;
	}

	public Double getEstMinProd() {
		return estMinProd;
	}

	public void setEstMinProd(Double estMinProd) {
		this.estMinProd = estMinProd;
	}

	public Double getPrecoProd() {
		return precoProd;
	}

	public void setPrecoProd(Double precoProd) {
		this.precoProd = precoProd;
	}

	public Double getVendaProd() {
		return vendaProd;
	}

	public void setVendaProd(Double vendaProd) {
		this.vendaProd = vendaProd;
	}

	public void setCmmProd(Double cmmProd) {
		if (cmmProd == null) {
			cmmProd = 0.00;
		}
		this.cmmProd = cmmProd;
	}

	public Double getCmmProd() {
		if (cmmProd == null) {
			cmmProd = 0.00;
		}
		return cmmProd;
	}

	public Double getSaidaCmmProd() {
		return saidaCmmProd;
	}

	public void setSaidaCmmProd(Double qtd) {
		if (saidaCmmProd == null) {
			saidaCmmProd = 0.00;
		}
		this.saidaCmmProd += qtd;
	}
	
	public Date getDataCadastroProd() {
		return dataCadastroProd;
	}

	public void setDataCadastroProd(Date dataCadastroProd) {
		this.dataCadastroProd = dataCadastroProd;
	}

	public Grupo getGrupo() {
		return grupo;
	}

	public void setGrupo(Grupo grupo) {
		this.grupo = grupo;
	}

	public Double getPercentualProd() {
		return percentualProd;
	}

	public void setPercentualProd(Double percentualProd) {
		this.percentualProd = percentualProd;
	}
	
	public void calculaPercentual() {
		if (saldoProd > 0) {
			percentualProd = (((vendaProd - precoProd) * 100) / precoProd);
		}	
	}


	public char getLetraProd() {
		return letraProd;
	}

	public void setLetraProd(char letraProd) {
		this.letraProd = letraProd;
	}
	
	public double letraClassProd(double acum, double custo) {
		double perc = ((precoProd) * 100) / custo;
		letraProd = 'A';
		acum += perc;
		if (acum > 80.01) {
			if (acum > 80.00 && acum < 90.00)  {
				letraProd = 'B';
			} else {
				letraProd = 'C';
			}
		}	
		return acum;
	}

	public Double calculaCmm() {
		if (saidaCmmProd != null) {
			Date dataHoje = new Date();
			LocalDate dt1 = DataStaticSGB.dateParaLocal(dataHoje);
			LocalDate dt2 = DataStaticSGB.dateParaLocal(dataCadastroProd);
			long dias = DataStaticSGB.durationPositivo(dt2, dt1).toDays();
			int meses = 0;
//			if (getDataCadastroProd() != null) {
//				long dif = dataHoje.getTime() - dataCadastroProd.getTime();
//				long  dias = TimeUnit.DAYS.convert(dif, TimeUnit.MILLISECONDS);
				if (dias > 30) {
					meses = (int) (dias / 30);
					cmmProd = saidaCmmProd / meses;
				} else {
					cmmProd = saidaCmmProd;
				}
		} 	
		return cmmProd;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigoProd == null) ? 0 : codigoProd.hashCode());
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
		Produto other = (Produto) obj;
		if (codigoProd == null) {
			if (other.codigoProd != null)
				return false;
		} else if (!codigoProd.equals(other.codigoProd))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Produto [codigoProd=" + codigoProd + ", grupoProd=" + grupoProd + ", nomeProd=" + nomeProd
				+ ", saldoProd=" + saldoProd + ", estMinProd=" + estMinProd + ", precoProd=" + precoProd
				+ ", vendaProd=" + vendaProd + ", cmmProd=" + cmmProd + ", saidaCmmProd=" + saidaCmmProd
				+ ", dataCadastroProd=" + dataCadastroProd + ", percentualProd=" + percentualProd + ", letraProd="
				+ letraProd + ", grupo=" + grupo + "]";
	}

// 		Date dt  = new Date();
// 		Calendar cal = Calendar.getInstance();
// 		cal.setTime(dt);
// 		int aa = cal.get(Calendar.YEAR);
// 		int mm = cal.get(Calendar.MONTH);
//      saidaCmmProd é só para calculo do cmm	
}
