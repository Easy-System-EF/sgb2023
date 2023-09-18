package gui.sgcpmodel.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import gui.sgcpmodel.dao.FornecedorDao;
import gui.sgcpmodel.entites.Fornecedor;
 
public class FornecedorDaoJDBC implements FornecedorDao {
	
// tb entra construtor p/ conex�o
	private Connection conn;
	
	public FornecedorDaoJDBC (Connection conn) {
		this.conn = conn;
	}

	String classe = "Fornecedor JDBC ";

	@Override
	public void insert(Fornecedor obj) {
		PreparedStatement st = null;
		ResultSet rs = null;
  		try {
			st = conn.prepareStatement(
					"INSERT INTO fornecedor " +
				      "(RazaoSocial, Rua, Numero, Complemento, Bairro, Cidade, UF, Cep, "
				      + "Ddd01, Telefone01, Ddd02, Telefone02, Contato, DddContato, "
				      + "TelefoneContato, Email, Pix, Observacao, Prazo, Parcela )" 
  				      + "VALUES " +
				      "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )",
 					 Statement.RETURN_GENERATED_KEYS); 
 
 			st.setString(1, obj.getRazaoSocial());
 			st.setString(2, obj.getRua());
   			st.setInt(3, obj.getNumero());
 			st.setString(4, obj.getComplemento());
 			st.setString(5, obj.getBairro());
 			st.setString(6, obj.getCidade());
 			st.setString(7, obj.getUf());
  			st.setString(8,  (obj).getCep());
 			st.setInt(9, obj.getDdd01());
  			st.setInt(10, obj.getTelefone01());
 			st.setInt(11, obj.getDdd02());
  			st.setInt(12, obj.getTelefone02());
 			st.setString(13, obj.getContato());
 			st.setInt(14, obj.getDddContato());
  			st.setInt(15, obj.getTelefoneContato());
 			st.setString(16, obj.getEmail());
 			st.setString(17, obj.getPix());
  			st.setString(18, obj.getObservacao());
  			st.setInt(19, obj.getPrazo());
  			st.setInt(20, obj.getParcela());
  			
 			int rowsaffectad = st.executeUpdate();
			
			if (rowsaffectad > 0)
			{	rs = st.getGeneratedKeys();
				if (rs.next())
				{	int codigo = rs.getInt(1);
					obj.setCodigo(codigo);
//					System.out.println("Novo inserido: " + obj.getCodigo());
				}
				else
				{	throw new DbException("Erro!!! sem inclusão " + classe );
				}
			}	
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
	public void insertBackUp(Fornecedor obj) {
		PreparedStatement st = null;
		ResultSet rs = null;
  		try {
			st = conn.prepareStatement(
					"INSERT INTO fornecedor " +
				      "(Codigo, RazaoSocial, Rua, Numero, Complemento, Bairro, Cidade, UF, Cep, "
				      + "Ddd01, Telefone01, Ddd02, Telefone02, Contato, DddContato, "
				      + "TelefoneContato, Email, Pix, Observacao, Prazo, Parcela )" 
  				      + "VALUES " +
				      "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )",
 					 Statement.RETURN_GENERATED_KEYS); 
 
   			st.setInt(1, obj.getCodigo());
 			st.setString(2, obj.getRazaoSocial());
 			st.setString(3, obj.getRua());
   			st.setInt(4, obj.getNumero());
 			st.setString(5, obj.getComplemento());
 			st.setString(6, obj.getBairro());
 			st.setString(7, obj.getCidade());
 			st.setString(8, obj.getUf());
  			st.setString(9,  (obj).getCep());
 			st.setInt(10, obj.getDdd01());
  			st.setInt(11, obj.getTelefone01());
 			st.setInt(12, obj.getDdd02());
  			st.setInt(13, obj.getTelefone02());
 			st.setString(14, obj.getContato());
 			st.setInt(15, obj.getDddContato());
  			st.setInt(16, obj.getTelefoneContato());
 			st.setString(17, obj.getEmail());
 			st.setString(18, obj.getPix());
  			st.setString(19, obj.getObservacao());
  			st.setInt(20, obj.getPrazo());
  			st.setInt(21, obj.getParcela());
  			
 			st.executeUpdate();
			
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
	public void update(Fornecedor obj) {
		PreparedStatement st = null;
  		try {
			st = conn.prepareStatement(
					"UPDATE fornecedor " +  
							"SET RazaoSocial = ?, "
							+ "Rua = ?, "
							+ "Numero = ?, "
							+ "Complemento = ?, "
							+ "Bairro = ?, "
							+ "Cidade = ?, "
							+ "UF = ?, "
							+ "Cep = ?, "
							+ "Ddd01 = ?, "
							+ "Telefone01 = ?, "
							+ "Ddd02 = ?, "
							+ "Telefone02 = ?, "
							+ "Contato = ?, "
							+ "DddContato = ?, "
							+ "TelefoneContato = ?, "
							+ "Email = ?, "
							+ "Pix = ?, "
							+ "Observacao = ?, " 
							+ "Prazo = ?, "
							+ "Parcela = ? " +
 					"WHERE (Codigo = ?)",
        			 Statement.RETURN_GENERATED_KEYS);
			
			st.setString(1, obj.getRazaoSocial());
			st.setString(2,  obj.getRua());
			st.setInt(3, obj.getNumero());
			st.setString(4,  obj.getComplemento());
			st.setString(5,  obj.getBairro());
			st.setString(6,  obj.getCidade());
			st.setString(7,  obj.getUf());
			st.setString(8, obj.getCep());
			st.setInt(9, obj.getDdd01());
			st.setInt(10, obj.getTelefone01());
			st.setInt(11, obj.getDdd02());
			st.setInt(12, obj.getTelefone02());
			st.setString(13,  obj.getContato());
			st.setInt(14, obj.getDddContato());
			st.setInt(15, obj.getTelefoneContato());
			st.setString(16,  obj.getEmail());
			st.setString(17,  obj.getPix());
			st.setString(18, obj.getObservacao());
  			st.setInt(19, obj.getPrazo());
  			st.setInt(20, obj.getParcela());
			st.setInt(21, obj.getCodigo());
    			
			st.executeUpdate();
   		} 
 		catch (SQLException e) {
 				throw new DbException ( "Erro!!! sem atualização " + classe + " " + e.getMessage()); }
 		finally {
 			DB.closeStatement(st);
		}
	}

	@Override
	public void deleteById(int codigo) {
		PreparedStatement st = null;
   		try {
			st = conn.prepareStatement(
					"DELETE FROM fornecedor WHERE Codigo = ?", Statement.RETURN_GENERATED_KEYS);
				  
			st.setInt(1, codigo);
			st.executeUpdate();
   		}
 		catch (SQLException e) {
			throw new DbException ( "Erro!!! não excluído " + classe + " " + e.getMessage()); }
 		finally {
 			DB.closeStatement(st);
		}
	}

	@Override
	public Fornecedor findById(int codigo) {
 		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
				 "SELECT * FROM fornecedor " +
 				 "WHERE Codigo = ?");
 			
			st.setInt(1, codigo);
			rs = st.executeQuery();
			if (rs.next())
			{	Fornecedor obj = instantiateFornecedores(rs);
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
	public List<Fornecedor> findAll() {
		PreparedStatement st = null; 
		ResultSet rs = null;
		try {
			st = conn.prepareStatement( 
					"SELECT * FROM fornecedor " +
					"ORDER BY RazaoSocial");
			
			rs = st.executeQuery();
			
			List<Fornecedor> list = new ArrayList<>();
			
			while (rs.next())
			{	Fornecedor obj = instantiateFornecedores(rs);
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
	public List<Fornecedor> findPesquisa(String str) {
		PreparedStatement st = null; 
		ResultSet rs = null;
		try {
			st = conn.prepareStatement( 
					"SELECT * FROM fornecedor " +
						"WHERE RazaoSocial like ? " +	
					"ORDER BY RazaoSocial ");
			
			st.setString(1, str + "%");
			rs = st.executeQuery();
			
			List<Fornecedor> list = new ArrayList<>();
			
			while (rs.next())
			{	Fornecedor obj = instantiateFornecedores(rs);
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
	
	private Fornecedor instantiateFornecedores(ResultSet rs) throws SQLException {
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
 		forn.setObservacao(rs.getString("Observacao"));
 		forn.setPrazo(rs.getInt("Prazo"));
 		forn.setParcela(rs.getInt("Parcela"));
        return forn;
	}
   }
