package logica;

import java.util.Date;

public class Socio extends Cliente{
	
	private int numeroSocio;
	private Date fechaRegistro;
	private String usuario;
	private String contrase�a;
	

	public Socio(String nombre, String apellidos, String DNI, String telefono, int socio, String usu, String contra, Date fechaRegistro) {
		super(nombre, apellidos, DNI, telefono);
		numeroSocio = socio;
		usuario = usu;
		contrase�a = contra;
		this.fechaRegistro = fechaRegistro;
	}

	

	public Date getFechaRegistro() {
		return fechaRegistro;
	}



	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}



	public int getNumeroSocio() {
		return numeroSocio;
	}


	public void setNumeroSocio(int numeroSocio) {
		this.numeroSocio = numeroSocio;
	}


	public String getUsuario() {
		return usuario;
	}


	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}


	public String getContrase�a() {
		return contrase�a;
	}


	public void setContrase�a(String contrase�a) {
		this.contrase�a = contrase�a;
	}
	
	@Override
	public String toString() {
		return super.toString()+ " N�Socio: "+getNumeroSocio();
	}

}
