package gui.util;

import gui.sgbmodel.service.AdiantamentoService;
import gui.sgbmodel.service.CartelaService;
import gui.sgbmodel.service.EntradaService;
import gui.sgbmodel.service.FuncionarioService;
import gui.sgbmodel.service.ProdutoService;

public class LimpaTab {

	public static void main(String[] args) {

		AdiantamentoService adService = new AdiantamentoService();
//		CartelaVirtualService carVirService = new CartelaVirtualService();
		CartelaService carService = new CartelaService();
		EntradaService entService = new EntradaService();
		ProdutoService prodService = new ProdutoService();
		FuncionarioService funService = new FuncionarioService();
		
		adService.zeraAll();
//		carVirService.zeraAll();
		carService.zeraAll();
		entService.zeraAll();
		funService.zeraAll();
		prodService.zeraAll();
		
		System.out.println("Kbou!!!");

	}

}
