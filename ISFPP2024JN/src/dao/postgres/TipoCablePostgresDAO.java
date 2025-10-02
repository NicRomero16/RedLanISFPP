package dao.postgres;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.TreeMap;

import dao.TipoCableDAO;
import modelo.TipoCable;

public class TipoCablePostgresDAO implements TipoCableDAO {

    private static final String SQL_SELECT_ALL = "SELECT codigo, descripcion, velocidad FROM tipo_cable";
    private static final String SQL_INSERT     = "INSERT INTO tipo_cable (codigo, descripcion, velocidad) VALUES (?, ?, ?)";
    private static final String SQL_UPDATE     = "UPDATE tipo_cable SET descripcion = ?, velocidad = ? WHERE codigo = ?";
    private static final String SQL_DELETE     = "DELETE FROM tipo_cable WHERE codigo = ?";

    @Override
    public void insertar(TipoCable tc) throws SQLException{
        try (Connection conn = ConexionPostgres.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_INSERT)) {
            ps.setString(1, tc.getCodigo());
            ps.setString(2, tc.getDescripcion());
            ps.setInt(3, tc.getVelocidad());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al insertar TipoCable " + tc.getCodigo(), e);
        }
    }

    @Override
    public void actualizar(TipoCable tc) throws SQLException{
        try (Connection conn = ConexionPostgres.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_UPDATE)) {
            ps.setString(1, tc.getDescripcion());
            ps.setInt(2, tc.getVelocidad());
            ps.setString(3, tc.getCodigo());
            int updated = ps.executeUpdate();
            if (updated == 0) {
                throw new RuntimeException("TipoCable " + tc.getCodigo() + " no existe.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar TipoCable " + tc.getCodigo(), e);
        }
    }

    @Override
    public void borrar(TipoCable tc) throws SQLException{
        try (Connection conn = ConexionPostgres.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_DELETE)) {
            ps.setString(1, tc.getCodigo());
            int deleted = ps.executeUpdate();
            if (deleted == 0) {
                throw new RuntimeException("TipoCable " + tc.getCodigo() + " no existe.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al borrar TipoCable " + tc.getCodigo(), e);
        }
    }

    @Override
    public TreeMap<String, TipoCable> buscarTodos() {
        TreeMap<String, TipoCable> map = new TreeMap<>();
        try (Connection conn = ConexionPostgres.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_SELECT_ALL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String codigo = rs.getString("codigo");
                String descripcion = rs.getString("descripcion");
                int velocidad = rs.getInt("velocidad");
                map.put(codigo, new TipoCable(codigo, descripcion, velocidad));
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar todos los TipoCable: " + e.getMessage());
            e.printStackTrace();
        }
        return map;
    }
}
