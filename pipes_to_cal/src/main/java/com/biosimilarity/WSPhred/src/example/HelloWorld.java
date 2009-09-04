/**
 * Created by IntelliJ IDEA.
 * User: lgm
 * Date: Sep 9, 2008
 * Time: 9:59:46 AM
 * To change this template use File | Settings | File Templates.
 */
package example;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.xml.ws.Endpoint;

@WebService()
public class HelloWorld {
  @WebMethod
  public String sayHelloWorldFrom(String from) {
    String result = "Hello, world, from " + from;
    System.out.println(result);
    return result;
  }
  public static void main(String[] argv) {
    Object implementor = new HelloWorld ();
    String address = "http://localhost:9000/HelloWorld";
    Endpoint.publish(address, implementor);
  }
}