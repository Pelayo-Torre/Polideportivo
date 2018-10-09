package igu;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class RendererTablaHorarios extends DefaultTableCellRenderer{

	private static final long serialVersionUID = 1L;

	@Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
       Component c=super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
	   if(table.getModel().getValueAt(row, column).toString().equals("Socio"))
    	   c.setBackground(Color.GREEN);
	   else if(table.getModel().getValueAt(row, column).toString().equals("No Socio"))
    	   c.setBackground(Color.CYAN);
	   else if(table.getModel().getValueAt(row, column).toString().equals("Centro") || table.getModel().getValueAt(row, column).toString().equals("NO DISPONIBLE"))
    	   c.setBackground(Color.YELLOW);
	   else if(table.getModel().getValueAt(row, column).toString().equals(""))
    	   c.setBackground(Color.WHITE);
       else 
    	  c.setBackground(Color.RED);
        return this;
    }

}
