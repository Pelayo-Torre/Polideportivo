package igu;

import java.awt.BorderLayout;

import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import logica.Actividad;
import logica.Polideportivo;
import logica.Reserva;
import logica.Socio;

import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import javax.swing.DefaultComboBoxModel;


import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;

import java.awt.FlowLayout;
import javax.swing.border.TitledBorder;
import javax.swing.UIManager;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.awt.event.ActionEvent;

import javax.swing.ListSelectionModel;
import java.awt.Font;
import javax.swing.JRadioButton;


public class ActividadSocio extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private Polideportivo poli;
	private String usuario_conectado;
	private JPanel panelSuperior;
	private JScrollPane scrollPaneTabla;
	private JTable tablaMisActividades;
	private JPanel panelAñadirReserva;
	private JPanel panellblEliminar;
	private JPanel panelbtnEliminar;
	private JPanel panelAñadir;
	private ModeloNoEditable modeloTabla;
	SimpleDateFormat formato1 = new SimpleDateFormat("dd/MM/yyyy - HH:mm"); 
	private JPanel panelSupremeteTabla;
	private JButton btnReservar;
	private JButton btnEliminar;
	private JPanel panelSupremeBotones;
	private JLabel label;
	private JLabel label_1;
	private JPanel panelSalir;
	private JButton btnSalir;
	private JPanel panelCombo;
	private JLabel lblActividad;
	private JComboBox<Actividad> comboBoxActividades;
	private JPanel panelActividades;
	private JPanel panelComboActividades;
	private JPanel panel;
	private JRadioButton rdbtnVerTodas;

	public ActividadSocio(Polideportivo poli2, String usuario) {
		setIconImage(Toolkit.getDefaultToolkit().getImage(ReservaInstalacionAdmVentana.class.getResource("/img/icon.jpg")));
		setTitle("Polideportivo: Actividades");
		setModal(true);
		poli=poli2;
		usuario_conectado=usuario;
		modeloTabla= new ModeloNoEditable(crearNombreColumnas(),0);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 1235, 488);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		contentPane.add(getPanelSupremeteTabla(), BorderLayout.CENTER);
		contentPane.add(getPanelSupremeBotones(), BorderLayout.EAST);
		contentPane.add(getPanel_3(), BorderLayout.SOUTH);
		comboBoxActividades.setSelectedIndex(0);
	}
	private JPanel getPanel_2() {
		if (panelSuperior == null) {
			panelSuperior = new JPanel();
			panelSuperior.setLayout(new GridLayout(3, 1, 0, 0));
			panelSuperior.add(getPanelCombo());
			panelSuperior.add(getPanel_1_1());
			panelSuperior.add(getPanel_2_1());
		}
		return panelSuperior;
	}
	private JScrollPane getScrollPaneTabla() {
		if (scrollPaneTabla == null) {
			scrollPaneTabla = new JScrollPane();
			scrollPaneTabla.setViewportBorder(null);
			scrollPaneTabla.setViewportView(getTablaMisActividades());
		}
		return scrollPaneTabla;
	}
	private JTable getTablaMisActividades() {
		if (tablaMisActividades == null) {
			tablaMisActividades = new JTable(modeloTabla);
			tablaMisActividades.removeColumn(tablaMisActividades.getColumnModel().getColumn(7));
			tablaMisActividades.removeColumn(tablaMisActividades.getColumnModel().getColumn(6));
			tablaMisActividades.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		}
		return tablaMisActividades;
	}
	private JPanel getPanel_1_1() {
		if (panelAñadirReserva == null) {
			panelAñadirReserva = new JPanel();
			panelAñadirReserva.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "A\u00F1adir reserva", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
			panelAñadirReserva.setLayout(new BorderLayout(0, 0));
			panelAñadirReserva.add(getPanelAñadir());
			panelAñadirReserva.add(getLabel(), BorderLayout.NORTH);
		}
		return panelAñadirReserva;
	}
	private JPanel getPanel_2_1() {
		if (panellblEliminar == null) {
			panellblEliminar = new JPanel();
			panellblEliminar.setBorder(new TitledBorder(null, "Eliminar reserva", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			panellblEliminar.setLayout(new BorderLayout(0, 0));
			panellblEliminar.add(getPanelbtnEliminar());
			panellblEliminar.add(getLabel_1(), BorderLayout.NORTH);
		}
		return panellblEliminar;
	}
	private JPanel getPanelbtnEliminar() {
		if (panelbtnEliminar == null) {
			panelbtnEliminar = new JPanel();
			panelbtnEliminar.setBorder(new EmptyBorder(20, 0, 20, 0));
			panelbtnEliminar.setLayout(new BorderLayout(0, 0));
			panelbtnEliminar.add(getBtnEliminar());
		}
		return panelbtnEliminar;
	}
	private JPanel getPanelAñadir() {
		if (panelAñadir == null) {
			panelAñadir = new JPanel();
			panelAñadir.setBorder(new EmptyBorder(20, 0, 20, 0));
			panelAñadir.setLayout(new BorderLayout(0, 0));
			panelAñadir.add(getBtnReservar());
		}
		return panelAñadir;
	}
	
	private void actualizarTabla() {
		modeloTabla.getDataVector().removeAllElements();
		añadirElementosTabla(modeloTabla,poli.getSocioByUser(usuario_conectado), ((Actividad)comboBoxActividades.getSelectedItem()).getCodActividad());
		modeloTabla.fireTableDataChanged();
	}
	
	private void guardadoDeDatos(Actividad a, Reserva r, Socio sc){
		//***** LOCAL ****
		apuntarmeActividad(sc,r, a);
		//***** BD ******
		try {
			//compruebo que no exista ya...si no actualiza el valor CANCELADA en la tabla APUNTADO_A
			boolean existApuntadoA = poli.getConexion().existeEnApuntadoA(sc.getDNI(), r.getIdReserva(), a.getCodActividad()); // si existe estará 100% en candelada = 'true' por que si no, no llegaría hasta este punto y habría salgo un mensaje de que ya esta apuntado
			if(existApuntadoA)
				poli.getConexion().cambiarEstadoCanceladaApuntadoA(sc.getDNI(),r.getIdReserva(), a.getCodActividad(), "False");
			else
				poli.getConexion().apuntarUsuarioActividad(sc.getDNI(),r.getIdReserva(),a.getCodActividad());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private String[] crearNombreColumnas() {
		String[] col = new String[8];
		col[0] = "Instalación";
		col[1] = "Fecha inicial";
		col[2] = "Fecha final";
		col[3] = "Duración";
		col[4] = "Reserva";
		col[5] = "Plazas disponibles";
		col[6] = "idReserva";
		col[7] = "codAct";
		return col;
	}
	
	private void añadirElementosTabla(ModeloNoEditable modeloTabla, Socio sc, String codAct) {
		Object[] row = new Object[8];
		for(Actividad a: poli.getActividades()) {
			if(!rdbtnVerTodas.isSelected()) {
				if(a.getCodActividad().equals(codAct)) {
					ArrayList<Reserva> r = a.obtenerReservasActDeUnSocio(sc);
					for (Map.Entry<Reserva, ArrayList<Socio>> res: a.getApuntadosActividades().entrySet()) {
						Calendar c1 = Calendar.getInstance(); c1.setTime(res.getKey().getFechaReserva()); c1.add(Calendar.HOUR_OF_DAY, res.getKey().getTiempoReserva());
						String fechaConFormato1 = formato1.format(res.getKey().getFechaReserva()),  fechaConFormato2 = formato1.format(c1.getTime());
						row[0] = res.getKey().getInstalacion();
						row[1] = fechaConFormato1;
						row[2] = fechaConFormato2;
						row[3] = res.getKey().getTiempoReserva()+"h";
						row[4] = (r.contains(res.getKey()))?"APUNTADO":"-";
						row[5] = a.plazasDisponibles(res.getKey());
						row[6] = res.getKey();
						row[7] = a;
						modeloTabla.addRow(row);
					}
				}
			}else {
				ArrayList<Reserva> r = a.obtenerReservasActDeUnSocio(sc);
				for (Map.Entry<Reserva, ArrayList<Socio>> res: a.getApuntadosActividades().entrySet()) {
					Calendar c1 = Calendar.getInstance(); c1.setTime(res.getKey().getFechaReserva()); c1.add(Calendar.HOUR_OF_DAY, res.getKey().getTiempoReserva());
					String fechaConFormato1 = formato1.format(res.getKey().getFechaReserva()),  fechaConFormato2 = formato1.format(c1.getTime());
					row[0] = res.getKey().getInstalacion();
					row[1] = fechaConFormato1;
					row[2] = fechaConFormato2;
					row[3] = res.getKey().getTiempoReserva()+"h";
					row[4] = (r.contains(res.getKey()))?"APUNTADO":"-";
					row[5] = a.plazasDisponibles(res.getKey());
					row[6] = res.getKey();
					row[7] = a;
					modeloTabla.addRow(row);
				}	
			}
		}
	}
	
	private boolean comprobarPlazasDisponibles(Reserva r, Actividad act) {
		return act.disponible(r);
	}
	
	private void apuntarmeActividad(Socio sc, Reserva r,  Actividad act) {
		act.apuntarme(r, sc);
	}
	
	private void desapuntarmeActividad(Socio sc, Reserva r,  Actividad act) {
		act.desapuntarmme(r, sc);
	}

	private boolean comprobarDisponibilidadUsuario(Socio sc, Reserva intento) {
		try {
			boolean comprobarReservasAInstalaciones = comprobarReservaInstalaciones(sc, intento);
			boolean comprobarReservasAActividades = comprobarReservaActividades(sc, intento, poli.getConexion().reservasActividadUsuario(sc.getDNI()));
			return comprobarReservasAInstalaciones && comprobarReservasAActividades;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	private boolean comprobarReservaInstalaciones(Socio sc, Reserva intento) {
		for(Reserva r: poli.getReservas()) {	
			if(!r.equals(intento) && !r.isReservaCentro()) {
				if(r.getEstado().equals(Reserva.LIBRE) && r.getCliente().getDNI().equals(sc.getDNI())) {
					if(poli.comprobarFechas(r,intento)) {
						JOptionPane.showMessageDialog(null, "Tienes una reserva de instalación a la misma hora");
						return false; //no esta disponible
					}
				}
			}
		}
		return true;//esta disponible
	}
	
	private boolean comprobarReservaActividades(Socio sc, Reserva intento, ArrayList<Reserva> res) {
		if(!res.isEmpty()) {	
			for(Reserva r: res) {
				if(r.equals(intento)) {
					JOptionPane.showMessageDialog(null,"Ya está apuntado.");
					return false;
				}
				if(r.getEstado().equals(Reserva.LIBRE)) {
					if(poli.comprobarFechas(r,intento)) {
						JOptionPane.showMessageDialog(null,"Tienes una reserva de actividad a la misma hora.");
						return false; //no esta disponible
					}
				}
				
			}
		}
		return true;//esta disponible
	}
	
	private JPanel getPanelSupremeteTabla() {
		if (panelSupremeteTabla == null) {
			panelSupremeteTabla = new JPanel();
			panelSupremeteTabla.setBorder(new TitledBorder(null, "Actividades", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			panelSupremeteTabla.setLayout(new BorderLayout(0, 0));
			panelSupremeteTabla.add(getScrollPaneTabla());
		}
		return panelSupremeteTabla;
	}
	
	private JComboBox<Actividad> getComboBoxActividades() {
		if (comboBoxActividades == null) {
			comboBoxActividades = new JComboBox<Actividad>();
			DefaultComboBoxModel<Actividad> mdl = new DefaultComboBoxModel<Actividad>();
			mdl = llenarComboActividades(mdl);
			comboBoxActividades.setModel(mdl);
			comboBoxActividades.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					actualizarTabla();
				}
			});
		}
		return comboBoxActividades;
	}
	
	private JRadioButton getRdbtnVerTodas() {
		if (rdbtnVerTodas == null) {
			rdbtnVerTodas = new JRadioButton("Ver todas");
			rdbtnVerTodas.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(comboBoxActividades.isEnabled()) {
						comboBoxActividades.setEnabled(false);
						actualizarTabla();
					}
					else {
						comboBoxActividades.setEnabled(true);
						actualizarTabla();
					}
				}
			});
		}
		return rdbtnVerTodas;
	}
	private JPanel getPanel() {
		if (panel == null) {
			panel = new JPanel();
			panel.add(getRdbtnVerTodas());
		}
		return panel;
	}
	private JButton getBtnReservar() {
		if (btnReservar == null) {
			btnReservar = new JButton("Reservar");
			btnReservar.setFont(new Font("Tahoma", Font.PLAIN, 20));
			btnReservar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(tablaMisActividades.getSelectedRow() != -1) {
						Actividad a = (Actividad)tablaMisActividades.getModel().getValueAt(tablaMisActividades.getSelectedRow(), 7);
						Reserva r =  (Reserva)tablaMisActividades.getModel().getValueAt(tablaMisActividades.getSelectedRow(), 6);
						Socio sc = poli.getSocioByUser(usuario_conectado);
						if(tiempoAdecuadoActividad(r)) {
							if(comprobarDisponibilidadUsuario(sc,r)){ //dentro de este método enseño mas mensajes por eso no hay else
									if(comprobarPlazasDisponibles(r, a)) {
										guardadoDeDatos(a, r, sc); actualizarTabla();
									}
									else
										JOptionPane.showMessageDialog(null,"No hay plazas disponibles");
							}
						} else
							JOptionPane.showMessageDialog(null,"Puede apuntarse a la actividad entre 1h y 24h antes de la hora inicial");
					}else
						JOptionPane.showMessageDialog(null,"Seleccione una actividad");
				}
			});
		}
		return btnReservar;
	}
	
	private boolean tiempoAdecuadoActividad(Reserva r) {
		Calendar c1 = Calendar.getInstance(); c1.setTime(new Date()); Calendar c2 = Calendar.getInstance(); c2.setTime(r.getFechaReserva());
		Calendar c3 = Calendar.getInstance(); c3.setTime(r.getFechaReserva());c2.add(Calendar.HOUR_OF_DAY, -1);c3.add(Calendar.HOUR_OF_DAY, -24);
		if((c1.after(c3) || c1.equals(c3)) && (c1.before(c2) || c1.equals(c2)))
			return true;
		return false;
	}
	private JButton getBtnEliminar() {
		if (btnEliminar == null) {
			btnEliminar = new JButton("Eliminar");
			btnEliminar.setFont(new Font("Tahoma", Font.PLAIN, 20));
			btnEliminar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(tablaMisActividades.getSelectedRow() != -1) {
						String value = (String)tablaMisActividades.getModel().getValueAt(tablaMisActividades.getSelectedRow(), 4);
						if(value.equals("APUNTADO")) {
							Actividad a = (Actividad)tablaMisActividades.getModel().getValueAt(tablaMisActividades.getSelectedRow(), 7);
							Reserva r =  (Reserva)tablaMisActividades.getModel().getValueAt(tablaMisActividades.getSelectedRow(), 6);
							Socio sc = poli.getSocioByUser(usuario_conectado);
							borrarReservaActividad(a,r,sc);actualizarTabla();
						} else
							JOptionPane.showMessageDialog(null, "No está apuntado a la actividad seleccionada");
					} else 
						JOptionPane.showMessageDialog(null, "Seleccione una actividad en la que se encuentre apuntado");
				}
			});
		}
		return btnEliminar;
	}
	
	public void borrarReservaActividad(Actividad a, Reserva r, Socio sc) {
		//******* LOCAL ********
		desapuntarmeActividad(sc, r, a);
		//******* BD ********
		try {
			poli.getConexion().cambiarEstadoCanceladaApuntadoA(sc.getDNI(), r.getIdReserva(), a.getCodActividad(), "True");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private JPanel getPanelSupremeBotones() {
		if (panelSupremeBotones == null) {
			panelSupremeBotones = new JPanel();
			panelSupremeBotones.setLayout(new BorderLayout(0, 0));
			panelSupremeBotones.add(getPanel_2());
		}
		return panelSupremeBotones;
	}
	private JLabel getLabel() {
		if (label == null) {
			label = new JLabel("                                                           ");
		}
		return label;
	}
	private JLabel getLabel_1() {
		if (label_1 == null) {
			label_1 = new JLabel("                                                        ");
		}
		return label_1;
	}
	private JPanel getPanel_3() {
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
			btnSalir = new JButton("Salir");
			btnSalir.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});
		}
		return btnSalir;
	}
	private JPanel getPanelCombo() {
		if (panelCombo == null) {
			panelCombo = new JPanel();
			panelCombo.setBorder(new EmptyBorder(0, 10, 0, 10));
			panelCombo.setLayout(new BorderLayout(0, 0));
			panelCombo.add(getPanelActividades(), BorderLayout.WEST);
			panelCombo.add(getPanelComboActividades());
			panelCombo.add(getPanel(), BorderLayout.SOUTH);
		}
		return panelCombo;
	}
	private JLabel getLblActividad() {
		if (lblActividad == null) {
			lblActividad = new JLabel("Actividad: ");
		}
		return lblActividad;
	}
	
	private DefaultComboBoxModel<Actividad> llenarComboActividades(DefaultComboBoxModel<Actividad> model){
		for(Actividad a: poli.getActividades())
			model.addElement(a);
		return model;
	}
	
	private JPanel getPanelActividades() {
		if (panelActividades == null) {
			panelActividades = new JPanel();
			panelActividades.setLayout(new BorderLayout(0, 0));
			panelActividades.add(getLblActividad(), BorderLayout.EAST);
		}
		return panelActividades;
	}
	private JPanel getPanelComboActividades() {
		if (panelComboActividades == null) {
			panelComboActividades = new JPanel();
			panelComboActividades.setBorder(new EmptyBorder(30, 0, 30, 0));
			panelComboActividades.setLayout(new BorderLayout(0, 0));
			panelComboActividades.add(getComboBoxActividades());
//			actualizarTabla();
		}
		return panelComboActividades;
	}


}
