// -*- mode: Scala;-*- 
// Filename:    Generators.scala 
// Authors:     lgm                                                    
// Creation:    Fri Jul 11 13:38:13 2008 
// Copyright:   Not supplied 
// Description: 
// ------------------------------------------------------------------------

package com.biosimilarity.magritte

import com.biosimilarity.magritte._

trait Generators {
  trait MExpressions {
    type Identifier
    trait MExpression
    case object Bottom extends MExpression {
      override def toString : String = "_|_"
    }
    case class Value( v : Any ) extends MExpression {
      override def toString : String =
	("#" + "<" + v.toString + ">")
    }
    case class Binder( x : Identifier ) extends MExpression {
      override def toString : String = x.toString
    }
    case class Mention( x : Identifier ) extends MExpression {
      override def toString : String = x.toString
    }
    def accStr[E]( sep : String)( acc : String, expr : E ) : String =
      acc match {
	case "" => expr.toString
	case _ => acc + sep + expr.toString
      }
    case class Abstraction( formals : List[Identifier], expr : MExpression )
	 extends MExpression {
	   override def toString : String =
	   ("\\"
	    + ("" /: formals)(accStr[Identifier](","))
	    + "."
	    + expr.toString)
	 }
    case class Application(
      head : MExpression,
      actuals : List[MExpression]
    ) extends MExpression {
      override def toString : String =
	("("
	 + head.toString
	 + ("" /: actuals)(accStr[MExpression](" "))
	 + ")")
    }

    trait ExprVisitor[R] {
      def visit( u : Option[R] )( sAct : (Option[R], Option[R]) => Option[R] )
      (
	acc : Option[R],
	expr : MExpression,
	action : (Option[R], Option[R], (MExpression)) => Option[R]	
      ) : Option[R]
      =
	expr match {
	  case Bottom => None
	  case Value( _ ) => action( acc, u, expr )
	  case Binder( _ ) => action( acc, u, expr )
	  case Mention( _ ) => action( acc, u, expr )
	  case Abstraction( fmls, body ) => {
	    val bodyR : Option[R] = visit( u )( sAct )( acc, body, action );
	    val fmlsR : Option[R] =
	      (( u /: fmls.map( Binder ) )
	       ( { ( nacc : Option[R], nexpr : MExpression) =>
		   sAct( nacc, action( u, u, nexpr ) ) } ))
	    action( bodyR, fmlsR, expr )
	  }
	  case Application( head, actuals ) => {
	    val hdR : Option[R] = visit( u )( sAct )( u, head, action );
	    val actlsR : Option[R] =
	      (( acc /: actuals )
	       ( { ( nacc : Option[R], nexpr : MExpression) =>
		   sAct( nacc, visit( u )( sAct )( u, nexpr, action ) ) } ));
	    action( hdR, actlsR, expr )
	  }
	}
    }    
  }
  trait MIdentifiers {
    type Expression
    trait MIdentifier
    case class StringId( id : String ) extends MIdentifier
    case class IntId( id : Int ) extends MIdentifier
    case class RId( id : Expression ) extends MIdentifier
  }
  object RMExpressions
  extends MExpressions
  with MIdentifiers {
    type Identifier = MIdentifier
    type Expression = MExpression
  }

  type TargetExpression = RMExpressions.Expression
  type TargetValue = RMExpressions.Value
  type TargetMention = RMExpressions.Mention
  type TargetAbstraction = RMExpressions.Abstraction
  type TargetApplication = RMExpressions.Application
}

object TheLambdaGenerators extends Generators {
}
