package org.vaadin.example;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.vaadin.example.Producto;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;

@Service
public class Api {
    private static final String urlPrefix = "http://localhost:8080/%s";

    // FUNCION PARA MOSTRAR LOS PRODUCTOS
    public ArrayList<Producto> productos() throws URISyntaxException, IOException, InterruptedException {
        String fullUrl = String.format(urlPrefix, "Productos");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(fullUrl))
                .GET()
                .build();

        HttpResponse<String> response = HttpClient
                .newBuilder()
                .build()
                .send(request, HttpResponse.BodyHandlers.ofString());

        ArrayList<Producto> listProductos = parsearRespuesta(response.body());
        System.out.println(listProductos);
        return listProductos;
    }

    private ArrayList<Producto> parsearRespuesta(String respuesta) {
        ArrayList<Producto> productos = new ArrayList<>();

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            // Suponemos que la respuesta es un array JSON de productos
            productos = objectMapper.readValue(respuesta, new TypeReference<ArrayList<Producto>>() {});
        } catch (IOException e) {
            e.printStackTrace();
            // Manejar la excepción según tus necesidades
        }

        return productos;
    }

    // FUNCION PARA AGREGAR PRODUCTOS
    public void agregarProductoAPI(Producto nuevoProducto) throws URISyntaxException, IOException, InterruptedException {
        try {
            String fullUrl = String.format(urlPrefix, "Productos/Agregar");

            // Configura ObjectMapper para convertir el objeto Producto a InputStream
            ObjectMapper objectMapper = new ObjectMapper();
            InputStream inputStream = new ByteArrayInputStream(objectMapper.writeValueAsBytes(nuevoProducto));

            // Construye la solicitud HTTP con el objeto convertido a InputStream
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(fullUrl))
                    .POST(HttpRequest.BodyPublishers.ofInputStream(() -> inputStream))
                    .header("Content-Type", "application/json")
                    .build();

            // Envía la solicitud y obtiene la respuesta
            HttpResponse<String> response = HttpClient
                    .newBuilder()
                    .build()
                    .send(request, HttpResponse.BodyHandlers.ofString());

            // Verifica si la solicitud fue exitosa (código de respuesta 2xx)
            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                System.out.println("Producto agregado correctamente");
            } else {
                System.out.println("Error al agregar el producto. Código de respuesta: " + response.statusCode());
                System.out.println("Respuesta del servidor: " + response.body());
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error durante la solicitud HTTP: " + e.getMessage());
        }
    }

    // FUNCION PARA ELIMINAR PRODUCTOS

    public void eliminarProducto(Producto productoEliminar) throws URISyntaxException, IOException, InterruptedException {
        try {
            String fullUrl = String.format(urlPrefix, "Productos/Eliminar");

            // Configura ObjectMapper para convertir el objeto Producto a JSON
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonProductoEliminar = objectMapper.writeValueAsString(productoEliminar);

            // Construye la solicitud HTTP DELETE con el objeto JSON en el cuerpo
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(fullUrl))
                    .DELETE()
                    .header("Content-Type", "application/json")
                    .method("DELETE", HttpRequest.BodyPublishers.ofString(jsonProductoEliminar))
                    .build();

            // Envía la solicitud y obtiene la respuesta
            HttpResponse<String> response = HttpClient
                    .newBuilder()
                    .build()
                    .send(request, HttpResponse.BodyHandlers.ofString());

            // Verifica si la solicitud fue exitosa (código de respuesta 2xx)
            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                System.out.println("Producto eliminado correctamente");
                System.out.println(productoEliminar.getNombre());

            } else {
                System.out.println("Error al eliminar el producto. Código de respuesta: " + response.statusCode());
                System.out.println("Respuesta del servidor: " + response.body());
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error durante la solicitud HTTP: " + e.getMessage());
        }
    }

    // FUNCION PARA ACTUALIZAR PRODUCTOS
    public void actualizarProducto(Producto productoActualizar) throws URISyntaxException, IOException, InterruptedException {
        try {
            String fullUrl = String.format(urlPrefix, "Productos/Actualizar"); // Cambiar "Actualizar" al endpoint correcto

            // Configura ObjectMapper para convertir el objeto Producto a JSON
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonProductoActualizar = objectMapper.writeValueAsString(productoActualizar);

            // Construye la solicitud HTTP PUT con el objeto JSON en el cuerpo
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(fullUrl))
                    .PUT(HttpRequest.BodyPublishers.ofString(jsonProductoActualizar))
                    .header("Content-Type", "application/json")
                    .build();

            // Envía la solicitud y obtiene la respuesta
            HttpResponse<String> response = HttpClient
                    .newBuilder()
                    .build()
                    .send(request, HttpResponse.BodyHandlers.ofString());

            // Verifica si la solicitud fue exitosa (código de respuesta 2xx)
            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                System.out.println("Producto actualizado correctamente");
                System.out.println(productoActualizar.getNombre());
            } else {
                System.out.println("Error al actualizar el producto. Código de respuesta: " + response.statusCode());
                System.out.println("Respuesta del servidor: " + response.body());
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error durante la solicitud HTTP: " + e.getMessage());
        }
    }




}
