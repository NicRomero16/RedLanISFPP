package servicios;

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
        equipoDAO.insertar(equipo);				
    }

    @Override
    public void actualizar(Equipo equipo, Equipo equipoModificado) {
        equipoDAO.actualizar(equipo,equipoModificado);						
    }

    @Override
    public void borrar(Equipo equipo) {
        equipoDAO.borrar(equipo);
    }

    @Override
    public TreeMap<String,Equipo> buscarTodos() {
        return equipoDAO.buscarTodos();
    }
}
