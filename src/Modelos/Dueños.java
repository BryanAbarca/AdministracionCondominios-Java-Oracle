package Modelos;

import Conexion.GestorBD;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class Dueños extends Observable {

    private String id = "";
    private String nombre = "";
    private String ape1 = "";
    private String ape2 = "";
    private String habilitado = "";
    private GestorBD gestor = null;

    public Dueños() {
        this.gestor = new GestorBD();
    }

    public void agregarObservadorDueños(Observer observador) {
        addObserver(observador);
        setChanged();
        notifyObservers("Observador dueños");
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApe1() {
        return ape1;
    }

    public void setApe1(String ape1) {
        this.ape1 = ape1;
    }

    public String getApe2() {
        return ape2;
    }

    public void setApe2(String ape2) {
        this.ape2 = ape2;
    }

    public String getHabilitado() {
        return habilitado;
    }

    public void setHabilitado(String habilitado) {
        this.habilitado = habilitado;
    }

    public void cargarDueños(JTable tabla) {
        try {
            String cadena = "SELECT * FROM DUEÑOS";
            Statement st = gestor.getConexion().createStatement();
            ResultSet rs = st.executeQuery(cadena);
            while (rs.next()) {
                String habilitado = rs.getString(5);
                if (habilitado.equals("S")) {
                    habilitado = "HABILITADO";
                } else {
                    habilitado = "NO HABILITADO";
                }
                DefaultTableModel filiales = (DefaultTableModel) tabla.getModel();
                filiales.addRow(new Object[]{
                    rs.getString(1),
                    rs.getString(2) + " " + rs.getString(3) + " " + rs.getString(4),
                    habilitado
                });
            }
            setChanged();
            notifyObservers("CARGANDO DUEÑOS");
        } catch (SQLException e) {
            System.err.println("Error al cargar Dueños:" + e);
        } finally {
            gestor.cerrar();
        }
    }

    public void eliminarDueño(String idDueño, int fila, JTable table) {
        int opcRespuesta = JOptionPane.showConfirmDialog(null, "¿Desea eliminar al dueño seleccionado?");

        if (opcRespuesta == JOptionPane.YES_NO_OPTION) {
            try {
                CallableStatement cs = gestor.getConexion().prepareCall("{CALL SP_DEL_DUE(?)}");
                cs.setString(1, idDueño);
                cs.execute();

                ((DefaultTableModel) table.getModel()).removeRow(fila);

            } catch (SQLException e) {
                System.err.println("ERROR AL ELIMINAR DUEÑO: " + e);
            } finally {
                gestor.cerrar();
                gestor = null;
            }
        }
    }

    public void editarDueño(String idDueño, int fila, JTable table) throws SQLException {
        int opcRespuesta = JOptionPane.showConfirmDialog(null, "¿Desea cambiar el estado del dueño seleccionado?");

        if (opcRespuesta == JOptionPane.YES_NO_OPTION) {
            String nombre = "";
            String ape1 = "";
            String ape2 = "";
            String opcHabilitado = "";

            try {
                String cadena = "SELECT * FROM DUEÑOS WHERE ID_DUEÑO = '" + idDueño + "'";
                Statement st = gestor.getConexion().createStatement();
                ResultSet rs = st.executeQuery(cadena);
                while (rs.next()) {
                    nombre = rs.getString(2);
                    ape1 = rs.getString(3);
                    ape2 = rs.getString(4);
                    opcHabilitado = rs.getString(5);
                }
                setChanged();
                notifyObservers("CARGANDO DUEÑOS");
            } catch (SQLException e) {
                System.err.println("Error al cargar para editar Dueños:" + e);
            } finally {
                gestor.cerrar();
            }

            if (opcHabilitado.equals("S")) {
                try {
                    CallableStatement cs = gestor.getConexion().prepareCall("{CALL SP_UPD_DUE(?, ?, ?, ?, ?)}");
                    cs.setString(1, idDueño);
                    cs.setString(2, nombre);
                    cs.setString(3, ape1);
                    cs.setString(4, ape2);
                    cs.setString(5, "N");
                    cs.execute();

                    DefaultTableModel modelo = (DefaultTableModel) table.getModel();
                    modelo.setValueAt("NO HABILITADO", fila, 2);

                } catch (SQLException e) {
                    System.err.println("ERROR opc SI al editar dueño:" + e);
                } finally {
                    gestor.cerrar();
                }
            } else {
                try {
                    CallableStatement cs = gestor.getConexion().prepareCall("{CALL SP_UPD_DUE(?, ?, ?, ?, ?)}");
                    cs.setString(1, idDueño);
                    cs.setString(2, nombre);
                    cs.setString(3, ape1);
                    cs.setString(4, ape2);
                    cs.setString(5, "S");
                    cs.execute();

                    DefaultTableModel modelo = (DefaultTableModel) table.getModel();
                    modelo.setValueAt("HABILITADO", fila, 2);

                } catch (SQLException e) {
                    System.err.println("ERROR opc NO al editar dueño:" + e);
                } finally {
                    gestor.cerrar();
                }
            }

        }
    }

    public void agregarDueño(JTable table) throws SQLException {
        String var_id = (String) JOptionPane.showInputDialog(null, "Digite el ID del nuevo dueño");

        if (var_id != null && !var_id.isBlank() && !var_id.isEmpty() && !var_id.equals("")) {
            if (var_id.length() >= 9 && var_id.length() <= 15) {
                System.out.println(var_id);

                String var_nombre = (String) JOptionPane.showInputDialog(null, "Digite el nombre del nuevo dueño");

                if (var_nombre != null && !var_nombre.isBlank() && !var_nombre.isEmpty() && !var_nombre.equals("")) {
                    if (var_nombre.length() <= 15) {
                        System.out.println(var_nombre);

                        String var_ape1 = (String) JOptionPane.showInputDialog(null, "Digite el primer apellido del nuevo dueño");

                        if (var_ape1 != null && !var_ape1.isBlank() && !var_ape1.isEmpty() && !var_ape1.equals("")) {
                            if (var_nombre.length() <= 15) {
                                System.out.println(var_ape1);
                                String var_ape2 = (String) JOptionPane.showInputDialog(null, "Digite el segundo apellido del nuevo dueño");
                                if (var_ape2 != null && !var_ape2.isBlank() && !var_ape2.isEmpty() && !var_ape2.equals("")) {
                                    if (var_ape2.length() <= 15) {

                                        try {
                                            //EXECUTE SP_INS_DUE ('117180305', 'JEFTE', 'VEGA', 'HIDALGO', 'S');
                                            CallableStatement cs = gestor.getConexion().prepareCall("{CALL SP_INS_DUE(?, ?, ?, ?, ?)}");
                                            cs.setString(1, var_id.toUpperCase());
                                            cs.setString(2, var_nombre.toUpperCase());
                                            cs.setString(3, var_ape1.toUpperCase());
                                            cs.setString(4, var_ape2.toUpperCase());
                                            cs.setString(5, "S");
                                            cs.execute();

                                            DefaultTableModel modelo = (DefaultTableModel) table.getModel();
                                            modelo.addRow(new Object[]{var_id.toUpperCase(), var_nombre.toUpperCase() + " " + var_ape1.toUpperCase() + " " + var_ape2.toUpperCase(), "HABILITADO"});

                                        } catch (SQLException e) {
                                            System.err.println("Error:" + e);

                                        } finally {
                                            gestor.cerrar();
                                        }

                                    } else {
                                        JOptionPane.showMessageDialog(null, "El SEGUNDO APELLIDO debe contener maximo 15 digitos", "Dialog", JOptionPane.ERROR_MESSAGE);
                                        System.err.println("El SEGUNDO APELLIDO debe contener maximo 15 digitos");
                                    }
                                }
                            } else {
                                JOptionPane.showMessageDialog(null, "El PRIMER APELLIDO debe contener maximo 15 digitos", "Dialog", JOptionPane.ERROR_MESSAGE);
                                System.err.println("El PRIMER APELLIDO debe contener maximo 15 digitos");
                            }
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "El NOMBRE debe contener maximo 15 digitos", "Dialog", JOptionPane.ERROR_MESSAGE);
                        System.err.println("El NOMBRE debe contener maximo 15 digitos");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "El ID debe contener entre 9 y 15 digitos", "Dialog", JOptionPane.ERROR_MESSAGE);
                System.err.println("El ID debe contener entre 9 y 15 digitos");
            }
        }
    }
}
