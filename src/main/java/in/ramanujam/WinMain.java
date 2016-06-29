package in.ramanujam;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.Info;
import com.github.dockerjava.api.model.Ports;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.command.WaitContainerResultCallback;

public class WinMain {

    public static void main(String[] args) {

        DockerClientConfig config = DockerClientConfig.createDefaultConfigBuilder()
            .withDockerCertPath("C:/Users/roma/.docker/machine/certs")
            .withDockerHost("tcp://192.168.99.100:2376")
            .withDockerTlsVerify(true)
            .withApiVersion("1.21")
            .build();

        DockerClient dockerClient = DockerClientBuilder.getInstance(config).build();

        Info info = dockerClient.infoCmd().exec();


        ExposedPort tcp6379 = ExposedPort.tcp(6379);
        Ports portBindings = new Ports();
        portBindings.bind(tcp6379, new Ports.Binding("localhost", "7777"));

        CreateContainerResponse container = dockerClient.createContainerCmd("redis") // TODO: check that we are not duplicating containers
            .withExposedPorts(tcp6379) // TODO: be sure that the port 7777 will be available for the next run of main method
            .withPortBindings(portBindings)
            .exec();
        dockerClient.startContainerCmd(container.getId()).exec();
        dockerClient.waitContainerCmd(container.getId()).exec( new WaitContainerResultCallback()).awaitStatusCode();; // TODO: use waitContainerCmd!
        dockerClient.removeContainerCmd(container.getId()).exec();
//        dockerClient.stopContainerCmd(container.getId()).exec();
    }
}
