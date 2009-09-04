// -*- mode: Scala;-*- 
// Filename:    PipesExprToGraph.scala 
// Authors:     lgm                                                    
// Creation:    Thu Jun 19 05:44:22 2008 
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
import java.io.StringReader
import java.io.FileWriter

trait Weaver {  
  self : ConcreteGraphTypes =>

  // the following code is a case study in why it's a good idea to
  // learn to use Scala's for-notation variant of Haskell's
  // do-notation. All the nested option stuff should collapse.
  def addModuleVertex( acc : Option[( VertexIdMapType, GraphType )],
		       jsonObjMap : JMapType )
  : Option[( VertexIdMapType, GraphType )]
  =
    acc match {
      case Some( ( vmap, graph ) ) => 
	jsonObjMap match {
	  case TheArgonaut.JSONObjectMap( _, jmap ) => 
 	    jmap.get("id") match {
 	      case Some( jid ) => 
 		jid match {
 		  case TheArgonaut.JSONValueMap( jstrV ) => 
 		    jstrV match {
 		      case jstr : JStr => 
 			{
 			  graph.addVertex( jsonObjMap );
 			  Some(
 			    (
 			      vmap.update( jstr.string_, jsonObjMap ),
 			      graph
 			      )
 			    )
 			}
 		      case _ => None
 		    }
 		  case _ => None
 		}
 	      case None => None
 	    }
 	}
      case None => None
    }

  def findPipeVertex(
    vmap : VertexIdMapType,
    jsonEdgeMap : Map[String,JMapType],
    end : String
  ) : Option[JMapType] =
    jsonEdgeMap.get( end ) match {
      case Some( jsrc ) => 
	jsrc match {
	  case TheArgonaut.JSONObjectMap( jsrcObj, jsrcMap ) =>
	    jsrcMap.get("moduleid") match {
	      case Some( jsrcIdStr ) =>
		jsrcIdStr match {
		  case TheArgonaut.JSONValueMap( jsrcIdV ) =>
		    jsrcIdV match {
		      case jsrcIdVStr : JStr => 
			vmap.get(jsrcIdVStr.string_)
		    }
		  case _ => None
		}
	      case None => None
	    }
	  case _ => None
	}
      case None => None
    }    

  def addPipeEdge( acc : Option[( VertexIdMapType, EdgeIdMapType, GraphType )],
 		  jsonObjMap : JMapType )
   : Option[( VertexIdMapType, EdgeIdMapType, GraphType )]
   =
     acc match {
       case Some( ( vmap, emap, graph ) ) => 
 	jsonObjMap match {
 	  case TheArgonaut.JSONObjectMap( jobj, jmap ) => 
 	    findPipeVertex( vmap, jmap, "src") match {
	      case Some( jsrcObj ) => 
 		findPipeVertex( vmap, jmap, "tgt") match {
 		  case Some( jtgtObj ) => 
 		    jmap.get("id") match {
 		      case Some( jvmStr ) => 
 			jvmStr match {
 			  case TheArgonaut.JSONValueMap( jstrV ) => 
 			    jstrV match {
 			      case jstr : JStr => {
				val newEdge =				  
 				  graph.addEdge(
 				    jsrcObj,
 				    jtgtObj 
 				  );
				newEdge._wire = jsonObjMap;
 				Some(
 				  (
 				    vmap,
 				    emap.update(
 				      jstr.string_,
				      newEdge
 				    ),
 				    graph
 				  )
 				)
			      }
 			      case _ => None
 			    }
 			  case _ => None
 			}
 		      case None => None
 		    }
 		  case None => None
 		  }
	      case None => None
	    }
 	  case _ => None
 	}
       case None => None
     }

  def populateGraphVertices( graph : GraphType, mmods : List[JMapType]
    ) : Option[(VertexIdMapType, GraphType)]
  =
    ((Some((new HashMap[String,TheArgonaut.JSONMap](),graph))
      : Option[(VertexIdMapType, GraphType)])
     /: mmods)(addModuleVertex)

  def connectGraphVertices(
    graph : GraphType, mmap : VertexIdMapType, mwires : List[JMapType]
  ) : Option[GraphType]
  =
    ((Some(
      (mmap,
       new HashMap[String,PipeEdge](),
       graph)
      ) : Option[( VertexIdMapType, EdgeIdMapType, GraphType )])
     /: mwires)(addPipeEdge)
    match {
      case Some( (_,_,graph) ) => Some( graph )
      case None => None
    }  

  def JMapToGraph( jsonObj : Option[JMapType] )
  : Option[GraphType] =
    {
      val ans : GraphType =
	new DefaultDirectedGraph[TheArgonaut.JSONMap,PipeEdge](new PipeEdgeFactory())
      
      jsonObj match {
	case Some( jobjMap ) => 
	  jobjMap match {
	    case TheArgonaut.JSONObjectMap( jobj, jmap ) =>
	      {
		jmap.get("modules") match {
		  case Some( jsonMap ) => 
		    jsonMap match {
		      case TheArgonaut.JSONArrayMap( jsonModArr, mmods ) => 
			populateGraphVertices( ans, mmods )
			match {
			  case Some( (vmap,graph) ) =>
			    {
			      jmap.get("wires") match {
				case Some( wiresMap ) =>
				  wiresMap match {
				    case TheArgonaut.JSONArrayMap( jsonWireArr, mwires ) =>	
				      connectGraphVertices(
					graph, vmap, mwires
				      )
				    case _ => None
				  }
				case None => None
			      }
			    }
			  case None => None
			}
		      case _ => None
		    }
		  case None => None
		}
	      }
	    case _ => None
	  }
	case None => None
      }
    }

  def JSONToGraph( jsonObj : JSONObject ) : Option[GraphType] =
    JMapToGraph( TheArgonaut.wrapJSON( jsonObj ) )
}

object TheWeaver
  extends Weaver
  with ConcreteGraphTypes
  with Argonaut
  with Fleece {
    def parseAndGraph( str : String )
    : Option[GraphType] 
    =
      try {
	parseJSONStr( str ) match {
	  case Some( json ) => JSONToGraph( json )
	  case None => None
	}
      }
    catch {
      case _ => None
    }
}
