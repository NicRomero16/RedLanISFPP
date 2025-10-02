package dao.postgres;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import dao.EquipoDAO;
import modelo.Equipo;
import modelo.TipoEquipo;
import modelo.TipoPuerto;
import modelo.Ubicacion;

public class EquipoPostgresDAO implements EquipoDAO {

    // Equipo
    private static final String SQL_INSERT_EQUIPO = 
        "INSERT INTO equipo (codigo, descripcion, marca, modelo, estado, tipo_equipo_fk, ubicacion_fk) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE_EQUIPO = 
        "UPDATE equipo SET descripcion = ?, marca = ?, modelo = ?, estado = ?, tipo_equipo_fk = ?, ubicacion_fk = ? WHERE codigo = ?";
    private static final String SQL_DELETE_EQUIPO = 
        "DELETE FROM equipo WHERE codigo = ?";
    private static final String SQL_SELECT_ALL_EQUIPO = 
        "SELECT codigo, descripcion, marca, modelo, estado, tipo_equipo_fk, ubicacion_fk FROM equipo";

    // Puertos del equipo (tabla: puerto)
    private static final String SQL_INSERT_PUERTO =
        "INSERT INTO puerto (equipo_fk, tipo_puerto_fk, cantidad) VALUES (?, ?, ?)";
    private static final String SQL_DELETE_PUERTOS =
        "DELETE FROM puerto WHERE equipo_fk = ?";
    private static final String SQL_SELECT_PUERTOS_FOR_EQUIPO =
        "SELECT p.tipo_puerto_fk, tp.descripcion, tp.velocidad, p.cantidad " +
        "FROM puerto p JOIN tipo_puerto tp ON tp.codigo = p.tipo_puerto_fk " +
        "WHERE p.equipo_fk = ?";

    // IPs del equipo (tabla: direccion_ip)
    private static final String SQL_INSERT_IP =
        "INSERT INTO direccion_ip (ip_address, equipo_fk) VALUES (?, ?)";
    private static final String SQL_DELETE_IPS =
        "DELETE FROM direccion_ip WHERE equipo_fk = ?";
    private static final String SQL_SELECT_IPS_FOR_EQUIPO =
        "SELECT ip_address FROM direccion_ip WHERE equipo_fk = ?";

    // Aux: TipoEquipo y Ubicacion
    private static final String SQL_SEL_TIPO_EQUIPO = 
        "SELECT codigo, descripcion FROM tipo_equipo WHERE codigo = ?";
    private static final String SQL_SEL_UBICACION = 
        "SELECT codigo, descripcion FROM ubicacion WHERE codigo = ?";

    @Override
    public void insertar(Equipo e) {
        // Validaciones para cumplir NOT NULL en FKs y estado
        if (e.getTipoEquipo() == null) throw new RuntimeException("TipoEquipo es obligatorio.");
        if (e.getUbicacion() == null) throw new RuntimeException("Ubicacion es obligatoria.");

        try (Connection conn = ConexionPostgres.getConnection()) {
            conn.setAutoCommit(false);
            try {
                // equipo
                try (PreparedStatement ps = conn.prepareStatement(SQL_INSERT_EQUIPO)) {
                    ps.setString(1, e.getCodigo());
                    ps.setString(2, e.getDescripcion());
                    ps.setString(3, e.getMarca());
                    ps.setString(4, e.getModelo());
                    ps.setBoolean(5, e.getEstado()); // boolean primitivo
                    ps.setString(6, e.getTipoEquipo().getCodigo()); // NOT NULL
                    ps.setString(7, e.getUbicacion().getCodigo());  // NOT NULL
                    ps.executeUpdate();
                }

                // puertos (si tu modelo soporta 1 tipo + cantidad, usa índice 0)
                if (e.getCantidadPuertos() > 0) {
                    String tipoPuertoCodigo = e.obtenerCodigoTipoPuerto(0);
                    int cantidad = e.getCantidadPuertos();
                    try (PreparedStatement ps = conn.prepareStatement(SQL_INSERT_PUERTO)) {
                        ps.setString(1, e.getCodigo());
                        ps.setString(2, tipoPuertoCodigo);
                        ps.setInt(3, cantidad);
                        ps.executeUpdate();
                    }
                }

                // IPs
                if (e.getDireccionesIP() != null && !e.getDireccionesIP().isEmpty()) {
                    try (PreparedStatement ps = conn.prepareStatement(SQL_INSERT_IP)) {
                        for (String ip : e.getDireccionesIP()) {
                            if (ip == null || ip.isBlank()) continue;
                            ps.setString(1, ip);
                            ps.setString(2, e.getCodigo());
                            ps.addBatch();
                        }
                        ps.executeBatch();
                    }
                }

                conn.commit();
            } catch (SQLException ex) {
                conn.rollback();
                // 23505 = unique_violation
                if ("23505".equals(ex.getSQLState())) {
                    throw new RuntimeException("Equipo ya existe: " + e.getCodigo(), ex);
                }
                throw new RuntimeException("Error al insertar equipo " + e.getCodigo(), ex);
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error de conexión al insertar equipo " + e.getCodigo(), ex);
        }
    }

    @Override
    public void actualizar(Equipo original, Equipo modificado) {
        if (modificado.getTipoEquipo() == null) throw new RuntimeException("TipoEquipo es obligatorio.");
        if (modificado.getUbicacion() == null) throw new RuntimeException("Ubicacion es obligatoria.");
        String codigo = original.getCodigo();
        try (Connection conn = ConexionPostgres.getConnection()) {
            conn.setAutoCommit(false);
            try {
                // actualizar equipo base
                try (PreparedStatement ps = conn.prepareStatement(SQL_UPDATE_EQUIPO)) {
                    ps.setString(1, modificado.getDescripcion());
                    ps.setString(2, modificado.getMarca());
                    ps.setString(3, modificado.getModelo());
                    ps.setBoolean(4, modificado.getEstado());
                    ps.setString(5, modificado.getTipoEquipo().getCodigo());
                    ps.setString(6, modificado.getUbicacion().getCodigo());
                    ps.setString(7, codigo);
                    int upd = ps.executeUpdate();
                    if (upd == 0) throw new RuntimeException("Equipo no existe: " + codigo);
                }

                // reemplazar puertos
                try (PreparedStatement del = conn.prepareStatement(SQL_DELETE_PUERTOS)) {
                    del.setString(1, codigo);
                    del.executeUpdate();
                }
                if (modificado.getCantidadPuertos() > 0) {
                    String tipoPuertoCodigo = modificado.obtenerCodigoTipoPuerto(0);
                    int cantidad = modificado.getCantidadPuertos();
                    try (PreparedStatement ins = conn.prepareStatement(SQL_INSERT_PUERTO)) {
                        ins.setString(1, codigo);
                        ins.setString(2, tipoPuertoCodigo);
                        ins.setInt(3, cantidad);
                        ins.executeUpdate();
                    }
                }

                // reemplazar IPs
                try (PreparedStatement del = conn.prepareStatement(SQL_DELETE_IPS)) {
                    del.setString(1, codigo);
                    del.executeUpdate();
                }
                if (modificado.getDireccionesIP() != null && !modificado.getDireccionesIP().isEmpty()) {
                    try (PreparedStatement ins = conn.prepareStatement(SQL_INSERT_IP)) {
                        for (String ip : modificado.getDireccionesIP()) {
                            if (ip == null || ip.isBlank()) continue;
                            ins.setString(1, ip);
                            ins.setString(2, codigo);
                            ins.addBatch();
                        }
                        ins.executeBatch();
                    }
                }

                conn.commit();
            } catch (SQLException ex) {
                conn.rollback();
                throw new RuntimeException("Error al actualizar equipo " + codigo, ex);
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error de conexión al actualizar equipo " + codigo, ex);
        }
    }

    @Override
    public void borrar(Equipo e) {
        String codigo = e.getCodigo();
        try (Connection conn = ConexionPostgres.getConnection()) {
            conn.setAutoCommit(false);
            try {
                // primero dependencias
                try (PreparedStatement ps = conn.prepareStatement(SQL_DELETE_IPS)) {
                    ps.setString(1, codigo);
                    ps.executeUpdate();
                }
                try (PreparedStatement ps = conn.prepareStatement(SQL_DELETE_PUERTOS)) {
                    ps.setString(1, codigo);
                    ps.executeUpdate();
                }
                // finalmente equipo
                try (PreparedStatement ps = conn.prepareStatement(SQL_DELETE_EQUIPO)) {
                    ps.setString(1, codigo);
                    int del = ps.executeUpdate();
                    if (del == 0) throw new RuntimeException("Equipo no existe: " + codigo);
                }
                conn.commit();
            } catch (SQLException ex) {
                conn.rollback();
                throw new RuntimeException("Error al borrar equipo " + codigo, ex);
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error de conexión al borrar equipo " + codigo, ex);
        }
    }

    @Override
    public TreeMap<String, Equipo> buscarTodos() {
        TreeMap<String, Equipo> map = new TreeMap<>();
        try (Connection conn = ConexionPostgres.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_SELECT_ALL_EQUIPO);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Equipo equipo = mapRowToEquipo(rs, conn);
                map.put(equipo.getCodigo(), equipo);
            }
        } catch (SQLException ex) {
            System.err.println("Error al buscar equipos: " + ex.getMessage());
            ex.printStackTrace();
        }
        return map;
    }

    // Helpers

    private Equipo mapRowToEquipo(ResultSet rs, Connection conn) throws SQLException {
        String codigo = rs.getString("codigo");
        String descripcion = rs.getString("descripcion");
        String marca = rs.getString("marca");
        String modelo = rs.getString("modelo");
        boolean estado = rs.getBoolean("estado");
        String tipoEquipoCod = rs.getString("tipo_equipo_fk");
        String ubicacionCod = rs.getString("ubicacion_fk");

        TipoEquipo te = fetchTipoEquipo(conn, tipoEquipoCod);
        Ubicacion ub = fetchUbicacion(conn, ubicacionCod);

        Equipo e = new Equipo(codigo, descripcion, marca, modelo, te, ub, estado);

        // puertos
        try (PreparedStatement ps = conn.prepareStatement(SQL_SELECT_PUERTOS_FOR_EQUIPO)) {
            ps.setString(1, codigo);
            try (ResultSet rpu = ps.executeQuery()) {
                while (rpu.next()) {
                    String tpCod = rpu.getString("tipo_puerto_fk"); // Debe existir en el SELECT
                    String tpDesc = rpu.getString("descripcion");
                    int velocidad = rpu.getInt("velocidad");
                    int cantidad = rpu.getInt("cantidad");
                    TipoPuerto tp = new TipoPuerto(tpCod, tpDesc, velocidad);
                    e.agregarPuerto(cantidad, tp);
                }
            }
        }

        // IPs
        List<String> ips = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(SQL_SELECT_IPS_FOR_EQUIPO)) {
            ps.setString(1, codigo);
            try (ResultSet rip = ps.executeQuery()) {
                while (rip.next()) {
                    ips.add(rip.getString("ip_address"));
                }
            }
        }
        e.getDireccionesIP().addAll(ips);

        return e;
    }

    private TipoEquipo fetchTipoEquipo(Connection conn, String codigo) throws SQLException {
        if (codigo == null) return null;
        try (PreparedStatement ps = conn.prepareStatement(SQL_SEL_TIPO_EQUIPO)) {
            ps.setString(1, codigo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new TipoEquipo(rs.getString("codigo"), rs.getString("descripcion"));
                }
            }
        }
        return null;
    }

    private Ubicacion fetchUbicacion(Connection conn, String codigo) throws SQLException {
        if (codigo == null) return null;
        try (PreparedStatement ps = conn.prepareStatement(SQL_SEL_UBICACION)) {
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