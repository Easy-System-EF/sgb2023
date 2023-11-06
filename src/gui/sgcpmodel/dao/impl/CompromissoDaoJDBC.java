package gui.sgcpmodel.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import gui.sgcpmodel.dao.CompromissoDao;
import gui.sgcpmodel.entites.Compromisso;
import gui.sgcpmodel.entites.Fornecedor;
import gui.sgcpmodel.entites.TipoConsumo;
import gui.sgcpmodel.entites.consulta.ParPeriodo;

public class CompromissoDaoJDBC implements CompromissoDao {
 
// aq entra um construtor declarando a conex�o	
	private Connection conn;
	
	public CompromissoDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	String classe = "Compromisso ";
	
 	@Override
	public void insert(Compromisso obj) {
  		PreparedStatement st = null;
		ResultSet rs = null;
 		try {
			st = conn.prepareStatement(
					"INSERT INTO compromisso "  
					+ "(CodigoFornecedorCom, NomeFornecedorCom, NnfCom, DataCom, "
					+ "DataVencimentoCom, ValorCom, ParcelaCom, PrazoCom, "
					+ "FornecedorIdCom, TipoIdCom, PeriodoIdCom, situacaoCom ) " 
					+ "VALUES " +  
					"(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )",
 
					Statement.RETURN_GENERATED_KEYS); 

			st.setInt(1, obj.getFornecedor().getCodigo()); 
			st.setString(2, obj.getFornecedor().getRazaoSocial()); 
   			st.setInt(3, obj.getNnfCom());
			st.setDate(4, new java.sql.Date(obj.getDataCom().getTime()));
			st.setDate(5, new java.sql.Date(obj.getDataVencimentoCom().getTime()));
 			st.setDouble(6, obj.getValorCom());
			st.setInt(7, obj.getParcelaCom());
			st.setInt(8, obj.getPrazoCom());
 			st.setInt(9, obj.getFornecedor().getCodigo());
 			st.setInt(10, obj.getTipoFornecedor().getCodigoTipo());
 			st.setInt(11, obj.getParPeriodo().getIdPeriodo());
 			st.setInt(12, obj.getSituacaoCom());
  			
 			int rowsaffectad = st.executeUpdate();
				
			if (rowsaffectad > 0) {
				rs = st.getGeneratedKeys();
				if (rs.next()) {
					int codigo = rs.getInt(1);
					obj.setIdCom(codigo);
//					System.out.println("Novo inserido: " + obj.getCodigo());
				} else {
					throw new DbException("Erro!!! sem inclusão " + classe );
				}	
	  		}
   		}
 		catch (SQLException e) {
			throw new DbException ("Erro compromisso !!! no insert " + classe + " " + e.getMessage());  
		}
		finally {
 			DB.closeStatement(st);
 			DB.closeResultSet(rs);
		}
	}

 	@Override
	public void insertBackUp(Compromisso obj) {
  		PreparedStatement st = null;
		ResultSet rs = null;
 		try {
			st = conn.prepareStatement(
					"INSERT INTO compromisso "  
					+ "(IdCom, CodigoFornecedorCom, NomeFornecedorCom, NnfCom, DataCom, "
					+ "DataVencimentoCom, ValorCom, ParcelaCom, PrazoCom, "
					+ "FornecedorIdCom, TipoIdCom, PeriodoIdCom, SituacaoCom ) " 
					+ "VALUES " +  
					"(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )",
 
					Statement.RETURN_GENERATED_KEYS); 

			st.setInt(1, obj.getIdCom()); 
			st.setInt(2, obj.getFornecedor().getCodigo()); 
			st.setString(3, obj.getFornecedor().getRazaoSocial()); 
   			st.setInt(4, obj.getNnfCom());
			st.setDate(5, new java.sql.Date(obj.getDataCom().getTime()));
			st.setDate(6, new java.sql.Date(obj.getDataVencimentoCom().getTime()));
 			st.setDouble(7, obj.getValorCom());
			st.setInt(8, obj.getParcelaCom());
			st.setInt(9, obj.getPrazoCom());
 			st.setInt(10, obj.getFornecedor().getCodigo());
 			st.setInt(11, obj.getTipoFornecedor().getCodigoTipo());
 			st.setInt(12, obj.getParPeriodo().getIdPeriodo());
 			st.setInt(13, obj.getSituacaoCom());
  			
 			 st.executeUpdate();
				
   		}
 		catch (SQLException e) {
			throw new DbException ("Erro compromisso !!! no insert " + classe + " " + e.getMessage());  
		}
		finally {
 			DB.closeStatement(st);
 			DB.closeResultSet(rs);
		}
	}

	@Override
	public void update(Compromisso obj) {
		PreparedStatement st = null;
  		try {
			st = conn.prepareStatement(
					"UPDATE compromisso "   
							+ "SET CodigoFornecedorCom = ? , NomeFornecedorCom = ? , NnfCom = ? , DataCom = ? , " 
							+ "DataVencimentoCom = ? , ValorCom = ? , ParcelaCom = ? , PrazoCom = ? , FornecedorIdCom = ?, "
							  + "TipoIdCom = ? , PeriodoIdCom = ?, SituacaoCom = ? " +
						"WHERE (IdCom = ?)",
				 				Statement.RETURN_GENERATED_KEYS);
					
					st.setInt(1, obj.getFornecedor().getCodigo()); 
					st.setString(2, obj.getFornecedor().getRazaoSocial()); 
					st.setInt(3, obj.getNnfCom());
					st.setDate(4, new java.sql.Date(obj.getDataCom().getTime()));
					st.setDate(5, new java.sql.Date(obj.getDataVencimentoCom().getTime()));
					st.setDouble(6, obj.getValorCom());
					st.setInt(7, obj.getParcelaCom());
					st.setInt(8, obj.getPrazoCom());
					st.setInt(9, obj.getFornecedor().getCodigo());
					st.setInt(10, obj.getTipoFornecedor().getCodigoTipo());
					st.setInt(11, obj.getParPeriodo().getIdPeriodo());
		 			st.setInt(12, obj.getSituacaoCom());
		 			st.setInt(13, obj.getIdCom());
					st.executeUpdate();
 				
		   		}
		 		catch (SQLException e) {
					throw new DbException ("Erro compromisso !!! no insert " + classe + " " + e.getMessage());  
				}
				finally {
		 			DB.closeStatement(st);
				}
			}

 	
 	@Override
	public void deleteById(int cod, int nnf) {
 		PreparedStatement st = null;
   		try {
			st = conn.prepareStatement(
					"DELETE FROM compromisso WHERE codigoFornecedorCom = ? AND NnfCom = ? ", Statement.RETURN_GENERATED_KEYS);
				  
			st.setInt(1, cod);
			st.setInt(2, nnf);
 			st.executeUpdate();
      		}
  			catch (SQLException e) {
  				throw new DbException ( "Erro compromisso !!! sem exclusão " + classe + " " + e.getMessage()); }
  			finally {
  				DB.closeStatement(st);
  			}
		}

	@Override
	public Double findByTotalFor(int codTp) {
		Double totTipo = null;
		PreparedStatement st = null;
		ResultSet rs = null;
   		try {
			st = conn.prepareStatement(
					"SELECT SUM(ValorCom) AS 'total' from compromisso WHERE TipoIdCom = ? "); 
			
			st.setInt(1, codTp);
			rs = st.executeQuery();
			while (rs.next()) {
				totTipo = rs.getDouble("total");
			}	
   		}
 		catch (SQLException e) {
			throw new DbException ( "Erro!!! " + classe + "não totalizado " + e.getMessage()); }
 		finally {
 			DB.closeStatement(st);
		}
		return totTipo;
	}
 	
	@Override
	public List<Compromisso> findPesquisa(String str) {
		PreparedStatement st = null; 
		ResultSet rs = null;
		try {
			st = conn.prepareStatement( 
					"SELECT *, Fornecedor.*, TipoFornecedor.CodigoTipo, ParPeriodo.IdPeriodo " +
						"FROM compromisso " +
							"INNER JOIN fornecedor " +
 								"ON compromisso.FornecedorIdCom = fornecedor.Codigo " +
 							"INNER JOIN tipoFornecedor " +
 								"ON compromisso.TipoIdCom = tipoFornecedor.CodigoTipo " +
 							"INNER JOIN parPeriodo " +
 								"ON compromisso.PeriodoIdCom = parPeriodo.IdPeriodo " +
						"WHERE FornecedorIdCom.RazaoSocial like ? " +	
					"ORDER BY NomeFornecedorCom ");
			
			st.setString(1, str + "%");
			rs = st.executeQuery();
			
			List<Compromisso> list = new ArrayList<>();
			Map<Integer, Fornecedor> mapFor = new HashMap<>();
			Map<Integer, TipoConsumo> mapTp = new HashMap<>();
			Map<Integer, ParPeriodo> mapPer = new HashMap<>();
			
			while (rs.next()) {
				Fornecedor objFor = mapFor.get(rs.getInt("FornecedorIdCom"));
				if (objFor == null) {
					objFor = instantiateFornecedor(rs);
					mapFor.put(rs.getInt("FornecedorIdCom"), objFor);
				}	
				TipoConsumo objTp = mapTp.get(rs.getInt("TipoIdCom"));
				if (objTp == null) {
					objTp = instantiateTipoFornecedor(rs);
					mapTp.put(rs.getInt("TipoIdCom"), objTp);
				}	
				ParPeriodo objPer = mapPer.get(rs.getInt("PeriodoIdCom"));
				if (objPer == null) {
					objPer = instantiatePeriodo(rs, objFor, objTp);
					mapPer.put(rs.getInt("PeriodoIdCom"), objPer);
				}	
				Compromisso obj = instantiateCompromisso(rs, objFor, objTp, objPer);
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
	
 	private Compromisso instantiateCompromisso(ResultSet rs, Fornecedor forn, TipoConsumo tipoForn, ParPeriodo periodo) throws SQLException {
 		Compromisso obj = new Compromisso();
		obj.setIdCom(rs.getInt("IdCom"));
		obj.setCodigoFornecedorCom(rs.getInt("CodigoFornecedorCom"));
		obj.setNomeFornecedorCom(rs.getString("NomeFornecedorCom"));
   		obj.setNnfCom(rs.getInt("NnfCom"));
		obj.setDataCom(new java.util.Date(rs.getTimestamp("DataCom").getTime()));
		obj.setDataVencimentoCom(new java.util.Date(rs.getTimestamp("DataVencimentoCom").getTime()));
 		obj.setValorCom(rs.getDouble("ValorCom"));
  		obj.setParcelaCom(rs.getInt("ParcelaCom"));
		obj.setPrazoCom(rs.getInt("PrazoCom"));
		obj.setFornecedor(forn);
		obj.setTipoFornecedor(tipoForn);
		obj.setParPeriodo(periodo);
		obj.setSituacaoCom(rs.getInt("SituacaoCom"));
  		return obj;
	}

	private Fornecedor instantiateFornecedor(ResultSet rs) throws SQLException {
		Fornecedor forn = new Fornecedor();
		forn.setCodigo(rs.getInt("Codigo"));
		forn.setRazaoSocial(rs.getString("RazaoSocial"));
 		forn.setRua(rs.getString("Rua"));
		forn.setNumero(rs.getInt("Numero"));
		forn.setComplemento(rs.getString("Complemento"));
 		forn.setBairro(rs.getString("Bairro"));
		forn.setCidade(rs.getString("Cidade"));
		forn.setUf(rs.getString("UF"));
		forn.setCep(rs.getString("Cep"));
		forn.setDdd01(rs.getInt("Ddd01"));
		forn.setTelefone01(rs.getInt("Telefone01"));
		forn.setDdd02(rs.getInt("Ddd02"));
		forn.setTelefone02(rs.getInt("Telefone02"));
		forn.setContato(rs.getString("Contato"));
		forn.setDddContato(rs.getInt("DddContato"));
		forn.setTelefoneContato(rs.getInt("TelefoneContato"));
		forn.setEmail(rs.getString("Email"));
		forn.setPix(rs.getString("Pix"));
		forn.setObservacao(rs.getString("observacao"));
		forn.setPrazo(rs.getInt("Prazo"));
 		return forn;
 	}
  	
	private TipoConsumo instantiateTipoFornecedor(ResultSet rs) throws SQLException {
		TipoConsumo tipoForn = new TipoConsumo();
		tipoForn.setCodigoTipo(rs.getInt("CodigoTipo"));
		tipoForn.setNomeTipo(rs.getString("NomeTipo"));
 		return tipoForn;
	}

	private ParPeriodo instantiatePeriodo(ResultSet rs, Fornecedor objFor, TipoConsumo objTp) throws SQLException {
		ParPeriodo periodo = new ParPeriodo();
		periodo.setIdPeriodo(rs.getInt("IdPeriodo"));
		periodo.setDtiPeriodo(rs.getDate("DtiPeriodo"));
		periodo.setDtfPeriodo(rs.getDate("DtfPeriodo"));
		periodo.setFornecedor(objFor);
		periodo.setTipoConsumo(objTp);
  		return periodo;
	}

	@Override
	public List<Compromisso> findAll() {
 		PreparedStatement st = null;
		ResultSet rs = null;
		try {
// nome do fornecedor(apelido aqui) = ForId					
			st = conn.prepareStatement(
 // nome das entities fornecedor(apelido aqui) = ForName / TipoName

					"SELECT *, Fornecedor.Codigo, TipoFornecedor.CodigoTipo, ParPeriodo.IdPeriodo " +
					 	"FROM Compromisso " +
					 		"INNER JOIN fornecedor " +
					 			"on compromisso.FornecedorIdCom = fornecedor.Codigo " +
					 		"INNER JOIN Tipofornecedor " +
					 			"on compromisso.TipoIdCom = tipoFornecedor.CodigoTipo " +
					 		"INNER JOIN parPeriodo " +
					 			"on compromisso.PeriodoIdCom = parPeriodo.IdPeriodo " +
					 		"WHERE situacaoCom = 0 " +
    								"ORDER BY DataVencimentoCom ");
 			
 			rs = st.executeQuery();
 
 			List<Compromisso> list = new ArrayList<>();
 			Map<Integer, Fornecedor> mapFor = new HashMap<>();
 			Map<Integer, TipoConsumo> mapTp = new HashMap<>();
 			Map<Integer, ParPeriodo> mapPer = new HashMap<>();

 			while (rs.next()) {
 				Fornecedor objFor = mapFor.get(rs.getInt("FornecedorIdCom"));
 				if (objFor == null) {
 					objFor = instantiateFornecedor(rs);
 					mapFor.put(rs.getInt("FornecedorIdCom"), objFor);
 				}	
 				TipoConsumo objTp = mapTp.get(rs.getInt("TipoIdCom"));
 				if (objTp == null) {
 					objTp = instantiateTipoFornecedor(rs);
 					mapTp.put(rs.getInt("TipoIdCom"), objTp);
 				}	
 				ParPeriodo objPer = mapPer.get(rs.getInt("PeriodoIdCom"));
 				if (objPer == null) {
 					objPer = instantiatePeriodo(rs, objFor, objTp);
 					mapPer.put(rs.getInt("PeriodoIdCom"), objPer);
 				}	
 				Compromisso obj = instantiateCompromisso(rs, objFor, objTp, objPer);
 				list.add(obj);
 			}
 			return list;
		}
		catch (SQLException e) {
			throw new DbException (e.getMessage());
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
 	}

	@Override
	public List<Compromisso> findByFor(int cod) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT *, Fornecedor.Codigo, TipoFornecedor.CodigoTipo, ParPeriodo.IdPeriodo " +
						"FROM compromisso " +
							"INNER JOIN fornecedor " +
 								"ON compromisso.FornecedorIdCom = fornecedor.Codigo " +
 							"INNER JOIN tipoFornecedor " +
 								"ON compromisso.TipoIdCom = tipoFornecedor.CodigoTipo " +
 							"INNER JOIN parPeriodo " +
 								"ON compromisso.PeriodoIdCom = parPeriodo.IdPeriodo " +
						"WHERE CodigoFornecedorCom = ? " +	
					"ORDER BY NomeFornecedorCom ");
				
				st.setInt(1, cod);
				rs = st.executeQuery();
				
				List<Compromisso> list = new ArrayList<>();
				Map<Integer, Fornecedor> mapFor = new HashMap<>();
				Map<Integer, TipoConsumo> mapTp = new HashMap<>();
				Map<Integer, ParPeriodo> mapPer = new HashMap<>();
				
				while (rs.next()) {
					Fornecedor objFor = mapFor.get(rs.getInt("FornecedorIdCom"));
					if (objFor == null) {
						objFor = instantiateFornecedor(rs);
						mapFor.put(rs.getInt("FornecedorIdCom"), objFor);
					}	
					TipoConsumo objTp = mapTp.get(rs.getInt("TipoIdCom"));
					if (objTp == null) {
						objTp = instantiateTipoFornecedor(rs);
						mapTp.put(rs.getInt("TipoIdCom"), objTp);
					}	
					ParPeriodo objPer = mapPer.get(rs.getInt("PeriodoIdCom"));
					if (objPer == null) {
						objPer = instantiatePeriodo(rs, objFor, objTp);
						mapPer.put(rs.getInt("PeriodoIdCom"), objPer);
					}	
					Compromisso obj = instantiateCompromisso(rs, objFor, objTp, objPer);
					list.add(obj);
	 			}
				return list;
			}
		catch (SQLException e) {
			throw new DbException (e.getMessage());
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
 	}

	@Override
	public Compromisso findById(int cod, int nnf) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT *, Fornecedor.Codigo, TipoFornecedor.CodigoTipo, ParPeriodo.IdPeriodo " +
							"FROM compromisso " +
								"INNER JOIN fornecedor " +
	 								"ON compromisso.FornecedorIdCom = fornecedor.Codigo " +
	 							"INNER JOIN tipoFornecedor " +
	 								"ON compromisso.TipoIdCom = tipoFornecedor.CodigoTipo " +
	 							"INNER JOIN parPeriodo " +
	 								"ON compromisso.PeriodoIdCom = parPeriodo.IdPeriodo " +
	 						"WHERE CodigoFornecedorCom = ? AND NnfCom = ? " +	
						"ORDER BY DataCom ");
				
				st.setInt(1, cod);
				st.setInt(2, nnf);
				rs = st.executeQuery();
				
				while (rs.next()) {
					Fornecedor objFor = instantiateFornecedor(rs);
					TipoConsumo objTp = instantiateTipoFornecedor(rs);
					ParPeriodo objPer = instantiatePeriodo(rs, objFor, objTp);
					Compromisso obj = instantiateCompromisso(rs, objFor, objTp, objPer);
					return(obj);
	 			}
				return null;
			}
			
		catch (SQLException e) {
			throw new DbException (e.getMessage());
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
 	}

	@Override
	public List<Compromisso> findByTipo(int tp) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT *, Fornecedor.Codigo, TipoFornecedor.CodigoTipo, ParPeriodo.IdPeriodo " +
						"FROM compromisso " +
							"INNER JOIN fornecedor " +
 								"ON compromisso.FornecedorIdCom = fornecedor.Codigo " +
 							"INNER JOIN tipoFornecedor " +
 								"ON compromisso.TipoIdCom = tipoFornecedor.CodigoTipo " +
 							"INNER JOIN parPeriodo " +
 								"ON compromisso.PeriodoIdCom = parPeriodo.IdPeriodo " +
						"WHERE TipoIdCom = ? " +	
					"ORDER BY DataCom ");
				
				st.setInt(1, tp);
				rs = st.executeQuery();
				
				List<Compromisso> list = new ArrayList<>();
				Map<Integer, Fornecedor> mapFor = new HashMap<>();
				Map<Integer, TipoConsumo> mapTp = new HashMap<>();
				Map<Integer, ParPeriodo> mapPer = new HashMap<>();
				
				while (rs.next()) {
					Fornecedor objFor = mapFor.get(rs.getInt("FornecedorIdCom"));
					if (objFor == null) {
						objFor = instantiateFornecedor(rs);
						mapFor.put(rs.getInt("FornecedorIdCom"), objFor);
					}	
					TipoConsumo objTp = mapTp.get(rs.getInt("TipoIdCom"));
					if (objTp == null) {
						objTp = instantiateTipoFornecedor(rs);
						mapTp.put(rs.getInt("TipoIdCom"), objTp);
					}	
					ParPeriodo objPer = mapPer.get(rs.getInt("PeriodoIdCom"));
					if (objPer == null) {
						objPer = instantiatePeriodo(rs, objFor, objTp);
						mapPer.put(rs.getInt("PeriodoIdCom"), objPer);
					}	
					Compromisso obj = instantiateCompromisso(rs, objFor, objTp, objPer);
					list.add(obj);
	 			}
				return list;
			}
			catch (SQLException e) {
				throw new DbException (e.getMessage());
			}
			finally {
				DB.closeResultSet(rs);
				DB.closeStatement(st);
			}
 		}	
 	}
