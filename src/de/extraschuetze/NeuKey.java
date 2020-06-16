package de.extraschuetze;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.*;

public class NeuKey extends javax.swing.JFrame {

    // <editor-fold defaultstate="collapsed" desc="Initialisierungen">
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JList<String> jList1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton jButton1;
    private javax.swing.JTextField jTextField1;
    // </editor-fold>

    public static NeuKey INSTANCE;



    public static void Load() {
        NeuKey frame = new NeuKey();
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
        jTextField1 = new javax.swing.JTextField();

        setTitle("Neuer Key");
        setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        setResizable(false);
        getContentPane().setBackground(new Color(0, 191, 255));

        jLabel1.setFont(new Font("Tahoma", 0, 24)); // NOI18N
        jLabel1.setText("Neuer Key");

        jCheckBox1.setText("Valid");
        jCheckBox1.setBackground(new Color(0, 191, 255));

        jButton1.setText("Add");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jTextField1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jTextField1.setText("");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 231, Short.MAX_VALUE)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jCheckBox1)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 77, Short.MAX_VALUE)
                                                .addComponent(jButton1))
                                        .addComponent(jTextField1))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jCheckBox1)
                                        .addComponent(jButton1))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();

    }

    private void jButton1ActionPerformed(ActionEvent evt) {
        String key = jTextField1.getText();
        String valid;
        if(jCheckBox1.isSelected()) {
            valid = "1";
        } else {
            valid = "0";
        }
        String sql = "INSERT INTO `lizenzkeys`(`lizenzkey`, `valid`) VALUES ('" + key + "','" + valid + "')";
        try {
            onUpdate(sql);
        } catch (SQLException throwables) {

        }
        disconnect();
        Keys.updateTable();
        NeuKey.INSTANCE.setVisible(false);
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

    public static void onUpdate(String sql) throws SQLException {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String ip = "localhost";
            String port = "3306";
            String database = "keys";
            String user = "admin";
            String passoword = "XXXXXXXXXXXX";
            conn = DriverManager.getConnection("jdbc:mysql://" + ip + ":" + port + "/" + database + "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=Europe/Berlin&autoReconnect=true", user, password);

            Statement statement = conn.createStatement();

            statement.execute(sql);
        } catch (SQLException e) {

            e.printStackTrace();
        } catch (ClassNotFoundException e) {

        }

    }

}
