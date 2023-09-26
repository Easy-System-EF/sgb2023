package gui.sgbmodel.dao;

import gui.sgbmodel.entities.Cartela;

public interface CartelaCommitDao {
	void gravaCartelaCommit(Cartela car, String pc, String sit);
}
