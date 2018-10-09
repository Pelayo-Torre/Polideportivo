package logica;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Calendar;

import java.util.Map;

import bd.Conexion;

public class Polideportivo {
	private ArrayList<Instalacion> instalaciones = new ArrayList<Instalacion>();
	private ArrayList<Reserva> reservas = new ArrayList<Reserva>();
	private ArrayList<Cliente> clientes = new ArrayList<Cliente>();
	private ArrayList<Pago> pagos= new ArrayList<Pago>();
	private ArrayList<Actividad> actividades = new ArrayList<Actividad>();
	private ArrayList<Cuota> cuotas = new ArrayList<Cuota>();

	Conexion c;

	public Polideportivo() throws SQLException {
		c = new Conexion();
		clientes = c.getClientes();
		instalaciones = c.getInstalaciones();
		reservas = c.getReservas();
		pagos= c.getPagos();
		actividades = c.getActividades();
		cuotas= c.getCuotas();
		c.cargarInfoReservas();
	}
	
	public ArrayList<Cuota> getCuotas() {
		return cuotas;
	}
	
	public ArrayList<Actividad> getActividades(){
		return actividades;
	}
	public ArrayList<Pago> getPagos(){
		return pagos;
	}
	
	public ArrayList<Instalacion> getInstalaciones() {
		return instalaciones;
	}

	public ArrayList<Reserva> getReservas() {
		return reservas;
	}
	
	public ArrayList<Cliente> getClientes() {
		return clientes;
	}
	
	public Conexion getConexion() {
		return c;
	}
	
	public String obtenerIdReserva() {
		return c.obtenerIdReserva(reservas);
	}

	
	public Socio getSocioByUser(String user) {
		for(Cliente c: clientes) {
			if(c instanceof Socio) {
				if(((Socio) c).getUsuario().equals(user))
					return (Socio)c;
			}
		}
		return null;
	}
	
	public Cliente getClienteByDni(String dni) {
		for(Cliente c: clientes) {
			if(c.getDNI().equals(dni))
				return c;
			
		}
		return null;
	}
	
	public Actividad isReservaActividad(Reserva r) {
		for(Actividad act: actividades) {
			for (Map.Entry<Reserva, ArrayList<Socio>> a: act.apuntadosActividades.entrySet()) {
				if(a.getKey().equals(r))
					return act;
			}
		}
		return null;
	}
	
	public void ponerReservaAnulada(String idReserva) {
		for(int i = 0; i < getReservas().size(); i++) {
			if(getReservas().get(i).getIdReserva().equals(idReserva)) 
				getReservas().get(i).setEstado(Reserva.ANULADA);
		}
	}
	
	public boolean comprobarFechas(Reserva r, Reserva intento) {
		Calendar c1 = Calendar.getInstance();c1.setTime(r.getFechaReserva());Calendar c2 = Calendar.getInstance();c2.setTime(r.getFechaReserva());
		c2.add(Calendar.HOUR_OF_DAY, r.getTiempoReserva());
		Calendar c3 = Calendar.getInstance();c3.setTime(intento.getFechaReserva());Calendar c4 = Calendar.getInstance();c4.setTime(intento.getFechaReserva());
		c4.add(Calendar.HOUR_OF_DAY, intento.getTiempoReserva());
		if(c1.before(c4) && c2.after(c3)) 
			return true;
		return false;
	}
	
	/*
	 * Liquida las facturas del mes actual. Para ello busca para cada uno
	 * de los socios si ya tenían una entrada para este mes y les suma el importe de 
	 * las reservas pendientes. Si no tenían una entrada para este mes se le crea una
	 * y se le añade el importe de las reservas pendientes. 
	 * También guarda los datos actualizados a la base de datos.
	 */
	public void liquidarFacturasMes(String id) {
//		String id = Cuota.generarIdCuota(Calendar.getInstance());
		boolean existe;
		ArrayList<Pago> pagosLiquidados;
		for(Cliente sc: clientes) {
			if(sc instanceof Socio && !sc.getDNI().equals("0") && !sc.getDNI().equals("1")) {
				existe=false;
				for(Cuota cuota: cuotas) {
					if((cuota.getSocio().getDNI().equals(sc.getDNI())) && (cuota.getId_cuota().equals(id))) {
						pagosLiquidados=cuota.sumarFacturas(pagos);
						if(!pagosLiquidados.isEmpty()) {
							c.actualizarCuota(cuota);
							c.actualizarPagosLiquidados(pagosLiquidados);
						}
						existe=true;
					}
				}
				if(!existe) {
					Cuota nCuota = new Cuota((Socio) sc,id,Cuota.COSTE_SUSCRIPCION);
					pagosLiquidados=nCuota.sumarFacturas(pagos);
					cuotas.add(nCuota);
					c.agregarCuota(nCuota);
					c.actualizarPagosLiquidados(pagosLiquidados);
				}
			}
		}
	}
	
	
	public ArrayList<Reserva> ReservasById(ArrayList<String> idReserva){
		ArrayList<Reserva> res = new ArrayList<Reserva>();
		for(String id: idReserva) {
			for(Reserva reserva: reservas) {
				if(reserva.getIdReserva().equals(id))
					res.add(reserva);
			}
		}
		return res;
	}
}
