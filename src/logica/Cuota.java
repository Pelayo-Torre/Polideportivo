package logica;

import java.util.ArrayList;
import java.util.Calendar;


public class Cuota {
	
	public static final float COSTE_SUSCRIPCION = 20; 
	
	private Socio socio;
	private String id_cuota;
	private float importe;
	
	public Cuota(Socio sc, String id_cuota, float importe) {
		this.socio=sc;
		this.id_cuota=id_cuota;
		this.importe=importe;
	}

	public Socio getSocio() {
		return socio;
	}

	public String getId_cuota() {
		return id_cuota;
	}

	public float getImporte() {
		return importe;
	}
	
	public void aumentarImporte(float precio) {
		importe+=precio;
	}

	/*
	 * Dado un calendario que se le pasa por parámetro genera
	 * un id_cuota con el formato MMYYYY. Por ejemplo, para el 15 de noviembre 
	 * 2017 devuelve el string "112017". Los meses van del 20 del anterior 
	 * al 19 de este.
	 */
	public static String generarIdCuota(Calendar hoy) {
		if(hoy.get(Calendar.DAY_OF_MONTH)>20) hoy.add(Calendar.MONTH, 1);
		String mes = String.valueOf(hoy.get(Calendar.MONTH)+1);
		String año = String.valueOf(hoy.get(Calendar.YEAR));
		return mes+año;
		
	}

	/*
	 * Añade a la cuota los pagos pendientes de este mes.
	 * Recibe el array de todos los pagos y devuelve un array con 
	 * los pagos que han sido liquidados para que se pueda
	 * actualizar la base de datos.
	 */
	public ArrayList<Pago> sumarFacturas(ArrayList<Pago> pagos) {
		ArrayList<Pago> pagosLiquidados= new ArrayList<Pago>();
		Calendar limiteInferior = Calendar.getInstance();
		Calendar limiteSuperior = Calendar.getInstance();
		
		if(limiteInferior.get(Calendar.DAY_OF_MONTH)<20) 
			limiteInferior.add(Calendar.MONTH, -1);
		else 
			limiteSuperior.add(Calendar.MONTH, 1);
		
		limiteInferior.set(Calendar.DAY_OF_MONTH, 20);
		limiteSuperior.set(Calendar.DAY_OF_MONTH, 19);
		
		limiteInferior.set(Calendar.HOUR_OF_DAY,00);
		limiteSuperior.set(Calendar.HOUR_OF_DAY,00);
		
		limiteInferior.set(Calendar.MINUTE,00);
		limiteSuperior.set(Calendar.MINUTE,00);
		
		limiteInferior.set(Calendar.SECOND,00);
		limiteSuperior.set(Calendar.SECOND,00);
		
		Calendar fechaReserva = Calendar.getInstance();
		
		for(Pago p: pagos) {
			if(p.getCliente().getDNI().equals(socio.getDNI()) && !p.isLiquidada()) {
				fechaReserva.setTime(p.getReserva().getFechaReserva());
				if( (fechaReserva.after(limiteInferior) || (fechaReserva.compareTo(limiteInferior)==0) 
						&& fechaReserva.before(limiteSuperior) || (fechaReserva.compareTo(limiteSuperior)==0))) {
						aumentarImporte(p.getImporte());
						p.liquidarPago();
						pagosLiquidados.add(p);
				}	
			}
		}
		
		return pagosLiquidados;
	}
}
