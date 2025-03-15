package proyecto2.s.o;
import GUIs.FileSystemSimulator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author nelsoncarrillo
 */
public class Proyecto2SO {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            // TODO code application logic here
            FileSystemSimulator proyecto = new FileSystemSimulator();
        } catch (NoSuchFieldException ex) {
            Logger.getLogger(Proyecto2SO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(Proyecto2SO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(Proyecto2SO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
