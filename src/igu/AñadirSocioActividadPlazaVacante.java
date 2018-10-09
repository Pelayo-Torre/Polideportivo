package igu;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import logica.Cliente;
import logica.Comprobaciones;
import logica.Reserva;
import logica.Socio;

import java.awt.Toolkit;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class AñadirSocioActividadPlazaVacante extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JLabel lblIntroduceElDni;
	private JTextField textFieldDNI;
	private MonitorVentana mv;
	private AñadirSocioActividadPlazaVacante as;
	private Reserva reserva;
	private JLabel lblDni;
	private JTextField textFieldNSocio;
	private JLabel lblNsocioi;

	/**
	 * Create the dialog.
	 */
	public AñadirSocioActividadPlazaVacante(MonitorVentana mv, Reserva r) {
		reserva=r;
		as=this;
		this.mv=mv;
		setResizable(false);
		setTitle("Polideportivo: Apuntar socio plaza vacante");
		setIconImage(Toolkit.getDefaultToolkit().getImage(AñadirSocioActividadPlazaVacante.class.getResource("/img/icon.jpg")));
		setBounds(100, 100, 452, 155);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		contentPanel.add(getLblIntroduceElDni());
		contentPanel.add(getTextFieldDNI());
		contentPanel.add(getLblDni());
		contentPanel.add(getTextFieldNSocio());
		contentPanel.add(getLblNsocioi());
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
//						Socio socioAApuntar= buscarSocioPorDNI(getTextFieldDNI().getText());
						Socio socioAApuntar= buscarSocio();
						if(socioAApuntar==null) {
							JOptionPane.showMessageDialog(as, "Los datos introducidos no corresponden a ningún socio");
							getTextFieldDNI().setText("");
							getTextFieldNSocio().setText("");
						}
						else {
							if(comprobarNoSolapamientoReservas(socioAApuntar)) {
								mv.setSocioAAñadir(socioAApuntar);
								JOptionPane.showMessageDialog(mv, "Socio añadido correctamente");
								dispose();
							}
							else {
								mv.setSocioAAñadir(null);
								getTextFieldDNI().setText("");
							}
						}
					}
					
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
	
	private boolean comprobarNoSolapamientoReservas(Socio sc){
		
		return comprobarReservasAInstalaciones(sc) && comprobarApuntadoAActividades(sc);	
	}
	
	private boolean comprobarApuntadoAActividades(Socio sc) {
		try {
			ArrayList<Reserva> rActividadesApuntado = mv.getPolideportivo().getConexion().reservasActividadUsuario(sc.getDNI());
			for(Reserva r: rActividadesApuntado) {
				if(r.getEstado().equals(Reserva.LIBRE)) {
					Calendar c1 = Calendar.getInstance();c1.setTime(r.getFechaReserva());Calendar c2 = Calendar.getInstance();c2.setTime(r.getFechaReserva());
					c2.add(Calendar.HOUR_OF_DAY, r.getTiempoReserva());
					Calendar c3 = Calendar.getInstance();c3.setTime(reserva.getFechaReserva());Calendar c4 = Calendar.getInstance();c4.setTime(reserva.getFechaReserva());
					c4.add(Calendar.HOUR_OF_DAY, reserva.getTiempoReserva());
					//(StartA <= EndB)  and  (EndA >= StartB)
//					if( (c1.equals(c4) || c1.before(c4)) && (c2.equals(c3) || c2.after(c3)) ){
//						JOptionPane.showMessageDialog(this, "El usuario ya está apuntado a una actividad a esta hora");
//						return false; //no esta disponible
//					}
					if(mv.getPolideportivo().comprobarFechas(r,reserva)) {
						JOptionPane.showMessageDialog(null,"Tienes una reserva de actividad a la misma hora.");
						return false; //no esta disponible
					}
				}
			}
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.err.print("No se han podido recuperar los horarios de las actividades a las que estaba apuntado el usuario");
			e.printStackTrace();
		}
		return false;
		
	}
	
	/*
	 * Comprueba que el usuario al que va a apuntar el monitor a la actividad no tenga una 
	 * reserva de instalación para la misma hora.
	 */
	private boolean comprobarReservasAInstalaciones(Socio sc) {
		for(Reserva r: mv.getPolideportivo().getReservas()) {	
			if(!r.equals(reserva) && !r.isReservaCentro()) {
				if(r.getEstado().equals(Reserva.LIBRE) && r.getCliente().getDNI().equals(sc.getDNI())) {
					Calendar c1 = Calendar.getInstance();c1.setTime(r.getFechaReserva());Calendar c2 = Calendar.getInstance();c2.setTime(r.getFechaReserva());
					c2.add(Calendar.HOUR_OF_DAY, r.getTiempoReserva());
					Calendar c3 = Calendar.getInstance();c3.setTime(reserva.getFechaReserva());Calendar c4 = Calendar.getInstance();c4.setTime(reserva.getFechaReserva());
					c4.add(Calendar.HOUR_OF_DAY, reserva.getTiempoReserva());
//					if((c3.equals(c1) || c3.after(c1)) && (c4.before(c2) || c4.equals(c2))) {
//						JOptionPane.showMessageDialog(this, "El usuario ya tiene una instalación reservada para esta hora");
//						return false; //no esta disponible
					if(mv.getPolideportivo().comprobarFechas(r,reserva)) {
						JOptionPane.showMessageDialog(null, "Tienes una reserva de instalación a la misma hora");
						return false; //no esta disponible
					}
						
				}
			}
		}
		return true;//esta disponible
	}
	
	private Socio buscarSocio() {
		Comprobaciones c = new Comprobaciones();
		String dni=getTextFieldDNI().getText();
		String ns=getTextFieldNSocio().getText();
		if(dni.equals("") || !c.DNI(dni))
			return buscarSocioPorNS(Integer.parseInt(ns));
		else if(ns.equals("") || !Comprobaciones.isNumeric(ns))
			return buscarSocioPorDNI(dni);
		else if(Comprobaciones.isNumeric(ns) && c.DNI(dni))
			return buscarSocioPorNSyDNI(dni,Integer.parseInt(ns));
		else 
			return null;
	}
	
	private Socio buscarSocioPorNSyDNI(String dni, int ns) {
		for(Cliente s:mv.getPolideportivo().getClientes()) {
			if( s instanceof Socio && ((Socio) s).getNumeroSocio()==ns && s.getDNI().equals(dni))
				return (Socio) s;
		}
		return null;
	}
	
	private Socio buscarSocioPorNS(int ns) {
		for(Cliente s:mv.getPolideportivo().getClientes()) {
			if( s instanceof Socio && ((Socio) s).getNumeroSocio()==ns)
				return (Socio) s;
		}
		return null;
	}
	
	private Socio buscarSocioPorDNI(String dni) {
		for(Cliente s:mv.getPolideportivo().getClientes()) {
			if(s.getDNI().equals(dni) && s instanceof Socio)
				return (Socio) s;
		}
		return null;
	}
	private JLabel getLblIntroduceElDni() {
		if (lblIntroduceElDni == null) {
			lblIntroduceElDni = new JLabel("Introduce el DNI o el n\u00FAmero de socio del socio a apuntar: ");
			lblIntroduceElDni.setBounds(10, 11, 401, 14);
		}
		return lblIntroduceElDni;
	}
	private JTextField getTextFieldDNI() {
		if (textFieldDNI == null) {
			textFieldDNI = new JTextField();
			textFieldDNI.setBounds(108, 37, 129, 20);
			textFieldDNI.setColumns(10);
		}
		return textFieldDNI;
	}
	private JLabel getLblDni() {
		if (lblDni == null) {
			lblDni = new JLabel("DNI:");
			lblDni.setBounds(49, 40, 36, 14);
		}
		return lblDni;
	}
	private JTextField getTextFieldNSocio() {
		if (textFieldNSocio == null) {
			textFieldNSocio = new JTextField();
			textFieldNSocio.setBounds(108, 62, 129, 20);
			textFieldNSocio.setColumns(10);
		}
		return textFieldNSocio;
	}
	private JLabel getLblNsocioi() {
		if (lblNsocioi == null) {
			lblNsocioi = new JLabel("N\u00BASocio:");
			lblNsocioi.setBounds(49, 65, 51, 14);
		}
		return lblNsocioi;
	}
}
