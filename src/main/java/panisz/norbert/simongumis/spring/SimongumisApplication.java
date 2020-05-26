package panisz.norbert.simongumis.spring;

import com.vaadin.flow.spring.annotation.EnableVaadin;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import java.util.Arrays;
import java.util.Collections;

@EnableVaadin("panisz.norbert.simongumis")
@SpringBootApplication(scanBasePackages = "panisz.norbert.simongumis")
@EntityScan(basePackages = "panisz.norbert.simongumis.entities")
@EnableJpaRepositories(basePackages = "panisz.norbert.simongumis")
@EnableTransactionManagement
public class SimongumisApplication {

    public static void main(String[] args) {
        SpringApplication.run(SimongumisApplication.class, args);
    }

}


