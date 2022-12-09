/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelos;

import Conexion.GestorBD;
import Vistas.condominios;
import Vistas.cuotas;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author hilla
 */
public class Cuotas extends Observable {

    private GestorBD gestor = null;

    public Cuotas() {
        this.gestor = new GestorBD();
    }

    public void cerrarConexion() {
        this.gestor.cerrar();
        setChanged();
        notifyObservers("Se cerro la conexion");
    }

    public void agregarObservador(Observer observador) {
        addObserver(observador);
        setChanged();
        notifyObservers("Observador");
    }

    public void verCuotas(String idCuota, int idF) {
        cuotas cuot = new cuotas();
        cuot.iniciar(idCuota, idF);
    }

    public void cargarCuotas(String idCasa, JTable tabla) {

        try {
            String cadena = "SELECT * FROM CUOTAS WHERE ID_CASA= '" + idCasa + "'";
            Statement st = gestor.getConexion().createStatement();
            ResultSet rs = st.executeQuery(cadena);

            while (rs.next()) {

                DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();
                modelo.addRow(new Object[]{rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getFloat(6)});

            }
            setChanged();
            notifyObservers("CARGANDO CUOTAS");
        } catch (SQLException e) {
            System.err.println("Error:" + e);
        } finally {
            gestor.cerrar();
        }
    }

    public void agregarCuota(String idCasa, JTable tabla) {
        ArrayList<String> cuot = obtenerCuotas();
        if (cuot != null && cuot.size() > 0) {
            String cu = (String) JOptionPane.showInputDialog(null, "Seleccione el tipo de cuota", "Cuotas", JOptionPane.QUESTION_MESSAGE, null, cuot.toArray(), cuot.get(0));
            if (cu != null && !cu.isBlank() && !cu.isEmpty()) {
                String des = JOptionPane.showInputDialog(null, "Digite la descripcion");
                if (des != null && !des.isBlank() && !des.isEmpty()) {
                    int mon = Integer.parseInt(JOptionPane.showInputDialog(null, "Digite el monto de la cuota"));

                    try {
                        int idCuota = obtenerIDCuota(cu);
                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/YYYY");
                        String fecha = dtf.format(LocalDateTime.now());
                        CallableStatement pst = gestor.getConexion().prepareCall("{CALL SP_INS_CUO(?, ?, ?, ?, ?)}");
                        pst.setInt(1, idCuota);
                        pst.setString(2, idCasa);//ARREGLAR
                        pst.setString(3, des);
                        pst.setString(4, fecha);
                        pst.setInt(5, mon);
                        pst.execute();

                        DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();
                        modelo.addRow(new Object[]{0, idCuota, idCasa, des, fecha, mon});
                        setChanged();
                        notifyObservers("CARGANDO TABLA APARTAMENTOS");
                    } catch (SQLException e) {
                        System.err.println("Error:" + e);
                    } finally {
                        gestor.cerrar();
                    }
                }
            }
        }
    }

    public void eliminarCuota(int cuot, int fila, JTable tabla) {
        String[] respuesta = {"Si", "No"};
        int res = JOptionPane.showOptionDialog(null, "¿Está seguro que desea eliminar la cuota?", "Eliminar", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, respuesta, respuesta[0]);

        if (res == 0) {

            try {
                CallableStatement cs = gestor.getConexion().prepareCall("{CALL SP_DEL_CUO(?)}");
                cs.setInt(1, cuot);
                cs.execute();
                ((DefaultTableModel) tabla.getModel()).removeRow(fila);

            } catch (Exception e) {
                System.err.println("Error:" + e);
            } finally {
                gestor = null;
            }
        }

    }

    public int obtenerIDCuota(String cuot) {
        int id = 0;
        Statement st = null;
        ResultSet rs = null;

        try {
            st = gestor.getConexion().createStatement();
            rs = st.executeQuery("SELECT ID_TIPO FROM TIPO_CUOTAS WHERE DESCRIPCION = '" + cuot + "'");
            if (rs.next()) {
                id = rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("ERROR AL OBTENER EL ID DE CUOTA  " + e);
        } finally {
            gestor.cerrar();
        }

        return id;
    }

    public ArrayList<String> obtenerCuotas() {

        ArrayList<String> provincias = new ArrayList();
        String consulta = "SELECT * FROM TIPO_CUOTAS";
        try {
            Statement st = gestor.getConexion().createStatement();
            ResultSet rs = st.executeQuery(consulta);
            while (rs.next()) {
                provincias.add(rs.getString(2));
            }
            setChanged();
            notifyObservers("CARGANDO CUOTAS");
            return provincias;
        } catch (SQLException e) {
            System.err.println("Error:" + e);
        } finally {
            gestor.cerrar();
        }

        return null;
    }

}
