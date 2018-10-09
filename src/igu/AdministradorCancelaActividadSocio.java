package igu;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.HeadlessException;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;

import java.awt.Font;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTextField;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import logica.Actividad;
import logica.Comprobaciones;
import logica.Polideportivo;
import logica.Reserva;
import logica.Socio;
import java.awt.Toolkit;

@SuppressWarnings("serial")
public class AdministradorCancelaActividadSocio extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JScrollPane scrollPane_1;
	private JTable table2;
	
	private DefaultTableModel modelo;
	private Polideportivo poli;
	private RendererFiltradoReserva tabla;
	@SuppressWarnings("unused")
	private VentanaAdminPrincipalUltimate apv;
	@SuppressWarnings("unused")
	private Comprobaciones c;
	private JButton botonBuscar2;
	private JTextField textSocio;
	private JLabel lblNewLabel;


	/**
	 * Create the dialog.
	 */
	public AdministradorCancelaActividadSocio(VentanaAdminPrincipalUltimate ventanaAdminPrincipalUltimate) {
		setIconImage(Toolkit.getDefaultToolkit().getImage(AdministradorCancelaActividadSocio.class.getResource("/img/icon.jpg")));
		setTitle("Anular reservas de actividades");
		setModal(true);
		c = new Comprobaciones();
		this.apv = ventanaAdminPrincipalUltimate;
		poli = ventanaAdminPrincipalUltimate.getPolideportivo();
		
		
		table2 = new JTable();
		tabla = new RendererFiltradoReserva();
		table2.setDefaultRenderer(Object.class, tabla);
		modelo =new DefaultTableModel();
		table2.setModel(modelo);
		
		setBounds(100, 100, 735, 429);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		contentPanel.add(getScrollPane_1());
		contentPanel.add(getBotonBuscar2());
		contentPanel.add(getTextSocio());
		contentPanel.add(getLblNewLabel());
		
		
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("Anular Inscripci\u00F3n");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						if(socioSeleccionado(table2) == false){
							JOptionPane.showMessageDialog(null, "Seleccione un socio");
						}
						else{
							try {
								if(anularReserva() == true){
									JOptionPane.showMessageDialog(null, "Socio eliminado de la actividad");
									tabla.limpiar(table2);
									String dni = obtenerDNISocioAPartirNSocio(); 
									obtenerTuplas(dni);
								}
								else{
									JOptionPane.showMessageDialog(null, "Ya no se puede eliminar al socio de la actividad ya que ya ha comenzado.");
								}
							} catch (HeadlessException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				});
				okButton.setFont(new Font("Dialog", Font.BOLD, 12));
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancelar");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						dispose();
					}
				});
				cancelButton.setFont(new Font("Dialog", Font.BOLD, 12));
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
	
	
	@SuppressWarnings({ "unchecked", "unused", "rawtypes" })
	private DefaultComboBoxModel llenarComboActividades(DefaultComboBoxModel model){
		ArrayList<Actividad> noRep=new ArrayList<Actividad>();
		boolean flag;
		for(Actividad inst:poli.getActividades()) {
			flag=false;
			for(Actividad i: noRep ) {
				if(inst.getCodActividad().equals(i.getCodActividad())){
					flag = true;
				}
			}
			if(!flag){
				noRep.add(inst);
				model.addElement(inst.getNombreActividad());
			}	
		}
			
		return model;
	}
	private JScrollPane getScrollPane_1() {
		if (scrollPane_1 == null) {
			scrollPane_1 = new JScrollPane();
			scrollPane_1.setBounds(20, 55, 649, 289);
			scrollPane_1.setViewportView(getTable2());
		}
		return scrollPane_1;
	}
	private JTable getTable2() {
		
			//table2 = new JTable();
			table2.setModel(new DefaultTableModel(
				new Object[][] {
				},
				new String[] {
					"Nombre","Fecha", "Instalación", "Pista"
				}
			));
			table2.setFont(new Font("Dialog", Font.BOLD, 12));
		
		return table2;
	}
	
	private JButton getBotonBuscar2() {
		if (botonBuscar2 == null) {
			botonBuscar2 = new JButton("Buscar");
			botonBuscar2.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					tabla.limpiar(table2);
					String dni = obtenerDNISocioAPartirNSocio(); 
					if(dni == null){
						JOptionPane.showMessageDialog(null, "El nº de socio introducido no se corresponde con ninguno de nuestro centro");
					}
					else{
						try {
							obtenerTuplas(dni);
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
				}
			});
			botonBuscar2.setFont(new Font("Dialog", Font.BOLD, 12));
			botonBuscar2.setBounds(183, 17, 95, 20);
		}
		return botonBuscar2;
	}
	private JTextField getTextSocio() {
		if (textSocio == null) {
			textSocio = new JTextField();
			textSocio.setFont(new Font("Dialog", Font.BOLD, 12));
			textSocio.setBounds(77, 17, 86, 20);
			textSocio.setColumns(10);
		}
		return textSocio;
	}
	private JLabel getLblNewLabel() {
		if (lblNewLabel == null) {
			lblNewLabel = new JLabel("N\u00BA Socio");
			lblNewLabel.setFont(new Font("Dialog", Font.BOLD, 12));
			lblNewLabel.setBounds(20, 20, 62, 14);
		}
		return lblNewLabel;
	}
	
	
	
	
	

	
	
	/**
	 * Cancelar una reserva de un socio a una actividad
	 * @throws ParseException 
	 * @throws SQLException 
	 */
	private boolean anularReserva() throws ParseException, SQLException{
		int fila = table2.getSelectedRow();
		if(fila != -1)
		{
			Actividad actividad = obtenerActividadAPartirNombre(String.valueOf(table2.getValueAt(fila, 0))) ;
			String DNI = obtenerDNISocioAPartirNSocio();
			String fecha = String.valueOf(table2.getValueAt(fila, 1));
			String instalacion = String.valueOf(table2.getValueAt(fila, 2));
			String pista = String.valueOf(table2.getValueAt(fila, 3));
			System.out.println(instalacion+" "+pista);
			
			Date date = null;
			SimpleDateFormat formatoDeFecha = new SimpleDateFormat("yyyy-MM-dd hh:mm");
			date = formatoDeFecha.parse(fecha);
			
			Calendar cal = Calendar.getInstance();
			if(date.before(cal.getTime()) || date.equals(cal.getTime())){
				return false;
			}
			String idPista = calcularIdPista(instalacion, pista);
			Reserva reserva = obtenerReservaTabla(date, idPista, actividad);
			actividad.desapuntarmme(reserva, obtenerSocioAPartirDNI(DNI));
			poli.getConexion().cambiarEstadoCanceladaApuntadoA(DNI, reserva.getIdReserva(), actividad.getCodActividad(),"True");
			return true;
		}
		return false;
	}
	
	private Socio obtenerSocioAPartirDNI(String dni){
		for(int i=0; i<poli.getClientes().size(); i++){
			if(poli.getClientes().get(i) instanceof Socio){
				if(((Socio)poli.getClientes().get(i)).getDNI().equals(dni)){
					return (Socio) poli.getClientes().get(i);
				}
			}
		}
		return null;
	}
	
	private Actividad obtenerActividadAPartirNombre(String nombre){
		for(int i=0; i<poli.getActividades().size(); i++){
			if(poli.getActividades().get(i).getNombreActividad().equals(nombre)){
				return poli.getActividades().get(i);
			}
		}
		return null;
	}
	
	/**
	 * Obtener la reserva seleccionada de la tabla
	 * @param fecha
	 * @param idPista
	 * @return
	 * @throws ParseException
	 */
	private Reserva obtenerReservaTabla(Date fecha, String idPista, Actividad actividad) throws ParseException{
		Calendar cal = Calendar.getInstance();
		int año,mes,dia,hora;
		for(int i=0; i<poli.getReservas().size(); i++){
			cal.setTime(poli.getReservas().get(i).getFechaReserva());
			año=cal.get(Calendar.YEAR);mes=cal.get(Calendar.MONTH);dia=cal.get(Calendar.DAY_OF_MONTH);hora=cal.get(Calendar.HOUR_OF_DAY);
			String fechaLista = año+"-"+(mes+1)+"-"+dia+" "+hora+":00";
			Date date = null;
			SimpleDateFormat formatoDeFecha = new SimpleDateFormat("yyyy-MM-dd hh:mm");
			date = formatoDeFecha.parse(fechaLista);
			if(date.equals(fecha)){
				if(poli.getReservas().get(i).getTipoActividad() != null){
					if(poli.getReservas().get(i).getTipoActividad().equals(actividad.getNombreActividad())){
						if(poli.getReservas().get(i).getInstalacion().getIdPista().equals(idPista)){
							return poli.getReservas().get(i);
						}
					}
				}
			}
			//}
		}
		return null;
	}
	
	/**
	 * Calcular idPista de una instalacion
	 * @param instalacion
	 * @param pista
	 * @return
	 */
	private String calcularIdPista(String instalacion, String pista){
		for(int i=0; i<poli.getInstalaciones().size(); i++){
			if(poli.getInstalaciones().get(i).getTipo().equals(instalacion) && poli.getInstalaciones().get(i).getnPista().equals(pista)){
				return poli.getInstalaciones().get(i).getIdPista();
			}
		}
		return "";
	}
	
	
	/**
	 * Obtiene el dni del socio a buscar.
	 * @return
	 */
	private String obtenerDNISocioAPartirNSocio(){
		if(numeros() == true){
			int socio = Integer.parseInt(textSocio.getText().toString());
			for(int i=0; i<poli.getClientes().size(); i++){
				if(poli.getClientes().get(i) instanceof Socio){
					if(socio == ((Socio)poli.getClientes().get(i)).getNumeroSocio()){
						return poli.getClientes().get(i).getDNI();
					}
				}
			}
		}
		return null;
	}
	
	
	/**
	 * Obtiene un mapa con clave el dni del socio, y un array de String [actividad-reserva]
	 * @param dni
	 * @throws SQLException
	 */
	private void obtenerTuplas(String dni) throws SQLException{
		ArrayList<String[]> lista = new ArrayList<String[]>();
		lista = poli.getConexion().obtenerLista(dni);
		pintarTabla(lista);
	}
	
	/**
	 * Pinta en la tabla las actividades apuntadas del socio
	 * @param mapa
	 */
	private void pintarTabla(ArrayList<String[]> lista){
		Calendar cal = Calendar.getInstance();
		Object[] row = new Object[4];
		//Date fechaAhora = cal.getTime();
		int dia,mes,año,hora;
		for(int i=0; i<lista.size(); i++){
			Reserva reserva = obtenerReserva(lista.get(i)[1]);
			Actividad actividad = obtenerActividadAPartirCod(lista.get(i)[0]);
			if(lista.get(i)[2].equals("False")){
				if(reserva.getFechaReserva().after(new Date())){
					cal.setTime(reserva.getFechaReserva());
					dia = cal.get(Calendar.DAY_OF_MONTH);
					mes = cal.get(Calendar.MONTH);
					año = cal.get(Calendar.YEAR);
					hora = cal.get(Calendar.HOUR_OF_DAY);
					row[0] = actividad.getNombreActividad();
					row[1] = año+"-"+(mes+1)+"-"+dia+" "+hora+":00";
					row[2] = reserva.getInstalacion().getTipo();
					row[3] = reserva.getInstalacion().getnPista();
					((DefaultTableModel) table2.getModel()).addRow(row);
				}
			}
		}
	}
	
	/**
	 * Obtenemos la reserva a partir del idReserva que se le pasa por parámetro.
	 * @param idReserva
	 * @return
	 */
	private Reserva obtenerReserva(String idReserva){
		for(int i=0; i<poli.getReservas().size(); i++){
			if(poli.getReservas().get(i).getIdReserva().equals(idReserva)){
				return poli.getReservas().get(i);
			}
		}
		return null;
	}
	
	/**
	 * Obtenemos la actividad cuyo codActividad se pasa por parámetro.
	 * @param codActividad
	 * @return
	 */
	private Actividad obtenerActividadAPartirCod(String codActividad){
		for(int i=0; i<poli.getActividades().size(); i++){
			if(poli.getActividades().get(i).getCodActividad().equals(codActividad)){
				return poli.getActividades().get(i);
			}
		}
		return null;
	}

	/**
	 * Comprobar que se ha seleccionado un socio
	 * @param tabla
	 * @return
	 */
	public boolean socioSeleccionado(JTable tabla){
		if(tabla.getSelectedRow() == -1)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	
	private boolean numeros(){
		boolean numeros = true;
		for(char c : textSocio.getText().toCharArray()){
			if(!Character.isDigit(c)){
				numeros = false;
				break;}
		}
		if(numeros == false){
			return false;
		}
		return true;
	}
	
}
