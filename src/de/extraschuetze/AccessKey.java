package de.extraschuetze;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
public class AccessKey extends javax.swing.JFrame {

    // <editor-fold defaultstate="collapsed" desc="Initialisierungen">
    private javax.swing.JTextField jTextField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JButton jButton1;
    // </editor-fold>

    public static AccessKey INSTANCE;

    public static void Load() {
        AccessKey frame = new AccessKey();
        frame.initComponents();
        frame.setIconImage(Toolkit.getDefaultToolkit().getImage(LohnRechner.class.getResource("PB_Discord.png")));
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }

    private void initComponents() {

        INSTANCE = this;

        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jTextField = new javax.swing.JTextField();

        setTitle("Admins");
        setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        setResizable(false);
        getContentPane().setBackground(new Color(0, 191, 255));

        jLabel1.setFont(new Font("Tahoma", 0, 24)); // NOI18N
        jLabel1.setText("Admins");

        jButton1.setText("Login");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jTextField.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jTextField.setText("");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 231, Short.MAX_VALUE)
                                                .addComponent(jTextField))
                                        .addComponent(jButton1))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton1)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();

    }

    private void jButton1ActionPerformed(ActionEvent evt) {
        if(jTextField.getText().equals("lp191019")) {
            Keys.Load();
            AccessKey.INSTANCE.setVisible(false);
        }
    }

}
