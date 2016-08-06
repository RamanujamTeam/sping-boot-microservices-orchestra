package in.ramanujam.docker;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DockerClientFactory {

    @Value("${docker.host}")
    private String dockerHost;

    @Value("${docker.port}")
    private int dockerPort;

    @Value("${docker.cert.path}")
    private String dockerCertPath;

    @Value("${docker.tls.verify}")
    private boolean dockerTlsVerify;

    @Value("${docker.apiVersion}")
    private String dockerApiVersion;

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
                .withDockerCertPath(dockerCertPath)
                .withDockerHost("tcp://" + dockerHost + ":" + dockerPort)
                .withDockerTlsVerify(dockerTlsVerify)
                .withApiVersion(dockerApiVersion)
                .build();

        return DockerClientBuilder.getInstance(config).build();
    }

    private DockerClient getLinuxOSClient() {
        return DockerClientBuilder.getInstance("tcp://" + dockerHost + ":" + dockerPort).build();
    }
}
