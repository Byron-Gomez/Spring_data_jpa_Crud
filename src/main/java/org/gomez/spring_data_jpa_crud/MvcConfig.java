package org.gomez.spring_data_jpa_crud;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

// Configuración de Spring MVC para manejar recursos estáticos
@Configuration
public class MvcConfig implements WebMvcConfigurer {

    // Logger para realizar registros
    private final Logger log = LoggerFactory.getLogger(getClass());

    // Método para agregar manejadores de recursos (recursos estáticos)
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Llamada al método en la superclase para agregar manejadores de recursos
        WebMvcConfigurer.super.addResourceHandlers(registry);

        // Obtención de la ruta del directorio de recursos (uploads) y configuración del manejador
        String resourcePath = Paths.get("uploads").toAbsolutePath().toUri().toString();
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(resourcePath);
    }
}
