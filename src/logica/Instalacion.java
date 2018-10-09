package logica;

public class Instalacion {

	String idPista;
	String tipo;
	String nPista;
	String descripcion;
	private float precioHora;
	private boolean disponible;
	
	public Instalacion(String idPista, String tipo, String nPista, String descripcion,float precioHora, boolean dispo) {
		this.idPista = idPista;
		this.tipo = tipo;
		this.nPista = nPista;
		this.descripcion = descripcion;
		this.precioHora= precioHora;
		this.disponible = dispo;
	}

	public String getnPista() {
		return nPista;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public String getTipo() {
		return tipo;
	}
	
	public String getIdPista(){
		return idPista;
	}
	
	@Override
	public String toString(){
		return getTipo()+" "+getnPista();
	}

	public float getPrecioHora() {
		return precioHora;
	}

	public boolean isDisponible() {
		return disponible;
	}

	public void setDisponible(boolean disponible) {
		this.disponible = disponible;
	}
	
	
	
}
