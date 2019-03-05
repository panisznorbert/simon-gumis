package panisz.norbert.simongumis.spring;

import com.vaadin.flow.spring.annotation.EnableVaadin;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import panisz.norbert.simongumis.views.MainView;

@EnableVaadin("panisz.norbert.simongumis")
@SpringBootApplication(scanBasePackages = "panisz.norbert.simongumis")
@EntityScan(basePackages = "panisz.norbert.simongumis.entities")
@EnableJpaRepositories(basePackages = "panisz.norbert.simongumis")

public class SpringApplication extends SpringBootServletInitializer {
    private static Integer rendelesAzon = null;
    public static void main(String[] args) {
        org.springframework.boot.SpringApplication.run(SpringApplication.class, args);
    }

    public static Integer getRendelesAzon() {
        return rendelesAzon;
    }

    public static void setRendelesAzon(Integer rendelesAzon) {
        SpringApplication.rendelesAzon = rendelesAzon;
    }
}


