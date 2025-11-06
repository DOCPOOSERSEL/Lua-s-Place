package Datos;
import Clases.*;
import java.util.ArrayList;
import java.util.List;

public class guardadoLocal {
    private static List<proveedor> proveedores = new ArrayList<>();
    private static List<cliente> clientes = new ArrayList<>();
    private static List<empleado> empleados = new ArrayList<>();
    private static List<inventario> ingredientes = new ArrayList<>();
    private static List<pancasero> panCasero = new ArrayList<>();
    private static List<producto> productos = new ArrayList<>();
    private static List<receta> recetas = new ArrayList<>();
    private static List<carrito> carrito = new ArrayList<>();
    private static List<venta> ventas = new ArrayList<>();

    public static void agregarProveedor(proveedor p) {
        proveedores.add(p);
    }

    public static void agregarCliente(cliente c) {
        clientes.add(c);
    }

    public static void agregarEmpleado(empleado e) {
        empleados.add(e);
    }

    public static void agregarIngrediente(inventario i) {
        ingredientes.add(i);
    }

    public static void agregarPanCasero(pancasero pc) {
        panCasero.add(pc);
    }

    public static void agregarProducto(producto p) {
        productos.add(p);
    }

    public static void agregarReceta(receta r) {
        recetas.add(r);
    }

    public static void agregarCarrito(carrito c) {
        carrito.add(c);
    }

    public static void agregarVenta(venta v) {
        ventas.add(v);
    }



    public static List<proveedor> getProveedores() {
        return proveedores;
    }

    public static List<cliente> getClientes() {
        return clientes;
    }

    public static List<empleado> getEmpleados() {
        return empleados;
    }

    public static List<inventario> getIngredientes() {
        return ingredientes;
    }

    public static List<pancasero> getPanCasero() {
        return panCasero;
    }

    public static List<producto> getProductos() {
        return productos;
    }

    public static List<receta> getRecetas() {
        return recetas;
    }

    public static List<carrito> getCarrito() {
        return carrito;
    }

    public static List<venta> getVentas() {
        return ventas;
    }
}

