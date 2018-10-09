package bd;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import logica.Actividad;
import logica.Cliente;
import logica.Comprobaciones;
import logica.Cuota;
import logica.Instalacion;
import logica.NoSocio;
import logica.Pago;
import logica.Recibo;
import logica.Reserva;
import logica.Socio;

public class Conexion { 
	
	static String username = "IPS_2018_4";
	static String password = "trabajoips";
	static String stringConnection = "jdbc:oracle:thin:@156.35.94.99:1521:DESA";
	
	private ArrayList<Reserva> reservas;
	private ArrayList<Instalacion> instalaciones;
	private ArrayList<Cliente> clientes;
	private ArrayList<Pago> pagos;
	private ArrayList<Cuota> cuotas;
	private ArrayList<Actividad> actividades;
	@SuppressWarnings("unused")
	private Recibo recibo;
	
	//Mapa con las instalaciones no disponibles y horarios
	private Map<String, Date[]> mapa;
	//Lista donde guardo la pk de INSTALACION_NO_DISPONIBLE.
	private ArrayList<String> lista;
	
	
	
	

	public Conexion() throws SQLException {
		clientes=leerClientes();
		instalaciones=leerInstalaciones();
		reservas=leerReservas();
 		pagos=leerPagos();
		cuotas=leerCuotas();
		actividades = leerActividades();
		lista = new ArrayList<String>();
		mapa = cargarNoDisponibles();
		recibo = new Recibo();
		
	}
	
	private  Connection getConnection() throws SQLException{
		if(DriverManager.getDriver(stringConnection)== null)
		if(stringConnection.contains("oracle"))
			DriverManager.registerDriver(new oracle.jdbc.OracleDriver()); 
		else
			DriverManager.registerDriver(new org.hsqldb.jdbc.JDBCDriver()); 
		return DriverManager.getConnection(stringConnection,username,password);
	}
	
	public ArrayList<Cliente> leerClientes() throws SQLException{
		ArrayList<Cliente> clientes = new ArrayList<Cliente>();
		System.out.println("Obteniendo lista de clientes de la BD...");
		Connection con;
			con = getConnection();
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery("select * from CLIENTES");
			while(rs.next()){ 
				String dni = rs.getString("DNI");
				int num_socio = rs.getInt("NUM_SOCIO");
				String nombre = rs.getString("NOMBRE");
				String apellido = rs.getString("APELLIDOS");
				String telefono = rs.getString("TELEFONO");
				String usuario = rs.getString("USUARIO");
				String contraseña = rs.getString("CONTRASEÑA");
				Date fecha = rs.getTimestamp("FECHA_REGISTRO");
				String es_socio = rs.getString("SOCIO");
				if(es_socio.equals("True")){
					clientes.add(new Socio(nombre, apellido, dni, telefono,num_socio, usuario,
							contraseña, fecha));
//					System.out.println("nombre: " + nombre + " apellido: " + apellido + " dni: " + dni +
//							" telefono: " + telefono + " num_socio: " + num_socio + " usuario: " + usuario
//							+ " contraseña: " + contraseña);
				}
				else {
					clientes.add(new NoSocio(nombre, apellido, dni, telefono));
//					System.out.print("nombre: " + nombre + " apellido: " + apellido + " dni: " + dni +
//							" telefono: " +   telefono);
				}
			}
			rs.close();
			st.close();
			con.close();
			return clientes;
	}
	
	public ArrayList<Instalacion> leerInstalaciones() throws SQLException{
		ArrayList<Instalacion> instalaciones = new ArrayList<Instalacion>();
		System.out.println("Obteniendo lista de instalaciones de la BD...");
		Connection con;
			con = getConnection();
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery("select * from INSTALACIONES");
			while(rs.next()){ 
				String id_pista = rs.getString("ID_PISTA");
				String num_pista = rs.getString("NUM_PISTA");
				String descripcion = rs.getString("DESCRIPCION");
				String tipo = rs.getString("TIPO");
				float precioHora = rs.getFloat("PRECIO_HORA");
				boolean disponible = (rs.getString("DISPONIBLE").equals("True")?true:false);
				instalaciones.add(new Instalacion(id_pista, tipo, num_pista, descripcion,precioHora, disponible));
//				System.out.println("id_pista: " + id_pista + " num_pista: " + num_pista +
//						" descripcion: " + descripcion + " tipo: " + tipo);
			}
			rs.close();
			st.close();
			con.close();
			return instalaciones;
	}
	
	public ArrayList<Reserva> leerReservas() throws SQLException{
		ArrayList<Reserva> reservas = new ArrayList<Reserva>();
		ArrayList<String> idCad = new ArrayList<String>();
		System.out.println("Obteniendo lista de reservas de la BD...");
		Connection con;
			con = getConnection();
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery("select * from RESERVAS order by fecha_reserva");
			while(rs.next()){ 
				String id_reserva = rs.getString("ID_RESERVA");
				String id_pista = rs.getString("ID_PISTA");
				String dni = rs.getString("DNI");
				Date fecha_reserva = rs.getTimestamp("FECHA_RESERVA");;
				int tiempo_reserva = rs.getInt("DURACION");
				String estado = rs.getString("ESTADO").toUpperCase();
				Date hora_entrada = rs.getTimestamp("HORA_ENTRADA");
				Date hora_salida = rs.getTimestamp("HORA_SALIDA");
				String reserva_centro = rs.getString("RESERVA_CENTRO");
				String tipo_actividad = rs.getString("TIPO_ACTIVIDAD");
				boolean pagada=(rs.getString("PAGADA")=="True"?true:false);
				String motivoAnulacion = rs.getString("MOTIVO_ANULACION");
				Instalacion instalacion = obtenerInstalacion(id_pista, instalaciones);
				Calendar c1 = Calendar.getInstance(); c1.setTime(fecha_reserva);c1.add(Calendar.HOUR_OF_DAY, tiempo_reserva);
				if(c1.getTime().before(new Date())) {
					if(estado.equals("LIBRE")) {
						estado = "CADUCADA";
						idCad.add(id_reserva);
					}
				}
				if(reserva_centro.equals("True")) {
					reservas.add(new Reserva(instalacion, fecha_reserva, tiempo_reserva, tipo_actividad, id_reserva,estado));
				}else {
					Cliente cliente = obtenerCliente(dni, clientes);
					reservas.add(new Reserva(cliente, instalacion, fecha_reserva, tiempo_reserva, estado, hora_entrada, hora_salida,id_reserva,pagada, motivoAnulacion));
				}
				
			}
			rs.close();
			st.close();
			con.close();
			
			//******************** ACTUALIZAR CADUCADAS BD **********************
			actualizarEstadoCaducada(idCad);
			
			return reservas;
	}
	
	public void actualizarEstadoCaducada(ArrayList<String> cad) throws SQLException{
		Connection con = getConnection();
		for(String r: cad) {
			PreparedStatement ps =  con.prepareStatement("update RESERVAS set ESTADO='CADUCADA' where ID_RESERVA=?");
			ps.setString(1,r);
			ps.executeUpdate();
			ps.close();
		}
		con.close();
	}
	
	private Reserva obtenerReservabyId(String idReserva) {
		for (Reserva r:getReservas()) {
			if(r.getIdReserva().equals(idReserva))
					return r;
		}
		return null;
	}
	
	public Cliente obtenerCliente(String dni, ArrayList<Cliente> clientes){
		for(int i=0; i<clientes.size(); i++){
			if(dni.equals(clientes.get(i).getDNI())){
				return clientes.get(i);
			}
		}
		return null;
	}
	
	private Instalacion obtenerInstalacion(String id_pista, ArrayList<Instalacion> inst){
		for(Instalacion i : inst){
			if(id_pista.equals(i.getIdPista())){
				return i;
			}
		}
		return null;
	}
	
	public void actualizarReserva(int dia, int mes, int ano, int horaInicio, int horaFinal, String dni,
			String textInstalacion, String pista, Comprobaciones c, boolean pagado) throws SQLException{
		Calendar cal = Calendar.getInstance();
		int tiempo;
		if(horaInicio == 22 && horaFinal == 0){
			tiempo = 2;
		}
		else if(horaInicio == 23 && horaFinal == 1){
			tiempo = 2;
		}
		else if(horaInicio == 23 && horaFinal == 0){
			tiempo = 1;
		}
		else
		{
			tiempo = Math.abs(horaFinal-horaInicio);
		} 
		@SuppressWarnings("deprecation")
		Date date = new Date(ano -1900,mes, dia, horaInicio, 00);
		String pagada=(pagado?"True":"False");
		Timestamp sqlDate = new java.sql.Timestamp(date.getTime());
		
		cal.setTime(date);
		Connection con = getConnection();
		String idReserva = obtenerIdReserva(getReservas());
		String idPista="";
		idPista = obtenerIdPista(getInstalaciones(), textInstalacion, pista);
		
			PreparedStatement ps = con.prepareStatement("INSERT INTO RESERVAS (ID_RESERVA, ID_PISTA, DNI, FECHA_RESERVA, DURACION, ESTADO,"
					+ "HORA_ENTRADA, HORA_SALIDA, RESERVA_CENTRO, TIPO_ACTIVIDAD, PAGADA, MOTIVO_ANULACION) "
					+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?)");
			ps.setString(1, idReserva);
			ps.setString(2, idPista);
			ps.setString(3, dni);
			ps.setTimestamp(4, sqlDate);
			ps.setInt(5, tiempo);
			ps.setString(6, Reserva.LIBRE);
			ps.setDate(7, null);
			ps.setDate(8, null);
			ps.setString(9, "False");
			ps.setString(10, "");
			ps.setString(11, pagada);
			ps.setString(12, null);
			ps.executeUpdate();
			System.out.println("Base de datos actualizada");
			Reserva reserva=new Reserva(getClienteById(dni),getInstalacionById(idPista),date,tiempo,Reserva.LIBRE,null,null,idReserva,pagado,null);
			getReservas().add(reserva);
			//Si la reserva es de cliente y se pasa por cuota (socio) o efectivo (no socio)
			if(!reserva.isReservaCentro() && pagado) {
				
				if(reserva.getCliente() instanceof NoSocio)
					guardarPago(reserva, true);
				else 
					guardarPago(reserva,false);
			}
		
	}
	
	
	/*############### OPERACIONES CON PAGOS Y CUOTAS ###############*/
	
	public ArrayList<Pago> leerPagos() throws SQLException{
		ArrayList<Pago> pagos = new ArrayList<Pago>();
		System.out.println("Obteniendo lista de pagos de la BD...");
		Connection con;
			con = getConnection();
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery("select * from CONTABILIDAD");
			while(rs.next()){ 
				String id_RESERVA = rs.getString("ID_RESERVA");
				String id_pago = rs.getString("ID_PAGO");
				boolean efectivo = (rs.getString("PAGO_EFECTIVO").equals("True")?true:false);
				boolean liquidada = (rs.getString("LIQUIDADA").equals("True")?true:false);
				
				Reserva r = obtenerReservabyId(id_RESERVA);
				
				pagos.add(new Pago(id_pago,r,efectivo,liquidada));
			}
			return pagos;
	}
	
	/*
	 * Guarda un pago en la base de datos y en el array local de Polideportivo.
	 * El valor de la columna PAGO_EFECTIVO y LIQUIDADA es el mismo porque
	 * si se realiza el pago en efectivo (PAGO_EFECTIVO='True') este queda 
	 * liquidado (LIQUIDADA='True') y si se pasa por cuota no.
	 */
	public void guardarPago(Reserva reserva, boolean efectivo) {
		
		String id_pago= generarIdPago();
		String id_reserva=reserva.getIdReserva();
		String pago_efectivo=(efectivo?"True":"False");
		float importe=reserva.getPrecioReserva();
		
		try {
		Connection con = getConnection();
		PreparedStatement ps = con.prepareStatement("INSERT INTO CONTABILIDAD  VALUES (?,?,?,?,?)");
		ps.setString(1, id_pago);
		ps.setString(2, id_reserva);
		ps.setString(3, pago_efectivo);
		ps.setFloat(4, importe);
		ps.setString(5, pago_efectivo);
		ps.executeUpdate();
		ps.close();
		con.close();
		
		System.out.println("Pago guardado en la base de datos");
		
		pagos.add(new Pago(id_pago,reserva,efectivo,efectivo));
		Recibo.generarRecibo(reserva);
		}
		catch (SQLException e) {
			System.err.println("Error al añadir el pago a la base de datos");
			e.printStackTrace();
		}
				
	}
	
	/**
	 * Genera un idPago para cuando se añade un pago a la bd
	 * @return
	 */
	private String generarIdPago() {
		int max=0;
		for(Pago pago:pagos) {
			max=(max>Integer.parseInt(pago.getIdPago())?max:Integer.parseInt(pago.getIdPago()));
		}
		return String.valueOf(max+1);
	}
	
	/*
	 * Dado un array de pagos, actualiza la base de datos para 
	 * reflejar que ya han sido liquidados.
	 */
	public void actualizarPagosLiquidados(ArrayList<Pago> pagosLiquidados) {
		Connection con;
		try {
			con = getConnection();
			
			PreparedStatement ps =  con.prepareStatement("UPDATE CONTABILIDAD SET LIQUIDADA='True' WHERE ID_PAGO=?");
			for(Pago pago: pagosLiquidados) {
				ps.setString(1, pago.getIdPago());	
				ps.executeUpdate();
			}
			ps.close();
			con.close();
		} catch (SQLException e) {
			System.err.print("Error al actualizar los pagos liquidados en la bd.");
			e.printStackTrace();
		}
		
	}
	
	public ArrayList<Cuota> leerCuotas(){
		ArrayList<Cuota> cuotas = new ArrayList<Cuota>();
		System.out.println("Obteniendo lista de cuotas de la BD...");
		Connection con;
			try {
				con = getConnection();
			
				Statement st = con.createStatement();
				ResultSet rs = st.executeQuery("select * from CUOTA");
				while(rs.next()){ 
					String id_cuota = rs.getString("ID_CUOTA");
					String dni = rs.getString("DNI");
					float importe= rs.getFloat("IMPORTE");
					
					Socio sc = (Socio) obtenerCliente(dni, getClientes());
					
					cuotas.add(new Cuota(sc, id_cuota, importe));
				
				}
				rs.close();
				st.close();
				con.close();
			} catch (SQLException e) {
				System.err.println("Error al leer los datos sobre las cuotas de la base de datos.");
				e.printStackTrace();
			}
			return cuotas;
	}
	
	/*
	 * Dada una cuota actualiza el valor de su importe en la bd.
	 */
	public void actualizarCuota(Cuota cuota) {
		Connection con;
		try {
			con = getConnection();
		
			PreparedStatement ps =  con.prepareStatement("UPDATE CUOTA SET IMPORTE=? WHERE ID_CUOTA=? and DNI=?");
			ps.setFloat(1, cuota.getImporte());
			ps.setString(2, cuota.getId_cuota());
			ps.setString(3, cuota.getSocio().getDNI());
			ps.executeUpdate();
			ps.close();
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void agregarCuota(Cuota nCuota) {
		try {
			Connection con = getConnection();
			PreparedStatement ps = con.prepareStatement("INSERT INTO CUOTA  VALUES (?,?,?)");
			ps.setString(1, nCuota.getId_cuota());
			ps.setString(2, nCuota.getSocio().getDNI());
			ps.setFloat(3, nCuota.getImporte());
			ps.executeUpdate();
			ps.close();
			con.close();
			
			System.out.println("Nueva entrada de cuota guardada en la base de datos");
			
			}
			catch (SQLException e) {
				System.err.println("Error al añadir la cuota a la base de datos");
				e.printStackTrace();
			}
		
	}

	
	/*############### FIN DE OPERACIONES CON PAGOS Y CUOTAS ###############*/
	
	public int generarCodMismaActividad() throws SQLException {
		int cod_misma_actividad = 1;
		Connection con = getConnection(); Statement st = con.createStatement(); ResultSet rs = st.executeQuery("select max(cod_misma_actividad) as maximo from se_realiza_en");
		while(rs.next()){ 
			if(rs.getInt("maximo")!=0) {
				int valueBD = rs.getInt("maximo") + 1;
				cod_misma_actividad = valueBD;
			}
		}
		rs.close();st.close();con.close();
		return cod_misma_actividad;
	}
	
	public void añadirReserva(String idReserva, String idPista, Date fechaReserva, int tiempoReserva, String actividad, String codAct, boolean esActividad) throws SQLException {
		Connection con = getConnection();
		Timestamp sqlDate = new java.sql.Timestamp(fechaReserva.getTime());
		PreparedStatement ps = con.prepareStatement("INSERT INTO RESERVAS (ID_RESERVA, ID_PISTA, DNI, FECHA_RESERVA, DURACION, ESTADO,"
				+ "HORA_ENTRADA, HORA_SALIDA, RESERVA_CENTRO, TIPO_ACTIVIDAD) VALUES (?,?,?,?,?,?,?,?,?,?)");
		ps.setString(1, idReserva);
		ps.setString(2, idPista);
		ps.setString(3, "0");
		ps.setTimestamp(4, sqlDate);
		ps.setInt(5, tiempoReserva);
		ps.setString(6, "LIBRE");
		ps.setDate(7, null);
		ps.setDate(8, null);
		ps.setString(9, "True");
		if(esActividad)
			ps.setString(10, actividad);
		else
			ps.setString(10, null);
		ps.executeUpdate();
		System.out.println("Base de datos actualizada");
		ps.close();
		con.close();
		
		
	}
	
	public void añadirReservaParaActividadPeriodica(Actividad actSelected, ArrayList<Reserva> reservas) throws SQLException {
		Connection con = getConnection();
		PreparedStatement ps = con.prepareStatement("INSERT INTO RESERVAS (ID_RESERVA, ID_PISTA, DNI, FECHA_RESERVA, DURACION, ESTADO,"
				+ "HORA_ENTRADA, HORA_SALIDA, RESERVA_CENTRO, TIPO_ACTIVIDAD) VALUES (?,?,?,?,?,?,?,?,?,?)");
		for(Reserva reserva: reservas) {
			Timestamp sqlDate = new java.sql.Timestamp(reserva.getFechaReserva().getTime());
			ps.setString(1, reserva.getIdReserva());
			ps.setString(2, reserva.getInstalacion().getIdPista());
			ps.setString(3, "0");
			ps.setTimestamp(4, sqlDate);
			ps.setInt(5, reserva.getTiempoReserva());
			ps.setString(6, "LIBRE");
			ps.setDate(7, null);
			ps.setDate(8, null);
			ps.setString(9, "True");
			ps.setString(10, actSelected.getNombreActividad());
			ps.executeUpdate();
		}
		System.out.println("Base de datos actualizada");
		ps.close();
		con.close();
	}
	
	private Instalacion getInstalacionById(String id) {
		for(Instalacion i:getInstalaciones()) {
			if(i.getIdPista().equals(id))
				return i;
		}
		return null;
	}
	
	private Cliente getClienteById(String id) {
		for(Cliente c: getClientes()) {
			if(c.getDNI().equals(id))
				return c;
		}
		return null;
	}
	
	/**
	 * Método para obtener el id_Reserva 
	 * @param reservas
	 * @return
	 */
	public String obtenerIdReserva(ArrayList<Reserva> reservas){
		int max = 0;
		int id=0;
		for(int i = 0; i < reservas.size(); i++){
			 id=Integer.parseInt(reservas.get(i).getIdReserva());
			if( id>= max)
				max = Integer.parseInt(reservas.get(i).getIdReserva());
		}
		return String.valueOf(max + 1);
	}
	
	
	/**
	 * Obtener el id_pista a reservar
	 * @param instalaciones
	 * @param tipo
	 * @param nPista
	 * @return
	 */
	private String obtenerIdPista(ArrayList<Instalacion>instalaciones, String tipo, String nPista){
		for(int i=0; i<instalaciones.size(); i++){
			if(instalaciones.get(i).getTipo().equals(tipo) && instalaciones.get(i).getnPista().equals(nPista)){
				return instalaciones.get(i).getIdPista();
			}
		}
		return null;
	}
	
	public void actualizarOcupadoFinalizadoBD(String parametro1, String parametro2, String dni, String idPista, Date fechaReserva, Date hora) throws SQLException {
		Timestamp sqlhora = new java.sql.Timestamp(hora.getTime());
		Connection con = getConnection();
		Timestamp sqlfechaReserva = new java.sql.Timestamp(fechaReserva.getTime());
		PreparedStatement ps =  con.prepareStatement("update RESERVAS set " + parametro1 +"=?, ESTADO=? where ID_PISTA=? and DNI=? and FECHA_RESERVA=?");
		ps.setTimestamp(1,sqlhora); 
		ps.setString(2, parametro2);
		ps.setString(3,idPista);
		ps.setString(4,dni);
		ps.setTimestamp(5,sqlfechaReserva);
		ps.executeUpdate();
		ps.close();
		con.close();
	}
	
	public void actualizarCaducadoBD(String idReserva) throws SQLException {
		Connection con = getConnection();
		PreparedStatement ps =  con.prepareStatement("update RESERVAS set ESTADO='CADUCADA' where ID_RESERVA=?");
		ps.setString(1,idReserva);
		ps.executeUpdate();
		ps.close();
		con.close();
	}
	
	public void actualizarPagoReserva(boolean pagado, String idReserva) {
		String estadoPago=(pagado?"True":"False");
		try {
			Connection con = getConnection();
			PreparedStatement ps =  con.prepareStatement("update RESERVAS set PAGADA=? where ID_RESERVA=?");
			ps.setString(1,estadoPago);
			ps.setString(2, idReserva);
			ps.executeUpdate();
			ps.close();
			con.close();
		}
		catch(SQLException e) {
			System.err.println("Error al actualizar el estado del pago de la reserva");
			e.printStackTrace();
		}
			
	}
	
	public void guardarClienteNoSocioBD(String nombre, String apellidos, String dni, String telefono) throws SQLException {
		Connection con = getConnection();
		PreparedStatement ps = con.prepareStatement("INSERT INTO CLIENTES (DNI, NUM_SOCIO, NOMBRE, APELLIDOS, TELEFONO, FECHA_REGISTRO, USUARIO, CONTRASEÑA, SOCIO) VALUES (?,?,?,?,?,?,?,?,?)");
		ps.setString(1, dni);
		ps.setString(2, null);
		ps.setString(3, nombre);
		ps.setString(4, apellidos);
		ps.setString(5, telefono);
		ps.setTimestamp(6, null);
		ps.setString(7, null);
		ps.setString(8, null);
		String f = "False";
		ps.setString(9, f);
		ps.executeUpdate();
		ps.close();
		con.close();
	}
	
	/**
	 * El socio/administrador anula su reserva 
	 * Devuelve true si se anula y false en caso contrario en MisReservaVentana y AdministradorCancelaReservaVentana
	 * @param poli
	 * @param table
	 * @return
	 * @throws ParseException
	 * @throws SQLException
	 * @throws IOException 
	 */
	@SuppressWarnings({ "static-access", "deprecation" })
	public boolean anularReserva(String fechaTabla, String instalacion, String pista) throws ParseException, SQLException, IOException{
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd hh:mm");
		Date fecha = formato.parse(fechaTabla);
		String idReserva="";
		cal.add(Calendar.DATE, 1);//Adelantamos 1 día la fecha.
		//Cogemos la fecha de ahora
		int dia = cal.get(Calendar.DAY_OF_MONTH);
		int mes = cal.get(Calendar.MONTH);
		int año = cal.get(Calendar.YEAR);
		int hora = cal.get(Calendar.HOUR_OF_DAY);
		Date date = new Date(año-1900,mes,dia,hora,00);
		
		Connection con = getConnection();
		PreparedStatement ps = con.prepareStatement("update RESERVAS set ESTADO=?, MOTIVO_ANULACION=? "
				+ "where ID_RESERVA=?");
		
		for(int i=0; i<getReservas().size(); i++){
			if(!getReservas().get(i).isReservaCentro()){
				if(!getReservas().get(i).getEstado().equals(Reserva.ANULADA)){
					cal.setTime(getReservas().get(i).getFechaReserva());
					int diaReserva = cal.get(Calendar.DAY_OF_MONTH);
					int mesReserva = cal.get(Calendar.MONTH);
					int añoReserva = cal.get(Calendar.YEAR);
					int horaReserva = cal.get(Calendar.HOUR_OF_DAY);
					Date fecha2 = new Date(añoReserva-1900, mesReserva, diaReserva, horaReserva,00);
					//Date fecha2 = formato.parse(poli.getReservas().get(i).getFechaReserva().toString());
					if(fecha2.equals(fecha) && getReservas().get(i).getInstalacion().getTipo().equals(instalacion)
							&& getReservas().get(i).getInstalacion().getnPista().equals(pista)){
						if(fecha2.before(date) == true || fecha2.equals(date) == true){
							return false;
						}
						else{										
							idReserva = getReservas().get(i).getIdReserva();						
							ps.setString(1, Reserva.ANULADA);
							ps.setString(2, "PETICION PROPIA");
							ps.setString(3, idReserva);					
							getReservas().get(i).setMotivoAnulacion("PETICION PROPIA");
							getReservas().get(i).setEstado(Reserva.ANULADA);
							Recibo.generarReciboNoSocio(getReservas().get(i));
							ps.executeUpdate();
							ps.close();
							con.close();
							return true;
						}
					}
				}
			}	
		}	
		return false;
	}
	
	
	/**
	 * El administrador anula las reserva debido a metereología o necesidad del centro.
	 * @param poli
	 * @param tabla
	 * @throws SQLException 
	 * @throws ParseException 
	 * @throws IOException 
	 */
	@SuppressWarnings("static-access")
	public boolean anularReservaSinCoste(String idReserva, boolean tipo) throws SQLException, ParseException, IOException{
		Connection con = getConnection();
		PreparedStatement ps = con.prepareStatement("update RESERVAS set ESTADO=?, MOTIVO_ANULACION=? "
				+ "where ID_RESERVA=?");
		for(int i=0; i<getReservas().size(); i++){
			if(!getReservas().get(i).isReservaCentro()){
				if(!getReservas().get(i).getEstado().equals(Reserva.ANULADA)){
					if(getReservas().get(i).getIdReserva().equals(idReserva)){
						ps.setString(1, Reserva.ANULADA);
						if(tipo==true){
							ps.setString(2, "METEREOLOGIA");
							getReservas().get(i).setMotivoAnulacion("METEREOLOGIA");
						}
						else{
							ps.setString(2,"NECESIDAD_CENTRO");
							getReservas().get(i).setMotivoAnulacion("NECESIDAD_CENTRO");
						}
						ps.setString(3, idReserva);
						getReservas().get(i).setEstado(Reserva.ANULADA);
						Recibo.generarReciboNoSocio(getReservas().get(i));
						ps.executeUpdate();
						ps.close();
						con.close();
						return true;
					}
				}
			}
		}
		return false;
	}
	
//--------------------------HISTORIA 49-----------------------------//	
	/**
	 * Anular un conjunto de reservas.
	 * @param reservas
	 * @param motivo
	 * @throws SQLException
	 */
	public void anularConjuntoReservas(ArrayList<String> reservas, String motivo) throws SQLException{
		Connection con = getConnection();
		PreparedStatement ps = con.prepareStatement("update RESERVAS set ESTADO=?, MOTIVO_ANULACION=? "
				+ "where ID_RESERVA=?");
		for(int i=0; i<reservas.size(); i++){
			ps.setString(1, Reserva.ANULADA);
			ps.setString(2, motivo);
			ps.setString(3, reservas.get(i));
			ps.executeUpdate();
		}
		ps.close();
		con.close();
	}
	
	/**
	 * Añade una a instalación a no disponible.
	 * @param fechaInicio
	 * @param fechaFin
	 * @param idPista
	 * @throws SQLException
	 */
	public void anularInstalacion(Date fechaInicio, Date fechaFin, String idPista) throws SQLException{
		Date date [] = new Date[2];
		date[0]=fechaInicio;
		date[1]=fechaFin;
		Connection con = getConnection();
		Timestamp sqlDate = new java.sql.Timestamp(fechaInicio.getTime());
		Timestamp sqlDate2 = new java.sql.Timestamp(fechaFin.getTime());
		PreparedStatement ps = con.prepareStatement("INSERT INTO INSTALACION_NO_DISPONIBLE (ID_PISTA_NO_DISPONIBLE, HORA_INICIO, HORA_FIN, ID_PISTA) "
				+ "VALUES (?,?,?,?)");
		String id = obtenerIdInstalacionNoDisponible(lista);
		ps.setString(1, id);
		ps.setTimestamp(2, sqlDate);
		ps.setTimestamp(3, sqlDate2);
		ps.setString(4, idPista);
		ps.executeUpdate();
		ps.close();
		con.close();
		//Una vez actualizada la BBDD tenemos que actualizar en local la lista de pks y el map
		lista.add(id);
		mapa.put(id, date);
		System.out.println("Base de datos actualizada, la instalación ha quedado marcada como no disponible");
	}
	
	/**
	 * Método para obtener la pk de la instalación no disponible más alta 
	 * @param claves
	 * @return
	 */
	private String obtenerIdInstalacionNoDisponible(ArrayList<String> claves){
		int max = 0;
		int id=0;
		for(int i = 0; i < claves.size(); i++){
			 id=Integer.parseInt(claves.get(i));
			if( id>= max)
				max = Integer.parseInt(claves.get(i));
		}
		return String.valueOf(max + 1);
	}
	
	/**
	 * Añade al mapa las instalaciones no disponibles
	 * También guarda en una lista las pk para luego poder añadir una instalacion no disponible.
	 * @return
	 * @throws SQLException
	 */
	public Map<String, Date[]> cargarNoDisponibles() throws SQLException{
		System.out.println("Obteniendo instalaciones no disponibles...");
		Map<String, Date[]> mapa = new HashMap<String, Date[]>();
		Connection con = getConnection();
		Statement st = con.createStatement();
		ResultSet rs = st.executeQuery("SELECT * FROM INSTALACION_NO_DISPONIBLE");
		while(rs.next()){
			String pk = rs.getString("ID_PISTA_NO_DISPONIBLE");
			Date fechaInicio = rs.getTimestamp("HORA_INICIO");
			Date fechaFinal = rs.getTimestamp("HORA_FIN");
			Date [] date = new Date [2];
			date[0] = fechaInicio;
			date[1] = fechaFinal;
			mapa.put(pk, date);
			lista.add(pk);
		}	
		return mapa;
	}
	
	/**
	 * Obtengo las claves primarias de INSTALACION_NO_DISPONIBLE
	 * @param idInstalacion
	 * @return
	 * @throws SQLException
	 */
	public ArrayList<String> obtenerPks(String idInstalacion) throws SQLException{
		ArrayList<String> lista = new ArrayList<String>();
		Connection con = getConnection();
		PreparedStatement ps =  con.prepareStatement("SELECT ID_PISTA_NO_DISPONIBLE FROM INSTALACION_NO_DISPONIBLE WHERE ID_PISTA =?");
		ps.setString(1, idInstalacion);
		ResultSet st = ps.executeQuery();
		while(st.next()){ 
			String pk = st.getString("ID_PISTA_NO_DISPONIBLE");
			lista.add(pk);
		}
		ps.close();con.close();
		return lista;
	}
	
	public Map<String, Date[]> getMapa() {
		return mapa;
	}
	
	public ArrayList<String> getLista() {
		return lista;
	}
	
//--------------------------------------------------------------------//	

	public ArrayList<Reserva> getReservas() {
		return reservas;
	}

	public  ArrayList<Instalacion> getInstalaciones() {
		return instalaciones;
	}

	public ArrayList<Cliente> getClientes() {
		return clientes;
	}
	
	public ArrayList<Pago> getPagos(){
		return pagos;
	}
	
	public ArrayList<Cuota> getCuotas(){
		return cuotas;
	}
	
	public ArrayList<Actividad> getActividades(){
		return actividades;
	}
	
	public void cambiarEstadoAnuladaReservaCentro(String idReserva) throws SQLException {
		System.out.println("Cambiado estado a ANULADA en la tabla Reservas a id: " + idReserva);
		Connection con = getConnection(); 
		PreparedStatement ps =  con.prepareStatement("UPDATE Reservas SET ESTADO='ANULADA' WHERE ID_RESERVA =?");
		ps.setString(1,idReserva);
		if(ps.executeUpdate()==1)
			System.out.println("Data successfully introduced!!");
		else
			System.out.println("Some error has ocurred, data not introduced!!");	
		ps.close();
		con.close();
	}
	
	//********************* OPERACIONES DE CARGA DE DATOS ACTIVIDADES *******************************
	
	//Carga las actividades de la base de datos
	public ArrayList<Actividad> leerActividades() throws SQLException{
		ArrayList<Actividad> acts = new ArrayList<Actividad>(); System.out.println("Obteniendo actividades..."); 
		Connection con = getConnection(); Statement st = con.createStatement(); ResultSet rs = st.executeQuery("SELECT * FROM Actividades");
		while(rs.next()){ 
			String codAct = rs.getString("COD_ACTIVIDAD"), nomAct = rs.getString("NOMBRE_ACTIVIDAD"); int numPl = rs.getInt("NUM_PLAZAS"); boolean plazas_limitadas=(rs.getString("PLAZAS_LIMITADAS").equals("True")?true:false);
			acts.add(new Actividad(codAct, nomAct, numPl,plazas_limitadas));
		}
		rs.close();st.close();con.close();
		return acts;
	}
	
	//carga la informacion de las actividades con sus reservas
	public ArrayList<Actividad> cargarInfoReservas() throws SQLException{
		ArrayList<Actividad> acts = new ArrayList<Actividad>(); System.out.println("Obteniendo actividades..."); 
		Connection con = getConnection(); Statement st = con.createStatement(); ResultSet rs = st.executeQuery("SELECT * FROM SE_REALIZA_EN");
		while(rs.next()){ 
			String codAct = rs.getString("COD_ACTIVIDAD"), idRes = rs.getString("ID_RESERVA");
			Actividad act = obtenerActByCod(codAct);
			Reserva r = obtenerReservabyId(idRes);
			if(r.getEstado().equals(Reserva.LIBRE)) {
				act.getApuntadosActividades().put(r, new ArrayList<Socio>());
			}
		}
		rs.close();st.close();con.close();
		cargarDatosDeApuntadosActividad();
		return acts;
	}
		
	//Carga los datos de los usuarios para saber donde estan apuntados....
	public void cargarDatosDeApuntadosActividad() throws SQLException {
 		System.out.println("Obteniendo datos de las actividades..."); Connection con = getConnection(); Statement st = con.createStatement(); ResultSet rs = st.executeQuery("select * from APUNTADO_A");
		while(rs.next()){ 
			if(rs.getString("CANCELADA").equals("False")) {
				String codAct = rs.getString("COD_ACTIVIDAD"), idRes = rs.getString("ID_RESERVA"), dni = rs.getString("DNI");
				Socio sc = (Socio)obtenerCliente(dni,getClientes());
				Actividad actividad = obtenerActByCod(codAct); Reserva res = obtenerReservabyId(idRes); 
				if(actividad.getApuntadosActividades().containsKey(res)) 
					actividad.getApuntadosActividades().get(res).add(sc);
			}
		}
		rs.close();st.close();con.close();
	}

	//********************* OPERACIONES DE GUARDADO DE DATOS ACTIVIDADES *******************************
	
	//-------------------------CREACION DE UNA ACTIVIDAD----------------------------------
	public void crearActividad(String nombre, int plazas, String plazasLimitadas) throws SQLException{
		Connection con = getConnection();
		String id = obtenerIdActividad(actividades);
		PreparedStatement ps = con.prepareStatement("INSERT INTO ACTIVIDADES (COD_ACTIVIDAD, NOMBRE_ACTIVIDAD, NUM_PLAZAS, PLAZAS_LIMITADAS) "
				+ "VALUES (?,?,?,?)");
		ps.setString(1, id);
		ps.setString(2, nombre);
		ps.setInt(3, plazas);
		ps.setString(4, plazasLimitadas);
		ps.executeUpdate();
		ps.close();
		con.close();
		System.out.println("Actividad creada");
		//Las añado en local
		if(plazasLimitadas.equals("True")){
			actividades.add(new Actividad(id,nombre,plazas,true));
		}
		else{
			actividades.add(new Actividad(id,nombre,plazas));
		}
	}
	
	/**
	 * Método para obtener la pk de la actividad más alta 
	 * @param claves
	 * @return
	 */
	private String obtenerIdActividad(ArrayList<Actividad> actividades){
		int max = 0;
		int id=0;
		for(int i = 0; i < actividades.size(); i++){
			 id=Integer.parseInt(actividades.get(i).getCodActividad());
			if( id>= max)
				max = Integer.parseInt(actividades.get(i).getCodActividad());
		}
		return String.valueOf(max + 1);
	}
	//------------------------------------------------------------------------------------
	
	
	//Guarda la informacion cuando de donde se llevara a cabo la actividad
	public void reservarHorarioActividad(String codAct, String idRes, String cod_misma) throws SQLException {
		Connection con = getConnection(); PreparedStatement ps = con.prepareStatement("INSERT INTO SE_REALIZA_EN (COD_MISMA_ACTIVIDAD,COD_ACTIVIDAD,ID_RESERVA) VALUES (?,?,?)");
		ps.setString(1, codAct); ps.setString(2, idRes); ps.setString(3, cod_misma);ps.executeUpdate();
		ps.close();con.close();
	}
	
	//Guarda la informacion cuando de donde se llevara a cabo la actividad
		public void reservarHorarioActividadPeriodica(String codAct, ArrayList<Reserva> reservas, int cod_misma) throws SQLException {
			Connection con = getConnection(); 
			PreparedStatement ps = con.prepareStatement("INSERT INTO SE_REALIZA_EN (COD_MISMA_ACTIVIDAD,COD_ACTIVIDAD,ID_RESERVA) VALUES (?,?,?)");
			for(Reserva reserva: reservas) {
				ps.setInt(1, cod_misma); 
				ps.setString(2, codAct); 
				ps.setString(3, reserva.getIdReserva());ps.executeUpdate();
				
			}
			ps.close();
			con.close();
		}
	
	//Guarda la informacion cuando un usuario se quiere apuntar
	public void apuntarUsuarioActividad(String dni, String idRes, String codAct) throws SQLException {
		Connection con = getConnection();
		PreparedStatement ps = con.prepareStatement("INSERT INTO APUNTADO_A (COD_ACTIVIDAD,ID_RESERVA,DNI,CANCELADA) VALUES (?,?,?,?)");
		ps.setString(1, codAct); ps.setString(2, idRes);ps.setString(3, dni); ps.setString(4, "False");	
		ps.executeUpdate();ps.close();con.close();
	}
	
	public void cambiarEstadoCanceladaApuntadoA(ArrayList<Socio> soc, String idRes, String codAct, String valorCancelada) throws SQLException {
		Connection con = getConnection();
		PreparedStatement ps = con.prepareStatement("UPDATE APUNTADO_A set CANCELADA =? WHERE DNI=? AND COD_ACTIVIDAD=? AND ID_RESERVA=?");
		for(Socio s: soc) {
			ps.setString(1, valorCancelada);ps.setString(2, s.getDNI()); ps.setString(3, codAct); ps.setString(4, idRes);	
			ps.executeUpdate();
		}
		ps.close();con.close();
	}
	
	public void cambiarEstadoCanceladaApuntadoA(String dni, String idRes, String codAct, String valorCancelada) throws SQLException {
		Connection con = getConnection();
		PreparedStatement ps = con.prepareStatement("UPDATE APUNTADO_A set CANCELADA =? WHERE DNI=? AND COD_ACTIVIDAD=? AND ID_RESERVA=?");
			ps.setString(1, valorCancelada);ps.setString(2, dni); ps.setString(3, codAct); ps.setString(4, idRes);	
			ps.executeUpdate();
		
		ps.close();con.close();
	}
	
	public ArrayList<Reserva> reservasActividadUsuario(String dni) throws SQLException{
		Connection con = getConnection();
		ArrayList<Reserva> res = new ArrayList<Reserva>();
		PreparedStatement ps = con.prepareStatement("select ID_RESERVA from APUNTADO_A WHERE DNI=? and CANCELADA='False'");
		ps.setString(1, dni);
		ResultSet st = ps.executeQuery();
		while(st.next()){ 
			String id_reserva = st.getString("ID_RESERVA");
			res.add(reservaById(id_reserva));
		}
		ps.close();con.close();
		return res;
	}
	
	public Reserva reservaById(String id) {
		for(Reserva r: reservas) {
			if(r.getIdReserva().equals(id))
				return r;
		}
		return null;
	}
	
	public Actividad obtenerActByCod(String codAct) {
		for(Actividad a: getActividades()) {
			if(a.getCodActividad().equals(codAct))
				return a;
		}
		return null;
	}
	
	public boolean existeEnApuntadoA(String dni, String idRes, String codAct) throws SQLException {
		Connection con = getConnection();
		boolean result = false;
		PreparedStatement ps = con.prepareStatement("select count(*) as num from apuntado_a where dni=? and id_reserva=? and cod_actividad=?");
		ps.setString(1, dni);ps.setString(2, idRes);ps.setString(3, codAct);
		ResultSet st = ps.executeQuery();
		while(st.next()){ 
			int cont = st.getInt("num");
			if(cont == 1)
				result = true;
		}
		ps.close();con.close();
		return result;
	}

	
	

	
	
	public ArrayList<String> obtenerReservasActividadPeriodica(String idReserva, String codAct) throws SQLException{
		String cod_misma_act = obtenerCodMismaAct(idReserva, codAct);
		ArrayList<String> ids = new ArrayList<String>();
		Connection con = getConnection();
		PreparedStatement ps = con.prepareStatement("select id_reserva from se_realiza_en where cod_misma_actividad=?");
		ps.setString(1, cod_misma_act);
		ResultSet st = ps.executeQuery();
		while(st.next()){ 
			ids.add(st.getString("id_reserva"));
		}
		ps.close();con.close();
		return ids;
	}
	
	public String obtenerCodMismaAct(String idReserva, String codAct) throws SQLException {
		Connection con = getConnection();
		String cod_misma_act = null;
		PreparedStatement ps = con.prepareStatement("select cod_misma_actividad from se_realiza_en where id_reserva=? and cod_actividad=?");
		ps.setString(1, idReserva);ps.setString(2, codAct);
		ResultSet st = ps.executeQuery();
		while(st.next()){ 
			cod_misma_act = st.getString("cod_misma_actividad");
		}
		ps.close();con.close();
		return cod_misma_act;
	}
	
	/**
	 * Método para obtener las actividades apuntadas de un socio.
	 * @param dni
	 * @return
	 * @throws SQLException 
	 */
	public ArrayList<String[]> obtenerLista(String dni) throws SQLException{
		ArrayList<String[]> lista = new ArrayList<String[]>();
		Connection con = getConnection();
		PreparedStatement ps = con.prepareStatement("SELECT * FROM APUNTADO_A WHERE DNI=?");
		ps.setString(1, dni);
		ResultSet st = ps.executeQuery();
		while(st.next()){
			String codActividad = st.getString("COD_ACTIVIDAD");
			String idReserva = st.getString("ID_RESERVA");
			String cancelada = st.getString("CANCELADA");
			String[] trio = new String [3];
			trio[0] = codActividad;
			trio[1] = idReserva;
			trio[2] = cancelada;
			lista.add(trio);
		}	
		return lista;	
	}
	
	public void añadirReservaCentroInstalacionNoDisponible(String idPista, Date fecha, int duracion) throws SQLException{
		Connection con = getConnection();
		Timestamp sqlDate = new java.sql.Timestamp(fecha.getTime());
		PreparedStatement ps = con.prepareStatement("INSERT INTO RESERVAS (ID_RESERVA, ID_PISTA, DNI, FECHA_RESERVA, DURACION, ESTADO, "
				+ "HORA_ENTRADA, HORA_SALIDA, RESERVA_CENTRO, TIPO_ACTIVIDAD, PAGADA, MOTIVO_ANULACION) "
				+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?)");
		String idReserva = obtenerIdReserva(getReservas());
		ps.setString(1, idReserva);
		ps.setString(2, idPista);
		ps.setString(3, "0");
		ps.setTimestamp(4, sqlDate);
		ps.setInt(5, duracion);
		ps.setString(6, Reserva.LIBRE);
		ps.setDate(7, null);
		ps.setDate(8, null);
		ps.setString(9, "True");
		ps.setString(10, "NO DISPONIBLE");
		ps.setString(11, null);
		ps.setString(12, null);
		ps.executeUpdate();
		System.out.println("Base de datos actualizada");
		Reserva reserva=new Reserva(getInstalacionById(idPista),fecha,duracion,"NO DISPONIBLE",idReserva,Reserva.LIBRE);
		getReservas().add(reserva);
	}
	
}
