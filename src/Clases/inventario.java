package Clases;

public class inventario {
    private int idInv;
    private double costoInv;
    private String nombreInv;
    private int cantidadInv;
    private int idProv;

    public inventario() {}

    public int getIdInv() {
        return idInv;
    }

    public void setIdInv(int idInv) {
        this.idInv = idInv;
    }

    public double getCostoInv() {
        return costoInv;
    }

    public void setCostoInv(double costoInv) {
        this.costoInv = costoInv;
    }

    public String getNombreInv() {
        return nombreInv;
    }

    public void setNombreInv(String nombreInv) {
        this.nombreInv = nombreInv;
    }

    public int getCantidadInv() {
        return cantidadInv;
    }

    public void setCantidadInv(int cantidadInv) {
        this.cantidadInv = cantidadInv;
    }

    public int getIdProv() {
        return idProv;
    }

    public void setIdProv(int idProv) {
        this.idProv = idProv;
    }
}
