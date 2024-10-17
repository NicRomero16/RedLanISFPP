package excepciones;

public class EquipoInexistenteException extends RuntimeException {

	public EquipoInexistenteException(String mensaje) {
		super(mensaje);
	}
}