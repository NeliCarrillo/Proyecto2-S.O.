package Objetos;

/**
 *
 * @author nelsoncarrillo
 */
public class Directorio {
    private String padre;
    private String nombre;

    public Directorio(String padre, String nombre) {
        this.padre = padre;
        this.nombre = nombre;
    }
    
    public Directorio(String nombre) {
        this.padre = "FileSystem";
        this.nombre = nombre;
    }

    public String getPadre() {
        return padre;
    }

    public void setPadre(String padre) {
        this.padre = padre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

}
