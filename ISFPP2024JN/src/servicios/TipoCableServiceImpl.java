package servicios;

import java.sql.SQLException;
import java.util.TreeMap;

import dao.TipoCableDAO;
import dao.FactoryDAO;
import modelo.TipoCable;

public class TipoCableServiceImpl implements TipoCableService{

    private TipoCableDAO tipoCableDAO;
    
    public TipoCableServiceImpl(){
        tipoCableDAO = FactoryDAO.getTipoCableDAO();
    }
    
    @Override
    public void insertar(TipoCable tipoCable) {
        try {
			tipoCableDAO.insertar(tipoCable);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}				
    }

    @Override
    public void actualizar(TipoCable tipoCable) {
        try {
			tipoCableDAO.actualizar(tipoCable);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}						
    }

    @Override
    public void borrar(TipoCable tipoCable) {
        try {
			tipoCableDAO.borrar(tipoCable);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    @Override
    public TreeMap<String,TipoCable> buscarTodos() {
        return tipoCableDAO.buscarTodos();
    }
}
