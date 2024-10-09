package Excepciones;

public class NoExisteException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public NoExisteException(String mensaje) {
		super(mensaje);
	}
	
}
