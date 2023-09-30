package gui.sgbmodel.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import gui.sgbmodel.dao.AdiantamentoDao;
import gui.sgbmodel.entities.Adiantamento;

public class AdiantamentoDaoJDBC implements AdiantamentoDao {

// tb entra construtor p/ conex�o
	private Connection conn;
	
	public AdiantamentoDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	String classe = "Adiantamento JDBC ";
	
	@Override
	public void insert(Adiantamento obj) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"INSERT INTO adiantamento " +
							"(DataAdi, ValeAdi, MesAdi, AnoAdi, ValorCartelaAdi, CartelaAdi, " +
							"ComissaoAdi, TipoAdi, salarioAdi, codigoFun, NomeFun, mesFun, anoFun, " +
							"cargoFun, situacaoFun, salarioFun, cargoId, situacaoId )" +
								"VALUES " + 
							"(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )",
							Statement.RETURN_GENERATED_KEYS);

			st.setDate(1, new java.sql.Date(obj.getDataAdi().getTime()));
			st.setDouble(2, obj.getValeAdi());
			st.setInt(3, obj.getMesAdi());
			st.setInt(4, obj.getAnoAdi());
			st.setDouble(5,  obj.getValorCartelaAdi());
			st.setInt(6, obj.getCartelaAdi());
			st.setDouble(7, obj.getComissaoAdi());
			st.setString(8, obj.getTipoAdi());
			st.setDouble(9, obj.getSalarioAdi());
			st.setInt(10, obj.getCodigoFun());
			st.setString(11, obj.getNomeFun());
			st.setInt(12, obj.getMesFun());
			st.setInt(13, obj.getAnoFun());
			st.setString(14, obj.getCargoFun());
			st.setString(15, obj.getSituacaoFun());
			st.setDouble(16, obj.getSalarioFun());
   			st.setInt(17, obj.getCargo().getCodigoCargo());
   			st.setInt(18,  obj.getSituacao().getNumeroSit());


			int rowsaffectad = st.executeUpdate();

			if (rowsaffectad > 0) {
				rs = st.getGeneratedKeys();
				if (rs.next()) {
					int codigo = rs.getInt(1);
					obj.setNumeroAdi(codigo);
				} else {
					throw new DbException(classe + " Erro!!! sem inclusão");
				}
			}
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
	}

	@Override
	public void insertBackUp(Adiantamento obj) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"INSERT INTO adiantamento " +
							"(NumeroAdi, DataAdi, ValeAdi, MesAdi, AnoAdi, ValorCartelaAdi, CartelaAdi, " +
							"ComissaoAdi, TipoAdi, salarioAdi, codigoFun, NomeFun, mesFun, anoFun, " +
							"cargoFun, situacaoFun, salarioFun, cargoId, situacaoId )" +
								"VALUES " + 
							"(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )",
							Statement.RETURN_GENERATED_KEYS);

			st.setInt(1, obj.getNumeroAdi());
			st.setDate(2, new java.sql.Date(obj.getDataAdi().getTime()));
			st.setDouble(3, obj.getValeAdi());
			st.setInt(4, obj.getMesAdi());
			st.setInt(5, obj.getAnoAdi());
			st.setDouble(6,  obj.getValorCartelaAdi());
			st.setInt(7, obj.getCartelaAdi());
			st.setDouble(8, obj.getComissaoAdi());
			st.setString(9, obj.getTipoAdi());
			st.setDouble(10, obj.getSalarioAdi());
			st.setInt(11, obj.getCodigoFun());
			st.setString(12, obj.getNomeFun());
			st.setInt(13, obj.getMesFun());
			st.setInt(14, obj.getAnoFun());
			st.setString(15, obj.getCargoFun());
			st.setString(16, obj.getSituacaoFun());
			st.setDouble(17, obj.getSalarioFun());
   			st.setInt(18, obj.getCargo().getCodigoCargo());
   			st.setInt(19,  obj.getSituacao().getNumeroSit());

			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
	}

	@Override
	public void deleteById(Integer codigo) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("DELETE FROM adiantamento WHERE NumeroAdi = ? ",
					Statement.RETURN_GENERATED_KEYS);

			st.setInt(1, codigo);
			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException(classe + " Erro!!! não excluído " + e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public Double comSumTotal(int mes, int ano, int codFun) {
		Double totCom = null;
		PreparedStatement st = null;
		ResultSet rs = null;
   		try {
			st = conn.prepareStatement(
					
			"select SUM(ComissaoAdi) AS total from adiantamento " +
					"WHERE adiantamento.MesAdi = ? AND adiantamento.AnoAdi = ? AND adiantamento.CodigoFun = ? "); 
	
			st.setInt(1, mes);
			st.setInt(2, ano);
			st.setInt(3, codFun);
			rs = st.executeQuery();
			while (rs.next()) {
				totCom = rs.getDouble("total");
			}	
   		}
 		catch (SQLException e) {
			throw new DbException ( "Erro!!! " + classe + "não totalizado " + e.getMessage()); }
 		finally {
 			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
		return totCom;
	}

	@Override
	public Double valeSumTotal(int mes, int ano, int codFun) {
		Double totAdi = null;
		PreparedStatement st = null;
		ResultSet rs = null;
   		try {
			st = conn.prepareStatement(
			
			"select SUM(ValeAdi) AS total from adiantamento " +
				"WHERE adiantamento.MesAdi = ? AND adiantamento.AnoAdi = ? AND adiantamento.CodigoFun = ? "); 

			st.setInt(1, mes);
			st.setInt(2, ano);
			st.setInt(3, codFun);
			
			rs = st.executeQuery();
			while (rs.next()) {
				totAdi = rs.getDouble("total");
			}	
   		}
 		catch (SQLException e) {
			throw new DbException ( "Erro!!! " + classe + "não totalizado " + e.getMessage()); }
 		finally {
 			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
		return totAdi;
	}

	@Override
	public List<Adiantamento> findPesquisaFun(String str) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT *, funcionario.* " + 
						"FROM adiantamento " + 
						"WHERE NomeFun like ? ");

			st.setString(1, str);
			rs = st.executeQuery();
			
			List<Adiantamento> adi = new ArrayList<>();

			if (rs.next()) {
				Adiantamento obj = instantiateAdiantamento(rs);
				adi.add(obj);
			}
			return adi;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public List<Adiantamento> findByCartela(Integer idCar) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT * FROM adiantamento " + 
						"WHERE CartelaAdi = ? ");

			st.setInt(1, idCar);
			rs = st.executeQuery();

			List<Adiantamento> adi = new ArrayList<>();

			if (rs.next()) {
				Adiantamento obj = instantiateAdiantamento(rs);
				adi.add(obj);
			}
			return adi;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

//auxiliar
// tipo => "A" = vale
//         "C" = comiss�o
	@Override
	public List<Adiantamento> findMesTipo(Integer mes, Integer ano, String tipo) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT * " + 
						"FROM adiantamento " +
							"WHERE MesAdi = ? AND AnoAdi = ? AND TipoAdi = ? " +
						"ORDER BY - DataAdi ");

			st.setInt(1, mes);
			st.setInt(2, ano);
			st.setString(3, tipo);
			rs = st.executeQuery();

			List<Adiantamento> list = new ArrayList<>();

			while (rs.next()) {
				Adiantamento obj = instantiateAdiantamento(rs);
				list.add(obj);
			}
			return list;
		} 
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		} 
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public Adiantamento findMesIdFun(Integer cod, Integer mes, Integer ano, String tipo) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT *, funcionario.* " + 
						"FROM adiantamento, Funcionario " +
							"WHERE CodigoFun = ? AND MesAdi = ? AND AnoAdi = ? AND TipoAdi = ? " +
						"ORDER BY - DataAdi ");

			st.setInt(1, cod);
			st.setInt(2, mes);
			st.setInt(3, ano);
			st.setString(4, tipo);
			rs = st.executeQuery();

			while (rs.next()) {
				Adiantamento obj = instantiateAdiantamento(rs);
				return obj;
			}
			return null;
		} 
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		} 
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public List<Adiantamento> findMes(Integer mes, Integer ano) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT * " + 
						"FROM adiantamento " +
							"WHERE MesAdi = ? AND AnoAdi = ? " +
						"ORDER BY - DataAdi ");

			st.setInt(1, mes);
			st.setInt(2, ano);
			rs = st.executeQuery();

			List<Adiantamento> list = new ArrayList<>();

			while (rs.next()) {
				Adiantamento obj = instantiateAdiantamento(rs);
				list.add(obj);
			}
			return list;
		} 
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		} 
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}
	
	@Override
	public List<Adiantamento> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT * " + 
						"FROM adiantamento " +
						"ORDER BY - NumeroAdi ");

			rs = st.executeQuery();

			List<Adiantamento> list = new ArrayList<>();

			while (rs.next()) {
				Adiantamento obj = instantiateAdiantamento(rs);
				list.add(obj);
			}
			return list;
		} 
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		} 
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}
	
	@Override
	public void zeraAll() {
		PreparedStatement st = null; 
		ResultSet rs = null;
		try {
			st = conn.prepareStatement( 
					"TRUNCATE TABLE sgb.Adiantamento " );

			st.executeUpdate();

		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	} 
	

	private Adiantamento instantiateAdiantamento(ResultSet rs) throws SQLException {
		Adiantamento adiantamento = new Adiantamento();
		adiantamento.setNumeroAdi(rs.getInt("NumeroAdi"));
		adiantamento.setDataAdi(new java.util.Date(rs.getTimestamp("DataAdi").getTime()));
		adiantamento.setValeAdi(rs.getDouble("ValeAdi"));
		adiantamento.setMesAdi(rs.getInt("MesAdi"));
		adiantamento.setAnoAdi(rs.getInt("AnoAdi"));
		adiantamento.setValorCartelaAdi(rs.getDouble("ValorCartelaAdi"));
		adiantamento.setCartelaAdi(rs.getInt("CartelaAdi"));
		adiantamento.setComissaoAdi(rs.getDouble("ComissaoAdi"));
		adiantamento.setTipoAdi(rs.getString("TipoAdi"));
		adiantamento.setSalarioAdi(rs.getDouble("SalarioAdi"));
		adiantamento.setCodigoFun(rs.getInt("CodigoFun"));
		adiantamento.setNomeFun(rs.getString("NomeFun"));
		adiantamento.setMesFun(rs.getInt("MesFun"));
		adiantamento.setAnoFun(rs.getInt("AnoFun"));
		adiantamento.setSalarioFun(rs.getDouble("SalarioFun"));
		adiantamento.setCargoFun(rs.getNString("CargoFun"));
		adiantamento.setSituacaoFun(rs.getString("SituacaoFun"));
		adiantamento.setSalarioFun(rs.getDouble("SalarioFun"));
		return adiantamento;
	}
}
