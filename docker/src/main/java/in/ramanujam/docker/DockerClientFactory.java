package in.ramanujam.docker;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;
import in.ramanujam.common.properties.DockerProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DockerClientFactory {

    @Autowired
    private DockerProperties dockerProps;

    public DockerClient getClient() {
        if (isWindows())
            return getWindowsOSClient();
        else
            return getLinuxOSClient();
    }

    public static boolean isWindows() {
        return getOSName().contains("win");
    }

    public static boolean isMac() {
        return getOSName().contains("mac");
    }

    public static boolean isUnix() {
        final String OS = getOSName();
        return (OS.contains("nix") || OS.contains("nux") || OS.contains("aix"));
    }

    private static String getOSName() {
        return System.getProperty("os.name").toLowerCase();
    }

    private DockerClient getWindowsOSClient() {
        DockerClientConfig config = DockerClientConfig.createDefaultConfigBuilder()
                .withDockerCertPath(dockerProps.getDockerCertPath())
                .withDockerHost("tcp://" + dockerProps.getDockerHost() + ":" + dockerProps.getDockerPort())
                .withDockerTlsVerify(dockerProps.getDockerTlsVerify())
                .withApiVersion(dockerProps.getDockerApiVersion())
                .build();

        return DockerClientBuilder.getInstance(config).build();
    }

    private DockerClient getLinuxOSClient() {
        return DockerClientBuilder.getInstance("tcp://" + dockerProps.getDockerHost() + ":" + dockerProps.getDockerPort()).build();
    }
}
