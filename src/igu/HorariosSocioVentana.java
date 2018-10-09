package igu;

import java.awt.BorderLayout;
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
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.JButton;

@SuppressWarnings("serial")
public class HorariosSocioVentana extends JDialog {

	private JPanel contentPane;
	private JPanel panelSeleccion;
	private JLabel lblSeleccione;
	@SuppressWarnings("rawtypes")
	private JComboBox comboBoxInstalaciones;
	private JScrollPane scrollPaneTabla;
	private JTable tableHorarios;
	private ModeloNoEditable modeloTabla;
	private RenderTablaHorariosSocio rendererTabla;
	private Polideportivo poli;
	private String usuario_conectado;
	private JPanel panel;
	private JLabel lblNewLabel;
	private JLabel lblNew;
	private JLabel lblNewLabel_1;
	private JLabel lblNewLabel_2;
	private JLabel lblNewLabel_3;
	private JLabel lblOtrasReservas;
	private JPanel panelInferior;
	private JButton btnSalir;
	private JPanel panelCentral;
	private JPanel panelBotones;
	private JButton btnAtras;
	private JButton btnAdelante;
	int sumSemanas;

	/**
	 * Create the frame.
	 * @param usuario 
	 * @param poli2 
	 * @throws ParseException 
	 */
	public HorariosSocioVentana(Polideportivo poli2, String usuario) throws ParseException {
		setIconImage(Toolkit.getDefaultToolkit().getImage(ReservaInstalacionAdmVentana.class.getResource("/img/icon.jpg")));
		setTitle("Polideportivo: Horarios socio");
		setModal(true);
		modeloTabla= new ModeloNoEditable(crearNombreColumnas(),0);
		poli=poli2;
		usuario_conectado=usuario;
		rendererTabla= new RenderTablaHorariosSocio();
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 999, 580);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		contentPane.add(getPanelSeleccion(), BorderLayout.NORTH);
		contentPane.add(getPanelInferior(), BorderLayout.SOUTH);
		contentPane.add(getPanelCentral(), BorderLayout.CENTER);
		
	}

	private JPanel getPanelSeleccion() {
		if (panelSeleccion == null) {
			panelSeleccion = new JPanel();
			panelSeleccion.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
			panelSeleccion.add(getLblSeleccione());
			panelSeleccion.add(getComboBoxInstalaciones());
			panelSeleccion.add(getPanel());
		}
		return panelSeleccion;
	}
	private JLabel getLblSeleccione() {
		if (lblSeleccione == null) {
			lblSeleccione = new JLabel("Seleccione la instalaci\u00F3n para la que desea comprobar la disponibilidad: ");
		}
		return lblSeleccione;
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private JComboBox getComboBoxInstalaciones() {
		if (comboBoxInstalaciones == null) {
			comboBoxInstalaciones = new JComboBox();

			comboBoxInstalaciones.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					modeloTabla.getDataVector().removeAllElements();
					añadirReservas(modeloTabla);
					modeloTabla.fireTableDataChanged();
				}
			});
			DefaultComboBoxModel mdl = new DefaultComboBoxModel();
			mdl = llenarComboIns(mdl);
			comboBoxInstalaciones.setModel(mdl);
			comboBoxInstalaciones.setSelectedIndex(0);
		}
		return comboBoxInstalaciones;
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
				if(valor != null) {
					fila[i] = valor;
				}
				else 
					fila[i] = "";
			}
			modeloTabla.addRow(fila);
		}
	}
	
	public String reservaExistente(Instalacion in, Calendar ini, Calendar fin) {
		for(Reserva r: poli.getReservas()) {
			if(r.getEstado().equals(Reserva.LIBRE)) {
				if(r.getInstalacion().getIdPista().equals(in.getIdPista())) {
					Calendar c1 = Calendar.getInstance();c1.setTime(r.getFechaReserva());
					Calendar c2 = Calendar.getInstance();c2.setTime(r.getFechaReserva());
					c2.add(Calendar.HOUR_OF_DAY, r.getTiempoReserva());
					if((ini.equals(c1) || ini.after(c1)) && (fin.before(c2) || fin.equals(c2))) {
						if(r.isReservaCentro())
							if(r.getTipoActividad() != null)
								return r.getTipoActividad();
							else
								return "Centro";
						else {
							if(r.getCliente() instanceof Socio) {
								Socio s = (Socio)r.getCliente();
								return (s.getUsuario().equals(usuario_conectado))?"Propia":"Socio";
							}else
								return "Socio";
						}
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
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public DefaultComboBoxModel llenarComboIns(DefaultComboBoxModel mdl) {
		for(int i = 0; i < poli.getInstalaciones().size(); i++)
			mdl.addElement((Instalacion)poli.getInstalaciones().get(i));
		return mdl;
	}
	
	@SuppressWarnings("unused")
	private String[] obtenerInstalaciones(){
		String[] inst=new String[poli.getInstalaciones().size()];
		int cont = 0;
		for(Instalacion i:poli.getInstalaciones()){
			inst[cont++]=i.toString();
		}
		return inst;
	}
	
	private JScrollPane getScrollPaneTabla() {
		if (scrollPaneTabla == null) {
			scrollPaneTabla = new JScrollPane();
			scrollPaneTabla.setViewportView(getTableHorarios());
		}
		return scrollPaneTabla;
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
	private JPanel getPanel() {
		if (panel == null) {
			panel = new JPanel();
			panel.setBorder(new TitledBorder(null, "Leyenda", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			panel.add(getLblNewLabel());
			panel.add(getLblNewLabel_1());
			panel.add(getLblNew());
			panel.add(getLblNewLabel_2());
			panel.add(getLabel_1());
			panel.add(getLabel_2());
		}
		return panel;
	}
	private JLabel getLblNewLabel() {
		if (lblNewLabel == null) {
			lblNewLabel = new JLabel("New ");
			lblNewLabel.setForeground(Color.GREEN);
			lblNewLabel.setBorder(new LineBorder(new Color(0, 0, 0)));
			lblNewLabel.setOpaque(true);
			lblNewLabel.setBackground(Color.GREEN);
		}
		return lblNewLabel;
	}
	private JLabel getLblNew() {
		if (lblNew == null) {
			lblNew = new JLabel("New");
			lblNew.setBorder(new LineBorder(new Color(0, 0, 0)));
			lblNew.setOpaque(true);
			lblNew.setForeground(Color.RED);
			lblNew.setBackground(Color.RED);
		}
		return lblNew;
	}
	private JLabel getLblNewLabel_1() {
		if (lblNewLabel_1 == null) {
			lblNewLabel_1 = new JLabel("Reservas propias");
		}
		return lblNewLabel_1;
	}
	private JLabel getLblNewLabel_2() {
		if (lblNewLabel_2 == null) {
			lblNewLabel_2 = new JLabel("Actividades");
		}
		return lblNewLabel_2;
	}
	private JLabel getLabel_1() {
		if (lblNewLabel_3 == null) {
			lblNewLabel_3 = new JLabel("New");
			lblNewLabel_3.setBorder(new LineBorder(new Color(0, 0, 0)));
			lblNewLabel_3.setOpaque(true);
			lblNewLabel_3.setForeground(Color.YELLOW);
			lblNewLabel_3.setBackground(Color.YELLOW);
		}
		return lblNewLabel_3;
	}
	private JLabel getLabel_2() {
		if (lblOtrasReservas == null) {
			lblOtrasReservas = new JLabel("Otras reservas");
		}
		return lblOtrasReservas;
	}
	private JPanel getPanelInferior() {
		if (panelInferior == null) {
			panelInferior = new JPanel();
			FlowLayout flowLayout = (FlowLayout) panelInferior.getLayout();
			flowLayout.setAlignment(FlowLayout.RIGHT);
			panelInferior.add(getBtnSalir());
		}
		return panelInferior;
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
	private JPanel getPanelCentral() {
		if (panelCentral == null) {
			panelCentral = new JPanel();
			panelCentral.setLayout(new BorderLayout(0, 0));
			panelCentral.add(getScrollPaneTabla());
			panelCentral.add(getPanelBotones(), BorderLayout.NORTH);
		}
		return panelCentral;
	}
	private JPanel getPanelBotones() {
		if (panelBotones == null) {
			panelBotones = new JPanel();
			panelBotones.add(getBtnAtras());
			panelBotones.add(getBtnAdelante());
		}
		return panelBotones;
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
	
	private void semanaMenos() {
		sumSemanas--;
		modeloTabla = new ModeloNoEditable(crearNombreColumnas(), 0);
		añadirReservas(modeloTabla);
		tableHorarios.setModel(modeloTabla);
		for(int i=1; i<tableHorarios.getColumnCount(); i++)
			tableHorarios.getColumnModel().getColumn(i).setCellRenderer(rendererTabla);
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
	private void semanaMas() {
		sumSemanas++;
		modeloTabla = new ModeloNoEditable(crearNombreColumnas(), 0);
		añadirReservas(modeloTabla);
		tableHorarios.setModel(modeloTabla);
		for(int i=1; i<tableHorarios.getColumnCount(); i++)
			tableHorarios.getColumnModel().getColumn(i).setCellRenderer(rendererTabla);
	}
}
