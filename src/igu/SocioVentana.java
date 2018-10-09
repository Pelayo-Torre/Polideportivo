package igu;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.Toolkit;
import java.awt.Color;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import logica.Comprobaciones;
import logica.Polideportivo;
import logica.Socio;

import javax.swing.ListSelectionModel;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Calendar;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class SocioVentana extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JPanel panelTitulo;
	private JLabel labelTitulo;
	private JPanel panelFotos;
	private JPanel panelDatosPersonales;
	private JLabel label;
	private JLabel labelNombre;
	private JLabel lblNewLabel;
	private JLabel labelApellidos;
	private JLabel lblNewLabel_1;
	private JLabel labelDNI;
	private JLabel lblNewLabel_2;
	private JLabel labelTelefono;
	private JLabel lblNewLabel_3;
	private JLabel labelSocio;
	private JLabel lblNewLabel_4;
	private JLabel labelUsuario;
	private JLabel lblNewLabel_5;
	private JLabel labelFecha;
	private JScrollPane scrollPane;
	private JTable table;
	
	private Polideportivo poli;
	private DefaultTableModel modelo;
	private RendererFiltradoReserva tabla;
	@SuppressWarnings("unused")
	private Comprobaciones c;
	private static String instalacion;
	private String numeroSocio;
	private JButton botonReservar;
	
	private String usuario;
	private JButton botonCerrarSesion;
	
	private SocioVentana sv;
	private JButton btnActividad;
	private JButton botonMisReservas;

	/**
	 * Create the dialog.
	 * @param user 
	 * @param polideportivo 
	 */
	public SocioVentana(Polideportivo polideportivo, String user) {
		setIconImage(Toolkit.getDefaultToolkit().getImage(ReservaInstalacionAdmVentana.class.getResource("/img/icon.jpg")));
		poli = polideportivo;
		c = new Comprobaciones();		
		sv=this;
		setModal(true);
		setTitle("Polideportivo: Socio");
		table = new JTable();
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setFont(new Font("Dialog", Font.BOLD, 16));
		tabla = new RendererFiltradoReserva();
		table.setDefaultRenderer(Object.class, tabla);
		
		modelo =new DefaultTableModel();
		table.setModel(modelo);
		
		setBounds(100, 100, 1028, 671);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		contentPanel.add(getPanelTitulo());
		contentPanel.add(getPanelFotos());
		contentPanel.add(getPanelDatosPersonales());
		
		
		tabla.añadirInstalaciones(table, poli.getInstalaciones(), this);
		contentPanel.add(getBotonReservar());
		contentPanel.add(getBotonCerrarSesion());
		contentPanel.add(getBtnActividad());
		contentPanel.add(getBotonMisReservas());
		
		usuario=user;
		
		rellenaValores(user);
	}
	
	
	private JPanel getPanelTitulo() {
		if (panelTitulo == null) {
			panelTitulo = new JPanel();
			panelTitulo.setBounds(10, 11, 436, 63);
			panelTitulo.setLayout(null);
			panelTitulo.add(getLabelTitulo());
		}
		return panelTitulo;
	}
	private JLabel getLabelTitulo() {
		if (labelTitulo == null) {
			labelTitulo = new JLabel("Centro Deportivo EII");
			labelTitulo.setHorizontalAlignment(SwingConstants.CENTER);
			labelTitulo.setForeground(Color.RED);
			labelTitulo.setFont(new Font("Algerian", Font.BOLD, 30));
			labelTitulo.setBounds(0, 0, 436, 63);
		}
		return labelTitulo;
	}
	private JPanel getPanelFotos() {
		if (panelFotos == null) {
			panelFotos = new JPanel();
			panelFotos.setBounds(260, 85, 742, 496);
			panelFotos.setLayout(new BorderLayout(0, 0));
			panelFotos.add(getScrollPane());
		}
		return panelFotos;
	}
	private JPanel getPanelDatosPersonales() {
		if (panelDatosPersonales == null) {
			panelDatosPersonales = new JPanel();
			panelDatosPersonales.setBorder(new TitledBorder(null, "Datos Personales", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			panelDatosPersonales.setBounds(10, 85, 240, 254);
			panelDatosPersonales.setLayout(null);
			panelDatosPersonales.add(getLabel());
			panelDatosPersonales.add(getLabelNombre());
			panelDatosPersonales.add(getLblNewLabel());
			panelDatosPersonales.add(getLabelApellidos());
			panelDatosPersonales.add(getLblNewLabel_1());
			panelDatosPersonales.add(getLabelDNI());
			panelDatosPersonales.add(getLblNewLabel_2());
			panelDatosPersonales.add(getLabelTelefono());
			panelDatosPersonales.add(getLblNewLabel_3());
			panelDatosPersonales.add(getLabelSocio());
			panelDatosPersonales.add(getLblNewLabel_4());
			panelDatosPersonales.add(getLabelUsuario());
			panelDatosPersonales.add(getLblNewLabel_5());
			panelDatosPersonales.add(getLabelFecha());
		}
		return panelDatosPersonales;
	}
	private JLabel getLabel() {
		if (label == null) {
			label = new JLabel("Nombre:");
			label.setFont(new Font("Dialog", Font.BOLD, 12));
			label.setBounds(10, 26, 59, 22);
		}
		return label;
	}
	private JLabel getLabelNombre() {
		if (labelNombre == null) {
			labelNombre = new JLabel("");
			labelNombre.setForeground(Color.BLUE);
			labelNombre.setFont(new Font("Dialog", Font.BOLD, 14));
			labelNombre.setBounds(79, 26, 151, 22);
		}
		return labelNombre;
	}
	private JLabel getLblNewLabel() {
		if (lblNewLabel == null) {
			lblNewLabel = new JLabel("Apellidos:");
			lblNewLabel.setFont(new Font("Dialog", Font.BOLD, 12));
			lblNewLabel.setBounds(10, 59, 59, 22);
		}
		return lblNewLabel;
	}
	private JLabel getLabelApellidos() {
		if (labelApellidos == null) {
			labelApellidos = new JLabel("");
			labelApellidos.setForeground(Color.BLUE);
			labelApellidos.setFont(new Font("Dialog", Font.BOLD, 14));
			labelApellidos.setBounds(79, 59, 151, 22);
		}
		return labelApellidos;
	}
	private JLabel getLblNewLabel_1() {
		if (lblNewLabel_1 == null) {
			lblNewLabel_1 = new JLabel("DNI:");
			lblNewLabel_1.setFont(new Font("Dialog", Font.BOLD, 12));
			lblNewLabel_1.setBounds(10, 92, 59, 22);
		}
		return lblNewLabel_1;
	}
	private JLabel getLabelDNI() {
		if (labelDNI == null) {
			labelDNI = new JLabel("");
			labelDNI.setForeground(Color.BLUE);
			labelDNI.setFont(new Font("Dialog", Font.BOLD, 14));
			labelDNI.setBounds(79, 92, 151, 22);
		}
		return labelDNI;
	}
	private JLabel getLblNewLabel_2() {
		if (lblNewLabel_2 == null) {
			lblNewLabel_2 = new JLabel("Tel\u00E9fono:");
			lblNewLabel_2.setFont(new Font("Dialog", Font.BOLD, 12));
			lblNewLabel_2.setBounds(10, 125, 59, 22);
		}
		return lblNewLabel_2;
	}
	private JLabel getLabelTelefono() {
		if (labelTelefono == null) {
			labelTelefono = new JLabel("");
			labelTelefono.setForeground(Color.BLUE);
			labelTelefono.setFont(new Font("Dialog", Font.BOLD, 14));
			labelTelefono.setBounds(79, 125, 151, 22);
		}
		return labelTelefono;
	}
	private JLabel getLblNewLabel_3() {
		if (lblNewLabel_3 == null) {
			lblNewLabel_3 = new JLabel("N\u00BA Socio:");
			lblNewLabel_3.setFont(new Font("Dialog", Font.BOLD, 12));
			lblNewLabel_3.setBounds(10, 158, 59, 22);
		}
		return lblNewLabel_3;
	}
	private JLabel getLabelSocio() {
		if (labelSocio == null) {
			labelSocio = new JLabel("");
			labelSocio.setForeground(Color.BLUE);
			labelSocio.setFont(new Font("Dialog", Font.BOLD, 14));
			labelSocio.setBounds(79, 158, 151, 22);
		}
		return labelSocio;
	}
	private JLabel getLblNewLabel_4() {
		if (lblNewLabel_4 == null) {
			lblNewLabel_4 = new JLabel("Usuario:");
			lblNewLabel_4.setFont(new Font("Dialog", Font.BOLD, 12));
			lblNewLabel_4.setBounds(10, 191, 59, 22);
		}
		return lblNewLabel_4;
	}
	private JLabel getLabelUsuario() {
		if (labelUsuario == null) {
			labelUsuario = new JLabel("");
			labelUsuario.setForeground(Color.BLUE);
			labelUsuario.setFont(new Font("Dialog", Font.BOLD, 14));
			labelUsuario.setBounds(79, 191, 151, 22);
		}
		return labelUsuario;
	}
	private JLabel getLblNewLabel_5() {
		if (lblNewLabel_5 == null) {
			lblNewLabel_5 = new JLabel("Fecha de registro:");
			lblNewLabel_5.setFont(new Font("Dialog", Font.BOLD, 12));
			lblNewLabel_5.setBounds(10, 224, 108, 22);
		}
		return lblNewLabel_5;
	}
	private JLabel getLabelFecha() {
		if (labelFecha == null) {
			labelFecha = new JLabel("");
			labelFecha.setForeground(Color.BLUE);
			labelFecha.setFont(new Font("Dialog", Font.BOLD, 14));
			labelFecha.setBounds(128, 224, 102, 22);
		}
		return labelFecha;
	}
	private JScrollPane getScrollPane() {
		if (scrollPane == null) {
			scrollPane = new JScrollPane();
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
					"", "Descripci\u00F3n", "Nombre"
				}
			){
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				public boolean isCellEditable(int row, int column){
					return false;
				}
			}
					
					
					);
			table.setShowVerticalLines(false);
			table.setShowHorizontalLines(false);
			table.setShowGrid(false);
		//}
		return table;
	}
	
	private void abrirVentanaSocioHaceReserva() throws ParseException {
		this.setModal(false);
		SocioEligeHorarioReserva svd = new SocioEligeHorarioReserva(poli, usuario, this);		
		svd.setLocationRelativeTo(this);
		svd.setVisible(true);
	}
	
	@SuppressWarnings("unused")
	private void abrirVentanaHorariosSocio() throws ParseException{
		this.setModal(false);
		HorariosSocioVentana vhs=new HorariosSocioVentana(poli,usuario);
		vhs.setLocationRelativeTo(this);
		vhs.setVisible(true);
	}
	
	private JButton getBotonReservar() {
		if (botonReservar == null) {
			botonReservar = new JButton("Reservar");
			botonReservar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(instalacionSeleccionada(table) == false)
					{
						JOptionPane.showMessageDialog(sv, "Por favor, seleccione una instalación.");
					}
					else
					{
						instalacion = añadirInstalacion(table);
						numeroSocio = labelSocio.getText().toString();
						try {
							abrirVentanaSocioHaceReserva();
						} catch (ParseException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
					
				}
			});
			botonReservar.setFont(new Font("Dialog", Font.BOLD, 12));
			botonReservar.setBounds(636, 592, 111, 31);
		}
		return botonReservar;
	}
	
	
	private void mostrarVentanaMisReservas() throws SQLException{
		this.setModal(false);
		MisReservasVentana mrv = new MisReservasVentana(this);
		mrv.setLocationRelativeTo(this);
		mrv.setVisible(true);
	}
	

	public Polideportivo getPoli() {
		return poli;
	}


	/**
	 * Añade la instalación a la ventana reserva
	 * @param tabla
	 * @return
	 */
	public String añadirInstalacion(JTable tabla){
		int fila = tabla.getSelectedRow();
		String nombre = "";
		if(fila != -1)
		{
			nombre = (String) tabla.getValueAt(fila, 2);
		}
		return nombre;
	}
	
	public static String getInstalacion() {
		return instalacion;
	}
	
	public String getNumeroSocio(){
		return numeroSocio;
	}
	
	/**
	 * Método para rellenar los valores del socio por defecto.
	 */
	private void rellenaValores(String usuario){
		int dia, mes, año;
		Calendar cal = Calendar.getInstance();
		for(int i=0; i<poli.getClientes().size(); i++)
		{
			if(poli.getClientes().get(i) instanceof Socio)
			{
				if(((Socio) poli.getClientes().get(i)).getUsuario().equals(usuario))
				{
					labelNombre.setText(((Socio) poli.getClientes().get(i)).getNombre().toString());
					labelApellidos.setText(((Socio) poli.getClientes().get(i)).getApellidos().toString());
					labelTelefono.setText(((Socio) poli.getClientes().get(i)).getTelefono());
					labelDNI.setText(((Socio) poli.getClientes().get(i)).getDNI());
					labelSocio.setText(String.valueOf(((Socio) poli.getClientes().get(i)).getNumeroSocio()));
					labelUsuario.setText(((Socio) poli.getClientes().get(i)).getUsuario().toString());
					
					cal.setTime(((Socio) poli.getClientes().get(i)).getFechaRegistro());
					dia = cal.get(Calendar.DAY_OF_MONTH);
					mes = cal.get(Calendar.MONTH);
					año = cal.get(Calendar.YEAR);
					labelFecha.setText(dia+"/"+(mes+1)+"/"+año);
				}
			}
		}
	}
	private JButton getBotonCerrarSesion() {
		if (botonCerrarSesion == null) {
			botonCerrarSesion = new JButton("Cerrar Sesi\u00F3n");
			botonCerrarSesion.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					int resultado = JOptionPane.showConfirmDialog(sv, "¿Está seguro de que quiere cerrar sesión?","Cerrar sesión",JOptionPane.YES_NO_OPTION);
					if(resultado == JOptionPane.YES_OPTION)
					{
						dispose();
					}
				}
			});
			botonCerrarSesion.setFont(new Font("Dialog", Font.BOLD, 12));
			botonCerrarSesion.setBounds(871, 51, 131, 23);
		}
		return botonCerrarSesion;
	}


	public String getDNI() {
		return getLabelDNI().getText();
	}
	
	public String getSocio(){
		return getLabelSocio().getText();
	}
	
	private JButton getBtnActividad() {
		if (btnActividad == null) {
			btnActividad = new JButton("Actividad");
			btnActividad.setFont(new Font("Dialog", Font.BOLD, 12));
			btnActividad.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					abrirVentanaActividadSocio();
				}
			});
			btnActividad.setBounds(757, 592, 111, 29);
		}
		return btnActividad;
	}
	
	private void abrirVentanaActividadSocio() {
		this.setModal(false);
		ActividadSocio vhs=new ActividadSocio(poli,usuario);
		vhs.setLocationRelativeTo(this);
		vhs.setVisible(true);
	}
	
	private JButton getBotonMisReservas() {
		if (botonMisReservas == null) {
			botonMisReservas = new JButton("Mis Reservas");
			botonMisReservas.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					try {
						mostrarVentanaMisReservas();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
			botonMisReservas.setFont(new Font("Dialog", Font.BOLD, 12));
			botonMisReservas.setBounds(750, 51, 111, 23);
		}
		return botonMisReservas;
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
