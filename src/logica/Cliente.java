package logica;

public class Cliente {
	
	private String nombre;
	private String apellidos;
	private String DNI;
	private String telefono;
	
	public Cliente(String nombre, String apellidos, String DNI, String telefono)
	{
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.DNI = DNI;
		this.telefono = telefono;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getDNI() {
		return DNI;
	}

	public void setDNI(String dNI) {
		DNI = dNI;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	
	@Override
	public String toString() {
		return getNombre()+" "+getApellidos()+" "+getDNI();
	}
	

}
