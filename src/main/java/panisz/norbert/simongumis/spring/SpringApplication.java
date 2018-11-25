package panisz.norbert.simongumis.spring;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@ComponentScan(basePackageClasses = AdminController.class)
@SpringBootApplication
@EntityScan(basePackages = "panisz.norbert.simongumis.entities")
@EnableJpaRepositories(basePackages = "panisz.norbert.simongumis.repositories")
public class SpringApplication {
    public static void main(String[] args) {
        org.springframework.boot.SpringApplication.run(SpringApplication.class, args);
    }
}
