package igu;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

//import org.jvnet.substance.SubstanceLookAndFeel;

import logica.Polideportivo;

import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class VentanaPrincipal extends JFrame {

	private JPanel contentPane;
	private JPanel panelNorte;
	private JLabel label;
	private JPanel panelCentro;
	private JButton btnIniciarSesin;
	private Polideportivo polideportivo;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VentanaPrincipal frame = new VentanaPrincipal();
//					JFrame.setDefaultLookAndFeelDecorated(true);
//					JDialog.setDefaultLookAndFeelDecorated(true);
//					SubstanceLookAndFeel.setSkin("org.jvnet.substance.skin.MistSilverSkin");
					frame.setVisible(true);
					frame.setLocationRelativeTo(null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 * @throws SQLException 
	 */
	public VentanaPrincipal() throws SQLException {
		setIconImage(Toolkit.getDefaultToolkit().getImage(ReservaInstalacionAdmVentana.class.getResource("/img/icon.jpg")));
		polideportivo=new Polideportivo();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Polideportivo");
		setBounds(100, 100, 840, 421);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		contentPane.add(getPanelNorte(), BorderLayout.NORTH);
		contentPane.add(getPanelCentro(), BorderLayout.CENTER);
	}

	private JPanel getPanelNorte() {
		if (panelNorte == null) {
			panelNorte = new JPanel();
			panelNorte.add(getLabel());
		}
		return panelNorte;
	}
	private JLabel getLabel() {
		if (label == null) {
			label = new JLabel("");
			label.setIcon(new ImageIcon(VentanaPrincipal.class.getResource("/img/polideportivo-2.jpg")));
		}
		return label;
	}
	private JPanel getPanelCentro() {
		if (panelCentro == null) {
			panelCentro = new JPanel();
			panelCentro.add(getBtnIniciarSesin());
		}
		return panelCentro;
	}
	private JButton getBtnIniciarSesin() {
		if (btnIniciarSesin == null) {
			btnIniciarSesin = new JButton("Iniciar Sesi\u00F3n");
			btnIniciarSesin.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						mostrarVentanaIniciarSesion();
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				}
			});
		}
		return btnIniciarSesin;
	}
	
	private void mostrarVentanaIniciarSesion() throws SQLException{
		IniciarSesion is = new IniciarSesion(this);
		is.setLocationRelativeTo(this);
		is.setVisible(true);
		
	}
	
	public Polideportivo getPolideportivo() {
		return polideportivo;
	}
}
