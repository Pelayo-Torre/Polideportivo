package igu;

import java.awt.Component;
import java.awt.Font;
import java.awt.Image;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import logica.Instalacion;

@SuppressWarnings("serial")
public class RendererFiltradoReserva extends DefaultTableCellRenderer{
	
	/**
	 * Método para limpiar la tabla.
	 * @param table
	 */
	public void limpiar(JTable table){
		try{
			DefaultTableModel modelo = (DefaultTableModel) table.getModel();
			int f = table.getRowCount();
			for(int i=0; f>i; i++)
			{
				modelo.removeRow(0);
			}
		}catch(Exception e){
			
		}
	}
	
	/**
	 * Método para añadir las instalaciones a la tabla para que las vea el socio
	 * @param tabla
	 * @param instalaciones
	 * @param ventanaSocioHaceReserva
	 */
	@SuppressWarnings("unused")
	public void añadirInstalaciones(JTable tabla, ArrayList<Instalacion>instalaciones, SocioVentana ventanaSocioHaceReserva){
		Object [] fila = new Object[3];
		DefaultTableCellRenderer cell = new DefaultTableCellRenderer();
		instalaciones = aux(instalaciones);
	
		for(int i=0; i<instalaciones.size();i++)
		{
			ImageIcon foto = new ImageIcon(SocioVentana.class.getResource("/img/"+instalaciones.get(i).getTipo()+".jpg"));
			Image img = foto.getImage();
			Image img2 = img.getScaledInstance(240, 235, Image.SCALE_SMOOTH);
			ImageIcon foto2 = new ImageIcon(img2);
			JLabel lbFoto = new JLabel();
			lbFoto.setIcon(foto2);
			lbFoto.setHorizontalAlignment(SwingConstants.CENTER);
			
			JTextPane pane = new JTextPane();
			pane.setText(instalaciones.get(i).getDescripcion());
			pane.setFont(new Font("Dialog", Font.BOLD, 16));
			
			fila[0] = lbFoto;
			fila[1] = pane;
			fila[2] = instalaciones.get(i).getTipo();
			
			((DefaultTableModel)tabla.getModel()).addRow(fila);	
			tabla.setRowHeight(i, 240);
			
			
//			//Para Centrar el texto en la tabla
//			for(int j=0; j<tabla.getColumnCount(); j++)
//			{
//				DefaultTableColumnModel columnaModelo = (DefaultTableColumnModel) tabla.getColumnModel();
//				TableColumn columna = columnaModelo.getColumn(j);
//				TableCellRenderer r = columna.getHeaderRenderer();
//				int var = 0;
//				for(int k=0; k<tabla.getRowCount(); k++)
//				{
//					r = tabla.getCellRenderer(k, j);
//					Component componente = r.getTableCellRendererComponent(tabla, tabla.getValueAt(k, j), false, false, k, j);
//					var = Math.max(var, componente.getPreferredSize().width);
//				}
//				columna.setPreferredWidth(var+2);
//			}	
		}		
	}
	
	/**
	 * Método para quitar reservas repetidas
	 */
	private ArrayList<Instalacion> aux(ArrayList<Instalacion> instalacion){
		ArrayList<Instalacion> lista = new ArrayList<Instalacion>();
		boolean c = false;
		for(Instalacion a : instalacion){
			c = false;
			for(Instalacion b : lista){
				if(a.getTipo().equals(b.getTipo())){
					c = true;
				}
			}
			if(!c){
				lista.add(a);
			}
		}
		return lista;
	}
	
	
	/**
	 * Método para insertar en la tabla imágenes botones...
	 */
	@Override
	public Component getTableCellRendererComponent(JTable tabla, Object objeto, boolean art, boolean art2, int i, int j){
		//Comprobamos que el objeto pasado como parámetro es una instancia de una JLabel para añadir la imagen en la tabla
		if(objeto instanceof JLabel)
		{
			JLabel etiqueta = (JLabel) objeto;
			return etiqueta;
		}
		if(objeto instanceof JButton)
		{
			JButton boton = (JButton) objeto;
			return boton;
		}
		if(objeto instanceof JTextPane)
		{
			JTextPane pane = (JTextPane) objeto;
			return pane;
		}
		if(objeto instanceof JSpinner)
		{
			JSpinner spinner = (JSpinner) objeto;
			return spinner;
		}
		return super.getTableCellRendererComponent(tabla, objeto, art, art2, i, j);
	}
	
	
	

}
