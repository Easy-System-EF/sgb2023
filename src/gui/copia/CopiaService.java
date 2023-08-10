package gui.copia;

import java.util.List;

public class CopiaService {
	private CopiaDao dao = DaoFactory.createBackUpDao();

	public List<Copia> findAll() {
		return dao.findAll();
	}

	public void saveOrUpdate(Copia obj) {
		dao.insert(obj);
	}
	public void remove(int cod) {
		dao.deleteById(cod);
	}
}
