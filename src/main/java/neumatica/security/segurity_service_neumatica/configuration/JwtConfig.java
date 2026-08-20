package neumatica.security.segurity_service_neumatica.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.security.converter.RsaKeyConverters;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;

import java.io.InputStream;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@Configuration
public class JwtConfig {

	@Value("${app.jwt.private-key}")
    private Resource privateKeyResource;

    @Value("${app.jwt.public-key}")
    private Resource publicKeyResource;

    @Bean
    public RSAPrivateKey privateKey() throws Exception {

        try (InputStream inputStream = this.privateKeyResource.getInputStream()) {

            return RsaKeyConverters
                    .pkcs8()
                    .convert(inputStream);
        }
    }

    @Bean
    public RSAPublicKey publicKey() throws Exception {

        try (InputStream inputStream = this.publicKeyResource.getInputStream()) {

            return RsaKeyConverters
                    .x509()
                    .convert(inputStream);
        }
    }

    @Bean
    public JwtEncoder jwtEncoder(
            RSAPublicKey publicKey,
            RSAPrivateKey privateKey) {

        JWK jwk = new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .build();

        return new NimbusJwtEncoder(
                new ImmutableJWKSet<>(new JWKSet(jwk))
        );
    }
    
    @Bean
    public JwtDecoder jwtDecoder(RSAPublicKey publicKey) {

        return NimbusJwtDecoder
                .withPublicKey(publicKey)
                .build();
    }
}
