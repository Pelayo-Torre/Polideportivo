package igu;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import logica.AñadirReservaActividad;
import logica.Actividad;
import logica.Instalacion;
import logica.Polideportivo;
import logica.Reserva;

import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;

import java.awt.SystemColor;
import java.awt.Font;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
 
public class ReservaActividadPeriodica extends JDialog {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JPanel panelReservas;
	private JPanel panelBotones;
	private JButton btnCancelar;
	private JButton btnReservar;
	private JLabel lblDia;
	private JLabel lblMes;
	private JLabel lblAo;
	private JLabel lblHoraInicio;
	private JComboBox<Integer> comboBoxDia;
	private JComboBox<String> comboBoxMes;
	private JComboBox<Integer> comboBoxAño;
	private JComboBox<String> comboBoxHinicio;
	private JLabel lblHfinal;
	private JComboBox<String> comboBoxHfinal;
	private JLabel lblInstalacion;
	private JComboBox<Instalacion> comboBoxInstalacion;
	private Polideportivo poli;
	private int duracion_reserva;
	private Date fecha_intento_reserva;
	private Instalacion instalacion_reserva;
	private JTextPane textPaneActividad;
	private JPanel panelActividades;
	private JScrollPane scrollPaneTabla;
	private JTable tablaActividades;
	private JButton btnAñadir;
	private JButton btnEliminar;
	private Actividad actSelected;
	private ModeloNoEditable modeloTablaActividades;
	private JLabel lblSemanas;
	private JSpinner spinner;
	SimpleDateFormat formato1 = new SimpleDateFormat("yyyy/MM/dd"); 
	SimpleDateFormat formato2 = new SimpleDateFormat("HH:mm"); 
	private String[] meses = {"Enero","Febrero","Marzo","Abril","Mayo","Junio","Julio","Agosto","Septiembre","Octubre","Noviembre","Diciembre"};
	DefaultComboBoxModel<Integer> comboDias = new DefaultComboBoxModel<Integer>();
	DefaultComboBoxModel<String> comboMeses = new DefaultComboBoxModel<String>();
	int cont;
	
	public ReservaActividadPeriodica(ReservaActividad ra, Actividad actividad) {
		setResizable(false);
		String[] nombreColumnas = {"Nº","Instalación","Fecha primera clase","Fecha última clase","Duración","Hora inicio","Hora fin","Día","Nº Semanas","DateInicio","DateFinal","DuracionH"};
		modeloTablaActividades = new ModeloNoEditable(nombreColumnas,0);
		setIconImage(Toolkit.getDefaultToolkit().getImage(ReservaInstalacionAdmVentana.class.getResource("/img/icon.jpg")));
		setTitle("Polideportivo: Reserva de Actividades");
		poli = ra.getPolideportivo();
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 1134, 773);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		contentPane.add(getPanelReservas(), BorderLayout.CENTER);
		contentPane.add(getPanelBotones(), BorderLayout.SOUTH);
		actSelected = actividad;
		textPaneActividad.setText("Reserva de instalación para " + actSelected.getNombreActividad());
	}

	private int duracionReserva() {
		int Hinicio = Integer.parseInt((String)getComboBoxHinicio().getSelectedItem());
		int Dia = (Integer)getComboBoxDia().getSelectedItem();
		String MesEscrito = (String)getComboBoxMes().getSelectedItem();
		int MesNumerado = conversorMes(MesEscrito);
		int Año = (Integer)getComboBoxAño().getSelectedItem();
		Calendar inicio = Calendar.getInstance();
		Calendar fin = Calendar.getInstance();
		int Hfinal = Integer.parseInt((String)getComboBoxHfinal().getSelectedItem());
		inicio.set(Año, MesNumerado, Dia, Hinicio, 0, 0);
		inicio.set(Calendar.MILLISECOND,0);
		fecha_intento_reserva = inicio.getTime();
		fin.set(Año, MesNumerado, Dia, Hfinal, 0, 0);
		fin.set(Calendar.MILLISECOND,0);
		if(inicio.equals(fin) || fin.before(inicio))
			fin.add(Calendar.DATE,1);
		int milis1 = (int) inicio.getTimeInMillis();
		int milis2 = (int)fin.getTimeInMillis();
		int diff = milis2-milis1;
		return (diff / (60 * 60 * 1000));
	}

	private Instalacion informacionPista() {
		Instalacion instalacion = (Instalacion)getComboBoxInstalacion().getSelectedItem();
		return instalacion;
	}
	
	private int conversorMes(String srt){
		for(int i = 0; i < meses.length; i++) {
			if(srt.equals(meses[i]))
				return i;
		}
		return -1;
	}
	
	public Date modificarMinSegMiliSeg(Date fecha) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(fecha);
		calendar.set(Calendar.SECOND,0); calendar.set(Calendar.MILLISECOND,0); calendar.set(Calendar.MINUTE,0);
		return calendar.getTime();
	}
	
	private boolean comprobarInstalacionDisponible(Instalacion instalacion) {
		if(instalacion.isDisponible() == false){
			JOptionPane.showMessageDialog(null, "La instalación no se encuentra disponible");
			return false;
		}
		return true;
	}
	
	public void comprobarReserva(Instalacion instalacion, Date fecha_intento_reserva, int duracion, ArrayList<Reserva> res) {
		for(Reserva reserva: poli.getReservas()) {
			if(reserva.getEstado().equals(Reserva.LIBRE)) {
				if(reserva.getInstalacion().getIdPista().equals(instalacion.getIdPista())) {
					Calendar c3 = Calendar.getInstance();c3.setTime(fecha_intento_reserva);
					Calendar c4 = (Calendar)c3.clone(); c4.add(Calendar.HOUR_OF_DAY, duracion); 
					Calendar c1 = Calendar.getInstance();c1.setTime(reserva.getFechaReserva());
					Calendar c2 = (Calendar)c1.clone(); c2.add(Calendar.HOUR_OF_DAY, reserva.getTiempoReserva());
					if(c1.before(c4) && c2.after(c3)) 
						res.add(reserva);
				}
			}
		}
	}
	
	public DefaultComboBoxModel<Instalacion> llenarComboIns(DefaultComboBoxModel<Instalacion> mdl) {
		for(int i = 0; i < poli.getInstalaciones().size(); i++)
			mdl.addElement((Instalacion)poli.getInstalaciones().get(i));
		return mdl;
	}

	private void añadirReservasActividadesTabla() {
		Object[] row = new Object[12];
		row[0] = cont++;
		row[1] = instalacion_reserva;
		row[2] = formato1.format(getFechaIntentoReserva());
		Calendar c1 = Calendar.getInstance(); c1.setTime(getFechaIntentoReserva());c1.add(Calendar.WEEK_OF_YEAR, (Integer)getSpinner().getValue() - 1);
		row[3] = formato1.format(c1.getTime());
		row[4] = getDuracion() + "h";
		row[5] = formato2.format(getFechaIntentoReserva());
		Calendar c2 = Calendar.getInstance(); c2.setTime(getFechaIntentoReserva()); c2.add(Calendar.HOUR_OF_DAY, duracion_reserva);
		row[6] = formato2.format(c2.getTime());
		Calendar c3 = Calendar.getInstance(); c3.setTime(getFechaIntentoReserva()); 
		String[] dias = {"Domingo","Lunes","Martes","Miércoles","Jueves","Viernes","Sábado"};
		row[7] = dias[c3.get(Calendar.DAY_OF_WEEK) - 1];
		row[8] = (Integer)getSpinner().getValue();
		row[9] = getFechaIntentoReserva();
		row[10] = c2.getTime();
		row[11] = getDuracion();
		modeloTablaActividades.addRow(row);
	}
	
	private JPanel getPanelReservas() {
		if (panelReservas == null) {
			panelReservas = new JPanel();
			panelReservas.setLayout(null);
			panelReservas.add(getLblDia());
			panelReservas.add(getLblMes());
			panelReservas.add(getLblAo());
			panelReservas.add(getLblHoraInicio());
			panelReservas.add(getComboBoxDia());
			panelReservas.add(getComboBoxMes());
			panelReservas.add(getComboBoxAño());
			panelReservas.add(getComboBoxHinicio());
			panelReservas.add(getLblHfinal());
			panelReservas.add(getComboBoxHfinal());
			panelReservas.add(getLblInstalacion());
			panelReservas.add(getComboBoxInstalacion());
			panelReservas.add(getTextPaneActividad());
			panelReservas.add(getPanelActividades());
			panelReservas.add(getBtnAñadir());
			panelReservas.add(getBtnEliminar());
			panelReservas.add(getLblSemanas());
			panelReservas.add(getSpinner());
		}
		return panelReservas;
	}
	private JPanel getPanelBotones() {
		if (panelBotones == null) {
			panelBotones = new JPanel();
			FlowLayout flowLayout = (FlowLayout) panelBotones.getLayout();
			flowLayout.setAlignment(FlowLayout.RIGHT);
			panelBotones.add(getBtnReservar());
			panelBotones.add(getBtnCancelar());
		}
		return panelBotones;
	}
	private JButton getBtnCancelar() {
		if (btnCancelar == null) {
			btnCancelar = new JButton("Cancelar");
			btnCancelar.setFont(new Font("Tahoma", Font.PLAIN, 15));
			btnCancelar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					dispose();
				}
			});
		}
		return btnCancelar;
	}
	private JButton getBtnReservar() {
		if (btnReservar == null) {
			btnReservar = new JButton("Reservar");
			btnReservar.setFont(new Font("Tahoma", Font.PLAIN, 15));
			btnReservar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(modeloTablaActividades.getRowCount() != -1) {
						comprobarReservasAEfectuar();
					}else 
						JOptionPane.showMessageDialog(null, "Debe añadir alguna reserva");
				}
			});
		}
		return btnReservar;
	}
	

	private void comprobarReservasAEfectuar() { //compruebo que sean validas con el resto de reservas existentes
		int count = modeloTablaActividades.getRowCount();
		Map< Integer , ArrayList<Reserva> > todasLasIncompatibles = new HashMap< Integer , ArrayList<Reserva> >();
		if(comprobarInstalacionDisponible(instalacion_reserva)) {
			for(int i = 0; i < modeloTablaActividades.getRowCount(); i++) {//cojo el intento i y compruebo la incompatibilidad con otras reservas
				int semanas = (Integer) modeloTablaActividades.getValueAt(i, 8);
				int duracion = (Integer) modeloTablaActividades.getValueAt(i, 11);
				Date fecha = (Date) modeloTablaActividades.getValueAt(i, 9);
				ArrayList<Reserva> res = comprobarTodasReservas( instalacion_reserva , duracion , fecha , semanas ); //obtendre una serie de 
				//reservas incompatibles por cada intento de reserva que hay en la tabla
				if(res.isEmpty()) //si el intento no genera incompatibilidad reduzco el contador
					count--;
				else //en caso contrario se añade al map de incompatibilidades
					todasLasIncompatibles.put((Integer) modeloTablaActividades.getValueAt(i, 0), res);
			}
			if(count == 0) {//si el contado llego a 0 es que ninguna reserva genero ninguna incompatibilidad y puedo guardar todo
				añadirReservasDeLaTabla();
			}
			else {//si el contador no es 0 aparecere la pantalla enseñando los conflictos
				abrirVentanaConfirmarActividadPeriodica(todasLasIncompatibles);
			}
		}	
	}
	
	private void añadirReservasDeLaTabla() {
		int cod = AñadirReservaActividad.obtenerIdCodMismaAct(poli);
		System.out.println("COD_MISMA_ACTIVIDAD = " + cod);
		int numeroReservas = modeloTablaActividades.getRowCount();
		añadirAct(numeroReservas, cod);
		modeloTablaActividades.getDataVector().removeAllElements();
		modeloTablaActividades.fireTableDataChanged();
		JOptionPane.showMessageDialog(null, "La reserva se ha producido correctamente.");
	}
	
	private ArrayList<Reserva> comprobarTodasReservas(Instalacion in, int duracion, Date fecha, int semanas) {
		Calendar c1 = Calendar.getInstance(); c1.setTime(fecha);
		ArrayList<Reserva> reservasExistentes = new ArrayList<Reserva>();
		for(int i = 0; i < semanas; i++) {
			comprobarReserva(instalacion_reserva,c1.getTime(),duracion,reservasExistentes);
			c1.add(Calendar.WEEK_OF_YEAR, 1);
		}
		return reservasExistentes;
	}
	
	public Date getFechaIntentoReserva() {
		return (Date)fecha_intento_reserva.clone();
	}
	
	private void añadirAct(int numeroReservas, int codMismaActividad) {
		ArrayList<Reserva> res = new ArrayList<Reserva>();
		String  idReserva = poli.obtenerIdReserva();
		for(int j = 0; j < numeroReservas; j++) {
			int semanas = (Integer) modeloTablaActividades.getValueAt(j, 8);
			int duracion = (Integer) modeloTablaActividades.getValueAt(j, 11);
			Date fecha = (Date) modeloTablaActividades.getValueAt(j, 9);
			Calendar c1 = Calendar.getInstance(); c1.setTime(fecha);
			for(int i = 0; i < semanas; i++) {
				res.add( new Reserva( instalacion_reserva , c1.getTime() , duracion , actSelected.getNombreActividad() , idReserva , Reserva.LIBRE ) );
				c1.add( Calendar.WEEK_OF_YEAR, 1);
				int idReservaEntero = Integer.parseInt(idReserva) + 1;
				idReserva = String.valueOf(idReservaEntero);
			}
		}
		AñadirReservaActividad.añadirReserva( poli , codMismaActividad , res , actSelected );
	}
	
	private void abrirVentanaConfirmarActividadPeriodica(Map< Integer , ArrayList<Reserva> > todasLasIncompatibles) {
		ConfirmarActividadPeriodica vrs = new ConfirmarActividadPeriodica(this, todasLasIncompatibles);
		vrs.setLocationRelativeTo(this);
		vrs.setModal(true);
		vrs.setVisible(true);
	}
	
	private JLabel getLblDia() {
		if (lblDia == null) {
			lblDia = new JLabel("D\u00EDa:");
			lblDia.setFont(new Font("Tahoma", Font.PLAIN, 15));
			lblDia.setBounds(91, 94, 28, 16);
		}
		return lblDia;
	}
	private JLabel getLblMes() {
		if (lblMes == null) {
			lblMes = new JLabel("Mes:");
			lblMes.setFont(new Font("Tahoma", Font.PLAIN, 15));
			lblMes.setBounds(396, 94, 38, 16);
		}
		return lblMes;
	}
	private JLabel getLblAo() {
		if (lblAo == null) {
			lblAo = new JLabel("A\u00F1o:");
			lblAo.setFont(new Font("Tahoma", Font.PLAIN, 15));
			lblAo.setBounds(724, 94, 38, 16);
		}
		return lblAo;
	}
	private JLabel getLblHoraInicio() {
		if (lblHoraInicio == null) {
			lblHoraInicio = new JLabel("Hora inicio:");
			lblHoraInicio.setFont(new Font("Tahoma", Font.PLAIN, 15));
			lblHoraInicio.setBounds(49, 148, 70, 16);
		}
		return lblHoraInicio;
	}
	private JComboBox<Integer> getComboBoxDia() {
		if (comboBoxDia == null) {
			comboBoxDia = new JComboBox<Integer>();
			comboBoxDia.setModel(comboDias);
			comboBoxDia.setBounds(131, 89, 170, 29);
		}
		return comboBoxDia;
	}
	
	private void añadirDias(String mes) {
		Calendar c1 = Calendar.getInstance(); c1.setTime(new Date());
		Calendar c2 = (Calendar) c1.clone();
		int mesInt = conversorMes(mes);
		c1.set(Calendar.MONTH, mesInt);
		for(int i = (mesInt == c2.get(Calendar.MONTH))?c1.get(Calendar.DAY_OF_MONTH):1; i <= c1.getActualMaximum(Calendar.DAY_OF_MONTH); i++) 
			comboDias.addElement(i);
	}
	
	private JComboBox<String> getComboBoxMes() {
		if (comboBoxMes == null) {
			comboBoxMes = new JComboBox<String>();
			comboBoxMes.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					Calendar c1 = Calendar.getInstance(); c1.setTime(new Date());
					if((String)comboBoxMes.getSelectedItem() != null) {
						comboDias.removeAllElements();
						añadirDias((String)comboBoxMes.getSelectedItem());
						comboBoxDia.setSelectedIndex(0);
					}
				}
			});
			comboBoxMes.setModel(comboMeses);
			comboBoxMes.setBounds(446, 89, 170, 29);
		}
		return comboBoxMes;
	}
	private void añadirMeses(int year) {
		Calendar c1 = Calendar.getInstance(); c1.setTime(new Date());
		for(int i = (year == c1.get(Calendar.YEAR))?c1.get(Calendar.MONTH):0; i < meses.length; i++) {
			comboMeses.addElement(meses[i]);

		}
	}
	
	private JComboBox<Integer> getComboBoxAño() {
		if (comboBoxAño == null) {
			comboBoxAño = new JComboBox<Integer>();
			comboBoxAño.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					comboMeses.removeAllElements();
					Calendar c1 = Calendar.getInstance(); c1.setTime(new Date());
					añadirMeses((Integer)comboBoxAño.getSelectedItem());
					comboBoxMes.setSelectedItem(0);
				}
			});
			DefaultComboBoxModel<Integer> comboAños = new DefaultComboBoxModel<>();
			añadirAños(comboAños);
			comboBoxAño.setModel(comboAños);
			comboBoxAño.setBounds(771, 89, 170, 29);
			comboBoxAño.setSelectedIndex(0);
		}
		return comboBoxAño;
	}
	private void añadirAños(DefaultComboBoxModel<Integer> combo) {
		Calendar c1 = Calendar.getInstance(); c1.setTime(new Date());
		for(int i = 0; i < 20; i++) {
			combo.addElement(c1.get(Calendar.YEAR));
			c1.add(Calendar.YEAR, 1);
		}
	}
	private JComboBox<String> getComboBoxHinicio() {
		if (comboBoxHinicio == null) {
			comboBoxHinicio = new JComboBox<String>();
			comboBoxHinicio.setModel(new DefaultComboBoxModel<String>(new String[] {"00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23"}));
			comboBoxHinicio.setBounds(131, 143, 170, 29);
		}
		return comboBoxHinicio;
	}
	private JLabel getLblHfinal() {
		if (lblHfinal == null) {
			lblHfinal = new JLabel("Hora final:");
			lblHfinal.setFont(new Font("Tahoma", Font.PLAIN, 15));
			lblHfinal.setBounds(363, 148, 70, 16);
		}
		return lblHfinal;
	}
	private JComboBox<String> getComboBoxHfinal() {
		if (comboBoxHfinal == null) {
			comboBoxHfinal = new JComboBox<String>();
			comboBoxHfinal.setModel(new DefaultComboBoxModel<String>(new String[] {"00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23"}));
			comboBoxHfinal.setBounds(446, 143, 170, 29);
		}
		return comboBoxHfinal;
	}
	private JLabel getLblInstalacion() {
		if (lblInstalacion == null) {
			lblInstalacion = new JLabel("Instalaci\u00F3n:");
			lblInstalacion.setFont(new Font("Tahoma", Font.PLAIN, 15));
			lblInstalacion.setBounds(683, 148, 76, 16);
		}
		return lblInstalacion;
	}
	private JComboBox<Instalacion> getComboBoxInstalacion() {
		if (comboBoxInstalacion == null) {
			comboBoxInstalacion = new JComboBox<Instalacion>();
			DefaultComboBoxModel<Instalacion> mdl = new DefaultComboBoxModel<Instalacion>();
			mdl = llenarComboIns(mdl);
			comboBoxInstalacion.setModel(mdl);
			comboBoxInstalacion.setBounds(771, 143, 170, 29);
		}
		return comboBoxInstalacion;
	}

	private JTextPane getTextPaneActividad() {
		if (textPaneActividad == null) {
			textPaneActividad = new JTextPane();
			textPaneActividad.setEditable(false);
			textPaneActividad.setFont(new Font("Tahoma", Font.PLAIN, 25));
			textPaneActividad.setBackground(SystemColor.menu);
			textPaneActividad.setBounds(35, 26, 906, 36);
		}
		return textPaneActividad;
	}
	private JPanel getPanelActividades() {
		if (panelActividades == null) {
			panelActividades = new JPanel();
			panelActividades.setBounds(33, 252, 908, 396);
			panelActividades.setLayout(new BorderLayout(0, 0));
			panelActividades.add(getScrollPaneTabla());
		}
		return panelActividades;
	}
	private JScrollPane getScrollPaneTabla() {
		if (scrollPaneTabla == null) {
			scrollPaneTabla = new JScrollPane();
			scrollPaneTabla.setViewportView(getTablaActividades());
		}
		return scrollPaneTabla;
	}
	
	public int getDuracion() {
		return duracion_reserva;
	}
	private JTable getTablaActividades() {
		if (tablaActividades == null) {
			tablaActividades = new JTable();
			tablaActividades = new JTable(modeloTablaActividades);
			tablaActividades.removeColumn(tablaActividades.getColumnModel().getColumn(11));
			tablaActividades.removeColumn(tablaActividades.getColumnModel().getColumn(10));
			tablaActividades.removeColumn(tablaActividades.getColumnModel().getColumn(9));
			tablaActividades.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		}
		return tablaActividades;
	}
	private JButton getBtnAñadir() {
		if (btnAñadir == null) {
			btnAñadir = new JButton("A\u00F1adir");
			btnAñadir.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Date hoy = new Date();
					duracion_reserva = duracionReserva();
					instalacion_reserva = informacionPista();
					if(!getFechaIntentoReserva().before(hoy)){
						if(añadirRepetida() && incompatibleConIntentosExistentes()) {
							añadirReservasActividadesTabla();
							modeloTablaActividades.fireTableDataChanged();
							bloquearSeleccionInstalacion();
						}
						else
							JOptionPane.showMessageDialog(null, "Estas intentando añadir una reserva que ya tienes");
					} else 
						JOptionPane.showMessageDialog(null, "La hora seleccionada ya ha pasado");
				}
			});
			btnAñadir.setFont(new Font("Tahoma", Font.PLAIN, 20));
			btnAñadir.setBounds(953, 289, 120, 67);
		}
		return btnAñadir;
	}
	
	private void bloquearSeleccionInstalacion() {
		comboBoxInstalacion.setEnabled(false);
	}
	
	private boolean incompatibleConIntentosExistentes() {
		//intento actual
		Calendar c3 = Calendar.getInstance();c3.setTime(getFechaIntentoReserva());
		Calendar c4 = (Calendar)c3.clone(); c4.add(Calendar.HOUR_OF_DAY, getDuracion()); 
		//cojo un intento i de reserva que esta en la tabla y lo comparo con el intento actual a lo largo del tiempo
		//solo necesito que una fecha del intento i sea incompatible con la fecha de inicio del intento actual para denegar la reserva del intento actual
		for(int i = 0; i < modeloTablaActividades.getRowCount(); i++) {
			Calendar c1 = Calendar.getInstance();c1.setTime((Date)modeloTablaActividades.getValueAt(i, 9)); //fecha inicio
			Calendar c2 =  Calendar.getInstance(); c2.setTime((Date)modeloTablaActividades.getValueAt(i, 10)); //fecha inicio + duracion
			for(int j = 0; j < (Integer)modeloTablaActividades.getValueAt(i, 8); j++) {
				if(c1.before(c4) && c2.after(c3)) {
					return false;
				}
				c1.add(Calendar.WEEK_OF_YEAR, 1);
				c2.add(Calendar.WEEK_OF_YEAR, 1);
			}	
		}
		return true;
	}
	
	private boolean añadirRepetida() {
		for(int i = 0; i < modeloTablaActividades.getRowCount(); i++) {
			if(getFechaIntentoReserva().equals((Date)modeloTablaActividades.getValueAt(i, 9)) && getDuracion() == (Integer)modeloTablaActividades.getValueAt(i, 11)) {
				return false;
			}
		}
		return true;
	}

	private JButton getBtnEliminar() {
		if (btnEliminar == null) {
			btnEliminar = new JButton("Eliminar");
			btnEliminar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					if(tablaActividades.getRowCount() != 0) {
						if(tablaActividades.getSelectedRow() != -1){
							modeloTablaActividades.removeRow(tablaActividades.getSelectedRow());
							if(modeloTablaActividades.getRowCount() == 0)
								activarSeleccionInstalacion();
						} else
							JOptionPane.showMessageDialog(null, "Seleccione una reserva");
					}else
						JOptionPane.showMessageDialog(null, "No hay ninguna reserva añadida");
				}
			});
			btnEliminar.setFont(new Font("Tahoma", Font.PLAIN, 20));
			btnEliminar.setBounds(953, 467, 120, 67);
		}
		return btnEliminar;
	}
	
	private void activarSeleccionInstalacion() {
		comboBoxInstalacion.setEnabled(true);
	}
	
	
	private JLabel getLblSemanas() {
		if (lblSemanas == null) {
			lblSemanas = new JLabel("N\u00BA Semanas:");
			lblSemanas.setFont(new Font("Tahoma", Font.PLAIN, 15));
			lblSemanas.setBounds(35, 200, 87, 16);
		}
		return lblSemanas;
	}
	private JSpinner getSpinner() {
		if (spinner == null) {
			spinner = new JSpinner();
			spinner.setModel(new SpinnerNumberModel(new Integer(1), new Integer(1), null, new Integer(1)));
			spinner.setBounds(131, 195, 55, 29);
			spinner.setEditor(new JSpinner.DefaultEditor(spinner));
		}
		return spinner;
	}
}
