package dao.secuencial;

import java.util.Map;
import java.util.ResourceBundle;

import modelo.TipoEquipo;



public class TipoEquipoSecuencialDAO {

	private Map<String, TipoEquipo> map;
	private String name;
	private boolean update;

	public TipoEquipoSecuencialDAO() {
		ResourceBundle rb = ResourceBundle.getBundle("secuencial");
		name = rb.getString("tipoEquipo");
		update = true;
	}

	private Map<String, TipoEquipo> readFromFile(String file) {
		return map;
		
	}

}
