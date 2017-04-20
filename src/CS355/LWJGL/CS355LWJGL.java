package CS355.LWJGL;

/**
 *
 * @author Brennan Smith
 */
public class CS355LWJGL 
{
    
    public static void main(String[] args) 
  {
    CS355.LWJGL.LWJGLSandbox main = null;
    try 
    {
      main = new CS355.LWJGL.LWJGLSandbox();
      main.create(new CS355.LWJGL.StudentLWJGLController());
      main.run();
    }
    catch(Exception ex) 
    {
      ex.printStackTrace();
    }
    finally {
      if(main != null) 
      {
        main.destroy();
      }
    }
  }
    
}
