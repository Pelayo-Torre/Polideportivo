package igu;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import logica.Cliente;
import logica.Polideportivo;
import logica.Socio;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.Arrays;
import java.awt.event.ActionEvent;
import javax.swing.JPasswordField;

public class IniciarSesion extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField textFieldUsuario;
	private Polideportivo polideportivo;
	private JPasswordField textFieldClave;


	/**
	 * Create the dialog.
	 * @param ventanaPrincipal 
	 * @throws SQLException 
	 */
	public IniciarSesion(VentanaPrincipal ventanaPrincipal) throws SQLException {
		setIconImage(Toolkit.getDefaultToolkit().getImage(ReservaInstalacionAdmVentana.class.getResource("/img/icon.jpg")));
		setModal(true);
		polideportivo=ventanaPrincipal.getPolideportivo();
		setTitle("Polideportivo: Inicio de sesi\u00F3n");
		setResizable(false);
		setBounds(100, 100, 291, 181);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JLabel lblUsuario = new JLabel("Usuario: ");
			lblUsuario.setBounds(10, 48, 58, 14);
			contentPanel.add(lblUsuario);
		}
		{
			textFieldUsuario = new JTextField();
			textFieldUsuario.setBounds(92, 45, 86, 20);
			contentPanel.add(textFieldUsuario);
			textFieldUsuario.setColumns(10);
		}
		{
			JLabel lblContrasea = new JLabel("Contrase\u00F1a: ");
			lblContrasea.setBounds(10, 78, 72, 14);
			contentPanel.add(lblContrasea);
		}
		
		JLabel lblIntroduzcaSusDatos = new JLabel("Introduzca sus datos: ");
		lblIntroduzcaSusDatos.setBounds(10, 20, 154, 14);
		contentPanel.add(lblIntroduzcaSusDatos);
		contentPanel.add(getTextFieldClave());
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if(isPasswordCorrect(getTextFieldClave().getPassword(), textFieldUsuario.getText())) {
							if(textFieldUsuario.getText().equals("admin")){
									abrirVentanaAdminPrincipalUltimate();					
							}
							else if(textFieldUsuario.getText().equals("monitor")){
								abrirVentanaPrincipalMonitor();
							}
							else
								abrirVentanaSocio();
						}
						else {
							JOptionPane.showMessageDialog(null, "Contraseña o usuario incorrectos");
						}
						
					}

				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
	private JPasswordField getTextFieldClave() {
		if (textFieldClave == null) {
			textFieldClave = new JPasswordField();
			textFieldClave.setBounds(92, 76, 86, 20);
		}
		return textFieldClave;
	}
	
	private boolean isPasswordCorrect(char[] input,String user) {
		boolean isCorrect = true;
		char[] correctPassword = null;
			for(Cliente c: polideportivo.getClientes()) {
				if(c instanceof Socio) {
					if(((Socio) c).getUsuario().equals(user)) 
						correctPassword=((Socio) c).getContraseña().toCharArray();
				}
			}
		if(correctPassword==null)
			return false;
	    if (input.length != correctPassword.length) {
	        isCorrect = false;
	    } else {
	        isCorrect = Arrays.equals (input, correctPassword);
	    }

	   
	    Arrays.fill(correctPassword,'0');

	    return isCorrect;
	}

	private void abrirVentanaSocio() {
		SocioVentana vs= new SocioVentana(polideportivo,textFieldUsuario.getText() );
		vs.setLocationRelativeTo(this);
		dispose();
		vs.setVisible(true);
		
	}
	
	private void abrirVentanaAdminPrincipalUltimate() {
		VentanaAdminPrincipalUltimate vh;							
		vh = new VentanaAdminPrincipalUltimate(polideportivo);
		vh.setLocationRelativeTo(this);
		dispose();
		vh.setVisible(true);					
		
	}
	
	private void abrirVentanaPrincipalMonitor() {
		MonitorVentana vh;							
		vh = new MonitorVentana(polideportivo);
		vh.setLocationRelativeTo(this);
		vh.setModal(true);
		dispose();
		vh.setVisible(true);					
		
	}
}
