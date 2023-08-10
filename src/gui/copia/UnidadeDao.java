package gui.copia;

import java.util.List;

public interface UnidadeDao {

	void insert(Unidade obj);
	void deleteById(int cod);
	List<Unidade> findAll();
}
