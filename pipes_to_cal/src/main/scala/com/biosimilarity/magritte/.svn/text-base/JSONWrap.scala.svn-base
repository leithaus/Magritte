// -*- mode: Scala;-*- 
// Filename:    JSONWrap.scala 
// Authors:     lgm                                                    
// Creation:    Thu Jun 19 23:38:57 2008 
// Copyright:   Not supplied 
// Description: Sadly, the Java output of BNFC is very
// Java-idiosyncratic. It's not expressed in a functional
// idiom. And, i don't really know the Java idioms it relies on. All
// this visitor pattern stuff... This code is a half-way house to
// building a Scala-based JSON abstract syntax. This experience
// underscores the need to have Scala as a target of BNFC.
// ------------------------------------------------------------------------

package com.biosimilarity.magritte

import com.biosimilarity.magritte.json._
import com.biosimilarity.magritte.json.Absyn._

import scala.collection.immutable.HashMap
import scala.collection.jcl.LinkedList

trait Fleece {
  trait JSONMap {
    def toStr( str : String ) : String
    def blanks( n : Int ) : String =
      n match {
	case 0 => ""
	case 1 => " "
	case _ => " " + blanks( n-1 )
      }
  }

  case class JSONValueMap( jsonValue : JSONValue ) extends JSONMap {
    override def toStr(str : String) : String =
      PrettyPrinter.print( jsonValue )
  }
  case class JSONArrayMap( 
    jarr : JSONArray,
    jmaps : List[JSONMap]
  ) extends JSONMap {
    def sa( indentation : String )( str : String, jmap : JSONMap ) : String =   
      str match {
	case "" => indentation + jmap.toStr(indentation)
	case s => str + ",\n" + indentation + jmap.toStr(indentation)
      }
    override def toStr(indentation : String) : String =
      ("["
       + "\n"
       + ("" /: jmaps)(sa(indentation + "  "))
       + "\n"
       + indentation
       + "]")
  }
  case class JSONObjectMap(
    jobj : JSONObject,
    jmap : Map[String,JSONMap]
  ) extends JSONMap {

    def idString : Option[String] =
      jmap match {
	case null => None
	case _ =>
	  jmap.get("id") match {
	    case Some( jid ) =>
	      jid match {
 		case JSONValueMap( jstrV ) => 
 		  jstrV match {
 		    case jstr : JStr => Some( jstr.string_ )
		    case _ => None
		  }
		case _ => None
	      }
	    case None => None
	  }
      }

    override def toString : String =
      idString match {
	case Some( idStr ) => idStr
	case None => super.toString
      }

    def sa( indentation : String )( str : String, fldJMap : (String, JSONMap) )
    : String =
      fldJMap match {
	case (fldStr, jmap) =>
	  str match {
	    case "" =>
	      (indentation
	       + fldStr
	       + " = "
	       + jmap.toStr(blanks(fldStr.length + 3) + indentation))
	    case _ =>
	      (str
	       + ",\n"
	       + indentation
	       + fldStr
	       + " = "
	       + jmap.toStr(blanks(fldStr.length + 3) + indentation))
	  }
      }
    override def toStr(indentation : String) : String =
      ("{"
       + "\n"
       + ("" /: jmap)(sa(indentation + "  "))
       + "\n"
       + indentation
       + "}")
  }

  def toJSONMap( jsonValue : JSONValue ) : Option[JSONMap] =
    jsonValue match {
      case jfalse : JFal => Some(JSONValueMap( jfalse ))
      case jtrue  : JTru => Some(JSONValueMap( jtrue ))
      case jnull  : JNul => Some(JSONValueMap( jnull ))
      case jnum   : JNum => Some(JSONValueMap( jnum ))
      case jstr   : JStr => Some(JSONValueMap( jstr ))
      case jarr   : JArr => toJSONArrayMap( jarr.jsonarray_ )
      case jobj   : JObj => toJSONObjectMap( jobj.jsonobject_ ) 
    }

  def rePair( acc : Option[Map[String,JSONMap]], jsonPair : JSONPair )
  : Option[Map[String,JSONMap]] =
    acc match {
      case None => None
      case Some( amap ) => 
	jsonPair match {
	  case jpair : JPair =>
	    {
	      toJSONMap( jpair.jsonvalue_ ) match {
		case Some( v ) => 
		  Some(
		    (amap.update( jpair.string_, v)
		     : Map[String,JSONMap])
		  )
		case None => None
	      }	      
	    }
	  case _ => None
	}
    }

  def reValue( acc : Option[List[JSONMap]], jsonValue : JSONValue ) 
  : Option[List[JSONMap]] =
    acc match {
      case Some( l ) =>
	toJSONMap( jsonValue ) match {
	  case Some( jmap ) => Some( jmap :: l ) 
	  case None => None
	}
      case None => None
    }

  def toJSONArrayMap( jsonArray : JSONArray ) : Option[JSONArrayMap] =
    jsonArray match {
      case jarray : JArray =>
	((Some(List()) : Option[List[JSONMap]])
	 /: new LinkedList( jarray.listjsonvalue_ ))(reValue)
	match {
	  case Some( l ) => Some( JSONArrayMap( jsonArray, l ) )
	  case None => None
	}
      case _ => None
    }

  def toJSONObjectMap( jsonObj : JSONObject )
  : Option[JSONObjectMap] =
    {            
      jsonObj match {
	case jobj : JObject => 
	  ((Some(new HashMap[String,JSONMap]()) : Option[Map[String,JSONMap]])
	   /: new LinkedList( jobj.listjsonpair_ ))(rePair)
	    match {
		case Some( jmap ) => 
		  Some(
		    new JSONObjectMap(
		      jsonObj,
		      jmap)
		    )
		case None => None
	      }
	case _ => None
      }
    }

  def wrapJSON( json : Any ) : Option[JSONMap] =
    json match {
      case jobj : JSONObject => toJSONObjectMap( jobj )
      case jval : JSONValue => toJSONMap( jval )
      case _ => None
    }  
}

object TheFleece extends Fleece {      
}
