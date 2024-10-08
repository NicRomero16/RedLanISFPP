package aplicacion;

import java.io.IOException;
import java.util.TreeMap;

import dao.secuencial.*;
import modelo.*;

public class TestCompilacion {

	public static void main(String[] args) throws IOException {
        TipoPuertoSecuencialDAO dao = new TipoPuertoSecuencialDAO();
        
        TreeMap<String, TipoPuerto> tiposDePuertos = dao.buscarTodos();
        System.out.println(tiposDePuertos);

       
    }

}
