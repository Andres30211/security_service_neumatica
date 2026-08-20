package neumatica.security.segurity_service_neumatica.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.Base64;

public class GenerateKeys {

	public static void main(String[] args) throws Exception {
        run();
    }

    public static void run() throws Exception {

        KeyPairGenerator generator =
                KeyPairGenerator.getInstance("RSA");

        generator.initialize(2048);

        KeyPair keyPair = generator.generateKeyPair();

        String privateKey = """
                -----BEGIN PRIVATE KEY-----
                %s
                -----END PRIVATE KEY-----
                """.formatted(
                Base64.getMimeEncoder(64, "\n".getBytes())
                        .encodeToString(
                                keyPair.getPrivate().getEncoded()
                        )
        );

        String publicKey = """
                -----BEGIN PUBLIC KEY-----
                %s
                -----END PUBLIC KEY-----
                """.formatted(
                Base64.getMimeEncoder(64, "\n".getBytes())
                        .encodeToString(
                                keyPair.getPublic().getEncoded()
                        )
        );

        Path directory =
                Path.of("src/main/resources/keys");

        Files.createDirectories(directory);

        Files.writeString(
                directory.resolve("private.pem"),
                privateKey
        );

        Files.writeString(
                directory.resolve("public.pem"),
                publicKey
        );

        System.out.println("================================");
        System.out.println("CLAVES GENERADAS CORRECTAMENTE");
        System.out.println("================================");
        System.out.println("Private: " +
                directory.resolve("private.pem").toAbsolutePath());
        System.out.println("Public: " +
                directory.resolve("public.pem").toAbsolutePath());
    }
}
