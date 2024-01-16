package org.vaadin.example;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@Route("/EditarProducto")
public class ModalEditarProductos extends Dialog {

    @Autowired
    private Api api;
    private Producto producto;

    private TextField nombre = new TextField("Nombre");
    private TextField categoria = new TextField("Categoria");
    private TextField precio = new TextField("Precio");
    private TextField ean13 = new TextField("Codigo de barras");

    public ModalEditarProductos(@Autowired Api api,Producto producto) {
        this.api = api;
        this.producto = producto;

        // Configura el formulario
        FormLayout formLayout = new FormLayout(nombre, categoria, precio, ean13);

        // Establece el contenido del formulario con los valores iniciales del producto
        setDatosProductoEnFormulario(producto);

        ean13.setReadOnly(true); // Hacer el campo de solo lectura

        Button guardar = new Button("Guardar", event -> {
            try {
                actualizarProducto();
                close();
            } catch (Exception e) {
                e.printStackTrace();
                Notification.show("Error al actualizar el producto: " + e.getMessage(), 3000, Notification.Position.MIDDLE);
            }
        });

        Button volver = new Button("Volver", event -> close());

        // Agrega los componentes al diálogo
        add(formLayout, guardar, volver);
    }

    // Método para establecer los datos del producto en el formulario del diálogo
    private void setDatosProductoEnFormulario(Producto producto) {
        // Utiliza un método auxiliar para establecer el valor en "-" si es nulo
        nombre.setValue(EsNulo(producto.getNombre()));
        categoria.setValue(EsNulo(producto.getCategoria()));
        ean13.setValue(EsNulo(producto.getEAN13()));
        precio.setValue(EsNulo(producto.getPrecio()));
    }

    private void actualizarProducto() {
        // Validación básica
        if (nombre.isEmpty() || categoria.isEmpty() || ean13.isEmpty() || precio.isEmpty()) {
            // En caso de campos vacíos, utiliza los valores predeterminados ya mostrados
            nombre.setValue(EsNulo(producto.getNombre()));
            categoria.setValue(EsNulo(producto.getCategoria()));
            ean13.setValue(EsNulo(producto.getEAN13()));
            precio.setValue(EsNulo(producto.getPrecio()));

            // Muestra un mensaje indicando que se utilizan valores predeterminados
            Notification.show("Se utilizaron valores predeterminados", 3000, Notification.Position.MIDDLE);
            return;
        }

        try {
            // Actualiza los datos del producto
            producto.setNombre(nombre.getValue());
            producto.setCategoria(categoria.getValue());
            producto.setPrecio(precio.getValue());

            // Llama a la API para actualizar el producto
            api.actualizarProducto(producto);

            // Muestra un mensaje de éxito al usuario
            Notification.show("Producto actualizado correctamente", 3000, Notification.Position.MIDDLE);
        } catch (Exception e) {
            e.printStackTrace();
            // Puedes mostrar un mensaje de error más específico si es necesario
            Notification.show("Error al actualizar el producto: " + e.getMessage(), 3000, Notification.Position.MIDDLE);
        }
    }

    // Método auxiliar para devolver '-' si el valor es nulo
    private String EsNulo(String value) {
        return value != null ? value : "-";
    }

}
