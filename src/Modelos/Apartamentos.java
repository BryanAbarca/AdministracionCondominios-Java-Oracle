package Modelos;

import Conexion.GestorBD;
import Vistas.agregarAparta;
import Vistas.condominios;
import java.sql.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Observable;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author hilla
 */
public class Apartamentos extends Observable {

    private GestorBD gestor = null;

    public Apartamentos() {
        this.gestor = new GestorBD();
    }

    public void verFilial(String idAparta, int idFilial) {
        condominios VistaCondominio = new condominios();
        VistaCondominio.iniciar(idAparta, idFilial);
    }

    public void verAgregarAparta() {
        agregarAparta newframe = new agregarAparta();
        newframe.setVisible(true);
        newframe.setResizable(false);
        newframe.setLocationRelativeTo(null);

    }

    public void cargarFilial(int idFilial, JTextField cantApartamentos, JTextField cantonFilial, JTextField cedJuridica, JTextField distritoFilial, JTextField nombreFilial, JTextField provinciaFilial) {
        try {
            String cadena = "SELECT FIL.ID_FILIAL, FIL.NOMBRE, FIL.CANT_APART, PROV.DESCRIPCION, CAN.DESCRIPCION, DIS.DESCRIPCION, FIL.HABILITADO FROM FILIALES FIL JOIN PROVINCIAS PROV ON (FIL.ID_PROVINCIA = PROV.ID_PROVINCIA) JOIN CANTONES CAN ON (FIL.ID_CANTON = CAN.ID_CANTON) JOIN DISTRITOS DIS ON (FIL.ID_DISTRITO = DIS.ID_DISTRITO) WHERE ID_FILIAL= " + idFilial;
            Statement st = gestor.getConexion().createStatement();
            ResultSet rs = st.executeQuery(cadena);
            while (rs.next()) {
                cedJuridica.setText(rs.getString(1));
                nombreFilial.setText(rs.getString(2));
                cantApartamentos.setText(rs.getInt(3) + "");
                provinciaFilial.setText(rs.getString(4));
                cantonFilial.setText(rs.getString(5));
                distritoFilial.setText(rs.getString(6));

            }
            setChanged();
            notifyObservers("CARGANDO FILIALES");
        } catch (SQLException e) {
            System.err.println("Error:" + e);
        } finally {
            gestor.cerrar();
        }
    }

    public void cargarApartamentos(int idFilial, JTable tabla) {
        try {
            String cadena = "SELECT APAR.ID_CASA, APAR.ID_DUE??O, DUE.NOMBRE, DUE.APE1, DUE.APE2 FROM APARTAMENTOS APAR JOIN DUE??OS DUE ON (APAR.ID_DUE??O = DUE.ID_DUE??O) WHERE ID_FILIAL= " + idFilial;
            Statement st = gestor.getConexion().createStatement();
            ResultSet rs = st.executeQuery(cadena);
            String nombreCompleto = "";
            while (rs.next()) {
                nombreCompleto = rs.getString(3) + " " + rs.getString(4) + " " + rs.getString(5);
                DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();
                modelo.addRow(new Object[]{rs.getString(1), rs.getString(2), nombreCompleto});
            }
            setChanged();
            notifyObservers("CARGANDO APARTAMENTOS");
        } catch (SQLException e) {
            System.err.println("Error:" + e);
        } finally {
            gestor.cerrar();
        }
    }

    public void agregarAparta(JTable tabla, int idFilial) {
        String dueno = "";
        String id = JOptionPane.showInputDialog(null, "Digite el id del apartamento");
        if (id != null && !id.isBlank() && !id.isEmpty()) {
            // ArrayList<String> cuot = obtenerDuenos();

            ArrayList<String> due??os = obtenerDue??os();

            if (due??os != null && due??os.size() > 0) {
                dueno = (String) JOptionPane.showInputDialog(null, "Seleccione un due??o", "Due??os", JOptionPane.QUESTION_MESSAGE, null, due??os.toArray(), due??os.get(0));
                if (dueno != null && !dueno.isBlank() && !dueno.isEmpty()) {
                    // int idDue = obtenerIdDueno(dueno);
                    String nombre = JOptionPane.showInputDialog(null, "Digite su nombre");
                    if (dueno != null && !dueno.isBlank() && !dueno.isEmpty()) {
                        try {
                            CallableStatement pst = gestor.getConexion().prepareCall("{CALL SP_INS_APAR(?, ?, ?, ?)}");
                            pst.setString(1, id);
                            pst.setInt(2, idFilial);
                            pst.setString(3, dueno);
                            pst.setString(4, "S");
                            pst.execute();

                            DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();
                            modelo.addRow(new Object[]{id, dueno, nombre});

                            setChanged();
                            notifyObservers("CARGANDO TABLA APARTAMENTOS");
                        } catch (SQLException e) {
                            System.err.println("Error:" + e);
                        } finally {
                            gestor.cerrar();
                        }
                    }

                }
            } else {
                JOptionPane.showMessageDialog(null, "No existen due??os");
            }
            //String dueno = JOptionPane.showInputDialog(null, "Digite la cedula del due??o");
        }
    }
    public void eliminarAparta(String apar, int fila, JTable tabla) {
        String[] respuesta = {"Si", "No"};
        int res = JOptionPane.showOptionDialog(null, "??Est?? seguro que desea eliminar el apartamento?", "Eliminar", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, respuesta, respuesta[0]);

        if (res == 0) {

            try {
                CallableStatement cs = gestor.getConexion().prepareCall("{CALL SP_DEL_APAR(?)}");
                cs.setString(1, apar);
                cs.execute();
                ((DefaultTableModel) tabla.getModel()).removeRow(fila);

            } catch (Exception e) {
                System.err.println("Error:" + e);
            } finally {
                gestor = null;
            }
        }

    }

    public ArrayList<String> obtenerDue??os() {

        ArrayList<String> due??os = new ArrayList();
        String consulta = "SELECT * FROM DUE??OS";
        try {
            Statement st = gestor.getConexion().createStatement();
            ResultSet rs = st.executeQuery(consulta);
            while (rs.next()) {
                due??os.add(rs.getString(1));
            }
            setChanged();
            notifyObservers("CARGANDO DUE??OS");
            return due??os;
        } catch (SQLException e) {
            System.err.println("Error:" + e);
        } finally {
            gestor.cerrar();
        }

        return null;
    }

    public int obtenerIdDueno(String due) {
        int id = 0;
        Statement st = null;
        ResultSet rs = null;

        try {
            st = gestor.getConexion().createStatement();
            rs = st.executeQuery("SELECT ID_DUE??O FROM DUE??OS WHERE NOMBRE,APE1,APE2 = '" + due + "'");
            if (rs.next()) {
                id = rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("ERROR AL OBTENER EL ID DE DUE??O  " + e);
        } finally {
            gestor.cerrar();
        }

        return id;
    }

    public ArrayList<String> obtenerDuenos() {

        ArrayList<String> provincias = new ArrayList();
        String consulta = "SELECT NOMBRE,APE1,APE2 FROM DUE??OS";
        try {
            Statement st = gestor.getConexion().createStatement();
            ResultSet rs = st.executeQuery(consulta);
            while (rs.next()) {
                provincias.add(rs.getString(2));
            }
            setChanged();
            notifyObservers("CARGANDO DUE??OS");
            return provincias;
        } catch (SQLException e) {
            System.err.println("Error:" + e);
        } finally {
            gestor.cerrar();
        }

        return null;
    }

}
