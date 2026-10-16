
package hello;

import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class HelloWorld {
    public static void main(String[] args) {
        int port = 8080;

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Servidor iniciado en http://localhost:" + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();

                System.out.println("Conexion aceptada IP del cliente "
                        + clientSocket.getInetAddress());

                manejarPeticion(clientSocket);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void manejarPeticion(Socket clientSocket) {
        try (Socket socket = clientSocket;
             OutputStream output = socket.getOutputStream()) {

            String respuesta = "Hola Cliente!";
            byte[] contenido = respuesta.getBytes(StandardCharsets.UTF_8);

            StringBuilder response = new StringBuilder();

            addHeader(response, contenido.length);
            response.append(respuesta);

            new Thread(() -> {
                try {
                    System.out.println("Ejecucion en segundo plano iniciada");

                    Thread.sleep(5000);

                    System.out.println("Ejecucion en segundo plano terminada");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    e.printStackTrace();
                }
            }).start();

            output.write(response.toString().getBytes(StandardCharsets.UTF_8));
            output.flush();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void addHeader(StringBuilder strBuilder,
                                  int contenidoLongitud) {
        strBuilder.append("HTTP/1.1 200 OK\r\n")
                .append("Content-Type: text/plain; charset=UTF-8\r\n")
                .append("Content-Length: ").append(contenidoLongitud)
                .append("\r\n")
                .append("Connection: close\r\n")
                .append("\r\n");
    }
}
