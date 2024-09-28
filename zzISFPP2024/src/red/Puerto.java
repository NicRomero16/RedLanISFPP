package red;

import java.util.Objects;

public class Puerto {
	int cantidad;
	TipoPuerto tipopuerto;
	public Puerto(int cantidad, TipoPuerto tipopuerto) {
		super();
		this.cantidad = cantidad;
		this.tipopuerto = tipopuerto;
	}
	public int getCantidad() {
		return cantidad;
	}
	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}
	public TipoPuerto getTipopuerto() {
		return tipopuerto;
	}
	public void setTipopuerto(TipoPuerto tipopuerto) {
		this.tipopuerto = tipopuerto;
	}
	@Override
	public int hashCode() {
		return Objects.hash(tipopuerto);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Puerto other = (Puerto) obj;
		return Objects.equals(tipopuerto, other.tipopuerto);
	}
	@Override
	public String toString() {
		return "Puerto [cantidad=" + cantidad + ", tipopuerto=" + tipopuerto + "]";
	}
	

}
