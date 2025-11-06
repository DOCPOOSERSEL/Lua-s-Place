package Clases;

public class receta {
    private int idReceta;
    private int idPan;
    private int idInv;
    private double cantidadIng;

    public receta() {}

    public int getIdReceta() {
        return idReceta;
    }
    public void setIdReceta(int idReceta) {
        this.idReceta = idReceta;
    }
    public int getIdPan() {
        return idPan;
    }
    public void setIdPan(int idPan) {
        this.idPan = idPan;
    }
    public int getIdInv() {
        return idInv;
    }
    public void setIdInv(int idInv) {
        this.idInv = idInv;
    }
    public double getCantidadIng() {
        return cantidadIng;
    }
    public void setCantidadIng(double cantidadIng) {
        this.cantidadIng = cantidadIng;
    }
}
