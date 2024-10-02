package modelo;

import java.util.Objects;

public class TipoCable {

	private String cable;
	private String descripcion;
	private int velocidad;

	public TipoCable(String cable, String descripcion, int velocidad) {
		super();
		this.cable = cable;
		this.descripcion = descripcion;
		this.velocidad = velocidad;
	}

	public String getCable() {
		return cable;
	}

	public void setCable(String cable) {
		this.cable = cable;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public int getVelocidad() {
		return velocidad;
	}

	public void setVelocidad(int velocidad) {
		this.velocidad = velocidad;
	}

	@Override
	public int hashCode() {
		return Objects.hash(cable, descripcion, velocidad);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TipoCable other = (TipoCable) obj;
		return Objects.equals(cable, other.cable) && Objects.equals(descripcion, other.descripcion)
				&& velocidad == other.velocidad;
	}

	@Override
	public String toString() {
		return "TipoCable [cable=" + cable + ", descripcion=" + descripcion + ", velocidad=" + velocidad + "]";
	}
}
