package modelo;

public class Routers extends Maquina {

	private String modelo; // marca y modelo del equipo.
	private String firmware; // versión del firmware
	private int trhougput; // Capacidad de procesamiento del router en Mbps. (Ancho de banda)

	public Routers(String id, String ipAddress, String macAddress, boolean status, String ubicacion, String modelo,
			String firmware, int trhougput) {
		super(id, ipAddress, macAddress, status, ubicacion);
		this.modelo = modelo;
		this.firmware = firmware;
		this.trhougput = trhougput;
	}

	public String getModelo() {
		return modelo;
	}

	public void setModelo(String modelo) {
		this.modelo = modelo;
	}

	public String getFirmware() {
		return firmware;
	}

	public void setFirmware(String firmware) {
		this.firmware = firmware;
	}

	public int getTrhougput() {
		return trhougput;
	}

	public void setTrhougput(int trhougput) {
		this.trhougput = trhougput;
	}
	

}
