package de.extraschuetze;

import com.barcodelib.barcode.QRCode;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.prefs.Preferences;

public class LohnRechner extends javax.swing.JFrame {

    // <editor-fold defaultstate="collapsed" desc="Initialisierungen">
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private static javax.swing.JLabel jLabel14;
    private static javax.swing.JLabel jLabel15;
    private static javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;

    private static int uom = 0;        //  0 - Pixel, 1 - CM, 2 - Inch
    private static int resolution = 72;
    private static float leftMargin = 0;
    private static float rightMargin = 0;
    private static float topMargin = 0;
    private static float bottomMargin = 0;
    private static int rotate = 0;     //  0 - 0, 1 - 90, 2 - 180, 3 - 270

    private static float moduleSize = 5.000f;
    // </editor-fold>

    //TODO: Version ändern
    public static String version = "1.3";

    public static void main(String[] args) {
        VersionCheck.Load();
    }

    public static LohnRechner INSTANCE;

    public static void Load() {
        LohnRechner frame = new LohnRechner();
        frame.initComponents();
        frame.setIconImage(Toolkit.getDefaultToolkit().getImage(LohnRechner.class.getResource("PB_Discord.png")));
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                System.out.println(evt.paramString());
                if(evt.isControlDown() || evt.getKeyCode() == 65) {
                    AccessKey.Load();
                }
            }
        });
    }

    public static void go(double zeitPapa, double zeitPierre, double zeitPascal, double uberweisungPapa, double uberweisungPascal) {
        double[] lohn = rechnen(zeitPapa, zeitPierre, zeitPascal, uberweisungPapa, uberweisungPascal);
        double lohnPierre = runden(lohn[2]);
        double lohnPascal = runden(lohn[0]);
        ausgabeUberweisungen(lohnPierre, lohnPascal, uberweisungPascal);
    }

    public static void goLog(double zeitPapa, double zeitPierre, double zeitPascal, double uberweisungPapa, double uberweisungPascal) {
        double[] lohn = rechnen(zeitPapa, zeitPierre, zeitPascal, uberweisungPapa, uberweisungPascal);
        double lohnPierre = runden(lohn[2]);
        double lohnPascal = runden(lohn[0]);
        double lohnPapa = runden(lohn[1]);
        String[] arr = ausgabeLog(lohnPierre, lohnPascal, lohnPapa, uberweisungPapa, uberweisungPascal);
        log(arr);
    }

    public static void log(String[] arr) {
        GregorianCalendar cal = new GregorianCalendar(); // 11.05.2007
        Date date = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("(yyyy.MM.dd-HH.mm.ss)");

        String str = "LohnRechnung" + sdf.format(date) + ".txt";
        File file = new File(System.getProperty("user.home") + "/Desktop/" + str);
        if (!file.exists()) {

            try {
                file.createNewFile();
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true)));
                for (String s : arr) {
                    bw.write(s);
                    bw.newLine();
                }
                bw.close();
            } catch (IOException e) {
            }

        }
    }

    private static double[] rechnen(double zPapa, double zPierre, double zPascal, double uPapa, double uPascal) {
        double zGesamt = gesamtAbreitszeit(zPascal, zPierre, zPapa);
        double uGesamt = gesamtLohn(uPascal, uPapa);
        double stundenLohn = durchschnitt(zGesamt, uGesamt);
        double[] lohn = new double[3];
        lohn[0] = lohn(zPascal, stundenLohn);
        lohn[1] = lohn(zPapa, stundenLohn);
        lohn[2] = lohn(zPierre, stundenLohn);
        return lohn;
    }

    private static double papaToPierre(double lohnPierre) {
        return lohnPierre;
    }

    private static double papaToPascal(double lohnPascal, double uPascal) {
        return lohnPascal - uPascal;
    }

    private static double durchschnitt(double gesamtArbeitszeit, double gesamtLohn) {
        return gesamtLohn / gesamtArbeitszeit;
    }

    private static double gesamtAbreitszeit(double zeitPascal, double zeitPierre, double zeitPapa) {
        return zeitPapa + zeitPierre + zeitPascal;
    }

    private static double gesamtLohn(double lohnPascal, double lohnPapa) {
        return lohnPapa + lohnPascal;
    }

    private static double lohn(double zeit, double stundenlohn) {
        return stundenlohn * zeit;
    }

    private static double runden(double value) {
        double d = Math.pow(10, 2);
        return Math.round(value * d) / d;
    }

    private static void ausgabeUberweisungen(double gehaltPierre, double gehaltPascal, double uberweisungPascal) {

        GregorianCalendar cal = new GregorianCalendar(); // 11.05.2007
        Date date = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        String zweck = "Zeitungsgeld vom " + sdf.format(date);

        if (papaToPascal(gehaltPascal, uberweisungPascal) > 0) {
            jLabel14.setText("Papa zu Pascal: " + runden(papaToPascal(gehaltPascal, uberweisungPascal)) + "€");
            try {
                generate(runden(papaToPascal(gehaltPascal, uberweisungPascal)), zweck, "Pascal Voigt", "COBADEFFXXX", "DE85 2604 0030 0602 7643 00");
            } catch (Exception e) {

            }
        } else {
            jLabel14.setText("Papa zu Pascal: 0.00€");
        }
        if (papaToPierre(gehaltPierre) > 0) {
            jLabel15.setText("Papa zu Pierre: " + runden(papaToPierre(gehaltPierre)) + "€");
            try {
                generate(runden(papaToPierre(gehaltPierre)), zweck, "Pierre Voigt", "COBADEFFXXX", "DE89 2604 0030 0602 6843 00");
            } catch (Exception e) {

            }
        } else {
            jLabel15.setText("Papa zu Pierre: 0.00€");
        }
        if (uberweisungPascal > gehaltPascal) {
            jLabel16.setText("Pascal zu Papa: " + runden((uberweisungPascal - gehaltPascal)) + "€");
            try {
                generate(runden(uberweisungPascal - gehaltPascal), zweck, "Veikko Voigt", "BYLADEM1001", "DE65 1203 0000 0016 8728 89");
            } catch (Exception e) {

            }
        } else {
            jLabel16.setText("Pascal zu Papa: 0.00€");
        }
    }

    private static String[] ausgabeLog(double gehaltPierre, double gehaltPascal, double gehaltPapa, double uberweisungPapa, double uberweisungPascal) {

        String[] arr = new String[13];

        arr[0] = "Grundgehalt:";
        arr[1] = "Pascal: " + gehaltPascal + "€";
        arr[2] = "Papa: " + gehaltPapa + "€";
        arr[3] = "Pierre: " + gehaltPierre + "€";
        arr[4] = "----------------------------------------------";

        arr[5] = "Aufs Konto:";
        arr[6] = "Pascal: " + uberweisungPascal + "€";
        arr[7] = "Papa: " + uberweisungPapa + "€";
        arr[8] = "----------------------------------------------";

        arr[9] = "Überweisungen:";
        if (papaToPascal(gehaltPascal, uberweisungPascal) > 0) {
            arr[10] = "Papa zu Pascal: " + runden(papaToPascal(gehaltPascal, uberweisungPascal)) + "€";
        } else {
            arr[10] = "Papa zu Pascal: 0.0€";
        }
        if (papaToPierre(gehaltPierre) > 0) {
            arr[11] = "Papa zu Pierre: " + runden(papaToPierre(gehaltPierre)) + "€";
        } else {
            arr[11] = "Papa zu Pierre: 0.0€";
        }
        if (uberweisungPascal > gehaltPascal) {
            arr[12] = "Pascal zu Papa: " + runden((uberweisungPascal - gehaltPascal)) + "€";
        } else {
            arr[12] = "Pascal zu Papa: 0.0€";
        }

        return arr;
    }

    private void initComponents() {

        INSTANCE = this;

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jTextField4 = new javax.swing.JTextField();
        jTextField5 = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel13 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        setTitle("Lohn Rechner");
        getContentPane().setBackground(new Color(0, 191, 255));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Lohnrechner");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel2.setText("Eingabe");

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel3.setText("Arbeitszeit Pascal:");

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel4.setText("Arbeitszeit Pierre:");

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel5.setText("Arbeitszeit Papa:");

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel6.setText("Überweisung Papa:");

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel7.setText("Überweisung Pascal:");

        jTextField1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        jTextField2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        jTextField3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        jTextField4.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        jTextField5.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel8.setText("Stunden");

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel9.setText("Stunden");

        jLabel10.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel10.setText("Stunden");

        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel11.setText("€");

        jLabel12.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel12.setText("€");

        jLabel13.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel13.setText("Ausgabe");

        jButton1.setText("Rechnen");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Stunden");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Speichern");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jButton3.setEnabled(false);

        jButton4.setText("Key ändern");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel14.setText("Papa zu Pascal: 0.00€");

        jLabel15.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel15.setText("Papa zu Pierre: 0.00€");

        jLabel16.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel16.setText("Pascal zu Papa: 0.00€");

        jLabel17.setText("Version: " + version);

        jLabel17.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N

        jLabel18.setText("Made by Pascal Voigt");
        jLabel18.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(layout.createSequentialGroup()
                                                        .addComponent(jLabel18)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(jButton4))
                                                .addGroup(layout.createSequentialGroup()
                                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                                        .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                        .addGroup(layout.createSequentialGroup()
                                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                                                        .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                                        .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                                        .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                                        .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                                        .addGroup(layout.createSequentialGroup()
                                                                                                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                                .addComponent(jLabel9))
                                                                                        .addGroup(layout.createSequentialGroup()
                                                                                                .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                                .addComponent(jLabel10))
                                                                                        .addGroup(layout.createSequentialGroup()
                                                                                                .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                                .addComponent(jLabel11))
                                                                                        .addGroup(layout.createSequentialGroup()
                                                                                                .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                                .addComponent(jLabel12))
                                                                                        .addGroup(layout.createSequentialGroup()
                                                                                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                                .addComponent(jLabel8))))
                                                                        .addGroup(layout.createSequentialGroup()
                                                                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, 83, Short.MAX_VALUE)))
                                                                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 283, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addGap(0, 0, Short.MAX_VALUE))))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel3)
                                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel8))
                                .addGap(10, 10, 10)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel4)
                                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel9))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel5)
                                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel10))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel6)
                                        .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel11))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel7)
                                        .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel12))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jButton1)
                                        .addComponent(jButton2)
                                        .addComponent(jButton3))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel13)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel15)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel14)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel16)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel17)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel18)
                                        .addComponent(jButton4))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }

    private void jButton4ActionPerformed(ActionEvent evt) {
        LohnRechner.INSTANCE.setVisible(false);
        Preferences prefs = Preferences.userRoot().node("LohnRechner-Config");
        prefs.remove("key");
        LicenceCheck.Load();
    }

    private void jButton2ActionPerformed(ActionEvent evt) {
        Stunden.Load();
    }

    private void jButton3ActionPerformed(ActionEvent evt) {
        try {
            double zeitPapa = 0.0;
            double zeitPierre = 0.0;
            double zeitPascal = 0.0;
            double uPascal = 0.0;
            double uPapa = 0.0;
            if (!jTextField3.getText().equals("")) {
                zeitPapa = Double.valueOf(jTextField3.getText());
            }
            if (!jTextField2.getText().equals("")) {
                zeitPierre = Double.valueOf(jTextField2.getText());
            }
            if (!jTextField1.getText().equals("")) {
                zeitPascal = Double.valueOf(jTextField1.getText());
            }
            if (!jTextField4.getText().equals("")) {
                uPapa = Double.valueOf(jTextField4.getText());
            }
            if (!jTextField5.getText().equals("")) {
                uPascal = Double.valueOf(jTextField5.getText());
            }
            LohnRechner.goLog(zeitPapa, zeitPierre, zeitPascal, uPapa, uPascal);
        } catch (NumberFormatException e) {

        } catch (Exception e) {

        }
    }

    private void jButton1ActionPerformed(ActionEvent evt) {
        try {
            double zeitPapa = 0.0;
            double zeitPierre = 0.0;
            double zeitPascal = 0.0;
            double uPascal = 0.0;
            double uPapa = 0.0;
            if (!jTextField3.getText().equals("")) {
                zeitPapa = Double.valueOf(jTextField3.getText());
            }
            if (!jTextField2.getText().equals("")) {
                zeitPierre = Double.valueOf(jTextField2.getText());
            }
            if (!jTextField1.getText().equals("")) {
                zeitPascal = Double.valueOf(jTextField1.getText());
            }
            if (!jTextField4.getText().equals("")) {
                uPapa = Double.valueOf(jTextField4.getText());
            }
            if (!jTextField5.getText().equals("")) {
                uPascal = Double.valueOf(jTextField5.getText());
            }
            LohnRechner.go(zeitPapa, zeitPierre, zeitPascal, uPapa, uPascal);
            jButton3.setEnabled(true);
        } catch (NumberFormatException e) {

        }
    }

    public static void generate(double betrag, String zweck, String name, String bic, String iban) throws Exception {
        QRCode barcode = new QRCode();
        barcode.setData("BCD\n" +
                "001\n" +
                "1\n" +
                "SCT\n" +
                bic + "\n" +
                name + "\n" +
                iban + "\n" +
                "EUR" + runden(betrag) + "\n" +
                "\n" +
                "\n" +
                zweck + "\n" +
                "\n");
        barcode.setDataMode(QRCode.MODE_BYTE);
        barcode.setVersion(10);
        barcode.setEcl(QRCode.ECL_M);


        barcode.setUOM(uom);
        barcode.setModuleSize(moduleSize);
        barcode.setLeftMargin(leftMargin);
        barcode.setRightMargin(rightMargin);
        barcode.setTopMargin(topMargin);
        barcode.setBottomMargin(bottomMargin);
        barcode.setResolution(resolution);
        barcode.setRotate(rotate);

        GregorianCalendar cal = new GregorianCalendar(); // 11.05.2007
        Date date = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("(yyyy.MM.dd-HH.mm.ss)");
        String str = "Überweisung" + sdf.format(date) + name + ".png";

        String FileFolder = System.getenv("APPDATA") + "\\" + "LohnRechner";

        File directory = new File(FileFolder);

        if (directory.exists() == false) {
            if (directory.mkdir()) {
            }
        }

        String path = System.getenv("APPDATA") + "\\" + "LohnRechner\\" + str;
        barcode.renderBarcode(path);

        new BildLaden(path);

        File file = new File(path);
        file.delete();

    }

}
