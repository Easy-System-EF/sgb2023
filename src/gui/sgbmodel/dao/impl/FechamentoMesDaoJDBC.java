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
import gui.sgbmodel.dao.FechamentoMesDao;
import gui.sgbmodel.entities.FechamentoMes;
  
public class FechamentoMesDaoJDBC implements FechamentoMesDao {
	
// tb entra construtor p/ conex�o
	private Connection conn;
	
	public FechamentoMesDaoJDBC (Connection conn) {
		this.conn = conn;
	}

	String classe = "Fechamento Mes JDBC";
	
	@Override
	public void insert(FechamentoMes obj) {
		PreparedStatement st = null;
		ResultSet rs = null;
  		try {
			st = conn.prepareStatement(
					"INSERT INTO FechamentoMes " +
				      "(cartelaFechamentoMes, DataFechamentoMes, SituacaoFechamentoMes," +
				        "ValorCartelaFechamentoMes, ValorProdutoFechamentoMes, " +
				        "ValorComissaoFechamentoMes, ValorResultadoFechamentoMes, " +
				        "ValorAcumuladoFechamentoMes ) " +
  				    "VALUES " +
  				      	"(?, ?, ?, ?, ?, ?, ?, ? )",
 					 Statement.RETURN_GENERATED_KEYS); 
			st.setString(1, obj.getCartelaFechamentoMes());
			st.setString(2, obj.getDataFechamentoMes());
			st.setString(3, obj.getSituacaoFechamentoMes());
 			st.setString(4, obj.getValorCartelaFechamentoMes());
 			st.setString(5, obj.getValorProdutoFechamentoMes());
 			st.setString(6, obj.getValorComissaoFechamentoMes());
 			st.setString(7, obj.getValorResultadoFechamentoMes());
 			st.setString(8, obj.getValorAcumuladoFechamentoMes());
 			
 			int rowsaffectad = st.executeUpdate();
			
			if (rowsaffectad > 0)
			{	rs = st.getGeneratedKeys();
				if (rs.next())
				{	int codigo = rs.getInt(1);
					obj.setNumeroFechamentoMes(codigo);
				}
				else
				{	throw new DbException("Erro!!! sem inclusão" );
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
	public List<FechamentoMes> findAll() {
		PreparedStatement st = null; 
		ResultSet rs = null;
		try {
			st = conn.prepareStatement( 
					"SELECT * " +
						"From FechamentoMes " +	
					"ORDER BY NumeroFechamentoMes");
			
			rs = st.executeQuery();
			
			List<FechamentoMes> list = new ArrayList<>();
			
			while (rs.next()) {
				FechamentoMes obj = instantiateFechamentoMes(rs);
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
					"TRUNCATE TABLE sgb.FechamentoMes " );

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
	
	private FechamentoMes instantiateFechamentoMes(ResultSet rs) throws SQLException {
		FechamentoMes fechamento = new FechamentoMes();
		fechamento.setNumeroFechamentoMes(rs.getInt("NumeroFechamentoMes"));
		fechamento.setCartelaFechamentoMes(rs.getString("CartelaFechamentoMes"));
		fechamento.setDataFechamentoMes(rs.getString("DataFechamentoMes"));
		fechamento.setSituacaoFechamentoMes(rs.getString("SituacaoFechamentoMes"));
		fechamento.setValorCartelaFechamentoMes(rs.getString("ValorCartelaFechamentoMes"));
		fechamento.setValorProdutoFechamentoMes(rs.getString("ValorProdutoFechamentoMes"));
		fechamento.setValorComissaoFechamentoMes(rs.getString("ValorComissaoFechamentoMes"));
		fechamento.setValorResultadoFechamentoMes(rs.getString("ValorResultadoFechamentoMes"));
		fechamento.setValorAcumuladoFechamentoMes(rs.getString("ValorAcumuladoFechamentoMes"));
        return fechamento;
	}
}
