package igu;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ElegirBorrarPeriodicaPuntualVentana extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	@SuppressWarnings("unused")
	private CancelarReservaCentroAdm crca;
	@SuppressWarnings("unused")
	private int fila;

	/**
	 * Create the dialog.
	 */
	public ElegirBorrarPeriodicaPuntualVentana(CancelarReservaCentroAdm crca, int fila) {
		ElegirBorrarPeriodicaPuntualVentana ebpp =this;
		this.crca=crca;
		setTitle("Polideportivo: Borrado de peri\u00F3dicas");
		setIconImage(Toolkit.getDefaultToolkit().getImage(ElegirBorrarPeriodicaPuntualVentana.class.getResource("/img/icon.jpg")));
		setResizable(false);
		setBounds(100, 100, 653, 147);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setLayout(new FlowLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		{
			JLabel lblHaSeleccionadoUna = new JLabel("Ha seleccionado una actividad peri\u00F3dica.");
			contentPanel.add(lblHaSeleccionadoUna);
		}
		{
			JLabel lbldeseaBorrarTodas = new JLabel("\u00BFDesea borrar todas las reservas asociadas a ella o solamente la reserva seleccionada?");
			contentPanel.add(lbldeseaBorrarTodas);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JPanel panel = new JPanel();
				buttonPane.add(panel);
				{
					JButton btnTodas = new JButton("Todas");
					panel.add(btnTodas);
					{
						JButton btnUna = new JButton("S\u00F3lo esa");
						panel.add(btnUna);
						btnUna.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								crca.borrarReservaPuntual(fila);
								JOptionPane.showMessageDialog(ebpp, "La reserva se ha borrado con éxito");
								dispose();
							}
						});
					}
					btnTodas.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							crca.borrarReservaPeriodica(fila);
							JOptionPane.showMessageDialog(ebpp, "Las reserva se han borrado con éxito");
							dispose();
						}
					});
				}
			}
			{
				JButton cancelButton = new JButton("Cancelar");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}

}
