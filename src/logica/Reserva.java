package logica;
import java.util.Calendar;
import java.util.Date;

public class Reserva {
	
	

	private Cliente cliente;
	
	public static final String CADUCADA="CADUCADA";
	public static final String LIBRE = "LIBRE";
	public static final String OCUPADA = "OCUPADA";
	public static final String FINALIZADA = "FINALIZADA";
	public static final String ANULADA = "ANULADA";
	
	private String estado;
	private Date horaEntrada;
	private Date horaSalida;
	
	private Instalacion instalacion;
	private Date fechaReserva;
	private int tiempoReserva;
	
	private boolean reservaCentro;
	private String tipoActividad;
	
	private String idReserva;
	private boolean pagada;
	private float precio;
	private String motivoAnulacion;
	
	
	/*
	 * Constructor para reservas de clientes.
	 */
	public Reserva(Cliente cliente, Instalacion instalacion, Date fechaReserva,
			int tiempoReserva, String estado, Date horaEntrada, Date horaSalida, String idReserva, boolean pagada
			,String motivoAnulacion) {
		this.cliente = cliente;
		this.instalacion = instalacion;
		this.fechaReserva = fechaReserva;
		this.tiempoReserva = tiempoReserva;
		this.estado = estado;
		this.horaEntrada = horaEntrada;
		this.horaSalida = horaSalida;
		this.idReserva=idReserva;
		this.pagada=pagada;
		this.motivoAnulacion = motivoAnulacion;
		precio=calcularPrecio(instalacion, tiempoReserva);
	}
	
	/*
	 * Constructor para reservas de centro.
	 */
	public Reserva(Instalacion inst, Date fechaReserva, int tiempoReserva, String tipoActividad,String idReserva, String estado){
		this.instalacion= inst;
		this.fechaReserva = fechaReserva;
		this.tiempoReserva = tiempoReserva;
		reservaCentro=true;
		this.tipoActividad=tipoActividad;
		this.idReserva=idReserva;
		this.estado=estado;
	}
	
	private float calcularPrecio(Instalacion inst, int nHoras) {
		return inst.getPrecioHora()*nHoras;
	}
	
	public Date getFechaReserva() {
		return fechaReserva;
	}
	
	public int getTiempoReserva() {
		return tiempoReserva;
	}
	
	public Cliente getCliente() {
		return cliente;
	}
	public Instalacion getInstalacion() {
		return instalacion;
	}

	public boolean isReservaCentro() {
		return reservaCentro;
	}
	
	public String getTipoActividad() {
		return tipoActividad;
	}

	public String getEstado() {
		return estado;
	}

	public Date getHora_entrada() {
		
		return horaEntrada;
	}

	public Date getHora_salida() {
		
		return horaSalida;
	}

	public void setEstado(String estado) {
		this.estado=estado;
		
	}

	public void setHora_entrada(Date date) {
		horaEntrada=date;
		
	}

	public void setHora_salida(Date date) {
		horaSalida=date;
		
	}
	
	public void setIdReserva(String id) {
		idReserva=id;
	}
	
	public String getIdReserva() {
		return idReserva;
	}
	
	/*
	 * Devuelve la horas reservadas en formato "hh:00 - hh:00"
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		Calendar cEntrada= Calendar.getInstance();
		cEntrada.setTime(fechaReserva);
		Calendar cSalida=Calendar.getInstance();
		cSalida.setTime(fechaReserva);
		cSalida.add(Calendar.HOUR_OF_DAY, tiempoReserva);
		return cEntrada.get(Calendar.HOUR_OF_DAY)+":00 - "+cSalida.get(Calendar.HOUR_OF_DAY)+":00";
		
		
	}
	
	public float getPrecioReserva() {
		return precio;
		
	}
	
	public boolean isPagada() {
		return pagada;
	}
	
	public void pagar() {
		pagada=true;
	}
	public String getMotivoAnulacion() {
		return motivoAnulacion;
	}

	public void setMotivoAnulacion(String motivoAnulacion) {
		this.motivoAnulacion = motivoAnulacion;
	}
}
