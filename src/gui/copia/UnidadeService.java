package gui.copia;

import java.util.List;

public class UnidadeService {
	private UnidadeDao dao = DaoFactory.createUnidadeDao();

	public List<Unidade> findAll() {
		return dao.findAll();
	}

	public void saveOrUpdate(Unidade obj) {
		dao.insert(obj);
	}
	public void remove(int cod) {
		dao.deleteById(cod);
	}
}
