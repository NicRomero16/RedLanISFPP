package red;

import java.util.List;
import java.util.Objects;

public class Equipo {

	String codigo;
	String descripcion;
	String marca;
	String modelo;
	List<String> direccionesIP;
	Ubicacion ubicacion;
	List<Puerto> puertos;
	TipoEquipo tipoEquipo;

	public Equipo(String codigo, String descripcion, String marca, String modelo, List<String> direccionesIP,
			Ubicacion ubicacion, List<Puerto> puertos, TipoEquipo tipoEquipo) {
		super();
		this.codigo = codigo;
		this.descripcion = descripcion;
		this.marca = marca;
		this.modelo = modelo;
		this.direccionesIP = direccionesIP;
		this.ubicacion = ubicacion;
		this.puertos = puertos;
		this.tipoEquipo = tipoEquipo;
	}

	public String getCodigo() {
		return codigo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getMarca() {
		return marca;
	}

	public void setMarca(String marca) {
		this.marca = marca;
	}

	public String getModelo() {
		return modelo;
	}

	public void setModelo(String modelo) {
		this.modelo = modelo;
	}

	public List<String> getDireccionesIP() {
		return direccionesIP;
	}

	public Ubicacion getUbicacion() {
		return ubicacion;
	}

	public void setUbicacion(Ubicacion ubicacion) {
		this.ubicacion = ubicacion;
	}

	public List<Puerto> getPuertos() {
		return puertos;
	}

	public void setPuertos(List<Puerto> puertos) {
		this.puertos = puertos;
	}

	public TipoEquipo getTipoEquipo() {
		return tipoEquipo;
	}

	public void setTipoEquipo(TipoEquipo tipoEquipo) {
		this.tipoEquipo = tipoEquipo;
	}

	private class Puerto {
		int cantidad;
		TipoPuerto tipoPuertos;

		public Puerto(int cantidad, TipoPuerto tipoPuertos) {
			super();
			this.cantidad = cantidad;
			this.tipoPuertos = tipoPuertos;
		}

		public int getCantidad() {
			return cantidad;
		}

		public TipoPuerto getTipoPuertos() {
			return tipoPuertos;
		}

	}

	@Override
	public int hashCode() {
		return Objects.hash(codigo);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Equipo other = (Equipo) obj;
		return Objects.equals(codigo, other.codigo);
	}

	@Override
	public String toString() {
		return "Equipo [codigo=" + codigo + ", descripcion=" + descripcion + ", marca=" + marca + ", modelo=" + modelo
				+ ", ubicacion=" + ubicacion + ", tipoEquipo=" + tipoEquipo + "]";
	}
}
