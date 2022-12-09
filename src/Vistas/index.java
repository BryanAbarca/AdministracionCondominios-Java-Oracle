package Vistas;

import Controlador.Controlador;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JOptionPane;

public class index extends javax.swing.JFrame implements Observer {

    private String idCuotas;
    private Controlador controlador;

    public index() {
        super("Lista Filiales");
        this.controlador = new Controlador();
        initComponents();
    }

    public void iniciar() {
        this.controlador.agregarObservador(this);
        this.controlador.cargarFiliales(tbFiliales);

        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        btnAgregarFilial = new javax.swing.JButton();
        btnEliminarFilial = new javax.swing.JButton();
        EntrarFilial = new javax.swing.JButton();
        scroll = new javax.swing.JScrollPane();
        tbFiliales = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        btnDueños = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnAgregarFilial.setBackground(new java.awt.Color(119, 221, 119));
        btnAgregarFilial.setForeground(new java.awt.Color(0, 0, 0));
        btnAgregarFilial.setText("Ingresar Filial");
        btnAgregarFilial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarFilialActionPerformed(evt);
            }
        });
        jPanel1.add(btnAgregarFilial, new org.netbeans.lib.awtextra.AbsoluteConstraints(19, 475, 122, 43));

        btnEliminarFilial.setBackground(new java.awt.Color(255, 102, 102));
        btnEliminarFilial.setForeground(new java.awt.Color(0, 0, 0));
        btnEliminarFilial.setText("Eliminar");
        btnEliminarFilial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarFilialActionPerformed(evt);
            }
        });
        jPanel1.add(btnEliminarFilial, new org.netbeans.lib.awtextra.AbsoluteConstraints(159, 475, 122, 43));

        EntrarFilial.setBackground(new java.awt.Color(204, 204, 255));
        EntrarFilial.setForeground(new java.awt.Color(0, 0, 0));
        EntrarFilial.setText("Entrar");
        EntrarFilial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EntrarFilialActionPerformed(evt);
            }
        });
        jPanel1.add(EntrarFilial, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 475, 122, 43));

        tbFiliales.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Id ", "nombre", "Apartamentos", "Provincia", "Canton", "Distrito", "Activo"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        scroll.setViewportView(tbFiliales);
        if (tbFiliales.getColumnModel().getColumnCount() > 0) {
            tbFiliales.getColumnModel().getColumn(0).setMaxWidth(50);
            tbFiliales.getColumnModel().getColumn(1).setMinWidth(150);
            tbFiliales.getColumnModel().getColumn(2).setMinWidth(100);
            tbFiliales.getColumnModel().getColumn(2).setMaxWidth(100);
            tbFiliales.getColumnModel().getColumn(3).setMinWidth(100);
            tbFiliales.getColumnModel().getColumn(4).setMinWidth(100);
            tbFiliales.getColumnModel().getColumn(5).setMinWidth(100);
            tbFiliales.getColumnModel().getColumn(6).setMinWidth(50);
            tbFiliales.getColumnModel().getColumn(6).setMaxWidth(50);
        }

        jPanel1.add(scroll, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 60, 740, 370));

        jLabel2.setText("Seleccione el filial");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 30, -1, -1));

        btnDueños.setBackground(new java.awt.Color(102, 255, 204));
        btnDueños.setForeground(new java.awt.Color(0, 0, 0));
        btnDueños.setText("DUEÑOS");
        btnDueños.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDueñosActionPerformed(evt);
            }
        });
        jPanel1.add(btnDueños, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 475, 122, 43));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 852, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 540, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAgregarFilialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarFilialActionPerformed
        this.controlador.agregarFilial(tbFiliales);

        //si no esta en blanco -si no está repetido -falta los datos nuevos de la tabla

    }//GEN-LAST:event_btnAgregarFilialActionPerformed

    private void btnEliminarFilialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarFilialActionPerformed
        int fila = tbFiliales.getSelectedRow();
        if (fila != -1) {
            int idFilial = (int) tbFiliales.getValueAt(fila, 0);
            try {
                controlador.eliminarFilial(fila, idFilial, tbFiliales);
                //*setVisible(false);
            } catch (Exception e) {
                System.err.println("Error en ver Filiales: " + e);
            }
        }
    }//GEN-LAST:event_btnEliminarFilialActionPerformed

    private void EntrarFilialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EntrarFilialActionPerformed
        int fila = tbFiliales.getSelectedRow();

        if (fila != -1) {
            int idFilial = (int) tbFiliales.getValueAt(fila, 0);
            try {
                //controlador.verFilial(idFilial);
                controlador.verFilial(idCuotas, idFilial);
                setVisible(false);
                this.dispose();
            } catch (Exception e) {
                System.err.println("Error en ver Filiales: " + e);
            }
        }
    }//GEN-LAST:event_EntrarFilialActionPerformed

    private void btnDueñosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDueñosActionPerformed
        Dueños vistaDueños = new Dueños();
        vistaDueños.iniciar();
        setVisible(false);
        this.dispose();
    }//GEN-LAST:event_btnDueñosActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(index.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(index.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(index.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(index.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new index().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton EntrarFilial;
    public javax.swing.JButton btnAgregarFilial;
    private javax.swing.JButton btnDueños;
    public javax.swing.JButton btnEliminarFilial;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane scroll;
    private javax.swing.JTable tbFiliales;
    // End of variables declaration//GEN-END:variables

    @Override
    public void update(Observable o, Object arg) {
        // throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
