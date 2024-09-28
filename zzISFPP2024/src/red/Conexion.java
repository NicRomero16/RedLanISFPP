package red;

public class Conexion {
	Equipo equipo1;
	Equipo equipo2;
	TipoCable tipoCable;

	public Conexion(Equipo equipo1, Equipo equipo2, TipoCable tipoCable) {
		super();
		this.equipo1 = equipo1;
		this.equipo2 = equipo2;
		this.tipoCable = tipoCable;
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

}
