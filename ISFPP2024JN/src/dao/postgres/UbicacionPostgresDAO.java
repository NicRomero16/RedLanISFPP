package dao.postgres; 

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.TreeMap;

import dao.UbicacionDAO;
import modelo.Ubicacion;

public class UbicacionPostgresDAO implements UbicacionDAO {

    // ------------------------------------
    // 1. Sentencias SQL estáticas (CRUD)
    // ------------------------------------
    private static final String SQL_SELECT_ALL = "SELECT codigo, descripcion FROM ubicacion";
    private static final String SQL_SELECT_BY_PK = "SELECT codigo, descripcion FROM ubicacion WHERE codigo = ?";
    private static final String SQL_INSERT = "INSERT INTO ubicacion (codigo, descripcion) VALUES (?, ?)";
    private static final String SQL_UPDATE = "UPDATE ubicacion SET descripcion = ? WHERE codigo = ?";
    private static final String SQL_DELETE = "DELETE FROM ubicacion WHERE codigo = ?";
    
    public UbicacionPostgresDAO() {}

    // ------------------------------------
    // 2. Método INSERTAR (Crear)
    // ------------------------------------
    @Override
    // NOTA: Lanzamos SQLException si falla la inserción (ej. PK duplicada)
    public void insertar(Ubicacion u) throws SQLException { 
        try (Connection conn = ConexionPostgres.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_INSERT)) {
            ps.setString(1, u.getCodigo());
            ps.setString(2, u.getDescripcion());
            ps.executeUpdate();
        }
    }
    
    // ------------------------------------
    // 3. Método ACTUALIZAR
    // ------------------------------------
    @Override
    // NOTA: Usamos solo el objeto Ubicacion modificado para la actualización.
    public void actualizar(Ubicacion u) throws SQLException { 
        try (Connection conn = ConexionPostgres.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_UPDATE)) {
            ps.setString(1, u.getDescripcion()); // El nuevo valor
            ps.setString(2, u.getCodigo());      // La clave para el WHERE
            
            if (ps.executeUpdate() == 0) {
                 // Error si no se actualizó ninguna fila.
                 throw new SQLException("La ubicación con código " + u.getCodigo() + " no existe.");
            }
        }
    }

    // ------------------------------------
    // 4. Método BORRAR
    // ------------------------------------
    @Override
    public void borrar(Ubicacion u) throws SQLException {
        try (Connection conn = ConexionPostgres.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_DELETE)) {
            ps.setString(1, u.getCodigo());
            if (ps.executeUpdate() == 0) {
                throw new SQLException("La ubicación con código " + u.getCodigo() + " no existe.");
            }
        }
    }

    // ------------------------------------
    // 5. Método BUSCARTODOS (Leer)
    // ------------------------------------
    @Override
    public TreeMap<String, Ubicacion> buscarTodos() {
        TreeMap<String, Ubicacion> map = new TreeMap<>();
        try (Connection conn = ConexionPostgres.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_SELECT_ALL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String codigo = rs.getString("codigo");
                String descripcion = rs.getString("descripcion");
                map.put(codigo, new Ubicacion(codigo, descripcion));
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar todas las Ubicaciones: " + e.getMessage());
            e.printStackTrace();
        }
        return map;
    }
    
    // Si la interfaz lo declara, agrega @Override
    public Ubicacion buscarPorCodigo(String codigo) throws SQLException {
        try (Connection conn = ConexionPostgres.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_SELECT_BY_PK)) {
            ps.setString(1, codigo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Ubicacion(rs.getString("codigo"), rs.getString("descripcion"));
                }
            }
        }
        return null;
    }

}