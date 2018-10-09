package igu;

import java.awt.BorderLayout;


import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import logica.Instalacion;
import logica.Polideportivo;
import logica.Reserva;


import javax.swing.JButton;
import java.awt.FlowLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.DefaultComboBoxModel;
import java.awt.event.ActionListener;

import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.awt.event.ActionEvent;

import java.awt.Toolkit;


public class ReservaInstalacionAdmVentana extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JPanel panelReservas;
	private JPanel panelBotones;
	private JButton btnCancelar;
	private JButton btnReservar;
	private JLabel lblReservasDeInstalaciones;
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
	private JLabel lblInstalacin;
	private JComboBox<Instalacion> comboBoxInstalacion;
	private Polideportivo poli;
	private JLabel lblParaReservarLa;
	private int duracion_reserva;
	private Date fecha_intento_reserva;
	private Instalacion instalacion_reserva;
	DefaultComboBoxModel<Integer> comboDias = new DefaultComboBoxModel<Integer>();
	DefaultComboBoxModel<String> comboMeses = new DefaultComboBoxModel<String>();
	private String[] meses = {"Enero","Febrero","Marzo","Abril","Mayo","Junio","Julio","Agosto","Septiembre","Octubre","Noviembre","Diciembre"};
	

	public ReservaInstalacionAdmVentana(VentanaAdminPrincipalUltimate ventanaAdminPrincipalUltimate) {
		setIconImage(Toolkit.getDefaultToolkit().getImage(ReservaInstalacionAdmVentana.class.getResource("/img/icon.jpg")));
		setTitle("Poliportivo: Reserva de Instalaciones");
		poli = ventanaAdminPrincipalUltimate.getPolideportivo();
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 667, 368);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		contentPane.add(getPanelReservas(), BorderLayout.CENTER);
		contentPane.add(getPanelBotones(), BorderLayout.SOUTH);
	}

	private JPanel getPanelReservas() {
		if (panelReservas == null) {
			panelReservas = new JPanel();
			panelReservas.setLayout(null);
			panelReservas.add(getLblReservasDeInstalaciones());
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
			panelReservas.add(getLblInstalacin());
			panelReservas.add(getComboBoxInstalacion());
			panelReservas.add(getLblParaReservarLa());

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
			btnReservar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Date hoy = new Date();
					duracion_reserva = duracionReserva();
					instalacion_reserva = informacionPista();
					if(!fecha_intento_reserva.before(hoy)) {
						if(comprobarReserva(instalacion_reserva)) {
							añadirReserva(instalacion_reserva);
							JOptionPane.showMessageDialog(null, "La reserva se ha producido correctamente.");
						}
					} else 
						JOptionPane.showMessageDialog(null, "La hora seleccionada ya ha pasado");
				}
			});
		}
		return btnReservar;
	}
	private JLabel getLblReservasDeInstalaciones() {
		if (lblReservasDeInstalaciones == null) {
			lblReservasDeInstalaciones = new JLabel("Reservas de instalaciones ");
			lblReservasDeInstalaciones.setBounds(12, 23, 274, 16);
		}
		return lblReservasDeInstalaciones;
	}
	private JLabel getLblDia() {
		if (lblDia == null) {
			lblDia = new JLabel("D\u00EDa:");
			lblDia.setBounds(46, 96, 42, 16);
		}
		return lblDia;
	}
	private JLabel getLblMes() {
		if (lblMes == null) {
			lblMes = new JLabel("Mes:");
			lblMes.setBounds(46, 145, 42, 16);
		}
		return lblMes;
	}
	private JLabel getLblAo() {
		if (lblAo == null) {
			lblAo = new JLabel("A\u00F1o:");
			lblAo.setBounds(46, 196, 42, 16);
		}
		return lblAo;
	}
	private JLabel getLblHoraInicio() {
		if (lblHoraInicio == null) {
			lblHoraInicio = new JLabel("Hora inicio:");
			lblHoraInicio.setBounds(312, 145, 80, 16);
		}
		return lblHoraInicio;
	}
	private JComboBox<Integer> getComboBoxDia() {
		if (comboBoxDia == null) {
			comboBoxDia = new JComboBox<Integer>();
			comboBoxDia.setModel(comboDias);
			comboBoxDia.setBounds(94, 93, 145, 22);
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
				public void actionPerformed(ActionEvent e) {
					Calendar c1 = Calendar.getInstance(); c1.setTime(new Date());
					if((String)comboBoxMes.getSelectedItem() != null) {
						comboDias.removeAllElements();
						añadirDias((String)comboBoxMes.getSelectedItem());
						comboBoxDia.setSelectedIndex(0);
					}
				}
			});
			comboBoxMes.setModel(comboMeses);
			comboBoxMes.setBounds(94, 142, 145, 22);
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
			comboBoxAño.setBounds(94, 193, 145, 22);
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
			comboBoxHinicio.setBounds(404, 142, 145, 22);
		}
		return comboBoxHinicio;
	}
	private JLabel getLblHfinal() {
		if (lblHfinal == null) {
			lblHfinal = new JLabel("Hora final:");
			lblHfinal.setBounds(312, 196, 80, 16);
		}
		return lblHfinal;
	}
	private JComboBox<String> getComboBoxHfinal() {
		if (comboBoxHfinal == null) {
			comboBoxHfinal = new JComboBox<String>();
			comboBoxHfinal.setModel(new DefaultComboBoxModel<String>(new String[] {"00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23"}));
			comboBoxHfinal.setBounds(404, 193, 145, 22);
		}
		return comboBoxHfinal;
	}
	private JLabel getLblInstalacin() {
		if (lblInstalacin == null) {
			lblInstalacin = new JLabel("Instalaci\u00F3n:");
			lblInstalacin.setBounds(312, 96, 66, 16);
		}
		return lblInstalacin;
	}
	private JComboBox<Instalacion> getComboBoxInstalacion() {
		if (comboBoxInstalacion == null) {
			comboBoxInstalacion = new JComboBox<Instalacion>();
			DefaultComboBoxModel<Instalacion> mdl = new DefaultComboBoxModel<Instalacion>();
			mdl = llenarComboIns(mdl);
			comboBoxInstalacion.setModel(mdl);
			comboBoxInstalacion.setBounds(402, 93, 145, 22);
		}
		return comboBoxInstalacion;
	}
	
//	private boolean comprobarFechaNoValida() {
//		if(c.comprobarDiasMes(Integer.parseInt((String) comboBoxDia.getSelectedItem()),c.mes((String)comboBoxMes.getSelectedItem())) == false){
//			JOptionPane.showMessageDialog(null, "Por favor, introduzca una fecha correcta.");
//			return false;
//		}
//		else{
//			return true;
//		}
//	}
	
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
	
	private void añadirReserva(Instalacion instalacion_reserva) {
		boolean esActividad = false;
		//******* GUARDADO LOCAL *******
		String  idReserva = poli.obtenerIdReserva();
		Reserva r =  new Reserva(instalacion_reserva, fecha_intento_reserva, duracion_reserva,null,idReserva,Reserva.LIBRE);
		poli.getReservas().add(r);
		
		Collections.sort(poli.getReservas(), new Comparator<Reserva>() {

            @Override
            public int compare(Reserva arg0, Reserva arg1) {
                return arg0.getFechaReserva().compareTo(arg1.getFechaReserva());
            }

        });
		System.out.println("Reserva añadida :" + r.getIdReserva());
		//******* GUARDADO BD *******
		try {
			poli.getConexion().añadirReserva(idReserva,instalacion_reserva.getIdPista(),  fecha_intento_reserva, duracion_reserva,null, null, esActividad);
		} catch (SQLException e) {
			e.printStackTrace();
		}
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
	private JLabel getLblParaReservarLa() {
		if (lblParaReservarLa == null) {
			lblParaReservarLa = new JLabel("Para reservar la instalacion durante 24h seleccione la misma hora de inicio y final.");
			lblParaReservarLa.setBounds(65, 52, 515, 16);
		}
		return lblParaReservarLa;
	}
	
	public Date modificarMinSegMiliSeg(Date fecha) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(fecha);
		calendar.set(Calendar.SECOND,0); calendar.set(Calendar.MILLISECOND,0); calendar.set(Calendar.MINUTE,0);
		return calendar.getTime();
	}
	
	public boolean comprobarReserva(Instalacion instalacion) {
		if(instalacion.isDisponible() == false){
			JOptionPane.showMessageDialog(null, "La instalación no se encuentra disponible");
			return false;
		}
		Calendar calendar1 = Calendar.getInstance();
		calendar1.setTime(fecha_intento_reserva);
		Date date1 = calendar1.getTime();
		ArrayList<Reserva> reservas = poli.getReservas();
		for(int i = 0; i < reservas.size(); i++) {
			if(reservas.get(i).getEstado().equals(Reserva.LIBRE)) {
				if(reservas.get(i).getInstalacion().getIdPista().equals(instalacion.getIdPista())) {
					Calendar calendar2 = Calendar.getInstance();
					calendar2.setTime(reservas.get(i).getFechaReserva());
					Calendar calendar3 = Calendar.getInstance();
					calendar3.setTime(reservas.get(i).getFechaReserva());
					calendar3.add(Calendar.HOUR, reservas.get(i).getTiempoReserva());
					Date date2 = calendar2.getTime();
					Date date3 = calendar3.getTime();
					Date hoy = new Date();
					if(date1.before(hoy)) {
						JOptionPane.showMessageDialog(null, "La fecha ya ha pasado");
						return false;
					}
					if((date1.after(date2) || date2.equals(date1)) && date1.before(date3)) {
						JOptionPane.showMessageDialog(null, "Ya existe una reserva");
						return false;
					}
				}
			}
		}
		return true;
	}
	
	public DefaultComboBoxModel<Instalacion> llenarComboIns(DefaultComboBoxModel<Instalacion> mdl) {
		for(int i = 0; i < poli.getInstalaciones().size(); i++)
			mdl.addElement((Instalacion)poli.getInstalaciones().get(i));
		return mdl;
	}
	
}
