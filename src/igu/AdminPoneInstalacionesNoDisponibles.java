package igu;


import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;

import java.awt.Font;
import java.awt.Toolkit;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;

import javax.swing.JComboBox;
import javax.swing.border.TitledBorder;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import logica.Comprobaciones;
import logica.Instalacion;
import logica.NoSocio;
import logica.Polideportivo;
import logica.Recibo;
import logica.Reserva;
import logica.Socio;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;


@SuppressWarnings({ "rawtypes", "serial" })
public class AdminPoneInstalacionesNoDisponibles extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JPanel panelInicio;
	private JPanel panelFinal;
	private JLabel lblNewLabel;
	private JLabel lblNewLabel_1;
	private JLabel lblNewLabel_2;
	private JLabel lblNewLabel_3;
	private JLabel lblNewLabel_4;
	
	private JComboBox comboHoraInicio;
	private JComboBox comboDiaInicio;
	private JComboBox comboMesInicio;
	private JLabel lblNewLabel_5;
	private JLabel lblNewLabel_6;
	private JLabel lblNewLabel_7;
	private JComboBox comboHoraFinal;
	private JComboBox comboDiaFinal;
	private JComboBox comboMesFinal;
	private JLabel lblNewLabel_8;
	private JComboBox comboAnoInicio;
	private JLabel lblNewLabel_9;
	private JComboBox comboAnoFinal;
	private JComboBox comboInstalacion;
	private JComboBox comboPista;
	private JScrollPane scrollPane;
	private JButton Cancelar;
	private JButton botonCancelarReservas;
	private JTable table;
	private DefaultTableModel modelo;
	private Polideportivo poli;
	private RendererFiltradoReserva tabla;
	private Comprobaciones c;
	private GregorianCalendar calendar = new GregorianCalendar();
	private JButton botonAfectados;
	
	Calendar cal = Calendar.getInstance();
	//Lista de las reservas que se van a anular.
	private ArrayList<String> idReserva = new ArrayList<String>();
	//Lista de las reservas que se anularon durante la ejecución
	private ArrayList<String> reservasAnuladas = new ArrayList<String>();
	
	private JRadioButton radioBotonMetereología;
	private JRadioButton radioBotonCentro;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JLabel lblNewLabel_10;
	
	//Éstas fechas servirán para poner la instalación como no disponible durante el tiempo delimitado por ambas fechas.
	private Date fechaI;	//Fecha inicial en la que la instalación va a quedar como no disponible
	private Date fechaF;	//Fecha final en la que la instalación va a quedar como no disponible
	private String idPistaS;	//id de la instalación que va a quedar como no disponible.
	private JButton botonReservasAnuladas;
	private JLabel labelReserva;
	
	@SuppressWarnings("unused")
	private VentanaAdminPrincipalUltimate apv;

	/**
	 * Create the dialog.
	 * @throws SQLException 
	 */
	public AdminPoneInstalacionesNoDisponibles(VentanaAdminPrincipalUltimate apv2) {
		setIconImage(Toolkit.getDefaultToolkit().getImage(ReservaInstalacionAdmVentana.class.getResource("/img/icon.jpg")));
		setTitle("Poner instalaciones como no disponibles");
		setModal(true);
		this.apv = apv2;
		poli = apv2.getPolideportivo();
		c = new Comprobaciones();
		
		
		setBounds(100, 100, 914, 590);
		getContentPane().setLayout(null);
		contentPanel.setBounds(0, 0, 898, 551);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel);
		contentPanel.setLayout(null);
		contentPanel.add(getPanelInicio());
		contentPanel.add(getPanelFinal());
		contentPanel.add(getLblNewLabel_3());
		contentPanel.add(getLblNewLabel_4());
		contentPanel.add(getComboInstalacion());
		contentPanel.add(getComboPista());
		
		table = new JTable();
		tabla = new RendererFiltradoReserva();
		table.setDefaultRenderer(Object.class, tabla);
		
		modelo =new DefaultTableModel();
		table.setModel(modelo);
		
		contentPanel.add(getScrollPane());
		contentPanel.add(getCancelar());
		contentPanel.add(getBotonCancelarReservas());
		contentPanel.add(getBotonAfectados());
		contentPanel.add(getRadioBotonMetereología());
		contentPanel.add(getRadioBotonCentro());
		contentPanel.add(getLblNewLabel_10());
		contentPanel.add(getBotonReservasAnuladas());
		contentPanel.add(getLabelReserva());
		
	}
	private JPanel getPanelInicio() {
		if (panelInicio == null) {
			panelInicio = new JPanel();
			panelInicio.setBorder(new TitledBorder(null, "Fecha inicial", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			panelInicio.setBounds(10, 11, 250, 174);
			panelInicio.setLayout(null);
			panelInicio.add(getLblNewLabel());
			panelInicio.add(getLblNewLabel_1());
			panelInicio.add(getLblNewLabel_2());
			panelInicio.add(getComboHoraInicio());
			panelInicio.add(getComboDiaInicio());
			panelInicio.add(getComboMesInicio());
			panelInicio.add(getLblNewLabel_8());
			panelInicio.add(getComboAnoInicio());
		}
		return panelInicio;
	}
	private JPanel getPanelFinal() {
		if (panelFinal == null) {
			panelFinal = new JPanel();
			panelFinal.setBorder(new TitledBorder(null, "Fecha final", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			panelFinal.setBounds(270, 11, 250, 174);
			panelFinal.setLayout(null);
			panelFinal.add(getLblNewLabel_5());
			panelFinal.add(getLblNewLabel_6());
			panelFinal.add(getLblNewLabel_7());
			panelFinal.add(getComboHoraFinal());
			panelFinal.add(getComboDiaFinal());
			panelFinal.add(getComboMesFinal());
			panelFinal.add(getLblNewLabel_9());
			panelFinal.add(getComboAnoFinal());
		}
		return panelFinal;
	}
	private JLabel getLblNewLabel() {
		if (lblNewLabel == null) {
			lblNewLabel = new JLabel("Hora inicio:");
			lblNewLabel.setFont(new Font("Dialog", Font.BOLD, 12));
			lblNewLabel.setBounds(10, 11, 73, 29);
		}
		return lblNewLabel;
	}
	private JLabel getLblNewLabel_1() {
		if (lblNewLabel_1 == null) {
			lblNewLabel_1 = new JLabel("D\u00EDa inicio:");
			lblNewLabel_1.setFont(new Font("Dialog", Font.BOLD, 12));
			lblNewLabel_1.setBounds(10, 51, 73, 29);
		}
		return lblNewLabel_1;
	}
	private JLabel getLblNewLabel_2() {
		if (lblNewLabel_2 == null) {
			lblNewLabel_2 = new JLabel("Mes inicio:");
			lblNewLabel_2.setFont(new Font("Dialog", Font.BOLD, 12));
			lblNewLabel_2.setBounds(10, 91, 73, 29);
		}
		return lblNewLabel_2;
	}
	private JLabel getLblNewLabel_3() {
		if (lblNewLabel_3 == null) {
			lblNewLabel_3 = new JLabel("Instalaci\u00F3n:");
			lblNewLabel_3.setFont(new Font("Dialog", Font.BOLD, 12));
			lblNewLabel_3.setBounds(540, 11, 77, 29);
		}
		return lblNewLabel_3;
	}
	private JLabel getLblNewLabel_4() {
		if (lblNewLabel_4 == null) {
			lblNewLabel_4 = new JLabel("Pista:");
			lblNewLabel_4.setFont(new Font("Dialog", Font.BOLD, 12));
			lblNewLabel_4.setBounds(540, 55, 77, 29);
		}
		return lblNewLabel_4;
	}
	@SuppressWarnings("unchecked")
	private JComboBox getComboHoraInicio() {
		if (comboHoraInicio == null) {
			comboHoraInicio = new JComboBox();
			comboHoraInicio.setFont(new Font("Dialog", Font.BOLD, 12));
			comboHoraInicio.setModel(new DefaultComboBoxModel(new String[] {"00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23"}));
			comboHoraInicio.setBounds(93, 11, 73, 29);
		}
		return comboHoraInicio;
	}
	@SuppressWarnings("unchecked")
	private JComboBox getComboDiaInicio() {
		if (comboDiaInicio == null) {
			comboDiaInicio = new JComboBox();
			int dia = calendar.get(Calendar.DATE)-1;
			comboDiaInicio.setFont(new Font("Dialog", Font.BOLD, 12));
			comboDiaInicio.setModel(new DefaultComboBoxModel(new String[] {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"}));
			comboDiaInicio.setSelectedIndex(dia);
			comboDiaInicio.setBounds(93, 51, 73, 29);
		}
		return comboDiaInicio;
	}
	@SuppressWarnings("unchecked")
	private JComboBox getComboMesInicio() {
		if (comboMesInicio == null) {
			comboMesInicio = new JComboBox();
			int mes = calendar.get(Calendar.MONTH);
			comboMesInicio.setFont(new Font("Dialog", Font.BOLD, 12));
			comboMesInicio.setModel(new DefaultComboBoxModel(new String[] {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"}));
			comboMesInicio.setSelectedIndex(mes);
			comboMesInicio.setBounds(93, 91, 145, 29);
		}
		return comboMesInicio;
	}
	private JLabel getLblNewLabel_5() {
		if (lblNewLabel_5 == null) {
			lblNewLabel_5 = new JLabel("Hora final:");
			lblNewLabel_5.setFont(new Font("Dialog", Font.BOLD, 12));
			lblNewLabel_5.setBounds(10, 11, 73, 29);
		}
		return lblNewLabel_5;
	}
	private JLabel getLblNewLabel_6() {
		if (lblNewLabel_6 == null) {
			lblNewLabel_6 = new JLabel("D\u00EDa final:");
			lblNewLabel_6.setFont(new Font("Dialog", Font.BOLD, 12));
			lblNewLabel_6.setBounds(10, 51, 73, 29);
		}
		return lblNewLabel_6;
	}
	private JLabel getLblNewLabel_7() {
		if (lblNewLabel_7 == null) {
			lblNewLabel_7 = new JLabel("Mes final:");
			lblNewLabel_7.setFont(new Font("Dialog", Font.BOLD, 12));
			lblNewLabel_7.setBounds(10, 91, 73, 29);
		}
		return lblNewLabel_7;
	}
	@SuppressWarnings("unchecked")
	private JComboBox getComboHoraFinal() {
		if (comboHoraFinal == null) {
			comboHoraFinal = new JComboBox();	
			comboHoraFinal.setFont(new Font("Dialog", Font.BOLD, 12));
			comboHoraFinal.setModel(new DefaultComboBoxModel(new String[] {"00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23"}));
			comboHoraFinal.setBounds(93, 11, 73, 29);
		}
		return comboHoraFinal;
	}
	@SuppressWarnings("unchecked")
	private JComboBox getComboDiaFinal() {
		if (comboDiaFinal == null) {
			comboDiaFinal = new JComboBox();
			int dia = calendar.get(Calendar.DATE)-1;
			comboDiaFinal.setModel(new DefaultComboBoxModel(new String[] {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"}));
			comboDiaFinal.setSelectedIndex(dia);
			comboDiaFinal.setFont(new Font("Dialog", Font.BOLD, 12));
			comboDiaFinal.setBounds(93, 51, 73, 29);
		}
		return comboDiaFinal;
	}
	@SuppressWarnings("unchecked")
	private JComboBox getComboMesFinal() {
		if (comboMesFinal == null) {
			comboMesFinal = new JComboBox();
			int mes = calendar.get(Calendar.MONTH);
			comboMesFinal.setModel(new DefaultComboBoxModel(new String[] {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"}));
			comboMesFinal.setSelectedIndex(mes);
			comboMesFinal.setFont(new Font("Dialog", Font.BOLD, 12));
			comboMesFinal.setBounds(93, 91, 145, 29);
		}
		return comboMesFinal;
	}
	private JLabel getLblNewLabel_8() {
		if (lblNewLabel_8 == null) {
			lblNewLabel_8 = new JLabel("A\u00F1o inicio:");
			lblNewLabel_8.setFont(new Font("Dialog", Font.BOLD, 12));
			lblNewLabel_8.setBounds(10, 131, 73, 29);
		}
		return lblNewLabel_8;
	}
	@SuppressWarnings("unchecked")
	private JComboBox getComboAnoInicio() {
		if (comboAnoInicio == null) {
			comboAnoInicio = new JComboBox();
			int ano = calendar.get(Calendar.YEAR);
			comboAnoInicio.setModel(new DefaultComboBoxModel(new String[] {"2017", "2018", "2019", "2020", "2021", "2022", "2023", "2024", "2025", "2026", "2027", "2028", "2029", "2030", "2031", "2032", "2033", "2034", "2035", "2036", "2037", "2038", "2039", "2040"}));
			comboAnoInicio.setSelectedItem(ano);
			comboAnoInicio.setFont(new Font("Dialog", Font.BOLD, 12));
			comboAnoInicio.setBounds(93, 131, 73, 29);
		}
		return comboAnoInicio;
	}
	private JLabel getLblNewLabel_9() {
		if (lblNewLabel_9 == null) {
			lblNewLabel_9 = new JLabel("A\u00F1o final:");
			lblNewLabel_9.setFont(new Font("Dialog", Font.BOLD, 12));
			lblNewLabel_9.setBounds(10, 131, 73, 29);
		}
		return lblNewLabel_9;
	}
	@SuppressWarnings("unchecked")
	private JComboBox getComboAnoFinal() {
		if (comboAnoFinal == null) {
			comboAnoFinal = new JComboBox();
			int ano = calendar.get(Calendar.YEAR);
			comboAnoFinal.setFont(new Font("Dialog", Font.BOLD, 12));
			comboAnoFinal.setModel(new DefaultComboBoxModel(new String[] {"2017", "2018", "2019", "2020", "2021", "2022", "2023", "2024", "2025", "2026", "2027", "2028", "2029", "2030", "2031", "2032", "2033", "2034", "2035", "2036", "2037", "2038", "2039", "2040"}));
			comboAnoFinal.setSelectedItem(ano);
			comboAnoFinal.setBounds(93, 131, 73, 29);
		}
		return comboAnoFinal;
	}
	@SuppressWarnings("unchecked")
	private JComboBox getComboInstalacion() {
		if (comboInstalacion == null) {
			comboInstalacion = new JComboBox();
			DefaultComboBoxModel model = new DefaultComboBoxModel();
			llenarComboInstalaciones(model);
			comboInstalacion.setModel(model);
			comboInstalacion.setFont(new Font("Dialog", Font.BOLD, 12));
			comboInstalacion.setBounds(627, 11, 230, 29);
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
	
	@SuppressWarnings("unchecked")
	private JComboBox getComboPista() {
		if (comboPista == null) {
			comboPista = new JComboBox();
			comboPista.setModel(new DefaultComboBoxModel(new String[] {"1", "2"}));
			comboPista.setFont(new Font("Dialog", Font.BOLD, 12));
			comboPista.setBounds(627, 55, 77, 29);
		}
		return comboPista;
	}
	
	private JScrollPane getScrollPane() {
		if (scrollPane == null) {
			scrollPane = new JScrollPane();
			scrollPane.setBounds(10, 226, 878, 280);
			scrollPane.setViewportView(getTable());
		}
		return scrollPane;
	}
	private JButton getCancelar() {
		if (Cancelar == null) {
			Cancelar = new JButton("Cancelar");
			Cancelar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					dispose();
				}
			});
			Cancelar.setFont(new Font("Dialog", Font.BOLD, 12));
			Cancelar.setBounds(799, 517, 89, 23);
		}
		return Cancelar;
	}
	private JButton getBotonCancelarReservas() {
		if (botonCancelarReservas == null) {
			botonCancelarReservas = new JButton("Cancelar Instalaci\u00F3n");
			botonCancelarReservas.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					try {
						if(comprobar() == true){
							tabla.limpiar(table);
							cancelarReservas();
						}
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}	
				}
			});
			botonCancelarReservas.setFont(new Font("Dialog", Font.BOLD, 12));
			botonCancelarReservas.setBounds(611, 517, 178, 23);
		}
		return botonCancelarReservas;
	}
	private JTable getTable() {
			table.setModel(new DefaultTableModel(
				new Object[][] {
				},
				new String[] {
					"Nombre", "Apellidos", "DNI", "Fecha", "Nº Socio", "Teléfono"
				}
			)
			{public boolean isCellEditable(int row, int column){
					return false;
				}
			}
					
					);
			table.setFont(new Font("Dialog", Font.BOLD, 12));
		
		return table;
	}
	private JButton getBotonAfectados() {
		if (botonAfectados == null) {
			botonAfectados = new JButton("Reservas afectadas");
			botonAfectados.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					tabla.limpiar(table);
					try {
						comprobar();
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
			botonAfectados.setFont(new Font("Dialog", Font.BOLD, 12));
			botonAfectados.setBounds(441, 517, 160, 23);
		}
		return botonAfectados;
	}
	private JRadioButton getRadioBotonMetereología() {
		if (radioBotonMetereología == null) {
			radioBotonMetereología = new JRadioButton("Metereolog\u00EDa");
			buttonGroup.add(radioBotonMetereología);
			radioBotonMetereología.setFont(new Font("Dialog", Font.BOLD, 12));
			radioBotonMetereología.setBounds(540, 162, 125, 23);
		}
		return radioBotonMetereología;
	}
	private JRadioButton getRadioBotonCentro() {
		if (radioBotonCentro == null) {
			radioBotonCentro = new JRadioButton("Necesidad Centro");
			radioBotonCentro.setSelected(true);
			buttonGroup.add(radioBotonCentro);
			radioBotonCentro.setFont(new Font("Dialog", Font.BOLD, 12));
			radioBotonCentro.setBounds(540, 136, 146, 23);
		}
		return radioBotonCentro;
	}
	private JLabel getLblNewLabel_10() {
		if (lblNewLabel_10 == null) {
			lblNewLabel_10 = new JLabel("Motivo de la anulaci\u00F3n");
			lblNewLabel_10.setFont(new Font("Dialog", Font.BOLD, 12));
			lblNewLabel_10.setBounds(540, 106, 153, 23);
		}
		return lblNewLabel_10;
	}
	private JButton getBotonReservasAnuladas() {
		if (botonReservasAnuladas == null) {
			botonReservasAnuladas = new JButton("Reservas anuladas");
			botonReservasAnuladas.setEnabled(false);
			botonReservasAnuladas.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					tabla.limpiar(table);
					labelReserva.setText("Reservas anuladas: ");
					pintarReservasAnuladas();
				}
			});
			botonReservasAnuladas.setFont(new Font("Dialog", Font.BOLD, 12));
			botonReservasAnuladas.setBounds(283, 517, 148, 23);
		}
		return botonReservasAnuladas;
	}
	private JLabel getLabelReserva() {
		if (labelReserva == null) {
			labelReserva = new JLabel("Reservas afectadas en el horario especificado:");
			labelReserva.setFont(new Font("Dialog", Font.BOLD, 14));
			labelReserva.setBounds(10, 196, 370, 23);
		}
		return labelReserva;
	}
	
	
	
	
	
	
	
	
	
	/**
	 * Comprobamos que las fechas sean correctas y mostramos las reservas.
	 * @return
	 * @throws ParseException 
	 * @throws SQLException 
	 */
	@SuppressWarnings("deprecation")
	private boolean comprobar() throws ParseException, SQLException{	
		//Valores de los combos para las fechas
		int horaInicio = Integer.parseInt((String) comboHoraInicio.getSelectedItem());
		int diaInicio = Integer.parseInt((String) comboDiaInicio.getSelectedItem());
		int mesInicio = c.mes((String) comboMesInicio.getSelectedItem());
		int anoInicio = Integer.parseInt((String) comboAnoInicio.getSelectedItem());
		int horaFinal = Integer.parseInt((String) comboHoraFinal.getSelectedItem());
		int diaFinal = Integer.parseInt((String) comboDiaFinal.getSelectedItem());
		int mesFinal = c.mes((String) comboMesFinal.getSelectedItem());
		int anoFinal = Integer.parseInt((String) comboAnoFinal.getSelectedItem());
		
		//Valores de los combos para la instalación
		String tipo = String.valueOf((String) comboInstalacion.getSelectedItem());
		String nPista = String.valueOf((String) comboPista.getSelectedItem());
		
		//Obtenemos las fechas
		Date fechaInicial = new Date(anoInicio-1900, mesInicio, diaInicio, horaInicio, 00, 00);
		Date fechaFinal = new Date(anoFinal-1900, mesFinal, diaFinal, horaFinal, 00, 00);
		
		//Comprobamos que la fechas introducidas sean correctas, febrero no tiene 30 días.
		if(c.comprobarDiasMes(diaInicio, mesInicio) == false){
			JOptionPane.showMessageDialog(null, "Las fechas introducidas son incorrectas");
			return false;
		}
		else if(c.comprobarDiasMes(diaFinal, mesFinal) == false){
			JOptionPane.showMessageDialog(null, "Las fechas introducidas son incorrectas");
			return false;
		}
		else if(comprobarFechas(fechaInicial, fechaFinal) == false ){
			JOptionPane.showMessageDialog(null, "Las fechas introducidas son incorrectas");
			return false;
		}
		else if(fechaPasada(fechaInicial,fechaFinal) == false){
			JOptionPane.showMessageDialog(null, "Las fechas introducidas son incorrectas");
			return false;
		}
		//Guardamos valores.
		fechaI = fechaInicial;
		fechaF = fechaFinal;
		//Obtenemos el idPista
		String idPista = obtenerIdPista(tipo,nPista);
		idPistaS = idPista;
		//Pintamos las reservas
		reservasEliminar(fechaInicial, fechaFinal, idPista);
		return true;
	}
	
	
	
	/**
	 * Método para obtener el idPista seleccionado
	 * @param tipo
	 * @param nPista
	 * @return
	 */
	private String obtenerIdPista(String tipo, String nPista){
		for(int i=0; i<poli.getInstalaciones().size(); i++){
			if(poli.getInstalaciones().get(i).getTipo().equals(tipo) && poli.getInstalaciones().get(i).getnPista().equals(nPista)){
				return poli.getInstalaciones().get(i).getIdPista();
			}
		}
		return null;
	}
	
	/**
	 * Comprueba que la fechaFinal sea antes que la fecha1.
	 * @param date1
	 * @param date2
	 * @return
	 */
	private boolean comprobarFechas(Date date1, Date date2){
		if(date2.before(date1) || date2.equals(date1)){
			return false;
		}
		
		return true;
	}
	
	/**
	 * Comprueba que no se muestre una fecha pasada
	 * @param date1
	 * @param date2
	 * @return
	 */
	private boolean fechaPasada(Date date1, Date date2){
		Calendar c = Calendar.getInstance();
		if(date1.before(c.getTime()) || date2.before(c.getTime())){
			return false;
		}
		return true;
	}
	
	/**
	 * Método que selecciona las reservas que se van a cancelar.
	 * @param fechaInicio
	 * @param fechaFin
	 * @param idPista
	 * @throws ParseException 
	 */
	@SuppressWarnings("deprecation")
	private void reservasEliminar(Date fechaInicio, Date fechaFin, String idPista) throws ParseException{
		idReserva.clear();
		for(Reserva reservas: poli.getReservas()){
			if(reservas.isReservaCentro() == false){
				if(!reservas.getEstado().equals("ANULADA")){
					if(reservas.getInstalacion().getIdPista().equals(idPista)){
						cal.setTime(reservas.getFechaReserva());
						int dia = cal.get(Calendar.DAY_OF_MONTH); int mes = cal.get(Calendar.MONTH); int año = cal.get(Calendar.YEAR); int hora = cal.get(Calendar.HOUR_OF_DAY);
						Date fechaReservaInicial = new Date(año-1900, mes, dia, hora, 00, 00);
						cal.add(Calendar.HOUR_OF_DAY, reservas.getTiempoReserva());
						int dia2 = cal.get(Calendar.DAY_OF_MONTH); int mes2 = cal.get(Calendar.MONTH); int año2 = cal.get(Calendar.YEAR); int hora2 = cal.get(Calendar.HOUR_OF_DAY);
						Date fechaReservaFinal = new Date(año2-1900, mes2, dia2, hora2, 00,00);
						//fecha de reserva entre los límites
						if(fechaReservaInicial.after(fechaInicio) && fechaReservaFinal.before(fechaFin)){
							pintarReserva(reservas);
						}
						//fecha de reserva fuera por la izq y dentro por drch
						else if(fechaReservaInicial.before(fechaInicio) && fechaReservaFinal.after(fechaInicio)){
							pintarReserva(reservas);
						}
						//fecha de reserva igual por izq y dentro drch
						else if(fechaReservaInicial.equals(fechaInicio) || fechaReservaFinal.equals(fechaFin)){
							pintarReserva(reservas);
						}
						//fecha de reserva dentro por izq y fuera por drch
						else if(fechaReservaInicial.before(fechaFin) && fechaReservaFinal.after(fechaFin)){
							pintarReserva(reservas);
						}
					}
				}
			}
		}
		//Cambiamos el titulo del contenido de la tabla.
		labelReserva.setText("Reservas afectadas en el horario especificado:");
	}
	
	/**
	 * Añade las reservas a la tabla.
	 * @param reserva
	 */
	private void pintarReserva(Reserva reserva){
		Calendar cal = Calendar.getInstance();
		int dia,mes,año,hora;
		cal.setTime(reserva.getFechaReserva());
		dia = cal.get(Calendar.DAY_OF_MONTH);
		mes = cal.get(Calendar.MONTH);
		año = cal.get(Calendar.YEAR);
		hora = cal.get(Calendar.HOUR_OF_DAY);
		Object [] array = new Object[6];
			array[0] = reserva.getCliente().getNombre();
			array[1] = reserva.getCliente().getApellidos();
			array[2] = reserva.getCliente().getDNI();
			array[3] = año+"-"+(mes+1)+"-"+dia+" "+hora+":00";
			if(reserva.getCliente() instanceof Socio){
				array[4] = ((Socio)reserva.getCliente()).getNumeroSocio();
			}
			else{
				array[4] = "No Socio";
			}
			array[5] = reserva.getCliente().getTelefono();
		((DefaultTableModel) table.getModel()).addRow(array);	
		idReserva.add(reserva.getIdReserva());
	} 
	
	/**
	 * Pone las reservas a ANULADAS
	 */
	private void cancelarReservas(){
		String motivo = "";
		if(radioBotonCentro.isSelected() == true){
			motivo = "NECESIDAD_CENTRO";
		}
		else{
			motivo = "METEREOLOGIA";
		}
		//Las ponemos a anuladas en la BBDD
		try {
			poli.getConexion().anularConjuntoReservas(idReserva, motivo);			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//Las ponemos a anuladas en local.
		for(int i=0; i<idReserva.size(); i++){
			for(int j=0; j<poli.getReservas().size(); j++){
				if(poli.getReservas().get(j).getIdReserva().equals(idReserva.get(i))){
					reservasAnuladas.add(idReserva.get(i));
					poli.getReservas().get(j).setEstado("ANULADA");
					poli.getReservas().get(j).setMotivoAnulacion(motivo);
					//Recorrer id de reserva y generar recibo de devolución para los no socios.
					if(reservaNoSocio(idReserva.get(i)) == true){
						try {
							Recibo.generarReciboNoSocio(poli.getReservas().get(j));
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
		//Añadimos la instalacion a no disponible.
		anularInstalacion();
		//Habilitamos el botón para poder ver las reservas anuladas.
		botonReservasAnuladas.setEnabled(true);
	}
	
	private boolean reservaNoSocio(String idReserva){
		for(int i=0; i<poli.getReservas().size(); i++){
			if(poli.getReservas().get(i).getIdReserva().equals(idReserva)){
				if(poli.getReservas().get(i).getCliente() instanceof NoSocio){
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Anula la instalación en la fecha seleccionada.
	 */
	private void anularInstalacion(){
		try {
			if(puedesReservar(idPistaS, fechaI, fechaF) == false){
				JOptionPane.showMessageDialog(null, "La instalación seleccionada en el horario seleccionado\n          ya se encuentra anulada");
			}
			else{
				poli.getConexion().anularInstalacion(fechaI, fechaF, idPistaS);
				int diff=(int) ((fechaF.getTime()-fechaI.getTime())/(60*60 * 1000));//Diferencia en horas.
				poli.getConexion().añadirReservaCentroInstalacionNoDisponible(idPistaS, fechaI, diff);
				System.out.println("Instalación anulada correctamente");
				JOptionPane.showMessageDialog(null, "La instalación se ha anulado exitosamente");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Pinta en la tabla las reservas anuladas.
	 */
	public void pintarReservasAnuladas(){
		for(int i=0; i<reservasAnuladas.size(); i++){
			pintarReserva(obtenerReserva(reservasAnuladas.get(i)));
		}
	}
	
	/**
	 * Obtiene la reserva
	 * @param idReserva
	 * @return
	 */
	public Reserva obtenerReserva(String idReserva){
		for(Reserva r : poli.getReservas()){
			if(r.getIdReserva().equals(idReserva)){
				return r;
			}
		}
		return null;
	}
	
	public boolean puedesReservar(String idInstalacion, Date fechaI, Date fechaF){
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
					if(comprobarDisponibilidad(fechaInicialInstalacion, fechaFinalInstalacion, fechaI, fechaF) == false){
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
	
	private boolean comprobarDisponibilidad(Date fechaInicialInstalacion, Date fechaFinalInstalacion, Date fechaI, Date fechaF){
		//Caso1: fechaI y fechaF dentro de los limites.
		if(fechaI.after(fechaInicialInstalacion) && fechaF.before(fechaFinalInstalacion)){
			return false;
		}
		//Caso2:
		else if(fechaI.equals(fechaInicialInstalacion) && fechaF.before(fechaFinalInstalacion)){
			return false;
		}
		//Caso3:
		else if(fechaI.after(fechaInicialInstalacion) &&  fechaF.equals(fechaFinalInstalacion)){
			return false;
		}
		else if(fechaI.equals(fechaInicialInstalacion) && fechaF.equals(fechaFinalInstalacion)){
			return false;
		}
		return true;
	}
	
	
}
