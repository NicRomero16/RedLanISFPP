package dao.postgres; 

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.TreeMap;

import dao.TipoPuertoDAO; 
import modelo.TipoPuerto;

public class TipoPuertoPostgresDAO implements TipoPuertoDAO {

    private static final String SQL_SELECT_ALL = "SELECT codigo, descripcion, velocidad FROM tipo_puerto";
    private static final String SQL_SELECT_BY_PK = "SELECT codigo, descripcion, velocidad FROM tipo_puerto WHERE codigo = ?";
    private static final String SQL_INSERT = "INSERT INTO tipo_puerto (codigo, descripcion, velocidad) VALUES (?, ?, ?)";
    private static final String SQL_UPDATE = "UPDATE tipo_puerto SET descripcion = ?, velocidad = ? WHERE codigo = ?";
    private static final String SQL_DELETE = "DELETE FROM tipo_puerto WHERE codigo = ?";
    
    public TipoPuertoPostgresDAO() {}

    @Override
    public void insertar(TipoPuerto tp) throws SQLException { 
        try (Connection conn = ConexionPostgres.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_INSERT)) {
            ps.setString(1, tp.getCodigo());
            ps.setString(2, tp.getDescripcion());
            ps.setInt(3, tp.getVelocidad());
            ps.executeUpdate();
        } catch (SQLException e) {
            if ("23505".equals(e.getSQLState())) {
                throw new SQLException("Ya existe un TipoPuerto con código " + tp.getCodigo(), e);
            }
            throw e;
        }
    }
    
    @Override
    public void actualizar(TipoPuerto tp) throws SQLException { 
        try (Connection conn = ConexionPostgres.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_UPDATE)) {
            ps.setString(1, tp.getDescripcion());
            ps.setInt(2, tp.getVelocidad());
            ps.setString(3, tp.getCodigo());
            if (ps.executeUpdate() == 0) {
                throw new SQLException("El TipoPuerto con código " + tp.getCodigo() + " no existe.");
            }
        }
    }
    
    @Override
    public void borrar(TipoPuerto tp) throws SQLException {
        try (Connection conn = ConexionPostgres.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_DELETE)) {
            ps.setString(1, tp.getCodigo());
            if (ps.executeUpdate() == 0) {
                throw new SQLException("El TipoPuerto con código " + tp.getCodigo() + " no existe.");
            }
        }
    }
    
    @Override
    public TreeMap<String, TipoPuerto> buscarTodos() {
        TreeMap<String, TipoPuerto> map = new TreeMap<>();
        try (Connection conn = ConexionPostgres.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_SELECT_ALL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String codigo = rs.getString("codigo");
                String descripcion = rs.getString("descripcion");
                int velocidad = rs.getInt("velocidad");
                map.put(codigo, new TipoPuerto(codigo, descripcion, velocidad));
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar todos los TipoPuerto: " + e.getMessage());
            e.printStackTrace();
        }
        return map;
    }
}