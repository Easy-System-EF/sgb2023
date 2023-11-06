package gui.sgb;

import java.util.ArrayList;
import java.util.List;

import gui.sgbmodel.entities.Produto;
import gui.sgbmodel.service.ProdutoService;

public class ProdutoMVRForm {

		private ProdutoService proService;
		Produto prod = new Produto();
		List<Produto> listP = new ArrayList<>();

		/*
		 * incl moka material - produto ak renomear materia p/ mat alguma coisa renomear
		 * produto para material c/ tds os metodos - entra sai ........
		 */
	// auxiliar
		String classe = "ProdutoMRV List ";
		public String user = "usuário";
		Double materialCusto = 0.00;

	// injeta a dependencia com set (invers�o de controle de inje�ao)	
		public void setProdutoMVRService(ProdutoService proService) {
			this.proService = proService;
		}

		/*
		 * MATERIAL
		 */
		public void materialCustoEstoque() {
//			listP.sort((p1, p2) -> p1.getNomeProd().toUpperCase().compareTo(p2.getNomeProd().toUpperCase()));
//			listP.removeIf(x -> x.getPrecoProd() == 0);
//			listP.removeIf(x -> x.getVendaProd() == 0);
//			listP.removeIf(x -> x.getSaldoProd() == 0);
			List<Produto> listProd0 = proService.findAll();
			listProd0.removeIf(x -> x.getPrecoProd() == 0);
			listProd0.removeIf(x -> x.getVendaProd() == 0);
			listProd0.removeIf(x -> x.getSaldoProd() == 0);
			for (Produto p0 : listProd0) {
					materialCusto += p0.getPrecoProd();
			}
		}
		
		public void materialPercentual() {
//			listP.sort((p1, p2) -> p1.getNomeProd().toUpperCase().compareTo(p2.getNomeProd().toUpperCase()));
//			listP.removeIf(x -> x.getPrecoProd() == 0);
//			listP.removeIf(x -> x.getVendaProd() == 0);
//			listP.removeIf(x -> x.getSaldoProd() == 0);
			List<Produto> listProd1 = proService.findAll();
			listProd1.removeIf(x -> x.getPrecoProd() == 0);
			listProd1.removeIf(x -> x.getVendaProd() == 0);
			listProd1.removeIf(x -> x.getSaldoProd() == 0);
			for (Produto m : listProd1) {
				m.calculaPercentual();
				proService.saveOrUpdate(m);
			}
		}

		public void materialClassifica() {
			double acum = 0.00;
//			listP.sort((p1, p2) -> p1.getPrecoProd().compareTo(p2.getPrecoProd()));
			List<Produto> listProd2 = proService.findABC();
			listProd2.removeIf(x -> x.getPercentualProd() == 0.00);
			for (Produto m2 : listProd2) {				
						m2.setLetraProd(' ');
						acum = m2.letraClassProd(acum, materialCusto);
						proService.saveOrUpdate(m2);
			}	
		}
}
