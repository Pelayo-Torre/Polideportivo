package igu;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import logica.Actividad;
import logica.Polideportivo;
import logica.Reserva;
import logica.Socio;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.Toolkit;
import javax.swing.JComboBox;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.DefaultComboBoxModel;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;

@SuppressWarnings("serial")
public class MonitorVentana extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JButton btnEliminar;
	private JComboBox<Actividad> cbActividad;
	private JPanel panel_1;
	private JLabel lblNewLabel;
	private JPanel panel_2;
	private JButton btnAadirALa;
	
	private ModeloNoEditable modeloTabla;
	private Polideportivo poli;
	private JScrollPane scrollPane;
	private JTable table;
	private JButton btnCerrarSesion;
	
	private MonitorVentana mv;
	private JPanel panel_3;
	private JLabel lbl;
	private JLabel lblPlazasRestantes;
	private JLabel lblSeleccionaElHorario;
	private JPanel panel_4;
	private JComboBox<Reserva> cbHorario;
	private Socio socioAAñadir;
	
//	/**
//	 * Launch the application.
//	 */
//	public static void main(String[] args) {
//		try {
//			MonitorVentana dialog = new MonitorVentana(new Polideportivo());
//			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
//			dialog.setVisible(true);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

	/**
	 * Create the dialog.
	 */
	public MonitorVentana(Polideportivo polideportivo) {
		poli=polideportivo;
		mv=this;
		String[] nombreColumnas={"Nombre","Apellidos","DNI","NºSocio"};
		modeloTabla= new ModeloNoEditable(nombreColumnas,0);
		
		setIconImage(Toolkit.getDefaultToolkit().getImage(MonitorVentana.class.getResource("/img/icon.jpg")));
		setTitle("Polideportivo: Monitor");
		setBounds(100, 100, 718, 854);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		contentPanel.add(getScrollPane(), BorderLayout.CENTER);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			buttonPane.add(getBtnCerrarSesion());
		}
		{
			JPanel panel = new JPanel();
			getContentPane().add(panel, BorderLayout.EAST);
			panel.setLayout(new GridLayout(0, 1, 0, 0));
			panel.add(getPanel_1());
			panel.add(getPanel_4());
			panel.add(getPanel_3());
			panel.add(getPanel_2());
		}
	}
	private JButton getBtnEliminar() {
		if (btnEliminar == null) {
			btnEliminar = new JButton("Eliminar de la lista");
			btnEliminar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					eliminarClienteDeActividad();					
					modeloTabla.getDataVector().removeAllElements();
					añadirApuntadosATabla();
					modeloTabla.fireTableDataChanged();
					actualizarPlazasRestantes();
					btnEliminar.setEnabled(false);
				}
			});
			btnEliminar.setEnabled(false);
		}
		return btnEliminar;
	}
	/*
	 * Elimina el cliente seleccionado en la tabla de la lista 
	 * para esa actividad en ese horario.
	 */
	private void eliminarClienteDeActividad() {
		String dni = (String) getTable().getModel().getValueAt(getTable().getSelectedRow(), 2);
		Socio sc= (Socio) poli.getConexion().obtenerCliente(dni, poli.getClientes());
		Actividad actividad = (Actividad) getCbActividad().getSelectedItem();
		Reserva reserva = (Reserva) getCbHorario().getSelectedItem();
		actividad.desapuntarmme(reserva, sc);
		try {
			poli.getConexion().cambiarEstadoCanceladaApuntadoA(dni, reserva.getIdReserva(), actividad.getCodActividad(),"True");
		} catch (SQLException e) {
			System.err.println("Problemas al desapuntar a un socio de una actividad en la bd");
			e.printStackTrace();
		}
	}
	private JComboBox<Actividad> getCbActividad() {
		if (cbActividad == null) {
			cbActividad = new JComboBox<Actividad>();
			cbActividad.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					obtenerHorario();
					modeloTabla.getDataVector().removeAllElements();
					añadirApuntadosATabla();
					modeloTabla.fireTableDataChanged();
					actualizarPlazasRestantes();
					getBtnEliminar().setEnabled(false);
				}
			});
			cbActividad.setModel(new DefaultComboBoxModel<Actividad>(obtenerActividades()));
			if(cbActividad.getModel().getSize()>0)
				cbActividad.setSelectedIndex(0);

		}
		return cbActividad;
	}
	/*
	 * Coloca el valor de las plazas que quedan en la label asignadaa ello
	 * teniendo en cuenta la actividad seleccionada y el horario
	 */
	private void actualizarPlazasRestantes() {
		Actividad actividad = (Actividad) getCbActividad().getSelectedItem();
		Reserva reserva = (Reserva) getCbHorario().getSelectedItem();
		if(actividad==null || reserva==null) 
			return;
		int plazasRestantes = actividad.plazasDisponibles(reserva);
		getLblPlazasRestantes().setText(String.valueOf(plazasRestantes));
		if(plazasRestantes==0)
			getBtnAadirALaLista().setEnabled(false);
		else 
			getBtnAadirALaLista().setEnabled(true);
			
	}
	/*
	 * Borra los horarios anteriores del combo y lo llena con las
	 * reservas del día actual para la actividad seleccionada
	 */
	private void obtenerHorario() {
		getCbHorario().removeAllItems();
		if(getCbActividad().getSelectedItem()==null)
			return;
		ArrayList<Reserva> reservasHoy=((Actividad) getCbActividad().getSelectedItem()).obtenerReservasHoy();
		Reserva[] reservas = new Reserva[reservasHoy.size()];
		reservas = reservasHoy.toArray(reservas);
		getCbHorario().setModel(new DefaultComboBoxModel<Reserva>(reservas));
	}
	/*
	 * Añade las actividades que tienen plazas limitadas y
	 * tienen clases para el día de hoy al cb de Activididades
	 */
	private Actividad[] obtenerActividades() {
		
		int nActividades=0;
		for(Actividad a: poli.getActividades()) {
			if(a.tienePlazasLimitadas()&& !a.obtenerReservasHoy().isEmpty()) 
				nActividades++;
		}
		//Por si no hay actividades que cunplan los resquisitos
		nActividades=(nActividades==0?1:nActividades);
		
		Actividad[] actividades = new Actividad[nActividades];
		int cont=0;
		for(Actividad a: poli.getActividades()) {
			if(a.tienePlazasLimitadas() && !a.obtenerReservasHoy().isEmpty()) 
				actividades[cont++]=a;;
		}
		return actividades;
	}
	private JPanel getPanel_1() {
		if (panel_1 == null) {
			panel_1 = new JPanel();
			panel_1.add(getLblNewLabel());
			panel_1.add(getCbActividad());
		}
		return panel_1;
	}
	private JLabel getLblNewLabel() {
		if (lblNewLabel == null) {
			lblNewLabel = new JLabel("Selecciona la actividad:");
		}
		return lblNewLabel;
	}
	private JPanel getPanel_2() {
		if (panel_2 == null) {
			panel_2 = new JPanel();
			FlowLayout fl_panel_2 = new FlowLayout(FlowLayout.CENTER, 5, 5);
			panel_2.setLayout(fl_panel_2);
			panel_2.add(getBtnEliminar());
			panel_2.add(getBtnAadirALaLista());
		}
		return panel_2;
	}
	private JButton getBtnAadirALaLista() {
		if (btnAadirALa == null) {
			btnAadirALa = new JButton("A\u00F1adir a la lista");
			btnAadirALa.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Reserva r = (Reserva) getCbHorario().getSelectedItem();
					abrirVentanaApuntar(r);
					if(getSocioAAñadir()!=null) {
						Actividad a = (Actividad) getCbActividad().getSelectedItem(); 
						a.apuntarme(r,getSocioAAñadir());
						try {
//							poli.getConexion().apuntarUsuarioActividad(getSocioAAñadir().getDNI(), r.getIdReserva(), a.getCodActividad());
							boolean existApuntadoA = poli.getConexion().existeEnApuntadoA(getSocioAAñadir().getDNI(), r.getIdReserva(), a.getCodActividad()); // si existe estará 100% en candelada = 'true' por que si no, no llegaría hasta este punto y habría salgo un mensaje de que ya esta apuntado
							if(existApuntadoA)
								poli.getConexion().cambiarEstadoCanceladaApuntadoA(getSocioAAñadir().getDNI(),r.getIdReserva(), a.getCodActividad(), "False");
							else
								poli.getConexion().apuntarUsuarioActividad(getSocioAAñadir().getDNI(),r.getIdReserva(),a.getCodActividad());
						} catch (SQLException e1) {
							System.err.println("Problemas al apuntar un socio a una actividad");
							e1.printStackTrace();
						}
						
						modeloTabla.getDataVector().removeAllElements();
						añadirApuntadosATabla();
						modeloTabla.fireTableDataChanged();
						actualizarPlazasRestantes();
						getBtnEliminar().setEnabled(false);
						
					}
				}
			});
			btnAadirALa.setEnabled(false);
		}
		return btnAadirALa;
	}
	private void abrirVentanaApuntar(Reserva r) {
		AñadirSocioActividadPlazaVacante as = new AñadirSocioActividadPlazaVacante(this,r);
		as.setModal(true);
		as.setLocationRelativeTo(this);
		as.setVisible(true);
	}
	private JScrollPane getScrollPane() {
		if (scrollPane == null) {
			scrollPane = new JScrollPane();
			scrollPane.setViewportView(getTable());
		}
		return scrollPane;
	}
	private JTable getTable() {
		if (table == null) {
			table = new JTable();
			table.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent arg0) {
					getBtnEliminar().setEnabled(true);
				}
			});
			table.setModel(modeloTabla);
//			añadirApuntadosATabla();
			
		}
		return table;
	}
	/**
	 * Dada la activdad elegida en el combo y el horario seleccionado,
	 * añade a la tabla los usuarios que se han apuntado en ella.
	 */
	private void añadirApuntadosATabla() {
		Actividad actividad = (Actividad) getCbActividad().getSelectedItem();
		Reserva reserva = (Reserva) getCbHorario().getSelectedItem();
		if(actividad==null || reserva==null)
			return;
		ArrayList<Socio> sociosApuntados = actividad.getApuntadosActividades().get(reserva);
		
		for (Socio socio : sociosApuntados) {
			String[] fila = new String[4];
			fila[0] = socio.getNombre();
			fila[1] = socio.getApellidos();
			fila[2] = socio.getDNI();
			fila[3] = String.valueOf(socio.getNumeroSocio());
			modeloTabla.addRow(fila);
		}
	}
	private JButton getBtnCerrarSesion() {
		
		if (btnCerrarSesion == null) {
			btnCerrarSesion = new JButton("Cerrar Sesi\u00F3n");
			btnCerrarSesion.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					int resultado = JOptionPane.showConfirmDialog(mv, "¿Está seguro de que quiere cerrar sesión?","Cerrar sesión",JOptionPane.YES_NO_OPTION);
					if(resultado == JOptionPane.YES_OPTION)
					{
						dispose();
					}
				
				}
			});
		}
		return btnCerrarSesion;
	}
	private JPanel getPanel_3() {
		if (panel_3 == null) {
			panel_3 = new JPanel();
			panel_3.add(getLbl());
			panel_3.add(getLblPlazasRestantes());
		}
		return panel_3;
	}
	private JLabel getLbl() {
		if (lbl == null) {
			lbl = new JLabel("Plazas restantes :");
			lbl.setFont(new Font("Tahoma", Font.PLAIN, 14));
		}
		return lbl;
	}
	private JLabel getLblPlazasRestantes() {
		if (lblPlazasRestantes == null) {
			lblPlazasRestantes = new JLabel();
			lblPlazasRestantes.setFont(new Font("Tahoma", Font.BOLD, 15));
		}
		return lblPlazasRestantes;
	}
	private JLabel getLblSeleccionaElHorario() {
		if (lblSeleccionaElHorario == null) {
			lblSeleccionaElHorario = new JLabel("Selecciona el horario:");
		}
		return lblSeleccionaElHorario;
	}
	private JPanel getPanel_4() {
		if (panel_4 == null) {
			panel_4 = new JPanel();
			panel_4.add(getLblSeleccionaElHorario());
			panel_4.add(getCbHorario());
		}
		return panel_4;
	}
	private JComboBox<Reserva> getCbHorario() {
		if (cbHorario == null) {
			cbHorario = new JComboBox<Reserva>();
			cbHorario.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					modeloTabla.getDataVector().removeAllElements();
					añadirApuntadosATabla();
					modeloTabla.fireTableDataChanged();
					actualizarPlazasRestantes();
					getBtnEliminar().setEnabled(false);
				}
			});
			obtenerHorario();
			if(cbHorario.getModel().getSize()>0)
				cbHorario.setSelectedIndex(0);

		}
		return cbHorario;
	}
	
	public Socio getSocioAAñadir() {
		return socioAAñadir;
	}
	public void setSocioAAñadir(Socio s) {
		socioAAñadir=s;
	}
	public Polideportivo getPolideportivo() {
		return poli;
	}
}
