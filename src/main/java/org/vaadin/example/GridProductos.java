package org.vaadin.example;


import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import org.springframework.beans.factory.annotation.Autowired;

import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collection;

@Route("/Productos")
public class GridProductos extends VerticalLayout {

   // private final ServicioProducto productService;
    private final Api API;
    public GridProductos(@Autowired Api API) throws URISyntaxException, IOException, InterruptedException {

        this.API = API;

        Grid<Producto> grid = new Grid<>(Producto.class, false);

        grid.addColumn(Producto::getNombre).setHeader("Nombre");
        grid.addColumn(Producto::getCategoria).setHeader("Categoria");
        grid.addColumn(Producto::getEAN13).setHeader("EAN13");
        grid.addColumn(Producto::getPrecio).setHeader("Precio");

        grid.setItems(API.productos());

        Button btnEliminar = new Button("Eliminar");
        Button btnActualizar = new Button("Actualizar");

        // Agregar el botón Eliminar inicialmente oculto
        btnActualizar.setVisible(false);

        // Agregar el botón Eliminar inicialmente oculto
        btnEliminar.setVisible(false);

        btnEliminar.addClickListener(event -> {
            //seleccionar producto del grid
            Producto productoEliminar = grid.asSingleSelect().getValue();

            if (productoEliminar != null) {
                try {
                    API.eliminarProducto(productoEliminar);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                // actualizamos grid con los nuevos productos
                try {
                    grid.setItems(API.productos());
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
              //  remove(btnEliminar); // Opcional: quitar el botón después de eliminar
            } else {
                Notification.show("Selecciona un producto antes de eliminar");
            }
        });

        btnActualizar.addClickListener(event -> {
            // Seleccionar producto del grid
            Producto productoActualizar = grid.asSingleSelect().getValue();

            if (productoActualizar != null) {
                // Crear una instancia de la ventana modal
                ModalEditarProductos modalEditarProducto = new ModalEditarProductos(API,productoActualizar);

                // Abrir la ventana modal
                modalEditarProducto.open();

                // actualizamos grid con los nuevos productos
                try {
                    grid.setItems(API.productos());
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            } else {
                Notification.show("Selecciona un producto antes de actualizar");
            }
        });

        // Manejar el evento de selección del grid
        grid.asSingleSelect().addValueChangeListener(event -> {
            btnEliminar.setVisible(event.getValue() != null);
            btnActualizar.setVisible(event.getValue() != null);
        });

        Button Volver = new Button("Volver", event -> {
            try {
                UI.getCurrent().navigate(Navbar.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        add(grid,btnEliminar,btnActualizar,Volver);

    }



}
