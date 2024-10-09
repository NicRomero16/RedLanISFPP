package Excepciones;

public class ArchivoExistenteException extends RuntimeException {

	private static final long serialVersionUID = 2L;

	public ArchivoExistenteException(String mensaje) {
		super(mensaje);
	}
}
