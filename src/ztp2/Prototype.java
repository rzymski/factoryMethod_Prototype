package ztp2;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.event.*;
import java.util.*;

class Database extends AbstractTableModel {
    private List<TableHeader> headers;
    private List<List<TableData>> data;

    public Database() {
        headers = new ArrayList<TableHeader>();
        data = new ArrayList<List<TableData>>();
    }
    public void addRow() {
        List<TableData> row = new ArrayList<TableData>();
        for(TableHeader col:headers) {
            row.add(col.createTable());// wywołanie metody używającej prototypu
        }
        data.add(row);
        fireTableStructureChanged();
    }
    public void addCol(TableHeader type) {
        headers.add(type);
        for(List<TableData> row:data) {
            row.add(type.createTable());// wywołanie metody używającej prototypu
        }
        fireTableStructureChanged();
    }

    public int getRowCount() { return data.size(); }
    public int getColumnCount() { return headers.size(); }
    public String getColumnName(int column) {
        return headers.get(column).toString();
    }
    public Object getValueAt(int row, int column) {
        return data.get(row).get(column);
    }
}

abstract class TableData implements Cloneable{
    final static Random rnd = new Random();
    @Override
    public TableData clone() {
        TableData obj = null;
        try {
            obj = (TableData) super.clone();
            obj.setType();
        } catch (CloneNotSupportedException ex) {
        }
        return obj;
    }
    protected abstract void setType();

}

class TableDataInt extends TableData
{
    private int data;
    public TableDataInt() { setType(); }
    public String toString() { return Integer.toString(data); }
    @Override
    protected void setType() {
        data = rnd.nextInt(100);
    }
}

class TableDataDouble extends TableData
{
    private double data;
    public TableDataDouble() { setType(); }
    public String toString() { return Double.toString(data); }
    @Override
    protected void setType() {
        data = rnd.nextInt(100) + rnd.nextInt(100)/100.0;
    }
}

class TableDataChar extends TableData
{
    private char data;
    public TableDataChar() { setType(); }
    public String toString() { return Character.toString(data); }
    @Override
    protected void setType() {
        data = (char)rnd.nextInt(65, 91);
    }
}

class TableDataBoolean extends TableData
{
    private boolean data;
    public TableDataBoolean() { setType(); }
    public String toString() { return Boolean.toString(data); }
    @Override
    protected void setType() {
        data = rnd.nextInt(2) > 0;
    }
}

//prothotype
class TableHeader
{
    private String type;
    private TableData tableData;
    public TableHeader(String type)
    {
        this.type = type;
        switch (type) {
            case "INT":
                tableData = new TableDataInt();
                break;
            case "DOUBLE":
                tableData = new TableDataDouble();
                break;
            case "CHAR":
                tableData = new TableDataChar();
                break;
            case "BOOLEAN":
                tableData = new TableDataBoolean();
                break;
            default:
                tableData = null;
        }
    }
    public String toString() { return type; }
    public TableData createTable()
    {
        return tableData.clone();
    }
}

public class Prototype {
    public static void main(String[] args) {
        final JFrame frame = new JFrame("Baza danych");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        final Database database = new Database();

        JTable table = new JTable(database);
        JMenuBar bar = new JMenuBar();

        JButton row = new JButton("Dodaj Wiersz");
        JButton col = new JButton("Dodaj Kolumnę");

        bar.add(row);
        bar.add(col);

        frame.add(new JScrollPane(table));
        frame.setJMenuBar(bar);

        frame.pack();
        frame.setVisible(true);

        row.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ev)
            {
                database.addRow();
            }
        });
        col.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ev)
            {
                Object option = JOptionPane.showInputDialog(
                        frame,
                        "Podaj typ kolumny",
                        "Dodaj Kolumnę",
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        /*new ztp2.TableHeader[] {
                                new TableHeaderInt(),
                                new TableHeaderDouble(),
                                new TableHeaderChar(),
                                new TableHeaderBoolean(),
                        }, null);*/
                        new TableHeader[] {
                                new TableHeader("INT"),
                                new TableHeader("DOUBLE"),
                                new TableHeader("CHAR"),
                                new TableHeader("BOOLEAN"),
                        }, null);
                if(option == null)
                    return;
                database.addCol((TableHeader)option);
            }
        });
    }
}