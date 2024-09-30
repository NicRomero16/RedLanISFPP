package modelo;

public class Maquina {
	private String id; //identificador único del equipo (p. ej., "PC1").	// Identificador único del router (p. ej., "Router1").
	private String ipAddress; // dirección IP asignada al equipo. 	// dirección IP del router.
	private String macAddress; // dirección MAC de la placa de red.	// dirección MAC del router.
	private boolean status; // activo/inactivo. 
	private String ubicacion; // ubicación física del equipo (p. ej., sala 101).// ubicación física del router (p. ej., sala de servidores).
	
	public Maquina(String id, String ipAddress, String macAddress, boolean status, String ubicacion) {
		super();
		this.id = id;
		this.ipAddress = ipAddress;
		this.macAddress = macAddress;
		this.status = status;
		this.ubicacion = ubicacion;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public String getMacAddress() {
		return macAddress;
	}
	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}
	public boolean getStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public String getUbicacion() {
		return ubicacion;
	}
	public void setUbicacion(String ubicacion) {
		this.ubicacion = ubicacion;
	}

	
}
