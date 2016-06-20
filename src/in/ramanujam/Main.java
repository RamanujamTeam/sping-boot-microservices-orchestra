package in.ramanujam;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.Info;
import com.github.dockerjava.core.DockerClientBuilder;

public class Main {

    public static void main(String[] args) {

        DockerClient dockerClient = DockerClientBuilder.getInstance("tcp://localhost:2375").build();
        Info info = dockerClient.infoCmd().exec();
        System.out.println("!!! INFO:");
        System.out.print(info);

        CreateContainerResponse container = dockerClient.createContainerCmd("redis")
                .withCmd("touch", "/test")
                .exec();
        System.out.println("!!! START:");
        dockerClient.startContainerCmd(container.getId()).exec();
        System.out.println("!!! STOP:");
//		dockerClient.stopContainerCmd(container.getId()).exec();
        System.out.println("!!! END:");
        dockerClient.removeContainerCmd(container.getId()).exec();
//		dockerClient.waitContainerCmd(container.getId()).exec();

    }


}
