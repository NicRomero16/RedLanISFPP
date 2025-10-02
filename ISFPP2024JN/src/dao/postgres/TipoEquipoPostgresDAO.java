package dao.postgres; 

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.TreeMap;

import dao.TipoEquipoDAO; 
import modelo.TipoEquipo;

public class TipoEquipoPostgresDAO implements TipoEquipoDAO {

    // ------------------------------------
    // 1. Sentencias SQL estáticas (CRUD)
    // ------------------------------------
    private static final String SQL_SELECT_ALL = "SELECT codigo, descripcion FROM tipo_equipo";
    private static final String SQL_SELECT_BY_PK = "SELECT codigo, descripcion FROM tipo_equipo WHERE codigo = ?";
    private static final String SQL_INSERT = "INSERT INTO tipo_equipo (codigo, descripcion) VALUES (?, ?)";
    private static final String SQL_UPDATE = "UPDATE tipo_equipo SET descripcion = ? WHERE codigo = ?";
    private static final String SQL_DELETE = "DELETE FROM tipo_equipo WHERE codigo = ?";
    
    // El constructor ya no necesita cargar archivos, se conecta al usar los métodos.
    public TipoEquipoPostgresDAO() {}

    // ------------------------------------
    // 2. Método INSERTAR (Crear)
    // ------------------------------------
    @Override
    // NOTA: Reemplazamos ArchivoExistenteException por SQLException o una excepción personalizada de DB
    public void insertar(TipoEquipo te) throws SQLException { 
        try (Connection conn = ConexionPostgres.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_INSERT)) {
            ps.setString(1, te.getCodigo());
            ps.setString(2, te.getDescripcion());
            try {
                ps.executeUpdate();
            } catch (SQLException e) {
                // 23505 = unique_violation en PostgreSQL
                if ("23505".equals(e.getSQLState())) {
                    throw new SQLException("Ya existe un TipoEquipo con código " + te.getCodigo(), e);
                }
                throw e;
            }
        }
    }
    
    // ------------------------------------
    // 3. Método ACTUALIZAR
    // ------------------------------------
    @Override
    // NOTA: Ya no necesitamos pasar el objeto viejo, solo el modificado
    public void actualizar(TipoEquipo te) throws SQLException { 
        try (Connection conn = ConexionPostgres.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_UPDATE)) {
            ps.setString(1, te.getDescripcion()); // El nuevo valor
            ps.setString(2, te.getCodigo());      // La clave para el WHERE
            if (ps.executeUpdate() == 0) {
                throw new SQLException("El tipo de equipo con código " + te.getCodigo() + " no existe.");
            }
        }
    }

    // ------------------------------------
    // 4. Método BORRAR
    // ------------------------------------
    @Override
    public void borrar(TipoEquipo te) throws SQLException {
        try (Connection conn = ConexionPostgres.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_DELETE)) {
            ps.setString(1, te.getCodigo());
            if (ps.executeUpdate() == 0) {
                throw new SQLException("El tipo de equipo con código " + te.getCodigo() + " no existe.");
            }
        }
    }

    // ------------------------------------
    // 5. Método BUSCARTODOS (Leer)
    // ------------------------------------
    @Override
    public TreeMap<String, TipoEquipo> buscarTodos() {
        TreeMap<String, TipoEquipo> map = new TreeMap<>();
        try (Connection conn = ConexionPostgres.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_SELECT_ALL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String codigo = rs.getString("codigo");
                String descripcion = rs.getString("descripcion");
                map.put(codigo, new TipoEquipo(codigo, descripcion));
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar todos los TipoEquipo: " + e.getMessage());
            e.printStackTrace();
        }
        return map;
    }
    
    // Agrega @Override si está declarado en la interfaz
    public TipoEquipo buscarPorCodigo(String codigo) throws SQLException {
        try (Connection conn = ConexionPostgres.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_SELECT_BY_PK)) {
            ps.setString(1, codigo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new TipoEquipo(rs.getString("codigo"), rs.getString("descripcion"));
                }
            }
        }
        return null;
    }
}