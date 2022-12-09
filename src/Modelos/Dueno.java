package Modelos;

import Conexion.GestorBD;
import Vistas.editarDueno;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Observable;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author hilla
 */
public class Dueno extends Observable{
    private GestorBD gestor = null;
    
    public Dueno() {
        this.gestor = new GestorBD();
    }
    public void verDueno(int idDueno) {
        editarDueno dueno = new editarDueno();
       //* dueno.iniciar(idDueno);

    }
     public void cargarDueno(JTable tabla) {
        try {
            String cadena = "SELECT * FROM DUEÑOS";
            Statement st = gestor.getConexion().createStatement();
            ResultSet rs = st.executeQuery(cadena);
            while (rs.next()) {
                DefaultTableModel duenos = (DefaultTableModel) tabla.getModel();
                duenos.addRow(new Object[]{rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5)});
            }
            setChanged();
            notifyObservers("CARGANDO DUEÑOS");
        } catch (SQLException e) {
            System.err.println("Error:" + e);
        } finally {
            gestor.cerrar();
        }
    }
}
