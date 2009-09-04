
package org.trampoline.CAL;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for mult complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="mult">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="m" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="n" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "mult", propOrder = {
    "m",
    "n"
})
public class Mult {

    protected int m;
    protected int n;

    /**
     * Gets the value of the m property.
     * 
     */
    public int getM() {
        return m;
    }

    /**
     * Sets the value of the m property.
     * 
     */
    public void setM(int value) {
        this.m = value;
    }

    /**
     * Gets the value of the n property.
     * 
     */
    public int getN() {
        return n;
    }

    /**
     * Sets the value of the n property.
     * 
     */
    public void setN(int value) {
        this.n = value;
    }

}
