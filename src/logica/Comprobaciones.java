package logica;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class Comprobaciones {

	/**
	 * M�todo que comprueba el uso correcto de los comboboxes de las horas
	 */
	public boolean comprobarComboHoras(int valorHoraInicial, int valorHoraFinal){
		if(valorHoraInicial == 23 && (valorHoraFinal == 00 || valorHoraFinal == 01)){
			return true;
		}
		else if(valorHoraInicial == 22 && valorHoraFinal == 00){
			return true;
		}
		else if(valorHoraFinal < valorHoraInicial){
			return false;
		}	//La hora inicial no puede ser mayor que la final
		else if(valorHoraInicial == valorHoraFinal){
			return false;
		}	//Las horas no pueden coincidir
		else if(Math.abs(valorHoraFinal - valorHoraInicial) > 2){
			return false;
		}	//Las pistas como mucho son reservadas por dos horas.
		
		return true;
	}
	
	/**
	 * M�todo que realiza la conversi�n.
	 * @param srt
	 * @return
	 */
	public int mes(String srt){
		if(srt.equals("Enero")){return 0;}
		else if(srt.equals("Febrero")){return 1;}
		else if(srt.equals("Marzo")){return 2;}
		else if(srt.equals("Abril")){return 3;}
		else if(srt.equals("Mayo")){return 4;}
		else if(srt.equals("Junio")){return 5;}
		else if(srt.equals("Julio")){return 6;}
		else if(srt.equals("Agosto")){return 7;}
		else if(srt.equals("Septiembre")){return 8;}
		else if(srt.equals("Octubre")){return 9;}
		else if(srt.equals("Noviembre")){return 10;}
		else {return 11;}
	}
	
	
	/**
	 * M�todo para evitar reservar a una fecha donde ya haya una reserva
	 */
	@SuppressWarnings("deprecation")
	public boolean reservado(Polideportivo poli, int valorDiaCombo, int valorMesCombo, int valorA�oCombo,
			int valorHoraInicialCombo, int valorHoraFinalCombo, String comboInstalacion, String comboPista){
		Calendar cal = Calendar.getInstance();
		int tiempo = Math.abs(valorHoraFinalCombo-valorHoraInicialCombo);
		if(valorHoraInicialCombo == 22 && valorHoraFinalCombo==0){
			tiempo =2;
		}
		else if(valorHoraInicialCombo == 23 && valorHoraFinalCombo==0){
			tiempo = 1;
		}
		else if(valorHoraInicialCombo == 23 && valorHoraFinalCombo==1){
			tiempo = 2;
		}
		String nombre = "";
		nombre = comboInstalacion;
		String pista = comboPista;
		String nombre2;
		String pista2;
		for(int i=0; i<poli.getReservas().size(); i++)
		{
			if(!poli.getReservas().get(i).isReservaCentro()){
				if(!poli.getReservas().get(i).getEstado().equals(Reserva.ANULADA)){
					cal.setTime(poli.getReservas().get(i).getFechaReserva());
					int dia = cal.get(Calendar.DAY_OF_MONTH);
					int mes = cal.get(Calendar.MONTH);
					int a�o = cal.get(Calendar.YEAR);
					int horaReservaInicial = cal.get(Calendar.HOUR_OF_DAY);
					int tiempo2 = poli.getReservas().get(i).getTiempoReserva();
					nombre2 = poli.getReservas().get(i).getInstalacion().getTipo();
					pista2 = poli.getReservas().get(i).getInstalacion().getnPista();
					if(a�o == valorA�oCombo && mes == valorMesCombo &&nombre.equals(nombre2) && pista.equals(pista2))
					{
						Date fechaInicioReserva = new Date(a�o-1900,mes,dia,horaReservaInicial,00);
						//System.out.println(fechaInicioReserva.toString());
						cal.add(Calendar.HOUR, tiempo2);
						Date fechaFinalReserva = cal.getTime();
						//System.out.println(fechaFinalReserva.toString());
						
						Date fechaComboInicio = new Date(valorA�oCombo-1900, valorMesCombo, valorDiaCombo, valorHoraInicialCombo, 00);
						//System.out.println(fechaComboInicio.toString());
						Date fechaComboFinal;
						if(valorHoraInicialCombo == 23 || valorHoraInicialCombo == 22){
							//fechaComboFinal =new Date(valorA�oCombo-1900, valorMesCombo, valorDiaCombo, valorHoraFinalCombo, 00);
							cal.setTime(fechaComboInicio);
							cal.add(Calendar.HOUR, tiempo);
							fechaComboFinal = cal.getTime();
							//System.out.println(fechaComboFinal.toString());
						}
						else
						{
							fechaComboFinal =new Date(valorA�oCombo-1900, valorMesCombo, valorDiaCombo, valorHoraFinalCombo, 00);
							//System.out.println(fechaComboFinal.toString());
						}
							if(fechaComboInicio.before(fechaInicioReserva) && fechaInicioReserva.before(fechaComboFinal)){
								return false;
							}
							else if(fechaComboInicio.after(fechaInicioReserva) && fechaComboFinal.before(fechaFinalReserva)){
								return false;
							}
							else if(fechaComboInicio.before(fechaFinalReserva) && fechaComboFinal.after(fechaFinalReserva)){
								return false;
							}
							else if(fechaComboInicio.before(fechaInicioReserva) && fechaComboFinal.after(fechaFinalReserva)){
								return false;
							}
							else if(fechaComboInicio.equals(fechaInicioReserva)){
								return false;
							}
							else if(fechaComboFinal.equals(fechaFinalReserva)){
								return false;
							}
						}
					
					//tiempo que quieres hacer la reserva.
					//tiempo2 es el tiempo de la reserva ya hecha
				}
				
			}
			else{
				if(!poli.getReservas().get(i).getEstado().equals(Reserva.ANULADA)){

					
					cal.setTime(poli.getReservas().get(i).getFechaReserva());
					int dia = cal.get(Calendar.DAY_OF_MONTH);
					int mes = cal.get(Calendar.MONTH);
					int a�o = cal.get(Calendar.YEAR);
					int horaReservaInicial = cal.get(Calendar.HOUR_OF_DAY);
					int tiempo2 = poli.getReservas().get(i).getTiempoReserva();
					nombre2 = poli.getReservas().get(i).getInstalacion().getTipo();
					pista2 = poli.getReservas().get(i).getInstalacion().getnPista();
					if(a�o == valorA�oCombo && mes == valorMesCombo &&nombre.equals(nombre2) && pista.equals(pista2))
					{
						Date fechaInicioReserva = new Date(a�o-1900,mes,dia,horaReservaInicial,00);
						//System.out.println(fechaInicioReserva.toString());
						cal.add(Calendar.HOUR, tiempo2);
						Date fechaFinalReserva = cal.getTime();
						//System.out.println(fechaFinalReserva.toString());
						
						Date fechaComboInicio = new Date(valorA�oCombo-1900, valorMesCombo, valorDiaCombo, valorHoraInicialCombo, 00);
						//System.out.println(fechaComboInicio.toString());
						Date fechaComboFinal;
						if(valorHoraInicialCombo == 23 || valorHoraInicialCombo == 22){
							//fechaComboFinal =new Date(valorA�oCombo-1900, valorMesCombo, valorDiaCombo, valorHoraFinalCombo, 00);
							cal.setTime(fechaComboInicio);
							cal.add(Calendar.HOUR, tiempo);
							fechaComboFinal = cal.getTime();
							//System.out.println(fechaComboFinal.toString());
						}
						else
						{
							fechaComboFinal =new Date(valorA�oCombo-1900, valorMesCombo, valorDiaCombo, valorHoraFinalCombo, 00);
							//System.out.println(fechaComboFinal.toString());
						}
							if(fechaComboInicio.before(fechaInicioReserva) && fechaInicioReserva.before(fechaComboFinal)){
								return false;
							}
							else if(fechaComboInicio.after(fechaInicioReserva) && fechaComboFinal.before(fechaFinalReserva)){
								return false;
							}
							else if(fechaComboInicio.before(fechaFinalReserva) && fechaComboFinal.after(fechaFinalReserva)){
								return false;
							}
							else if(fechaComboInicio.before(fechaInicioReserva) && fechaComboFinal.after(fechaFinalReserva)){
								return false;
							}
							else if(fechaComboInicio.equals(fechaInicioReserva)){
								return false;
							}
							else if(fechaComboFinal.equals(fechaFinalReserva)){
								return false;
							}
						}
				}
				
			}
			
			
		}
		return true;
	}
	

	
	/**
	 * M�todo que comprueba que los dias de los meses sean correctos
	 * febrero no tiene 30 dias
	 * @return
	 */
	public boolean comprobarDiasMes(int dia, int mes){
		if(mes == 3 || mes == 5 || mes == 8 || mes == 10)
		{
			if(dia == 31)
			{
				return false;
			}
			return true;
		}
		else if(mes == 1)
		{
			if(dia > 28)
			{
				return false;
			}
			return true;
		}
		return true;
	}
	
	/**
	 * Comprobar que un campo de texto contenga �nicamente texto
	 */
	@SuppressWarnings("deprecation")
	public boolean comprobarTexto(String texto)
	{
		boolean variable = true;
		for(char c : texto.toCharArray()){
			if(!(Character.isLetter(c) || Character.isSpace(c))){
				variable = false;
			}
		}
		if(variable == false){
			return false;
		}
		else if(texto.toString().length() == 0){
			return false;
		}
		else{
			return true;
		}
	}
	
	/**
	 * M�todo que comprueba que se a�ada un n�mero de tel�fono correcto
	 */
	public boolean comprobarTelefono(String telefono){
		boolean variable = true;
		for(char c : telefono.toCharArray()){
			if(!Character.isDigit(c)){
				variable = false;
			}
		}
		if(variable == false){
			return false;
		}
		else if(telefono.toString().length() != 9){
			return false;
		}
		else{return true;}
	}
	
	/**
	 * M�todo que comprueba que se a�ada un n�mero de tel�fono correcto
	 */
	public boolean comprobarPlazas(String plazas){
		boolean variable = true;
		for(char c : plazas.toCharArray()){
			if(!Character.isDigit(c)){
				variable = false;
			}
		}
		if(variable == false){
			return false;
		}
		else{return true;}
	}
	
	/**
	 * M�todo que comprueba si los campos de texto  est�n vac�os
	 */
	public boolean comprobarCamposTextoVacios(String texto){
		if(texto.length() == 0){
			return false;
		}
		else
		{
			return true;
		}
	}
	
	/**
	 * M�todo que impida reservar una fecha anterior al d�a en el que estamos
	 */
	@SuppressWarnings({ "deprecation" })
	public boolean reservaAnterior(int diaCombo,int mesCombo , int a�oCombo, int horaInicio){
		GregorianCalendar calendar = new GregorianCalendar();
		int hora = calendar.get(Calendar.HOUR_OF_DAY);
		int dia = calendar.get(Calendar.DAY_OF_MONTH);
		int mes = calendar.get(Calendar.MONTH);
		int a�o = calendar.get(Calendar.YEAR);
		Date fechaCombo = new Date(a�oCombo-1900,mesCombo,diaCombo,horaInicio,00);
		Date fechaHoy = new Date(a�o-1900,mes,dia,hora,00);
		if(fechaCombo.before(fechaHoy) || fechaCombo.equals(fechaHoy))
		{
			return false;
		}
		return true;

	}
	
	/**
	 * Comprueba que el DNI sea correcto
	 * @param nif
	 * @return true si lo es, false si no
	 */
	public boolean DNI(String nif){
		for(int i=0; i<nif.length(); i++)
		{
			char cad = nif.charAt(i);	//charAt te devuelve el caracter que se encuentra en la posici�n i.
			if(i<8)
			{
				if(cad != '0' && cad != '1' &&cad != '2' &&cad != '3' &&cad != '4' &&cad != '5' &&cad != '6' &&cad != '7' &&cad != '8' &&cad != '9')
				{
					return false;
				}
			}
			if(i==8)
			{
				if( cad == '0' || cad == '1' ||cad == '2' ||cad == '3' ||cad == '4' ||cad == '5' ||cad == '6' ||cad == '7' ||cad == '8' ||cad == '9')
				{
					return false;
				}
			}
			if(nif.length() != 9)
			{
				return false;
			}
		}
		return true;
	}
	
	/**
	 * M�todo para evitar reservar mas de 7 d�as de antelaci�n. 
	 */
	@SuppressWarnings({ "deprecation" })
	public boolean reservarSieteDias(int a�oCombo, int mesCombo, int diaCombo, int horaCombo){
		Calendar cal = Calendar.getInstance();
		Date date = new Date((a�oCombo-1900),mesCombo, diaCombo, horaCombo, 00);
		cal.add(Calendar.DATE, 7);
		int dia = cal.get(Calendar.DAY_OF_MONTH);
		int hora = cal.get(Calendar.HOUR_OF_DAY);
		int mes = cal.get(Calendar.MONTH);
		int a�o = cal.get(Calendar.YEAR);
		Date date2 = new Date(a�o-1900,mes,dia,hora, 00);
		if(date.after(date2))
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	
	
	/**
	 * M�todo para comprobar que el socio no tiene otra reserva a esa misma hora.
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public boolean socioConReserva(Polideportivo poli, int numeroSocioTexto, int horaComboInicio, int horaComboFinal,
			int mesCombo, int diaCombo, int a�oCombo){
		Calendar cal = Calendar.getInstance();
		int tiempo = Math.abs(horaComboFinal-horaComboInicio);
		if(horaComboInicio == 22 && horaComboFinal==0){
			tiempo =2;
		}
		else if(horaComboInicio == 23 && horaComboFinal==0){
			tiempo = 1;
		}
		else if(horaComboInicio == 23 && horaComboFinal==1){
			tiempo = 2;
		}
		//Date fechaCombo = new Date((a�oCombo-1900),mesCombo,diaCombo,horaComboInicio,00);
		//cal.setTime(fechaCombo);
		for(int i=0; i<poli.getReservas().size(); i++){
				if(poli.getReservas().get(i).getCliente() instanceof Socio)
				{
					if(((Socio)poli.getReservas().get(i).getCliente()).getNumeroSocio() == numeroSocioTexto){
						if(!poli.getReservas().get(i).getEstado().equals(Reserva.ANULADA)){
							cal.setTime(poli.getReservas().get(i).getFechaReserva());
							int dia = cal.get(Calendar.DAY_OF_MONTH);
							int mes = cal.get(Calendar.MONTH);
							int a�o = cal.get(Calendar.YEAR);
							int horaReservaInicial = cal.get(Calendar.HOUR_OF_DAY);
							int tiempo2 = poli.getReservas().get(i).getTiempoReserva();
							if(a�o == a�oCombo && mes == mesCombo)
							{
								Date fechaInicioReserva = new Date(a�o-1900,mes,dia,horaReservaInicial,00);
								cal.add(Calendar.HOUR, tiempo2);
								Date fechaFinalReserva = cal.getTime();
								Date fechaComboInicio = new Date(a�oCombo-1900, mesCombo, diaCombo, horaComboInicio, 00);
								Date fechaComboFinal;
								if(horaComboInicio == 23 || horaComboInicio == 22){
									cal.setTime(fechaComboInicio);
									cal.add(Calendar.HOUR, tiempo);
									fechaComboFinal = cal.getTime();
								}
								else
								{
									fechaComboFinal =new Date(a�oCombo-1900, mesCombo, diaCombo, horaComboFinal, 00);
								}
								if(fechaComboInicio.before(fechaInicioReserva) && fechaInicioReserva.before(fechaComboFinal)){
									return false;
								}
								else if(fechaComboInicio.after(fechaInicioReserva) && fechaComboFinal.before(fechaFinalReserva)){
									return false;
								}
								else if(fechaComboInicio.before(fechaFinalReserva) && fechaComboFinal.after(fechaFinalReserva)){
									return false;
								}
								else if(fechaComboInicio.before(fechaInicioReserva) && fechaComboFinal.after(fechaFinalReserva)){
									return false;
								}
								else if(fechaComboInicio.equals(fechaInicioReserva)){
									return false;
								}
								else if(fechaComboFinal.equals(fechaFinalReserva)){
									return false;
								}
							}
						}
						
					}
				}
			//}
			
		}
		return true;
	}
	
	/**
	 * Comprueba la disponibilidad de una instalaci�n en una fecha determinada
	 * @param fechaInicialInstalacion
	 * @param fechaFinalInstalacion
	 * @param fechaI
	 * @param fechaF
	 * @return
	 */
	public boolean comprobarDisponibilidad(Date fechaInicialInstalacion, Date fechaFinalInstalacion, Date fechaI, Date fechaF){
		//Caso1: fechaI y fechaF dentro de los limites.
		if(fechaI.after(fechaInicialInstalacion) && fechaF.before(fechaFinalInstalacion)){
			return false;
		}
		//Caso2: 
		else if(fechaI.before(fechaInicialInstalacion) && fechaF.after(fechaInicialInstalacion)){
			return false;
		}
		//Caso3:
		else if(fechaI.before(fechaFinalInstalacion) && fechaF.after(fechaFinalInstalacion)){
			return false;
		}
		//Caso4:
		else if(fechaI.equals(fechaInicialInstalacion) || fechaF.equals(fechaFinalInstalacion)){
			return false;
		}
		return true;
	}
	
	
	
	/**
	 * M�todo que comprobar� que si se ha eliminado una reserva debido a cuestiones metereol�gicas 
	 * o por necesidad del centro, que no se permita hacer una reserva en la instalaci�n y fecha de la reserva anulada.
	 * @param poli
	 * @param reserva
	 * @return true si se puede reservar, false en caso contrario.
	 */
	@SuppressWarnings("deprecation")
	public boolean NoReservaPosible(Polideportivo poli, Reserva reserva){
		Calendar cal = Calendar.getInstance();
		//Recorremos todas las reservas
		for(int i=0; i<poli.getReservas().size(); i++){
			if(!poli.getReservas().get(i).isReservaCentro()){
				//Nos quedamos con las reservas anuladas
				if(poli.getReservas().get(i).getEstado().equals(Reserva.ANULADA)){
					if(poli.getReservas().get(i).getMotivoAnulacion() != null){
						//Nos quedamos con aquellas reservas cuya anulaci�n fue debido a metereolog�a o necesidad centro
						if(poli.getReservas().get(i).getMotivoAnulacion().equals("METEREOLOGIA") || 
								poli.getReservas().get(i).getMotivoAnulacion().equals("NECESIDAD_CENTRO")){
							//Comprobamos que sea la misma instalaci�n
							if(poli.getReservas().get(i).getInstalacion().getIdPista().equals(reserva.getInstalacion().getIdPista())){
								//Comprobamos que las fechas sean iguales.
								
								//Fecha reserva lista
								cal.setTime(poli.getReservas().get(i).getFechaReserva());
								int dia = cal.get(Calendar.DAY_OF_MONTH);
								int mes = cal.get(Calendar.MONTH);
								int a�o = cal.get(Calendar.YEAR);
								int horaReservaInicial = cal.get(Calendar.HOUR_OF_DAY);
								int tiempo2 = poli.getReservas().get(i).getTiempoReserva();
								
								//Fecha nueva reserva
								cal.setTime(reserva.getFechaReserva());
								int diaNueva = cal.get(Calendar.DAY_OF_MONTH);
								int mesNueva = cal.get(Calendar.MONTH);
								int a�oNueva = cal.get(Calendar.YEAR);
								int horaReservaInicialNueva = cal.get(Calendar.HOUR_OF_DAY);
								int tiempo2Nueva = reserva.getTiempoReserva();
								
								cal.setTime(poli.getReservas().get(i).getFechaReserva());
								
								if(a�o == a�oNueva && mes == mesNueva)
								{
									Date fechaInicioReserva = new Date(a�o-1900,mes,dia,horaReservaInicial,00);
									cal.add(Calendar.HOUR, tiempo2);
									Date fechaFinalReserva = cal.getTime();
									Date fechaNuevaInicio = new Date(a�oNueva-1900, mesNueva, diaNueva, horaReservaInicialNueva, 00);
									Date fechaNuevaFinal;
									if(horaReservaInicialNueva == 23 || horaReservaInicialNueva == 22){
										cal.setTime(fechaNuevaInicio);
										cal.add(Calendar.HOUR, tiempo2Nueva);
										fechaNuevaFinal = cal.getTime();
									}
									else
									{
										fechaNuevaFinal =new Date(a�oNueva-1900, mesNueva, diaNueva, horaReservaInicialNueva+tiempo2Nueva, 00);
									}
									if(fechaNuevaInicio.before(fechaInicioReserva) && fechaInicioReserva.before(fechaNuevaFinal)){
										return false;
									}
									else if(fechaNuevaInicio.after(fechaInicioReserva) && fechaNuevaFinal.before(fechaFinalReserva)){
										return false;
									}
									else if(fechaNuevaInicio.before(fechaFinalReserva) && fechaNuevaFinal.after(fechaFinalReserva)){
										return false;
									}
									else if(fechaNuevaInicio.before(fechaInicioReserva) && fechaNuevaFinal.after(fechaFinalReserva)){
										return false;
									}
									else if(fechaNuevaInicio.equals(fechaInicioReserva)){
										return false;
									}
									else if(fechaNuevaFinal.equals(fechaFinalReserva)){
										return false;
									}
								}
							}
						}
					}
					
						
				}
			}
		}
		return true;
	}
	
	public static boolean isNumeric(String cad) {
		for(int i=0;i<cad.length();i++) {
			if(!Character.isDigit(cad.codePointAt(i)))
				return false;
		}
		return true;
		
	}
}
