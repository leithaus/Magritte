/* ************************************************************* *
 * Generated by WSDL2CALWSClient                                 *
 * From: /Users/lgm/work/src/projex/bobj/magritte/pipe-cutter/pipes_to_cal/src/test/data/schema/CalculatorWSService.wsdl and /Users/lgm/work/src/projex/bobj/magritte/pipe-cutter/pipes_to_cal/src/test/data/schema/CalculatorWSService_schema1.xsd *
 * on Tue Oct 28 10:11:33 PDT 2008 *
 * ************************************************************* */

package com.test.service;

import javax.xml.ws.WebServiceRef;
import javax.xml.ws.WebServiceContext;
import javax.annotation.Resource;
import java.net.*;
import java.io.*;
import org.me.calculator.client.CalculatorWS;
import org.me.calculator.client.CalculatorWSService;

public class CalculatorWSService_4CALTrampoline
{
CalculatorWSService service;
public int mult(int m, int n)
{
CalculatorWS port = service.getCalculatorWSPort();
int returnRslt = port.mult( m, n );
return( returnRslt );
}

public int add(int i, int j)
{
CalculatorWS port = service.getCalculatorWSPort();
int returnRslt = port.add( i, j );
return( returnRslt );
}

}
