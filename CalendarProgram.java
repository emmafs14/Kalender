package eksamenprojekt.kalender;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
// Kode er fra linje 10-209 er delvist inspireret fra https://javahungry.blogspot.com/2013/06/calendar-implementation-gui-based.html.
//Koden er blevet modificeret således den passer vores behov og projekt
public class CalendarProgram{
    static JLabel lblMåned, lblÅr;
    static JButton btnFør, btnNæste;
    static JTable tblKalender;
    static JComboBox cmbÅr;
    static JFrame frmMain;
    static Container pane;
    static DefaultTableModel mtbKalender; //Table model
    static JScrollPane stblKalender; //
    static JPanel pnlKalender;
    static int rigtigÅr, rigtigMåned, rigtigDag, nuværendeÅr, nuværendeMåned;
    private static DefaultTableModel mtblKalender;
    private static JScrollPane stblKaldener;
    
    public static void main (String args[]){
        try {UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());}
        catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {}
        
        
        //forbereder vores vindue
        frmMain = new JFrame ("Kalender Program"); //vi laver vinduet
        frmMain.setSize(500, 500); //vi afgører størrelsen af vinduet
        pane = frmMain.getContentPane();
        pane.setLayout(null);
        frmMain.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //gør vi kan lukke programmet når vi trykker kryds
        frmMain.setLocationRelativeTo(null); //centrere vinduet
        
        //opretter controls
        lblMåned = new JLabel ("Januar");
        lblÅr = new JLabel ("Skift År:");
        cmbÅr = new JComboBox();
        btnFør = new JButton ("<---");
        btnNæste = new JButton ("--->");
        mtblKalender = new DefaultTableModel(){public boolean isCellEditable(int rowIndex, int mColIndex){return false;}};
        tblKalender = new JTable(mtblKalender);
        stblKalender = new JScrollPane(tblKalender);
        pnlKalender = new JPanel(null);
        
        //sætter border
        pnlKalender.setBorder(BorderFactory.createTitledBorder("Kalender"));
        
       //Register action listeners
        btnFør.addActionListener(new btnPrev_Action());
        btnNæste.addActionListener(new btnNext_Action());
        cmbÅr.addActionListener(new cmbYear_Action());
        
      pane.add(pnlKalender);
        pnlKalender.add(lblMåned);
        pnlKalender.add(lblÅr);
        pnlKalender.add(cmbÅr);
        pnlKalender.add(btnFør);
        pnlKalender.add(btnNæste);
        pnlKalender.add(stblKalender);
        
        //størrelsesforhold på elem´enterne
        pnlKalender.setBounds(0, 0, 500, 600);
        lblMåned.setBounds(160-lblMåned.getPreferredSize().width/2, 25, 100, 25);
        lblÅr.setBounds(10, 305, 80, 20);
        cmbÅr.setBounds(230, 305, 80, 20);
        btnFør.setBounds(10, 25, 100, 25);
        btnNæste.setBounds(360, 25, 100, 25);
        stblKalender.setBounds(10, 50, 450, 400);
        
        //gør vinduet synligt
        frmMain.setResizable(false);
        frmMain.setVisible(true);
        
        //Få fat i den rigtige date
        GregorianCalendar cal = new GregorianCalendar(); //erklarer vores kalender
        rigtigDag = cal.get(GregorianCalendar.DAY_OF_MONTH); //får fat i vores dag
        rigtigMåned = cal.get(GregorianCalendar.MONTH); //får fat i vores måned
        rigtigÅr = cal.get(GregorianCalendar.YEAR); //Får fat i vores år
        nuværendeMåned = rigtigMåned; //får fat i vores nuværrende måned og år
        nuværendeÅr = rigtigÅr;
        
        //Ugedagene tilføjes
        String[] headers = {"søn", "man", "tir", "ons", "tor", "fre","lør"};
        for (int i=0; i<7; i++){
            mtblKalender.addColumn(headers[i]);
        }
        
        tblKalender.getParent().setBackground(tblKalender.getBackground()); //Set background
        
    
        tblKalender.getTableHeader().setResizingAllowed(false);
        tblKalender.getTableHeader().setReorderingAllowed(false);
        
 
        tblKalender.setColumnSelectionAllowed(true);
        tblKalender.setRowSelectionAllowed(true);
        tblKalender.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        //Set row/column count
        tblKalender.setRowHeight(38);
        mtblKalender.setColumnCount(7);
        mtblKalender.setRowCount(6);
        
        //Populate table
        for (int i=rigtigÅr-100; i<=rigtigÅr+100; i++){
            cmbÅr.addItem(String.valueOf(i));
        }
        
        //Refresh calendar
        refreshCalendar (rigtigMåned, rigtigÅr); //Refresh calendar
    }
    
    public static void refreshCalendar(int måned, int år){
        //Variables
        String[] måneder =  {"Januar", "Februar", "Marts", "April", "Maj", "Juni", "Juli", "August", "September", "Oktober", "November", "December"};
        int nod, som; //antal af dage, starten på månederne
        
        //Allow/disallow buttons
        btnFør.setEnabled(true);
        btnNæste.setEnabled(true);
        if (måned == 0 && år <= rigtigÅr-10){btnFør.setEnabled(false);} //Too early
        if (måned == 11 && år >= rigtigÅr+100){btnNæste.setEnabled(false);} //Too late
        lblMåned.setText(måneder[måned]); //Refresh the month label (at the top)
        lblMåned.setBounds(220-lblMåned.getPreferredSize().width/50, 25, 90, 25); //Re-align label with calendar
        cmbÅr.setSelectedItem(String.valueOf(år)); //Select the correct year in the combo box
        
        //Clear table
        for (int i=0; i<6; i++){
            for (int j=0; j<7; j++){
                mtblKalender.setValueAt(null, i, j);
            }
        }
        
        //Get first day of month and number of days
        GregorianCalendar cal = new GregorianCalendar(år, måned, 1);
        nod = cal.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
        som = cal.get(GregorianCalendar.DAY_OF_WEEK);
        
        //Draw calendar
        for (int i=1; i<=nod; i++){
            int row = (i+som-2)/7;
            int column  =  (i+som-2)%7;
            mtblKalender.setValueAt(i, row, column);
        }
        
        tblKalender.setDefaultRenderer(tblKalender.getColumnClass(0), new tblCalendarRenderer());
    }
    
    static class tblCalendarRenderer extends DefaultTableCellRenderer{
        @Override
        public Component getTableCellRendererComponent (JTable table, Object value, boolean selected, boolean focused, int row, int column){
            super.getTableCellRendererComponent(table, value, selected, focused, row, column);
            if (column == 0 || column == 6){ //weekend
                setBackground(new Color(255, 220, 220));
            }
            else{ //Uge
                setBackground(new Color(255, 255, 255));
            }
            if (value != null){
                if (Integer.parseInt(value.toString()) == rigtigDag && nuværendeMåned == rigtigMåned && nuværendeÅr == rigtigÅr){ //Idag
                    setBackground(new Color(200,200,250));
                }
            }
            setBorder(null);
            setForeground(Color.black);
            return this;
        }
    }
    
    static class btnPrev_Action implements ActionListener{
        
        public void actionPerformed (ActionEvent e){
            if (nuværendeMåned == 0){ //et år tibage
                nuværendeMåned = 11;
                nuværendeÅr -= 1;
            }
            else{ //en måned tilbage
                nuværendeMåned -= 1;
            }
            refreshCalendar(nuværendeMåned, nuværendeÅr);
        }
    }
    static class btnNext_Action implements ActionListener{
        
        public void actionPerformed (ActionEvent e){
            if (nuværendeMåned == 11){ //et år frem
                nuværendeMåned = 0;
                nuværendeÅr += 1;
            }
            else{ //en måned frem
                nuværendeMåned += 1;
            }
            refreshCalendar(nuværendeMåned, nuværendeÅr);
        }
    }
    static class cmbYear_Action implements ActionListener{
        
        public void actionPerformed (ActionEvent e){
            if (cmbÅr.getSelectedItem() != null){
                String b = cmbÅr.getSelectedItem().toString();
                nuværendeÅr = Integer.parseInt(b);
                refreshCalendar(nuværendeMåned, nuværendeÅr);
            }
        }
    }
}