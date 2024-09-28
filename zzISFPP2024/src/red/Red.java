package red;

import java.util.List;
import java.util.Objects;

public class Red {
	String nombre;
	List<Conexion> conexiones;
	List<Ubicacion> ubicaciones;
	List<Equipo> equipos;

	public Red(String nombre, List<Conexion> conexiones, List<Ubicacion> ubicaciones, List<Equipo> equipos) {
		super();
		this.nombre = nombre;
		this.conexiones = conexiones;
		this.ubicaciones = ubicaciones;
		this.equipos = equipos;
	}

	public String getNombre() {
		return nombre;
	}

	public List<Conexion> getConexiones() {
		return conexiones;
	}

	public void setConexiones(List<Conexion> conexiones) {
		this.conexiones = conexiones;
	}

	public List<Ubicacion> getUbicaciones() {
		return ubicaciones;
	}

	public void setUbicaciones(List<Ubicacion> ubicaciones) {
		this.ubicaciones = ubicaciones;
	}

	public List<Equipo> getEquipos() {
		return equipos;
	}

	public void setEquipos(List<Equipo> equipos) {
		this.equipos = equipos;
	}

	@Override
	public int hashCode() {
		return Objects.hash(nombre);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Red other = (Red) obj;
		return Objects.equals(nombre, other.nombre);
	}

	@Override
	public String toString() {
		return "Red [nombre=" + nombre + "]";
	}
}
