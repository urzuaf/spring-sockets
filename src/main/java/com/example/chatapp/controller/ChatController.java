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

@RestController
@Controller
public class ChatController {

    private static final String MESSAGES_FILE = "messages.json";
    private static final String USERS_FILE = "users.json";
    private static final String CONNECTIONS_FILE = "connections.json";

    @PostMapping("/saveMessage")
    public boolean saveMessage(@RequestBody String mensajes) {
        try {

            // Guardar el contenido en el archivo
            Files.write(Paths.get(MESSAGES_FILE), mensajes.getBytes(), StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING);

            // System.out.println("Mensaje guardado exitosamente en " + MESSAGES_FILE);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false; // Retorna false si ocurre algún error
        }
    }

    @GetMapping("/loadMessages")
    public String loadMessages() {
        try {
            Path path = Paths.get(MESSAGES_FILE);

            // Leer y devolver el contenido completo del archivo JSON como String
            String jsonContent = Files.readString(path);
            // System.out.println("Contenido del archivo JSON: " + jsonContent); //
            // Verificar contenido

            return jsonContent;
        }catch (NoSuchFileException e) {
           return "no archivo de mensajes";
        } 
        catch (IOException e) {
            e.printStackTrace();

            // Si falla, hacer una solicitud HTTP al servidor remoto para obtener el archivo
            try {
                System.out.println("Leyendo de servidor externo");
                String serverUrl = "http://34.176.232.182:8080/loadMessages";
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(serverUrl))
                        .build();

                // Realizar la solicitud GET
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() == 200) {
                    // Si la respuesta es exitosa, devolver el contenido del archivo desde el
                    // servidor
                    return response.body();
                } else {
                    // Si la respuesta no es exitosa, devolver un JSON vacío
                    return "{}";
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                return "{}"; // Devolver JSON vacío en caso de error también al intentar la petición HTTP
            }
        }
        
    }

    @GetMapping("/loadConnections")
    public String loadConnections() {
        try {
            Path path = Paths.get(CONNECTIONS_FILE);

            // Leer y devolver el contenido completo del archivo JSON como String
            String jsonContent = Files.readString(path);
            // System.out.println("Contenido del archivo JSON: " + jsonContent); //
            // Verificar contenido

            return jsonContent;
        } catch (IOException e) {
            e.printStackTrace();
            return "{}"; // Devolver JSON vacío en caso de error
        }
    }

    @PostMapping("/saveUser")
    public boolean saveUser(@RequestBody String mensajes) {
        try {
            Path path = Paths.get(USERS_FILE);

            if (!Files.exists(path)) {
                Files.createFile(path);
                System.out.println("Archivo no existía, se ha creado: " + MESSAGES_FILE);
            }

            // Guardar el contenido en el archivo
            Files.write(Paths.get(USERS_FILE), mensajes.getBytes(), StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING);

            // System.out.println("Mensaje guardado exitosamente en " + MESSAGES_FILE);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false; // Retorna false si ocurre algún error
        }
    }

    @PostMapping("/saveConnection")
    public boolean saveConnection(@RequestBody String mensajes) {
        try {
            // Guardar el contenido en el archivo
            Files.write(Paths.get(CONNECTIONS_FILE), mensajes.getBytes(), StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING);

            // System.out.println("Mensaje guardado exitosamente en " + MESSAGES_FILE);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false; // Retorna false si ocurre algún error
        }
    }

    @GetMapping("/loadUsers")
    public String loadUsers() {
        try {
            Path path = Paths.get(USERS_FILE);

            // Leer y devolver el contenido completo del archivo JSON como String
            String jsonContent = Files.readString(path);
            // System.out.println("Contenido del archivo JSON: " + jsonContent); //
            // Verificar contenido

            return jsonContent;
        } catch (IOException e) {
            e.printStackTrace();
            return "{}"; // Devolver JSON vacío en caso de error
        }
    }

    @GetMapping("/health")
    public String healthCheck() {
        return "ok";
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
