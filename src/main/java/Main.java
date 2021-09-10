import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;

//для проверки работы методов получения Query Parameters - start Main
// and go to link
//http://localhost:9999/classic.html?name=alena&lastname=ocheretnaia&age=36&value=1&country=usa&value=1

public class Main {

    public static void main(String[] args) {

        var server = new Server(64);

        server.addHandler("GET", "/classic.html", ((request, out) -> {
            final var filePath = Path.of(".", "public", request.getPath());
            final var mimeType = Files.probeContentType(filePath);

            final var template = Files.readString(filePath);
            final var content = template.replace(
                    "{time}",
                    LocalDateTime.now().toString()
            ).getBytes();
            out.write((
                    "HTTP/1.1 200 OK\r\n" +
                            "Content-Type: " + mimeType + "\r\n" +
                            "Content-Length: " + content.length + "\r\n" +
                            "Connection: close\r\n" +
                            "\r\n"
            ).getBytes());
            out.write(content);
            out.flush();
        }));

        server.startServer(9999);

    }
}