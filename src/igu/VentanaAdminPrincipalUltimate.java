package igu;


import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.HashMap;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import logica.Polideportivo;
import java.awt.CardLayout;

public class VentanaAdminPrincipalUltimate extends JDialog {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel panelPrincipal = new JPanel();
	private JButton btnDisponibilidad;
	private JButton btnCerrarSesin;
	private VentanaAdminPrincipalUltimate apv;
	private Polideportivo polideportivo;
	private JButton btnLiquidarFacturas;
	private JComboBox<String> cbMes;
	private JComboBox<String> cbAno;
	private JPanel panelLiquidar;
	private HashMap<String,String> meses;
	private JButton btnActividad;
	private JButton btnInstalacion;
	private JButton btnCliente;
	private JPanel panel;
	private JPanel panelAct;
	private JButton btnReservarActividad;
	private JButton btnCancelarActividad;
	private JButton btnCancelarAsistencia;
	private JPanel panelIns;
	private JButton btnReservarInstalacion;
	private JButton btnCancelarReserva;
	private JPanel panelCliente;
	private JButton btnReservarSocio;
	private JButton btnReservarNoSocio;
	private JButton btnCancelarReservaCliente;
	private JButton btnConfirmarAsistencia;
	private JButton btnPonerNoDisponible;


	public VentanaAdminPrincipalUltimate(Polideportivo polideportivo) {
		asignarMeses();
		setIconImage(Toolkit.getDefaultToolkit().getImage(ReservaInstalacionAdmVentana.class.getResource("/img/icon.jpg")));
		apv=this;
		this.polideportivo=polideportivo;
		setTitle("Polideportivo: Administrador");
		setResizable(false);
		setModal(true);
		setBounds(100, 100, 815, 358);
		getContentPane().setLayout(null);
		panelPrincipal.setBounds(0, 0, 809, 323);
		panelPrincipal.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(panelPrincipal);
		panelPrincipal.setLayout(null);
		panelPrincipal.add(getBtnDisponibilidad());
		panelPrincipal.add(getBtnCerrarSesin());
		panelPrincipal.add(getPanelLiquidar());
		panelPrincipal.add(getBtnActividad());
		panelPrincipal.add(getBtnInstalacion());
		panelPrincipal.add(getBtnCliente());
		panelPrincipal.add(getPanel());
	}
	
	private void abrirVentanaReservaInstalacion() {
		ReservaInstalacionAdmVentana ra = new ReservaInstalacionAdmVentana(this);
		ra.setLocationRelativeTo(this);
		ra.setModal(true);
		ra.setVisible(true);
	}
	
	private void abrirVentanaReservaActividad() { 
		ReservaActividad ra = new ReservaActividad(this);
		ra.setLocationRelativeTo(this);
		ra.setModal(true);
		ra.setVisible(true);	
	}
	
	private void abrirVentanaReservaNOSocio() {
		ReservaNoSociosVentana vrns= new ReservaNoSociosVentana(this);
		vrns.setLocationRelativeTo(this);
		vrns.setModal(true);
		vrns.setVisible(true);
	}
	
	private void abrirVentanaReservaSocio() {
		ReservaSociosVentana vrs= new ReservaSociosVentana(this);
		vrs.setLocationRelativeTo(this);
		vrs.setVisible(true);
	}
	
	private JButton getBtnDisponibilidad() {
		if (btnDisponibilidad == null) {
			btnDisponibilidad = new JButton("Disponibilidad");
			btnDisponibilidad.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					abrirVentanaHorariosAdmin();
				}

				
			});
			btnDisponibilidad.setBounds(609, 160, 159, 38);
		}
		return btnDisponibilidad;
	}
	
	private void abrirVentanaHorariosAdmin() {
		HorariosAdminVentana hva= new HorariosAdminVentana(getPolideportivo());
		hva.setLocationRelativeTo(this);
		hva.setModal(true);
		hva.setVisible(true);
	}
	
	private JButton getBtnCerrarSesin() {
		if (btnCerrarSesin == null) {
			btnCerrarSesin = new JButton("Cerrar Sesi\u00F3n");
			btnCerrarSesin.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					int resultado = JOptionPane.showConfirmDialog(apv, "¿Está seguro de que quiere cerrar sesión?","Cerrar sesión",JOptionPane.YES_NO_OPTION);
					if(resultado == JOptionPane.YES_OPTION)
					{
						dispose();
					}
				}
			});
			btnCerrarSesin.setBounds(609, 26, 159, 38);
		}
		return btnCerrarSesin;
	}
	
	private void abrirVentanaCancelarReservaSocioActividad(){
        AdministradorCancelaActividadSocio acas = new AdministradorCancelaActividadSocio(this);
        acas.setLocationRelativeTo(this);
        acas.setModal(true);
        acas.setVisible(true);
    }
	
	private void abrirVentanaAdministradorCancelaReservas(){
		AdministradorCancelaReservaNoCentroVentana acrv = new AdministradorCancelaReservaNoCentroVentana(this);
		acrv.setLocationRelativeTo(this);
		acrv.setModal(true);
		acrv.setVisible(true);
	}
	
	public Polideportivo getPolideportivo() {
		return polideportivo;
	}
	
	private void abrirVentanaReservaEfectiva() {
        ReservaEfectiva hva= new ReservaEfectiva(getPolideportivo());
        hva.setLocationRelativeTo(this);
        hva.setModal(true);
        hva.setVisible(true);
    }
	
	private void abrirVentanaAdminPoneInstalacionesNoDisponibles(){
        AdminPoneInstalacionesNoDisponibles apind = new AdminPoneInstalacionesNoDisponibles(this);
        apind.setLocationRelativeTo(this);
        apind.setModal(true);
        apind.setVisible(true);
    }
	
	private void abrirVentanaCancelarReservaActividad() { //añadida
		CancelarReservaCentroAdm crca = new CancelarReservaCentroAdm(this);
		crca.setLocationRelativeTo(this);
		crca.setModal(true);
		crca.setVisible(true);
	}
	private JButton getBtnLiquidarFacturas() {
		if (btnLiquidarFacturas == null) {
			btnLiquidarFacturas = new JButton("Liquidar facturas");
			btnLiquidarFacturas.setBounds(31, 48, 150, 35);
			btnLiquidarFacturas.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					String idCuota = generarIdCuota();
					if(comprobarFechaLiquidacion()) {
						polideportivo.liquidarFacturasMes(idCuota);
						mostrarVentanaLiquidacion(idCuota);
					}
					else
						JOptionPane.showMessageDialog(apv, "Introduce una fecha correcta");
				}
			});
		}
		return btnLiquidarFacturas;
	}
	private void mostrarVentanaLiquidacion(String idCuota) {
		LiquidarFacturasVentana lf = new LiquidarFacturasVentana(idCuota,polideportivo);
		lf.setLocationRelativeTo(this);
		lf.setModal(true);
		lf.setVisible(true);
		
	}
	private JComboBox<String> getCbMes() {
		if (cbMes == null) {
			cbMes = new JComboBox<String>();
		
			cbMes.setModel(new DefaultComboBoxModel<String>(new String[] {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"}));
			cbMes.setSelectedIndex(Calendar.getInstance().get(Calendar.MONTH));
			cbMes.setBounds(10, 17, 102, 20);
		}
		return cbMes;
	}
	private JComboBox<String> getCbAno() {
		if (cbAno == null) {
			cbAno = new JComboBox<String>();
			cbAno.setModel(new DefaultComboBoxModel<String>(new String[] {"2016", "2017"}));
			cbAno.setSelectedIndex(1);
			cbAno.setBounds(141, 17, 63, 20);
		}
		return cbAno;
	}
	private JPanel getPanelLiquidar() {
		if (panelLiquidar == null) {
			panelLiquidar = new JPanel();
			panelLiquidar.setBorder(new TitledBorder(null, "Liquidaciones", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			panelLiquidar.setBounds(583, 211, 214, 99);
			panelLiquidar.setLayout(null);
			panelLiquidar.add(getBtnLiquidarFacturas());
			panelLiquidar.add(getCbMes());
			panelLiquidar.add(getCbAno());
		}
		return panelLiquidar;
	}
	
	private String generarIdCuota() {
		return (meses.get(getCbMes().getSelectedItem())+ (getCbAno().getSelectedItem()));
	}
	
	/**
	 * Comprueba que el mes para el que se quiere
	 * liquidar las facturas no esté en el futuro.
	 * @return
	 */
	private boolean comprobarFechaLiquidacion() {
		Calendar hoy= Calendar.getInstance();
		int mesSeleccionado = Integer.parseInt(meses.get(getCbMes().getSelectedItem()));
		int añoSeleccionado = Integer.parseInt((String) getCbAno().getSelectedItem());
		
		int mesActual = hoy.get(Calendar.MONTH)+1;
		int añoActual = hoy.get(Calendar.YEAR);
		
		return !( ( (mesSeleccionado > mesActual) && (añoSeleccionado == añoActual) ) || (añoSeleccionado > añoActual) );
			
	}
	
	private void asignarMeses() {
		meses= new HashMap<String, String>();
		meses.put("Enero", "01");
		meses.put("Febrero", "02");
		meses.put("Marzo", "03");
		meses.put("Abril", "04");
		meses.put("Mayo", "05");
		meses.put("Junio", "06");
		meses.put("Julio", "07");
		meses.put("Agosto", "08");
		meses.put("Septiembre", "09");
		meses.put("Octubre", "10");
		meses.put("Noviembre", "11");
		meses.put("Diciembre", "12");
	}
	private JButton getBtnActividad() {
		if (btnActividad == null) {
			btnActividad = new JButton("Gesti\u00F3n Actividad");
			btnActividad.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					((CardLayout)panel.getLayout()).show(panel,"panel1"); 
				}
			});
			btnActividad.setBounds(31, 25, 159, 40);
		}
		return btnActividad;
	}
	private JButton getBtnInstalacion() {
		if (btnInstalacion == null) {
			btnInstalacion = new JButton("Gesti\u00F3n Instalaci\u00F3n");
			btnInstalacion.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					((CardLayout)panel.getLayout()).show(panel,"panel2"); 
				}
			});
			btnInstalacion.setBounds(209, 25, 159, 40);
		}
		return btnInstalacion;
	}
	private JButton getBtnCliente() {
		if (btnCliente == null) {
			btnCliente = new JButton("Gesti\u00F3n Cliente");
			btnCliente.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					((CardLayout)panel.getLayout()).show(panel,"panel3"); 
				}
			});
			btnCliente.setBounds(387, 25, 159, 40);
		}
		return btnCliente;
	}
	private JPanel getPanel() {
		if (panel == null) {
			panel = new JPanel();
			panel.setBounds(31, 78, 515, 232);
			panel.setLayout(new CardLayout(0, 0));
			panel.add(getPanelAct(), "panel1");
			panel.add(getPanelIns(), "panel2");
			panel.add(getPanelCliente(), "panel3");
		}
		return panel;
	}
	private JPanel getPanelAct() {
		if (panelAct == null) {
			panelAct = new JPanel();
			panelAct.setBorder(new TitledBorder(null, "Gesti\u00F3n Actividad", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			panelAct.setLayout(null);
			panelAct.add(getBtnReservarActividad());
			panelAct.add(getBtnCancelarActividad());
			panelAct.add(getBtnCancelarAsistencia());
		}
		return panelAct;
	}
	private JButton getBtnReservarActividad() {
		if (btnReservarActividad == null) {
			btnReservarActividad = new JButton("Reservar Actividad");
			btnReservarActividad.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					abrirVentanaReservaActividad();
				}
			});
			btnReservarActividad.setBounds(37, 45, 159, 40);
		}
		return btnReservarActividad;
	}
	private JButton getBtnCancelarActividad() {
		if (btnCancelarActividad == null) {
			btnCancelarActividad = new JButton("Cancelar Actividad");
			btnCancelarActividad.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					abrirVentanaCancelarReservaActividad();
				}
			});
			btnCancelarActividad.setBounds(269, 45, 159, 40);
		}
		return btnCancelarActividad;
	}
	private JButton getBtnCancelarAsistencia() {
		if (btnCancelarAsistencia == null) {
			btnCancelarAsistencia = new JButton("Cancelar Asistencia");
			btnCancelarAsistencia.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					abrirVentanaCancelarReservaSocioActividad();
				}
			});
			btnCancelarAsistencia.setBounds(37, 114, 159, 40);
		}
		return btnCancelarAsistencia;
	}
	private JPanel getPanelIns() {
		if (panelIns == null) {
			panelIns = new JPanel();
			panelIns.setBorder(new TitledBorder(null, "Gesti\u00F3n Instalaci\u00F3n", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			panelIns.setLayout(null);
			panelIns.add(getBtnReservarInstalacion());
			panelIns.add(getBtnCancelarReserva());
			panelIns.add(getBtnPonerNoDisponible());
		}
		return panelIns;
	}
	private JButton getBtnReservarInstalacion() {
		if (btnReservarInstalacion == null) {
			btnReservarInstalacion = new JButton("Reservar Instalaci\u00F3n");
			btnReservarInstalacion.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					abrirVentanaReservaInstalacion();
				}
			});
			btnReservarInstalacion.setBounds(37, 45, 159, 40);
		}
		return btnReservarInstalacion;
	}
	private JButton getBtnCancelarReserva() {
		if (btnCancelarReserva == null) {
			btnCancelarReserva = new JButton("Cancelar Reserva");
			btnCancelarReserva.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					abrirVentanaCancelarReservaActividad();
				}
			});
			btnCancelarReserva.setBounds(269, 45, 159, 40);
		}
		return btnCancelarReserva;
	}
	private JPanel getPanelCliente() {
		if (panelCliente == null) {
			panelCliente = new JPanel();
			panelCliente.setBorder(new TitledBorder(null, "Gesti\u00F3n Cliente", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			panelCliente.setLayout(null);
			panelCliente.add(getBtnReservarSocio());
			panelCliente.add(getBtnReservarNoSocio());
			panelCliente.add(getBtnCancelarReservaCliente());
			panelCliente.add(getBtnConfirmarAsistencia());
		}
		return panelCliente;
	}
	private JButton getBtnReservarSocio() {
		if (btnReservarSocio == null) {
			btnReservarSocio = new JButton("Reservar Socio");
			btnReservarSocio.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					abrirVentanaReservaSocio();
				}
			});
			btnReservarSocio.setBounds(37, 45, 159, 40);
		}
		return btnReservarSocio;
	}
	private JButton getBtnReservarNoSocio() {
		if (btnReservarNoSocio == null) {
			btnReservarNoSocio = new JButton("Reservar No Socio");
			btnReservarNoSocio.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					abrirVentanaReservaNOSocio();
				}
			});
			btnReservarNoSocio.setBounds(269, 45, 159, 40);
		}
		return btnReservarNoSocio;
	}
	private JButton getBtnCancelarReservaCliente() {
		if (btnCancelarReservaCliente == null) {
			btnCancelarReservaCliente = new JButton("Cancelar Reserva");
			btnCancelarReservaCliente.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					abrirVentanaAdministradorCancelaReservas();
				}
			});
			btnCancelarReservaCliente.setBounds(37, 114, 159, 40);
		}
		return btnCancelarReservaCliente;
	}
	private JButton getBtnConfirmarAsistencia() {
		if (btnConfirmarAsistencia == null) {
			btnConfirmarAsistencia = new JButton("Confirmar Asistencia");
			btnConfirmarAsistencia.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					abrirVentanaReservaEfectiva();
				}
			});
			btnConfirmarAsistencia.setBounds(269, 114, 159, 40);
		}
		return btnConfirmarAsistencia;
	}
	private JButton getBtnPonerNoDisponible() {
		if (btnPonerNoDisponible == null) {
			btnPonerNoDisponible = new JButton("Poner no disponible");
			btnPonerNoDisponible.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					abrirVentanaAdminPoneInstalacionesNoDisponibles();
				}
			});
			btnPonerNoDisponible.setBounds(37, 114, 159, 40);
		}
		return btnPonerNoDisponible;
	}
}
