package modelo;

import java.util.Objects;

public class Conexion {

	private Equipo equipo1;

	private TipoPuerto tipoPuerto1;
	private Equipo equipo2;

	private TipoPuerto tipoPuerto2;
	private TipoCable tipoCable;

	public Conexion(Equipo equipo1, TipoPuerto tipoPuerto1, Equipo equipo2, TipoPuerto tipoPuerto2,
			TipoCable tipoCable) {
		super();
		this.equipo1 = equipo1;
		this.equipo2 = equipo2;
		this.tipoCable = tipoCable;
		this.tipoPuerto1 = tipoPuerto1;
		this.tipoPuerto2 = tipoPuerto2;
	}

	public Equipo getEquipo1() {
		return equipo1;
	}

	public void setEquipo1(Equipo equipo1) {
		this.equipo1 = equipo1;
	}

	public Equipo getEquipo2() {
		return equipo2;
	}

	public void setEquipo2(Equipo equipo2) {
		this.equipo2 = equipo2;
	}

	public TipoCable getTipoCable() {
		return tipoCable;
	}

	public void setTipoCable(TipoCable tipoCable) {
		this.tipoCable = tipoCable;
	}

	public TipoPuerto getTipoPuerto1() {
		return tipoPuerto1;
	}

	public void setTipoPuerto1(TipoPuerto tipoPuerto1) {
		this.tipoPuerto1 = tipoPuerto1;
	}

	public TipoPuerto getTipoPuerto2() {
		return tipoPuerto2;
	}

	public void setTipoPuerto2(TipoPuerto tipoPuerto2) {
		this.tipoPuerto2 = tipoPuerto2;
	}

	@Override
	public int hashCode() {
		return Objects.hash(equipo1, equipo2, tipoCable, tipoPuerto1, tipoPuerto2);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Conexion other = (Conexion) obj;
		return Objects.equals(equipo1, other.equipo1) && Objects.equals(equipo2, other.equipo2)
				&& Objects.equals(tipoCable, other.tipoCable) && Objects.equals(tipoPuerto1, other.tipoPuerto1)
				&& Objects.equals(tipoPuerto2, other.tipoPuerto2);
	}

	public void leerConexiones() {
		System.out.println("Equipo1=" + equipo1);
		System.out.println("Tipo de puerto 1=" + tipoPuerto1);
		System.out.println("Equipo2=" + equipo2);
		System.out.println("Tipo de puerto 2=" + tipoPuerto2);
		System.out.println("Tipo de cable=" + tipoCable);
	}

	@Override
	public String toString() {
		return "\nConexion [\n --equipo1=" + equipo1 + ", tipoPuerto1=" + tipoPuerto1 + ", \n --equipo2=" + equipo2
				+ ", tipoPuerto2=" + tipoPuerto2 + ", \n --tipoCable=" + tipoCable + "]\n";
	}

}
