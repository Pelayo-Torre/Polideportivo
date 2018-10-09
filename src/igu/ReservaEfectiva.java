package igu;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JButton;
import javax.swing.JDialog;

import java.awt.FlowLayout;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.border.TitledBorder;


import logica.Cliente;
import logica.Polideportivo;
import logica.Reserva;
import logica.Socio;

import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;

import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JRadioButton;

public class ReservaEfectiva extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JPanel panelDatosUsuario;
	private JScrollPane scrollPaneTablaReservas;
	private JTable tableReservas;
	private JPanel panelDatos;
	private JPanel panelBotones;
	private JButton btnBuscar;
	private JButton btnIniciar;
	private JButton btnFinalizar;
	private JTextField textFieldDato;
	private JPanel panelLabels;
	private JPanel panelBtnBuscar;
	private ModeloNoEditable modeloTablaReservas;
	private JPanel panelBtnIniciar;
	private JPanel panelBtnFinalizar;
	SimpleDateFormat formato = new SimpleDateFormat("yyyy/MM/dd - HH:mm"); 
	private int fila_selec;
	private Polideportivo poli;
	private JPanel panelBtnPagar;
	private JButton btnPagar;
	private JPanel panelInferior;
	private JButton btnSalir;
	private JPanel panelSeleccion;
	private JLabel lblIntroduzcaElValor;
	private JRadioButton rdbtnDNI;
	private JRadioButton rdbtnNumSocio;
	private final ButtonGroup buttonGroup = new ButtonGroup();

	public ReservaEfectiva(Polideportivo polideportivo) {
		String[] nombreColumnas = {"DNI","Instalación","Reserva","Duración","Estado","Inicio","Fin","Estado del pago"};
		modeloTablaReservas = new ModeloNoEditable(nombreColumnas,0);
		poli = polideportivo;
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 1123, 677);
		setTitle("Polideportivo: Confirmar Asistencia");
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		contentPane.add(getPanelDatosUsuario(), BorderLayout.NORTH);
		contentPane.add(getScrollPaneTablaReservas(), BorderLayout.CENTER);
		contentPane.add(getPanelInferior(), BorderLayout.SOUTH);
	}

	private JPanel getPanelDatosUsuario() {
		if (panelDatosUsuario == null) {
			panelDatosUsuario = new JPanel();
			panelDatosUsuario.setBorder(new EmptyBorder(10, 20, 10, 20));
			panelDatosUsuario.setLayout(new GridLayout(0, 2, 0, 0));
			panelDatosUsuario.add(getPanelDatos());
			panelDatosUsuario.add(getPanelBotones());
		}
		return panelDatosUsuario;
	}
	private JScrollPane getScrollPaneTablaReservas() {
		if (scrollPaneTablaReservas == null) {
			scrollPaneTablaReservas = new JScrollPane();
			scrollPaneTablaReservas.setViewportView(getTableReservas());
		}
		return scrollPaneTablaReservas;
	}
	private JTable getTableReservas() {
		if (tableReservas == null) {
			tableReservas = new JTable(modeloTablaReservas);
			tableReservas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			tableReservas.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent arg0) {
					activarBotonesBusqueda();
				}
			});
			
		}
		return tableReservas;
	}
	private JPanel getPanelDatos() {
		if (panelDatos == null) {
			panelDatos = new JPanel();
			panelDatos.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0)), "Seleccione el tipo de dato que desea introducir", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
			panelDatos.setLayout(new GridLayout(2, 1, 0, 0));
			panelDatos.add(getPanelSeleccion());
			panelDatos.add(getPanelLabels());
		}
		return panelDatos;
	}
	private JPanel getPanelBotones() {
		if (panelBotones == null) {
			panelBotones = new JPanel();
			panelBotones.setBorder(new EmptyBorder(5, 0, 0, 0));
			panelBotones.setLayout(new GridLayout(1, 4, 0, 0));
			panelBotones.add(getPanelBtnBuscar());
			panelBotones.add(getPanelBtnIniciar());
			panelBotones.add(getPanelBtnFinalizar());
			panelBotones.add(getPanelBtnPagar());
		}
		return panelBotones;
	}
	private JButton getBtnBuscar() {
		if (btnBuscar == null) {
			btnBuscar = new JButton("Buscar ");
			btnBuscar.setFont(new Font("Tahoma", Font.PLAIN, 15));
			btnBuscar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					if(comprobarTextFields()) {
						modeloTablaReservas.getDataVector().removeAllElements();
						modeloTablaReservas.fireTableDataChanged();
						if(buscarReservaConDatos()) {
							actualizarModeloBuscar();
							activarBotonesBusqueda();
							}
					} else {
						JOptionPane.showMessageDialog(null, "Rellene los datos de usuario");
					}
				}

				
			});
		}
		return btnBuscar;
	}
	private JButton getBtnIniciar() {
		if (btnIniciar == null) {
			btnIniciar = new JButton("Iniciar");
			btnIniciar.setFont(new Font("Tahoma", Font.PLAIN, 15));
			btnIniciar.setEnabled(false);
			btnIniciar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
						cambioEstadoReserva(tableReservas.getSelectedRow(),"iniciar");
						modeloTablaReservas.getDataVector().removeAllElements();
						buscarReservaConDatos();
						actualizarModelo();
						activarBotonesBusqueda();
				}
			});
		}
		return btnIniciar;
	}
	private JButton getBtnFinalizar() {
		if (btnFinalizar == null) {
			btnFinalizar = new JButton("Finalizar");
			btnFinalizar.setFont(new Font("Tahoma", Font.PLAIN, 15));
			btnFinalizar.setEnabled(false);
			btnFinalizar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
						cambioEstadoReserva(tableReservas.getSelectedRow(),"finalizar");
						comprobarSocioHaPagado(tableReservas.getSelectedRow());
						modeloTablaReservas.getDataVector().removeAllElements();
						buscarReservaConDatos();
						actualizarModelo();
						activarBotonesBusqueda();
				}
			});
		}
		return btnFinalizar;
	}
	private JTextField getTextFieldDato() {
		if (textFieldDato == null) {
			textFieldDato = new JTextField();
			textFieldDato.setColumns(10);
		}
		return textFieldDato;
	}
	private JPanel getPanelLabels() {
		if (panelLabels == null) {
			panelLabels = new JPanel();
			panelLabels.setBorder(new EmptyBorder(10, 70, 10, 50));
			panelLabels.setLayout(new BoxLayout(panelLabels, BoxLayout.X_AXIS));
			panelLabels.add(getLblIntroduzcaElValor());
			panelLabels.add(getTextFieldDato());
		}
		return panelLabels;
	}
	private JPanel getPanelBtnBuscar() {
		if (panelBtnBuscar == null) {
			panelBtnBuscar = new JPanel();
			panelBtnBuscar.setBorder(new EmptyBorder(5, 5, 5, 0));
			panelBtnBuscar.setLayout(new BorderLayout(0, 0));
			panelBtnBuscar.add(getBtnBuscar());
		}
		return panelBtnBuscar;
	}
	private JPanel getPanelBtnIniciar() {
		if (panelBtnIniciar == null) {
			panelBtnIniciar = new JPanel();
			panelBtnIniciar.setBorder(new EmptyBorder(5, 5, 5, 0));
			panelBtnIniciar.setLayout(new BorderLayout(0, 0));
			panelBtnIniciar.add(getBtnIniciar());
		}
		return panelBtnIniciar;
	}
	private JPanel getPanelBtnFinalizar() {
		if (panelBtnFinalizar == null) {
			panelBtnFinalizar = new JPanel();
			panelBtnFinalizar.setBorder(new EmptyBorder(5, 5, 5, 0));
			panelBtnFinalizar.setLayout(new BorderLayout(0, 0));
			panelBtnFinalizar.add(getBtnFinalizar());
		}
		return panelBtnFinalizar;
	}
	
	private ArrayList<Reserva> buscarReservasPorDNI(String DNI) {
		ArrayList<Reserva> r = poli.getReservas();
		ArrayList<Reserva> ret = new ArrayList<Reserva>();
		for(int i = 0; i < r.size(); i++) {
			if(!r.get(i).isReservaCentro()) {
				if(r.get(i).getCliente().getDNI().equals(DNI) && !comprobarCaducada(r.get(i))) {
					ret.add(r.get(i));
				}
			}
		}
		return ret;
	}
	
	private ArrayList<Reserva> buscarReservasPorNumSocio(String numSocio) {
		int numSocioInt = Integer.parseInt(numSocio);
		ArrayList<Reserva> r = poli.getReservas();
		ArrayList<Reserva> ret = new ArrayList<Reserva>();
		for(int i = 0; i < r.size(); i++) {
			if(!r.get(i).isReservaCentro()) {
				if(r.get(i).getCliente() instanceof Socio) {
					if(((Socio)r.get(i).getCliente()).getNumeroSocio() == numSocioInt)
						ret.add(r.get(i));
				}
			}
		}
		return ret;
	}
	
	private void añadirReservasTabla(ArrayList<Reserva> reservas, ModeloNoEditable modeloTabla) {
		Object[] row = new Object[8];
		for(Reserva r: reservas) {
			if(!r.getEstado().equals(Reserva.ANULADA)) {
				String fechaConFormato = formato.format(r.getFechaReserva());
				row[0] = r.getCliente().getDNI();
				row[1] = r.getInstalacion().getTipo() + " " + r.getInstalacion().getnPista();
				row[2] = fechaConFormato;
				row[3] = "0" + r.getTiempoReserva() +":00 h"; //las reservas de usuario siempre seran de 1 o 2h.
				row[4] = r.getEstado();
				if(r.getEstado().equals(Reserva.LIBRE)) {
					row[5] = "-";
					row[6] = "-";
				}
				else if(r.getEstado().equals(Reserva.OCUPADA)) {
					String d1 = formato.format(r.getHora_entrada());
					row[5] = d1;
					row[6] = "-";
					
				}else if(r.getEstado().equals(Reserva.FINALIZADA)) {
					String d1 = formato.format(r.getHora_entrada());
					String d2 = formato.format(r.getHora_salida());
					row[5] = d1;
					row[6] = d2;
				}
				row[7]=(r.isPagada()?"Pagada":"No pagada");
				modeloTabla.addRow(row);
			}
		}
	}
	
	private boolean buscarReservaConDatos() {
		String Dato = textFieldDato.getText();
		if(verificarDato(Dato)) { 
			ArrayList<Reserva> reservas = new ArrayList<Reserva>();
			if(rdbtnDNI.isSelected())
				reservas = buscarReservasPorDNI(Dato);
			if(!rdbtnDNI.isSelected())
				reservas = buscarReservasPorNumSocio(Dato);
			if(reservas.isEmpty()) {
				btnIniciar.setEnabled(false);
				JOptionPane.showMessageDialog(null, "Sin historial de reservas");
				return false;
			} else {
				añadirReservasTabla(reservas, modeloTablaReservas);
				return true;
			}
		}
		return false;
	}

	private boolean verificarDato(String dato){
		if(rdbtnDNI.isSelected())
			return verificarDNI(dato);
		else
			return verificarNumSocio(dato);
		
	}
	
	private boolean verificarDNI(String DNI) {
		ArrayList<Cliente> r = poli.getClientes();
		for(int i = 0; i < r.size(); i++) {
			if(r.get(i).getDNI().equals(DNI)) 
				return true;
		}
		JOptionPane.showMessageDialog(null, "El DNI seleccionado no existe");
		return false;
	}
	
	private boolean verificarNumSocio(String numSocio) {
		if(isNumeric(numSocio)) {
			int numSocioEntero = Integer.parseInt(numSocio);
			for(Cliente cliente: poli.getClientes()) {
				if(cliente instanceof Socio) {
					if(((Socio) cliente).getNumeroSocio() == numSocioEntero)
						return true;
				}
			}
			JOptionPane.showMessageDialog(null, "El numero de socio seleccionado no existe");
			return false;
		}
		else {
			JOptionPane.showMessageDialog(null, "El valor introducido no es numérico");
			return false;
		}
	}
	
	public static boolean isNumeric(String str){
	    for (char c : str.toCharArray()){
	        if (!Character.isDigit(c)) 
	        	return false;
	    }
	    return true;
	}
	
	
	private void cambioEstadoReserva(int index, String estado) {
		String DNI = (String)tableReservas.getValueAt(index, 0);
		String fecha = (String)tableReservas.getValueAt(index, 2);
		Date fechaReserva = new Date();
		try {
			fechaReserva = formato.parse(fecha);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Reserva rSelec = buscarReservaSeleccionTabla(DNI, fechaReserva);
		cambiarDatosReserva(rSelec, estado);
	}
	
	public void cambiarDatosReserva(Reserva r, String estado){
		Date f = new Date();
		if(estado.equals("iniciar")) {
			if(f.after(r.getFechaReserva()) ||  f.equals(r.getFechaReserva())) {
				//*****LOCAL*****
				r.setEstado(Reserva.OCUPADA);
				r.setHora_entrada(f);
				try {
				//*****BD*****
					poli.getConexion().actualizarOcupadoFinalizadoBD("HORA_ENTRADA",Reserva.OCUPADA,r.getCliente().getDNI(),r.getInstalacion().getIdPista(),r.getFechaReserva(),f);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			} else
				JOptionPane.showMessageDialog(null, "Sólo puede iniciar la reserva a la hora indicada o posterior.");
		}
		if(estado.equals("finalizar")) {
			//*****LOCAL*****
			r.setEstado(Reserva.FINALIZADA);
			r.setHora_salida(f);
			try {
			//*****BD*****
				poli.getConexion().actualizarOcupadoFinalizadoBD("HORA_SALIDA",Reserva.FINALIZADA,r.getCliente().getDNI(),r.getInstalacion().getIdPista(),r.getFechaReserva(),f);
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
	}
	
	private Reserva buscarReservaSeleccionTabla(String DNI, Date horaReserva) {
		ArrayList<Reserva> reservas = poli.getReservas();
		for(Reserva res: reservas) {
			if(!res.isReservaCentro()) {
				if(res.getCliente().getDNI().equals(DNI) && comprobarFecha(res.getFechaReserva(),horaReserva))
					return res;
			}
		}
		return null;
	}
	
	private void activarBotonesBusqueda() {
		int selec = tableReservas.getSelectedRow();
		fila_selec = selec;
		if(selec != -1) {
			String sim = (String)tableReservas.getValueAt(selec, 5);
			String sim2 = (String)tableReservas.getValueAt(selec, 6);
			String estadoPago= (String)tableReservas.getValueAt(selec, 7);
			String dniCliente = (String)tableReservas.getValueAt(selec, 0);
			if(sim!=null && sim2!=null) {
				if(sim.equals("-") && sim2.equals("-")) {
					btnIniciar.setEnabled(true);
					btnFinalizar.setEnabled(false);
					btnPagar.setEnabled(false);
				}
				else if(!sim.equals("-") && sim2.equals("-")) {
					btnIniciar.setEnabled(false);
					btnFinalizar.setEnabled(true);
					if(estadoPago.equals("No pagada")) {
						if(rdbtnDNI.isSelected()) {
							if(isClienteSocio(dniCliente))
								btnPagar.setEnabled(true);
						} else
							btnPagar.setEnabled(true);
					}
					else
						btnPagar.setEnabled(false);
				}
				else {
					btnIniciar.setEnabled(false);
					btnFinalizar.setEnabled(false);
					btnPagar.setEnabled(false);
				}
			}
		}
		else {
			JOptionPane.showMessageDialog(null, "Seleccione una reserva");
		}
	}
	

	private boolean comprobarCaducada(Reserva r) {
		Calendar c = Calendar.getInstance();
		c.setTime(r.getFechaReserva());
		c.add(Calendar.HOUR_OF_DAY, r.getTiempoReserva());
		Date hoy = new Date();
		if(c.getTime().before(hoy)) {
			r.setEstado(Reserva.CADUCADA);
			try {
				poli.getConexion().actualizarCaducadoBD(r.getIdReserva());
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return true;
		}
	return false;
	}
	
	@SuppressWarnings("deprecation")
	private boolean comprobarFecha(Date d1, Date d2) {
		if(d1.getYear() == d2.getYear() && d1.getMonth() == d2.getMonth() && d1.getDate() == d2.getDate() && d1.getHours() == d2.getHours())
			return true;
		return false;
	}
	
	private boolean comprobarTextDni() {
		if(textFieldDato.getText().isEmpty())
			return false;
		return true;
	}
	
	private boolean comprobarTextFields() {
		if(comprobarTextDni())
			return true;
		return false;
	}
	
	private void actualizarModelo() {
		modeloTablaReservas.fireTableDataChanged();
		tableReservas.setRowSelectionInterval(fila_selec, fila_selec);
	}
	
	private void actualizarModeloBuscar() {
		modeloTablaReservas.fireTableDataChanged();
		fila_selec = 0;
		tableReservas.setRowSelectionInterval(fila_selec, fila_selec);
	}
	private JPanel getPanelBtnPagar() {
		if (panelBtnPagar == null) {
			panelBtnPagar = new JPanel();
			panelBtnPagar.setBorder(new EmptyBorder(5, 5, 5, 0));
			panelBtnPagar.setLayout(new GridLayout(0, 1, 0, 0));
			panelBtnPagar.add(getBtnPagar());
		}
		return panelBtnPagar;
	}
	private JButton getBtnPagar() {
		if (btnPagar == null) {
			btnPagar = new JButton("Registrar \r\npago");
			btnPagar.setFont(new Font("Tahoma", Font.PLAIN, 13));
			btnPagar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					cambiarEstadoPagado(tableReservas.getSelectedRow(),true);
					modeloTablaReservas.getDataVector().removeAllElements();
					buscarReservaConDatos();
					actualizarModelo();
					activarBotonesBusqueda();
				}
			});
			btnPagar.setPreferredSize(new Dimension(71, 23));
			btnPagar.setMinimumSize(new Dimension(71, 23));
			btnPagar.setMaximumSize(new Dimension(71, 23));
			btnPagar.setEnabled(false);
		}
		return btnPagar;
	}
	private void comprobarSocioHaPagado(int index) {
		if(tableReservas.getValueAt(index, 7).equals("No pagada")) {
			cambiarEstadoPagado(index, false);
		}
	}
	private void cambiarEstadoPagado(int index, boolean efectivo) {
		if(efectivo) JOptionPane.showMessageDialog(this, "Se ha registrado el pago y se ha generado un recibo");
		String DNI = (String)tableReservas.getValueAt(index, 0);
		String fecha = (String)tableReservas.getValueAt(index, 2);
		Date fechaReserva = new Date();
		try {
			fechaReserva = formato.parse(fecha);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Reserva rSelec = buscarReservaSeleccionTabla(DNI, fechaReserva);
		rSelec.pagar();
		poli.getConexion().actualizarPagoReserva(rSelec.isPagada(), rSelec.getIdReserva());
		poli.getConexion().guardarPago(rSelec, efectivo);
	}
	
	private boolean isClienteSocio(String dni){
		for(Cliente c: poli.getClientes()) {
			if(c instanceof Socio && c.getDNI().equals(dni))
				return true;
		}
		return false;
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
			btnSalir = new JButton("Cancelar");
			btnSalir.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					dispose();
				}
			});
		}
		return btnSalir;
	}
	private JPanel getPanelSeleccion() {
		if (panelSeleccion == null) {
			panelSeleccion = new JPanel();
			panelSeleccion.add(getRdbtnDNI());
			panelSeleccion.add(getRdbtnNumSocio());
		}
		return panelSeleccion;
	}
	private JLabel getLblIntroduzcaElValor() {
		if (lblIntroduzcaElValor == null) {
			lblIntroduzcaElValor = new JLabel("Introduzca el valor seleccionado: ");
		}
		return lblIntroduzcaElValor;
	}
	private JRadioButton getRdbtnDNI() {
		if (rdbtnDNI == null) {
			rdbtnDNI = new JRadioButton("Buscar por DNI");
			rdbtnDNI.setSelected(true);
			buttonGroup.add(rdbtnDNI);
		}
		return rdbtnDNI;
	}
	private JRadioButton getRdbtnNumSocio() {
		if (rdbtnNumSocio == null) {
			rdbtnNumSocio = new JRadioButton("Buscar por n\u00FAmero de socio");
			buttonGroup.add(rdbtnNumSocio);
		}
		return rdbtnNumSocio;
	}
}
