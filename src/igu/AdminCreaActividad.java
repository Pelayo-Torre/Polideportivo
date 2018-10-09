package igu;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;

import java.awt.Font;

import javax.swing.JTextField;
import javax.swing.JCheckBox;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.SQLException;

import logica.Comprobaciones;
import logica.Polideportivo;
import java.awt.Toolkit;

public class AdminCreaActividad extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JLabel lblNewLabel;
	private JTextField textNombreActividad;
	private JCheckBox checkPlazasLimitadas;
	private JLabel labelPlazas;
	private JTextField textPlazas;
	
	
	private Comprobaciones c;
	private Polideportivo poli;
	//private ReservaActividad ra;

//	/**
//	 * Launch the application.
//	 */
//	public static void main(String[] args) {
//		try {
//			AdminCreaActividad dialog = new AdminCreaActividad();
//			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
//			dialog.setVisible(true);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

	/**
	 * Create the dialog.
	 */
	public AdminCreaActividad(ReservaActividad ra) {
		setIconImage(Toolkit.getDefaultToolkit().getImage(AdminCreaActividad.class.getResource("/img/icon.jpg")));
		setResizable(false);
		setModal(true);
		c = new Comprobaciones();
		poli = ra.getPolideportivo();
		
		setTitle("Creaci\u00F3n de una Actividad");
		setBounds(100, 100, 450, 204);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		contentPanel.add(getLblNewLabel());
		contentPanel.add(getTextNombreActividad());
		contentPanel.add(getCheckPlazasLimitadas());
		contentPanel.add(getLabelPlazas());
		contentPanel.add(getTextPlazas());
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton botonCreacion = new JButton("Crear actividad");
				botonCreacion.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if(comprobarCampos() == true){
							try {
								añadirActividad();
							} catch (SQLException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
					}
				});
				botonCreacion.setFont(new Font("Dialog", Font.BOLD, 12));
				botonCreacion.setActionCommand("OK");
				buttonPane.add(botonCreacion);
				getRootPane().setDefaultButton(botonCreacion);
			}
			{
				JButton cancelButton = new JButton("Cancelar");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						dispose();
					}
				});
				cancelButton.setFont(new Font("Dialog", Font.BOLD, 12));
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
	private JLabel getLblNewLabel() {
		if (lblNewLabel == null) {
			lblNewLabel = new JLabel("Nombre:");
			lblNewLabel.setFont(new Font("Dialog", Font.BOLD, 12));
			lblNewLabel.setBounds(10, 11, 64, 20);
		}
		return lblNewLabel;
	}
	private JTextField getTextNombreActividad() {
		if (textNombreActividad == null) {
			textNombreActividad = new JTextField();
			textNombreActividad.setBounds(71, 12, 270, 20);
			textNombreActividad.setColumns(10);
		}
		return textNombreActividad;
	}
	private JCheckBox getCheckPlazasLimitadas() {
		if (checkPlazasLimitadas == null) {
			checkPlazasLimitadas = new JCheckBox("Plazas Limitadas");
			checkPlazasLimitadas.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(checkPlazasLimitadas.isSelected()==true){
						labelPlazas.setEnabled(true);
						textPlazas.setEnabled(true);
					}
					else{
						labelPlazas.setEnabled(false);
						textPlazas.setEnabled(false);
					}
				}
			});
			checkPlazasLimitadas.setFont(new Font("Dialog", Font.BOLD, 12));
			checkPlazasLimitadas.setBounds(10, 48, 128, 23);
		}
		return checkPlazasLimitadas;
	}
	private JLabel getLabelPlazas() {
		if (labelPlazas == null) {
			labelPlazas = new JLabel("N\u00BA de plazas:");
			labelPlazas.setEnabled(false);
			labelPlazas.setFont(new Font("Dialog", Font.BOLD, 12));
			labelPlazas.setBounds(10, 90, 81, 20);
		}
		return labelPlazas;
	}
	private JTextField getTextPlazas() {
		if (textPlazas == null) {
			textPlazas = new JTextField();
			textPlazas.setEnabled(false);
			textPlazas.setFont(new Font("Dialog", Font.BOLD, 12));
			textPlazas.setBounds(101, 91, 86, 20);
			textPlazas.setColumns(10);
		}
		return textPlazas;
	}
	
	
	
	
	
	/**
	 * Comprobamos que los campos de texto sean correctos
	 * @return
	 */
	private boolean comprobarCampos(){
		if(c.comprobarTexto(textNombreActividad.getText()) == false){
			JOptionPane.showMessageDialog(null, "Nombre de actividad incorrecto");
			return false;
		}
		else if(c.comprobarCamposTextoVacios(textNombreActividad.getText()) == false){
			JOptionPane.showMessageDialog(null, "Campos de texto vacíos");
			return false;
		}
		else{
			if(checkPlazasLimitadas.isSelected() == true){
				if(c.comprobarCamposTextoVacios(textPlazas.getText()) == false){
					JOptionPane.showMessageDialog(null, "Campos de texto vacíos");
					return false;
				}
				else if(c.comprobarPlazas(textPlazas.getText().toString()) == false){
					JOptionPane.showMessageDialog(null, "El nº de plazas indicado no es correcto.");
					return false;
				}
			}
		}		
		return true;		
	}
	
	private void añadirActividad() throws SQLException{
		String nombre = textNombreActividad.getText().toString();
		if(comprobarActividadExistente(textNombreActividad.getText()) == true){
			if(checkPlazasLimitadas.isSelected() == true){
				int numeroPlazas = Integer.parseInt(textPlazas.getText());
				poli.getConexion().crearActividad(nombre, numeroPlazas, "True");
			}
			else{
				poli.getConexion().crearActividad(nombre, 15, "False");
			}
			JOptionPane.showMessageDialog(null, "Actividad creada con éxito");
		}
		else{
			JOptionPane.showMessageDialog(null, "La actividad que ha escogido ya se encuentra entre las existentes.");
		}
	}
	
	/**
	 * Compruebo que la actividad no estécreada.
	 * @param nombre
	 * @return
	 */
	private boolean comprobarActividadExistente(String nombre){
		String a = nombre.toLowerCase();
		String b = "";
		for(int i=0; i<poli.getActividades().size(); i++){
			b = poli.getActividades().get(i).getNombreActividad().toLowerCase();
			if(a.equals(b)){
				return false;
			}
		}
		return true;
	}
	
}
