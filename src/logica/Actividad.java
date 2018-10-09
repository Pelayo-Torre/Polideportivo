package logica;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class Actividad {
	String codActividad;
	String nombreActividad;
	int plazas;
	Map<Reserva, ArrayList<Socio>> apuntadosActividades;
	boolean plazasLimitadas;

	public Actividad(String codActividad, String nombreActividad, int plazas) {
		this.codActividad = codActividad;
		this.nombreActividad = nombreActividad;
		this.plazas = plazas;
		apuntadosActividades = new HashMap<Reserva,ArrayList<Socio>>();
		//TODO chapucilla para probar, cambiar la bd para que lo acepte
		plazasLimitadas=true;
	}
	
	public Actividad(String codActividad, String nombreActividad, int plazas, boolean plazasLimitadas) {
		this.codActividad = codActividad;
		this.nombreActividad = nombreActividad;
		this.plazas = plazas;
		apuntadosActividades = new HashMap<Reserva,ArrayList<Socio>>();
		this.plazasLimitadas=plazasLimitadas;
	}
	
	public ArrayList<Reserva> obtenerReservasActDeUnSocio(Socio sc){
		ArrayList<Reserva> rep = new ArrayList<Reserva>();
		for (Map.Entry<Reserva, ArrayList<Socio>> r: apuntadosActividades.entrySet()) {
			if(r.getValue().contains(sc))
				rep.add(r.getKey());
		}
		return rep;
	}
	
	public void apuntarme(Reserva r, Socio soc) {
		apuntadosActividades.get(r).add(soc);
	}
	
	public void desapuntarmme(Reserva r, Socio sc) {
		apuntadosActividades.get(r).remove(sc);
	}

	
	public boolean disponible(Reserva r) {
		return apuntadosActividades.get(r).size() < plazas;
	}
	
	public Reserva obtenerReserva(Date reserva) {
		for (Map.Entry<Reserva, ArrayList<Socio>> r: apuntadosActividades.entrySet()) {
			if(r.getKey().getFechaReserva().equals(reserva))
				return r.getKey();
		}
		return null;
	}
	
	public int plazasDisponibles(Reserva r) {
		return plazas - apuntadosActividades.get(r).size();
	}

	public String getCodActividad() {
		return codActividad;
	}

	public String getNombreActividad() {
		return nombreActividad;
	}

	public int getPlazas() {
		return plazas;
	}

	public Map<Reserva,ArrayList<Socio>> getApuntadosActividades() {
		return apuntadosActividades;
	}
	
	@Override
	public String toString() {
		return getNombreActividad();
	}
	
	/**
	 * Obtiene todos los horarios del día actual en los que 
	 * se va a realizar la actividad 
	 * @return
	 */
	public ArrayList<Reserva> obtenerReservasHoy(){
		SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
		Date hoy = new Date();
		ArrayList<Reserva> reservasHoy = new ArrayList<Reserva>();
		for (Map.Entry<Reserva, ArrayList<Socio>> r: apuntadosActividades.entrySet()) {
			if(fmt.format(hoy).equals(fmt.format(r.getKey().getFechaReserva())))
				reservasHoy.add(r.getKey());
		}
		return reservasHoy;
	}
	
	public boolean tienePlazasLimitadas() {
		return plazasLimitadas;
	}
	
	public ArrayList<Socio> obtenerApuntados(Reserva r){
		return apuntadosActividades.get(r);
	}
}
