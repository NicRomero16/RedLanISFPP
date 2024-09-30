package modelo;

public class Conexiones {
	private Maquina sourceNode; // Nodo de origen de la conexión (p. ej., "PC1").
	private Maquina targetNode; // Nodo de destino de la conexión (p. ej., "Router1").
	private String tipoDeConexion; // : (fibra, utp, wifi, etc.).
	private int bandwidth; // Ancho de banda de la conexión en Mbps.
	private int latencia; // Latencia de la conexión en ms.
	private boolean status; // activa/inactiva.
	private double errorRate; // Tasa de errores en la conexión en %.

	public Conexiones(Maquina sourceNode, Maquina targetNode, String tipoDeConexion, int bandwidth, int latencia,
			boolean status, double errorRate) {
		super();
		this.sourceNode = sourceNode;
		this.targetNode = targetNode;
		this.tipoDeConexion = tipoDeConexion;
		this.bandwidth = bandwidth;
		this.latencia = latencia;
		this.status = status;
		this.errorRate = errorRate;
	}

	public Maquina getSourceNode() {
		return sourceNode;
	}

	public void setSourceNode(Maquina sourceNode) {
		this.sourceNode = sourceNode;
	}

	public Maquina getTargetNode() {
		return targetNode;
	}

	public void setTargetNode(Maquina targetNode) {
		this.targetNode = targetNode;
	}

	public String getTipoDeConexion() {
		return tipoDeConexion;
	}

	public void setTipoDeConexion(String tipoDeConexion) {
		this.tipoDeConexion = tipoDeConexion;
	}

	public int getBandwidth() {
		return bandwidth;
	}

	public void setBandwidth(int bandwidth) {
		this.bandwidth = bandwidth;
	}

	public int getLatencia() {
		return latencia;
	}

	public void setLatencia(int latencia) {
		this.latencia = latencia;
	}

	public boolean getStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public double getErrorRate() {
		return errorRate;
	}

	public void setErrorRate(double errorRate) {
		this.errorRate = errorRate;
	}

}
