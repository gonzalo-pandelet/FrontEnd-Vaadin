package org.vaadin.example;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.textfield.TextField;

import org.springframework.beans.factory.annotation.Autowired;

@Route("/AñadirProductos")
public class AñadirProducto extends VerticalLayout {
    @Autowired
    private Api api;

    private TextField Nombre = new TextField("Nombre");
    private TextField categoria = new TextField("Categoria");
    private TextField precio = new TextField("Precio");
    private TextField EAN13 = new TextField("Codigo de barras");

    public AñadirProducto(@Autowired Api api) {
        this.api = api;

        // Configura el formulario
        FormLayout formLayout = new FormLayout(Nombre, categoria, precio, EAN13);

        Button guardar = new Button("Guardar", event -> {
            try {
                agregarNuevoProducto();
            } catch (Exception e) {
                e.printStackTrace();
                Notification.show("Error al agregar el producto: " + e.getMessage(), 3000, Notification.Position.MIDDLE);
            }
        });

        Button Volver = new Button("Volver", event -> {
            try {
                UI.getCurrent().navigate(Navbar.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // Agrega los componentes al diálogo
        add(formLayout, guardar,Volver);
    }

    private void agregarNuevoProducto() {
        // Validación básica
        if (Nombre.isEmpty() || categoria.isEmpty() || EAN13.isEmpty() || precio.isEmpty()) {
            Notification.show("Todos los campos son obligatorios", 3000, Notification.Position.MIDDLE);
            return;
        }

        try {
            Producto nuevoProducto = new Producto();
            nuevoProducto.setNombre(Nombre.getValue());
            nuevoProducto.setCategoria(categoria.getValue());
            nuevoProducto.setEAN13(EAN13.getValue());
            nuevoProducto.setPrecio(precio.getValue());

            api.agregarProductoAPI(nuevoProducto);

            // Muestra un mensaje de éxito al usuario
            Notification.show("Producto agregado correctamente", 3000, Notification.Position.MIDDLE);
        } catch (Exception e) {
            e.printStackTrace();
            // Puedes mostrar un mensaje de error más específico si es necesario
            Notification.show("Error al agregar el producto: " + e.getMessage(), 3000, Notification.Position.MIDDLE);
        }
    }
}
