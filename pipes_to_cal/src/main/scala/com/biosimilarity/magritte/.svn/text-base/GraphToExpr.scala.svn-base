// -*- mode: Scala;-*- 
// Filename:    GraphToExpr.scala 
// Authors:     lgm                                                    
// Creation:    Thu Jun 26 11:36:02 2008 
// Copyright:   Not supplied 
// Description: 
// ------------------------------------------------------------------------

package com.biosimilarity.magritte

import com.biosimilarity.magritte._
import org.jgrapht._
import org.jgrapht.graph._
import org.jgrapht.ext._

import scala.collection.immutable.HashMap

trait GraphTraverser[T] {
  self : ConcreteGraphTypes =>

    def sunk( graph : GraphType)( acc : List[VertexType], v : VertexType )
    : List[VertexType] =
      if (graph.outDegreeOf( v ) == 0) acc ::: List( v ) else acc    

    def sourceIt( state : Option[T] )( graph : GraphType )( acc : List[T], e : EdgeType )
    : List[T] =
    (acc ::: List( compileComponent( state )( graph, graph.getEdgeSource( e ) ) ) )

  def compileComponent( state : Option[T] )
  ( graph : GraphType, vertex : VertexType ) : T

  def compile( state : Option[T] )( graph : GraphType ) : List[T] = 
    (((List() : List[VertexType])
      /: scala.collection.jcl.Set.apply[VertexType](graph.vertexSet))
     ( sunk( graph ) )).map( v => compileComponent( state )( graph, v ) )
}

trait Compiler extends GraphTraverser[TheLambdaGenerators.RMExpressions.Expression] {
  self : ConcreteGraphTypes =>    

  override def compileComponent(
    state : Option[TheLambdaGenerators.RMExpressions.Expression]
  )( graph : GraphType, vertex : VertexType )
    : TheLambdaGenerators.RMExpressions.Expression = 
      TheLambdaGenerators.RMExpressions.Application(
	TheLambdaGenerators.RMExpressions.Value( vertex ),
	(((List() : List[TheLambdaGenerators.RMExpressions.Expression])
	  /:
	  scala.collection.jcl.Set.apply[EdgeType](
	    graph.incomingEdgesOf( vertex )
	  ))( sourceIt( None  )( graph ) ))
      )

}

object TheCompiler
  extends Compiler
  with ConcreteGraphTypes
  with Weaver {
}
