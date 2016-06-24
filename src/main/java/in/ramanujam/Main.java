package in.ramanujam;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.Ports;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.command.WaitContainerResultCallback;
import org.springframework.boot.SpringApplication;

public class Main {

    public static void main(String[] args) {

        DockerClient dockerClient = DockerClientBuilder.getInstance("tcp://localhost:2375").build();

        ExposedPort tcp6379 = ExposedPort.tcp(6379);
        Ports portBindings = new Ports();
        portBindings.bind(tcp6379, new Ports.Binding("localhost", "7777"));

        CreateContainerResponse container = dockerClient.createContainerCmd("redis") // TODO: check that we are not duplicating containers
                .withExposedPorts(tcp6379) // TODO: be sure that the port 7777 will be available for the next run of main method
                .withPortBindings(portBindings)
                .exec();
        dockerClient.startContainerCmd(container.getId()).exec();

        SpringApplication.run(RedisFillerMain.class, args);

        dockerClient.waitContainerCmd(container.getId()).exec( new WaitContainerResultCallback()).awaitStatusCode(); // TODO: use waitContainerCmd!
//        dockerClient.stopContainerCmd(container.getId()).exec();
    }
}
