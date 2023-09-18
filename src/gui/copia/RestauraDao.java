package gui.copia;

import java.util.List;

public interface RestauraDao {

	void insert(Restaura obj);
	void deleteById(int cod);
	List<Restaura> findAll();
	void zeraAll();
}
