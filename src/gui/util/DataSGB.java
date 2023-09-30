package gui.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DataSGB {

	public static void main(String[] args) {

//		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
//		SimpleDateFormat sdfT = new SimpleDateFormat("dd/MM/yyyy HH:MM:ss");
////		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
//		DateTimeFormatter dtf3 = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm").withZone(ZoneId.systemDefault());
////		// withZone c/ default ou a qtd de hs de fuso horario;
//		DateTimeFormatter dtf4 = DateTimeFormatter.ISO_DATE_TIME;
////		DateTimeFormatter dtf5 = DateTimeFormatter.ISO_INSTANT;
		
//		LocalDate d01 = LocalDate.now();
//		LocalDateTime d02 = LocalDateTime.now();
		Instant min = DataStaticSGB.instantAtual();
		Date dt = new Date(System.currentTimeMillis());
		System.out.println(dt + " " + min);
//		Instant d03 = Instant.now();
//		
//		LocalDate d04 = LocalDate.parse("2023-07-14");
//		LocalDateTime  d05 = LocalDateTime.parse("2023-07-14T11:30:30");
//		Instant d06 = Instant.parse("2023-07-14T11:30:30Z");
//		Instant d07 = Instant.parse("2023-07-14T11:30:30-03:00"); // hr londres
//		
//		LocalDate d08 = LocalDate.parse("14/07/2023", dtf);
		LocalDateTime d09 = LocalDateTime.parse("14/07/2023 11:30", dtf2);
//		LocalDate d10 = LocalDate.of(2023, 7, 14);
		LocalDateTime d11 = LocalDateTime.of(2023, 7, 14, 11, 30);

//		LocalDate dt1 = DataStaticSGB.criaLocalAtual();
//		LocalDate dt2 = DataStaticSGB.converteString("2023-07-15");
//		LocalDate dt3 = DataStaticSGB.criaAnoMesDia(2023, 10, 17);
//		LocalDate dt4 = DataStaticSGB.converteFormataString("15/09/2020");
//		Date dt5 = DataStaticSGB.localParaDateFormatada(dt1);
//		String dt55 = sdf.format(dt5);
//		boolean dt6 = DataStaticSGB.anoBissexto(dt4);
//		long dt7 = DataStaticSGB.durationPositivo(dt4, dt3).toHours();
//		long dt8 = DataStaticSGB.durationNegativo(dt3, dt4).toDays();;
//		LocalDate dt9 = DataStaticSGB.maisDiasLocal(dt4, 1127);
//		Date dt10 = DataStaticSGB.somaDiasDate(sdf.format(dt5), 30);
//		Duration dt11 = DataStaticSGB.intervalDiasDate(sdf.format(dt5), sdf.format(dt10));
//		Date dt12 = DataStaticSGB.somaMesDate(dt55, 12);
//		
//		System.out.println("dt1 " + dt1);
//		System.out.println("dt2 " + dt2);
//		System.out.println("dt3 " + dt3);
//		System.out.println("dt4 " + dt4);
//		System.out.println("dt5 " + dt55);
//		System.out.println("dt6 " + dt6);
//		System.out.println("dt7 " + dt7.toDays());
//		System.out.println("dt8 " + dt8.toDays());
//		System.out.println("dt9 " + dt9);
//		System.out.println("dt10 " + dt10);
//		System.out.println("dt11 " + dt11.toDays());
//		System.out.println("dt12 " + sdf.format(dt12));
		
//// INSTANCIAÇÃO datas		
//		// output formato iso default
//		System.out.println("d01 local date                      " + d01);
//		System.out.println("d01 local date                      " + d01.toString());
//		System.out.println("d02 local date time                 " + d02);
//		System.out.println("d03 instant                         " + d03);
//		System.out.println("d04 local date parse                " + d04);
//		System.out.println("d05 local date time parse           " + d05);
//		System.out.println("d06 instant parse                   " + d06);
//		System.out.println("d07 instant parse                   " + d07);
//		System.out.println("d08 local date parse formatado      " + d08);
		System.out.println("d09 local date time parse formatado " + d09); 
//		System.out.println("d10 local date of                   " + d10);
		System.out.println("d11 local date time of              " + d11);
//		
////FORMATAÇÃO datas		
//		// esse é o ........
//		System.out.println("d04 local date parse                " + d04.format(dtf));
//		System.out.println("d04 local date parse                " + dtf.format(d04));
//		System.out.println("d05 local date time parse           " + d05.format(dtf));
//		System.out.println("d05 local date time parse           " + dtf2.format(d05));
//		System.out.println("d05 local date time parse           " + dtf4.format(d05));
//		
//		System.out.println("d06 instant parse                   " + dtf3.format(d06));
//		System.out.println("d06 instant parse                   " + dtf5.format(d06));
//		System.out.println("d06 instant parse                   " + d06.toString());
//		
////OPERAÇÕES datas		
//	//	Instant d06 = Instant.parse("2023-07-14T11:30:30Z");
//		LocalDate r1 = LocalDate.ofInstant(d06, ZoneId.systemDefault());
//		System.out.println("r1 d06 instant parse                   " + r1);
//		System.out.println("r1 pronto para parse Date " + dtf.format(r1));
//		// vide dataStatic.converteInstante
//		LocalDate r2 = LocalDate.ofInstant(d06, ZoneId.of("Portugal"));
//		LocalDateTime r3 = LocalDateTime.ofInstant(d06, ZoneId.systemDefault()); // fuso - 3 hs de Londres
//		LocalDateTime r4 = LocalDateTime.ofInstant(d06, ZoneId.of("Portugal")); // fuso + 1 hr de Londres
//		System.out.println("r2 d06 instant parse                   " + r2);
//		System.out.println("r3 d06 instant parse                   " + r3);
//		System.out.println("r4 d06 instant parse                   " + r4);
//		
////BUSCANDO datas
//		System.out.println("dia d04 local date parse                " + d04.getDayOfMonth());
//		System.out.println("mes d04 local date parse                " + d04.getMonthValue());
//		System.out.println("mes nome d04 local date parse           " + d04.getMonth());
//		System.out.println("ano d04 local date parse                " + d04.getYear());
//		System.out.println("dia do ano d04 local date parse         " + d04.getDayOfYear());
//		System.out.println("HH d05 local date time parse            " + d05.getHour());
//		System.out.println("mm d05 local date time parse            " + d05.getMinute());
//		
//		LocalDate minusLD = d04.minusDays(7);
//		LocalDate plusLD = d04.plusDays(7);
//		Instant minusI = d06.minus(7, ChronoUnit.DAYS);
//		Instant plusI = d06.plus(7, ChronoUnit.DAYS);
//// CALCULOS datas
//		System.out.println("-7 dias d04 local date parse            " + minusLD);
//		System.out.println("+7 dias d04 local date parse            " + plusLD);
//		System.out.println("-7 d06 instant parse                    " + minusI);
//		System.out.println("+7 d06 instant parse                    " + plusI);
//		
//		Duration d1 = Duration.between(minusLD.atStartOfDay(), plusLD.atStartOfDay()); 
//		Duration d2 = Duration.between(plusLD.atStartOfDay(), minusLD.atStartOfDay()); 
//		Duration d3 = Duration.between(minusI,  plusI); 
//		Duration d4 = Duration.between(plusI, minusI);
//		
//		System.out.println("intervalo -  datas d04 local date parse " + d1.toDays());
//		System.out.println("intervalo +  datas d04 local date parse " + d2.toDays());
//		System.out.println("intervalo -  datas d06 instant parse    " + d3.toDays());
//		System.out.println("intervalo +  datas d06 instant parse    " + d4.toDays());
		
		LocalDate dtn = DataStaticSGB.criaLocalAtual();
		LocalDate dta = DataStaticSGB.menosAnoLocal(dtn, 5);
		LocalDate dtp = DataStaticSGB.maisAnoLocal(dtn, 2);
System.out.println(dtn + " " + dta + " " + dtp );		

		
	}
}
