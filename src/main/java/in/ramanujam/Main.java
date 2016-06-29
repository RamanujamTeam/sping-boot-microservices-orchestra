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
        Ports redisPortBinding = new Ports();
        redisPortBinding.bind(tcp6379, new Ports.Binding("localhost", "7777"));

        ExposedPort tcp9200 = ExposedPort.tcp(9200);
        Ports ESPortBinding = new Ports();
        ESPortBinding.bind(tcp9200, new Ports.Binding("localhost", "5555"));

        CreateContainerResponse redisContainer = dockerClient.createContainerCmd("redis") // TODO: check that we are not duplicating containers
                .withExposedPorts(tcp6379) // TODO: be sure that the port 7777 will be available for the next run of main method
                .withPortBindings(redisPortBinding)
                .exec();

        CreateContainerResponse ESContainer = dockerClient.createContainerCmd("elasticsearch")
                .withExposedPorts(tcp9200)
                .withPortBindings(ESPortBinding)
                .exec();

        dockerClient.startContainerCmd(redisContainer.getId()).exec();
        dockerClient.startContainerCmd(ESContainer.getId()).exec();

        SpringApplication.run(RedisFillerMain.class, args);

        dockerClient.waitContainerCmd(redisContainer.getId()).exec( new WaitContainerResultCallback()).awaitStatusCode(); // TODO: use waitContainerCmd!
//        dockerClient.stopContainerCmd(container.getId()).exec();
    }
}
