package gui.sgb; 

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

import gui.sgbmodel.entities.Empresa;
import gui.sgbmodel.service.EmpresaService;
import gui.util.Extenso;
import gui.util.Imprimir;
import javafx.fxml.Initializable;

public class ConvenioImprimeController implements Initializable, Serializable {

	private static final long serialVersionUID = 1L;

	Locale localeBr = new Locale("pt", "BR");
 	DecimalFormat df = new DecimalFormat("#,###,##0.00"); 
	Calendar cal = Calendar.getInstance();

	String tabelaMeses = "Janeiro,Fevereiro,Março,Abril,Maio,Junho,Julho,Agosto,Setembro,Outubro,Novembro,Dezembro";
 	String mes = "";
  	String path = "c:\\Dataprol\\WINPRINT\\convenio.prn";
	String pathI = "C:\\Arqs\\impr\\convenio.txt";
	String comp = " ";
	String extso = "";
	String extso1 = "";
	String extso2 = "";
	String nomeFirma = "";
	String endereco = "";
	String telefone = "";
	String email = "";
	
	int diaHj = 0;
	int mesHj = 0;
	int anoHj = 0;
	int tam = 0;

 	public static Integer numEmp = null;

	Empresa empresa = new Empresa();
	EmpresaService empService = new EmpresaService();

	String linha01 = "                              R E C I B O";
	String linha02 = "          Recebi de xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx, ";
	String linha03 = "o valor de R$9.999,99, referente a adiantamento nesta data.";
	String linha4a = "";
	String linha4b = "";
	String linha05 = "                         _________________________";
	String linha06 = "";
	String linha07 = "";
	String linhaBranco = "                                                               ";
	String linhaEmail = "";
	String linhaTelefone = "";
	String margem = "  ";

	public void imprimeConvenio(String nome, double total) {
		empresa = empService.findById(numEmp);
		nomeFirma = empresa.getNomeEmp();
		endereco = empresa.getEnderecoEmp();
		telefone = empresa.getTelefoneEmp();
		email = empresa.getEmailEmp();
  		grava(nome, total);
  		Imprimir.relatorio(pathI);
   	}

	public void grava(String nome, Double total) {
	 		try
 			{	BufferedWriter bwP = new BufferedWriter(new FileWriter(pathI));
 						Date dtHj = new Date();
						cal.setTime(dtHj);
 						diaHj = cal.get(Calendar.DAY_OF_MONTH);
 						mesHj = cal.get(Calendar.MONTH);
 						anoHj = cal.get(Calendar.YEAR);
 						String[] meses = tabelaMeses.split(",");
 						mes = meses[mesHj];
 						String dfVlr = df.format(total); 
 						tam = nome.length();
 						Asteriscos(41, tam);
 						linha02 = String.format("%s%s%s%s%s",   margem,
 								"          Recebi de ", nome, comp, ","); 
 						linha03 = String.format("%s%s%s%s", margem, 
 								"o valor de R$", dfVlr, ", referente serviços, nesta data.");
 						int parou = 0;
 						int i = 0;
 					    extso =  Extenso.valorPorExtenso(total);
 						tam = extso.length();
 						if (tam < 63) {
						    Asteriscos(59, extso.length());
 	 						linha4a = String.format("%s%s%s%s%s%s", margem, 
 	 								"(", extso, comp, ")", ".");
 						}
 						if (tam > 62) {
 							while (parou < 1) {
 							    String[] ext = extso.split(" ");
						    	if (ext[i].contains("centavo")) {
						    		parou += 1;
						    	}
						    	if (ext[i].contains("centavos")) {
						    		parou += 1;
						    	}
 							    if (extso1.length() <= 55) {
 							    	extso1 += ext[i] + " ";
 							    } else {	
 							    	extso2 += ext[i] + " ";
 							    }	
 							    if (parou == 0) {
 							    	i += 1; 							    	
 							    }	
 							}
 	 							Asteriscos(60, extso1.length());
 	 	 						linha4a = String.format("%s%s%s%s%s", margem, 
 	 	 								"(", extso1, comp, ",");
 							    Asteriscos(60, extso2.length());
 	 	 						linha4b = String.format("%s%s", margem, extso2) +
 	 	 								  String.format("%s", comp) +
 	 	 								  String.format("%s%s", ")", ".");
 						}	
 						linha06 = String.format("%s%s%s", margem, "                         ", nomeFirma);
 						linhaEmail = String.format("%s%s%s", margem, "                         ", email);
 						linhaTelefone = String.format("%s%s%s", margem, "                         ", telefone);
 						linha07 = String.format
 	 							("%s%s%d%s%s%s%d", margem, "          Contagem, ", 
 	 									diaHj, " de ", mes, " de ", anoHj);
 						bwP.write(linhaBranco);
 						bwP.newLine();
 						bwP.write(linhaBranco);
 						bwP.newLine();
 						bwP.write(linha01);
 						bwP.newLine();
 						bwP.write(linhaBranco);
 						bwP.newLine();
 						bwP.write(linha02);
 						bwP.newLine();
 						bwP.write(linha03);
 						bwP.newLine();
 						bwP.write(linha4a);
 						bwP.newLine();
 						bwP.write(linha4b);
						bwP.newLine();
 						bwP.write(linhaBranco);
						bwP.newLine();
 						bwP.write(linhaBranco);
						bwP.newLine();
						bwP.write(linha05);
						bwP.newLine();
						bwP.write(linha06);
						bwP.newLine();
						bwP.write(linhaEmail);
						bwP.newLine();
						bwP.write(linhaTelefone);
						bwP.newLine();
 						bwP.write(linhaBranco);
						bwP.newLine();
 						bwP.write(linhaBranco);
						bwP.newLine();
						bwP.write(linha07);
						bwP.close();
	 			}	
 	 			catch(IllegalArgumentException e1) {
	 				System.out.println(e1.getMessage());
	 			}	catch(	IOException e2) {
	 				System.out.println(e2.getMessage());	 			
	 			}
	 		}

	public String Asteriscos(int lh, int tam) {
		comp = " ";
		int limite = lh - tam;
		for (int i = 1 ; i < limite ; i ++) {
			comp = comp + "*";
		}
		return comp;
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
	}

}
