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
import logica.NoSocio;
import logica.Polideportivo;
import logica.Reserva;
import logica.Socio;

import javax.swing.border.TitledBorder;

@SuppressWarnings({ "rawtypes", "serial" })
public class ReservaNoSociosVentana extends JDialog {

	private final JPanel contentPanel = new JPanel();
	
	private JComboBox comboDia;
	private JComboBox comboMes;
	private JComboBox comboHoraInicio;
	private JComboBox comboInstalacion;
	@SuppressWarnings("unused")
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
	private JLabel labelHoraFin;
	private JComboBox comboHoraFinal;
	
	private DefaultTableModel modelo;
	private Polideportivo poli;
	private RendererFiltradoReserva tabla;
	private Comprobaciones c;
	
	private GregorianCalendar calendar = new GregorianCalendar();
	private JLabel labelAno;
	private JComboBox comboAno;
	private JPanel panelReserva;
	private JPanel panel;
	private JLabel labelNombre;
	private JTextField textNombre;
	private JLabel labelApellidos;
	private JTextField textApellidos;
	private JLabel labelDNI;
	private JTextField textDNI;
	private JLabel labelTelefono;
	private JTextField textTelefono;
	private JLabel labelPista;
	private JComboBox comboPista;

	/**
	 * Create the dialog.
	 * @param ventanaAdminPrincipalUltimate 
	 */
	public ReservaNoSociosVentana(VentanaAdminPrincipalUltimate ventanaAdminPrincipalUltimate) {
		setIconImage(Toolkit.getDefaultToolkit().getImage(ReservaInstalacionAdmVentana.class.getResource("/img/icon.jpg")));
		setResizable(false);
		setModal(true);
		c = new Comprobaciones();
		poli = ventanaAdminPrincipalUltimate.getPolideportivo();
		setBounds(100, 100, 903, 619);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		setTitle("Polideportivo: Reserva No Socios");
		contentPanel.setLayout(null);
		contentPanel.add(getLblNewLabel());
		
		table = new JTable();
		tabla = new RendererFiltradoReserva();
		table.setDefaultRenderer(Object.class, tabla);
		
		modelo =new DefaultTableModel();
		table.setModel(modelo);
		
		contentPanel.add(getPanelReservas());
		contentPanel.add(getPanelReserva());
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
				if(!ejecutarReserva())
				{
					
				}
				else
				{
					if(comprobar() == false) {
						JOptionPane.showMessageDialog(null, "Los datos introducidos no se corresponden con el dni especificado");
					}
					else {
						try {
							if(!existeNoSocio(textDNI.getText()))
								guardarClienteNoSocio();
							poli.getConexion().actualizarReserva(Integer.parseInt((String) comboDia.getSelectedItem()),
									 c.mes((String) comboMes.getSelectedItem()), Integer.parseInt((String) comboAno.getSelectedItem())
									 , Integer.parseInt((String) comboHoraInicio.getSelectedItem()), Integer.parseInt((String) comboHoraFinal.getSelectedItem())
									 , textDNI.getText().toString(), String.valueOf(comboInstalacion.getSelectedItem()), 
									 String.valueOf(comboPista.getSelectedItem()), c,true);
							JOptionPane.showMessageDialog(null, "La reserva se ha producido correctamente y se ha generado un recibo");
						
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
					
				}
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
			comboDia.setBounds(55, 22, 60, 25);
			int dia = calendar.get(Calendar.DATE)-1;
			comboDia.setModel(new DefaultComboBoxModel(new String[] {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"}));
			comboDia.setSelectedIndex(dia);						
			comboDia.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return comboDia;
	}
	
	@SuppressWarnings("unchecked")
	private JComboBox getComboMes() {
		if (comboMes == null) {
			comboMes = new JComboBox();
			comboMes.setBounds(55, 56, 124, 25);
			int mes = calendar.get(Calendar.MONTH);
			comboMes.setModel(new DefaultComboBoxModel(new String[] {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"}));
			comboMes.setSelectedIndex(mes);
			comboMes.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return comboMes;
	}
	
	@SuppressWarnings("unchecked")
	private JComboBox getComboAno() {
		if (comboAno == null) {
			comboAno = new JComboBox();
			comboAno.setBounds(55, 93, 60, 25);
			int ano = calendar.get(Calendar.YEAR);
			comboAno.setModel(new DefaultComboBoxModel(new String[] {"2017", "2018", "2019", "2020", "2021", "2022", "2023", "2024", "2025", "2026", "2027", "2028", "2029", "2030", "2031", "2032", "2033", "2034", "2035", "2036", "2037", "2038", "2039", "2040"}));
			comboAno.setSelectedItem(ano);
			comboAno.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return comboAno;
	}
	
	@SuppressWarnings("unchecked")
	private JComboBox getComboHoraInicio() {
		if (comboHoraInicio == null) {
			comboHoraInicio = new JComboBox();
			comboHoraInicio.setBounds(288, 56, 82, 24);
			comboHoraInicio.setModel(new DefaultComboBoxModel(new String[] {"00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23"}));
			comboHoraInicio.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return comboHoraInicio;
	}
	@SuppressWarnings("unchecked")
	private JComboBox getComboInstalacion() {
		if (comboInstalacion == null) {
			comboInstalacion = new JComboBox();
			comboInstalacion.setBounds(487, 92, 124, 24);
			DefaultComboBoxModel modeloInst= new DefaultComboBoxModel();
			llenarComboInstalaciones(modeloInst);
			comboInstalacion.setModel(modeloInst);
			comboInstalacion.setFont(new Font("Dialog", Font.BOLD, 12));
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
			botonFiltrar.setBounds(780, 95, 87, 22);
			botonFiltrar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					tabla.limpiar(table);
					añadirReserva();
				}
			});
			botonFiltrar.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return botonFiltrar;
	}
	private JLabel getLblNewLabel() {
		if (lblNewLabel == null) {
			lblNewLabel = new JLabel("Reservas de las pistas para no socios.");
			lblNewLabel.setFont(new Font("Dialog", Font.BOLD, 14));
			lblNewLabel.setBounds(10, 11, 294, 25);
		}
		return lblNewLabel;
	}
	private JLabel getLabelDia() {
		if (labelDia == null) {
			labelDia = new JLabel("D\u00EDa:");
			labelDia.setBounds(10, 23, 35, 22);
			labelDia.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return labelDia;
	}
	private JLabel getLabelMes() {
		if (labelMes == null) {
			labelMes = new JLabel("Mes:");
			labelMes.setBounds(10, 56, 46, 25);
			labelMes.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return labelMes;
	}
	private JLabel getLabelHoraInicio() {
		if (labelHoraInicio == null) {
			labelHoraInicio = new JLabel("Hora inicio:");
			labelHoraInicio.setBounds(214, 23, 69, 25);
			labelHoraInicio.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return labelHoraInicio;
	}
	private JPanel getPanelReservas() {
		if (panelReservas == null) {
			panelReservas = new JPanel();
			panelReservas.setBounds(10, 297, 877, 233);
			panelReservas.setLayout(null);
			panelReservas.add(getScrollPane());
		}
		return panelReservas;
	}
	private JScrollPane getScrollPane() {
		if (scrollPane == null) {
			scrollPane = new JScrollPane();
			scrollPane.setBounds(0, 0, 877, 233);
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
					"Fecha", "N\u00BA Socio","Nombre", "DNI","Instalaci\u00F3n", "Pista","Horario", "Tiempo"
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
			labelInstalacion.setBounds(408, 92, 82, 25);
			labelInstalacion.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return labelInstalacion;
	}
	private JLabel getLabelHoraFin() {
		if (labelHoraFin == null) {
			labelHoraFin = new JLabel("Hora final:");
			labelHoraFin.setBounds(209, 94, 69, 22);
			labelHoraFin.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return labelHoraFin;
	}
	@SuppressWarnings("unchecked")
	private JComboBox getComboHoraFinal() {
		if (comboHoraFinal == null) {
			comboHoraFinal = new JComboBox();
			comboHoraFinal.setBounds(288, 93, 82, 24);
			comboHoraFinal.setModel(new DefaultComboBoxModel(new String[] {"00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23"}));
			comboHoraFinal.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return comboHoraFinal;
	}

	private JLabel getLabelAno() {
		if (labelAno == null) {
			labelAno = new JLabel("A\u00F1o:");
			labelAno.setBounds(10, 93, 35, 25);
			labelAno.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return labelAno;
	}
	private JPanel getPanelReserva() {
		if (panelReserva == null) {
			panelReserva = new JPanel();
			panelReserva.setBorder(new TitledBorder(null, "Reserva", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			panelReserva.setName("Reserva");
			panelReserva.setBounds(10, 157, 877, 129);
			panelReserva.setLayout(null);
			panelReserva.add(getLabelDia());
			panelReserva.add(getComboDia());
			panelReserva.add(getLabelHoraInicio());
			panelReserva.add(getComboHoraInicio());
			panelReserva.add(getLabelHoraFin());
			panelReserva.add(getComboHoraFinal());
			panelReserva.add(getLabelMes());
			panelReserva.add(getComboMes());
			panelReserva.add(getLabelInstalacion());
			panelReserva.add(getComboInstalacion());
			panelReserva.add(getLabelAno());
			panelReserva.add(getComboAno());
			panelReserva.add(getBotonFiltrar());
			panelReserva.add(getLabelPista());
			panelReserva.add(getComboPista());
		}
		return panelReserva;
	}
	private JPanel getPanel() {
		if (panel == null) {
			panel = new JPanel();
			panel.setBorder(new TitledBorder(null, "Datos del usuario", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			panel.setBounds(10, 47, 877, 99);
			panel.setLayout(null);
			panel.add(getLabelNombre());
			panel.add(getTextNombre());
			panel.add(getLabelApellidos());
			panel.add(getTextApellidos());
			panel.add(getLabelDNI());
			panel.add(getTextDNI());
			panel.add(getLabelTelefono());
			panel.add(getTextTelefono());
			
			JButton botonBuscarNoSocio = new JButton("Buscar");
			botonBuscarNoSocio.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					mostrarDatos();
				}
			});
			botonBuscarNoSocio.setFont(new Font("Dialog", Font.BOLD, 12));
			botonBuscarNoSocio.setBounds(182, 21, 89, 23);
			panel.add(botonBuscarNoSocio);
		}
		return panel;
	}
	
	private void mostrarDatos() {
		Cliente c = poli.getClienteByDni(getTextDNI().getText().toString());
		if(c!= null) {
			getTextNombre().setText(c.getNombre());
			getTextApellidos().setText(c.getApellidos());
			getTextTelefono().setText(c.getTelefono());
		}
		else {
			JOptionPane.showMessageDialog(this, "El cliente no está guardado en la base de datos");
		}
	}
	
	private boolean comprobar(){
		String dni = getTextDNI().getText().toString();
		boolean existe= (poli.getClienteByDni(dni)==null?false:true);
		String nombre = getTextNombre().getText().toString();
		String apellidos = getTextApellidos().getText().toString();
		String telefono = getTextTelefono().getText().toString();
		for(Cliente c: poli.getClientes()) {
			if(c.getDNI().equals(dni) && c.getApellidos().equals(apellidos) && c.getNombre().equals(nombre) && c.getTelefono().equals(telefono)) {
				return true;				
			}
		}
		return false || !existe;
	}
	
	private JLabel getLabelNombre() {
		if (labelNombre == null) {
			labelNombre = new JLabel("Nombre:");
			labelNombre.setFont(new Font("Dialog", Font.BOLD, 12));
			labelNombre.setBounds(11, 54, 61, 22);
		}
		return labelNombre;
	}
	private JTextField getTextNombre() {
		if (textNombre == null) {
			textNombre = new JTextField();
			textNombre.setFont(new Font("Dialog", Font.BOLD, 12));
			textNombre.setBounds(64, 55, 155, 20);
			textNombre.setColumns(10);
		}
		return textNombre;
	}
	private JLabel getLabelApellidos() {
		if (labelApellidos == null) {
			labelApellidos = new JLabel("Apellidos:");
			labelApellidos.setFont(new Font("Dialog", Font.BOLD, 12));
			labelApellidos.setBounds(229, 55, 61, 20);
		}
		return labelApellidos;
	}
	private JTextField getTextApellidos() {
		if (textApellidos == null) {
			textApellidos = new JTextField();
			textApellidos.setFont(new Font("Dialog", Font.BOLD, 12));
			textApellidos.setBounds(300, 55, 186, 20);
			textApellidos.setColumns(10);
		}
		return textApellidos;
	}
	private JLabel getLabelDNI() {
		if (labelDNI == null) {
			labelDNI = new JLabel("DNI:");
			labelDNI.setFont(new Font("Dialog", Font.BOLD, 12));
			labelDNI.setBounds(11, 23, 36, 22);
		}
		return labelDNI;
	}
	private JTextField getTextDNI() {
		if (textDNI == null) {
			textDNI = new JTextField();
			textDNI.setFont(new Font("Dialog", Font.BOLD, 12));
			textDNI.setBounds(64, 24, 98, 20);
			textDNI.setColumns(10);
		}
		return textDNI;
	}
	private JLabel getLabelTelefono() {
		if (labelTelefono == null) {
			labelTelefono = new JLabel("Tel\u00E9fono:");
			labelTelefono.setFont(new Font("Dialog", Font.BOLD, 12));
			labelTelefono.setBounds(510, 54, 61, 22);
		}
		return labelTelefono;
	}
	private JTextField getTextTelefono() {
		if (textTelefono == null) {
			textTelefono = new JTextField();
			textTelefono.setFont(new Font("Dialog", Font.BOLD, 12));
			textTelefono.setBounds(581, 55, 98, 20);
			textTelefono.setColumns(10);
		}
		return textTelefono;
	}

	private JLabel getLabelPista() {
		if (labelPista == null) {
			labelPista = new JLabel("Pista:");
			labelPista.setFont(new Font("Dialog", Font.BOLD, 12));
			labelPista.setBounds(623, 92, 35, 26);
		}
		return labelPista;
	}
	@SuppressWarnings("unchecked")
	private JComboBox getComboPista() {
		if (comboPista == null) {
			comboPista = new JComboBox();
			comboPista.setModel(new DefaultComboBoxModel(new String[] {"1", "2"}));
			comboPista.setFont(new Font("Dialog", Font.BOLD, 12));
			comboPista.setBounds(668, 92, 46, 26);
		}
		return comboPista;
	}
	
	
	
	
	
	
	
	
	/**
	 * Método para comprobar que los campos introducidos son válidos y se puede hacer la reserva
	 * @return
	 */
	private boolean ejecutarReserva(){
		if(c.comprobarCamposTextoVacios(textApellidos.getText().toString()) == false || c.comprobarCamposTextoVacios(textDNI.getText().toString()) == false ||
			c.comprobarCamposTextoVacios(textNombre.getText().toString()) == false || c.comprobarCamposTextoVacios(textTelefono.getText().toString()) == false)		
		{
			JOptionPane.showMessageDialog(this, "Campos de texto vacíos");
			return false;
		}
		else if(c.comprobarTexto(textNombre.getText().toString()) == false || c.comprobarTexto(textApellidos.getText().toString()) == false){
			JOptionPane.showMessageDialog(this, "Campos de texto erróneos");
			return false;
		}
		else if(c.DNI(textDNI.getText().toString()) == false){
			JOptionPane.showMessageDialog(this, "El DNI introducido es incorrecto");
			return false;
		}
		else if(c.comprobarTelefono(textTelefono.getText().toString())== false){
			JOptionPane.showMessageDialog(this, "El número de teléfono introducido es incorrecto");
			return false;
		}
		else if(c.comprobarDiasMes(Integer.parseInt((String) comboDia.getSelectedItem()),c.mes((String)comboMes.getSelectedItem())) == false){
			JOptionPane.showMessageDialog(this, "Por favor, introduzca una fecha correcta.");
			return false;
		}
		else if(c.comprobarComboHoras(Integer.parseInt((String) comboHoraInicio.getSelectedItem()), 
				Integer.parseInt((String) comboHoraFinal.getSelectedItem())) == false){
			JOptionPane.showMessageDialog(this, "Las horas introducidas no son válidas");
			return false;
		}
		else if(c.reservaAnterior(Integer.parseInt((String) comboDia.getSelectedItem()), c.mes((String) comboMes.getSelectedItem())
				, Integer.parseInt((String) comboAno.getSelectedItem()), Integer.parseInt((String) comboHoraInicio.getSelectedItem())) == false){
			JOptionPane.showMessageDialog(this, "La fecha que ha introducido ya ha pasado.");
			return false;
		}
		else if(noSocioConReserva() == false){
			JOptionPane.showMessageDialog(this, "El cliente ya tiene una reserva simultánea en ese horario");
			return false;
		}
		else if(c.reservarSieteDias(Integer.parseInt((String) comboAno.getSelectedItem()), c.mes((String) comboMes.getSelectedItem())
				, Integer.parseInt((String) comboDia.getSelectedItem()), Integer.parseInt((String) comboHoraInicio.getSelectedItem())) == false){
			JOptionPane.showMessageDialog(this, "La fecha que ha seleccionado, supera los 7 días permitidos.");
			return false;
		}
		else if(c.reservado(poli, Integer.parseInt((String) comboDia.getSelectedItem()),c.mes((String)comboMes.getSelectedItem()), 
				Integer.parseInt((String) comboAno.getSelectedItem()), Integer.parseInt((String) comboHoraInicio.getSelectedItem())
				, Integer.parseInt((String) comboHoraFinal.getSelectedItem()), String.valueOf(comboInstalacion.getSelectedItem())
				, String.valueOf(comboPista.getSelectedItem())) == false){
			JOptionPane.showMessageDialog(this, "Ya hay un reserva en el horario especificado");
			return false;
		}
		else if(c.NoReservaPosible(poli, obtenerReserva()) == false){
			JOptionPane.showMessageDialog(null, "La instalación no se encuentra disponible en el horario especificado.");
			return false;
		}
		else if(reservaNoDisponible() == false){	//Varias reservas anuladas.
			JOptionPane.showMessageDialog(null, "La instalación no se encuentra disponible en el horario especificado.");
			return false;
		}
		else{
            return true;
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
	
	@SuppressWarnings("deprecation")
	private Reserva obtenerReserva(){
		return new Reserva(getClienteByDNI(textDNI),
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
	
	private Cliente getClienteByDNI(JTextField nSocio) {
		String socio = String.valueOf(nSocio.getText().toString());
		for(Cliente c: poli.getClientes()) {
			if(c.getDNI().equals(socio)){
				return c;
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
	 * Método para comprobar que el no socio no tiene otra reserva a esa misma hora.
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private boolean noSocioConReserva(){
		Calendar cal = Calendar.getInstance();
		String clienteDNI = textDNI.getText().toString();
		int horaComboInicio = Integer.parseInt((String) comboHoraInicio.getSelectedItem());
		int horaComboFinal = Integer.parseInt((String) comboHoraFinal.getSelectedItem());
		int tiempo = Math.abs(horaComboFinal-horaComboInicio);
		int mesCombo = c.mes((String) comboMes.getSelectedItem());
		int diaCombo = Integer.parseInt((String) comboDia.getSelectedItem());
		int añoCombo = Integer.parseInt((String) comboAno.getSelectedItem());
		if(horaComboInicio == 22 && horaComboFinal==0){
			tiempo =2;
		}
		else if(horaComboInicio == 23 && horaComboFinal==0){
			tiempo = 1;
		}
		else if(horaComboInicio == 23 && horaComboFinal==1){
			tiempo = 2;
		}
		//Date fechaCombo = new Date((añoCombo-1900),mesCombo,diaCombo);
		//Date fechaCombo = new Date((añoCombo-1900),mesCombo,diaCombo,horaComboInicio,00);
		for(int i=0; i<poli.getReservas().size(); i++){
			if(poli.getReservas().get(i).getCliente() instanceof NoSocio)
			{
				if(!poli.getReservas().get(i).getEstado().equals(Reserva.ANULADA)){
					if(((NoSocio)poli.getReservas().get(i).getCliente()).getDNI().equals(clienteDNI)){
						cal.setTime(poli.getReservas().get(i).getFechaReserva());
						int dia = cal.get(Calendar.DAY_OF_MONTH);
						int mes = cal.get(Calendar.MONTH);
						int año = cal.get(Calendar.YEAR);
						int horaReservaInicial = cal.get(Calendar.HOUR_OF_DAY);
						int tiempo2 = poli.getReservas().get(i).getTiempoReserva();
						if(año == añoCombo && mes == mesCombo)
						{
							Date fechaInicioReserva = new Date(año-1900,mes,dia,horaReservaInicial,00);
							cal.add(Calendar.HOUR, tiempo2);
							Date fechaFinalReserva = cal.getTime();
							Date fechaComboInicio = new Date(añoCombo-1900, mesCombo, diaCombo, horaComboInicio, 00);
							Date fechaComboFinal;
							if(horaComboInicio == 23 || horaComboInicio == 22){
								cal.setTime(fechaComboInicio);
								cal.add(Calendar.HOUR, tiempo);
								fechaComboFinal = cal.getTime();
							}
							else
							{
								fechaComboFinal =new Date(añoCombo-1900, mesCombo, diaCombo, horaComboFinal, 00);
							}
							if(fechaComboInicio.before(fechaInicioReserva) && fechaInicioReserva.before(fechaComboFinal)){
								return false;
							}
							else if(fechaComboInicio.after(fechaInicioReserva) && fechaComboFinal.before(fechaFinalReserva)){
								return false;
							}
							else if(fechaComboInicio.before(fechaFinalReserva) && fechaComboFinal.after(fechaFinalReserva)){
								return false;
							}
							else if(fechaComboInicio.before(fechaInicioReserva) && fechaComboFinal.after(fechaFinalReserva)){
								return false;
							}
							else if(fechaComboInicio.equals(fechaInicioReserva)){
								return false;
							}
							else if(fechaComboFinal.equals(fechaFinalReserva)){
								return false;
							}
						}
					}
				}
				
			}
		}
		return true;
	}	
	
	public boolean existeNoSocio(String dni) {
		for(Cliente c: poli.getClientes()) {
			if(c.getDNI().equals(dni)) {
				return true;
			}
		}
		return false;
	}
	public void guardarClienteNoSocio() throws SQLException {
		Cliente c = new NoSocio(textNombre.getText(), textApellidos.getText(), textDNI.getText(), textTelefono.getText());
		poli.getClientes().add(c);
		poli.getConexion().guardarClienteNoSocioBD(textNombre.getText(), textApellidos.getText(), textDNI.getText(), textTelefono.getText());
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
