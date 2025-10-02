package controlador;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import dao.FactoryDAO;
import dao.EquipoDAO;
import dao.postgres.ConexionPostgres;
import servicios.EquipoService;
import servicios.EquipoServiceImpl;

public class DebugOrigen {
    public static void main(String[] args) throws Exception {
        EquipoDAO dao = FactoryDAO.getEquipoDAO();
        System.out.println("EquipoDAO en uso: " + dao.getClass().getName());

        // Conteo desde el servicio (debería ir a Postgres si la factory está en POSTGRES)
        EquipoService svc = new EquipoServiceImpl();
        int countService = svc.buscarTodos().size();
        System.out.println("Equipos (via servicio): " + countService);

        // Conteo directo en BD
        try (Connection c = ConexionPostgres.getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT COUNT(*) FROM equipo");
             ResultSet rs = ps.executeQuery()) {
            rs.next();
            int countDb = rs.getInt(1);
            System.out.println("Equipos (en BD): " + countDb);
        }
    }
}