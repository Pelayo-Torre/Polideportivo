package igu;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import logica.Reserva;


import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.JButton;
import javax.swing.JDialog;

import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.Map;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.awt.event.ActionEvent;
import java.awt.CardLayout;
import javax.swing.JScrollPane;
import java.awt.Toolkit;

public class ConfirmarActividadPeriodica extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JPanel panelPregunta;
	private JTextPane txtpnProblema;
	private JPanel panelInferior;
	private JButton btnCancelar;
	private JButton btnVerDetalles;
	private JPanel panelBotones;
	private JPanel panelDetalles;
	private Map< Integer , ArrayList<Reserva> > conflictosReservas;
	private boolean desplegada = false;
	private JPanel panel;
	private JPanel panelTelefonos;
	private JPanel panelDatosConflicto;
	private JScrollPane scrollPaneTexto;
	private JPanel panelInterior;
	private JTextPane textReservas;
	SimpleDateFormat formato = new SimpleDateFormat("yyyy/MM/dd HH:mm"); 
	private int height;
	private int width;
//	private Polideportivo poli;
//	private Date fecha_Reserva;
//	private int duracion;
//	private Instalacion ins;
//	private Actividad act;
//	private int semanas;
	private JTextPane txtpnLaReservaSe;
	private JPanel panelNumTef;
	private JScrollPane scrollPanetTel;
	private JTextPane textPaneTelefonos;
	private JPanel panelBot;
	private JButton btnNewButton;
	
	/**
	 * Create the frame.
	 */
	public ConfirmarActividadPeriodica(ReservaActividadPeriodica rap, Map< Integer , ArrayList<Reserva> > todasLasIncompatibles) {
		setTitle("Polideportivo: Conflictos");
		setIconImage(Toolkit.getDefaultToolkit().getImage(ConfirmarActividadPeriodica.class.getResource("/img/icon.jpg")));
		conflictosReservas = todasLasIncompatibles;
//		fecha_Reserva = fecha;
//		ins = in;
//		this.duracion = duracion;
//		this.act = act;
//		this.semanas = semanas;
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 685, 153);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new CardLayout(0, 0));
		contentPane.add(getPanelPregunta(), "panel1");
		contentPane.add(getPanelTelefonos(), "panel2");
		height = getSize().height;
		width = getSize().width;
	}

	private JPanel getPanelPregunta() {
		if (panelPregunta == null) {
			panelPregunta = new JPanel();
			panelPregunta.setLayout(new BorderLayout(0, 0));
			panelPregunta.add(getPanelInferior(), BorderLayout.SOUTH);
			panelPregunta.add(getPanel(), BorderLayout.NORTH);
			panelPregunta.add(getPanelDatosConflicto(), BorderLayout.CENTER);
		}
		return panelPregunta;
	}
	private JTextPane getTxtpnProblema() {
		if (txtpnProblema == null) {
			txtpnProblema = new JTextPane();
			txtpnProblema.setEditable(false);
			txtpnProblema.setBackground(UIManager.getColor("Button.background"));
			txtpnProblema.setText("Esta intentado reservar a una hora en la que ya existe alguna reserva. \u00BFEst\u00E1 seguro de que desea continuar?");
		}
		return txtpnProblema;
	}
	private JPanel getPanelInferior() {
		if (panelInferior == null) {
			panelInferior = new JPanel();
			panelInferior.setLayout(new BorderLayout(0, 0));
			panelInferior.add(getPanelBotones());
			panelInferior.add(getPanelDetalles(), BorderLayout.WEST);
		}
		return panelInferior;
	}
	
//	private void añadirReserva() {
//		String cod = AñadirReservaActividadAction.obtenerIdCodMismaAct(poli);
//		System.out.println("COD_MISMA_ACTIVIDAD = " + cod);
//		añadirAct(poli,ins,fecha_Reserva,duracion,act,cod);
//	}
//	
//	private void añadirAct(Polideportivo poli, Instalacion instalacion_reserva, Date fecha_intento_reserva, int duracion_reserva, Actividad actSelected,String cod) {
//		Calendar c1 = Calendar.getInstance(); c1.setTime(fecha_intento_reserva);
//		for(int i = 0; i < semanas; i++) {
//			AñadirReservaActividadAction.añadirReserva(poli,instalacion_reserva,c1.getTime(),duracion_reserva,actSelected,cod);
//			c1.add(Calendar.WEEK_OF_YEAR, 1);
//		}
//	}
//	
//	private void cancelarReservas() throws SQLException {
//		StringBuffer bf = new StringBuffer(); 
//		for(Reserva res: reservasExistentes) {
//			ArrayList<Socio> soc = new ArrayList<Socio>();
//			Actividad act = poli.isReservaActividad(res);
//			if(act != null) {
//				cancelarMasReservasDeActividad(act, res);
//				soc = act.obtenerApuntados(res);
//				if(!soc.isEmpty()) {//si hay alquien apuntado cambio su estado en la bd
//					for(Socio sc: soc) {
//						poli.getConexion().cambiarEstadoCanceladaApuntadoA(sc.getDNI(), res.getIdReserva(),act.getCodActividad(),"True");//BORRADO DE EN LA BD DE LA ACTIVIDAD
//						bf.append(sc.getNombre() + " " + sc.getApellidos() + " - Fecha: " + formato.format(res.getFechaReserva()) + " - Actividad: " + act.getNombreActividad());
//					}
//				}
//			act.getApuntadosActividades().remove(res);//BORRADO LOCAL DE LA RESERVA DE LA ACTIVIDAD
//			}
//			if(res.isReservaCentro())
//				bf.append("Reserva Centro -  Fecha: " + formato.format(res.getFechaReserva()) + " - Instalación: " + res.getInstalacion().getTipo() + " " + res.getInstalacion().getnPista());
//			else
//				bf.append("Reserva Socio - Nombre: " + res.getCliente().getNombre() + " " + res.getCliente().getApellidos() + " - Fecha: " + formato.format(res.getFechaReserva()) + " - Instalación: " + res.getInstalacion().getTipo() + " " + res.getInstalacion().getnPista());
//			textPaneTelefonos.setText(bf.toString());
//			poli.getConexion().cambiarEstadoAnuladaReservaCentro(res.getIdReserva());//LO USO PARA TODAS LAS RESERVAS
//			poli.ponerReservaAnulada(res.getIdReserva());//BORRADO LOCAL DE LA RESERVA
//		}
//	}
//	
//	private void cancelarMasReservasDeActividad(Actividad act, Reserva res) throws SQLException {
//		StringBuffer bf2 = new StringBuffer(); 
//		ArrayList<String> idReservaPeriodica = poli.getConexion().obtenerReservasActividadPeriodica(res.getIdReserva(), act.getCodActividad());
//		System.out.println(idReservaPeriodica);	
//		ArrayList<Reserva> reservas = poli.ReservasById(idReservaPeriodica);
//		for(Reserva reserva: reservas) {
//			ArrayList<Socio> soc = new ArrayList<Socio>();
//			soc = act.obtenerApuntados(reserva);
//			if(!soc.isEmpty()) {//si hay alquien apuntado cambio su estado en la bd
//				for(Socio sc: soc) {
//					poli.getConexion().cambiarEstadoCanceladaApuntadoA(sc.getDNI(), reserva.getIdReserva(),act.getCodActividad(),"True");//BORRADO DE EN LA BD DE LA ACTIVIDAD
//					bf2.append(sc.getNombre() + " " + sc.getApellidos() + " - Fecha: " + formato.format(reserva.getFechaReserva()) + " - Actividad: " + act.getNombreActividad());
//				}
//			}
//			act.getApuntadosActividades().remove(reserva);//BORRADO LOCAL DE LA RESERVA DE LA ACTIVIDAD
//			poli.getConexion().cambiarEstadoAnuladaReservaCentro(reserva.getIdReserva());//LO USO PARA TODAS LAS RESERVAS
//			poli.ponerReservaAnulada(reserva.getIdReserva());//BORRADO LOCAL DE LA RESERVA
//		}
//		String textoAnterior = textPaneTelefonos.getText();
//		textPaneTelefonos.setText(textoAnterior + bf2.toString());
//	}
	
	
	
	private JButton getBtnCancelar() {
		if (btnCancelar == null) {
			btnCancelar = new JButton("Cancelar");
			btnCancelar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					dispose();
				}
			});
		}
		return btnCancelar;
	}
	private JButton getBtnVerDetalles() {
		if (btnVerDetalles == null) {
			btnVerDetalles = new JButton("Ver más");
			btnVerDetalles.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(!desplegada) {
						desplegada = true;
						int maxTam = (conflictosReservas.size()*75 + height > 700)? 700:conflictosReservas.size()*75 + height;
						setSize(new Dimension(width, maxTam));
						btnVerDetalles.setText("Ver menos");
						añadirDatos();
					} else {
						desplegada = false;
						setSize(new Dimension(width, height));
						btnVerDetalles.setText("Ver más");
						quitarDatos();
					}
				}
			});
		}
		return btnVerDetalles;
	}
	
	private void añadirDatos() {
		StringBuffer a = new StringBuffer("Reservas incompatibles\n");
		for (Map.Entry<Integer, ArrayList<Reserva>> m : conflictosReservas.entrySet()) {
			a.append("RESERVA Nº " + m.getKey() + "\n");
			for(Reserva res: m.getValue()) {
				if(res.isReservaCentro()) {
					if(res.getTipoActividad() != null) 
						a.append("ACTIVIDAD  Instalación: " + res.getInstalacion().getTipo() + " " + res.getInstalacion().getnPista() + " - Fecha: " + formato.format(res.getFechaReserva()) + " - Actividad: " + res.getTipoActividad() + " - Duración: " + res.getTiempoReserva() + "h\n");
					else
						a.append("CENTRO  Instalación: " + res.getInstalacion().getTipo() + " " + res.getInstalacion().getnPista() + " - Fecha: " + formato.format(res.getFechaReserva()) +  " - Duración: " + res.getTiempoReserva() + "h\n");
				}
				else { 
					a.append("CLIENTE  Instalación: " + res.getInstalacion().getTipo() + " " + res.getInstalacion().getnPista() + " - Fecha: " + formato.format(res.getFechaReserva()) + " - Duración: " + res.getTiempoReserva() + "h\n");
				}
			}
			a.append("\n");
		}
		textReservas.setText(a.toString());
		panelInterior.setVisible(true);
	}
	
	
	private void quitarDatos() {
		panelInterior.setVisible(false);
	}
	
	private JPanel getPanelBotones() {
		if (panelBotones == null) {
			panelBotones = new JPanel();
			FlowLayout fl_panelBotones = (FlowLayout) panelBotones.getLayout();
			fl_panelBotones.setAlignment(FlowLayout.RIGHT);
			panelBotones.add(getBtnCancelar());
		}
		return panelBotones;
	}
	private JPanel getPanelDetalles() {
		if (panelDetalles == null) {
			panelDetalles = new JPanel();
			panelDetalles.add(getBtnVerDetalles());
		}
		return panelDetalles;
	}
	private JPanel getPanel() {
		if (panel == null) {
			panel = new JPanel();
			panel.add(getTxtpnProblema());
		}
		return panel;
	}
	private JPanel getPanelTelefonos() {
		if (panelTelefonos == null) {
			panelTelefonos = new JPanel();
			panelTelefonos.setLayout(new BorderLayout(0, 0));
			panelTelefonos.add(getTxtpnLaReservaSe(), BorderLayout.NORTH);
			panelTelefonos.add(getPanelNumTef(), BorderLayout.CENTER);
			panelTelefonos.add(getPanel_1_1(), BorderLayout.SOUTH);
		}
		return panelTelefonos;
	}
	private JPanel getPanelDatosConflicto() {
		if (panelDatosConflicto == null) {
			panelDatosConflicto = new JPanel();
			panelDatosConflicto.add(getScrollPaneTexto());
		}
		return panelDatosConflicto;
	}
	private JScrollPane getScrollPaneTexto() {
		if (scrollPaneTexto == null) {
			scrollPaneTexto = new JScrollPane();
			scrollPaneTexto.setViewportView(getPanelInterior());
			scrollPaneTexto.setBorder(null);
		}
		return scrollPaneTexto;
	}
	private JPanel getPanelInterior() {
		if (panelInterior == null) {
			panelInterior = new JPanel();
			panelInterior.setLayout(new BorderLayout(0, 0));
			panelInterior.add(getTextReservas(), BorderLayout.NORTH);
			panelInterior.setVisible(false);
		}
		return panelInterior;
	}
	private JTextPane getTextReservas() {
		if (textReservas == null) {
			textReservas = new JTextPane();
			textReservas.setText(" ");
			textReservas.setEditable(false);
			textReservas.setBackground(UIManager.getColor("Button.background"));
		}
		return textReservas;
	}
	private JTextPane getTxtpnLaReservaSe() {
		if (txtpnLaReservaSe == null) {
			txtpnLaReservaSe = new JTextPane();
			txtpnLaReservaSe.setBackground(UIManager.getColor("Button.background"));
			txtpnLaReservaSe.setText("La reserva se ha producido con exito, avise a los clientes listados a continuaci\u00F3n de que su reserva a sido cancelada.");
		}
		return txtpnLaReservaSe;
	}
	private JPanel getPanelNumTef() {
		if (panelNumTef == null) {
			panelNumTef = new JPanel();
			panelNumTef.add(getScrollPanetTel());
		}
		return panelNumTef;
	}
	private JScrollPane getScrollPanetTel() {
		if (scrollPanetTel == null) {
			scrollPanetTel = new JScrollPane();
			scrollPanetTel.setViewportView(getTextPaneTelefonos());
			scrollPanetTel.setBorder(null);
		}
		return scrollPanetTel;
	}
	private JTextPane getTextPaneTelefonos() {
		if (textPaneTelefonos == null) {
			textPaneTelefonos = new JTextPane();
			textPaneTelefonos.setBackground(UIManager.getColor("Button.background"));
		}
		return textPaneTelefonos;
	}
	private JPanel getPanel_1_1() {
		if (panelBot == null) {
			panelBot = new JPanel();
			FlowLayout flowLayout = (FlowLayout) panelBot.getLayout();
			flowLayout.setAlignment(FlowLayout.RIGHT);
			panelBot.add(getBtnNewButton());
		}
		return panelBot;
	}
	private JButton getBtnNewButton() {
		if (btnNewButton == null) {
			btnNewButton = new JButton("Aceptar");
		}
		return btnNewButton;
	}
}
