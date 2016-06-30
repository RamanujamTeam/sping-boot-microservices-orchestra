package in.ramanujam.properties;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesManager
{
  Properties appProps;

  public void load()
  {
    try ( InputStream in = getClass().getClassLoader().getResourceAsStream( "config.properties" ) )
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

  public static void main( String[] args )
  {
    PropertiesManager propertiesManager = new PropertiesManager();
    propertiesManager.load();
    System.out.println( propertiesManager.appProps.getProperty( "lol" ) );
  }
}
