// -*- mode: Scala;-*- 
// Filename:    WSDL2CALWSClient.scala 
// Authors:     lgm                                                    
// Creation:    Fri Sep 26 16:17:07 2008 
// Copyright:   Not supplied 
// Description: WSDL schema is about as unfriendly as it gets for
// typed access
// ------------------------------------------------------------------------

package com.biosimilarity.magritte

import java.io._
import java.net.URL
import java.lang.reflect.Field

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBIntrospector;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.sun.xml.xsom.XSSchema;
import com.sun.xml.xsom.XSComplexType;
import com.sun.xml.xsom.XSParticle;
import com.sun.xml.xsom.XSElementDecl;
import com.sun.xml.xsom.XSModelGroup;
import com.sun.xml.xsom.XSTerm;
import com.sun.xml.xsom.XSContentType;
import com.sun.xml.xsom.parser.XSOMParser;
import com.sun.xml.xsom.parser.SchemaDocument;
import com.sun.xml.xsom.impl.parser.DelayedRef;

import org.xmlsoap.schemas.wsdl._

trait WSDL2CALWSClient {
  val WSDLSchemaPackageName : String = "org.xmlsoap.schemas.wsdl"
  val wsdlPortTypeClassName : String = "TPortType"
  val javaTypeContainer : String = "class"
  val javaTypeAccess : String = "public"
  val javaMethodAccess : String = "public"
  val javaReturn : String = "return"
  val javaPackageKeyWord : String = "package"
  val javaImportKeyWord : String = "import"
  val javaThrowKeyWord : String = "throws"
  val jaxbExceptionSuffix : String = "_Exception"

  val javaImports : List[String] =
    // List("java.io.*"
// 	 ,"java.net.*"
// 	 ,"javax.annotation.Resource"
// 	 ,"javax.xml.ws.WebServiceContext"
//          ,"javax.xml.ws.WebServiceRef")
    List()

  val javaServiceFieldName : String = "service"  
  val portVar : String = "port"
  val generatorSuffix = "_4CALTrampoline"

  def portAccessMethod( wsdlFileName : String ) : String =
    "get" + wsdlPortTypeName( wsdlFileName ) + "Port"

  def portDeclAndAssignment( wsdlFileName : String ) : String =
    {
      (wsdlPortTypeName( wsdlFileName )
       + " "
       + portVar
       + " = "
       + javaServiceFieldName
       + "."
       + portAccessMethod( wsdlFileName ) + "(" + ")" + ";")
    }

  def javaPackageDecl( generatedPackageName : String ) : String = 
    javaPackageKeyWord + " " + generatedPackageName + ";"

  def javaImportDecls(
    clientPackageName : String,
    generatedPackageName : String,
    wsdlFileName : String
  ) : String =
    (
      ("" /: javaImports)({
	( acc, importStr ) =>
	  (javaImportKeyWord + " " + importStr + ";" + "\n") + acc
      })
      + (if (clientPackageName != generatedPackageName)
	  (javaImportKeyWord
	   + " "
	   + clientPackageName
	   + "."
	   + wsdlPortTypeName( wsdlFileName )
	   + ";"
	   + "\n"
	   + javaImportKeyWord
	   + " "
	   + clientPackageName
	   + "."
	   + wsdlServiceName( wsdlFileName )
	   + ";"
	   + "\n")
	 else "")
   )

  var _commentHeader : String =
    ("/* ************************************************************* *"
    + "\n"
    + " * Generated by WSDL2CALWSClient                                 *"
    + "\n"
    + " * From: %%FROMWSDLFILE%% and %%FROMSCHEMAFILE%% *"
    + "\n"
    + " * on %%DATE%% *"
    + "\n"
    + " * ************************************************************* */")
  
  def commentHeader(
    wsdlFileName : String,
    schemaFileName : String 
    )
  : String =
    _commentHeader.replaceAll(
      "%%FROMWSDLFILE%%",
      wsdlFileName
    ).replaceAll(
      "%%FROMSCHEMAFILE%%",
      schemaFileName ).replaceAll(
	"%%DATE%%",
	new java.util.Date().toString
	)

  var _schemaParser : XSOMParser = null

  def schemaParser = {
    if (_schemaParser == null) _schemaParser = new XSOMParser();
    _schemaParser
  }

  val _schema : scala.collection.mutable.Map[String, SchemaDocument]
  = new scala.collection.mutable.HashMap()

  //var _wsdlDefinitions : TDefinitions = null
  val _wsdlDefinitions : scala.collection.mutable.Map[String, TDefinitions] 
  = new scala.collection.mutable.HashMap()
  
  def parseWSDLFile( fileName : String ) : TDefinitions = {
    _wsdlDefinitions.get( fileName ) match {
      case Some( tdefns ) => tdefns
      case None => {
	val jctxt : JAXBContext =
	  JAXBContext.newInstance( WSDLSchemaPackageName );
	val ju : Unmarshaller = jctxt.createUnmarshaller();
	val fs : FileInputStream = new FileInputStream( fileName );
	val uObj : Object = ju.unmarshal( fs );
	val ans : TDefinitions =
	  uObj.asInstanceOf[JAXBElement[TDefinitions]].getValue() ;
	_wsdlDefinitions.update( fileName, ans );
	ans
      }
    }
  }

  def parseSchemaFile( fileName : String ) : SchemaDocument = {    
    _schema.get( fileName ) match {
      case Some( schemaDoc ) => schemaDoc
      case None => {
	schemaParser.parse( new URL( "file://" + fileName ) );
	val ans : SchemaDocument =
	  (scala.collection.jcl.Conversions.convertSet(
	    schemaParser.getDocuments()
	  ) filter { schemaDocument =>
	    schemaDocument.getSystemId.contains( fileName )
	   }).elements.next ;
	_schema.update( fileName, ans );
	ans
      }
    }
  }

  def isPortType( defn : org.xmlsoap.schemas.wsdl.TDocumented ) : Boolean = {
    val nameSplit : Array[java.lang.String] =
      defn.getClass().getName().split("\\.");
      nameSplit(nameSplit.length - 1).equals( wsdlPortTypeClassName )
  }

  def operations( fileName : String )
  : List[org.xmlsoap.schemas.wsdl.TOperation] = {
   scala.collection.jcl.Conversions.convertList((((for( defn <-
       scala.collection.jcl.Conversions.convertList(
	 parseWSDLFile( fileName ).getAnyTopLevelOptionalElement()
       ) if isPortType(defn) ) yield defn)(0)).asInstanceOf[org.xmlsoap.schemas.wsdl.TPortType]).getOperation()).toList
  }

  def wsdlPortTypeName( fileName : String ) : String =
    (scala.collection.jcl.Conversions.convertList(
      parseWSDLFile( fileName ).getAnyTopLevelOptionalElement()
      ) filter { 
	defn => isPortType( defn )
      }).elements.next.asInstanceOf[TPortType].getName

  def wsdlServiceName( fileName : String ) : String =
    parseWSDLFile( fileName ).getName

  def operationTypeResolution( xsType : XSComplexType ) : Array[XSElementDecl]
  =
    {
      xsType.getContentType.asParticle.getTerm.asModelGroup.getChildren.map(
	{
	  particle =>
	    val elemDecl : XSElementDecl = particle.getTerm.asElementDecl;
	  // This is a hack. The path-based access is not
	  // completing the resolution of delayed
	  // references. Calling run on the DelayedRef resolves
	  // it. i'm guessing that if i were to use the visitor
	  // apparatus there's probably some hook to resolve the
	  // references there. However, i just don't understand
	  // how to use that pattern in this context.
	  val typeField : Field =
	    elemDecl.getClass.getDeclaredField( "type" );
	  typeField.setAccessible( true );
	  val ref : DelayedRef = typeField.get( elemDecl ).asInstanceOf[DelayedRef];
	  ref.run;
	  elemDecl
	  }
      );
    }

  def operationJavaSignature(
    xsCallType : XSComplexType,
    xsResponseType : XSComplexType,
    opFaultDecls : Option[(TFault, XSComplexType)],
    oprn : TOperation
  ) : String
  = {
      val callTypeContent : Array[XSElementDecl] =
	operationTypeResolution( xsCallType );
      val responseTypeContent : Array[XSElementDecl] =
	operationTypeResolution( xsResponseType );
      val lastElemDecl : XSElementDecl =
	callTypeContent( callTypeContent.size - 1 );
      val opName : String = oprn.getName();
      val paramsDeclsString : String =
	("" /: callTypeContent.take( callTypeContent.size - 1 ))({
	  ( acc, elemDecl ) =>
	    elemDecl.getType.getName + " " + elemDecl.getName + ", " + acc
	}) + lastElemDecl.getType.getName + " " + lastElemDecl.getName;

      (javaMethodAccess
       + " "
       + responseTypeContent(0).getType.getName
       + " "
       + opName
       + "("
       + paramsDeclsString
       + ")"
       + (opFaultDecls match {
	   case Some( ( opFault, opFaultType ) ) =>
	     (" "
	      + javaThrowKeyWord
	      + " "
	      + opFault.getName
	      + jaxbExceptionSuffix )
	   case None => ""
	 })
       )
  }

  def generateOprnMethodBody( wsdlFileName : String)(
    oprn : org.xmlsoap.schemas.wsdl.TOperation,
    params : Array[XSElementDecl],
    result : Array[XSElementDecl]
    ) 
  : String
  =
    {
      val javaValueVar : String = result(0).getName + "Rslt";
      ("{"
       + "\n"
       + portDeclAndAssignment( wsdlFileName )
       + "\n"
       + (result(0).getType.getName + " " + javaValueVar + " = ")
       + (portVar + "." + oprn.getName)
       + "(" + " "
       + ("" /: params.take( params.size - 1 ))({
	   ( acc, elemDecl ) => elemDecl.getName + ", " + acc
	 })
       + params( params.size - 1 ).getName
       + " " + ")"
       + ";"
       + "\n"
       + (javaReturn + "( " + javaValueVar + " )")
       + ";"
       + "\n"
       + "}"
       + "\n")
    }

  def operationFault( oprn : TOperation ) : Option[TFault] 
  =
    {
      ((scala.collection.jcl.Conversions.convertList(
	oprn.getRest
	).map(
	  {
	    jxe =>
	      jxe.asInstanceOf[JAXBElement[TExtensibleAttributesDocumented]].getValue
	  }
	)
     ) filter {
       oprnIO => oprnIO.getClass.getName.contains( "TFault" )
     }).toList match {
	  case Nil => None
	  case x :: xs => Some( x.asInstanceOf[TFault] )
	}
    }

  def generateJavaWSClientString(
    clientPackageName : String,
    generatedPackageName : String
  )(
    wsdlFileName : String,
    schemaFileName : String,
    workingDirectoryName : String
    // outFileName : String
  ) : String
  = {
    // Sadly, none of this file name manipulation code is portable.
    // Shame on the person who wrote this in this day and age when we 
    // have nice wrapper classes to handle this sort of thing.

      val qualifiedSchemaFileName : String =
	workingDirectoryName + "/" + schemaFileName;
      val qualifiedWSDLFileName : String =
	workingDirectoryName + "/" + wsdlFileName;

      val schema : XSSchema =
	parseSchemaFile( qualifiedSchemaFileName ).getSchema();
      val cplxTypes : scala.collection.jcl.MapWrapper[String, XSComplexType]
	= scala.collection.jcl.Conversions.convertMap(
	    schema.getComplexTypes
	  );

      val wsdlInterfaceName =
	wsdlServiceName( qualifiedWSDLFileName ) + generatorSuffix;
	  
      val interfaceMethodDecls =
	("" /: operations( qualifiedWSDLFileName ))(
	{
	  (acc,e) => {
	    val opType : XSComplexType = 
	      cplxTypes.get( e.getName ) match {
		case Some( ct ) => ct
		case _ => throw new Exception( "call type not found" )
	      }
	    val opResponseType : XSComplexType =
	      cplxTypes.get( e.getName + "Response" ) match {
		case Some( ct ) => ct
		case _ => throw new Exception( "response type not found" )
	      }
	    val opFaultDecls : Option[(TFault, XSComplexType)] = 
	      operationFault( e ) match {
		case Some( opFault ) =>
		  cplxTypes.get( opFault.getName ) match {
		    case Some( ct ) => Some( ( opFault, ct ) )
		    case _ => None
		  }
		case None => None
	      }
	    (operationJavaSignature(
	      opType,
	      opResponseType,
	      opFaultDecls,
	      e
	      )
	     + "\n"
	     + generateOprnMethodBody( qualifiedWSDLFileName )(
		 e,
		 operationTypeResolution( opType ),
		 operationTypeResolution( opResponseType )
	       )
	     + "\n"
	     + acc)
	  }
	}
      );
    
    val clientCodeStr : String =
      (commentHeader( qualifiedWSDLFileName, qualifiedSchemaFileName )
       + "\n" + "\n"
       + javaPackageDecl( generatedPackageName )
       + "\n" + "\n"
       + javaImportDecls(
	   clientPackageName,
	   generatedPackageName,
	   qualifiedWSDLFileName
	 )
       + "\n"
       + javaTypeAccess 
       + " " 
       + javaTypeContainer
       + " "
       + wsdlInterfaceName
       + "\n"
       + "{"
       + "\n"
       + wsdlServiceName( qualifiedWSDLFileName )
       + " "
       + javaServiceFieldName
       + ";"
       + "\n"
       + interfaceMethodDecls
       + "}" + "\n")
            
      clientCodeStr
  }

  def generateJavaWSClientToFile(
    clientPackageName : String,
    generatedPackageName : String
  )(
    wsdlFileName : String,
    schemaFileName : String,
    workingDirectoryName : String
    // outFileName : String
  )
  {
    val clientCodeStr : String =
      generateJavaWSClientString( 
	clientPackageName,
	generatedPackageName
	)(
	  wsdlFileName,
	  schemaFileName,
	  workingDirectoryName
	  );

    val qualifiedWSDLFileName : String =
	workingDirectoryName + "/" + wsdlFileName;

    val wsdlInterfaceName =
	wsdlServiceName( qualifiedWSDLFileName ) + generatorSuffix;

    val fileWriter : FileWriter =
      new FileWriter(
	workingDirectoryName + "/" + wsdlInterfaceName + ".java"
      );

    fileWriter.write( clientCodeStr );
    fileWriter.flush();
    fileWriter.close();
  }
}

object TheWSDL2CALWSClient extends WSDL2CALWSClient {  
  def getWSDLFileName( args : Array[String] ) : String =
    args( 0 )
  def getSchemaFileName( args : Array[String] ) : String = {
    args.indexOf( "-schema" ) match {
      case -1 => getWSDLFileName( args ) + "_schema.xsd"
      case i => args( i+1 )
    }
  }
  def getWorkingDirectory( args : Array[String] ) : String = {
    args.indexOf( "-dir" ) match {
      case -1 => (new java.io.File( "." )).getCanonicalPath
      case i => args( i+1 )
    }
  }
  def getClientPackageName( args : Array[String] ) : String = {
    args.indexOf( "-clientPackage" ) match {
      case -1 => "org.me.client"
      case i => args( i+1 )
    }
  }
  def getGeneratedPackageName( args : Array[String] ) : String = {
    args.indexOf( "-generatedPackage" ) match {
      case -1 => getClientPackageName( args )
      case i => args( i+1 )
    }
  }
  def main( args : Array[String] ) {
    generateJavaWSClientToFile(
      getClientPackageName( args ),
      getGeneratedPackageName( args )
      )(      
	getWSDLFileName( args ),
	getSchemaFileName( args ),
	getWorkingDirectory( args )
      )
  }
}
