// -*- mode: Scala;-*- 
// Filename:    ConcreteGraphTypes.scala 
// Authors:     lgm                                                    
// Creation:    Fri Jul 11 13:46:08 2008 
// Copyright:   Not supplied 
// Description: 
// ------------------------------------------------------------------------

package com.biosimilarity.magritte

import com.biosimilarity.magritte._
import com.biosimilarity.magritte.json._
import com.biosimilarity.magritte.json.Absyn._
import org.jgrapht._
import org.jgrapht.graph._
import org.jgrapht.ext._

import scala.collection.immutable.HashMap
import java.io.FileWriter

trait ConcreteGraphTypes {
  type JMapType = TheArgonaut.JSONMap
  type JAMapType = TheArgonaut.JSONArrayMap
  type JOMapType = TheArgonaut.JSONObjectMap
  type JVMapType = TheArgonaut.JSONValueMap

  type VertexType = TheArgonaut.JSONMap
  type EdgeType = PipeEdge
  type GraphType = DirectedGraph[JMapType,PipeEdge]
  type VertexIdMapType = Map[String,JMapType]
  type EdgeIdMapType = Map[String,PipeEdge]  

  trait JMapIdentifierProvider {
    def nextSeqTag : String 
    var memoPad : Map[JMapType,String] = new HashMap[JMapType,String]
    def getName( jmap : JMapType ) : String =
      memoPad.get( jmap ) match {
	case Some( name ) => name
	case None => {
	  val name : String = computeName( jmap );
	  memoPad = memoPad.update( jmap, name );
	  name
	}
      }

    def getVertexJSONAttribute( jmap : JMapType, attr : String ) : String =
      jmap match {
	case jobjMap : JOMapType =>
	  jobjMap match {
	    case TheArgonaut.JSONObjectMap( _, jomap ) =>
	      jomap.get( attr ) match {
		case Some( jid ) =>
		  jid match {
 		    case TheArgonaut.JSONValueMap( jstrV ) => 
 		      jstrV match {
 			case jstr : JStr => jstr.string_.replace('-','_')
			case _ => throw new Exception("Bad JSON")
		      }
		    case _ => throw new Exception("Bad JSON")
		  }
		case None => throw new Exception(
		  "Bad JSON or invalid attr: " + attr
		)
	      }
	  }
	case jarrMap : JAMapType =>
	  throw new Exception("Bad graph")
	case jvalMap : JVMapType =>
	  throw new Exception("Bad graph")
      }

    def grabName( jmap : JMapType ) : String =
      getVertexJSONAttribute( jmap, "type" )

    def computeName( jmap : JMapType ) : String =
      (grabName( jmap ) + "_" + System.currentTimeMillis) //+ nextSeqTag
  }

  case class JMapVertexIdProvider( label : Int )       
       extends VertexNameProvider[JMapType] 
       with JMapIdentifierProvider {
	 var count : Int = label
	 override def nextSeqTag : String = {
	   count = count + 1;
	   ((count * 3) + 1).toString
	 }
	 override def getVertexName( jmap : JMapType ) : String =
	   getName( jmap )
       }

  case class JMapVertexDotIdProvider( labelseed : Int )
      extends JMapVertexIdProvider( labelseed ) {
	override def grabName( jmap : JMapType ) : String =
	  ( getVertexJSONAttribute( jmap, "type" )
	   + "_"
	   + getVertexJSONAttribute( jmap, "id" ) )
	override def getVertexName( jmap : JMapType ) : String =
	  getName( jmap ).replace( '.', '_' )
      }

  case class JMapVertexLabelProvider( label : Int )
       extends VertexNameProvider[JMapType] 
       with JMapIdentifierProvider {
	 var count : Int = label
	 override def nextSeqTag : String = {
	   count = count + 1;
	   ((count * 3) + 2).toString
	 }
	 override def getVertexName( jmap : JMapType ) : String =
	   getName( jmap ).replace( '.', '_' )
       }

  case class JMapEdgeNameProvider( label : Int )
       extends EdgeNameProvider[PipeEdge] 
       with JMapIdentifierProvider {
	 var count : Int = label
	 override def nextSeqTag : String = {
	   count = count + 1;
	   //(count * 3).toString
	   count.toString
	 }
	 override def getEdgeName( edge : PipeEdge ) : String =
	   nextSeqTag
       }

  def toDotFile( fileName : String, graph : GraphType ) : Unit = 
    new DOTExporter[JMapType,PipeEdge](
      new JMapVertexDotIdProvider(0),
      new JMapVertexLabelProvider(0),
      new JMapEdgeNameProvider(0)
    ).export( new FileWriter( fileName ), graph )

  def toFile( fileName : String, graph : GraphType ) : Unit =
    {
      val fw = new FileWriter( fileName );
      fw.write( graph.toString );
      fw.flush
    }
  
  trait Edgy[A,W,B] {
    def src : A
    def trgt : B
    def wire : W
  }
  class PipeEdge() extends Edgy[JMapType,JMapType,JMapType] {
    var _src : JMapType = null
    var _trgt : JMapType = null    
    var _wire : JMapType = null
    override def src = _src
    override def trgt = _trgt
    override def wire = _wire
    override def toString : String =
      ("("
       + src.toString
       + " -> "
       + trgt.toString
       + ")")

    def getEndProperty ( end : String ) ( property : String ) : String = 
      wire match {
	case null => ""
	case w : JMapType =>
	  w match {
	    case jobjMap : JOMapType =>
	      jobjMap match {
		case TheArgonaut.JSONObjectMap( _, jomap ) =>
		  jomap.get( end ) match {
		    case Some( jsrcMap ) => 
		      jsrcMap match {
			case TheArgonaut.JSONObjectMap(	_, srcJObjMap ) =>
			  srcJObjMap.get( property ) match {
			    case Some( jid ) =>
			      jid match {
 				case TheArgonaut.JSONValueMap( jstrV ) => 
 				  jstrV match {
 				    case jstr : JStr => jstr.string_
				    case _ => throw new Exception("Bad JSON")
				  }
				case _ => throw new Exception("Bad JSON")
			      }
			    case None =>
			      throw new Exception(
				("Bad JSON or bad property(\""+property+"\")")
				)
			  }
			case _ => throw new Exception("Bad JSON")
		      }
		    case None =>
		      throw new Exception(
			("Bad JSON or bad end(\""+end+"\")")
			)
		  }
	      }
	    case _ => throw new Exception("Bad JSON")
	  }
      }

    def sourceModuleId : String = getEndProperty( "src" )( "moduleid" )
    def targetModuleId : String = getEndProperty( "tgt" )( "moduleid" )
    def sourceTerminalId : String = getEndProperty( "src" )( "id" )
    def targetTerminalId : String = getEndProperty( "tgt" )( "id" )
  }
  class PipeEdgeFactory extends EdgeFactory[JMapType,PipeEdge] {
    override def createEdge(
      src : JMapType,
      trgt : JMapType
    ) : PipeEdge =
      {
	val ans = new PipeEdge()
	ans._src = src
	ans._trgt = trgt
	ans
      }
  }
}

object CGT extends ConcreteGraphTypes {
}
