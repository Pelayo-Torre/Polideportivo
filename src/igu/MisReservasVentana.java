package igu;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.HeadlessException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.Font;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import bd.Conexion;
import logica.Comprobaciones;
import logica.Polideportivo;
import logica.Reserva;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.IOException;

@SuppressWarnings("serial")
public class MisReservasVentana extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTable table;
	
	private DefaultTableModel modelo;
	private Polideportivo poli;
	private SocioVentana sv;
	private RendererFiltradoReserva tabla;
	@SuppressWarnings("unused")
	private Comprobaciones c;
	@SuppressWarnings("unused")
	private Conexion conexion;
	private JPanel buttonPane_1;


	/**
	 * Create the dialog.
	 * @throws SQLException 
	 */
	public MisReservasVentana(SocioVentana sv1) throws SQLException {
		setTitle("Mis Reservas");
		setModal(true);
		this.sv = sv1;
		poli = sv1.getPoli();
		c = new Comprobaciones();
		
		table = new JTable();
		tabla = new RendererFiltradoReserva();
		table.setDefaultRenderer(Object.class, tabla);
		
		modelo =new DefaultTableModel();
		table.setModel(modelo);
		
		
		setBounds(100, 100, 918, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		getPanelNorteEtiquetas();
		{
			buttonPane_1 = new JPanel();
			buttonPane_1.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane_1, BorderLayout.SOUTH);
			getBotonAnular(buttonPane_1);
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						dispose();
					}
				});
				cancelButton.setFont(new Font("Dialog", Font.BOLD, 12));
				cancelButton.setActionCommand("Cancel");
				buttonPane_1.add(cancelButton);
			}
		}
		añadirReservasPropias();
		//textNSocio.setText(sv.getNumeroSocio().toString());
	}

	private void getBotonAnular(JPanel buttonPane) {
		JButton botonAnular = new JButton("Anular Reserva");
		botonAnular.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(instalacionSeleccionada(table) == false){
					JOptionPane.showMessageDialog(null, "Seleccione una reserva para anularla");
				}
				else{
					try {
						if(poli.getConexion().anularReserva(calcularFecha(), calcularInstalacion(), calcularPista()) == false){
							JOptionPane.showMessageDialog(null, "La reserva ya no se puede anular debido a su cercanía.");
						}
						else
						{
							JOptionPane.showMessageDialog(null, "Reserva anulada.");
							tabla.limpiar(table);
							añadirReservasPropias();
						}
					} catch (HeadlessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		
		botonAnular.setFont(new Font("Dialog", Font.BOLD, 12));
		buttonPane.add(botonAnular);
	}

	
	private void getPanelNorteEtiquetas() {
		getPanelTabla();
		
	}

	private void getPanelTabla() {
		JPanel panel_4 = new JPanel();
		contentPanel.add(panel_4, BorderLayout.CENTER);
		panel_4.setLayout(new BorderLayout(0, 0));
		getScrollPanel(panel_4);
	}

	private void getScrollPanel(JPanel panel_4) {
		JScrollPane scrollPane = new JScrollPane();
		panel_4.add(scrollPane, BorderLayout.CENTER);
		getPanelTabla(scrollPane);
	}

	private void getPanelTabla(JScrollPane scrollPane) {
		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Fecha", "Horario", "Instalaci\u00F3n", "Pista", "Tiempo"
			}
		)
		{
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int row, int column){
				return false;
			}
		}
				);
		scrollPane.setViewportView(table);
	}

	/**
	 * Método para añadir las reservas propias en MisReservasVentana
	 * @param poli
	 * @param tabla
	 * @param dni
	 */
	public void añadirReservasPropias(){
		//ArrayList donde voy a guardar las reservas ordenadas.
				ArrayList<Reserva> listaOrdenada = new ArrayList<Reserva>();	
				
				Object [] array = new Object[5];
				Calendar cal = Calendar.getInstance();
				int dia,mes,año,hora, tiempo;
				for(int i = 0; i<poli.getReservas().size(); i++)
				{
					if(reservaPasada(poli.getReservas().get(i)) == true){
						if(!poli.getReservas().get(i).isReservaCentro()){
							if(!poli.getReservas().get(i).getEstado().equals("ANULADA")){
								if(poli.getReservas().get(i).getCliente().getDNI().equals(sv.getDNI())){	
									listaOrdenada.add(poli.getReservas().get(i));
								}
							}
							
						}
					}	
				}
				Collections.sort(listaOrdenada, new Comparator<Reserva>() {

					@Override
					public int compare(Reserva arg0, Reserva arg1) {
						return arg0.getFechaReserva().compareTo(arg1.getFechaReserva());
					}
				    
				});
				for(int j=0; j<listaOrdenada.size(); j++){			
					cal.setTime(listaOrdenada.get(j).getFechaReserva());
					dia = cal.get(Calendar.DAY_OF_MONTH);
					mes = cal.get(Calendar.MONTH);
					año = cal.get(Calendar.YEAR);
					hora = cal.get(Calendar.HOUR_OF_DAY);
					tiempo = listaOrdenada.get(j).getTiempoReserva();
					array[0] = (año+"-"+(mes+1)+"-"+dia+" "+hora+":00");
					if(listaOrdenada.get(j).isReservaCentro() == true){
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
							
					
					array[2] = listaOrdenada.get(j).getInstalacion().getTipo();
					array[3] = listaOrdenada.get(j).getInstalacion().getnPista();
					if(tiempo == 1){
						array[4] = tiempo+" hora";
					}
					else
					{
						array[4] = tiempo+" horas";
					}
					((DefaultTableModel) table.getModel()).addRow(array);
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
	

}

	


