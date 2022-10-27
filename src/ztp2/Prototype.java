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
    protected abstract String getType();

}

class TableDataInt extends TableData
{
    private int data;
    public TableDataInt() { setType(); }
    @Override
    public TableData clone() {
        TableDataInt obj = null;
        obj = (TableDataInt) super.clone();
        obj.setType();
        return obj;
    }
    public String toString() { return Integer.toString(data); }
    @Override
    protected void setType() {
        data = rnd.nextInt(100);
    }
    @Override
    protected String getType(){ return "INT";}
}

class TableDataDouble extends TableData
{
    private double data;
    public TableDataDouble() { setType(); }
    @Override
    public TableData clone() {
        TableDataDouble obj = null;
        obj = (TableDataDouble) super.clone();
        obj.setType();
        return obj;
    }
    public String toString() { return Double.toString(data); }
    @Override
    protected void setType() {
        data = rnd.nextInt(100) + rnd.nextInt(100)/100.0;
    }
    @Override
    protected String getType(){ return "DOUBLE";}
}

class TableDataChar extends TableData
{
    private char data;
    public TableDataChar() { setType(); }
    @Override
    public TableData clone() {
        TableDataChar obj = null;
        obj = (TableDataChar) super.clone();
        obj.setType();
        return obj;
    }
    public String toString() { return Character.toString(data); }
    @Override
    protected void setType() {
        data = (char)rnd.nextInt(65, 91);
    }
    @Override
    protected String getType(){ return "CHAR";}
}

class TableDataBoolean extends TableData
{
    private boolean data;
    public TableDataBoolean() { setType(); }
    @Override
    public TableData clone() {
        TableDataBoolean obj = null;
        obj = (TableDataBoolean) super.clone();
        obj.setType();
        return obj;
    }
    public String toString() { return Boolean.toString(data); }
    @Override
    protected void setType() {
        data = rnd.nextInt(2) > 0;
    }
    @Override
    protected String getType(){ return "BOOLEAN";}
}

//prothotype
class TableHeader
{
    private String type;
    private TableData data;
    public TableHeader(TableData tableData)
    {
        data = tableData.clone();
        this.type = data.getType();
    }
    public String toString() { return type; }
    public TableData createTable()
    {
        return data.clone();
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

//        TableData tableInt = new TableDataInt();
//        TableData tableDouble = new TableDataDouble();
//        TableData tableChar = new TableDataChar();
//        TableData tableBoolean = new TableDataBoolean();

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
                        new TableHeader[] {
                                new TableHeader(new TableDataInt()),
                                new TableHeader(new TableDataDouble()),
                                new TableHeader(new TableDataChar()),
                                new TableHeader(new TableDataBoolean()),
                        }, null);
//                        new TableHeader[] {
//                                new TableHeader(tableInt),
//                                new TableHeader(tableDouble),
//                                new TableHeader(tableChar),
//                                new TableHeader(tableBoolean),
//                        }, null);
                if(option == null)
                    return;
                database.addCol((TableHeader)option);
            }
        });
    }
}