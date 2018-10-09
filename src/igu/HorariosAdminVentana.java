package igu;

import java.awt.BorderLayout;
import java.awt.Color;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;

import logica.Instalacion;
import logica.Polideportivo;
import logica.Reserva;
import logica.Socio;

import javax.swing.DefaultComboBoxModel;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Toolkit;

import javax.swing.border.TitledBorder;
import javax.swing.border.LineBorder;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.ButtonGroup;

@SuppressWarnings("serial")
public class HorariosAdminVentana extends JDialog {

	private JPanel contentPane;
	private JPanel panel;
	private JLabel lblNewLabel;
	private JComboBox<Instalacion> comboBoxInstalaciones;
	private JScrollPane scrollPane;
	private JTable tableHorarios;
	private ModeloNoEditable modeloTabla;
	private RendererTablaHorarios rendererTabla;
	private JPanel panel_1;
	private JLabel lblRed;
	private JLabel lblSocio;
	private JLabel lblYellow;
	private JLabel lblActividad;
	@SuppressWarnings("unused")
	private JPanel panel_2;
	@SuppressWarnings("unused")
	private final ButtonGroup buttonGroup = new ButtonGroup();
	
	private Polideportivo polideportivo;
	private JPanel panelBotonesSemanas;
	private JPanel panelBotones;
	private JButton btnMenos;
	private JButton btnMas;
	int sumSemanas;
	private JLabel label;
	private JLabel lblActividad_1;
	private JLabel label_1;
	private JLabel lblCentro;
	private JPanel panelCentral;
	private JPanel panelNavegacion;
	private JButton btnAtras;
	private JButton btnAdelante;
	private JButton btnInicio;

	public Polideportivo getPolideportivo() {
		return polideportivo;
	}


	/**
	 * Create the frame.
	 * @throws ParseException 
	 */
	public HorariosAdminVentana(Polideportivo polideportivo){
		setResizable(false);
		setIconImage(Toolkit.getDefaultToolkit().getImage(ReservaInstalacionAdmVentana.class.getResource("/img/icon.jpg")));
		setTitle("Polideportivo: Horarios administrador");
		setModal(true);
		modeloTabla= new ModeloNoEditable(crearNombreColumnas(),0);
		rendererTabla= new RendererTablaHorarios();
		this.polideportivo=polideportivo;
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 1026, 571);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		contentPane.add(getPanel(), BorderLayout.NORTH);
		contentPane.add(getPanelCentral(), BorderLayout.CENTER);
	}

	private JPanel getPanel() {
		if (panel == null) {
			panel = new JPanel();
			panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
			panel.add(getLblNewLabel());
			panel.add(getComboBoxInstalaciones());
			panel.add(getPanel_1());
		}
		return panel;
	}
	private JLabel getLblNewLabel() {
		if (lblNewLabel == null) {
			lblNewLabel = new JLabel("Seleccione la instalaci\u00F3n para la que desea comprobar la disponibilidad: ");
		}
		return lblNewLabel;
	}
	private JComboBox<Instalacion> getComboBoxInstalaciones() {
		if (comboBoxInstalaciones == null) {
			comboBoxInstalaciones = new JComboBox<Instalacion>();
			comboBoxInstalaciones.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					modeloTabla.getDataVector().removeAllElements();
					añadirReservas(modeloTabla);
					modeloTabla.fireTableDataChanged();
				}
			});
			DefaultComboBoxModel<Instalacion> model=new DefaultComboBoxModel<Instalacion>();
			model=llenarComboInstalaciones(model);
			comboBoxInstalaciones.setModel(model);
			comboBoxInstalaciones.setSelectedIndex(0);
			
		}
		return comboBoxInstalaciones;
	}
	
	private DefaultComboBoxModel<Instalacion> llenarComboInstalaciones(DefaultComboBoxModel<Instalacion> model){
		for(Instalacion inst:polideportivo.getInstalaciones())
			model.addElement(inst);
		return model;
	}
	

	private JScrollPane getScrollPane() {
		if (scrollPane == null) {
			scrollPane = new JScrollPane();
			scrollPane.setViewportView(getTableHorarios());
		}
		return scrollPane;
	}
	private JTable getTableHorarios() {
		if (tableHorarios == null) {
			tableHorarios = new JTable(modeloTabla);
			tableHorarios.setRowSelectionAllowed(false);
			rendererTabla.setHorizontalAlignment(SwingConstants.CENTER);
			for(int i=1; i<tableHorarios.getColumnCount(); i++)
				tableHorarios.getColumnModel().getColumn(i).setCellRenderer(rendererTabla);
		}
		return tableHorarios;
	}


	private void añadirReservas(ModeloNoEditable modeloTabla){
		Object[] fila = new Object[9]; Date fInicio = new Date(); String[] diaMes = crearNombreColumnas();
		Calendar cInicio = Calendar.getInstance(); Calendar cFin = Calendar.getInstance(); cInicio.setTime(fInicio);
		for(int j = 0; j < 24; j++) {
			String horas = (j<10?"0"+j+":00":j+":00")+"-"+((j+1)<10?"0"+(j+1)+":00":(j+1)+":00");
			fila[0] = horas;
			int horaInicio = j; int horaFin = horaInicio + 1;
			for(int i = 1; i < 9; i++) {
				String[] diaMesPartes = diaMes[i].split("/");
				int mes = Integer.parseInt(diaMesPartes[1]) - 1; int dia = Integer.parseInt(diaMesPartes[0]);
				cInicio.set(cInicio.get(Calendar.YEAR), mes, dia, horaInicio, 0,0); cFin.set(cFin.get(Calendar.YEAR), mes, dia, horaFin, 0,0);
				cInicio.set(Calendar.MILLISECOND, 0);cFin.set(Calendar.MILLISECOND, 0);
				String valor = reservaExistente((Instalacion)comboBoxInstalaciones.getSelectedItem(), cInicio, cFin);
				if(valor != null) 
					fila[i] = valor;
				else 
					fila[i] = "";
			}
			modeloTabla.addRow(fila);
		}
	}
	
	public String reservaExistente(Instalacion in, Calendar ini, Calendar fin) {
		for(Reserva r: polideportivo.getReservas()) {
			if(!r.getEstado().equals(Reserva.ANULADA)) {
				if(r.getInstalacion().getIdPista().equals(in.getIdPista())) {
					Calendar c1 = Calendar.getInstance();c1.setTime(r.getFechaReserva());
					Calendar c2 = Calendar.getInstance();c2.setTime(r.getFechaReserva());
					c2.add(Calendar.HOUR_OF_DAY, r.getTiempoReserva());
					if((ini.equals(c1) || ini.after(c1)) && (fin.before(c2) || fin.equals(c2))) {
						if(r.isReservaCentro()) {
							if(r.getTipoActividad() != null)
								return r.getTipoActividad();
							else
								return "Centro";
						}
						else if(r.getCliente() instanceof Socio)
							return "Socio";
						else 
							return "No Socio";
					}
				}
			}
		}
		return null;
	}

	

	@SuppressWarnings("static-access")
	private String[] crearNombreColumnas() {
		String[] semana= new String[9];
		String dia;
		Calendar cal=Calendar.getInstance();
		cal.add(Calendar.WEEK_OF_YEAR, sumSemanas);
		semana[0]="Horas/Días";
		for(int i=1;i<9;i++) {
			dia=(cal.get(Calendar.DAY_OF_MONTH))+"/"+(cal.get(Calendar.MONTH)+1);
			semana[i]=dia;
			cal.add(cal.DATE, 1);
		}
		return semana;
		
	}

	private JPanel getPanel_1() {
		if (panel_1 == null) {
			panel_1 = new JPanel();
			panel_1.setBorder(new TitledBorder(null, "Tipos de reserva", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			panel_1.add(getLblRed());
			panel_1.add(getLblSocio());
			panel_1.add(getLblYellow());
			panel_1.add(getLblActividad());
			panel_1.add(getLabel());
			panel_1.add(getLblActividad_1());
			panel_1.add(getLabel_1());
			panel_1.add(getLblCentro());
		}
		return panel_1;
	}
	private JLabel getLblRed() {
		if (lblRed == null) {
			lblRed = new JLabel("Socio...");
			lblRed.setForeground(Color.GREEN);
			lblRed.setOpaque(true);
			lblRed.setBorder(new LineBorder(new Color(0, 0, 0)));
			lblRed.setBackground(Color.GREEN);
		}
		return lblRed;
	}
	private JLabel getLblSocio() {
		if (lblSocio == null) {
			lblSocio = new JLabel("Socio");
		}
		return lblSocio;
	}
	private JLabel getLblYellow() {
		if (lblYellow == null) {
			lblYellow = new JLabel("Activida");
			lblYellow.setBorder(new LineBorder(new Color(0, 0, 0)));
			lblYellow.setForeground(Color.CYAN);
			lblYellow.setOpaque(true);
			lblYellow.setBackground(Color.CYAN);
		}
		return lblYellow;
	}
	private JLabel getLblActividad() {
		if (lblActividad == null) {
			lblActividad = new JLabel("No Socio");
		}
		return lblActividad;
	}
	@SuppressWarnings("unused")
	private JPanel getPanel_3_1() {
		if (panelBotonesSemanas == null) {
			panelBotonesSemanas = new JPanel();
			panelBotonesSemanas.setLayout(new BorderLayout(0, 0));
			panelBotonesSemanas.add(getScrollPane(), BorderLayout.CENTER);
			panelBotonesSemanas.add(getPanel_2_1(), BorderLayout.NORTH);
		}
		return panelBotonesSemanas;
	}
	private JPanel getPanel_2_1() {
		if (panelBotones == null) {
			panelBotones = new JPanel();
			panelBotones.add(getBtnMenos());
			panelBotones.add(getBtnMas());
		}
		return panelBotones;
	}
	private JButton getBtnMenos() {
		if (btnMenos == null) {
			btnMenos = new JButton("\u2190");
			btnMenos.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					semanaMenos();
				}
			});
		}
		return btnMenos;
	}
	
	private void semanaMenos() {
		sumSemanas--;
		System.out.println(sumSemanas);
		modeloTabla = new ModeloNoEditable(crearNombreColumnas(), 0);
		añadirReservas(modeloTabla);
		tableHorarios.setModel(modeloTabla);
		for(int i=1; i<tableHorarios.getColumnCount(); i++)
			tableHorarios.getColumnModel().getColumn(i).setCellRenderer(rendererTabla);
	}
	
	private void semanaMas() {
		sumSemanas++;
		System.out.println(sumSemanas);
		modeloTabla = new ModeloNoEditable(crearNombreColumnas(), 0);
		añadirReservas(modeloTabla);
		tableHorarios.setModel(modeloTabla);
		for(int i=1; i<tableHorarios.getColumnCount(); i++)
			tableHorarios.getColumnModel().getColumn(i).setCellRenderer(rendererTabla);
	}
	
	private JButton getBtnMas() {
		if (btnMas == null) {
			btnMas = new JButton("\u2192");
			btnMas.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					semanaMas();
				}
			});
		}
		return btnMas;
	}
	private JLabel getLabel() {
		if (label == null) {
			label = new JLabel("Activida");
			label.setOpaque(true);
			label.setForeground(Color.RED);
			label.setBorder(new LineBorder(new Color(0, 0, 0)));
			label.setBackground(Color.RED);
		}
		return label;
	}
	private JLabel getLblActividad_1() {
		if (lblActividad_1 == null) {
			lblActividad_1 = new JLabel("Actividad");
		}
		return lblActividad_1;
	}
	private JLabel getLabel_1() {
		if (label_1 == null) {
			label_1 = new JLabel("Activida");
			label_1.setOpaque(true);
			label_1.setForeground(Color.YELLOW);
			label_1.setBorder(new LineBorder(new Color(0, 0, 0)));
			label_1.setBackground(Color.YELLOW);
		}
		return label_1;
	}
	private JLabel getLblCentro() {
		if (lblCentro == null) {
			lblCentro = new JLabel("Centro");
		}
		return lblCentro;
	}
	private JPanel getPanelCentral() {
		if (panelCentral == null) {
			panelCentral = new JPanel();
			panelCentral.setLayout(new BorderLayout(0, 0));
			panelCentral.add(getScrollPane());
			panelCentral.add(getPanelNavegacion(), BorderLayout.NORTH);
		}
		return panelCentral;
	}
	private JPanel getPanelNavegacion() {
		if (panelNavegacion == null) {
			panelNavegacion = new JPanel();
			panelNavegacion.add(getBtnAtras());
			panelNavegacion.add(getBtnInicio());
			panelNavegacion.add(getBtnAdelante());
		}
		return panelNavegacion;
	}
	private JButton getBtnAtras() {
		if (btnAtras == null) {
			btnAtras = new JButton("\u2190");
			btnAtras.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					semanaMenos();
				}
			});
		}
		return btnAtras;
	}
	private JButton getBtnAdelante() {
		if (btnAdelante == null) {
			btnAdelante = new JButton("\u2192");
			btnAdelante.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					semanaMas();
				}
			});
		}
		return btnAdelante;
	}
	
	private JButton getBtnInicio() {
		if (btnInicio == null) {
			btnInicio = new JButton("Inicio");
			btnInicio.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					sumSemanas=0;
					System.out.println(sumSemanas);
					modeloTabla = new ModeloNoEditable(crearNombreColumnas(), 0);
					añadirReservas(modeloTabla);
					tableHorarios.setModel(modeloTabla);
					for(int i=1; i<tableHorarios.getColumnCount(); i++)
						tableHorarios.getColumnModel().getColumn(i).setCellRenderer(rendererTabla);
				
				}
			});
		}
		return btnInicio;
	}
}
