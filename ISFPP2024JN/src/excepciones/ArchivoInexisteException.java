package excepciones;

public class ArchivoInexisteException extends RuntimeException {
	
	public ArchivoInexisteException(String msg) {
		super(msg);
	}
}
