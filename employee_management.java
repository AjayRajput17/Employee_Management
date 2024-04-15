import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.JTableHeader;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;

class InsertFrame extends JInternalFrame implements ActionListener {
    private JLabel lbl;
    private JLabel lbl_message;
    private JTextField[] txt;
    private JButton b;

    public InsertFrame() {
        super("Add New Employee", true, true, true, true);
        this.setLayout(new GridLayout(6, 2));
        String[] var1 = new String[] { "Employee Number", "Employee Name", "Job", "Salary" };
        this.txt = new JTextField[4];

        for (int var2 = 0; var2 < this.txt.length; ++var2) {
            this.lbl = new JLabel(var1[var2]);
            this.add(this.lbl);
            this.txt[var2] = new JTextField();
            this.add(this.txt[var2]);
        }

        String[] var4 = new String[] { "Insert", "Clear" };

        for (int var3 = 0; var3 < var4.length; ++var3) {
            this.b = new JButton(var4[var3]);
            this.add(this.b);
            this.b.addActionListener(this);
        }

        this.lbl_message = new JLabel(".....");
        this.add(this.lbl_message);
        this.setVisible(true);
        this.setSize(300, 250);
    }

    public void actionPerformed(ActionEvent var1) {
        String var2 = var1.getActionCommand();
        int var3;
        if (var2.equalsIgnoreCase("Insert")) {
            try {
                var3 = Integer.parseInt(this.txt[0].getText());
                String var4 = this.txt[1].getText().toUpperCase();
                String var5 = this.txt[2].getText().toUpperCase();
                double var6 = Double.parseDouble(this.txt[3].getText());
                String var8 = "insert into employee values(?,?,?,?)";
                Class.forName("com.mysql.cj.jdbc.Driver");
                String var9 = "jdbc:mysql://localhost:3306/employeedb";
                String var10 = "root";
                String var11 = "Ajay1718";
                Connection var12 = DriverManager.getConnection(var9, var10, var11);
                PreparedStatement var13 = var12.prepareStatement(var8);
                var13.setInt(1, var3);
                var13.setString(2, var4);
                var13.setString(3, var5);
                var13.setDouble(4, var6);
                int var14 = var13.executeUpdate();
                var13.close();
                var12.close();
                if (var14 == 1) {
                    this.lbl_message.setForeground(Color.BLUE);
                    this.lbl_message.setText("Record Inserted");
                } else {
                    this.lbl_message.setForeground(Color.RED);
                    this.lbl.setText("Record not Inserted");
                }
            } catch (Exception var15) {
                this.lbl_message.setText(var15.getMessage());
                var15.printStackTrace();
            }
        } else if (var2.equalsIgnoreCase("Clear")) {
            for (var3 = 0; var3 < this.txt.length; ++var3) {
                this.txt[var3].setText("");
            }

            this.lbl_message.setText("");
        }

    }
}

class SearchUpdateDeleteFrame extends JInternalFrame implements ActionListener, ListSelectionListener {
    private JSplitPane split_pane;
    private JPanel panel1, panel2;
    private JLabel lbl, lbl_message;
    private JTextField txt_search, txt[];
    private JList<String> emp_list;
    private JScrollPane jsp;
    private JButton b[];
    private Connection con;

    public SearchUpdateDeleteFrame() {
        super("Search Update Delete ", true, true, true, true);

        // panel-1
        panel1 = new JPanel();
        panel1.setLayout(null);

        lbl = new JLabel("Enter Employee Name To Search");
        lbl.setBounds(10, 10, 240, 30);
        panel1.add(lbl);

        txt_search = new JTextField();
        txt_search.setBounds(10, 45, 240, 30);
        panel1.add(txt_search);

        emp_list = new JList<String>();
        jsp = new JScrollPane(emp_list);
        jsp.setBounds(10, 80, 240, 150);
        panel1.add(jsp);
        getTableData();
        emp_list.addListSelectionListener(this);

        // panel-2
        panel2 = new JPanel();
        panel2.setLayout(new GridLayout(6, 2));

        String str[] = { "Employee Number", "Employee Name", "Job", "Salary" };
        txt = new JTextField[4];
        for (int i = 0; i < txt.length; i++) {

            lbl = new JLabel(str[i]);
            panel2.add(lbl);

            txt[i] = new JTextField();
            panel2.add(txt[i]);

        }
        txt[0].setEditable(false);

        String arr[] = { "Upadate", "Delete" };
        b = new JButton[2];

        for (int i = 0; i < arr.length; i++) {
            b[i] = new JButton(arr[i]);
            panel2.add(b[i]);
            b[i].addActionListener(this);
        }

        lbl_message = new JLabel("....");

        // split_pane
        split_pane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panel1, panel2);
        split_pane.setDividerLocation(250);
        split_pane.setDividerSize(5);
        this.add(split_pane);

        this.setSize(600, 300);
        this.setVisible(true);
    }

    public void connect() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");

        // create a connection
        String url = "jdbc:mysql://localhost:3306/employeedb";
        String user = "root";
        String pass = "Ajay1718";
        con = DriverManager.getConnection(url, user, pass);
    }

    public void disconnect() throws Exception {
        if (con != null && !con.isClosed()) {
            con.close();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            Object obj = e.getSource();

            if (obj == b[0]) {
                // Update
                int eno = Integer.parseInt(txt[0].getText());
                String enm = txt[1].getText().toUpperCase();
                String j = txt[2].getText().toUpperCase();
                double sal = Double.parseDouble(txt[3].getText());

                String sql = "update employee set ename=?,job=?,salary=? where empno=?";
                connect();
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setString(1, enm);
                ps.setString(2, j);
                ps.setDouble(3, sal);
                ps.setInt(4, eno);

                int n = ps.executeUpdate();
                ps.close();
                disconnect();

                if (n == 1) {
                    getTableData();
                    lbl_message.setText("Record Updated");
                    JOptionPane.showMessageDialog(this, "Record Updated");

                } else {
                    JOptionPane.showMessageDialog(this, "Record Not Updated");
                }
            } else if (obj == b[1]) {
                // Delete
                int eno = Integer.parseInt(txt[0].getText());
                String sql = "delete from employee where empno=?";
                connect();
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setInt(1, eno);
                int n = ps.executeUpdate();
                ps.close();
                disconnect();

                if (n == 1) {
                    getTableData();
                    for (int i = 0; i < txt.length; i++) {
                        txt[i].setText("");
                    }
                    lbl_message.setText("");
                    JOptionPane.showMessageDialog(this, "Record Deleted");
                } else {
                    JOptionPane.showMessageDialog(this, "Record Not Deleted");
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
            ex.printStackTrace();
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        try {
            String enm = emp_list.getSelectedValue();
            if (enm != null) {
                String sql = "select empno, job, salary from employee where ename=?";
                connect();
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setString(1, enm);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    int eno = rs.getInt("empno");
                    String j = rs.getString("job");
                    double sal = rs.getDouble("salary");

                    txt[0].setText(String.valueOf(eno));
                    txt[1].setText(enm);
                    txt[2].setText(j);
                    txt[3].setText(String.valueOf(sal));
                }
                rs.close();
                ps.close();
                disconnect();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void getTableData() {
        try {
            String sql = "select ename from employee";
            connect();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            Vector<String> v = new Vector<String>();

            while (rs.next()) {
                String enm = rs.getString("ename");
                v.add(enm);
            }
            rs.close();
            ps.close();
            disconnect();
            emp_list.setListData(v);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
            ex.printStackTrace();
        }
    }
}

// class ALLEmployeeFrame

class AllEmployeeFrame extends JInternalFrame {
    private JTable tbl;

    public AllEmployeeFrame() {
        super("All Employee", true, true, true, true);
        String cols[] = { "Employee Number", "Employee Name", "Job", "Salary" };
        Vector<String> cols_names = new Vector<String>();
        for (int i = 0; i < cols.length; i++) {
            cols_names.add(cols[i]);
        }

        Vector<Vector<String>> rows = new Vector<Vector<String>>();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            // create a connectionS
            String url = "jdbc:mysql://localhost:3306/employeedb";
            String user = "root";
            String pass = "Ajay1718";
            Connection con = DriverManager.getConnection(url, user, pass);

            String sql = "select * from employee";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int eno = rs.getInt("empno");
                String enm = rs.getString("ename");
                String job = rs.getString("job");
                Double sal = rs.getDouble("salary");

                Vector<String> single_row = new Vector<String>();
                single_row.add(eno + "");
                single_row.add(enm);
                single_row.add(job);
                single_row.add(sal + "");
                rows.add(single_row);
            }
            rs.close();
            ps.close();
            con.close();

            tbl = new JTable(rows, cols_names);
            JScrollPane jsp = JTable.createScrollPaneForTable(tbl);
            JTableHeader th = tbl.getTableHeader();
            th.setBackground(Color.PINK);
            th.setForeground(Color.BLUE);
            this.add(jsp, BorderLayout.CENTER);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
            ex.printStackTrace();
        }
        this.setSize(650, 300);
        this.setVisible(true);
    }
}

class MyFrame extends JFrame implements ActionListener {
    private JDesktopPane jdp;
    private JToolBar tbar;
    private JButton b[];
    private InsertFrame insert_frame;
    private AllEmployeeFrame all_employee_frame;
    private SearchUpdateDeleteFrame search_update_frame;

    public MyFrame() {
        super("Employee Management System");
        tbar = new JToolBar(JToolBar.HORIZONTAL);
        this.add(tbar, BorderLayout.NORTH);
        tbar.setFloatable(false);
        tbar.setLayout(new GridLayout(1, 3));

        String arr[] = { "Add New Employee", "Search, Update, Delete", "All Employee" };
        b = new JButton[3];
        for (int i = 0; i < arr.length; i++) {
            b[i] = new JButton(arr[i]);
            tbar.add(b[i]);
            b[i].addActionListener(this);
        }
        jdp = new JDesktopPane();
        this.add(jdp);
        this.setSize(950, 650);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();

        if (obj == b[0]) {
            // Add New Employee
            if (insert_frame == null) {
                insert_frame = new InsertFrame();
                jdp.add(insert_frame);
                insert_frame.setBounds(100, 10, 300, 250);
            } else {
                JOptionPane.showMessageDialog(this, "Insert Frame is Already opened");
            }
        } else if (obj == b[1]) {
            // Search, Update, Delete
            if (search_update_frame == null) {
                search_update_frame = new SearchUpdateDeleteFrame();
                search_update_frame.setBounds(320, 10, 600, 280);
                jdp.add(search_update_frame);
            } else {
                JOptionPane.showMessageDialog(this, "Search, Update, Delete Frame is Already Opened");
            }
        } else if (obj == b[2]) {
            // All Employee
            if (all_employee_frame == null) {
                all_employee_frame = new AllEmployeeFrame();
                all_employee_frame.setBounds(10, 270, 650, 300);
                jdp.add(all_employee_frame);
            } else {
                JOptionPane.showMessageDialog(this, "All Employee Frame is already opened");
            }
        }
    }
}

// Main Class
class Sample {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MyFrame f = new MyFrame();
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        });
    }
}
