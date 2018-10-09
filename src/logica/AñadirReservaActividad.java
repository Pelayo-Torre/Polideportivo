package logica;

import java.sql.SQLException;
import java.util.ArrayList;


public class AñadirReservaActividad {
	
	public static int obtenerIdCodMismaAct(Polideportivo poli){
		int cod_misma_act = 0;
		try {
			cod_misma_act = poli.getConexion().generarCodMismaActividad();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return cod_misma_act;
	}
	
	public static void añadirReserva( Polideportivo poli , int cod_misma_actividad , ArrayList<Reserva> res , Actividad actSelected ) {
		//******* GUARDADO BD *******
		try {
			poli.getConexion().añadirReservaParaActividadPeriodica( actSelected ,  res );
			//*******AÑADO LA NUEVA INFROMACION A LA TABLA SE_REALIZA_EN********
			poli.getConexion().reservarHorarioActividadPeriodica( actSelected.getCodActividad() , res ,  cod_misma_actividad);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		//******* GUARDADO LOCAL *******
		for(Reserva r: res) {
			actSelected.getApuntadosActividades().put(r, new ArrayList<Socio>());
			poli.getReservas().add(r);
			System.out.println("ID_RESERVA :" + r.getIdReserva());
		}
	}
}
