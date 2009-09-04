
package org.me.calculator.client;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.me.calculator.client package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _AddResponse_QNAME = new QName("http://calculator.me.org/", "addResponse");
    private final static QName _Add_QNAME = new QName("http://calculator.me.org/", "add");
    private final static QName _ArithmeticException_QNAME = new QName("http://calculator.me.org/", "ArithmeticException");
    private final static QName _Mult_QNAME = new QName("http://calculator.me.org/", "mult");
    private final static QName _MultResponse_QNAME = new QName("http://calculator.me.org/", "multResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.me.calculator.client
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Mult }
     * 
     */
    public Mult createMult() {
        return new Mult();
    }

    /**
     * Create an instance of {@link AddResponse }
     * 
     */
    public AddResponse createAddResponse() {
        return new AddResponse();
    }

    /**
     * Create an instance of {@link Add }
     * 
     */
    public Add createAdd() {
        return new Add();
    }

    /**
     * Create an instance of {@link ArithmeticException }
     * 
     */
    public ArithmeticException createArithmeticException() {
        return new ArithmeticException();
    }

    /**
     * Create an instance of {@link MultResponse }
     * 
     */
    public MultResponse createMultResponse() {
        return new MultResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://calculator.me.org/", name = "addResponse")
    public JAXBElement<AddResponse> createAddResponse(AddResponse value) {
        return new JAXBElement<AddResponse>(_AddResponse_QNAME, AddResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Add }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://calculator.me.org/", name = "add")
    public JAXBElement<Add> createAdd(Add value) {
        return new JAXBElement<Add>(_Add_QNAME, Add.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArithmeticException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://calculator.me.org/", name = "ArithmeticException")
    public JAXBElement<ArithmeticException> createArithmeticException(ArithmeticException value) {
        return new JAXBElement<ArithmeticException>(_ArithmeticException_QNAME, ArithmeticException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Mult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://calculator.me.org/", name = "mult")
    public JAXBElement<Mult> createMult(Mult value) {
        return new JAXBElement<Mult>(_Mult_QNAME, Mult.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MultResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://calculator.me.org/", name = "multResponse")
    public JAXBElement<MultResponse> createMultResponse(MultResponse value) {
        return new JAXBElement<MultResponse>(_MultResponse_QNAME, MultResponse.class, null, value);
    }

}
