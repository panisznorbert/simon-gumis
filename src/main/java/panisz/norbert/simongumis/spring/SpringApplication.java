package panisz.norbert.simongumis.spring;

import com.vaadin.flow.spring.annotation.EnableVaadin;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableVaadin("panisz.norbert.simongumis")
@SpringBootApplication
@EntityScan(basePackages = "panisz.norbert.simongumis.entities")
@EnableJpaRepositories(basePackages = "panisz.norbert.simongumis.repositories")
public class SpringApplication {
    public static void main(String[] args) {
        org.springframework.boot.SpringApplication.run(SpringApplication.class, args);
    }
}
