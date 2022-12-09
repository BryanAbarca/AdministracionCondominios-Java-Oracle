package Modelos;

import Conexion.GestorBD;
import Vistas.condominios;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JTable;
import Vistas.index;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JTextField;

import java.sql.*;
import java.util.ArrayList;
import javax.swing.JOptionPane;

import javax.swing.table.DefaultTableModel;

public class Filiales extends Observable {

    private int idFilial = 0;
    private String nombre = "";
    private String provincia = "";
    private String canton = "";
    private String distrito = "";
    private char habilitado = 'S';
    private int cantApartamentos = 0;
    private GestorBD gestor = null;

    public Filiales() {
        this.gestor = new GestorBD();
    }

    public int getCantApartamentos() {
        return cantApartamentos;
    }

    public void setCantApartamentos(int cantApartamentos) {
        this.cantApartamentos = cantApartamentos;
    }

    public int getIdFilial() {
        return idFilial;
    }

    public void setIdFilial(int idFilial) {
        this.idFilial = idFilial;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getCanton() {
        return canton;
    }

    public void setCanton(String canton) {
        this.canton = canton;
    }

    public String getDistrito() {
        return distrito;
    }

    public void setDistrito(String distrito) {
        this.distrito = distrito;
    }

    public char getHabilitado() {
        return habilitado;
    }

    public void setHabilitado(char habilitado) {
        this.habilitado = habilitado;
    }

    public GestorBD getGestor() {
        return gestor;
    }

    public void setGestor(GestorBD gestor) {
        this.gestor = gestor;
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

    public void agregarFilial(JTable tbFiliales) throws SQLException {
        String nombre = (String) JOptionPane.showInputDialog(null, "Ingrese el nombre del filial");
        if (nombre != null && !nombre.equals("")) {
            ArrayList<String> provincias = obtenerProvincias();
            if (provincias != null) {
                String provincia = (String) JOptionPane.showInputDialog(null, "Seleccione la provincia", "Provincias", JOptionPane.QUESTION_MESSAGE, null, provincias.toArray(), provincias.get(0));
                if (provincia != null) {
                    ArrayList<String> cantones = obtenerCantones(provincia);
                    if (cantones != null) {
                        String canton = (String) JOptionPane.showInputDialog(null, "Seleccione el cantón", "Cantones", JOptionPane.QUESTION_MESSAGE, null, cantones.toArray(), cantones.get(0));
                        if (canton != null) {
                            ArrayList<String> distritos = obtenerDistritos(canton);
                            if (distritos != null) {
                                String distrito = (String) JOptionPane.showInputDialog(null, "Seleccione el distrito", "Distritos", JOptionPane.QUESTION_MESSAGE, null, distritos.toArray(), distritos.get(0));

                                if (distrito != null) {
                                    int idProvincia = obtenerIDProv(provincia);
                                    int idCanton = obtenerIDCan(canton);
                                    int idDistrito = obtenerIDDis(distrito);

                                    Statement st = null;
                                    ResultSet rs = null;

                                    try {
                                        CallableStatement cs = gestor.getConexion().prepareCall("{CALL SP_INS_FIL(?,?,?,?,?,?)}");
                                        cs.setString(1, nombre.toUpperCase());
                                        cs.setInt(2, 0);
                                        cs.setInt(3, idProvincia);
                                        cs.setInt(4, idCanton);
                                        cs.setInt(5, idDistrito);
                                        cs.setString(6, "S");
                                        cs.execute();

                                        DefaultTableModel modelo = (DefaultTableModel) tbFiliales.getModel();
                                        modelo.addRow(new Object[]{idFilial, nombre, 0, provincia, canton, distrito, "S"});
                                    } catch (SQLException e) {
                                        System.err.println("Error:" + e);

                                    } finally {
                                        gestor.cerrar();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void eliminarFilial(int fila, int idFilial, JTable tbFiliales) {
        String[] respuesta = {"Si", "No"};
        int res = JOptionPane.showOptionDialog(null, "¿Está seguro que desea eliminar el filial?", "Eliminar", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, respuesta, respuesta[0]);

        if (res == 0) {
            try {
                CallableStatement cs = gestor.getConexion().prepareCall("{CALL SP_DEL_FIL(?)}");
                cs.setInt(1, idFilial);
                cs.execute();

                ((DefaultTableModel) tbFiliales.getModel()).removeRow(fila);

            } catch (Exception e) {
                System.err.println("Error:" + e);
            }
        }
    }

    public void modificarFilial() {

    }

    public ArrayList<String> obtenerProvincias() {

        ArrayList<String> provincias = new ArrayList();
        String consulta = "SELECT * FROM PROVINCIAS";
        try {
            Statement st = gestor.getConexion().createStatement();
            ResultSet rs = st.executeQuery(consulta);
            while (rs.next()) {
                provincias.add(rs.getString(2));
            }
            setChanged();
            notifyObservers("CARGANDO PROVINCIAS");
            return provincias;
        } catch (SQLException e) {
            System.err.println("Error:" + e);
        } finally {
            gestor.cerrar();
        }

        return null;
    }

    public ArrayList<String> obtenerCantones(String provincia) {

        String consulta = "";
        if (provincia.equals("ALAJUELA")) {
            consulta = "SELECT * FROM CANTONES WHERE ID_CANTON = 1 UNION SELECT * FROM CANTONES WHERE ID_CANTON = 2";
        } else if (provincia.equals("HEREDIA")) {
            consulta = "SELECT * FROM CANTONES WHERE ID_CANTON = 3 UNION SELECT * FROM CANTONES WHERE ID_CANTON = 4";
        } else if (provincia.equals("SAN JOSÉ")) {
            consulta = "SELECT * FROM CANTONES WHERE ID_CANTON = 5 UNION SELECT * FROM CANTONES WHERE ID_CANTON = 6";
        } else if (provincia.equals("CARTAGO")) {
            consulta = "SELECT * FROM CANTONES WHERE ID_CANTON = 7 UNION SELECT * FROM CANTONES WHERE ID_CANTON = 8";
        } else if (provincia.equals("PUNTARENAS")) {
            consulta = "SELECT * FROM CANTONES WHERE ID_CANTON = 9 UNION SELECT * FROM CANTONES WHERE ID_CANTON = 10";
        } else if (provincia.equals("LIMÓN")) {
            consulta = "SELECT * FROM CANTONES WHERE ID_CANTON = 11 UNION SELECT * FROM CANTONES WHERE ID_CANTON = 12";
        } else if (provincia.equals("GUANACASTE")) {
            consulta = "SELECT * FROM CANTONES WHERE ID_CANTON = 13 UNION SELECT * FROM CANTONES WHERE ID_CANTON = 14";
        }

        ArrayList<String> cantones = new ArrayList();
        try {
            Statement st = gestor.getConexion().createStatement();
            ResultSet rs = st.executeQuery(consulta);
            while (rs.next()) {
                cantones.add(rs.getString(2));
            }
            setChanged();
            notifyObservers("CARGANDO PROVINCIAS");
            return cantones;
        } catch (SQLException e) {
            System.err.println("Error:" + e);
        } finally {
            gestor.cerrar();
        }

        return null;

    }

    public ArrayList<String> obtenerDistritos(String canton) {

        String consulta = "";
        if (canton.equals("ALAJUELA")) {
            consulta = "SELECT * FROM DISTRITOS WHERE ID_DISTRITO = 1 UNION SELECT * FROM DISTRITOS WHERE ID_DISTRITO = 3";
        } else if (canton.equals("NARANJO")) {
            consulta = "SELECT * FROM DISTRITOS WHERE ID_DISTRITO = 2 UNION SELECT * FROM DISTRITOS WHERE ID_DISTRITO = 14";
        } else if (canton.equals("HEREDIA")) {
            consulta = "SELECT * FROM DISTRITOS WHERE ID_DISTRITO = 4 UNION SELECT * FROM DISTRITOS WHERE ID_DISTRITO = 15";
        } else if (canton.equals("BARVA")) {
            consulta = "SELECT * FROM DISTRITOS WHERE ID_DISTRITO = 6 UNION SELECT * FROM DISTRITOS WHERE ID_DISTRITO = 7";
        } else if (canton.equals("SAN JOSÉ")) {
            consulta = "SELECT * FROM DISTRITOS WHERE ID_DISTRITO = 9 UNION SELECT * FROM DISTRITOS WHERE ID_DISTRITO = 8";
        } else if (canton.equals("DESAMPARADOS")) {
            consulta = "SELECT * FROM DISTRITOS WHERE ID_DISTRITO = 11 UNION SELECT * FROM DISTRITOS WHERE ID_DISTRITO = 10";
        } else if (canton.equals("CARTAGO")) {
            consulta = "SELECT * FROM DISTRITOS WHERE ID_DISTRITO = 13 UNION SELECT * FROM DISTRITOS WHERE ID_DISTRITO = 14";
        } else if (canton.equals("PARAÍSO")) {
            consulta = "SELECT * FROM DISTRITOS WHERE ID_DISTRITO = 3 UNION SELECT * FROM DISTRITOS WHERE ID_DISTRITO = 4";
        } else if (canton.equals("PUNTARENAS")) {
            consulta = "SELECT * FROM DISTRITOS WHERE ID_DISTRITO = 5 UNION SELECT * FROM DISTRITOS WHERE ID_DISTRITO = 6";
        } else if (canton.equals("ESPARZA")) {
            consulta = "SELECT * FROM DISTRITOS WHERE ID_DISTRITO = 7 UNION SELECT * FROM DISTRITOS WHERE ID_DISTRITO = 8";
        } else if (canton.equals("LIMÓN")) {
            consulta = "SELECT * FROM DISTRITOS WHERE ID_DISTRITO = 9 UNION SELECT * FROM DISTRITOS WHERE ID_DISTRITO = 10";
        } else if (canton.equals("GUAPILES")) {
            consulta = "SELECT * FROM DISTRITOS WHERE ID_DISTRITO = 11 UNION SELECT * FROM DISTRITOS WHERE ID_DISTRITO = 12";
        } else if (canton.equals("LIBERIA")) {
            consulta = "SELECT * FROM DISTRITOS WHERE ID_DISTRITO = 13 UNION SELECT * FROM DISTRITOS WHERE ID_DISTRITO = 14";
        } else if (canton.equals("NICOYA")) {
            consulta = "SELECT * FROM DISTRITOS WHERE ID_DISTRITO = 13 UNION SELECT * FROM DISTRITOS WHERE ID_DISTRITO = 14";
        }
        ArrayList<String> cantones = new ArrayList();
        try {
            Statement st = gestor.getConexion().createStatement();
            ResultSet rs = st.executeQuery(consulta);
            while (rs.next()) {
                cantones.add(rs.getString(2));
            }
            setChanged();
            notifyObservers("CARGANDO PROVINCIAS");
            return cantones;
        } catch (SQLException e) {
            System.err.println("Error:" + e);
        } finally {
            gestor.cerrar();
        }

        return null;
    }

    public int obtenerIDProv(String provincia) {
        int id = 0;
        Statement st = null;
        ResultSet rs = null;

        try {
            st = gestor.getConexion().createStatement();
            rs = st.executeQuery("SELECT ID_PROVINCIA FROM PROVINCIAS WHERE DESCRIPCION ='" + provincia + "'");
            if (rs.next()) {
                id = rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("ERROR AL OBTENER IDProvincia " + e);
        } finally {
            gestor.cerrar();
        }

        return id;
    }

    public int obtenerIDCan(String canton) {
        int id = 0;
        Statement st = null;
        ResultSet rs = null;

        try {
            st = gestor.getConexion().createStatement();
            rs = st.executeQuery("SELECT ID_CANTON FROM CANTONES WHERE DESCRIPCION ='" + canton + "'");
            if (rs.next()) {
                id = rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("ERROR AL OBTENER IDCanton " + e);
        } finally {
            gestor.cerrar();
        }

        return id;
    }

    public int obtenerIDDis(String distrito) {
        int id = 0;
        Statement st = null;
        ResultSet rs = null;

        try {
            st = gestor.getConexion().createStatement();
            rs = st.executeQuery("SELECT ID_DISTRITO FROM DISTRITOS WHERE DESCRIPCION ='" + distrito + "'");
            if (rs.next()) {
                id = rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("ERROR AL OBTENER IDDistrito " + e);
        } finally {
            gestor.cerrar();
        }

        return id;
    }

    public void cargarFiliales(JTable tabla) {
        try {
            String cadena = "SELECT FIL.ID_FILIAL, FIL.NOMBRE, FIL.CANT_APART, PROV.DESCRIPCION,"
                    + " CAN.DESCRIPCION, DIS.DESCRIPCION, FIL.HABILITADO FROM FILIALES FIL JOIN PROVINCIAS PROV "
                    + "ON (FIL.ID_PROVINCIA = PROV.ID_PROVINCIA) JOIN CANTONES CAN ON (FIL.ID_CANTON = CAN.ID_CANTON) "
                    + "JOIN DISTRITOS DIS ON (FIL.ID_DISTRITO = DIS.ID_DISTRITO)";
            Statement st = gestor.getConexion().createStatement();
            ResultSet rs = st.executeQuery(cadena);
            while (rs.next()) {
                DefaultTableModel filiales = (DefaultTableModel) tabla.getModel();
                filiales.addRow(new Object[]{rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7)});
            }
            setChanged();
            notifyObservers("CARGANDO FILIALES");
        } catch (SQLException e) {
            System.err.println("Error:" + e);
        } finally {
            gestor.cerrar();
        }
    }

}
