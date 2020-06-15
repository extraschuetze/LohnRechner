package de.extraschuetze;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.*;
import java.util.prefs.Preferences;

public class LicenceCheck extends javax.swing.JFrame {

    // <editor-fold defaultstate="collapsed" desc="Initialisierungen">
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    // </editor-fold>

    public static LicenceCheck INSTANCE;

    private static int checkLicence(String license) {
        boolean valid = false;

        ResultSet set = onQuery("SELECT `lizenzkey`,`valid` FROM `lizenzkeys` WHERE 1");

        if(set == null) {
            return 3;
        }

        try {
            while (set.next()) {
                if(set.getString("lizenzkey").equals(license)) {
                    if(set.getString("valid").equals("1")) {
                        valid = true;
                    } else {
                        return 2;
                    }
                }
            }
        } catch (SQLException throwables) {

        } catch (NullPointerException e) {

        }

        disconnect();

        if (valid) {
            return 0;
        } else {
            return 1;
        }
    }

    private static void valid(String key) {
        Preferences prefs = Preferences.userRoot().node("LohnRechner-Config");
        prefs.put("key", key);
        LohnRechner.Load();
    }

    private static void invalid(int code) {
        if(code == 1) {
            LicenceCheck.INSTANCE.jLabel2.setText("Ungültiger Schlüssel");
        } else if(code == 2) {
            LicenceCheck.INSTANCE.jLabel2.setText("Schlüssel abgelaufen");
        } else if (code == 3) {
            LicenceCheck.INSTANCE.jLabel2.setText("Keine Internetverbindung");
        }
    }

    public static void Load() {
        LicenceCheck frame = new LicenceCheck();
        frame.initComponents();
        frame.setIconImage(Toolkit.getDefaultToolkit().getImage(LohnRechner.class.getResource("PB_Discord.png")));
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }

    public static boolean isNessasary() {
        Preferences prefs = Preferences.userRoot().node("LohnRechner-Config");
        if(prefs.get("key", null) != null || prefs.get("key", null) == "") {
            String key = prefs.get("key", null);
            if(LicenceCheck.checkLicence(key) == 0 || LicenceCheck.checkLicence(key) == 3) {
                return false;
            } else {
                Load();
                return true;
            }
        } else {
            Load();
            return true;
        }
    }

    private void initComponents() {

        INSTANCE = this;
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();

        setTitle("Lizenz");
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        getContentPane().setBackground(new Color(0, 191, 255));

        jTextField1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        jButton1.setText("Weiter");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Admin");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel1.setText("Lizenzschlüssel");

        jLabel2.setText("Schlüssel eingeben");
        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 231, Short.MAX_VALUE)
                                        .addComponent(jTextField1)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jButton1)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(jButton2))
                                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                                        .addComponent(jButton1)
                                        .addComponent(jButton2))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel2)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();

    }

    private void jButton2ActionPerformed(ActionEvent evt) {
        AccessKey.Load();
    }

    private void jButton1ActionPerformed(ActionEvent evt) {
        String key = jTextField1.getText();
        int valid = checkLicence(key);
        if(valid == 0) {
            LicenceCheck.INSTANCE.setVisible(false);
            valid(key);
        } else {
            invalid(valid);
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
            throwables.printStackTrace();
            return null;
        }

    }

}
