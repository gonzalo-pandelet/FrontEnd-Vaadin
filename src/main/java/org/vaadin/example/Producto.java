package org.vaadin.example;

public class Producto {
    private String Nombre;
    private String Categoria;
    private String precio;
    private String EAN13;

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public void setCategoria(String categoria) {
        Categoria = categoria;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public void setEAN13(String EAN13) {
        this.EAN13 = EAN13;
    }

    public String getNombre() {
        return Nombre;
    }

    public String getCategoria() {
        return Categoria;
    }

    public String getPrecio() {
        return precio;
    }

    public String getEAN13() {
        return EAN13;
    }
}
