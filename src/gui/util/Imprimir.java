package gui.util;

import java.awt.Desktop;
import java.awt.Font;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;

public class Imprimir {

	@SuppressWarnings("unused")
	private static String COURIER;
	@SuppressWarnings("unused")
	private static Font getFont(String COURIER, float size) {
		return null;
	}

	public static void relatorio(String str) {
		File file = new File(str);
		Desktop desktop = Desktop.getDesktop();
		try {
			desktop.print(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
//	public static PrintService impressora;
//	public Imprimir() {
//		detectaImpressora();
//	}
//	
//	private void detectaImpressora() {
//		try {
//			Scanner sc = new Scanner(System.in); 
//			char sn = ' ';
//			DocFlavor df = DocFlavor.SERVICE_FORMATTED.PRINTABLE;
//			PrintService[] ps = PrintServiceLookup.lookupPrintServices(df, null);
//			for (PrintService p : ps) {
//				System.out.println("Impressora  : " + p.getName());
//				if (p.getName().contains("Text") || p.getName().contains("Genereic")) {
//					sn = sc.next().charAt(0);
//					if (sn != ' ') {
//						impressora = p;
//						System.out.println("Selecionada: " + p.getName());
//						break;
//					}	
//				}
//			}
//		}
//		catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	public synchronized boolean imprime(String path) {
//		if (impressora == null) {
//			String msg = "Impressora n�o encontrada ";
//			System.out.println(msg);
//		} else {
//			try {
//				DocPrintJob dpj = impressora.createPrintJob();
//				InputStream stream = new ByteArrayInputStream(path.getBytes());
//				DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
//				Doc doc = new SimpleDoc(stream,  flavor, null);
//				dpj.print(doc, null);
//				return true;
//			}
//			catch (PrintException e) {
//				e.printStackTrace();
//			}
//		}
//		return false;
//	}
	
	public static void ImprJob(String str) throws FileNotFoundException {
//		FileInputStream file = new FileInputStream(str);
		@SuppressWarnings("resource")
		Scanner sc = new Scanner(System.in);
		PrintService[] pss = PrintServiceLookup.lookupPrintServices(null, null);
		PrintService impressora = null;
		int i = 0;
		System.out.println("N�mero e Impressora ");
		
		for (i = 0 ; i < pss.length ; i ++) {
			System.out.println(i + " " + pss[i].getName());
		}
			try {
				System.out.print("Digite o n�mero da impressora escolhida: ");
				int imp = sc.nextInt();
				if (imp > pss.length - 1) {
					System.out.println(i + " Impressora n�o existe");
					impressora = pss[0];
					System.out.println("Impressora padr�o - " + pss[0].getName());
				} else {
					impressora = pss[imp];
				}	
				sc.close();	
				DocPrintJob dpj = impressora.createPrintJob();
//			InputStream stream = new ByteArrayInputStream(str.getBytes(StandardCharsets.UTF_8));
				InputStream stream = new ByteArrayInputStream(str.getBytes());
				DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
				Doc doc = new SimpleDoc(stream, flavor, null);
				dpj.print(doc, null);			
			}
			catch (PrintException p) {
				p.printStackTrace();
			}
		}
}
