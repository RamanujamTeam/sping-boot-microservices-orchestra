package in.ramanujam.cloudconfig;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class ConfigServer {  // Note that configuration file with repository URI should be named application.yml - it won't work otherwise

    public static void main(String[] args) {
        SpringApplication.run(ConfigServer.class, args);
    }

}
