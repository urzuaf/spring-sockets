package com.example.chatapp.controller;

import org.springframework.web.bind.annotation.*;
import com.example.chatapp.model.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import java.util.Map;
import java.util.HashMap;

@RestController
@Controller
public class ChatController {

    private static final String MESSAGES_FILE = "messages.json";
    private static final String USERS_FILE = "users.json";
    private static final String CONNECTIONS_FILE = "connections.json";

    private static final String BASE_URL = "http://34.176.108.55:8080/";
    private static final String ALTERNATIVE_URL = "http://34.176.108.55:8080/";
    private static final OkHttpClient client = new OkHttpClient();

    @PostMapping("/saveMessage")
    public boolean saveMessage(@RequestBody String mensajes) {
        String urlToUse = isServerHealthy(BASE_URL) ? BASE_URL : ALTERNATIVE_URL;
        try {
            saveFile(urlToUse + "messages/write", mensajes);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false; // Retorna false si ocurre algún error
        }
    }

    @GetMapping("/loadMessages")
    public String loadMessages() {
        String urlToUse = isServerHealthy(BASE_URL) ? BASE_URL : ALTERNATIVE_URL;
        try {
            return getFile(urlToUse + "messages/read");
        } catch (Exception ex) {
            ex.printStackTrace();
            return "{}"; // Devolver JSON vacío en caso de error también al intentar la petición HTTP
        }
    }

    @PostMapping("/saveConnection")
    public boolean saveConnections(@RequestBody String mensajes) {
        String urlToUse = isServerHealthy(BASE_URL) ? BASE_URL : ALTERNATIVE_URL;
        try {
            saveFile(urlToUse + "connections/write", mensajes);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false; // Retorna false si ocurre algún error
        }
    }

    @GetMapping("/loadConnections")
    public String loadConnections() {
        String urlToUse = isServerHealthy(BASE_URL) ? BASE_URL : ALTERNATIVE_URL;
        try {
            return getFile(urlToUse + "connections/read");
        } catch (Exception ex) {
            ex.printStackTrace();
            return "{}"; // Devolver JSON vacío en caso de error también al intentar la petición HTTP
        }
    }

    @PostMapping("/saveUser")
    public boolean saveUser(@RequestBody String mensajes) {
        String urlToUse = isServerHealthy(BASE_URL) ? BASE_URL : ALTERNATIVE_URL;
        try {
            saveFile(urlToUse + "users/write", mensajes);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false; // Retorna false si ocurre algún error
        }
    }

    @GetMapping("/loadUsers")
    public String loadUsers() {
        String urlToUse = isServerHealthy(BASE_URL) ? BASE_URL : ALTERNATIVE_URL;
        try {
            return getFile(urlToUse + "users/read");
        } catch (Exception ex) {
            ex.printStackTrace();
            return "{}"; // Devolver JSON vacío en caso de error también al intentar la petición HTTP
        }
    }

    /*
     * @GetMapping("/loadConnections")
     * public String loadConnections() {
     * try {
     * Path path = Paths.get(CONNECTIONS_FILE);
     * 
     * // Leer y devolver el contenido completo del archivo JSON como String
     * String jsonContent = Files.readString(path);
     * // System.out.println("Contenido del archivo JSON: " + jsonContent); //
     * // Verificar contenido
     * 
     * return jsonContent;
     * } catch (IOException e) {
     * e.printStackTrace();
     * return "{}"; // Devolver JSON vacío en caso de error
     * }
     * }
     */

    /*
     * 
     * 
     * @PostMapping("/saveUser")
     * public boolean saveUser(@RequestBody String mensajes) {
     * try {
     * Path path = Paths.get(USERS_FILE);
     * 
     * if (!Files.exists(path)) {
     * Files.createFile(path);
     * System.out.println("Archivo no existía, se ha creado: " + MESSAGES_FILE);
     * }
     * 
     * // Guardar el contenido en el archivo
     * Files.write(Paths.get(USERS_FILE), mensajes.getBytes(),
     * StandardOpenOption.CREATE,
     * StandardOpenOption.TRUNCATE_EXISTING);
     * 
     * // System.out.println("Mensaje guardado exitosamente en " + MESSAGES_FILE);
     * return true;
     * } catch (IOException e) {
     * e.printStackTrace();
     * return false; // Retorna false si ocurre algún error
     * }
     * }
     */

    /*
     * 
     * @PostMapping("/saveConnection")
     * public boolean saveConnection(@RequestBody String mensajes) {
     * try {
     * // Guardar el contenido en el archivo
     * Files.write(Paths.get(CONNECTIONS_FILE), mensajes.getBytes(),
     * StandardOpenOption.CREATE,
     * StandardOpenOption.TRUNCATE_EXISTING);
     * 
     * // System.out.println("Mensaje guardado exitosamente en " + MESSAGES_FILE);
     * return true;
     * } catch (IOException e) {
     * e.printStackTrace();
     * return false; // Retorna false si ocurre algún error
     * }
     * }
     */

    /*
     * 
     * @GetMapping("/loadUsers")
     * public String loadUsers() {
     * try {
     * Path path = Paths.get(USERS_FILE);
     * 
     * // Leer y devolver el contenido completo del archivo JSON como String
     * String jsonContent = Files.readString(path);
     * // System.out.println("Contenido del archivo JSON: " + jsonContent); //
     * // Verificar contenido
     * 
     * return jsonContent;
     * } catch (IOException e) {
     * e.printStackTrace();
     * return "{}"; // Devolver JSON vacío en caso de error
     * }
     * }
     */

    // Método para leer datos de un archivo en el servidor
    public static String getFile(String endpoint) throws IOException {
        Request request = new Request.Builder()
                .url(endpoint)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Error en la solicitud: " + response);
            }
            return response.body().string();
        }
    }

    // Método para guardar datos en un archivo en el servidor
    public static void saveFile(String endpoint, String jsonData) throws IOException {
        okhttp3.RequestBody body = okhttp3.RequestBody.create(jsonData, MediaType.get("application/json"));

        Request request = new Request.Builder()
                .url(endpoint)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Error en la solicitud: " + response);
            }
            System.out.println("Datos guardados exitosamente.");
        }
    }

    private static boolean isServerHealthy(String url) {
        Request request = new Request.Builder()
                .url(url + "health")
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.isSuccessful();
        } catch (IOException e) {
            e.printStackTrace();
            return false; // Si ocurre un error en la solicitud, asumimos que el servidor no está
                          // disponible.
        }
    }

    /*
     * @GetMapping("/health")
     * public String healthCheck() {
     * return "OK";
     * }
     */

    @GetMapping("/health")
    public ResponseEntity<Map<String, Boolean>> healthCheck() {
        if (isServerHealthy(BASE_URL)) {
            return ResponseEntity.ok(Map.of("ok", true));
        }
        if (isServerHealthy(ALTERNATIVE_URL)) {
            return ResponseEntity.ok(Map.of("ok", true));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("ok", false));
    }

    private final SimpMessagingTemplate messagingTemplate;
    private static final Set<String> connectedUsers = Collections.synchronizedSet(new HashSet<>());

    public ChatController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/connect")
    public void connectUser(String username) {
        connectedUsers.add(username);
        broadcastUserList();
    }

    @MessageMapping("/disconnect")
    public void disconnectUser(String username) {
        connectedUsers.remove(username);
        broadcastUserList();
    }

    private void broadcastUserList() {
        messagingTemplate.convertAndSend("/topic/users", connectedUsers);
    }

    public Set<String> getConnectedUsers() {
        return connectedUsers;
    }

    @MessageMapping("/sendMessage") // Maneja mensajes enviados a "/app/sendMessage"
    @SendTo("/topic/public") // Envía el mensaje a los suscriptores de "/topic/public"
    public Message sendMessage(Message message) {
        return message;
    }

    @MessageMapping("/medico")
    @SendTo("/topic/medico")
    public Message sendToMedico(Message message) {
        return message;
    }

    @MessageMapping("/pabellon")
    @SendTo("/topic/pabellon")
    public Message sendToPabellon(Message message) {
        return message;
    }

    @MessageMapping("/examen")
    @SendTo("/topic/examen")
    public Message sendToExamenes(Message message) {
        return message;
    }

    @MessageMapping("/auxiliar")
    @SendTo("/topic/auxiliar")
    public Message sendToAuxiliar(Message message) {
        return message;
    }

    @MessageMapping("/general")
    @SendTo("/topic/general")
    public Message sendToGeneral(Message message) {
        return message;
    }
}
