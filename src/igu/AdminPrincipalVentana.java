package igu;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import java.awt.Toolkit;

import javax.swing.border.TitledBorder;

import logica.Polideportivo;

import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.HashMap;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;


@SuppressWarnings({ "unused", "serial" })
public class AdminPrincipalVentana extends JDialog {

	
	private final JPanel contentPanel = new JPanel();
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JButton btnReservar;
	private JButton btnDisponibilidad;
	private JButton btnCerrarSesin;
	private JPanel panel_1;
	private JButton brtn;
	private JRadioButton rdbtnActividad;
	private JRadioButton rdbtnSocio;
	private JRadioButton rdbtnNoSocio;
	private AdminPrincipalVentana apv;
	private Polideportivo polideportivo;
	private JButton botonCancelar;
	private JButton botonCancelarActividad;
	private JRadioButton rdbtnIns;
	private JButton botonInstalacion;
	private JButton btnLiquidarFacturas;
	private JComboBox<String> cbMes;
	private JComboBox<String> cbAno;
	private JPanel panel_2;
	private HashMap<String,String> meses;


	public AdminPrincipalVentana(Polideportivo polideportivo) {
//		asignarMeses();
//		setIconImage(Toolkit.getDefaultToolkit().getImage(ReservaInstalacionAdmVentana.class.getResource("/img/icon.jpg")));
//		apv=this;
//		this.polideportivo=polideportivo;
//		setTitle("Polideportivo: Administrador");
//		setResizable(false);
//		setModal(true);
//		setBounds(100, 100, 709, 274);
//		getContentPane().setLayout(new BorderLayout());
//		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
//		getContentPane().add(contentPanel, BorderLayout.CENTER);
//		contentPanel.setLayout(null);
//		{
//			JPanel panel = new JPanel();
//			panel.setBorder(new TitledBorder(null, "Selecciona el tipo de Reserva", TitledBorder.LEADING, TitledBorder.TOP, null, null));
//			panel.setBounds(10, 21, 196, 213);
//			contentPanel.add(panel);
//			panel.setLayout(null);
//			panel.add(getRdbtnActividad());
//			panel.add(getRdbtnSocio());
//			panel.add(getRdbtnNoSocio());
//			panel.add(getRdbtnIns());
//			
//		}
//		contentPanel.add(getBtnDisponibilidad());
//		contentPanel.add(getBtnCerrarSesin());
//		contentPanel.add(getPanel_1());
//		contentPanel.add(getBotonInstalacion());
//		contentPanel.add(getPanel_2());
//	}
//	
//	private JRadioButton getRdbtnActividad() {
//		if(rdbtnActividad==null) {
//			rdbtnActividad = new JRadioButton("Actividad");
//			rdbtnActividad.addActionListener(new ActionListener() {
//				public void actionPerformed(ActionEvent e) {
//					if(!getBtnReservar().isEnabled()) {
//						getBtnReservar().setEnabled(true);
//					}
//				}
//			});
//			rdbtnActividad.setBounds(26, 28, 122, 23);
//			buttonGroup.add(rdbtnActividad);
//		}
//		return rdbtnActividad;
//	}
//	
//	private JRadioButton getRdbtnSocio(){
//		if(rdbtnSocio==null) {
//			rdbtnSocio = new JRadioButton("Socio");
//			rdbtnSocio.addActionListener(new ActionListener() {
//				public void actionPerformed(ActionEvent e) {
//					if(!getBtnReservar().isEnabled()) {
//						getBtnReservar().setEnabled(true);
//					}
//				}
//			});
//			rdbtnSocio.setBounds(26, 74, 107, 23);
//			buttonGroup.add(rdbtnSocio);	
//		}
//		return rdbtnSocio;
//	}
//	
//	private JRadioButton getRdbtnNoSocio(){
//		if(rdbtnNoSocio==null) {
//			rdbtnNoSocio = new JRadioButton("No Socio");
//			rdbtnNoSocio.addActionListener(new ActionListener() {
//				public void actionPerformed(ActionEvent e) {
//					if(!getBtnReservar().isEnabled()) {
//						getBtnReservar().setEnabled(true);
//					}
//				}
//			});
//			rdbtnNoSocio.setBounds(26, 120, 107, 23);
//			buttonGroup.add(rdbtnNoSocio);	
//		}
//		return rdbtnNoSocio;
//	}
//	
//	private JButton getBtnReservar() {
//		if (btnReservar == null) {
//			btnReservar = new JButton("Reservar");
//			btnReservar.setBounds(12, 23, 214, 35);
//			btnReservar.addActionListener(new ActionListener() {
//				public void actionPerformed(ActionEvent e) {
//					if(getRdbtnSocio().isSelected())
//						abrirVentanaReservaSocio();
//					if(getRdbtnNoSocio().isSelected())
//						abrirVentanaReservaNOSocio();
//					if(getRdbtnActividad().isSelected()) 
//						abrirVentanaReservaActividad();
//					if(getRdbtnIns().isSelected())
//						abrirVentanaReservaInstalacion();
//				}
//			});
//			btnReservar.setEnabled(false);
//		}
//		return btnReservar;
//	}
////	
////	private void abrirVentanaReservaInstalacion() {
////		ReservaInstalacionAdmVentana ra = new ReservaInstalacionAdmVentana(this);
////		ra.setLocationRelativeTo(this);
////		ra.setModal(true);
////		ra.setVisible(true);
////	}
////	
////	private void abrirVentanaReservaActividad() {
////		ReservaActividad ra = new ReservaActividad(this);
////		ra.setLocationRelativeTo(this);
////		ra.setModal(true);
////		ra.setVisible(true);
////		
////	}
////	
////	private void abrirVentanaReservaNOSocio() {
////		ReservaNoSociosVentana vrns= new ReservaNoSociosVentana(this);
////		vrns.setLocationRelativeTo(this);
////		vrns.setModal(true);
////		vrns.setVisible(true);
////		
////		
////	}
////	
////	private void abrirVentanaReservaSocio() {
////		ReservaSociosVentana vrs= new ReservaSociosVentana(this);
////		vrs.setLocationRelativeTo(this);
////		vrs.setVisible(true);
////		
//////		vrs.setModal(true);
////	}
////	
////	private JButton getBtnDisponibilidad() {
////		if (btnDisponibilidad == null) {
////			btnDisponibilidad = new JButton("Disponibilidad");
////			btnDisponibilidad.addActionListener(new ActionListener() {
////				public void actionPerformed(ActionEvent e) {
////					abrirVentanaHorariosAdmin();
////				}
////
////				
////			});
////			btnDisponibilidad.setBounds(464, 89, 214, 35);
////		}
////		return btnDisponibilidad;
////	}
////	
////	private void abrirVentanaHorariosAdmin() {
////		HorariosAdminVentana hva= new HorariosAdminVentana(getPolideportivo());
////		hva.setLocationRelativeTo(this);
////		hva.setModal(true);
////		hva.setVisible(true);
////	}
////	
//	private JButton getBtnCerrarSesin() {
//		if (btnCerrarSesin == null) {
//			btnCerrarSesin = new JButton("Cerrar Sesi\u00F3n");
//			btnCerrarSesin.addActionListener(new ActionListener() {
//				public void actionPerformed(ActionEvent e) {
//					int resultado = JOptionPane.showConfirmDialog(apv, "¿Está seguro de que quiere cerrar sesión?","Cerrar sesión",JOptionPane.YES_NO_OPTION);
//					if(resultado == JOptionPane.YES_OPTION)
//					{
//						dispose();
//					}
//				}
//			});
//			btnCerrarSesin.setBounds(560, 11, 133, 23);
//		}
//		return btnCerrarSesin;
//	}
//	private JPanel getPanel_1() {
//		if (panel_1 == null) {
//			panel_1 = new JPanel();
//			panel_1.setBorder(new TitledBorder(null, "Gestionar Reservas", TitledBorder.LEADING, TitledBorder.TOP, null, null));
//			panel_1.setBounds(218, 21, 236, 213);
//			panel_1.setLayout(null);
//			panel_1.add(getBrtn());
//			panel_1.add(getBotonCancelar());
//			panel_1.add(getBtnReservar());
//			panel_1.add(getBotonCancelarActividad());
//		}
//		return panel_1;
//	}
//	private JButton getBrtn() {
//		if (brtn == null) {
//			brtn = new JButton("Confirmar Asistencia");
//			brtn.addActionListener(new ActionListener() {
//				public void actionPerformed(ActionEvent e) {
//					abrirVentanaReservaEfectiva();
//				}
//			});
//			brtn.setBounds(10, 116, 214, 35);
//		}
//		return brtn;
//	}
//	
//	private void abrirVentanaReservaEfectiva() {
//		ReservaEfectiva hva= new ReservaEfectiva(getPolideportivo());
//		hva.setLocationRelativeTo(this);
//		hva.setModal(true);
//		hva.setVisible(true);
//	}
//	
//	private void abrirVentanaAdministradorCancelaReservas(){
//		AdministradorCancelaReservaNoCentroVentana acrv = new AdministradorCancelaReservaNoCentroVentana(this);
//		acrv.setLocationRelativeTo(this);
//		acrv.setModal(true);
//		acrv.setVisible(true);
//	}
//	
//	public Polideportivo getPolideportivo() {
//		return polideportivo;
//	}
//	
//	private void abrirVentanaCancelarReservaActividad() {
//		CancelarReservaCentroAdm crca = new CancelarReservaCentroAdm(this);
//		crca.setLocationRelativeTo(this);
//		crca.setModal(true);
//		crca.setVisible(true);
//	}
//	
//	private void abrirVentanaCancelarReservaSocioActividad(){
//		AdministradorCancelaActividadSocio acas = new AdministradorCancelaActividadSocio(this);
//		acas.setLocationRelativeTo(this);
//		acas.setModal(true);
//		acas.setVisible(true);
//	}
//	
//	private void abrirVentanaAdminPoneInstalacionesNoDisponibles(){
//		AdminPoneInstalacionesNoDisponibles apind = new AdminPoneInstalacionesNoDisponibles(this);
//		apind.setLocationRelativeTo(this);
//		apind.setModal(true);
//		apind.setVisible(true);
//	}
//	
//	private JButton getBotonCancelar() {
//		if (botonCancelar == null) {
//			botonCancelar = new JButton("Cancelar Reservas");
//			botonCancelar.addActionListener(new ActionListener() {
//				public void actionPerformed(ActionEvent arg0) {
//					if(getRdbtnActividad().isSelected())
//						abrirVentanaCancelarReservaActividad();
//					else
//						abrirVentanaAdministradorCancelaReservas();
//				}
//			});
//			botonCancelar.setBounds(10, 67, 214, 36);
//		}
//		return botonCancelar;
//	}
//	private JButton getBotonCancelarActividad() {
//		if (botonCancelarActividad == null) {
//			botonCancelarActividad = new JButton("Cancelar reserva socio actividad");
//			botonCancelarActividad.setBounds(10, 162, 214, 35);
//			botonCancelarActividad.addActionListener(new ActionListener() {
//				public void actionPerformed(ActionEvent arg0) {
//					abrirVentanaCancelarReservaSocioActividad();
//				}
//			});
//		}
//		return botonCancelarActividad;
//	}
//	private JRadioButton getRdbtnIns() {
//		if (rdbtnIns == null) {
//			rdbtnIns = new JRadioButton("Instalaci\u00F3n");
//			rdbtnIns.addActionListener(new ActionListener() {
//				public void actionPerformed(ActionEvent arg0) {
//					if(!getBtnReservar().isEnabled()) {
//						getBtnReservar().setEnabled(true);
//					}
//				}
//			});
//			buttonGroup.add(rdbtnIns);
//			rdbtnIns.setBounds(26, 164, 127, 25);
//		}
//		return rdbtnIns;
//	}
//	private JButton getBotonInstalacion() {
//		if (botonInstalacion == null) {
//			botonInstalacion = new JButton("Cancelar Instalaci\u00F3n");
//			botonInstalacion.addActionListener(new ActionListener() {
//				public void actionPerformed(ActionEvent arg0) {
//					abrirVentanaAdminPoneInstalacionesNoDisponibles();
//				}
//			});
//			botonInstalacion.setBounds(464, 44, 214, 35);
//		}
//		return botonInstalacion;
//	}
//	private JButton getBtnLiquidarFacturas() {
//		if (btnLiquidarFacturas == null) {
//			btnLiquidarFacturas = new JButton("Liquidar facturas");
//			btnLiquidarFacturas.setBounds(31, 48, 150, 35);
//			btnLiquidarFacturas.addActionListener(new ActionListener() {
//				public void actionPerformed(ActionEvent arg0) {
//					String idCuota = generarIdCuota();
//					if(comprobarFechaLiquidacion()) {
//						polideportivo.liquidarFacturasMes(idCuota);
//						mostrarVentanaLiquidacion(idCuota);
//					}
//					else
//						JOptionPane.showMessageDialog(apv, "Introduce una fecha correcta");
//				}
//			});
//		}
//		return btnLiquidarFacturas;
//	}
//	private void mostrarVentanaLiquidacion(String idCuota) {
//		LiquidarFacturasVentana lf = new LiquidarFacturasVentana(idCuota,polideportivo);
//		lf.setLocationRelativeTo(this);
//		lf.setModal(true);
//		lf.setVisible(true);
//		
//	}
//	private JComboBox<String> getCbMes() {
//		if (cbMes == null) {
//			cbMes = new JComboBox<String>();
//		
//			cbMes.setModel(new DefaultComboBoxModel<String>(new String[] {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"}));
//			cbMes.setSelectedIndex(Calendar.getInstance().get(Calendar.MONTH));
//			cbMes.setBounds(10, 17, 102, 20);
//		}
//		return cbMes;
//	}
//	private JComboBox<String> getCbAno() {
//		if (cbAno == null) {
//			cbAno = new JComboBox<String>();
//			cbAno.setModel(new DefaultComboBoxModel<String>(new String[] {"2016", "2017"}));
//			cbAno.setSelectedIndex(1);
//			cbAno.setBounds(141, 17, 63, 20);
//		}
//		return cbAno;
//	}
//	private JPanel getPanel_2() {
//		if (panel_2 == null) {
//			panel_2 = new JPanel();
//			panel_2.setBorder(new TitledBorder(null, "Liquidaciones", TitledBorder.LEADING, TitledBorder.TOP, null, null));
//			panel_2.setBounds(464, 135, 214, 99);
//			panel_2.setLayout(null);
//			panel_2.add(getBtnLiquidarFacturas());
//			panel_2.add(getCbMes());
//			panel_2.add(getCbAno());
//		}
//		return panel_2;
//	}
//	
//	private String generarIdCuota() {
//		return (meses.get(getCbMes().getSelectedItem())+ (getCbAno().getSelectedItem()));
//	}
//	
//	/**
//	 * Comprueba que el mes para el que se quiere
//	 * liquidar las facturas no esté en el futuro.
//	 * @return
//	 */
//	private boolean comprobarFechaLiquidacion() {
//		Calendar hoy= Calendar.getInstance();
//		int mesSeleccionado = Integer.parseInt(meses.get(getCbMes().getSelectedItem()));
//		int añoSeleccionado = Integer.parseInt((String) getCbAno().getSelectedItem());
//		
//		int mesActual = hoy.get(Calendar.MONTH)+1;
//		int añoActual = hoy.get(Calendar.YEAR);
//		
//		return !( ( (mesSeleccionado > mesActual) && (añoSeleccionado == añoActual) ) || (añoSeleccionado > añoActual) );
//			
//	}
//	
//	private void asignarMeses() {
//		meses= new HashMap<String, String>();
//		meses.put("Enero", "01");
//		meses.put("Febrero", "02");
//		meses.put("Marzo", "03");
//		meses.put("Abril", "04");
//		meses.put("Mayo", "05");
//		meses.put("Junio", "06");
//		meses.put("Julio", "07");
//		meses.put("Agosto", "08");
//		meses.put("Septiembre", "09");
//		meses.put("Octubre", "10");
//		meses.put("Noviembre", "11");
//		meses.put("Diciembre", "12");
//	}
}
}
