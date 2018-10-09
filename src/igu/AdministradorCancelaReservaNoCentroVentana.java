package igu;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.HeadlessException;

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

import logica.Comprobaciones;
import logica.Instalacion;
import logica.Polideportivo;
import logica.Recibo;
import logica.Reserva;
import logica.Socio;

import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;

import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.awt.event.ActionEvent;
import java.awt.Color;
import java.io.IOException;

import javax.swing.UIManager;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;


public class AdministradorCancelaReservaNoCentroVentana extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	private final JPanel contentPanel = new JPanel();
	
	
	Calendar calendar;
	
	private Polideportivo poli;
	
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JPanel panelFiltrado_1;
	private JPanel panelFiltradoTodasReservas_1;
	@SuppressWarnings("rawtypes")
	private JComboBox comboDia;
	private JLabel labelMes;
	@SuppressWarnings("rawtypes")
	private JComboBox comboMes;
	private JLabel labelAño;
	@SuppressWarnings("rawtypes")
	private JComboBox comboAño;
	private JLabel labelInstalación;
	@SuppressWarnings({ "rawtypes", "unused" })
	private JComboBox comboBox;
	@SuppressWarnings({ "rawtypes", "unused" })
	private JComboBox comboBox_1;
	@SuppressWarnings("rawtypes")
	private JComboBox comboInstalacion;
	private JLabel labelPista;
	@SuppressWarnings("rawtypes")
	private JComboBox comboPista;
	private JButton botonFiltrarTodasReservas;
	private JPanel panelFiltradoSocio;
	private JLabel labelSocio;
	private JTextField textNumeoSocio;
	private JButton botonFiltrarReservaSocio;
	private JPanel panelFiltradoNoSocio;
	private JLabel labelDNI;
	private JTextField textDNI;
	private JButton botonFiltrarReservasNoSocio;
	private JPanel panelRadioBotones;
	private JRadioButton radioBotonPeticionCliente;
	private JRadioButton radioBotonMetereologia;
	private JRadioButton radioBotonNecesidadCentro;
	private JPanel panelTabla;
	private JScrollPane scrollPane;
	private JTable table;
	
	@SuppressWarnings("unused")
	private VentanaAdminPrincipalUltimate apv;
	@SuppressWarnings("unused")
	private Recibo recibo;
	RendererFiltradoReserva tabla;
	private DefaultTableModel modelo;
	Comprobaciones c;

	/**
	 * Create the dialog.
	 */
	public AdministradorCancelaReservaNoCentroVentana(VentanaAdminPrincipalUltimate ventanaAdminPrincipalUltimate) {
		setIconImage(Toolkit.getDefaultToolkit().getImage(ReservaInstalacionAdmVentana.class.getResource("/img/icon.jpg")));
		setTitle("Cancelaci\u00F3n de una reserva");
		setModal(true);
		
		this.apv = ventanaAdminPrincipalUltimate;
		poli = ventanaAdminPrincipalUltimate.getPolideportivo();
		recibo = new Recibo();
		calendar = Calendar.getInstance();
		c=new Comprobaciones();
		table = new JTable();
		tabla = new RendererFiltradoReserva();
		table.setDefaultRenderer(Object.class, tabla);
		
		modelo =new DefaultTableModel();
		table.setModel(modelo);
		
		
		setBounds(100, 100, 1128, 594);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		getPanelFiltrado();
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton botonAnularReserva = new JButton("Anular Reserva");
				botonAnularReserva.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						try {
							if(instalacionSeleccionada(table) == false){
								JOptionPane.showMessageDialog(null, "Seleccione una reserva para anularla");
							}
							else if(radioBotonPeticionCliente.isSelected()==true){
								if(poli.getConexion().anularReserva(calcularFecha(), calcularInstalacion(), calcularPista()) == true){
									JOptionPane.showMessageDialog(null, "La reserva se ha anulado correctamente");
								}
								else
									JOptionPane.showMessageDialog(null, "La reserva no se ha anulado debido a su cercanía");
							}
							else{
								if(radioBotonMetereologia.isSelected() == true){
									if(isInstalacionExterior() == true){	
										poli.getConexion().anularReservaSinCoste(calcularIdReserva(), true);
										poli.getConexion().añadirReservaCentroInstalacionNoDisponible(idPista(), idReserva().getFechaReserva(), idReserva().getTiempoReserva());
										JOptionPane.showMessageDialog(null, "La reserva se ha anulado correctamente");
									}
									else{
										JOptionPane.showMessageDialog(null, "La instalación donde se desarrolla la reserva no se encuentra en el exterior");
									}
								}
								else if(radioBotonNecesidadCentro.isSelected() == true){
									//GenerarReciboReservaNoSocio();
									poli.getConexion().anularReservaSinCoste(calcularIdReserva(), false);
									poli.getConexion().añadirReservaCentroInstalacionNoDisponible(idPista(), idReserva().getFechaReserva(), idReserva().getTiempoReserva());
									JOptionPane.showMessageDialog(null, "La reserva se ha anulado correctamente");
									
								}
								
							}
						} catch (HeadlessException e) {
							e.printStackTrace();
						} catch (SQLException e) {
							e.printStackTrace();
						} catch (ParseException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				});
				botonAnularReserva.setFont(new Font("Dialog", Font.BOLD, 12));
				botonAnularReserva.setActionCommand("OK");
				buttonPane.add(botonAnularReserva);
				getRootPane().setDefaultButton(botonAnularReserva);
			}
			{
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
		}
		
	}
	
	private String idPista(){
		String ins = calcularInstalacion();
		String p = calcularPista();
		for(int i=0; i<poli.getReservas().size(); i++){
			if(poli.getReservas().get(i).getInstalacion().getTipo().equals(ins) 
					&& poli.getReservas().get(i).getInstalacion().getnPista().equals(p)){
				return poli.getReservas().get(i).getInstalacion().getIdPista();
			}
		}
		return null;
	}
	
	private Reserva idReserva(){
		String id = calcularIdReserva();
		for(int i=0; i<poli.getReservas().size(); i++){
			if(poli.getReservas().get(i).getIdReserva().equals(id)){
				return poli.getReservas().get(i);
			}
		}
		return null;
	}

	private void getPanelFiltrado() {
		contentPanel.setLayout(null);
		panelFiltrado_1 = new JPanel();
		panelFiltrado_1.setBounds(5, 5, 207, 499);
		contentPanel.add(panelFiltrado_1);
		JPanel panelFiltradoTodasReservas = getPanelFiltradoTodasReservas(panelFiltrado_1);
		getLabelDia(panelFiltradoTodasReservas);
		panelFiltradoTodasReservas_1.add(getComboDia());
		panelFiltradoTodasReservas_1.add(getLabelMes());
		panelFiltradoTodasReservas_1.add(getComboMes());
		panelFiltradoTodasReservas_1.add(getLabelAño());
		panelFiltradoTodasReservas_1.add(getComboAño());
		panelFiltradoTodasReservas_1.add(getLabelInstalación());
		panelFiltradoTodasReservas_1.add(getComboInstalacion());
		panelFiltradoTodasReservas_1.add(getLabelPista());
		panelFiltradoTodasReservas_1.add(getComboPista());
		panelFiltradoTodasReservas_1.add(getBotonFiltrarTodasReservas());
		panelFiltrado_1.add(getPanelFiltradoSocio());
		panelFiltrado_1.add(getPanelFiltradoNoSocio());
		contentPanel.add(getPanelRadioBotones());
		contentPanel.add(getPanelTabla());

	}

	private void getLabelDia(JPanel panelFiltradoTodasReservas) {
		panelFiltradoTodasReservas_1.setLayout(null);
		JLabel labelDia = new JLabel("D\u00EDa:");
		labelDia.setBounds(10, 21, 44, 20);
		labelDia.setFont(new Font("Dialog", Font.BOLD, 12));
		panelFiltradoTodasReservas.add(labelDia);
	}

	private JPanel getPanelFiltradoTodasReservas(JPanel panelFiltrado) {
		panelFiltrado_1.setLayout(null);
		panelFiltradoTodasReservas_1 = new JPanel();
		panelFiltradoTodasReservas_1.setBounds(10, 11, 164, 241);
		panelFiltradoTodasReservas_1.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Filtrado General", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panelFiltrado.add(panelFiltradoTodasReservas_1);
		return panelFiltradoTodasReservas_1;
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private JComboBox getComboDia() {
		if (comboDia == null) {
			comboDia = new JComboBox();
			int dia = calendar.get(Calendar.DATE)-1;
			comboDia.setModel(new DefaultComboBoxModel(new String[] {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"}));
			comboDia.setSelectedIndex(dia);	
			comboDia.setFont(new Font("Dialog", Font.BOLD, 12));
			comboDia.setBounds(64, 21, 52, 20);
		}
		return comboDia;
	}
	private JLabel getLabelMes() {
		if (labelMes == null) {
			labelMes = new JLabel("Mes:");
			labelMes.setFont(new Font("Dialog", Font.BOLD, 12));
			labelMes.setBounds(10, 52, 52, 20);
		}
		return labelMes;
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private JComboBox getComboMes() {
		if (comboMes == null) {
			comboMes = new JComboBox();
			int mes = calendar.get(Calendar.MONTH);
			comboMes.setModel(new DefaultComboBoxModel(new String[] {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"}));
			comboMes.setSelectedIndex(mes);
			comboMes.setFont(new Font("Dialog", Font.BOLD, 12));
			comboMes.setBounds(44, 52, 110, 20);
		}
		return comboMes;
	}
	private JLabel getLabelAño() {
		if (labelAño == null) {
			labelAño = new JLabel("A\u00F1o:");
			labelAño.setFont(new Font("Dialog", Font.BOLD, 12));
			labelAño.setBounds(10, 83, 44, 20);
		}
		return labelAño;
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private JComboBox getComboAño() {
		if (comboAño == null) {
			comboAño = new JComboBox();
			int ano = calendar.get(Calendar.YEAR);
			comboAño.setModel(new DefaultComboBoxModel(new String[] {"2017", "2018", "2019", "2020", "2021", "2022", "2023", "2024", "2025", "2026", "2027", "2028", "2029", "2030"}));
			comboAño.setSelectedItem(ano);
			comboAño.setFont(new Font("Dialog", Font.BOLD, 12));
			comboAño.setBounds(64, 83, 75, 19);
		}
		return comboAño;
	}
	private JLabel getLabelInstalación() {
		if (labelInstalación == null) {
			labelInstalación = new JLabel("Instalaci\u00F3n:");
			labelInstalación.setFont(new Font("Dialog", Font.BOLD, 12));
			labelInstalación.setBounds(10, 114, 98, 20);
		}
		return labelInstalación;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
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
	

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private JComboBox getComboInstalacion() {
		if (comboInstalacion == null) {
			comboInstalacion = new JComboBox();
			DefaultComboBoxModel model = new DefaultComboBoxModel();
			llenarComboInstalaciones(model);
			comboInstalacion.setModel(model);
			comboInstalacion.setFont(new Font("Dialog", Font.BOLD, 12));
			comboInstalacion.setBounds(10, 145, 144, 20);
		}
		return comboInstalacion;
	}
	private JLabel getLabelPista() {
		if (labelPista == null) {
			labelPista = new JLabel("Pista:");
			labelPista.setFont(new Font("Dialog", Font.BOLD, 12));
			labelPista.setBounds(10, 176, 52, 20);
		}
		return labelPista;
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private JComboBox getComboPista() {
		if (comboPista == null) {
			comboPista = new JComboBox();
			comboPista.setModel(new DefaultComboBoxModel(new String[] {"1", "2"}));
			comboPista.setFont(new Font("Dialog", Font.BOLD, 12));
			comboPista.setBounds(64, 176, 52, 20);
		}
		return comboPista;
	}
	private JButton getBotonFiltrarTodasReservas() {
		if (botonFiltrarTodasReservas == null) {
			botonFiltrarTodasReservas = new JButton("Filtrar Reservas");
			botonFiltrarTodasReservas.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					filtrar1();
				}
			});
			botonFiltrarTodasReservas.setFont(new Font("Dialog", Font.BOLD, 12));
			botonFiltrarTodasReservas.setBounds(10, 207, 129, 23);
		}
		return botonFiltrarTodasReservas;
	}
	private JPanel getPanelFiltradoSocio() {
		if (panelFiltradoSocio == null) {
			panelFiltradoSocio = new JPanel();
			panelFiltradoSocio.setBorder(new TitledBorder(null, "Filtrado Socio", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			panelFiltradoSocio.setBounds(10, 263, 164, 108);
			panelFiltradoSocio.setLayout(null);
			panelFiltradoSocio.add(getLabelSocio());
			panelFiltradoSocio.add(getTextNumeoSocio());
			panelFiltradoSocio.add(getBotonFiltrarReservaSocio());
		}
		return panelFiltradoSocio;
	}
	private JLabel getLabelSocio() {
		if (labelSocio == null) {
			labelSocio = new JLabel("N\u00FAmero de Socio:");
			labelSocio.setHorizontalAlignment(SwingConstants.LEFT);
			labelSocio.setFont(new Font("Dialog", Font.BOLD, 12));
			labelSocio.setBounds(10, 22, 125, 22);
		}
		return labelSocio;
	}
	private JTextField getTextNumeoSocio() {
		if (textNumeoSocio == null) {
			textNumeoSocio = new JTextField();
			textNumeoSocio.setFont(new Font("Dialog", Font.BOLD, 12));
			textNumeoSocio.setHorizontalAlignment(SwingConstants.CENTER);
			textNumeoSocio.setBounds(10, 45, 86, 20);
			textNumeoSocio.setColumns(10);
		}
		return textNumeoSocio;
	}
	private JButton getBotonFiltrarReservaSocio() {
		if (botonFiltrarReservaSocio == null) {
			botonFiltrarReservaSocio = new JButton("Filtrar Reservas");
			botonFiltrarReservaSocio.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					filtrar2();
				}
			});
			botonFiltrarReservaSocio.setFont(new Font("Dialog", Font.BOLD, 12));
			botonFiltrarReservaSocio.setBounds(10, 76, 125, 23);
		}
		return botonFiltrarReservaSocio;
	}
	private JPanel getPanelFiltradoNoSocio() {
		if (panelFiltradoNoSocio == null) {
			panelFiltradoNoSocio = new JPanel();
			panelFiltradoNoSocio.setBorder(new TitledBorder(null, "Filtrado no Socio", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			panelFiltradoNoSocio.setBounds(10, 382, 164, 108);
			panelFiltradoNoSocio.setLayout(null);
			panelFiltradoNoSocio.add(getLabelDNI());
			panelFiltradoNoSocio.add(getTextDNI());
			panelFiltradoNoSocio.add(getBotonFiltrarReservasNoSocio());
		}
		return panelFiltradoNoSocio;
	}
	private JLabel getLabelDNI() {
		if (labelDNI == null) {
			labelDNI = new JLabel("DNI Solicitante:");
			labelDNI.setFont(new Font("Dialog", Font.BOLD, 12));
			labelDNI.setBounds(10, 19, 102, 22);
		}
		return labelDNI;
	}
	private JTextField getTextDNI() {
		if (textDNI == null) {
			textDNI = new JTextField();
			textDNI.setFont(new Font("Dialog", Font.BOLD, 12));
			textDNI.setHorizontalAlignment(SwingConstants.CENTER);
			textDNI.setBounds(10, 43, 125, 20);
			textDNI.setColumns(10);
		}
		return textDNI;
	}
	private JButton getBotonFiltrarReservasNoSocio() {
		if (botonFiltrarReservasNoSocio == null) {
			botonFiltrarReservasNoSocio = new JButton("Filtrar Reservas");
			botonFiltrarReservasNoSocio.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					filtrar3();
				}
			});
			botonFiltrarReservasNoSocio.setFont(new Font("Dialog", Font.BOLD, 12));
			botonFiltrarReservasNoSocio.setBounds(10, 74, 125, 23);
		}
		return botonFiltrarReservasNoSocio;
	}
	private JPanel getPanelRadioBotones() {
		if (panelRadioBotones == null) {
			panelRadioBotones = new JPanel();
			panelRadioBotones.setBounds(268, 5, 834, 51);
			panelRadioBotones.setLayout(null);
			panelRadioBotones.add(getRadioBotonPeticionCliente());
			panelRadioBotones.add(getRadioBotonMetereologia());
			panelRadioBotones.add(getRadioBotonNecesidadCentro());
		}
		return panelRadioBotones;
	}
	private JRadioButton getRadioBotonPeticionCliente() {
		if (radioBotonPeticionCliente == null) {
			radioBotonPeticionCliente = new JRadioButton("Cancelar Reserva a petici\u00F3n del cliente");
			radioBotonPeticionCliente.setSelected(true);
			buttonGroup.add(radioBotonPeticionCliente);
			radioBotonPeticionCliente.setFont(new Font("Dialog", Font.BOLD, 12));
			radioBotonPeticionCliente.setBounds(19, 15, 255, 23);
		}
		return radioBotonPeticionCliente;
	}
	private JRadioButton getRadioBotonMetereologia() {
		if (radioBotonMetereologia == null) {
			radioBotonMetereologia = new JRadioButton("Cancelar Reserva por Metereolog\u00EDa");
			buttonGroup.add(radioBotonMetereologia);
			radioBotonMetereologia.setFont(new Font("Dialog", Font.BOLD, 12));
			radioBotonMetereologia.setBounds(276, 15, 225, 23);
		}
		return radioBotonMetereologia;
	}
	private JRadioButton getRadioBotonNecesidadCentro() {
		if (radioBotonNecesidadCentro == null) {
			radioBotonNecesidadCentro = new JRadioButton("Cancelar Reserva por necesidad del centro");
			buttonGroup.add(radioBotonNecesidadCentro);
			radioBotonNecesidadCentro.setFont(new Font("Dialog", Font.BOLD, 12));
			radioBotonNecesidadCentro.setBounds(514, 15, 285, 23);
		}
		return radioBotonNecesidadCentro;
	}
	private JPanel getPanelTabla() {
		if (panelTabla == null) {
			panelTabla = new JPanel();
			panelTabla.setBounds(222, 67, 880, 437);
			panelTabla.setLayout(new BorderLayout(0, 0));
			panelTabla.add(getScrollPane(), BorderLayout.CENTER);
		}
		return panelTabla;
	}
	private JScrollPane getScrollPane() {
		if (scrollPane == null) {
			scrollPane = new JScrollPane();
			scrollPane.setViewportView(getTable());
		}
		return scrollPane;
	}
	private JTable getTable() {
		
			table.setModel(new DefaultTableModel(
				new Object[][] {
				},
				new String[] {
					"Fecha", "Horario", "Instalaci\u00F3n", "Pista", "DNI", "Nº Socio","TipoActividad", "Número Reserva"
				}
			)
			{
				private static final long serialVersionUID = 1L;

				public boolean isCellEditable(int row, int column){
					return false;
				}
			});
		//table.removeColumn(table.getColumnModel().getColumn(7));
		//table.getColumnModel().getColumn(7).setResizable(false);
		table.getColumnModel().getColumn(7).setMaxWidth(0);
		table.getColumnModel().getColumn(7).setMinWidth(0);
		table.getTableHeader().getColumnModel().getColumn(7).setMaxWidth(0);
		table.getTableHeader().getColumnModel().getColumn(7).setMinWidth(0);
		
		return table;
	}
	
	/**
	 * Comprueba campos correctos del primer filtrado.
	 * @return
	 */
	private boolean filtrar1(){
		if(c.comprobarDiasMes(Integer.parseInt((String) comboDia.getSelectedItem()),c.mes((String)comboMes.getSelectedItem())) == false){
			JOptionPane.showMessageDialog(null, "Introduzca una fecha correcta");
			return false;
		}
		else{
			tabla.limpiar(table);
			añadirTodasReservasVentanaAdmin();
			return true;
		}
	}
	
	private boolean filtrar2(){
		if(c.comprobarCamposTextoVacios(textNumeoSocio.getText().toString()) == false){
			JOptionPane.showMessageDialog(null, "Campo de texto vacío");
			return false;
		}
		else if(c.comprobarTexto(textNumeoSocio.getText().toString()) == true){
			JOptionPane.showMessageDialog(null, "El nº de socio introducido es incorrecto");
			return false;
		}
		else if(socioRegistrado() == false){
			JOptionPane.showMessageDialog(null, "El nº de socio introducido no existe");
			return false;
		}
		else{
			tabla.limpiar(table);
			añadirReservasSocioVentanaAdmin();
			return true;
		}
	}
	
	private boolean filtrar3(){
		if(c.comprobarCamposTextoVacios(textDNI.getText().toString()) == false){
			JOptionPane.showMessageDialog(null, "Campo de texto vacío");
			return false;
		}
		else if(c.DNI(textDNI.getText().toString()) == false){
			JOptionPane.showMessageDialog(null, "El DNI introducido es incorrecto");
			return false;
		}
		else{
			tabla.limpiar(table);
			añadirReservasNoSocioVentanaAdmin();
			return true;
		}
	}
	
	/**
	 * Método para comprobar que el numero introducido es socio.
	 * @return
	 */
	private boolean socioRegistrado(){
		int numeroSocioTexto = Integer.parseInt(textNumeoSocio.getText().toString());
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
	 * Método que añade las reservas en la ventana admin para anular reservas
	 */
	public void añadirTodasReservasVentanaAdmin(){
		Collections.sort(poli.getReservas(), new Comparator<Reserva>() {

			@Override
			public int compare(Reserva arg0, Reserva arg1) {
				return arg0.getFechaReserva().compareTo(arg1.getFechaReserva());
			}
		    
		});
		Object [] array = new Object[8];
		Calendar cal = Calendar.getInstance();
		int diaCombo,mesCombo,añoCombo,dia,mes,año ,tiempo, hora;
		String instalacion = String.valueOf(comboInstalacion.getSelectedItem());
		String pista = String.valueOf(comboPista.getSelectedItem());
		diaCombo = Integer.parseInt((String) comboDia.getSelectedItem());
		mesCombo = c.mes((String) comboMes.getSelectedItem());
		añoCombo = Integer.parseInt((String) comboAño.getSelectedItem());
		for(int i = 0; i<poli.getReservas().size(); i++)
		{
			if(reservaPasada(poli.getReservas().get(i)) == true){
				if(!poli.getReservas().get(i).isReservaCentro()){
					cal.setTime(poli.getReservas().get(i).getFechaReserva());
					dia = cal.get(Calendar.DAY_OF_MONTH);
					mes = cal.get(Calendar.MONTH);
					año = cal.get(Calendar.YEAR);
					hora = cal.get(Calendar.HOUR_OF_DAY);
					if(diaCombo == dia && mesCombo == mes && añoCombo == año &&	//Sacamos las reservas que sean de esa fecha
							poli.getReservas().get(i).getInstalacion().getTipo().equals(instalacion) &&
							poli.getReservas().get(i).getInstalacion().getnPista().equals(pista)){
						tiempo =  poli.getReservas().get(i).getTiempoReserva();
						if(!poli.getReservas().get(i).isReservaCentro()){
							if(!poli.getReservas().get(i).getEstado().equals(Reserva.ANULADA)){
								array[0] = año+"-"+(mes+1)+"-"+dia+" "+hora+":00";
								if(hora == 22 && tiempo == 2){
									array[1] = hora + ":00 - "+"00:00";
								}
								else if(hora == 23 && tiempo == 1){
									array[1] = hora + ":00 - "+"00:00";
								}
								else if(hora == 23 && tiempo==2){
									array[1] = hora + ":00 - "+"01:00";
								}
								else
								{
									array[1] = hora + ":00 - "+(hora+tiempo)+":00";
								}
								array[2] = poli.getReservas().get(i).getInstalacion().getTipo();
								array[3] = poli.getReservas().get(i).getInstalacion().getnPista();
								array[4] = poli.getReservas().get(i).getCliente().getDNI();
								if(poli.getReservas().get(i).getCliente() instanceof Socio){
									array[5] = ((Socio)poli.getReservas().get(i).getCliente()).getNumeroSocio();
								}
								else{
									array[5] = "   -";
								}
								array[6] = "   -";
								array[7] = poli.getReservas().get(i).getIdReserva();
								((DefaultTableModel) table.getModel()).addRow(array);
							}
						}
						else
						{
							array[0] = año+"-"+(mes+1)+"-"+dia+" "+hora+":00";
							array[1] = hora+":00";
							array[2] = poli.getReservas().get(i).getInstalacion().getTipo();
							array[3] = poli.getReservas().get(i).getInstalacion().getnPista();
							array[4] = "Reserva Centro";
							array[5] = "   -";
							array[6] = poli.getReservas().get(i).getTipoActividad();
							array[7] = poli.getReservas().get(i).getIdReserva();
							((DefaultTableModel) table.getModel()).addRow(array);
						}
					}
				}
				
			}
			
		}
	}
	
	
	/**
	 * Método que añade las reservas del socio en la ventana AdministradorCancelaReservaVentana
	 * @param poli
	 * @param tabla
	 * @param socio2
	 */
	public void añadirReservasSocioVentanaAdmin(){
		Collections.sort(poli.getReservas(), new Comparator<Reserva>() {

			@Override
			public int compare(Reserva arg0, Reserva arg1) {
				return arg0.getFechaReserva().compareTo(arg1.getFechaReserva());
			}
		    
		});
		Object [] array = new Object[8];
		Calendar cal = Calendar.getInstance();
		int año,dia,mes,hora,tiempo;
		int socio = Integer.parseInt(textNumeoSocio.getText().toString());
		for(int i = 0; i<poli.getReservas().size(); i++)
		{
			if(reservaPasada(poli.getReservas().get(i)) == true){
				if(!poli.getReservas().get(i).isReservaCentro()){
					if(!poli.getReservas().get(i).getEstado().equals(Reserva.ANULADA)){
						if(poli.getReservas().get(i).getCliente() instanceof Socio){
							if(((Socio)poli.getReservas().get(i).getCliente()).getNumeroSocio()==socio){
								cal.setTime(poli.getReservas().get(i).getFechaReserva());
								dia = cal.get(Calendar.DAY_OF_MONTH);
								mes = cal.get(Calendar.MONTH);
								año = cal.get(Calendar.YEAR);
								hora = cal.get(Calendar.HOUR_OF_DAY);
								tiempo =  poli.getReservas().get(i).getTiempoReserva();
								array[0] = año+"-"+(mes+1)+"-"+dia+" "+hora+":00";
								
								if(poli.getReservas().get(i).isReservaCentro() == true){
									array[1] = hora+":00";
								}
								else
								{
									if(hora == 22 && tiempo == 2){
										array[1] = hora + ":00 - "+"00:00";
									}
									else if(hora == 23 && tiempo == 1){
										array[1] = hora + ":00 - "+"00:00";
									}
									else if(hora == 23 && tiempo==2){
										array[1] = hora + ":00 - "+"01:00";
									}
									else
									{
										array[1] = hora + ":00 - "+(hora+tiempo)+":00";
									}		
								}		
								
								array[2] = poli.getReservas().get(i).getInstalacion().getTipo();
								array[3] = poli.getReservas().get(i).getInstalacion().getnPista();
								array[4] = poli.getReservas().get(i).getCliente().getDNI();
								array[5] = ((Socio)poli.getReservas().get(i).getCliente()).getNumeroSocio();
								array[6] = "   -";
								array[7] = poli.getReservas().get(i).getIdReserva();
								((DefaultTableModel) table.getModel()).addRow(array);
							}
						}
					}
					
				}
			}	
		}
	}
	
	/**
	 * Método que añade las reservas del no socio en la ventana AdministradorCancelaReservaVentana
	 * @param poli
	 * @param table
	 * @param dni2
	 */
	public void añadirReservasNoSocioVentanaAdmin(){
		Collections.sort(poli.getReservas(), new Comparator<Reserva>() {

			@Override
			public int compare(Reserva arg0, Reserva arg1) {
				return arg0.getFechaReserva().compareTo(arg1.getFechaReserva());
			}
		    
		});
		Object [] array = new Object[8];
		Calendar cal = Calendar.getInstance();
		int año,dia,mes,hora,tiempo;
		String dni = textDNI.getText();
		for(int i = 0; i<poli.getReservas().size(); i++)
		{
			if(reservaPasada(poli.getReservas().get(i)) == true){
				if(!poli.getReservas().get(i).isReservaCentro()){
					if(!poli.getReservas().get(i).getEstado().equals(Reserva.ANULADA)){
						if(poli.getReservas().get(i).getCliente().getDNI().equals(dni)){
							cal.setTime(poli.getReservas().get(i).getFechaReserva());
							dia = cal.get(Calendar.DAY_OF_MONTH);
							mes = cal.get(Calendar.MONTH);
							año = cal.get(Calendar.YEAR);
							hora = cal.get(Calendar.HOUR_OF_DAY);
							tiempo =  poli.getReservas().get(i).getTiempoReserva();
							array[0] = año+"-"+(mes+1)+"-"+dia+" "+hora+":00";					
							if(hora == 22 && tiempo == 2){
								array[1] = hora + ":00 - "+"00:00";
							}
							else if(hora == 23 && tiempo == 1){
								array[1] = hora + ":00 - "+"00:00";
							}
							else if(hora == 23 && tiempo==2){
								array[1] = hora + ":00 - "+"01:00";
							}
							else
							{
								array[1] = hora + ":00 - "+(hora+tiempo)+":00";
							}		
							array[2] = poli.getReservas().get(i).getInstalacion().getTipo();
							array[3] = poli.getReservas().get(i).getInstalacion().getnPista();
							array[4] = poli.getReservas().get(i).getCliente().getDNI();
							array[5] = "   -";
							array[6] = "   -";
							array[7] = poli.getReservas().get(i).getIdReserva();
							((DefaultTableModel) table.getModel()).addRow(array);
						}
					}
					
				}
			}
		}
	}
	
	/**
	 * Método que impida mostrar reservas ya pasadas.
	 * @param reserva
	 * @return
	 */
	private boolean reservaPasada(Reserva reserva){
		java.util.Date date = new Date();
		if(reserva.getFechaReserva().before(date)){
			return false;
		}
		return true;
	}
	
	/**
	 * Para saber si la instalacion es exterior o no.
	 * @return
	 */
	private boolean isInstalacionExterior(){
		int fila = table.getSelectedRow();
		if(fila != -1)
		{
			String instalacion=String.valueOf(table.getValueAt(fila, 2));
			String nPista=String.valueOf(table.getValueAt(fila, 3));
			String idPista="";
			for(Instalacion i: poli.getInstalaciones()){
				if(i.getTipo().equals(instalacion) && i.getnPista().equals(nPista)){
					idPista = i.getIdPista();
				}
			}
			if(idPista.equals("1") || idPista.equals("2") || idPista.equals("3") ||
					idPista.equals("4") || idPista.equals("5") || idPista.equals("6")){
				return true;
			}
		}
		return false;
	}
	
	private String calcularFecha(){
		int fila = table.getSelectedRow();
		if(fila != -1)
		{
			return String.valueOf(table.getValueAt(fila, 0));
		}
		return "";
	}
	
	private String calcularInstalacion(){
		int fila = table.getSelectedRow();
		if(fila != -1)
		{
			return (String) String.valueOf(table.getValueAt(fila, 2));
		}
		return "";
	}
	
	private String calcularPista(){
		int fila = table.getSelectedRow();
		if(fila != -1)
		{
			return String.valueOf(table.getValueAt(fila, 3));
		}
		return "";
	}
	
	private String calcularIdReserva(){
		int fila = table.getSelectedRow();
		if(fila != -1)
		{
			return String.valueOf(table.getValueAt(fila, 7));
		}
		return "";
	}
	
	/**
	 * Método para asegurarse de que hay una fila seleccionada.
	 * @param tabla
	 * @return
	 */
	public boolean instalacionSeleccionada(JTable tabla){
		if(tabla.getSelectedRow() == -1)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	
	
	
	
	
	
	
}
