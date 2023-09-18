package gui.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import db.DbException;

public class DataStaticSGB {

	static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	static SimpleDateFormat sdfAno = new SimpleDateFormat("yyyyy/MM/dd HH:mm:ss");
	static SimpleDateFormat sdfAnoTraco = new SimpleDateFormat("yyyyy-MM-dd");
	static SimpleDateFormat sdfTime = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	static DateTimeFormatter dtfano = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	static DateTimeFormatter dtfTime = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

	public static LocalDate criaLocalAtual() {
		LocalDate dtN = LocalDate.now();
		return dtN;
	}
	
	public static LocalDateTime criaLocalTimeAtual() {
		LocalDateTime dtT = LocalDateTime.now();
		return dtT;
	}
	
	public static LocalDate converteString(String data) {
		LocalDate dtS = LocalDate.parse(data);
		return dtS;
	}
	
	public static LocalDateTime converteTimeString(String data) {
		LocalDateTime dtT = LocalDateTime.parse(data);
		return dtT;
	}
	
	public static LocalDate converteFormataString(String data) {
		LocalDate dtFS = LocalDate.parse(data, dtf);
		return dtFS;
	}
	
	public static LocalDate converteTimeFormataString(String data) {
		LocalDate dtFS = LocalDate.parse(data, dtfTime);
		return dtFS;
	}
	
	public static LocalDate criaAnoMesDia(int ano, int mes, int dia) {
		LocalDate dtYMD = LocalDate.of(ano,  mes,  dia);
		return dtYMD;
	}
	
	public static LocalDate menosDiasLocal(LocalDate data, int dia) {
		LocalDate dtMD = data.minusDays(dia);
		return dtMD;
	}
	
	public static LocalDate maisDiasLocal(LocalDate data, int dia) {
		LocalDate dtPD = data.plusDays(dia);
		return dtPD;
	}
	
	public static LocalDate menosMesLocal(LocalDate data, int mes) {
		LocalDate dtMD = data.minusMonths(mes);
		return dtMD;
	}
	
	public static LocalDate maisMesLocal(LocalDate data, int mes) {
		LocalDate dtPD = data.plusMonths(mes);
		return dtPD;
	}
	
	public static LocalDate menosAnoLocal(LocalDate data, int ano) {
		LocalDate dtMD = data.minusYears(ano);
		return dtMD;
	}
	
	public static LocalDate maisAnoLocal(LocalDate data, int ano) {
		LocalDate dtPD = data.plusYears(ano);
		return dtPD;
	}
	
	public static Instant instantAtual() {
		Instant dtI = Instant.now();
		return dtI;
	}
	
	public static Instant instantMenosDia(Instant data, int dia) {
		Instant dtIMD = data.minus(5, ChronoUnit.DAYS);
		return dtIMD;
	}
	
	public static Instant instantMaisDia(Instant data, int dia) {
		Instant dtIMD = data.plus(5, ChronoUnit.DAYS);
		return dtIMD;
	}
	
	public static Instant instantMenosMes(Instant data, int mes) {
		Instant dtIMM = data.minus(5, ChronoUnit.MONTHS);
		return dtIMM;
	}
	
	public static Instant instantMenosAno(Instant data, int ano) {
		Instant dtIPY = data.minus(5, ChronoUnit.YEARS);
		return dtIPY;
	}
	
	public static Instant instantMaisAno(Instant data, int ano) {
		Instant dtIPY = data.plus(5, ChronoUnit.YEARS);
		return dtIPY;
	}
	
	public static Duration durationPositivo(LocalDate data1, LocalDate data2) {
		Duration dtDP = Duration.between(data1.atStartOfDay(), data2.atStartOfDay());
		return dtDP;
	}
	
	public static Duration durationNegativo(LocalDate data1, LocalDate data2) {
		Duration dtDN = Duration.between(data1.atStartOfDay(), data2.atStartOfDay());
		return dtDN;
	}
	
	public static int diaDaData(LocalDate data) {
		int dtD = data.getDayOfMonth();
		return dtD;
	}
	
	public static int mesDaData(LocalDate data) {
		int dtM = data.getMonthValue();
		return dtM;
	}
	
	public static int anoDaData(LocalDate data) {
		int dtY = data.getYear();
		return dtY;
	}
	
	public static boolean anoBissexto(LocalDate data) {
		boolean res = data.isLeapYear();
		return res;
	}
	
	public static int ultimoDiaMes(LocalDate data) {
		int dtF = data.lengthOfMonth();
		return dtF;
	}	
	
	public static Date InstantParaDateFormatada(Instant dataLocal) {
		LocalDate resultado = LocalDate.ofInstant(dataLocal, ZoneId.systemDefault());
		String data = dtf.format(resultado);
		Date instantConvertida = new Date();
		try {
			instantConvertida = sdf.parse(data);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return instantConvertida;

	}
	
	public static Date localParaDateSdfAno(LocalDate dataLocal) {
		String data = dtfano.format(dataLocal);
		Date localDate = new Date();
		try {
			localDate = sdfAnoTraco.parse(data);
		} catch (ParseException e) {
			throw new DbException(e.getMessage());
		}
		return localDate;
	}

	public static Date localParaDateFormatada(LocalDate dataLocal) {
		String data = dtf.format(dataLocal);
		Date localConvertida = new Date();
		try {
			localConvertida = sdf.parse(data);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return localConvertida;

	}

	public static LocalDate dateParaLocal(Date data) {
		String dt1 = sdf.format(data);
		LocalDate dt2 = converteFormataString(dt1);
		return dt2;
	}

	public static Date somaDiasDate(String data, int dias) {
		LocalDate dt1 = converteFormataString(data);
		LocalDate dt2 = maisDiasLocal(dt1, dias);
		String dataFor = dtf.format(dt2);
		Date localConvertida = new Date();
		try {
			localConvertida = sdf.parse(dataFor);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return localConvertida;
	}
	
	public static Date somaMesDate(String data, int mes) {
		LocalDate dt1 = converteFormataString(data);
		LocalDate dt2 = maisMesLocal(dt1, mes);
		String dataFor = dtf.format(dt2);
		Date localConvertida = new Date();
		try {
			localConvertida = sdf.parse(dataFor);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return localConvertida;
	}
	
	public static Duration intervalDiasDate(String data1, String data2) {
		LocalDate dt1 = converteFormataString(data1);
		LocalDate dt2 = converteFormataString(data2);
		Duration dt3 = durationPositivo(dt1, dt2);
		return dt3;
	}
}
