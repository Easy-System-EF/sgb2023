package gui.copia;

import java.util.List;

public class RestauraService {

	private RestauraDao dao = DaoFactory.createRestauraDao();

	public List<Restaura> findAll() {
		return dao.findAll();
	}

	public void saveOrUpdate(Restaura obj) {
		dao.insert(obj);
	}
	public void remove(int cod) {
		dao.deleteById(cod);
	}
	
	public void zeraAll() {
   		dao.zeraAll();
	} 	
}
