package igu;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.toedter.calendar.JDateChooser;

import logica.Actividad;
import logica.Instalacion;
import logica.Polideportivo;
import logica.Reserva;
import logica.Socio;

import javax.swing.BoxLayout;
import java.awt.Font;
import java.awt.Dimension;

public class CancelarReservaCentroAdm extends JDialog{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JPanel panelDatos;
	private JPanel panelInstalaciones;
	private JPanel panelBotones;
	private JButton btnEliminar;
	private JScrollPane scrollPaneTablaReservas;
	private JTable tableReservas;
	private JPanel panelBusquedaInstalacion;
	private JPanel panelSeleccionFecha;
	private JLabel lblSeleccioneUnaInstalacin;
	private JComboBox<Instalacion> comboBoxInstalaciones;
	private JLabel lblSeleccioneLaFecha;
	private Polideportivo polideportivo;
	private ModeloNoEditable modeloTabla;
	SimpleDateFormat formato1 = new SimpleDateFormat("dd/MM/yyyy - HH:mm"); 
	private JButton btnBuscar;
	private JDateChooser dateChooser;
	private JLabel lblMostrarTodas;
	private JRadioButton rdbtnTodasReservas;
	private JPanel panelBuscar;
	private JPanel panelEliminar;
	private JPanel panelSalir;
	private JButton btnSalir;
	private JLabel lblSeleccionaUnaActividad;
	private JComboBox<String> cbActividades;
	private JPanel panelBusquedaActividad;
	private JRadioButton rdbtnInstalaciones;
	private JLabel lblVerTodasLas;
	private JRadioButton rdActividades;
	private JLabel lblVerTodasLas_1;
	private CancelarReservaCentroAdm crca;
	
	String [] botones = { "Eliminar periódica", "Eliminar puntual", "Cancelar" };
	 
	//Object [] botones = { " boton uno", " boton dos", "boton N" };


	/**
	 * Create the frame.
	 */
	public CancelarReservaCentroAdm(VentanaAdminPrincipalUltimate ventanaAdminPrincipalUltimate) {
		setIconImage(Toolkit.getDefaultToolkit().getImage(ReservaInstalacionAdmVentana.class.getResource("/img/icon.jpg")));
		setTitle("Poliportivo: Cancelar reserva centro");
		modeloTabla = new ModeloNoEditable(crearNombreColumnas(),0);
		this.polideportivo=ventanaAdminPrincipalUltimate.getPolideportivo();
		crca=this;
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setModal(true);
		setBounds(100, 100, 1054, 624);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		contentPane.add(getPanelDatos(), BorderLayout.NORTH);
		contentPane.add(getScrollPaneTablaReservas(), BorderLayout.CENTER);
		contentPane.add(getPanelSalir(), BorderLayout.SOUTH);
	}

	private JPanel getPanelDatos() {
		if (panelDatos == null) {
			panelDatos = new JPanel();
			panelDatos.setLayout(new GridLayout(0, 2, 0, 0));
			panelDatos.add(getPanelInstalaciones());
			panelDatos.add(getPanelBotones());
		}
		return panelDatos;
	}
	private JPanel getPanelInstalaciones() {
		if (panelInstalaciones == null) {
			panelInstalaciones = new JPanel();
			panelInstalaciones.setLayout(new GridLayout(3, 1, 0, 0));
			panelInstalaciones.add(getPanelBusquedaInstalacion());
			panelInstalaciones.add(getPanelBusquedaActividad());
			panelInstalaciones.add(getPanelSeleccionFecha());
		}
		return panelInstalaciones;
	}
	private JPanel getPanelBotones() {
		if (panelBotones == null) {
			panelBotones = new JPanel();
			panelBotones.setLayout(new BoxLayout(panelBotones, BoxLayout.X_AXIS));
			panelBotones.add(getPanelBuscar());
			panelBotones.add(getPanelEliminar());
		}
		return panelBotones;
	}
	private JButton getBtnEliminar() {
		if (btnEliminar == null) {
			btnEliminar = new JButton("Eliminar");
			btnEliminar.setFont(new Font("Tahoma", Font.PLAIN, 17));
			btnEliminar.setEnabled(false);
			btnEliminar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(tableReservas.getSelectedRow() != -1) {
						int n = obtenerTipoActividad(tableReservas.getSelectedRow());
						if(n == 1){
							int resultado = JOptionPane.showConfirmDialog(crca, "Ha seleccionado una actividad puntual, ¿desea eliminarla?","Eliminar actividad",JOptionPane.YES_NO_OPTION);
							if(resultado == JOptionPane.YES_OPTION){
								borrarReservaPuntual(tableReservas.getSelectedRow());
								JOptionPane.showMessageDialog(null, "La reserva puntual se ha borrado con éxito");
							}
						}
						else if(n == 0){
							int resultado = JOptionPane.showConfirmDialog(crca, "Ha seleccionado una reserva del Centro, ¿desea eliminarla?","Eliminar reserva centro",JOptionPane.YES_NO_OPTION);
							if(resultado == JOptionPane.YES_OPTION){
								borrarReservaPuntual(tableReservas.getSelectedRow());
								JOptionPane.showMessageDialog(null, "La reserva del centro se ha borrado con éxito");
							}
						}
						else{
							mostrarDialogDecidirBorradoPeriodica(tableReservas.getSelectedRow());
//							int resultado = JOptionPane.showConfirmDialog(crca, "Ha seleccionado una actividad periódica, ¿desea eliminarla?","Eliminar actividad",JOptionPane.YES_NO_OPTION);
//							if(resultado == JOptionPane.YES_OPTION){
//								borrarReservaPeriodica(tableReservas.getSelectedRow());
//								JOptionPane.showMessageDialog(null, "La reserva periódica se ha borrado con éxito");
//							}
						}
						modeloTabla.getDataVector().removeAllElements();
						añadirReservas(modeloTabla,obtenerReservasFiltradas(rdbtnTodasReservas.isSelected(), getRdbtnInstalaciones().isSelected(), getRdActividades().isSelected()));
						modeloTabla.fireTableDataChanged();
					}
					else
						JOptionPane.showMessageDialog(null, "Seleccione una reserva");
				}
			});
		}
		return btnEliminar;
	}
	
	private void mostrarDialogDecidirBorradoPeriodica(int fila) {
		ElegirBorrarPeriodicaPuntualVentana ebpp= new ElegirBorrarPeriodicaPuntualVentana(this,fila);
		ebpp.setModal(true);
		ebpp.setLocationRelativeTo(this);
		ebpp.setVisible(true);
	}
	
	private JScrollPane getScrollPaneTablaReservas() {
		if (scrollPaneTablaReservas == null) {
			scrollPaneTablaReservas = new JScrollPane();
			scrollPaneTablaReservas.setViewportView(getTableReservas());
		}
		return scrollPaneTablaReservas;
	}
	private JTable getTableReservas() {
		if (tableReservas == null) {
			tableReservas = new JTable(modeloTabla);
			tableReservas.removeColumn(tableReservas.getColumnModel().getColumn(6));
			tableReservas.removeColumn(tableReservas.getColumnModel().getColumn(5));
			tableReservas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			tableReservas.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent arg0) {
					activarBotones();
				}
			});
		}
		return tableReservas;
	}
	private JPanel getPanelBusquedaInstalacion() {
		if (panelBusquedaInstalacion == null) {
			panelBusquedaInstalacion = new JPanel();
			FlowLayout flowLayout = (FlowLayout) panelBusquedaInstalacion.getLayout();
			flowLayout.setAlignment(FlowLayout.LEFT);
			panelBusquedaInstalacion.add(getLblSeleccioneUnaInstalacin());
			panelBusquedaInstalacion.add(getComboBoxInstalaciones());
			panelBusquedaInstalacion.add(getLblVerTodasLas());
			panelBusquedaInstalacion.add(getRdbtnInstalaciones());
		}
		return panelBusquedaInstalacion;
	}
	private JPanel getPanelSeleccionFecha() {
		if (panelSeleccionFecha == null) {
			panelSeleccionFecha = new JPanel();
			FlowLayout flowLayout = (FlowLayout) panelSeleccionFecha.getLayout();
			flowLayout.setAlignment(FlowLayout.LEFT);
			panelSeleccionFecha.add(getLblSeleccioneLaFecha());
			panelSeleccionFecha.add(getDateChooser());
			panelSeleccionFecha.add(getLblMostrarTodas());
			panelSeleccionFecha.add(getRdbtnTodasReservas());
		}
		return panelSeleccionFecha;
	}
	private JLabel getLblSeleccioneUnaInstalacin() {
		if (lblSeleccioneUnaInstalacin == null) {
			lblSeleccioneUnaInstalacin = new JLabel("Seleccione una instalaci\u00F3n: ");
		}
		return lblSeleccioneUnaInstalacin;
	}
	private JComboBox<Instalacion> getComboBoxInstalaciones() {
		if (comboBoxInstalaciones == null) {
			comboBoxInstalaciones = new JComboBox<Instalacion>();
			comboBoxInstalaciones.setEnabled(false);
			comboBoxInstalaciones.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					modeloTabla.getDataVector().removeAllElements();
					modeloTabla.fireTableDataChanged();
				}
			});
			DefaultComboBoxModel<Instalacion> model=new DefaultComboBoxModel<Instalacion>();
			model = llenarComboInstalaciones(model);
			comboBoxInstalaciones.setModel(model);
			comboBoxInstalaciones.setSelectedIndex(0);
		}
		return comboBoxInstalaciones;
	}
	private JLabel getLblSeleccioneLaFecha() {
		if (lblSeleccioneLaFecha == null) {
			lblSeleccioneLaFecha = new JLabel("Seleccione la fecha de inicio:");
		}
		return lblSeleccioneLaFecha;
	}
	
	private DefaultComboBoxModel<Instalacion> llenarComboInstalaciones(DefaultComboBoxModel<Instalacion> model){
		for(Instalacion inst:polideportivo.getInstalaciones())
			model.addElement(inst);
		return model;
	}
	
	public Polideportivo getPolideportivo() {
		return polideportivo;
	}
	
	private String[] crearNombreColumnas() {
		String[] columnas = new String[7];
		columnas[0] = "Fecha de inicio";
		columnas[1] = "Fecha de fin";
		columnas[2] = "Duración";
		columnas[3] = "Actividad";
		columnas[4] = "Instalación";
		columnas[5] = "idActividad";
		columnas[6] = "Reserva";
		return columnas;
	}
	

	private void añadirReservas(ModeloNoEditable modeloTabla, ArrayList<Reserva> res) {
		Object[] row = new Object[7];
		Collections.sort(res, new Comparator<Reserva>() {

			@Override
			public int compare(Reserva arg0, Reserva arg1) {
				return arg0.getFechaReserva().compareTo(arg1.getFechaReserva());
			}
		    
		});
		for(Reserva r: res) {
			Actividad act = polideportivo.isReservaActividad(r);
			
				Calendar c1 = Calendar.getInstance(); c1.setTime(r.getFechaReserva()); c1.add(Calendar.HOUR_OF_DAY, r.getTiempoReserva());
				String fechaConFormato1 = formato1.format(r.getFechaReserva()),  fechaConFormato2 = formato1.format(c1.getTime());
				row[0] = fechaConFormato1;
				row[1] = fechaConFormato2;
				row[2] = r.getTiempoReserva()>=10?r.getTiempoReserva()+":00 h":"0"+r.getTiempoReserva()+":00 h";
				row[3] = (act != null)?act.getNombreActividad():"CENTRO";
				row[4] = r.getInstalacion().toString();
				row[5] = (act != null)?act.getCodActividad():"NINGUNA";
				row[6] = r;
				modeloTabla.addRow(row);
			
		}
	}
	
	private int obtenerTipoActividad(int fila){
		Reserva res = (Reserva)getTableReservas().getModel().getValueAt(fila, 6);
		String codActividad = (String)getTableReservas().getModel().getValueAt(fila, 5);
		ArrayList<String> reservas = new ArrayList<String>();
		if(!codActividad.equals("NINGUNA")) {
			Actividad act = polideportivo.getConexion().obtenerActByCod(codActividad);
			//Obtenemos todas las reservas de ese cod_misma_actividad
			try {
				reservas = polideportivo.getConexion().obtenerReservasActividadPeriodica(res.getIdReserva(), act.getCodActividad());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return reservas.size();
	}
	
	public void borrarReservaPeriodica(int fila){
		ponerReservasPeriodicasAnuladas(fila);
	}
	
	private void ponerReservasPeriodicasAnuladas(int fila){
		ArrayList<Socio> soc = new ArrayList<Socio>();
		ArrayList<String> reservas = new ArrayList<String>();
		Reserva res = (Reserva)getTableReservas().getModel().getValueAt(fila, 6);
		String codActividad = (String)getTableReservas().getModel().getValueAt(fila, 5);		
		try {
			if(!codActividad.equals("NINGUNA")) {
				Actividad act = polideportivo.getConexion().obtenerActByCod(codActividad);
				//Obtenemos todas las reservas de ese cod_misma_actividad
				reservas = polideportivo.getConexion().obtenerReservasActividadPeriodica(res.getIdReserva(), act.getCodActividad());
				for(int i=0; i<reservas.size(); i++){
					Reserva r = obtenerReserva(reservas.get(i));
					if(!r.getEstado().equals(Reserva.ANULADA)) {
						soc = act.obtenerApuntados(r);
						if(!soc.isEmpty()) {//si hay alquien apuntado cambio su estado en la bd
//							for(Socio sc: soc)
//								polideportivo.getConexion().cambiarEstadoCanceladaApuntadoA(sc.getDNI(), res.getIdReserva(),act.getCodActividad(),"True");
							polideportivo.getConexion().cambiarEstadoCanceladaApuntadoA(soc,r.getIdReserva(),act.getCodActividad(),"True");//BORRADO DE EN LA BD DE LA ACTIVIDAD
						}
						act.getApuntadosActividades().remove(r);//BORRADO LOCAL DE LA RESERVA DE LA ACTIVIDAD
						polideportivo.getConexion().cambiarEstadoAnuladaReservaCentro(r.getIdReserva());//BORRADO EN LA BD DE LA RESERVA
						polideportivo.ponerReservaAnulada(r.getIdReserva());//BORRADO LOCAL DE LA RESERVA
					}	
				}	
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private Reserva obtenerReserva(String idReserva){
		for(Reserva r: polideportivo.getReservas()){
			if(r.getIdReserva().equals(idReserva)){
				return r;
			}
		}
		return null;
	}
	public void borrarReservaPuntual(int fila) {
		ponerReservasPuntualesAnuladas(fila);
	}

	private void ponerReservasPuntualesAnuladas(int fila) {
		ArrayList<Socio> soc = new ArrayList<Socio>();
		Reserva res = (Reserva)getTableReservas().getModel().getValueAt(fila, 6);
		String codActividad = (String)getTableReservas().getModel().getValueAt(fila, 5);
		try {
			if(!codActividad.equals("NINGUNA")) {
				Actividad act = polideportivo.getConexion().obtenerActByCod(codActividad);
				soc = act.obtenerApuntados(res);
				if(!soc.isEmpty()) {//si hay alquien apuntado cambio su estado en la bd
//					for(Socio sc: soc)
//						polideportivo.getConexion().cambiarEstadoCanceladaApuntadoA(sc.getDNI(), res.getIdReserva(),act.getCodActividad(),"True");
					polideportivo.getConexion().cambiarEstadoCanceladaApuntadoA(soc,res.getIdReserva(),act.getCodActividad(),"True");//BORRADO DE EN LA BD DE LA ACTIVIDAD
				}
				act.getApuntadosActividades().remove(res);//BORRADO LOCAL DE LA RESERVA DE LA ACTIVIDAD
			}
			polideportivo.getConexion().cambiarEstadoAnuladaReservaCentro(res.getIdReserva());//BORRADO EN LA BD DE LA RESERVA
		} catch (SQLException e) {
			e.printStackTrace();
		}
		polideportivo.ponerReservaAnulada(res.getIdReserva());//BORRADO LOCAL DE LA RESERVA
	}

	public void activarBotones() {
		if(tableReservas.getSelectedRow() != -1)
			btnEliminar.setEnabled(true);
		else
			btnEliminar.setEnabled(false);
	}
	private JButton getBtnBuscar() {
		if (btnBuscar == null) {
			btnBuscar = new JButton("Buscar");
			btnBuscar.setFont(new Font("Tahoma", Font.PLAIN, 17));
			btnBuscar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					ArrayList<Reserva> res = obtenerReservasFiltradas(rdbtnTodasReservas.isSelected(), getRdbtnInstalaciones().isSelected(), getRdActividades().isSelected());
					if(!res.isEmpty()) {
						modeloTabla.getDataVector().removeAllElements();
						añadirReservas(modeloTabla, res);
						modeloTabla.fireTableDataChanged();
					} else {
						modeloTabla.getDataVector().removeAllElements();
						JOptionPane.showMessageDialog(null, "No existen reservas para los datos introducidos");
						modeloTabla.fireTableDataChanged();
					}
				}
			});
		}
		return btnBuscar;
	}
	private JDateChooser getDateChooser() {
		if (dateChooser == null) {
			dateChooser = new JDateChooser(new Date());
			dateChooser.setMinSelectableDate(new Date());
			dateChooser.setEnabled(false);
		}
		return dateChooser;
	}
	
	private ArrayList<Reserva> obtenerReservasFiltradas(boolean todasFechas, boolean todasInstalaciones, boolean todasActividades) {
		ArrayList<Reserva> reservasF = new ArrayList<Reserva>();
		for(Reserva r: polideportivo.getReservas()) {
			if(r.getEstado().equals(Reserva.LIBRE) && (r.getFechaReserva().after(new Date()))) {
				
				Actividad actividad = polideportivo.isReservaActividad(r);
				Calendar c1 = Calendar.getInstance(); c1.setTime(r.getFechaReserva());
				Calendar c2 = Calendar.getInstance(); c2.setTime(dateChooser.getDate());
				boolean fechas= false;
				boolean inst= false;
				boolean acti= false;
				if(!todasFechas) {
					fechas=c1.get(Calendar.DAY_OF_MONTH) == c2.get(Calendar.DAY_OF_MONTH);
				}
				if(!todasInstalaciones) {
					inst= (((Instalacion)comboBoxInstalaciones.getSelectedItem()).getIdPista().equals(r.getInstalacion().getIdPista()));
				}
				if(!todasActividades) {
					acti=(actividad!=null) && (cbActividades.getSelectedItem().equals(actividad.getNombreActividad()));
				}
				
				if( (todasFechas || (!todasFechas && fechas)) 
						&& (todasInstalaciones || (!todasInstalaciones && inst)) 
						&& (todasActividades || (!todasActividades && acti))  
						&& r.isReservaCentro())
					reservasF.add(r);
						
					
			}
		}
		
		return reservasF;
	}
	private JLabel getLblMostrarTodas() {
		if (lblMostrarTodas == null) {
			lblMostrarTodas = new JLabel("Ver todas las reservas");
		}
		return lblMostrarTodas;
	}
	private JRadioButton getRdbtnTodasReservas() {
		if (rdbtnTodasReservas == null) {
			rdbtnTodasReservas = new JRadioButton("");
			rdbtnTodasReservas.setSelected(true);
			rdbtnTodasReservas.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent arg0) {
					if(rdbtnTodasReservas.isSelected())
						dateChooser.setEnabled(false);
					else
						dateChooser.setEnabled(true);
				}
			});
		}
		return rdbtnTodasReservas;
	}

	private JPanel getPanelBuscar() {
		if (panelBuscar == null) {
			panelBuscar = new JPanel();
			panelBuscar.setBorder(new EmptyBorder(5, 20, 5, 10));
			panelBuscar.setLayout(new BorderLayout(0, 0));
			panelBuscar.add(getBtnBuscar());
		}
		return panelBuscar;
	}
	private JPanel getPanelEliminar() {
		if (panelEliminar == null) {
			panelEliminar = new JPanel();
			panelEliminar.setBorder(new EmptyBorder(5, 10, 5, 20));
			panelEliminar.setLayout(new BorderLayout(0, 0));
			panelEliminar.add(getBtnEliminar(), BorderLayout.CENTER);
		}
		return panelEliminar;
	}
	private JPanel getPanelSalir() {
		if (panelSalir == null) {
			panelSalir = new JPanel();
			FlowLayout flowLayout = (FlowLayout) panelSalir.getLayout();
			flowLayout.setAlignment(FlowLayout.RIGHT);
			panelSalir.add(getBtnSalir());
		}
		return panelSalir;
	}
	private JButton getBtnSalir() {
		if (btnSalir == null) {
			btnSalir = new JButton("Cancelar");
			btnSalir.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});
		}
		return btnSalir;
	}
	private JLabel getLblSeleccionaUnaActividad() {
		if (lblSeleccionaUnaActividad == null) {
			lblSeleccionaUnaActividad = new JLabel("Seleccione una actividad:    ");
			lblSeleccionaUnaActividad.setMinimumSize(new Dimension(131, 14));
			lblSeleccionaUnaActividad.setMaximumSize(new Dimension(131, 14));
		}
		return lblSeleccionaUnaActividad;
	}
	private JComboBox<String> getCbActividades() {
		if (cbActividades == null) {
			cbActividades = new JComboBox<String>();
			cbActividades.setEnabled(false);
			DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
			model=llenarComboActividades(model);
			cbActividades.setModel(model);
			cbActividades.setSelectedIndex(0);
		}
		return cbActividades;
	}
	
	private DefaultComboBoxModel<String> llenarComboActividades(DefaultComboBoxModel<String> model) {
		for (Actividad actividad: polideportivo.getActividades()) {
			model.addElement(actividad.getNombreActividad());
		}
		return model;
	}
	private JPanel getPanelBusquedaActividad() {
		if (panelBusquedaActividad == null) {
			panelBusquedaActividad = new JPanel();
			FlowLayout flowLayout = (FlowLayout) panelBusquedaActividad.getLayout();
			flowLayout.setAlignment(FlowLayout.LEFT);
			panelBusquedaActividad.add(getLblSeleccionaUnaActividad());
			panelBusquedaActividad.add(getCbActividades());
			panelBusquedaActividad.add(getLblVerTodasLas_1());
			panelBusquedaActividad.add(getRdActividades());
		}
		return panelBusquedaActividad;
	}
	private JRadioButton getRdbtnInstalaciones() {
		if (rdbtnInstalaciones == null) {
			rdbtnInstalaciones = new JRadioButton("");
			rdbtnInstalaciones.setSelected(true);
			rdbtnInstalaciones.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					if(rdbtnInstalaciones.isSelected())
						getComboBoxInstalaciones().setEnabled(false);
					else
						getComboBoxInstalaciones().setEnabled(true);
					
				}
			});
		}
		return rdbtnInstalaciones;
	}
	private JLabel getLblVerTodasLas() {
		if (lblVerTodasLas == null) {
			lblVerTodasLas = new JLabel("Ver todas las instalaciones");
		}
		return lblVerTodasLas;
	}
	private JRadioButton getRdActividades() {
		if (rdActividades == null) {
			rdActividades = new JRadioButton("");
			rdActividades.setSelected(true);
			rdActividades.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					if(rdActividades.isSelected())
						getCbActividades().setEnabled(false);
					else
						getCbActividades().setEnabled(true);

				}
			});
		}
		return rdActividades;
	}
	private JLabel getLblVerTodasLas_1() {
		if (lblVerTodasLas_1 == null) {
			lblVerTodasLas_1 = new JLabel("Ver todas las actividades");
		}
		return lblVerTodasLas_1;
	}
}
