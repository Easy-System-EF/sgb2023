package gui.copia;

import java.util.List;

public interface CopiaDao {

	void insert(Copia obj);
	void deleteById(int cod);
	List<Copia> findAll();
}
