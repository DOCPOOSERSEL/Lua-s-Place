package Clases;

public class proveedor {

    private int idProv;
    private String nombreProv;
    private String calleProv;
    private String ciudadProv;
    private String cpProv;
    private String telefonoProv;
    private String fisMorProv; // ENUM 'f' / 'm'


    public proveedor() {}

    public int getIdProv() {
        return idProv;
    }

    public void setIdProv(int idProv) {
        this.idProv = idProv;
    }

    public String getNombreProv() {
        return nombreProv;
    }

    public void setNombreProv(String nombreProv) {
        this.nombreProv = nombreProv;
    }

    public String getCalleProv() {
        return calleProv;
    }

    public void setCalleProv(String calleProv) {
        this.calleProv = calleProv;
    }

    public String getCiudadProv() {
        return ciudadProv;
    }

    public void setCiudadProv(String ciudadProv) {
        this.ciudadProv = ciudadProv;
    }

    public String getCpProv() {
        return cpProv;
    }

    public void setCpProv(String cpProv) {
        this.cpProv = cpProv;
    }

    public String getTelefonoProv() {
        return telefonoProv;
    }

    public void setTelefonoProv(String telefonoProv) {
        this.telefonoProv = telefonoProv;
    }

    public String getFisMorProv() {
        return fisMorProv;
    }

    public void setFisMorProv(String fisMorProv) {
        this.fisMorProv = fisMorProv;
    }
}
