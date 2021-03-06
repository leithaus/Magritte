/* ************************************************************* *
 * Generated by WSDL2CALWSClient                                 *
 * From: /Users/lgm/work/src/projex/bobj/magritte/pipe-cutter/pipes_to_cal/src/test/data/schema/CalculatorWSService.wsdl and /Users/lgm/work/src/projex/bobj/magritte/pipe-cutter/pipes_to_cal/src/test/data/schema/CalculatorWSService_schema1.xsd *
 * on Tue Oct 28 10:11:33 PDT 2008 *
 * ************************************************************* */

package org.trampoline.CAL;
import javax.xml.ws.WebServiceRef;
import javax.xml.ws.WebServiceContext;
import javax.annotation.Resource;

public class CalculatorWSService_4CALTrampoline
{
@WebServiceRef(wsdlLocation = "http://localhost:8080/CalculatorApp/CalculatorWSService?wsdl")
CalculatorWSService service;

@Resource
protected WebServiceContext context;
public int mult(int m, int n) throws ArithmeticException_Exception
{
    if (service == null) service = new CalculatorWSService();
CalculatorWS port = service.getCalculatorWSPort();
int returnRslt = port.mult( m, n );
return( returnRslt );
}

public int add(int i, int j)
{
    if (service == null) service = new CalculatorWSService();
CalculatorWS port = service.getCalculatorWSPort();
int returnRslt = port.add( i, j );
return( returnRslt );
}

}
