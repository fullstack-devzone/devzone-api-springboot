package com.sivalabs.devzone;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "devzone")
@Data
public class ApplicationProperties {
    private boolean importDataEnabled = true;
    private String importFilePath;
    private JwtConfig jwt = new JwtConfig();

    @Data
    public static class JwtConfig {
        private String issuer = "DevZone";
        private String header = "Authorization";
        private Long expiresIn = 604_800L;
        private String secret = "";
    }
}
