package Funciones;

public class itemCombo {
    public int id;
    public String nombre;

    public itemCombo(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return nombre; // <-- MOSTRAR SOLO EL NOMBRE
    }
}
