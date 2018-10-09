package logica;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Recibo {

	
	public static void generarRecibo(Reserva reserva) {
		SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy_hh-mm");
		String date = df.format(reserva.getFechaReserva());
		String filename="Recibo"+reserva.getCliente().getDNI()+"-"+date+".txt";
		try (PrintWriter writer =new PrintWriter(filename, "utf-16")) {
		   writer.println("Reserva de instalación "+reserva.getInstalacion().getTipo()+ " "+ reserva.getInstalacion().getIdPista());
		   writer.println("Usuario: "+reserva.getCliente().toString());
		   writer.println("Fecha: " + reserva.getFechaReserva());
		   writer.println("Precio/hora: "+reserva.getInstalacion().getPrecioHora()+ 
				   "€  Tiempo de reserva: "+reserva.getTiempoReserva()+(reserva.getTiempoReserva()==1?" hora":" horas"));
		   writer.print("Importe total=  "+reserva.getPrecioReserva()+"€");
		   writer.close();
	   
		}
		catch(IOException e) {
			e.printStackTrace();
			System.err.println("Problemas al generar el recibo");
		}
	}
	
	/**
	 * Recibo de devolución para un no socio cuando se cancela una reserva.
	 * @param reserva
	 * @throws IOException 
	 */
	public static void generarReciboNoSocio(Reserva reserva) throws IOException {
		if(reserva.getCliente() instanceof NoSocio){
			Calendar cal = Calendar.getInstance();
			int dia,mes,año,hora;
			cal.setTime(reserva.getFechaReserva());
			dia = cal.get(Calendar.DAY_OF_MONTH);
			mes = cal.get(Calendar.MONTH);
			año = cal.get(Calendar.YEAR);
			hora = cal.get(Calendar.HOUR_OF_DAY);
			SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy_hh-mm");
			String date = df.format(reserva.getFechaReserva());
			BufferedWriter writer = new BufferedWriter(new FileWriter("ReciboDevolucion-"+reserva.getCliente().getDNI()+"-"+date+".txt"));
			writer.write("----------Recibo de devolución----------");
			writer.newLine();
			writer.write("MOTIVO DE ANULACIÓN: "+reserva.getMotivoAnulacion());
			writer.newLine();
			writer.write("Reserva de instalación "+reserva.getInstalacion().getTipo()+ "   Pista: "+ reserva.getInstalacion().getnPista());
			writer.newLine();
			writer.write("Usuario: "+reserva.getCliente().toString());
			writer.newLine();
			writer.write("Fecha: " + año+"-"+(mes+1)+"-"+dia+" "+hora+":00");
			writer.newLine();
			writer.write("Nº de horas: "+reserva.getTiempoReserva());
			writer.newLine();
			writer.write("Se le ha devuelto la siguiente cantidad: "+reserva.getPrecioReserva()+"€");
			writer.newLine();
			writer.write("----------------------------------------");
			writer.close();
		}
	}
}
