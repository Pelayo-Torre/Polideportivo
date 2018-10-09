package igu;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import logica.Cliente;
import logica.Comprobaciones;
import logica.Instalacion;
import logica.Polideportivo;
import logica.Reserva;
import logica.Socio;

import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;

import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.awt.event.ActionEvent;
import java.awt.Color;


@SuppressWarnings({ "rawtypes", "serial" })
public class SocioHaceReservaVentana extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JLabel labelSocio;
	private JTextField textSocio;
	private JPanel panelReserva;
	private JLabel lblNewLabel;
	private JComboBox comboDia;
	private JLabel lblNewLabel_1;
	private JComboBox comboMes;
	private JLabel lblNewLabel_2;
	private JComboBox comboAno;
	private JLabel lblNewLabel_3;
	private JComboBox comboHoraInicio;
	private JLabel lblNewLabel_4;
	private JComboBox comboHoraFinal;
	private JLabel lblNewLabel_5;
	private JTextField textInstalacion;
	private JPanel panelPago;
	private JLabel lblNewLabel_6;
	private JRadioButton radioBotonCuota;
	private JRadioButton radioBotonMano;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JPanel panelObservaciones;
	private JLabel lblNewLabel_7;
	private JLabel lblNewLabel_8;
	private JLabel lblNewLabel_9;
	private JLabel lblNewLabel_10;
	private JLabel lblNewLabel_11;
	private JLabel lblNewLabel_12;
	private JLabel lblNewLabel_13;
	
	private Comprobaciones c;
	Calendar calendar;
	static SocioEligeHorarioReserva vs;
	private Polideportivo poli;
	
	private JLabel lblNewLabel_14;
	private JComboBox comboPista;
	
	private String dni;

	/**
	 * Create the dialog.
	 */
	@SuppressWarnings("static-access")
	public SocioHaceReservaVentana(SocioEligeHorarioReserva vs1) {
		setIconImage(Toolkit.getDefaultToolkit().getImage(ReservaInstalacionAdmVentana.class.getResource("/img/icon.jpg")));
		setTitle("Polideportivo: Efectuar reserva");
		setModal(true);
		poli = vs1.getPoli();
		this.vs = vs1;
		c = new Comprobaciones();
		calendar = Calendar.getInstance();
		
		
		
		setBounds(100, 100, 636, 383);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		contentPanel.add(getLabelSocio());
		contentPanel.add(getTextSocio());
		contentPanel.add(getPanelReserva());
		contentPanel.add(getPanelPago());
		contentPanel.add(getPanel_1());
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton botonConfirmar = new JButton("Confirmar Reserva");
				botonConfirmar.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						finalizarReserva();
						vs1.actualizarTabla();
						
					}
				});
				botonConfirmar.setFont(new Font("Dialog", Font.BOLD, 12));
				botonConfirmar.setActionCommand("OK");
				buttonPane.add(botonConfirmar);
				getRootPane().setDefaultButton(botonConfirmar);
			}
			{
				JButton botonCancelar = new JButton("Cancelar");
				botonCancelar.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						vs.setModal(true);
						dispose();
					}
				});
				botonCancelar.setFont(new Font("Dialog", Font.BOLD, 12));
				botonCancelar.setActionCommand("Cancel");
				buttonPane.add(botonCancelar);
			}
		}
		
		textSocio.setText(vs1.getSocio().toString());
		dni=vs1.getDNI();
		comboHoraInicio.setSelectedItem(vs1.getHoraInicio());
		comboHoraFinal.setSelectedItem(vs1.getHoraFinal());
		comboDia.setSelectedItem(vs1.getDia());
		comboMes.setSelectedIndex(vs1.getMes());
		textInstalacion.setText(vs1.getInstalacion());
		comboPista.setSelectedItem(vs1.getNPista());
		
	}
	private JLabel getLabelSocio() {
		if (labelSocio == null) {
			labelSocio = new JLabel("Socio:");
			labelSocio.setBounds(10, 11, 48, 26);
			labelSocio.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return labelSocio;
	}
	private JTextField getTextSocio() {
		if (textSocio == null) {
			textSocio = new JTextField();
			textSocio.setForeground(Color.BLUE);
			textSocio.setBounds(57, 11, 86, 26);
			textSocio.setHorizontalAlignment(SwingConstants.CENTER);
			textSocio.setEditable(false);
			textSocio.setFont(new Font("Dialog", Font.BOLD, 14));
			textSocio.setColumns(10);
		}
		return textSocio;
	}
	private JPanel getPanelReserva() {
		if (panelReserva == null) {
			panelReserva = new JPanel();
			panelReserva.setBounds(10, 48, 375, 128);
			panelReserva.setBorder(new TitledBorder(null, "Datos de la Reserva", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			panelReserva.setLayout(null);
			panelReserva.add(getLblNewLabel());
			panelReserva.add(getComboDia());
			panelReserva.add(getLblNewLabel_1());
			panelReserva.add(getComboMes());
			panelReserva.add(getLblNewLabel_2());
			panelReserva.add(getComboAno());
			panelReserva.add(getLblNewLabel_3());
			panelReserva.add(getComboHoraInicio());
			panelReserva.add(getLblNewLabel_4());
			panelReserva.add(getComboHoraFinal());
			panelReserva.add(getLblNewLabel_5());
			panelReserva.add(getTextInstalacion());
			panelReserva.add(getLblNewLabel_14());
			panelReserva.add(getComboPista());
		}
		return panelReserva;
	}
	private JLabel getLblNewLabel() {
		if (lblNewLabel == null) {
			lblNewLabel = new JLabel("D\u00EDa:");
			lblNewLabel.setFont(new Font("Dialog", Font.BOLD, 12));
			lblNewLabel.setBounds(20, 22, 34, 25);
		}
		return lblNewLabel;
	}
	@SuppressWarnings("unchecked")
	private JComboBox getComboDia() {
		if (comboDia == null) {
			comboDia = new JComboBox();
			comboDia.setBackground(Color.WHITE);
			comboDia.setForeground(Color.BLUE);
			int dia = calendar.get(Calendar.DAY_OF_MONTH)-1;
			comboDia.setModel(new DefaultComboBoxModel(new String[] {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"}));
			comboDia.setSelectedIndex(dia);
			comboDia.setFont(new Font("Dialog", Font.BOLD, 12));
			comboDia.setBounds(54, 24, 50, 22);
		}
		return comboDia;
	}
	private JLabel getLblNewLabel_1() {
		if (lblNewLabel_1 == null) {
			lblNewLabel_1 = new JLabel("Mes:");
			lblNewLabel_1.setFont(new Font("Dialog", Font.BOLD, 12));
			lblNewLabel_1.setBounds(115, 22, 34, 25);
		}
		return lblNewLabel_1;
	}
	@SuppressWarnings("unchecked")
	private JComboBox getComboMes() {
		if (comboMes == null) {
			comboMes = new JComboBox();
			comboMes.setBackground(Color.WHITE);
			comboMes.setForeground(Color.BLUE);
			int mes = calendar.get(Calendar.MONTH);
			comboMes.setModel(new DefaultComboBoxModel(new String[] {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"}));
			comboMes.setSelectedIndex(mes);
			comboMes.setFont(new Font("Dialog", Font.BOLD, 12));
			comboMes.setBounds(150, 22, 107, 25);
		}
		return comboMes;
	}
	private JLabel getLblNewLabel_2() {
		if (lblNewLabel_2 == null) {
			lblNewLabel_2 = new JLabel("A\u00F1o:");
			lblNewLabel_2.setFont(new Font("Dialog", Font.BOLD, 12));
			lblNewLabel_2.setBounds(267, 22, 34, 25);
		}
		return lblNewLabel_2;
	}
	@SuppressWarnings("unchecked")
	private JComboBox getComboAno() {
		if (comboAno == null) {
			comboAno = new JComboBox();
			comboAno.setBackground(Color.WHITE);
			int ano = calendar.get(Calendar.YEAR);
			comboAno.setModel(new DefaultComboBoxModel(new String[] {"2017", "2018", "2019", "2020", "2021", "2022", "2023", "2024", "2025", "2026", "2027", "2028", "2029", "2030", "2031", "2032", "2033", "2034", "2035", "2036", "2037", "2038", "2039", "2040"}));
			comboAno.setSelectedItem(ano);
			comboAno.setFont(new Font("Dialog", Font.BOLD, 12));
			comboAno.setBounds(301, 22, 55, 22);
		}
		return comboAno;
	}
	private JLabel getLblNewLabel_3() {
		if (lblNewLabel_3 == null) {
			lblNewLabel_3 = new JLabel("Hora de Inicio:");
			lblNewLabel_3.setFont(new Font("Dialog", Font.BOLD, 12));
			lblNewLabel_3.setBounds(20, 58, 81, 25);
		}
		return lblNewLabel_3;
	}
	@SuppressWarnings("unchecked")
	private JComboBox getComboHoraInicio() {
		if (comboHoraInicio == null) {
			comboHoraInicio = new JComboBox();
			comboHoraInicio.setBackground(Color.WHITE);
			comboHoraInicio.setForeground(Color.BLUE);
			comboHoraInicio.setModel(new DefaultComboBoxModel(new String[] {"00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23"}));
			comboHoraInicio.setFont(new Font("Dialog", Font.BOLD, 12));
			comboHoraInicio.setBounds(111, 58, 81, 22);
		}
		return comboHoraInicio;
	}
	private JLabel getLblNewLabel_4() {
		if (lblNewLabel_4 == null) {
			lblNewLabel_4 = new JLabel("Hora final:");
			lblNewLabel_4.setFont(new Font("Dialog", Font.BOLD, 12));
			lblNewLabel_4.setBounds(210, 58, 68, 25);
		}
		return lblNewLabel_4;
	}
	@SuppressWarnings("unchecked")
	private JComboBox getComboHoraFinal() {
		if (comboHoraFinal == null) {
			comboHoraFinal = new JComboBox();
			comboHoraFinal.setBackground(Color.WHITE);
			comboHoraFinal.setFont(new Font("Dialog", Font.BOLD, 12));
			comboHoraFinal.setModel(new DefaultComboBoxModel(new String[] {"00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23"}));
			comboHoraFinal.setBounds(275, 60, 81, 22);
		}
		return comboHoraFinal;
	}
	private JLabel getLblNewLabel_5() {
		if (lblNewLabel_5 == null) {
			lblNewLabel_5 = new JLabel("Instalaci\u00F3n:");
			lblNewLabel_5.setFont(new Font("Dialog", Font.BOLD, 12));
			lblNewLabel_5.setBounds(23, 94, 81, 22);
		}
		return lblNewLabel_5;
	}
	private JTextField getTextInstalacion() {
		if (textInstalacion == null) {
			textInstalacion = new JTextField();
			textInstalacion.setForeground(Color.BLUE);
			textInstalacion.setEditable(false);
			textInstalacion.setFont(new Font("Dialog", Font.BOLD, 14));
			textInstalacion.setBounds(106, 91, 138, 25);
			textInstalacion.setColumns(10);
		}
		return textInstalacion;
	}
	private JPanel getPanelPago() {
		if (panelPago == null) {
			panelPago = new JPanel();
			panelPago.setBounds(10, 184, 375, 114);
			panelPago.setBorder(new TitledBorder(null, "Forma de pago", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			panelPago.setLayout(null);
			panelPago.add(getLblNewLabel_6());
			panelPago.add(getRadioBotonCuota());
			panelPago.add(getRadioBotonMano());
		}
		return panelPago;
	}
	private JLabel getLblNewLabel_6() {
		if (lblNewLabel_6 == null) {
			lblNewLabel_6 = new JLabel("Seleccione la forma de pago:");
			lblNewLabel_6.setFont(new Font("Dialog", Font.BOLD, 12));
			lblNewLabel_6.setBounds(10, 22, 295, 14);
		}
		return lblNewLabel_6;
	}
	private JRadioButton getRadioBotonCuota() {
		if (radioBotonCuota == null) {
			radioBotonCuota = new JRadioButton("A\u00F1adir importe a la cuota.");
			radioBotonCuota.setSelected(true);
			buttonGroup.add(radioBotonCuota);
			radioBotonCuota.setFont(new Font("Dialog", Font.BOLD, 12));
			radioBotonCuota.setBounds(20, 43, 188, 23);
		}
		return radioBotonCuota;
	}
	private JRadioButton getRadioBotonMano() {
		if (radioBotonMano == null) {
			radioBotonMano = new JRadioButton("En efectivo.");
			buttonGroup.add(radioBotonMano);
			radioBotonMano.setFont(new Font("Dialog", Font.BOLD, 12));
			radioBotonMano.setBounds(20, 69, 109, 23);
		}
		return radioBotonMano;
	}
	private JPanel getPanel_1() {
		if (panelObservaciones == null) {
			panelObservaciones = new JPanel();
			panelObservaciones.setBorder(new TitledBorder(null, "Observaciones", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			panelObservaciones.setBounds(395, 48, 216, 250);
			panelObservaciones.setLayout(null);
			panelObservaciones.add(getLblNewLabel_7());
			panelObservaciones.add(getLblNewLabel_8());
			panelObservaciones.add(getLblNewLabel_9());
			panelObservaciones.add(getLblNewLabel_10());
			panelObservaciones.add(getLblNewLabel_11());
			panelObservaciones.add(getLblNewLabel_12());
			panelObservaciones.add(getLblNewLabel_13());
		}
		return panelObservaciones;
	}
	private JLabel getLblNewLabel_7() {
		if (lblNewLabel_7 == null) {
			lblNewLabel_7 = new JLabel("* El n\u00FAmero m\u00E1ximo de horas ");
			lblNewLabel_7.setFont(new Font("Dialog", Font.BOLD, 12));
			lblNewLabel_7.setBounds(10, 27, 196, 23);
		}
		return lblNewLabel_7;
	}
	private JLabel getLblNewLabel_8() {
		if (lblNewLabel_8 == null) {
			lblNewLabel_8 = new JLabel("   que puede reservar son 2.");
			lblNewLabel_8.setFont(new Font("Dialog", Font.BOLD, 12));
			lblNewLabel_8.setBounds(10, 44, 196, 23);
		}
		return lblNewLabel_8;
	}
	private JLabel getLblNewLabel_9() {
		if (lblNewLabel_9 == null) {
			lblNewLabel_9 = new JLabel("* No puede tener 2 reservar ");
			lblNewLabel_9.setFont(new Font("Dialog", Font.BOLD, 12));
			lblNewLabel_9.setBounds(10, 78, 196, 23);
		}
		return lblNewLabel_9;
	}
	private JLabel getLblNewLabel_10() {
		if (lblNewLabel_10 == null) {
			lblNewLabel_10 = new JLabel("   simult\u00E1neamente.");
			lblNewLabel_10.setFont(new Font("Dialog", Font.BOLD, 12));
			lblNewLabel_10.setBounds(10, 96, 196, 23);
		}
		return lblNewLabel_10;
	}
	private JLabel getLblNewLabel_11() {
		if (lblNewLabel_11 == null) {
			lblNewLabel_11 = new JLabel("* La reserva se puede hacer con ");
			lblNewLabel_11.setFont(new Font("Dialog", Font.BOLD, 12));
			lblNewLabel_11.setBounds(10, 132, 196, 23);
		}
		return lblNewLabel_11;
	}
	private JLabel getLblNewLabel_12() {
		if (lblNewLabel_12 == null) {
			lblNewLabel_12 = new JLabel("   una antelaci\u00F3n de 7 d\u00EDas y hasta ");
			lblNewLabel_12.setFont(new Font("Dialog", Font.BOLD, 12));
			lblNewLabel_12.setBounds(10, 149, 196, 23);
		}
		return lblNewLabel_12;
	}
	private JLabel getLblNewLabel_13() {
		if (lblNewLabel_13 == null) {
			lblNewLabel_13 = new JLabel("   el momento de su inicio.");
			lblNewLabel_13.setFont(new Font("Dialog", Font.BOLD, 12));
			lblNewLabel_13.setBounds(10, 166, 196, 23);
		}
		return lblNewLabel_13;
	}
	
	
	
	
	
	
	
	
	private void finalizarReserva(){
		if(c.comprobarDiasMes(Integer.parseInt((String) comboDia.getSelectedItem()),c.mes((String)comboMes.getSelectedItem())) == false){
			JOptionPane.showMessageDialog(null, "Por favor, introduzca una fecha correcta.");
				
		}
		else if(c.comprobarComboHoras(Integer.parseInt((String) comboHoraInicio.getSelectedItem()), 
				Integer.parseInt((String) comboHoraFinal.getSelectedItem())) == false){
			JOptionPane.showMessageDialog(this, "Las horas introducidas no son válidas, recuerde que solo puede reservar un máximo de dos horas.");
			
		}
		else if(c.reservaAnterior(Integer.parseInt((String) comboDia.getSelectedItem()), c.mes((String) comboMes.getSelectedItem())
				, Integer.parseInt((String) comboAno.getSelectedItem()), Integer.parseInt((String) comboHoraInicio.getSelectedItem())) == false){
			JOptionPane.showMessageDialog(this, "La fecha que ha introducido, se corresponde con un fecha atrasada.");
			
		}
		else if(c.reservarSieteDias(Integer.parseInt((String) comboAno.getSelectedItem()), c.mes((String) comboMes.getSelectedItem())
				, Integer.parseInt((String) comboDia.getSelectedItem()), Integer.parseInt((String) comboHoraInicio.getSelectedItem())) == false){
			JOptionPane.showMessageDialog(this, "La fecha que ha seleccionado, supera los 7 días permitidos.");
			
		}
		else if(c.socioConReserva(poli, Integer.parseInt(textSocio.getText().toString()), Integer.parseInt((String) comboHoraInicio.getSelectedItem())
				, Integer.parseInt((String) comboHoraFinal.getSelectedItem()), c.mes((String) comboMes.getSelectedItem()),
				Integer.parseInt((String) comboDia.getSelectedItem()), Integer.parseInt((String) comboAno.getSelectedItem())) == false)
		{
			JOptionPane.showMessageDialog(this, "Usted ya tiene una reserva simultánea a esa hora.");
			
		}
		else if(c.reservado(poli, Integer.parseInt((String) comboDia.getSelectedItem()),c.mes((String)comboMes.getSelectedItem()), 
				Integer.parseInt((String) comboAno.getSelectedItem()), Integer.parseInt((String) comboHoraInicio.getSelectedItem())
				, Integer.parseInt((String) comboHoraFinal.getSelectedItem()), String.valueOf(textInstalacion.getText())
				, String.valueOf(comboPista.getSelectedItem())) == false){
			JOptionPane.showMessageDialog(this, "Ya hay un reserva en el horario especificado");
			
		}
		else if(c.NoReservaPosible(poli, obtenerReserva()) == false){	//Reserva individual anulada
			JOptionPane.showMessageDialog(null, "La instalación no se encuentra disponible en el horario especificado.");
		}
		else if(reservaNoDisponible() == false){	//Varias reservas anuladas.
			JOptionPane.showMessageDialog(null, "La instalación no se encuentra disponible en el horario especificado.");
		}
		else {
			try {
				boolean pagadoCuota =tipoPago();
				poli.getConexion().actualizarReserva(Integer.parseInt((String) comboDia.getSelectedItem()),
						 c.mes((String) comboMes.getSelectedItem()), Integer.parseInt((String) comboAno.getSelectedItem())
						 , Integer.parseInt((String) comboHoraInicio.getSelectedItem()), Integer.parseInt((String) comboHoraFinal.getSelectedItem())
						 , dni, textInstalacion.getText().toString(), 
						 String.valueOf(comboPista.getSelectedItem()), c,pagadoCuota);
				JOptionPane.showMessageDialog(null, "Su reserva se ha producido correctamente");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
           
		}
		
	}
	
	private boolean puedesReservar(String idInstalacion, Date fechaI, Date fechaF){
		//Obtenemos las pk de INSTALACION_NO_DISPONIBLE que tengan como idInstalacion
		//el pasado como parámetro para no procesar así todo el mapa.
		ArrayList<String> lista = null;
		try {
			lista = poli.getConexion().obtenerPks(idInstalacion);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//Inicializamos contador para saber si hay alguna en la que no se pueda reservar
		int cont = 0;
		//Recorremos el HashMap junto con la lista de pks
		for(Map.Entry<String, Date[]> entry : poli.getConexion().getMapa().entrySet()){
			for(String pk: lista){
				if(entry.getKey().equals(pk)){
					//Una vez que lo encontramos:
					Date date [] = entry.getValue();
					Date fechaInicialInstalacion = date[0];
					Date fechaFinalInstalacion = date[1];
					if(c.comprobarDisponibilidad(fechaInicialInstalacion, fechaFinalInstalacion, fechaI, fechaF) == false){
						cont++;
					}
				}
			}
		}
		if(cont == 0){
			return true;
		}
		return false;
	}
	
	@SuppressWarnings("deprecation")
	private boolean reservaNoDisponible(){
		Calendar ca = Calendar.getInstance();
		Date fechaI = new Date(Integer.parseInt((String) comboAno.getSelectedItem())-1900, c.mes((String) comboMes.getSelectedItem()),
				Integer.parseInt((String) comboDia.getSelectedItem()),Integer.parseInt((String) comboHoraInicio.getSelectedItem()),00,00);
		int tiempo = calcularTiempo(comboHoraInicio, comboHoraFinal);
		ca.setTime(fechaI);
		ca.add(Calendar.HOUR, tiempo);
		int año = ca.get(Calendar.YEAR);
		int mes = ca.get(Calendar.MONTH);
		int dia = ca.get(Calendar.DAY_OF_MONTH);
		int hora = ca.get(Calendar.HOUR_OF_DAY);
		Date fechaF = new Date(año-1900,mes,dia,hora,00,00);
		if(puedesReservar(buscarInstalacion(textInstalacion, comboPista).getIdPista(), fechaI, fechaF) == false){
			return false;
		}
		return true;
	}
	
	/**
	 * Obtiene la reserva momentanea.
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private Reserva obtenerReserva(){
		return new Reserva(getClienteByNumeroSocio(textSocio),
				buscarInstalacion(textInstalacion, comboPista), 
				new Date(Integer.parseInt((String) comboAno.getSelectedItem())-1900, c.mes((String) comboMes.getSelectedItem()),
						Integer.parseInt((String) comboDia.getSelectedItem()),Integer.parseInt((String) comboHoraInicio.getSelectedItem()),00,00),
						calcularTiempo(comboHoraInicio, comboHoraFinal), Reserva.LIBRE, null, null, "",false,"");
	}
	
	private Instalacion buscarInstalacion(JTextField tipo2, JComboBox pista2) {
		String tipo = tipo2.getText().toString();
		String pista = String.valueOf(pista2.getSelectedItem());
		for(Instalacion i:poli.getInstalaciones()) {
			if(i.getTipo().equals(tipo) && i.getnPista().equals(pista))
				return i;
		}
		return null;
	}
	
	private Cliente getClienteByNumeroSocio(JTextField nSocio) {
		int socio = Integer.parseInt(nSocio.getText().toString());
		for(Cliente c: poli.getClientes()) {
			if(c instanceof Socio){
				if(((Socio)c).getNumeroSocio()==socio){
					return c;
				}
			}
		}
		return null;
	}
	
	private int calcularTiempo(JComboBox horaInicialCombo, JComboBox horaFinalCombo){
		int horaInicial = Integer.parseInt((String) horaInicialCombo.getSelectedItem());
		int horaFinal = Integer.parseInt((String) horaFinalCombo.getSelectedItem());
		if(horaInicial == 22 && horaFinal == 0){
			return 2;
		}
		else if(horaInicial == 23 && horaFinal == 0){
			return 1;
		}
		else if(horaInicial == 23 && horaFinal == 1){
			return 2;
		}
		else{
			return Math.abs(horaFinal-horaInicial);
		}
	}
	
	
	/**
	 * Comprueba el tipo de pago seleccionado por el cliente.
	 * @return true si se pasa el pago a la cuota(por lo que se paga inmediatamente) 
	 * o false si se paga en efectivo (por lo que se paga cuando use la pista)
	 */
	private boolean tipoPago() {
		if(getRadioBotonCuota().isSelected())
			return true;
		else 
			return false;
	}
	
	private JLabel getLblNewLabel_14() {
		if (lblNewLabel_14 == null) {
			lblNewLabel_14 = new JLabel("Pista:");
			lblNewLabel_14.setFont(new Font("Dialog", Font.BOLD, 12));
			lblNewLabel_14.setBounds(255, 94, 40, 23);
		}
		return lblNewLabel_14;
	}
	@SuppressWarnings("unchecked")
	private JComboBox getComboPista() {
		if (comboPista == null) {
			comboPista = new JComboBox();
			comboPista.setBackground(Color.WHITE);
			comboPista.setForeground(Color.BLUE);
			comboPista.setFont(new Font("Dialog", Font.BOLD, 12));
			comboPista.setModel(new DefaultComboBoxModel(new String[] {"1", "2"}));
			comboPista.setBounds(305, 93, 50, 24);
		}
		return comboPista;
	}
}
