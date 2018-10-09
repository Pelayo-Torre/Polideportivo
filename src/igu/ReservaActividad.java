package igu;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import logica.Actividad;
import logica.Polideportivo;

import javax.swing.JTextPane;
import java.awt.SystemColor;
import java.awt.Toolkit;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JDialog;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.FlowLayout;
import javax.swing.BoxLayout;
import java.awt.Font;

@SuppressWarnings("unused")
public class ReservaActividad extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JPanel pnBotones;
	private JPanel pnTexto;
	private JTextPane txtpnTextoInstruccion;
	private Polideportivo poli;
	private JButton btnNuevaActividad;
	private JComboBox<Actividad> comboBoxActividades;
	private JButton btnReservar;
	private JButton btnCancelar;
	private JPanel panel;
	private JLabel lblActividad;
	private final ButtonGroup buttonGroup = new ButtonGroup();

	/**
	 * Create the frame.
	 */
	public ReservaActividad(VentanaAdminPrincipalUltimate ventanaAdminPrincipalUltimate) {
		setIconImage(Toolkit.getDefaultToolkit().getImage(ReservaInstalacionAdmVentana.class.getResource("/img/icon.jpg")));
		setTitle("Poliportivo: Reserva de Actividades");
		poli = ventanaAdminPrincipalUltimate.getPolideportivo();
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 526, 228);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		contentPane.add(getPnBotones(), BorderLayout.CENTER);
		contentPane.add(getPnTexto(), BorderLayout.NORTH);
		contentPane.add(getPanel(), BorderLayout.SOUTH);
	}
	
	private DefaultComboBoxModel<Actividad> llenarComboActividades(DefaultComboBoxModel<Actividad> model){
		for(Actividad a: poli.getActividades())
			model.addElement(a);
		return model;
	}

	
	private void abrirVentanaReservaActividadPeriodica() {
		ReservaActividadPeriodica vrs= new ReservaActividadPeriodica(this, (Actividad)getComboBoxActividades().getSelectedItem());
		vrs.setLocationRelativeTo(this);
		vrs.setModal(true);
		vrs.setVisible(true);
	}
	
	private void abrirVentanaCrearActividad(){
		AdminCreaActividad aca = new AdminCreaActividad(this);
		aca.setLocationRelativeTo(this);
		aca.setModal(true);
		aca.setVisible(true);
	}
	
	private JPanel getPnBotones() {
		if (pnBotones == null) {
			pnBotones = new JPanel();
			pnBotones.setLayout(null);
			pnBotones.add(getBtnNuevaActividad());
			pnBotones.add(getComboBoxActividades());
			pnBotones.add(getLblActividad());
		}
		return pnBotones;
	}
	private JPanel getPnTexto() {
		if (pnTexto == null) {
			pnTexto = new JPanel();
			pnTexto.setLayout(new BoxLayout(pnTexto, BoxLayout.X_AXIS));
			pnTexto.add(getTxtpnTextoInstruccion());
		}
		return pnTexto;
	}
	private JTextPane getTxtpnTextoInstruccion() {
		if (txtpnTextoInstruccion == null) {
			txtpnTextoInstruccion = new JTextPane();
			txtpnTextoInstruccion.setFont(new Font("Tahoma", Font.PLAIN, 15));
			txtpnTextoInstruccion.setEditable(false);
			txtpnTextoInstruccion.setText("         Seleccione una actividad:");
			txtpnTextoInstruccion.setBackground(SystemColor.menu);
		}
		return txtpnTextoInstruccion;
	}
	
	public Polideportivo getPolideportivo() {
		return poli;
	}
	private JButton getBtnNuevaActividad() {
		if (btnNuevaActividad == null) {
			btnNuevaActividad = new JButton("Nueva");
			btnNuevaActividad.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					abrirVentanaCrearActividad();
				}
			});
			btnNuevaActividad.setFont(new Font("Tahoma", Font.PLAIN, 15));
			btnNuevaActividad.setBounds(345, 12, 95, 30);
		}
		return btnNuevaActividad;
	}
	private JComboBox<Actividad> getComboBoxActividades() {
		if (comboBoxActividades == null) {
			comboBoxActividades = new JComboBox<Actividad>();
			comboBoxActividades.setBounds(122, 12, 210, 30);
			DefaultComboBoxModel<Actividad> mdl = new DefaultComboBoxModel<Actividad>();
			mdl = llenarComboActividades(mdl);
			comboBoxActividades.setModel(mdl);
		}
		return comboBoxActividades;
	}
	private JButton getBtnReservar() {
		if (btnReservar == null) {
			btnReservar = new JButton("Reservar");
			btnReservar.setFont(new Font("Tahoma", Font.PLAIN, 15));
			btnReservar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(getComboBoxActividades().getItemCount() != 0) {
							abrirVentanaReservaActividadPeriodica();
					}
				}
			});
			btnReservar.setHorizontalAlignment(SwingConstants.RIGHT);
		}
		return btnReservar;
	}
	private JButton getBtnCancelar() {
		if (btnCancelar == null) {
			btnCancelar = new JButton("Cancelar");
			btnCancelar.setFont(new Font("Tahoma", Font.PLAIN, 15));
			btnCancelar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					dispose();
				}
			});
			btnCancelar.setHorizontalAlignment(SwingConstants.RIGHT);
		}
		return btnCancelar;
	}
	private JPanel getPanel() {
		if (panel == null) {
			panel = new JPanel();
			FlowLayout flowLayout = (FlowLayout) panel.getLayout();
			flowLayout.setAlignment(FlowLayout.RIGHT);
			panel.add(getBtnReservar());
			panel.add(getBtnCancelar());
		}
		return panel;
	}
	private JLabel getLblActividad() {
		if (lblActividad == null) {
			lblActividad = new JLabel("Actividad:");
			lblActividad.setFont(new Font("Tahoma", Font.PLAIN, 15));
			lblActividad.setBounds(48, 18, 62, 16);
		}
		return lblActividad;
	}
}
