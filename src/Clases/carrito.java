package Clases;

public class carrito {
    private int idCar;
    private Integer idProd; // Opcional
    private int cantidadProd;
    private Integer idPan;  // Opcional
    private int cantidadPan;

    public carrito() {}

    public int getIdCar() {
        return idCar;
    }

    public void setIdCar(int idCar) {
        this.idCar = idCar;
    }

    public Integer getIdProd() {
        return idProd;
    }

    public void setIdProd(Integer idProd) {
        this.idProd = idProd;
    }

    public int getCantidadProd() {
        return cantidadProd;
    }

    public void setCantidadProd(int cantidadProd) {
        this.cantidadProd = cantidadProd;
    }

    public Integer getIdPan() {
        return idPan;
    }

    public void setIdPan(Integer idPan) {
        this.idPan = idPan;
    }

    public int getCantidadPan() {
        return cantidadPan;
    }

    public void setCantidadPan(int cantidadPan) {
        this.cantidadPan = cantidadPan;
    }
}
