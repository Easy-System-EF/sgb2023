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
import gui.sgbmodel.dao.FechamentoAnoDao;
import gui.sgbmodel.entities.FechamentoAno;
  
public class FechamentoAnoDaoJDBC implements FechamentoAnoDao {
	
// tb entra construtor p/ conex�o
	private Connection conn;
	
	public FechamentoAnoDaoJDBC (Connection conn) {
		this.conn = conn;
	}

	String classe = "Fechamento Ano JDBC";
	
	@Override
	public void insert(FechamentoAno obj) {
		PreparedStatement st = null;
		ResultSet rs = null;
  		try {
			st = conn.prepareStatement(
					"INSERT INTO FechamentoAno " +
				      "(cartelaFechamentoAno, DataFechamentoAno, SituacaoFechamentoAno," +
				        "ValorCartelaFechamentoAno, ValorProdutoFechamentoAno, " +
				        "ValorComissaoFechamentoAno, ValorResultadoFechamentoAno, " +
				        "ValorAcumuladoFechamentoAno ) " +
  				    "VALUES " +
  				      	"(?, ?, ?, ?, ?, ?, ?, ? )",
 					 Statement.RETURN_GENERATED_KEYS); 
			st.setString(1, obj.getCartelaFechamentoAno());
			st.setString(2, obj.getDataFechamentoAno());
			st.setString(3, obj.getSituacaoFechamentoAno());
 			st.setString(4, obj.getValorCartelaFechamentoAno());
 			st.setString(5, obj.getValorProdutoFechamentoAno());
 			st.setString(6, obj.getValorComissaoFechamentoAno());
 			st.setString(7, obj.getValorResultadoFechamentoAno());
 			st.setString(8, obj.getValorAcumuladoFechamentoAno());
 			
 			int rowsaffectad = st.executeUpdate();
			
			if (rowsaffectad > 0)
			{	rs = st.getGeneratedKeys();
				if (rs.next())
				{	int codigo = rs.getInt(1);
					obj.setNumeroFechamentoAno(codigo);
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
	public List<FechamentoAno> findAll() {
		PreparedStatement st = null; 
		ResultSet rs = null;
		try {
			st = conn.prepareStatement( 
					"SELECT * " +
						"From FechamentoAno " +	
					"ORDER BY NumeroFechamentoAno");
			
			rs = st.executeQuery();
			
			List<FechamentoAno> list = new ArrayList<>();
			
			while (rs.next()) {
				FechamentoAno obj = instantiateFechamentoAno(rs);
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
					"TRUNCATE TABLE sgb.FechamentoAno " );

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
	
	private FechamentoAno instantiateFechamentoAno(ResultSet rs) throws SQLException {
		FechamentoAno fechamento = new FechamentoAno();
		fechamento.setNumeroFechamentoAno(rs.getInt("NumeroFechamentoAno"));
		fechamento.setCartelaFechamentoAno(rs.getString("CartelaFechamentoAno"));
		fechamento.setDataFechamentoAno(rs.getString("DataFechamentoAno"));
		fechamento.setSituacaoFechamentoAno(rs.getString("SituacaoFechamentoAno"));
		fechamento.setValorCartelaFechamentoAno(rs.getString("ValorCartelaFechamentoAno"));
		fechamento.setValorProdutoFechamentoAno(rs.getString("ValorProdutoFechamentoAno"));
		fechamento.setValorComissaoFechamentoAno(rs.getString("ValorComissaoFechamentoAno"));
		fechamento.setValorResultadoFechamentoAno(rs.getString("ValorResultadoFechamentoAno"));
		fechamento.setValorAcumuladoFechamentoAno(rs.getString("ValorAcumuladoFechamentoAno"));
        return fechamento;
	}
}
