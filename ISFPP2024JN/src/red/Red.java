package red;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Red {

	private String nombre;
	private List<Ubicacion> ubicaciones;
	private List<Equipo> equipos;
	private List<Conexion> conexiones;

	public Red(String nombre) {
		super();
		this.nombre = nombre;
		this.ubicaciones = new ArrayList<Ubicacion>();
		this.equipos = new ArrayList<Equipo>();
		this.conexiones = new ArrayList<Conexion>();
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
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

	public List<Conexion> getConexiones() {
		return conexiones;
	}

	public void setConexiones(List<Conexion> conexiones) {
		this.conexiones = conexiones;
	}

	@Override
	public int hashCode() {
		return Objects.hash(conexiones, equipos, nombre, ubicaciones);
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
		return Objects.equals(conexiones, other.conexiones) && Objects.equals(equipos, other.equipos)
				&& Objects.equals(nombre, other.nombre) && Objects.equals(ubicaciones, other.ubicaciones);
	}

	@Override
	public String toString() {
		return "Red [nombre=" + nombre + ", ubicaciones=" + ubicaciones + ", equipos=" + equipos + ", conexiones="
				+ conexiones + "]";
	}
}
