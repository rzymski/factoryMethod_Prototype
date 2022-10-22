package ztp1;

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
            row.add(col.createTable());// wywołanie metody fabrykującej
        }
        data.add(row);
        fireTableStructureChanged();
    }
    public void addCol(TableHeader type) {
        headers.add(type);
        for(List<TableData> row:data) {
            row.add(type.createTable());// wywołanie metody fabrykującej
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

interface TableData {
    final static Random rnd = new Random();
}

class TableDataInt implements TableData
{
    private int data;
    public TableDataInt() { data = rnd.nextInt(100); }
    public String toString() { return Integer.toString(data); }
}

class TableDataDouble implements TableData
{
    private double data;
    public TableDataDouble() { data = rnd.nextInt(100) + rnd.nextInt(100)/100.0; }
    public String toString() { return Double.toString(data); }
}

class TableDataChar implements TableData
{
    private char data;
    public TableDataChar() { data = (char)rnd.nextInt(65, 91); }
    public String toString() { return Character.toString(data); }
}

class TableDataBoolean implements TableData
{
    private boolean data;
    public TableDataBoolean() { data = rnd.nextInt(2) > 0; }
    public String toString() { return Boolean.toString(data); }
}

//factory method with parametr
class TableHeader
{
    private String type;
    public TableHeader(String type) { this.type = type; }
    public String toString() { return type; }
    public TableData createTable()
    {
        if(type.equals("INT")) return new TableDataInt();
        if(type.equals("DOUBLE")) return new TableDataDouble();
        if(type.equals("CHAR")) return new TableDataChar();
        if(type.equals("BOOLEAN")) return new TableDataBoolean();
        return null;
    }
}

public class FactoryMethod {
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
                        /*new ztp1.TableHeader[] {
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

/*abstract class TableHeader
{
    public abstract ztp1.TableData createTable();
}

class TableHeaderInt extends TableHeader
{
    public ztp1.TableData createTable() { return new ztp1.TableDataInt(); }

    public String toString() {
        return "INT";
    }
}

class TableHeaderDouble extends TableHeader
{
    public ztp1.TableData createTable() { return new ztp1.TableDataDouble(); }

    public String toString() {
        return "DOUBLE";
    }
}

class TableHeaderChar extends TableHeader
{
    public ztp1.TableData createTable() { return new ztp1.TableDataChar(); }

    public String toString() {
        return "CHAR";
    }
}

class TableHeaderBoolean extends TableHeader
{
    public ztp1.TableData createTable() { return new ztp1.TableDataBoolean(); }

    public String toString() {
        return "BOOLEAN";
    }
}*/
