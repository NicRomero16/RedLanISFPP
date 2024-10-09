package negocio;

public class EquipoInexistenteException extends RuntimeException {

	public EquipoInexistenteException(String mensaje) {
		super(mensaje);
	}
}