package Excepciones;

public class EquipoInexistenteException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public EquipoInexistenteException(String mensaje) {
		super(mensaje);
	}
	
}
