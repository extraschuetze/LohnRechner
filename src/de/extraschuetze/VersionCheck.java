package de.extraschuetze;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.*;
import java.util.prefs.Preferences;

public class VersionCheck extends javax.swing.JFrame {

    // <editor-fold defaultstate="collapsed" desc="Initialisierungen">
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    // </editor-fold>

    public static VersionCheck INSTANCE;

    private static int checkVersion() {
        boolean notig = true;

        ResultSet set = onQuery("SELECT `version` FROM `version` WHERE 1");

        if(set == null) {
            notig = false;
        }

        try {
            while (set.next()) {
                if(set.getString("version").equals(LohnRechner.version)) {
                    notig = false;
                }
            }
        } catch (SQLException throwables) {

        } catch (NullPointerException e) {

        }

        disconnect();

        if (notig) {
            return 1;
        } else {
            return 0;
        }
    }

    public static void Load() {
        if(checkVersion() == 0) {
            if(!LicenceCheck.isNessasary()) {
                LohnRechner.Load();
            }
        } else {
            VersionCheck frame = new VersionCheck();
            frame.initComponents();
            frame.setIconImage(Toolkit.getDefaultToolkit().getImage(LohnRechner.class.getResource("PB_Discord.png")));
            frame.setVisible(true);
            frame.setLocationRelativeTo(null);
        }
    }

    private void initComponents() {

        INSTANCE = this;
        jButton1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();

        setTitle("Update");
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        getContentPane().setBackground(new Color(0, 191, 255));

        jButton1.setText("Installieren");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel1.setFont(new Font("Tahoma", 0, 24)); // NOI18N
        jLabel1.setText("Update");

        jLabel2.setText("Eine neue Version ist verfügbar.");
        jLabel2.setFont(new Font("Tahoma", 0, 14)); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 231, Short.MAX_VALUE)
                                        .addComponent(jButton1)
                                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton1)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();

    }

    private void jButton1ActionPerformed(ActionEvent evt) {
        try {
            Desktop.getDesktop().browse(new URL("https://pasvo.eu/Lohn").toURI());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
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

}
