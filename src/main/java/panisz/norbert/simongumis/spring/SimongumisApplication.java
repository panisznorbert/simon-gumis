package panisz.norbert.simongumis.spring;

import com.vaadin.flow.spring.annotation.EnableVaadin;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import panisz.norbert.simongumis.services.RendelesService;

@EnableVaadin("panisz.norbert.simongumis")
@SpringBootApplication(scanBasePackages = "panisz.norbert.simongumis")
@EntityScan(basePackages = "panisz.norbert.simongumis.entities")
@EnableJpaRepositories(basePackages = "panisz.norbert.simongumis")
@EnableTransactionManagement
@Data
public class SimongumisApplication {

    public static RendelesService getRendelesService() {
        return rendelesService;
    }

    @Autowired
    private static RendelesService rendelesService;

    public static void main(String[] args) {
        SpringApplication.run(SimongumisApplication.class, args);
    }

}


