package servicios;

import java.sql.SQLException;
import java.util.TreeMap;

import modelo.Equipo;
import dao.EquipoDAO;
import dao.FactoryDAO;

public class EquipoServiceImpl implements EquipoService{
    
    private EquipoDAO equipoDAO;
    
    public EquipoServiceImpl(){
        // equipoDAO = new EquipoSecuencialDAO();
        equipoDAO = FactoryDAO.getEquipoDAO();
    }
    @Override
    public void insertar(Equipo equipo) {
        try {
			equipoDAO.insertar(equipo);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}				
    }

    @Override
    public void actualizar(Equipo equipo, Equipo equipoModificado) {
        try {
			equipoDAO.actualizar(equipo,equipoModificado);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (RuntimeException e){
            // Algunas implementaciones envuelven SQLException en RuntimeException
            e.printStackTrace();
            throw e;
        }                        
    }

    @Override
    public void borrar(Equipo equipo) {
        try {
			equipoDAO.borrar(equipo);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    @Override
    public TreeMap<String,Equipo> buscarTodos() {
        return equipoDAO.buscarTodos();
    }
}
