/*package serramineira.sistemas.leite.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Aplica a configuração a todos os endpoints da API
                .allowedOrigins("http://localhost:3000") // Permite requisições SOMENTE desta origem
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD", "TRACE", "CONNECT") // Métodos HTTP permitidos
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}

 */