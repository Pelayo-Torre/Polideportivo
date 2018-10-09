package igu;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import logica.Cuota;
import logica.Polideportivo;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class LiquidarFacturasVentana extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JLabel lblLasCuotasHan;
	private JScrollPane scrollPane;
	private JTable table;
	private ModeloNoEditable modeloTabla;
	private String idCuota;
	private Polideportivo polideportivo;


	/**
	 * Create the dialog.
	 */
	public LiquidarFacturasVentana(String idCuota, Polideportivo poli) {
		this.polideportivo=poli;
		this.idCuota=idCuota;
		setTitle("Polideportivo: Liquidar facturas");
		setIconImage(Toolkit.getDefaultToolkit().getImage(LiquidarFacturasVentana.class.getResource("/img/icon.jpg")));
		setBounds(100, 100, 664, 286);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		contentPanel.add(getLblLasCuotasHan());
		contentPanel.add(getScrollPane());
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton aceptarButton = new JButton("Aceptar");
				aceptarButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
				aceptarButton.setActionCommand("");
				buttonPane.add(aceptarButton);
			}
		}
	}
	private JLabel getLblLasCuotasHan() {
		if (lblLasCuotasHan == null) {
			lblLasCuotasHan = new JLabel("Las cuotas han sido actualizadas");
			lblLasCuotasHan.setBounds(242, 11, 193, 27);
		}
		return lblLasCuotasHan;
	}
	private JScrollPane getScrollPane() {
		if (scrollPane == null) {
			scrollPane = new JScrollPane();
			scrollPane.setBounds(10, 49, 628, 154);
			scrollPane.setViewportView(getTable());
		}
		return scrollPane;
	}
	private JTable getTable() {
		if (table == null) {
			añadirDatosATabla();
			table = new JTable(modeloTabla);
			table.setRowSelectionAllowed(false);
		}
		return table;
	}
	
	private void añadirDatosATabla() {
		String[] nColumnas = {"Nombre","Apellidos","NºSocio","DNI","Importe"};
		modeloTabla= new ModeloNoEditable(nColumnas,0);
		String[] fila = new String[5];
		for(Cuota cuota : polideportivo.getCuotas()) {
			if(cuota.getId_cuota().equals(idCuota)) {
				fila[0] = cuota.getSocio().getNombre();
				fila[1] = cuota.getSocio().getApellidos();
				fila[2] = String.valueOf(cuota.getSocio().getNumeroSocio());
				fila[3] = cuota.getSocio().getDNI();
				fila[4] = String.valueOf(cuota.getImporte());
				modeloTabla.addRow(fila);
			}
		}
		
	}
}
