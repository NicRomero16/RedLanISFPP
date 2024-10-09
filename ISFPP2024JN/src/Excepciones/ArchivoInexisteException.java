package Excepciones;

public class ArchivoInexisteException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public ArchivoInexisteException(String mensaje) {
		super(mensaje);
	}
	
}
