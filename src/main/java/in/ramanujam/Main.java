package in.ramanujam;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.Ports;
import com.github.dockerjava.api.model.WaitResponse;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.command.WaitContainerResultCallback;

import java.io.Closeable;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {

        DockerClient dockerClient = DockerClientBuilder.getInstance("tcp://localhost:2375").build();

        ExposedPort tcp6379 = ExposedPort.tcp(6379);
        Ports portBindings = new Ports();
        portBindings.bind(tcp6379, new Ports.Binding("localhost", "7777"));

        CreateContainerResponse container = dockerClient.createContainerCmd("redis") // TODO: check that we are not duplicating containers
                .withExposedPorts(tcp6379)
                .withPortBindings(portBindings)
                .exec();
        System.out.println("!!! START:");
        dockerClient.startContainerCmd(container.getId()).exec();
        dockerClient.waitContainerCmd(container.getId()).exec( new WaitContainerResultCallback()); // TODO: use waitContainerCmd!
        System.out.println("The End!");
//        dockerClient.stopContainerCmd(container.getId()).exec();
    }
}
