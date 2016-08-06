package in.ramanujam.common.properties;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public abstract class AbstractProperties { // TODO remove
    private Properties appProps;

    public void load() {
        try (InputStream in = getClass().getClassLoader().getResourceAsStream(getFile())) {
            appProps = new Properties();
            appProps.load(in);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Properties getAppProps() {
        return appProps;
    }

    protected abstract String getFile();
}
