package igu;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JComboBox;

import java.awt.Font;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;

import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;


import javax.swing.JScrollPane;
import javax.swing.JTextField;

import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.awt.event.ActionEvent;

import logica.Cliente;
import logica.Comprobaciones;
import logica.Instalacion;
import logica.Polideportivo;
import logica.Reserva;
import logica.Socio;

import javax.swing.border.TitledBorder;


@SuppressWarnings({ "rawtypes", "serial" })
public class ReservaSociosVentana extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JComboBox comboDia;
	private JComboBox comboMes;
	private JComboBox comboHoraInicio;
	private JComboBox comboInstalacion;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JButton botonFiltrar;
	private JLabel lblNewLabel;
	private JLabel labelDia;
	private JLabel labelMes;
	private JLabel labelHoraInicio;
	private JPanel panelReservas;
	private JScrollPane scrollPane;
	private JTable table;
	private JLabel labelInstalacion;
	private JLabel labelSocio;
	private JTextField textSocio;
	private JLabel labelHoraFin;
	private JComboBox comboHoraFinal;
	
	private DefaultTableModel modelo;
	private Polideportivo poli;
	private RendererFiltradoReserva tabla;
	private Comprobaciones c;
	
	private GregorianCalendar calendar = new GregorianCalendar();
	private JLabel labelAno;
	private JComboBox comboAno;
	private JLabel labelPista;
	private JComboBox comboPista;
	private JPanel panel;
	private JRadioButton rdbtnAadirImporteA;
	private JRadioButton rdbtnEnEfectivo;

	/**
	 * Create the dialog.
	 * @param ventanaAdminPrincipalUltimate 
	 */
	public ReservaSociosVentana(VentanaAdminPrincipalUltimate ventanaAdminPrincipalUltimate) {
		setIconImage(Toolkit.getDefaultToolkit().getImage(ReservaInstalacionAdmVentana.class.getResource("/img/icon.jpg")));
		setTitle("Polideportivo: Reserva Socios");
		setModal(true);
		setResizable(false);
		c = new Comprobaciones();
		poli = ventanaAdminPrincipalUltimate.getPolideportivo();
		setBounds(100, 100, 947, 540);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);

		contentPanel.setLayout(null);
		contentPanel.add(getComboDia());
		contentPanel.add(getComboMes());
		contentPanel.add(getComboHoraInicio());
		contentPanel.add(getComboInstalacion());
		contentPanel.add(getBotonFiltrar());
		contentPanel.add(getLblNewLabel());
		contentPanel.add(getLabelDia());
		contentPanel.add(getLabelMes());
		contentPanel.add(getLabelHoraInicio());
		
		table = new JTable();
		tabla = new RendererFiltradoReserva();
		table.setDefaultRenderer(Object.class, tabla);
		
		modelo =new DefaultTableModel();
		table.setModel(modelo);
		
		contentPanel.add(getPanelReservas());
		
		
		contentPanel.add(getLabelInstalacion());
		contentPanel.add(getLabelSocio());
		contentPanel.add(getTextSocio());
		contentPanel.add(getLabelHoraFin());
		contentPanel.add(getComboHoraFinal());
		contentPanel.add(getLabelAno());
		contentPanel.add(getComboAno());
		contentPanel.add(getLabelPista());
		contentPanel.add(getComboPista());
		contentPanel.add(getPanel());
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				getReservar(buttonPane);
			}
			{
				getCalcelar(buttonPane);
			}
		}
		
	}

	private void getCalcelar(JPanel buttonPane) {
		JButton botonCancelar = new JButton("Cancelar");
		botonCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		botonCancelar.setFont(new Font("Dialog", Font.BOLD, 12));
		botonCancelar.setActionCommand("Cancel");
		buttonPane.add(botonCancelar);
	}

	private void getReservar(JPanel buttonPane) {
		JButton botonReservar = new JButton("Reservar");
		botonReservar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//Comprobamos que la fecha es correcta
				ejecutarReserva();
				
			}
		});
		botonReservar.setFont(new Font("Dialog", Font.BOLD, 12));
		botonReservar.setActionCommand("OK");
		buttonPane.add(botonReservar);
		getRootPane().setDefaultButton(botonReservar);
	}
	
	@SuppressWarnings("unchecked")
	private JComboBox getComboDia() {
		if (comboDia == null) {
			comboDia = new JComboBox();
			int dia = calendar.get(Calendar.DATE)-1;
			comboDia.setModel(new DefaultComboBoxModel(new String[] {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"}));
			comboDia.setSelectedIndex(dia);						
			comboDia.setFont(new Font("Dialog", Font.BOLD, 12));
			comboDia.setBounds(55, 55, 60, 25);
		}
		return comboDia;
	}
	
	@SuppressWarnings("unchecked")
	private JComboBox getComboMes() {
		if (comboMes == null) {
			comboMes = new JComboBox();
			int mes = calendar.get(Calendar.MONTH);
			comboMes.setModel(new DefaultComboBoxModel(new String[] {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"}));
			comboMes.setSelectedIndex(mes);
			comboMes.setFont(new Font("Dialog", Font.BOLD, 12));
			comboMes.setBounds(55, 91, 124, 25);
		}
		return comboMes;
	}
	
	@SuppressWarnings("unchecked")
	private JComboBox getComboAno() {
		if (comboAno == null) {
			comboAno = new JComboBox();
			int ano = calendar.get(Calendar.YEAR);
			comboAno.setModel(new DefaultComboBoxModel(new String[] {"2017", "2018", "2019", "2020", "2021", "2022", "2023", "2024", "2025", "2026", "2027", "2028", "2029", "2030", "2031", "2032", "2033", "2034", "2035", "2036", "2037", "2038", "2039", "2040"}));
			comboAno.setSelectedItem(ano);
			comboAno.setFont(new Font("Dialog", Font.BOLD, 12));
			comboAno.setBounds(55, 127, 60, 25);
		}
		return comboAno;
	}
	
	@SuppressWarnings("unchecked")
	private JComboBox getComboHoraInicio() {
		if (comboHoraInicio == null) {
			comboHoraInicio = new JComboBox();
			comboHoraInicio.setModel(new DefaultComboBoxModel(new String[] {"00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23"}));
			comboHoraInicio.setFont(new Font("Dialog", Font.BOLD, 12));
			comboHoraInicio.setBounds(215, 55, 82, 24);
		}
		return comboHoraInicio;
	}
	@SuppressWarnings("unchecked")
	private JComboBox getComboInstalacion() {
		if (comboInstalacion == null) {
			comboInstalacion = new JComboBox();
			DefaultComboBoxModel model = new DefaultComboBoxModel();
			llenarComboInstalaciones(model);
			comboInstalacion.setModel(model);
			comboInstalacion.setFont(new Font("Dialog", Font.BOLD, 12));
			comboInstalacion.setBounds(215, 127, 145, 24);
		}
		return comboInstalacion;
	}
	
	@SuppressWarnings("unchecked")
	private DefaultComboBoxModel llenarComboInstalaciones(DefaultComboBoxModel model){
		ArrayList<Instalacion> noRep=new ArrayList<Instalacion>();
		boolean flag;
		for(Instalacion inst:poli.getInstalaciones()) {
			flag=false;
			for(Instalacion i: noRep ) {
				if(inst.getTipo().equals(i.getTipo())){
					flag = true;
				}
			}
			if(!flag){
				noRep.add(inst);
				model.addElement(inst.getTipo());
			}	
		}
			
		return model;
	}
	
	private JButton getBotonFiltrar() {
		if (botonFiltrar == null) {
			botonFiltrar = new JButton("Filtrar");
			botonFiltrar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					tabla.limpiar(table);
					añadirReserva();
				}
			});
			botonFiltrar.setFont(new Font("Dialog", Font.BOLD, 12));
			botonFiltrar.setBounds(844, 128, 87, 22);
		}
		return botonFiltrar;
	}
	private JLabel getLblNewLabel() {
		if (lblNewLabel == null) {
			lblNewLabel = new JLabel("Reservas de las pistas para socios.");
			lblNewLabel.setFont(new Font("Dialog", Font.BOLD, 14));
			lblNewLabel.setBounds(10, 11, 294, 25);
		}
		return lblNewLabel;
	}
	private JLabel getLabelDia() {
		if (labelDia == null) {
			labelDia = new JLabel("D\u00EDa:");
			labelDia.setFont(new Font("Dialog", Font.BOLD, 12));
			labelDia.setBounds(10, 56, 35, 22);
		}
		return labelDia;
	}
	private JLabel getLabelMes() {
		if (labelMes == null) {
			labelMes = new JLabel("Mes:");
			labelMes.setFont(new Font("Dialog", Font.BOLD, 12));
			labelMes.setBounds(10, 91, 46, 25);
		}
		return labelMes;
	}
	private JLabel getLabelHoraInicio() {
		if (labelHoraInicio == null) {
			labelHoraInicio = new JLabel("Hora inicio:");
			labelHoraInicio.setFont(new Font("Dialog", Font.BOLD, 12));
			labelHoraInicio.setBounds(136, 55, 69, 25);
		}
		return labelHoraInicio;
	}
	private JPanel getPanelReservas() {
		if (panelReservas == null) {
			panelReservas = new JPanel();
			panelReservas.setBounds(10, 171, 921, 284);
			panelReservas.setLayout(null);
			panelReservas.add(getScrollPane());
		}
		return panelReservas;
	}
	private JScrollPane getScrollPane() {
		if (scrollPane == null) {
			scrollPane = new JScrollPane();
			scrollPane.setBounds(0, 0, 921, 284);
			scrollPane.setViewportView(getTable());
		}
		return scrollPane;
	}
	private JTable getTable() {
		//if (table == null) {
			table.setModel(new DefaultTableModel(
				new Object[][] {
				},
				new String[] {
					"Fecha", "Nº Socio", "Nombre", "DNI","Instalaci\u00F3n", "Pista","Horario", "Tiempo"
				}
			)
			{
			

				public boolean isCellEditable(int row, int column){
					return false;
				}
			});
		//}
		return table;
	}
	private JLabel getLabelInstalacion() {
		if (labelInstalacion == null) {
			labelInstalacion = new JLabel("Instalaci\u00F3n:");
			labelInstalacion.setFont(new Font("Dialog", Font.BOLD, 12));
			labelInstalacion.setBounds(136, 127, 82, 25);
		}
		return labelInstalacion;
	}
	private JLabel getLabelSocio() {
		if (labelSocio == null) {
			labelSocio = new JLabel("N\u00BA socio:");
			labelSocio.setFont(new Font("Dialog", Font.BOLD, 12));
			labelSocio.setBounds(201, 91, 60, 24);
		}
		return labelSocio;
	}
	private JTextField getTextSocio() {
		if (textSocio == null) {
			textSocio = new JTextField();
			textSocio.setFont(new Font("Dialog", Font.BOLD, 12));
			textSocio.setBounds(254, 94, 86, 20);
			textSocio.setColumns(10);
		}
		return textSocio;
	}
	private JLabel getLabelHoraFin() {
		if (labelHoraFin == null) {
			labelHoraFin = new JLabel("Hora final:");
			labelHoraFin.setFont(new Font("Dialog", Font.BOLD, 12));
			labelHoraFin.setBounds(307, 55, 69, 22);
		}
		return labelHoraFin;
	}
	@SuppressWarnings("unchecked")
	private JComboBox getComboHoraFinal() {
		if (comboHoraFinal == null) {
			comboHoraFinal = new JComboBox();
			comboHoraFinal.setModel(new DefaultComboBoxModel(new String[] {"00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23"}));
			comboHoraFinal.setFont(new Font("Dialog", Font.BOLD, 12));
			comboHoraFinal.setBounds(375, 55, 82, 24);
		}
		return comboHoraFinal;
	}
	
	private JLabel getLabelAno() {
		if (labelAno == null) {
			labelAno = new JLabel("A\u00F1o:");
			labelAno.setFont(new Font("Dialog", Font.BOLD, 12));
			labelAno.setBounds(10, 127, 35, 25);
		}
		return labelAno;
	}
	private JLabel getLabelPista() {
		if (labelPista == null) {
			labelPista = new JLabel("Pista:");
			labelPista.setFont(new Font("Dialog", Font.BOLD, 12));
			labelPista.setBounds(370, 127, 35, 25);
		}
		return labelPista;
	}
	@SuppressWarnings("unchecked")
	private JComboBox getComboPista() {
		if (comboPista == null) {
			comboPista = new JComboBox();
			comboPista.setModel(new DefaultComboBoxModel(new String[] {"1", "2"}));
			comboPista.setFont(new Font("Dialog", Font.BOLD, 12));
			comboPista.setBounds(415, 127, 46, 25);
		}
		return comboPista;
	}
	
	
	
	
	
	
	
	
	
	/**
	 * Método para comprobar que los campos introducidos son válidos y se puede hacer la reserva
	 * @return
	 */
	private void ejecutarReserva(){
		int dia= Integer.parseInt((String) comboDia.getSelectedItem());
		int mes= c.mes((String)comboMes.getSelectedItem());
		int horaInicio= Integer.parseInt((String) comboHoraInicio.getSelectedItem());
		int horaFinal= Integer.parseInt((String) comboHoraFinal.getSelectedItem());
		int año= Integer.parseInt((String) comboAno.getSelectedItem());
		String instalacion= String.valueOf(comboInstalacion.getSelectedItem());
		String pista= String.valueOf(comboPista.getSelectedItem());
		
		if(!c.comprobarCamposTextoVacios(textSocio.getText().toString())){
			JOptionPane.showMessageDialog(this, "El campo de texto 'socio' se encuentra vacío.");
		}
		else if(!c.comprobarDiasMes(dia,mes)){
			JOptionPane.showMessageDialog(this, "Por favor, introduzca una fecha correcta.");
		}
		else if(!c.comprobarComboHoras(horaInicio,horaFinal)){
			JOptionPane.showMessageDialog(this, "Las horas introducidas no son válidas");
		}
		else if(!c.reservaAnterior(dia, mes ,año ,horaInicio)){
			JOptionPane.showMessageDialog(this, "La fecha que ha introducido, se corresponde con un fecha atrasada.");
		}
		else if(!c.reservarSieteDias(año, mes, dia, horaInicio)){
			JOptionPane.showMessageDialog(this, "La fecha que ha seleccionado, supera los 7 días permitidos.");
		}
		else if(campoSocio() == false){
		}
		else if(!c.reservado(poli, dia, mes, año, horaInicio, horaFinal, instalacion, pista)){
			JOptionPane.showMessageDialog(this, "Ya hay un reserva en el horario especificado");
		}
		else if(!c.NoReservaPosible(poli, obtenerReserva())){
			JOptionPane.showMessageDialog(this, "La instalación no se encuentra disponible en el horario especificado.");
		}
		else if(!reservaNoDisponible()){	//Varias reservas anuladas.
			JOptionPane.showMessageDialog(this, "La instalación no se encuentra disponible en el horario especificado.");
		}
		else{
			try {
				poli.getConexion().actualizarReserva(dia,mes, año, horaInicio, horaFinal, obtenerDNI(textSocio), instalacion, pista, c, false);
				JOptionPane.showMessageDialog(this, "Su reserva se ha producido correctamente");
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
		if(puedesReservar(buscarInstalacion(comboInstalacion, comboPista).getIdPista(), fechaI, fechaF) == false){
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
				buscarInstalacion(comboInstalacion, comboPista), 
				new Date(Integer.parseInt((String) comboAno.getSelectedItem())-1900, c.mes((String) comboMes.getSelectedItem()),
						Integer.parseInt((String) comboDia.getSelectedItem()),Integer.parseInt((String) comboHoraInicio.getSelectedItem()),00,00),
						calcularTiempo(comboHoraInicio, comboHoraFinal), Reserva.LIBRE, null, null, "",false,"");
	}
	
	private Instalacion buscarInstalacion(JComboBox tipo2, JComboBox pista2) {
		String tipo = String.valueOf(tipo2.getSelectedItem());
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
	 * Método que comprueba si el campo socio está en blanco o es incorrecto
	 */
	private boolean campoSocio(){
		boolean numeros = true;
		for(char c : textSocio.getText().toCharArray()){
			if(!Character.isDigit(c)){
				numeros = false;
				break;}
		}
		if(numeros == false){
			JOptionPane.showMessageDialog(this, "Por favor, introduzca un número de socio válido.");
			return false;
		}
		else if(socioRegistrado() == false){
			JOptionPane.showMessageDialog(this, "El número de socio introducido no se corresponde con ningún socio de nuestro polideportivo.");
			return false;
		}
		else if(c.socioConReserva(poli, Integer.parseInt(textSocio.getText().toString()), Integer.parseInt((String) comboHoraInicio.getSelectedItem())
				, Integer.parseInt((String) comboHoraFinal.getSelectedItem()), c.mes((String) comboMes.getSelectedItem()),
				Integer.parseInt((String) comboDia.getSelectedItem()), Integer.parseInt((String) comboAno.getSelectedItem())) == false){
			JOptionPane.showMessageDialog(this, "Este socio ya tiene una reserva simultánea a esa hora.");
			return false;
		}
		else
			return true;
		
	}
	
	/**
	 * Método para comprobar que el numero introducido es socio.
	 * @return
	 */
	private boolean socioRegistrado(){
		int numeroSocioTexto = Integer.parseInt(textSocio.getText().toString());
		int numeroSocio;
		for(int i=0; i<poli.getClientes().size(); i++)
		{
			if(poli.getClientes().get(i)instanceof Socio)
			{
				numeroSocio = (((Socio) poli.getClientes().get(i)).getNumeroSocio());
				if(numeroSocioTexto == numeroSocio){
					return true;
				}
			}
		}
		return false;
	}
	
	/**
     * Método para obtener el dni
     */
    private String obtenerDNI(JTextField textSocio){
        int socio = Integer.parseInt(textSocio.getText());
        for(int i=0; i<poli.getClientes().size(); i++){
            if(poli.getClientes().get(i) instanceof Socio)
            {
                if( ((Socio)poli.getClientes().get(i)).getNumeroSocio() == socio){
                    return poli.getClientes().get(i).getDNI();
                }
            }
        }
        return "";
    }
	private JPanel getPanel() {
		if (panel == null) {
			panel = new JPanel();
			panel.setBorder(new TitledBorder(null, "Forma de pago: ", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			panel.setBounds(493, 61, 179, 80);
			panel.setLayout(null);
			panel.add(getRdbtnAadirImporteA());
			panel.add(getRdbtnEnEfectivo());
		}
		return panel;
	}
	private JRadioButton getRdbtnAadirImporteA() {
		if (rdbtnAadirImporteA == null) {
			rdbtnAadirImporteA = new JRadioButton("A\u00F1adir importe a la cuota");
			rdbtnAadirImporteA.setFont(new Font("Dialog", Font.BOLD, 12));
			rdbtnAadirImporteA.setSelected(true);
			buttonGroup.add(rdbtnAadirImporteA);
			rdbtnAadirImporteA.setBounds(6, 20, 167, 23);
		}
		return rdbtnAadirImporteA;
	}
	private JRadioButton getRdbtnEnEfectivo() {
		if (rdbtnEnEfectivo == null) {
			rdbtnEnEfectivo = new JRadioButton("En efectivo");
			rdbtnEnEfectivo.setFont(new Font("Dialog", Font.BOLD, 12));
			buttonGroup.add(rdbtnEnEfectivo);
			rdbtnEnEfectivo.setBounds(6, 50, 144, 23);
		}
		return rdbtnEnEfectivo;
	}
	
	/**
	 * Añade reserva a la tabla
	 */
	public void añadirReserva(){
		Collections.sort(poli.getReservas(), new Comparator<Reserva>() {

			@Override
			public int compare(Reserva arg0, Reserva arg1) {
				return arg0.getFechaReserva().compareTo(arg1.getFechaReserva());
			}
		    
		});
		Object [] array = new Object[8];
		Calendar cal = Calendar.getInstance();
		int dia,mes, año, hora;
		int valorDiaCombo = Integer.parseInt((String) comboDia.getSelectedItem());
		int valorMesCombo = c.mes((String)comboMes.getSelectedItem());
		int valorAñoCombo = Integer.parseInt((String) comboAno.getSelectedItem());
		String tipo = String.valueOf(comboInstalacion.getSelectedItem());
		String nPista = String.valueOf(comboPista.getSelectedItem());
		for(int i = 0; i<poli.getReservas().size(); i++)
		{
			cal.setTime(poli.getReservas().get(i).getFechaReserva());
			dia = cal.get(Calendar.DAY_OF_MONTH);
			mes = cal.get(Calendar.MONTH);
			año = cal.get(Calendar.YEAR);
			hora = cal.get(Calendar.HOUR_OF_DAY);
			int t = poli.getReservas().get(i).getTiempoReserva();
			if(dia == valorDiaCombo && mes == valorMesCombo && año == valorAñoCombo
					&& poli.getReservas().get(i).getInstalacion().getTipo().equals(tipo)
					&& poli.getReservas().get(i).getInstalacion().getnPista().equals(nPista))
			{
				if(!poli.getReservas().get(i).isReservaCentro()){
					if(!poli.getReservas().get(i).getEstado().equals(Reserva.ANULADA)){
						array[0] = (dia+"/"+(mes+1)+"/"+año);
						if(poli.getReservas().get(i).getCliente() instanceof Socio){
							array[1] = ((Socio)poli.getReservas().get(i).getCliente()).getNumeroSocio();
							array[2] = poli.getReservas().get(i).getCliente().getNombre();
						}
						else{
							array[1] = "-";
							array[2] = poli.getReservas().get(i).getCliente().getNombre();
						}
						array[3] = poli.getReservas().get(i).getCliente().getDNI();
						array[4] = poli.getReservas().get(i).getInstalacion().getTipo();
						array[5] = poli.getReservas().get(i).getInstalacion().getnPista();
						if(hora == 22 && t == 2){
							array[6] = hora + ":00 - "+"00:00";
						}
						else if(hora == 23 && t == 1){
							array[6] = hora + ":00 - "+"00:00";
						}
						else if(hora == 23 && t==2){
							array[6] = hora + ":00 - "+"01:00";
						}
						
						else
						{
							array[6] = hora + ":00 - "+(hora+t)+":00";
						}		
						array[7] = t + " horas";
						((DefaultTableModel) table.getModel()).addRow(array);
					}
				}
				else
				{
					if(!poli.getReservas().get(i).getEstado().equals(Reserva.ANULADA)){
						array[0] = (dia+"/"+(mes+1)+"/"+año);
						array[1] = "Reserva Centro";
						array[2] = "Centro";
						array[3] = "-";
						array[4] = poli.getReservas().get(i).getInstalacion().getTipo();
						array[5] = poli.getReservas().get(i).getInstalacion().getnPista();
						array[6] = hora+":00";
						array[7] = t + " horas";
						((DefaultTableModel) table.getModel()).addRow(array);
					}
				}
			}
			
		}
	}
}
