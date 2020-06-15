package de.extraschuetze;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.sql.*;

public class Keys extends javax.swing.JFrame {

    // <editor-fold defaultstate="collapsed" desc="Initialisierungen">
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JButton jButton1;
    private javax.swing.JTable jTable;
    private javax.swing.JScrollPane jScrollPane;
    // </editor-fold>

    public static Keys INSTANCE;

    public static void Load() {
        Keys frame = new Keys();
        frame.initComponents();
        frame.setIconImage(Toolkit.getDefaultToolkit().getImage(LohnRechner.class.getResource("PB_Discord.png")));
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }

    private void initComponents() {

        INSTANCE = this;

        jLabel1 = new javax.swing.JLabel();
        jCheckBox1 = new javax.swing.JCheckBox();
        jButton1 = new javax.swing.JButton();
        jTable = new javax.swing.JTable();
        jScrollPane = new javax.swing.JScrollPane();

        setTitle("Keys");
        setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        setResizable(false);
        getContentPane().setBackground(new Color(0, 191, 255));

        jLabel1.setFont(new Font("Tahoma", 0, 24)); // NOI18N
        jLabel1.setText("Keys");

        jScrollPane.setViewportView(jTable);

        jCheckBox1.setText("Valid");
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });
        jCheckBox1.setBackground(new Color(0, 191, 255));

        jTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseEvent(evt);
            }
        });

        jButton1.setText("Neu");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        initTable();

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 402, Short.MAX_VALUE)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jCheckBox1)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jButton1)
                                                .addGap(0, 0, Short.MAX_VALUE)))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jCheckBox1)
                                        .addComponent(jButton1))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();

    }

    private void jTable1MouseEvent(MouseEvent evt) {
        int id1 = jTable.rowAtPoint(evt.getPoint()) + 1;
        int col = jTable.columnAtPoint(evt.getPoint()) + 1;
        String id = String.valueOf(id1);
        String spalte = String.valueOf(col);
        ResultSet set = onQuery("SELECT valid FROM lizenzkeys WHERE id=" + id);
        try {
            while (set.next()) {
                if(set.getString("valid").equals("0")) {
                    jCheckBox1.setSelected(false);
                } else {
                    jCheckBox1.setSelected(true);
                }
            }
        } catch (SQLException e) {}
    }

    private static int count() {
        ResultSet set = onQuery("SELECT * FROM lizenzkeys WHERE 1");
        int i = 0;
        try {
            while (set.next()) {
                i++;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return i;
    }

    private void jButton1ActionPerformed(ActionEvent evt) {
        NeuKey.Load();
    }

    private void jCheckBox1ActionPerformed(ActionEvent evt) {
        update();
    }

    private void update() {
        String valid;
        if (jCheckBox1.isSelected()) {
            valid = "1";
        } else {
            valid = "0";
        }
        String sql = "UPDATE `lizenzkeys` SET `valid` = '" + valid + "' WHERE `id` =" + (jTable.getSelectedRow() + 1);
        try {
            onUpdate(sql);
        } catch (SQLException throwables) {

        }
        //disconnect();
        updateTable();
    }

    private static Connection conn;

    public static void disconnect() {

        try {

            if (conn != null) {

                conn.close();

            }

        } catch (SQLException e) {

            e.printStackTrace();

        }

    }

    public static ResultSet onQuery(String sql) {

        conn = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://194.169.211.186:3306/ni2249076_1sql6?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=Europe/Berlin&autoReconnect=true", "ni2249076_1sql6", "keys4321");

            Statement statement = conn.createStatement();

            return statement.executeQuery(sql);
        } catch (SQLException | ClassNotFoundException throwables) {

            return null;
        }

    }

    public static void onUpdate(String sql) throws SQLException {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://194.169.211.186:3306/ni2249076_1sql6?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=Europe/Berlin&autoReconnect=true", "ni2249076_1sql6", "keys4321");

            Statement statement = conn.createStatement();

            statement.execute(sql);
        } catch (SQLException e) {

            e.printStackTrace();
        } catch (ClassNotFoundException e) {

        }

    }

    private static Object[][] getChannelTable() {
        int size = count();
        Object[][] channel = new String[size][3];
        ResultSet set = onQuery("SELECT * FROM lizenzkeys WHERE 1");
        int i = 0;
        try {
            while (set.next()) {
                channel[i][0] = set.getString("lizenzkey");
                channel[i][1] = set.getString("valid");
                channel[i][2] = set.getString("created_at");
                i++;
            }
        } catch (SQLException e) {}
        return channel;
    }

    private static void initTable() {
        Object[][] channel = getChannelTable();

        Keys.INSTANCE.jTable.setModel(new javax.swing.table.DefaultTableModel(
                channel,
                new String[]{
                        "Key", "Valid", "Created at"
                }
        ));

        Keys.INSTANCE.jScrollPane.setViewportView(Keys.INSTANCE.jTable);
    }

    public static void updateTable() {
        Object[][] channel = getChannelTable();

        Keys.INSTANCE.jTable.setModel(new javax.swing.table.DefaultTableModel(
                channel,
                new String[]{
                        "Key", "Valid", "Created at"
                }
        ));

    }

}
