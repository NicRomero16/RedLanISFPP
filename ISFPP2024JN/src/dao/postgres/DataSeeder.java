package dao.postgres;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataSeeder {

    public static void main(String[] args) {
        try (Connection conn = ConexionPostgres.getConnection()) {
            conn.setAutoCommit(false);
            try {
                cargarTipoEquipo(conn, "tipoEquipo.txt");
                cargarUbicacion(conn, "ubicacion.txt");
                cargarTipoPuerto(conn, "tipoPuerto.txt");
                cargarTipoCable(conn, "tipoCable.txt");
                cargarEquipos(conn, "equipo.txt");
                cargarConexiones(conn, "conexion.txt");
                conn.commit();
                System.out.println("Carga completada.");
            } catch (Exception e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ---- Carga de tablas maestras ----
    private static void cargarTipoEquipo(Connection conn, String file) throws IOException, SQLException {
        String sql = "INSERT INTO tipo_equipo (codigo, descripcion) VALUES (?, ?) ON CONFLICT (codigo) DO NOTHING";
        try (BufferedReader br = new BufferedReader(new FileReader(file));
             PreparedStatement ps = conn.prepareStatement(sql)) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = splitSemicolon(line);
                if (p.length < 2) continue;
                ps.setString(1, p[0].trim());
                ps.setString(2, p[1].trim());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private static void cargarUbicacion(Connection conn, String file) throws IOException, SQLException {
        String sql = "INSERT INTO ubicacion (codigo, descripcion) VALUES (?, ?) ON CONFLICT (codigo) DO NOTHING";
        try (BufferedReader br = new BufferedReader(new FileReader(file));
             PreparedStatement ps = conn.prepareStatement(sql)) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = splitSemicolon(line);
                if (p.length < 2) continue;
                ps.setString(1, p[0].trim());
                ps.setString(2, p[1].trim());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private static void cargarTipoPuerto(Connection conn, String file) throws IOException, SQLException {
        String sql = "INSERT INTO tipo_puerto (codigo, descripcion, velocidad) VALUES (?, ?, ?) ON CONFLICT (codigo) DO NOTHING";
        try (BufferedReader br = new BufferedReader(new FileReader(file));
             PreparedStatement ps = conn.prepareStatement(sql)) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = splitSemicolon(line);
                if (p.length < 3) continue;
                ps.setString(1, p[0].trim());
                ps.setString(2, p[1].trim());
                ps.setInt(3, parseIntSafe(p[2]));
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private static void cargarTipoCable(Connection conn, String file) throws IOException, SQLException {
        String sql = "INSERT INTO tipo_cable (codigo, descripcion, velocidad) VALUES (?, ?, ?) ON CONFLICT (codigo) DO NOTHING";
        try (BufferedReader br = new BufferedReader(new FileReader(file));
             PreparedStatement ps = conn.prepareStatement(sql)) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = splitSemicolon(line);
                if (p.length < 3) continue;
                ps.setString(1, p[0].trim());
                ps.setString(2, p[1].trim());
                ps.setInt(3, parseIntSafe(p[2]));
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    // ---- Carga de equipos + puertos + IPs ----
    private static void cargarEquipos(Connection conn, String file) throws IOException, SQLException {
        String sqlEquipo = "INSERT INTO equipo (codigo, descripcion, marca, modelo, estado, tipo_equipo_fk, ubicacion_fk) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?) ON CONFLICT (codigo) DO NOTHING";
        String sqlPuerto = "INSERT INTO puerto (equipo_fk, tipo_puerto_fk, cantidad) VALUES (?, ?, ?) " +
                "ON CONFLICT (equipo_fk, tipo_puerto_fk) DO NOTHING";
        String sqlIp = "INSERT INTO direccion_ip (equipo_fk, ip_address) VALUES (?, ?) ON CONFLICT DO NOTHING";

        try (BufferedReader br = new BufferedReader(new FileReader(file));
             PreparedStatement psEq = conn.prepareStatement(sqlEquipo);
             PreparedStatement psPu = conn.prepareStatement(sqlPuerto);
             PreparedStatement psIp = conn.prepareStatement(sqlIp)) {

            String line;
            while ((line = br.readLine()) != null) {
                String[] p = splitSemicolon(line);
                // Esperado: 9 campos -> [0..8]
                if (p.length < 9) continue;

                String codigo = p[0].trim();
                String descripcion = p[1].trim();
                String marca = emptyToNull(p[2]);
                String modelo = emptyToNull(p[3]);
                String tipoEq = p[4].trim();
                String ubic = p[5].trim();
                String puertoSpec = p[6].trim();      // ej: "1G,8"
                String ipList = p[7].trim();          // ej: "192.168.16.24,132.255.7.2"
                boolean estado = Boolean.parseBoolean(p[8].trim());

                // equipo
                psEq.setString(1, codigo);
                psEq.setString(2, descripcion);
                psEq.setString(3, marca);
                psEq.setString(4, modelo);
                psEq.setBoolean(5, estado);
                psEq.setString(6, tipoEq);
                psEq.setString(7, ubic);
                psEq.addBatch();

                // puerto (solo un tipo,cantidad por línea)
                if (!puertoSpec.isEmpty() && puertoSpec.contains(",")) {
                    String[] pp = puertoSpec.split(",", -1);
                    String tipoPuerto = pp[0].trim();
                    int cantidad = parseIntSafe(pp[1]);
                    if (!tipoPuerto.isEmpty() && cantidad >= 0) {
                        psPu.setString(1, codigo);
                        psPu.setString(2, tipoPuerto);
                        psPu.setInt(3, cantidad);
                        psPu.addBatch();
                    }
                }

                // IPs
                if (!ipList.isEmpty()) {
                    for (String ip : splitByComma(ipList)) {
                        String ipTrim = ip.trim();
                        if (!ipTrim.isEmpty()) {
                            psIp.setString(1, codigo);
                            psIp.setString(2, ipTrim);
                            psIp.addBatch();
                        }
                    }
                }
            }
            psEq.executeBatch();
            psPu.executeBatch();
            psIp.executeBatch();
        }
    }

    // ---- Carga de conexiones ----
    private static void cargarConexiones(Connection conn, String file) throws IOException, SQLException {
        String sql = "INSERT INTO conexion (equipo1_fk, tipopuerto1_fk, equipo2_fk, tipopuerto2_fk, tipocable_fk) " +
                     "VALUES (?, ?, ?, ?, ?)";
        try (BufferedReader br = new BufferedReader(new FileReader(file));
             PreparedStatement ps = conn.prepareStatement(sql)) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = splitSemicolon(line);
                if (p.length < 5) continue;
                ps.setString(1, p[0].trim()); // equipo1
                ps.setString(2, p[1].trim()); // tp1
                ps.setString(3, p[2].trim()); // equipo2
                ps.setString(4, p[3].trim()); // tp2
                ps.setString(5, p[4].trim()); // cable
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    // ---- Helpers ----
    private static String[] splitSemicolon(String line) {
        if (line == null) return new String[0];
        // recorta posibles ; finales vacíos
        while (line.endsWith(";")) line = line.substring(0, line.length() - 1);
        String[] arr = line.split(";", -1);
        List<String> out = new ArrayList<>();
        for (String a : arr) out.add(a);
        return out.toArray(new String[0]);
    }

    private static String[] splitByComma(String s) {
        return s.split(",", -1);
    }

    private static int parseIntSafe(String s) {
        try { return Integer.parseInt(s.trim()); } catch (Exception e) { return 0; }
    }

    private static String emptyToNull(String s) {
        return (s == null || s.isBlank()) ? null : s.trim();
    }
}