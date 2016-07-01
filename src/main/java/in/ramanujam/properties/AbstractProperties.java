package in.ramanujam.properties;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public abstract class AbstractProperties
{
  private Properties appProps;

  public void load()
  {
    try ( InputStream in = getClass().getClassLoader().getResourceAsStream( getFile() ) )
    {
      Properties defaultProps = new Properties();
      defaultProps.load( in );
      appProps = new Properties( defaultProps );
    }
    catch ( IOException e )
    {
      throw new RuntimeException( e );
    }
  }

  public Properties getAppProps()
  {
    return appProps;
  }

  protected abstract String getFile();
}
