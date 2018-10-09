package logica;

public class Pago {
	
	

	private String idPago;	
	private Reserva reserva;
	@SuppressWarnings("unused")
	private boolean efectivo;
	private float precio;
	private Cliente cliente;
	private boolean liquidada;
	
	public Pago(String idPago, Reserva reserva, boolean efectivo, boolean liquidada) {
		super();
		this.idPago = idPago;
		this.reserva = reserva;
		this.efectivo = efectivo;
		this.precio = reserva.getPrecioReserva();
		this.cliente = reserva.getCliente();
		this.liquidada = liquidada;
		
	}
	
	public String getIdPago() {
		return idPago;
	}

	public Reserva getReserva() {
		return reserva;
	}
	
	public Cliente getCliente() {
		return cliente;
	}
	
	public float getImporte() {
		return precio;
	}
	
	public boolean isLiquidada() {
		return liquidada;
	}
	
	public void liquidarPago() {
		liquidada = true;
	}
}
