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
import javax.swing.JOptionPane;
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

import java.awt.Font;

@SuppressWarnings({ "rawtypes", "serial" })
public class SocioEligeHorarioReserva extends JDialog {

	private JPanel contentPane;
	private JPanel panelSeleccion;
	private JLabel lblSeleccione;
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
	private JPanel panel_1;
	private JButton botonReservar;
	private SocioVentana sv;
	private String dni;
	private String socio;
	int sumSemanas;


	/**
	 * Create the frame.
	 * @param usuario 
	 * @param poli2 
	 * @throws ParseException 
	 */
	public SocioEligeHorarioReserva(Polideportivo poli2, String usuario, SocioVentana sv2) throws ParseException {
		setIconImage(Toolkit.getDefaultToolkit().getImage(ReservaInstalacionAdmVentana.class.getResource("/img/icon.jpg")));
		setTitle("Polideportivo: Horarios socio");
		setModal(true);
		sv = sv2;
		modeloTabla= new ModeloNoEditable(crearNombreColumnas(),0);
		poli=poli2;
		dni = sv2.getDNI();
		socio = sv2.getSocio();
		usuario_conectado=usuario;
		rendererTabla= new RenderTablaHorariosSocio();
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 999, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		contentPane.add(getPanelSeleccion(), BorderLayout.NORTH);
		contentPane.add(getPanel_1(), BorderLayout.SOUTH);
		contentPane.add(getPanelCentral(), BorderLayout.CENTER);
	}

	public Polideportivo getPoli() {
		return poli;
	}
	
	public String getDNI(){
		return dni;
	}
	
	public String getSocio(){
		return socio;
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
	@SuppressWarnings("unchecked")
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
	
	public void actualizarTabla(){
		modeloTabla.getDataVector().removeAllElements();
		añadirReservas(modeloTabla);
		modeloTabla.fireTableDataChanged();
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
	
	@SuppressWarnings({ "unchecked", "static-access" })
	public DefaultComboBoxModel llenarComboIns(DefaultComboBoxModel mdl) {
		for(int i = 0; i < poli.getInstalaciones().size(); i++){
			if(poli.getInstalaciones().get(i).getTipo().equals(sv.getInstalacion())){
				mdl.addElement((Instalacion)poli.getInstalaciones().get(i));
			}
		}
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
//			String[] nombreColumnas={"Lunes","Martes","Miércoles","Jueves","Viernes","Sábado","Domingo"};
//			modeloTabla= new ModeloNoEditable(nombreColumnas,24);
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
	private JPanel getPanel_1() {
		if (panel_1 == null) {
			panel_1 = new JPanel();
			panel_1.setLayout(new BorderLayout(0, 0));
			panel_1.add(getPanel_2(), BorderLayout.EAST);
		}
		return panel_1;
	}
	private JButton getBotonReservar() {
		if (botonReservar == null) {
			botonReservar = new JButton("Reservar");
			botonReservar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					if(horarioSeleccionado(tableHorarios) == false){
						JOptionPane.showMessageDialog(null, "Seleccione un horario para hacer la reserva");
					}
					else
					{
						if(obtenerPosicion()==null){
							
						}
						else{
							mostrarVentanaSocioHaceReserva();	
						}
						
					}
				}
			});
			botonReservar.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return botonReservar;
	}
	
	private JButton getBotonCancelar() {
		if (botonCancelar == null) {
			botonCancelar = new JButton("Cancelar");
			botonCancelar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					dispose();
				}
			});
			botonCancelar.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return botonCancelar;
	}
	private JPanel getPanel_2() {
		if (panel_2 == null) {
			panel_2 = new JPanel();
			panel_2.add(getBotonReservar());
			panel_2.add(getBotonCancelar());
		}
		return panel_2;
	}
	
	
	
	
	//************************NUEVO*********************************
	
	private  String horaInicio;
	private  String horaFinal;
	private  int mes;
	private  String dia;
	private  String instalacion;
	private  String nPista;
	private JButton botonCancelar;
	private JPanel panel_2;
	private JPanel panelCentral;
	private JPanel panelNavegacion;
	private JButton btnAtras;
	private JButton btnAdelante;
	private JButton btnInicio;
	
	public  String getHoraInicio(){
		return horaInicio;
	}
	public  String getHoraFinal(){
		return horaFinal;
	}
	public  int getMes(){
		return mes;
	}
	public  String getDia(){
		return dia;
	}
	public String getInstalacion(){
		return instalacion;
	}
	public  String getNPista(){
		return nPista;
	}
	
	/**
	 * Obtiene la posicion del horario especificado
	 * @return
	 */
	private String [] obtenerPosicion(){
		String [] pareja= new String [2];
		int fila = tableHorarios.getSelectedRow();
		int columna = tableHorarios.getSelectedColumn();
		String valorFila = (String) tableHorarios.getValueAt(fila, 0);
		String [] a = crearNombreColumnas();
		String valorColumna = a[columna];
		pareja[0] = valorFila;
		pareja[1] = valorColumna;
		if(!tableHorarios.getValueAt(fila, columna).equals("")){
			if(tableHorarios.getValueAt(fila, columna).equals("NO DISPONIBLE")){
				JOptionPane.showMessageDialog(null, "La instalación no se encuentra disponible en el horario especificado");
			}
			else if(tableHorarios.getValueAt(fila, columna).equals("Propia")){
				JOptionPane.showMessageDialog(null, "Usted ya tiene una reserva en el horario especificado");
			}
			else{
				JOptionPane.showMessageDialog(null, "Ya hay una reserva en el horario especificado");
			}
			return null;
		}
		else{
			obtenerValores(pareja);
			return pareja;
		}
	}
	
	/**
	 * Obtiene los valores del horario especificado.
	 * @param pareja
	 */
	@SuppressWarnings("static-access")
	private void obtenerValores(String [] pareja){
		String hora = pareja[0];
		String diaMes = pareja[1];
		String delimiter1 = ":";
		String delimiter2 = "/";
		String[] temp1;
		String[] temp2;
		temp1 = hora.split(delimiter1);
		temp2 = diaMes.split(delimiter2);
		String horaInicio = temp1[0];
		String dia = temp2[0];
		String mes = temp2[1];
		String horaFinal = calcularHoraFinal(horaInicio);
		String nPista = obtenerPista();
		
		this.horaInicio = horaInicio;
		this.mes = Integer.parseInt(mes)-1;
		this.dia = dia;
		this.horaFinal = horaFinal;
		System.out.println(horaFinal);
		System.out.println(this.horaFinal);
		this.instalacion = sv.getInstalacion();
		this.nPista = nPista;
		
	}
	
	private String calcularHoraFinal(String horaInicio){
		int hi = Integer.parseInt(horaInicio);
		int horaFinal = hi+1;
		if(horaFinal == 24){
			horaFinal = 00;}
		else if(horaFinal<10){
			//Si no el combo no lo pilla.
			return ("0"+String.valueOf(horaFinal));
		}
		return String.valueOf(horaFinal);
	}
	
	private void mostrarVentanaSocioHaceReserva(){
		this.setModal(false);
		SocioHaceReservaVentana vshr = new SocioHaceReservaVentana(this);
		vshr.setLocationRelativeTo(this);
		vshr.setVisible(true);
	}
	
	private String obtenerPista(){
		String instalacion = String.valueOf(comboBoxInstalaciones.getSelectedItem());
		if(instalacion.contains("1")){
			return "1";
		}
		return "2";
	}
	
	/**
	 * Método para asegurarse de que hay una fila seleccionada.
	 * @param tabla
	 * @return
	 */
	public boolean horarioSeleccionado(JTable tabla){
		if(tabla.getSelectedRow() == -1)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	
	//**************************************************************
	
	
	private JPanel getPanelCentral() {
		if (panelCentral == null) {
			panelCentral = new JPanel();
			panelCentral.setLayout(new BorderLayout(0, 0));
			panelCentral.add(getScrollPaneTabla());
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
			btnAtras.setEnabled(false);
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
	
	private void semanaMenos() {
			sumSemanas--;
			if(sumSemanas==0)getBtnAtras().setEnabled(false);
			System.out.println(sumSemanas);
			modeloTabla = new ModeloNoEditable(crearNombreColumnas(), 0);
			añadirReservas(modeloTabla);
			tableHorarios.setModel(modeloTabla);
			for(int i=1; i<tableHorarios.getColumnCount(); i++)
				tableHorarios.getColumnModel().getColumn(i).setCellRenderer(rendererTabla);
		
	}
	
	private void semanaMas() {
		getBtnAtras().setEnabled(true);
		sumSemanas++;
		System.out.println(sumSemanas);
		modeloTabla = new ModeloNoEditable(crearNombreColumnas(), 0);
		añadirReservas(modeloTabla);
		tableHorarios.setModel(modeloTabla);
		for(int i=1; i<tableHorarios.getColumnCount(); i++)
			tableHorarios.getColumnModel().getColumn(i).setCellRenderer(rendererTabla);
	}
	
	private JButton getBtnInicio() {
		if (btnInicio == null) {
			btnInicio = new JButton("Inicio");
			btnInicio.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					getBtnAtras().setEnabled(false);
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
