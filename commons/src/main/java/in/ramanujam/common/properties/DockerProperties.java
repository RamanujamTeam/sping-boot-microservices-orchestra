package in.ramanujam.common.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DockerProperties {
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

    public String getDockerCertPath() {
        return dockerCertPath;
    }

    public String getDockerHost() {
        return dockerHost;
    }

    public int getDockerPort() {
        return dockerPort;
    }

    public boolean getDockerTlsVerify() {
        return dockerTlsVerify;
    }

    public String getDockerApiVersion() {
        return dockerApiVersion;
    }
}
