package modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Equipo {
	private String codigo;
	private String descripcion;
	private String marca;
	private String modelo;
	private TipoEquipo tipoEquipo;
	private Ubicacion ubicacion;
	private List<Puerto> puertos;
	private List<String> direccionesIP;
	private boolean estado;

	public Equipo(String codigo, String descripcion, String marca, String modelo, TipoEquipo tipoEquipo,
			Ubicacion ubicacion, boolean estado) {
		super();
		this.codigo = codigo;
		this.descripcion = descripcion;
		this.marca = marca;
		this.modelo = modelo;
		this.tipoEquipo = tipoEquipo;
		this.ubicacion = ubicacion;
		this.puertos = new ArrayList<Puerto>();
		this.direccionesIP = new ArrayList<String>();
		this.estado = estado;
	}
	
	public void agregarDireccionIP(String ip) {
		this.direccionesIP.add(ip);
	}

	public void agregarPuerto(int cantidad, TipoPuerto tipoPuerto) {
		this.puertos.add(new Puerto(cantidad, tipoPuerto));
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

	public TipoEquipo getTipoEquipo() {
		return tipoEquipo;
	}

	public void setTipoEquipo(TipoEquipo tipoEquipo) {
		this.tipoEquipo = tipoEquipo;
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

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public List<String> getDireccionesIP() {
		return direccionesIP;
	}

	public void setDireccionesIP(List<String> direccionesIP) {
		this.direccionesIP = direccionesIP;
	}

	public boolean getEstado() {
		return estado;
	}

	public void setEstado(boolean estado) {
		this.estado = estado;
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

	public boolean contieneIp(String ip) {
		return direccionesIP.contains(ip);
	}

	public boolean contieneEquipo(String equipo) {
		return codigo.contains(equipo);
	}

	public String obtenerCodigoTipoPuerto(int index) {
	    if (index < 0 || index >= puertos.size()) {
	        return ""; // Índice fuera de rango, devuelve cadena vacía o lanza una excepción personalizada
	    }
	    Puerto puerto = puertos.get(index);
	    if (puerto.getTipoPuerto() == null) {
	        return ""; // Retorna cadena vacía si TipoPuerto es null
	    }
	    return this.puertos.get(index).getTipoPuerto().getCodigo();
	}

	public int getCantidadPuertos() {
	    int total = 0;
	    for (Puerto puerto : puertos) {
	        total += puerto.getCantidad();
	    }
	    return total;
	}

	@Override
	public String toString() {
		return "\nEquipo [\n codigo=" + codigo + ", \n descripcion=" + descripcion + ", \n marca=" + marca
				+ ", \n modelo=" + modelo + " \n " + tipoEquipo + ", \n " + ubicacion + ", \n " + puertos
				+ ", \n DireccionesIP=" + direccionesIP + ", \n estado=" + estado + "] \n";
	}

	private class Puerto {

		private int cantidad;
		private TipoPuerto tipoPuerto;

		private Puerto(int cantidad, TipoPuerto tipoPuerto) {

			this.cantidad = cantidad;
			this.tipoPuerto = tipoPuerto;
		}

		@SuppressWarnings("unused")
		private int getCantidad() {
			return cantidad;
		}

		@SuppressWarnings("unused")
		private TipoPuerto getTipoPuerto() {
			return tipoPuerto;
		}

		@Override
		public String toString() {
			return "cantidad=" + cantidad + ", " + tipoPuerto;
		}
	}



}