package dao.postgres;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import dao.ConexionDAO;
import modelo.Conexion;
import modelo.Equipo;
import modelo.TipoCable;
import modelo.TipoPuerto;
import modelo.TipoEquipo;
import modelo.Ubicacion;

public class ConexionPostgresDAO implements ConexionDAO {

    private static final String SQL_INSERT =
        "INSERT INTO conexion (equipo1_fk, tipopuerto1_fk, equipo2_fk, tipopuerto2_fk, tipocable_fk) VALUES (?, ?, ?, ?, ?)";

    private static final String SQL_DELETE =
        "DELETE FROM conexion WHERE equipo1_fk = ? AND tipopuerto1_fk = ? AND equipo2_fk = ? AND tipopuerto2_fk = ? AND tipocable_fk = ?";

    private static final String SQL_SELECT_ALL =
        "SELECT " +
        "  e1.codigo AS e1_cod, e1.descripcion AS e1_desc, e1.marca AS e1_marca, e1.modelo AS e1_modelo, e1.estado AS e1_estado, " +
        "  te1.codigo AS te1_cod, te1.descripcion AS te1_desc, u1.codigo AS u1_cod, u1.descripcion AS u1_desc, " +
        "  e2.codigo AS e2_cod, e2.descripcion AS e2_desc, e2.marca AS e2_marca, e2.modelo AS e2_modelo, e2.estado AS e2_estado, " +
        "  te2.codigo AS te2_cod, te2.descripcion AS te2_desc, u2.codigo AS u2_cod, u2.descripcion AS u2_desc, " +
        "  c.tipopuerto1_fk, tp1.descripcion AS tp1_desc, tp1.velocidad AS tp1_vel, " +
        "  c.tipopuerto2_fk, tp2.descripcion AS tp2_desc, tp2.velocidad AS tp2_vel, " +
        "  c.tipocable_fk,  tc.descripcion AS tc_desc,  tc.velocidad  AS tc_vel " +
        "FROM conexion c " +
        "JOIN equipo e1 ON e1.codigo = c.equipo1_fk " +
        "JOIN tipo_equipo te1 ON te1.codigo = e1.tipo_equipo_fk " +
        "JOIN ubicacion  u1  ON u1.codigo  = e1.ubicacion_fk " +
        "JOIN equipo e2 ON e2.codigo = c.equipo2_fk " +
        "JOIN tipo_equipo te2 ON te2.codigo = e2.tipo_equipo_fk " +
        "JOIN ubicacion  u2  ON u2.codigo  = e2.ubicacion_fk " +
        "JOIN tipo_puerto tp1 ON tp1.codigo = c.tipopuerto1_fk " +
        "JOIN tipo_puerto tp2 ON tp2.codigo = c.tipopuerto2_fk " +
        "JOIN tipo_cable  tc  ON tc.codigo  = c.tipocable_fk";

    // Para enriquecer equipos con puertos e IPs como en EquipoPostgresDAO
    private static final String SQL_SELECT_PUERTOS_FOR_EQUIPO =
        "SELECT p.tipo_puerto_fk, tp.descripcion, tp.velocidad, p.cantidad " +
        "FROM puerto p JOIN tipo_puerto tp ON tp.codigo = p.tipo_puerto_fk " +
        "WHERE p.equipo_fk = ?";
    private static final String SQL_SELECT_IPS_FOR_EQUIPO =
        "SELECT ip_address FROM direccion_ip WHERE equipo_fk = ?";

    @Override
    public void insertar(Conexion conexion) throws SQLException {
        try (Connection conn = ConexionPostgres.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_INSERT)) {

            ps.setString(1, conexion.getEquipo1().getCodigo());
            ps.setString(2, conexion.getTipoPuerto1().getCodigo());
            ps.setString(3, conexion.getEquipo2().getCodigo());
            ps.setString(4, conexion.getTipoPuerto2().getCodigo());
            ps.setString(5, conexion.getTipoCable().getCodigo());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al insertar conexión", e);
        }
    }

    // Nota: sin id en el modelo, hacemos delete+insert de la misma conexión
    @Override
    public void actualizar(Conexion conexion) throws SQLException {
        try (Connection conn = ConexionPostgres.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement del = conn.prepareStatement(SQL_DELETE);
                 PreparedStatement ins = conn.prepareStatement(SQL_INSERT)) {

                // delete
                del.setString(1, conexion.getEquipo1().getCodigo());
                del.setString(2, conexion.getTipoPuerto1().getCodigo());
                del.setString(3, conexion.getEquipo2().getCodigo());
                del.setString(4, conexion.getTipoPuerto2().getCodigo());
                del.setString(5, conexion.getTipoCable().getCodigo());
                del.executeUpdate();

                // insert (mismos valores o los ya modificados en el objeto)
                ins.setString(1, conexion.getEquipo1().getCodigo());
                ins.setString(2, conexion.getTipoPuerto1().getCodigo());
                ins.setString(3, conexion.getEquipo2().getCodigo());
                ins.setString(4, conexion.getTipoPuerto2().getCodigo());
                ins.setString(5, conexion.getTipoCable().getCodigo());
                ins.executeUpdate();

                conn.commit();
            } catch (SQLException ex) {
                conn.rollback();
                throw ex;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar conexión", e);
        }
    }

    @Override
    public void borrar(Conexion conexion) throws SQLException {
        try (Connection conn = ConexionPostgres.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_DELETE)) {

            ps.setString(1, conexion.getEquipo1().getCodigo());
            ps.setString(2, conexion.getTipoPuerto1().getCodigo());
            ps.setString(3, conexion.getEquipo2().getCodigo());
            ps.setString(4, conexion.getTipoPuerto2().getCodigo());
            ps.setString(5, conexion.getTipoCable().getCodigo());
            int del = ps.executeUpdate();
            if (del == 0) {
                throw new RuntimeException("La conexión no existe.");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al borrar conexión", e);
        }
    }

    @Override
    public List<Conexion> buscarTodos() {
        List<Conexion> list = new ArrayList<>();
        try (Connection conn = ConexionPostgres.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_SELECT_ALL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                // TipoEquipo/Ubicacion
                TipoEquipo te1 = new TipoEquipo(rs.getString("te1_cod"), rs.getString("te1_desc"));
                Ubicacion  ub1 = new Ubicacion (rs.getString("u1_cod"),  rs.getString("u1_desc"));
                TipoEquipo te2 = new TipoEquipo(rs.getString("te2_cod"), rs.getString("te2_desc"));
                Ubicacion  ub2 = new Ubicacion (rs.getString("u2_cod"),  rs.getString("u2_desc"));

                // Equipos con datos básicos
                Equipo e1 = new Equipo(rs.getString("e1_cod"), rs.getString("e1_desc"),
                                       rs.getString("e1_marca"), rs.getString("e1_modelo"),
                                       te1, ub1, rs.getBoolean("e1_estado"));
                Equipo e2 = new Equipo(rs.getString("e2_cod"), rs.getString("e2_desc"),
                                       rs.getString("e2_marca"), rs.getString("e2_modelo"),
                                       te2, ub2, rs.getBoolean("e2_estado"));

                // Enriquecer con puertos e IPs
                fillPuertosEIPs(conn, e1);
                fillPuertosEIPs(conn, e2);

                TipoPuerto tp1 = new TipoPuerto(
                    rs.getString("tipopuerto1_fk"),
                    rs.getString("tp1_desc"),
                    rs.getInt("tp1_vel")
                );
                TipoPuerto tp2 = new TipoPuerto(
                    rs.getString("tipopuerto2_fk"),
                    rs.getString("tp2_desc"),
                    rs.getInt("tp2_vel")
                );
                TipoCable tc = new TipoCable(
                    rs.getString("tipocable_fk"),
                    rs.getString("tc_desc"),
                    rs.getInt("tc_vel")
                );

                Conexion c = new Conexion(e1, tp1, e2, tp2, tc);
                list.add(c);
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar conexiones: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    // Helpers
    private void fillPuertosEIPs(Connection conn, Equipo e) throws SQLException {
        // Puertos
        try (PreparedStatement ps = conn.prepareStatement(SQL_SELECT_PUERTOS_FOR_EQUIPO)) {
            ps.setString(1, e.getCodigo());
            try (ResultSet rpu = ps.executeQuery()) {
                while (rpu.next()) {
                    String tpCod = rpu.getString("tipo_puerto_fk");
                    String tpDesc = rpu.getString("descripcion");
                    int velocidad = rpu.getInt("velocidad");
                    int cantidad = rpu.getInt("cantidad");
                    modelo.TipoPuerto tp = new modelo.TipoPuerto(tpCod, tpDesc, velocidad);
                    e.agregarPuerto(cantidad, tp);
                }
            }
        }
        // IPs
        try (PreparedStatement ps = conn.prepareStatement(SQL_SELECT_IPS_FOR_EQUIPO)) {
            ps.setString(1, e.getCodigo());
            try (ResultSet rip = ps.executeQuery()) {
                while (rip.next()) {
                    e.agregarDireccionIP(rip.getString("ip_address"));
                }
            }
        }
    }
}