// -*- mode: Scala;-*- 
// Filename:    GEMGraph.scala 
// Authors:     lgm                                                    
// Creation:    Tue Jul  8 16:13:08 2008 
// Copyright:   Not supplied 
// Description: 
// ------------------------------------------------------------------------

package com.biosimilarity.magritte

import com.biosimilarity.magritte._
import com.biosimilarity.magritte.CAL._
import org.jgrapht._
import org.jgrapht.graph._
import org.jgrapht.ext._

import org.openquark.cal.compiler.CALSourceGenerator;
import org.openquark.cal.compiler.MessageLogger;
import org.openquark.cal.compiler.ModuleName;
import org.openquark.cal.compiler.Scope;
import org.openquark.cal.compiler.SourceModel;
import org.openquark.cal.compiler.TypeException;
import org.openquark.cal.compiler.io.EntryPointSpec;
import org.openquark.cal.compiler.QualifiedName;
import org.openquark.cal.module.Cal.Collections.CAL_List;
import org.openquark.cal.module.Cal.Core.CAL_Prelude;
import org.openquark.cal.module.Cal.Utilities.CAL_Summary;
import org.openquark.cal.runtime.CALExecutorException;
import org.openquark.cal.services.BasicCALServices;
import org.openquark.cal.services.GemCompilationException;
import org.openquark.cal.valuenode.LiteralValueNode;
import org.openquark.gems.client.CollectorGem;
import org.openquark.gems.client.Gem;
import org.openquark.gems.client.GemGraph;
import org.openquark.gems.client.ReflectorGem;
import org.openquark.gems.client.ValueGem;
import org.openquark.gems.client.FunctionalAgentGem;
import org.openquark.gems.client.services.GemFactory;
import org.openquark.cal.services.Status;

import org.openquark.cal.compiler.TypeExpr;

import scala.collection.immutable.HashMap;

trait GemTargetCompilers {
  self : ConcreteGraphTypes =>
  var _workSpaceFileName : String = "gemcutter.default.cws"
  def WorkSpaceFileName : String = _workSpaceFileName

  var _messageLogger : MessageLogger = null
  def MessageLogger : MessageLogger = {
    if (_messageLogger == null) _messageLogger = new MessageLogger();
    _messageLogger
  }
  def logIt( msg : String ) : Unit =
    MessageLogger.logMessage((new Status( "\n" + msg )).asCompilerMessage())
  
  def dumpLogToString : String =
    dumpLogToWriter( new java.io.StringWriter() )

  def dumpLogToWriter( sw : java.io.StringWriter ) : String = {
    // sw.write(
//       ("current location: "
//        + new java.io.File( "." ).getCanonicalPath()
//        + "\n")
//     );    
    scala.collection.jcl.Conversions.convertList(
      com.biosimilarity.magritte.TheOtherPipesToCALBridge.MessageLogger.getCompilerMessages
    ).map( m => sw.write( m.toString.replace("Info: OK: ","") ) );

    sw.toString()
  }

  var _calServices : BasicCALServices = null
  def CALServices : BasicCALServices = {
    if (_calServices == null) {
      logIt(
	("/* **** initiating CAL services in "
	   + new java.io.File( "." ).getCanonicalPath()
	   + " **** */ \n")
	);

      _calServices =
	BasicCALServices.makeCompiled(
	  WorkSpaceFileName,
	  MessageLogger
	)
    };
    _calServices
  }

  var _gemFactory : GemFactory = null
  def GemFactoria : GemFactory = {
    if (_gemFactory == null) _gemFactory = new GemFactory( CALServices );
    _gemFactory
  }

  def GEM_GRAPH_TYPE_CHECKING_MODULE : ModuleName =
    ModuleName.make("GemCutterSaveModule")

  def RUN_MODULE : ModuleName =
    ModuleName.make("MagritteGemModel.RunModule")

  //val _defaultOutputContinuationModuleName : String = "Cal.Utilities.PrettyPrinter"
  val _defaultOutputContinuationModuleName : String = "Cal.Core.Prelude"
  //val _defaultOutputContinuationName : String = "pretty"
  val _defaultOutputContinuationName : String = "id"
  def defaultOutputContinuationQNameStr : String =
    _defaultOutputContinuationModuleName + "." + _defaultOutputContinuationName
  def defaultOutputContinuationQName : QualifiedName =
    QualifiedName.make(
      ModuleName.make( _defaultOutputContinuationModuleName ),
      _defaultOutputContinuationName
      )
  //def defaultOutputContinuationNeedsAccumulator : Boolean = true
  def defaultOutputContinuationNeedsAccumulator : Boolean = false

  val _defaultOutputContinuationAccumulatorModuleName : String = "Cal.Utilities.PrettyPrinter"
  val _defaultOutputContinuationAccumulatorName : String = "text"
  def defaultOutputContinuationAccumulatorQNameStr : String =
    _defaultOutputContinuationAccumulatorModuleName + _defaultOutputContinuationAccumulatorName
  def defaultOutputContinuationAccumulatorQName : QualifiedName =
    QualifiedName.make(
      ModuleName.make( _defaultOutputContinuationAccumulatorModuleName ),
      _defaultOutputContinuationAccumulatorName
      )

  //def defaultOutputContinuationAccumulatorNeedsSeed : Boolean = true
  def defaultOutputContinuationAccumulatorNeedsSeed : Boolean = false
  def defaultOutputContinuationAccumulatorSeed : java.lang.Object =
    " *output value* "
  var _seedGemPad : Gem = null

  def defaultOutputContinuationAccumulatorSeedGem( ggraph : GemGraph ) : Gem = {
    if (_seedGemPad == null) {
      _seedGemPad =
	new ValueGem(
	  new LiteralValueNode(
	    defaultOutputContinuationAccumulatorSeed,
	    CALServices.getPreludeTypeConstants().getStringType()
	  )
	);
      ggraph.addGem( _seedGemPad );
    };
    _seedGemPad
  }
  
  def defaultOutputContinuationAccumulatorSeedPos : Int = 0
  def defaultOutputContinuationAccumulatorPos : Int = 1
  var _defaultOutputContinuationAccumulatorPad : Gem = null

  def defaultOutputContinuationAccumulator( ggraph : GemGraph ) : Gem = {
    if (_defaultOutputContinuationAccumulatorPad == null) {
      _defaultOutputContinuationAccumulatorPad =
	GemFactoria.makeFunctionalAgentGem(
	  defaultOutputContinuationAccumulatorQName
	);
      
      ggraph.addGem( _defaultOutputContinuationAccumulatorPad );
      
      if ( defaultOutputContinuationAccumulatorNeedsSeed )
	{
	  ggraph.connectGems( 
	    defaultOutputContinuationAccumulatorSeedGem( ggraph ).getOutputPart(),
	    _defaultOutputContinuationAccumulatorPad.getInputPart(
	      defaultOutputContinuationAccumulatorSeedPos 
	    )
	  )
	};
    };
    _defaultOutputContinuationAccumulatorPad
  }

  def QuarkValue( v : Any ) : java.lang.Object 
  =
    throw new Exception( "write me" )
  def QuarkType( v : Any ) : TypeExpr
  =
    throw new Exception( "write me" )  

  trait ExpressionToGemGraph {
    def ExpressionToGemGraph(
      expr : TheLambdaGenerators.RMExpressions.Expression
    ) : GemGraph =
      throw new Exception( "not implemented" )

    def StringToGemGraph( exprStr : String ) : GemGraph =
      throw new Exception( "not implemented" )
    
  }
  
}

// The statefulness and non-compositional aspect of the GemGraph
  // makes this code particularly ugly.

  // Note: can we tighten this code by making RootedGemGraphs inherit
  // from GemGraphs?

case class RootedGemGraph[R]( root : R, graph : GemGraph )
case class JustifiedRootedGemGraph[R,J]( point : R, net : GemGraph, justification : J ) 
  extends RootedGemGraph[R]( point, net )

// 'HK' for higher-kinded... The issue is that we need to keep the
// link back to the root (the justification) as well as the root,
// itself. We have, however, a lot of code hanging off
// GraphToGemGraph. The change is a little painful. The question is
// whether we can use hk-techniques to manage the pain.
trait HKGraphToGemGraph[GraphShape[_,_],R,J]
     extends GraphTraverser[GraphShape[R,J]]
{
  self : ConcreteGraphTypes with GemTargetCompilers =>
    def getModuleName( vertex : VertexType ) : ModuleName
    def getName( vertex : VertexType ) : String
    def getQualifiedName( vertex : VertexType ) : QualifiedName 
    def inputFeedLinks( graph : GraphType, vertex : VertexType )
    : scala.collection.jcl.SetWrapper[EdgeType]
    def inputFeeds( graph : GraphType, vertex : VertexType )
    : List[VertexType]
}

trait GraphToGemGraph
     extends GraphTraverser[JustifiedRootedGemGraph[Gem,Option[CGT.EdgeType]]] {
  self : ConcreteGraphTypes with GemTargetCompilers =>

    def getModuleName( vertex : VertexType ) : ModuleName =
      {
	val qnstr = JMapVertexIdProvider( 0 ).grabName( vertex );
	ModuleName.make( qnstr.substring( 0, qnstr.lastIndexOf( "." ) ) )
      }
    def getName( vertex : VertexType ) : String =
      {
	val qnstr = JMapVertexIdProvider( 0 ).grabName( vertex );
	qnstr.substring( qnstr.lastIndexOf( "." )+1 )
      }
    def getQualifiedName( vertex : VertexType ) : QualifiedName 
    = {         
	val qnstr = JMapVertexIdProvider( 0 ).grabName( vertex );
	//val status : Status =
	//  new Status("Entering getQualifiedName with qnstr = " + qnstr + "\n");

	//MessageLogger.logMessage(status.asCompilerMessage());

	qnstr match {
	  case "output" => {
	    defaultOutputContinuationQName
	  }
	  case "_OUTPUT" => {
	    defaultOutputContinuationQName
	  }
	  case _ => {
	    val lstDotPos = qnstr.lastIndexOf( "." );
	    
	    QualifiedName.make(
	      ModuleName.make(
		qnstr.substring( 0, lstDotPos )
	      ),
	      qnstr.substring( lstDotPos+1 ) )
	  }
	}
      }

    def inputFeedLinks( graph : GraphType, vertex : VertexType )
    : scala.collection.jcl.SetWrapper[EdgeType]
    = 
      scala.collection.jcl.Set.apply[EdgeType](
	graph.incomingEdgesOf( vertex )
      )

    def inputFeeds( graph : GraphType, vertex : VertexType )
    : List[VertexType]
    = {
      (( (List() : List[VertexType]) /:	inputFeedLinks( graph, vertex ) )({
	( acc, e ) => acc ::: List( graph.getEdgeSource( e ) )
      }))
    }

    def sfn(fn : (Int => String), acc : List[String], i : Int)
    : List[String] =
      i match {
	case 0 => (fn.apply( i ) :: acc)
	case _ => sfn( fn, (fn.apply( i ) :: acc), (i - 1) )
      }

    def formalsList( faGem : FunctionalAgentGem ) : List[String] =
      {
	faGem.getGemEntity.getNNamedArguments match {
	  case 0 => (List() : List[String])
	  case numOfArgs =>
	    sfn(
	      { ( i : Int ) => faGem.getGemEntity.getNamedArgument( i ) },
	      (List() : List[String]),
	      (numOfArgs-1)
	    )
	}
      }

    def formalsPositionMap(
      graph : GraphType, vertex : VertexType, gem : FunctionalAgentGem
      ) : Map[String,Int]
    =
      {
	val fmls : List[String] = formalsList( gem );

	((new HashMap[String,Int] : Map[String,Int]) /:
	 inputFeedLinks( graph, vertex ).map(
	   { ( e : EdgeType ) => e.targetTerminalId }
	 ))({ ( acc : Map[String,Int], fml : String ) =>
	   acc.update( fml, fmls.indexOf( fml ) )
	   })
      }

    def connectComponent(
      ggraph : GemGraph,
      appHd : FunctionalAgentGem,
      fmlsPosMap : Map[String,Int],
      vNStr : String
    )(
	acc : Int, rgg : JustifiedRootedGemGraph[Gem,Option[CGT.EdgeType]]
      ) : Int =
       rgg match {
	 case JustifiedRootedGemGraph( subroot, _, j ) => {

	   val justifyingEdge : CGT.EdgeType =
	     j match {
	       case Some( e ) => e
	       case None => throw new Exception( "unconnected subroot" )
	     };
	   
	   val fmlsId : String =
	     justifyingEdge.targetTerminalId;
	   
	   val posInFmls : Option[Int] = fmlsPosMap.get( fmlsId );

	   val inputPartNum : Int =
	     posInFmls match {
	       case Some( p ) => {
		 logIt( 
		   "/* ******************************"
		 );
		 
		 logIt(
		   (
		     "got fmls position : "
		     + vNStr
		     + "("
		     + fmlsId
		     + ") : "
		     + p
		   )
		 );
		 
		 val fmlPos : Int =
		   (if (p < 0) {
		     logIt(
		       "position too small, using " + acc
		     );
		     acc
		   } else p);
		 
		 logIt( 
		   "****************************** */"
		 );
		 
		 fmlPos
	       }
	       case None => {
		 logIt(
		   "/* *********************************"
		 );
		 logIt(
		   "missed position, using " + acc
		 );
		 logIt( 
		   "****************************** */"
		 );
		 acc
	       }
	     }
	   
	   ggraph.connectGems(
	     subroot.getOutputPart(),
	     appHd.getInputPart(
	       inputPartNum
	     )
	   );
	   
	   logIt(
	     ("\n"
	      + "ggraph.connectGems( "
	      + (subroot.toString.replace( '.', '_' ) + ".getOutputPart()")
	      + ", "
	      + (appHd.toString.replace( '.', '_' ) + ".getInputPart( "
		 + acc 
		 + " )")
	      + " );"
	    )
	   );
	   
	   (acc + 1)
	 }
       }
       
    override def compileComponent(
      state : Option[JustifiedRootedGemGraph[Gem,Option[CGT.EdgeType]]]
    )(
      graph : GraphType,
      vertex : VertexType
    ) : JustifiedRootedGemGraph[Gem,Option[CGT.EdgeType]] = {	
      val vNStr : String =
	JMapVertexIdProvider( 0 ).grabName( vertex );

      logIt(
	("/* **** compiling vertex : " + vNStr + " **** */")
	);

      val just : Option[CGT.EdgeType] =
	(scala.collection.jcl.Conversions.convertSet(
	  graph.outgoingEdgesOf( vertex )
	)).toList match {
	  case Nil => None
	  case e::es => Some( e.asInstanceOf[CGT.EdgeType] )
	}
      
      val appHd : FunctionalAgentGem =
	GemFactoria.makeFunctionalAgentGem(
	  getQualifiedName( vertex )
	);

      val uvNStr : String =
	vNStr match {
	  case "output" =>
	    defaultOutputContinuationQNameStr
	  case "_OUTPUT" =>
	    defaultOutputContinuationQNameStr
	  case _ => vNStr
	}

      val lstDotPos = uvNStr.lastIndexOf( "." );

      logIt(
	("\n"
	 + "val "
	 + appHd.toString.replace( '.', '_' )
	 + " : Gem = GemFactoria.makeFunctionalAgentGem( "
	 + " QualifiedName.make( "
	 + " ModuleName.make( "
	 + ("\"" + uvNStr.substring( 0, lstDotPos ) + "\"")
	 + " ), "
	 + ("\"" + uvNStr.substring( lstDotPos+1 ) + "\"")
	 + " )"
	 + " );\n")
	);
      

      logIt(
	("/* **** functional agent gem for vertex with name "
	 + vNStr
	 +" is "
	 + appHd 
	 + " **** */")
	);
      
      state match {
	case Some( JustifiedRootedGemGraph( _, ggraph, _ ) ) => {
	  val nGraph : JustifiedRootedGemGraph[Gem,Option[CGT.EdgeType]] = 
	    new JustifiedRootedGemGraph( appHd, ggraph, just );
	  
	  ggraph.addGem( appHd );
	  
	  logIt(
	    "/* **** about to compile feeds: ****"
	  );

	  for( v <- inputFeeds( graph, vertex ) ) yield logIt(
	    "**** " + getQualifiedName( v ) + " ****"
	  );
	  
	  logIt(
	    "**** */"
	    );

	  val subRoots : List[JustifiedRootedGemGraph[Gem,Option[CGT.EdgeType]]] =
	    (((List() : List[JustifiedRootedGemGraph[Gem,Option[CGT.EdgeType]]])
	      /: inputFeedLinks( graph, vertex ))(
		sourceIt( Some( nGraph ) )( graph )
	      ));	 

	  val fmlsPosMap : Map[String,Int] =
	    formalsPositionMap( graph, vertex, appHd );
	  
	  // This cannot be in sourceIt because we won't have the
	  // index!
	  
	  (( (0 : Int) /: subRoots )
	   {(
	     acc: Int,
	     rgg : JustifiedRootedGemGraph[Gem,Option[CGT.EdgeType]]
	     ) => (connectComponent(ggraph, appHd, fmlsPosMap, vNStr)(acc, rgg))
	    });
	  
	  nGraph
	}
	case None => throw new Exception("bad component")
      }
      
    }

    def GraphToGemGraph( graph : GraphType )
    : List[GemGraph] = 
      compile(
	Some(
	  JustifiedRootedGemGraph(
	    null,
	    {
	      val gg : GemGraph = new GemGraph();
	      gg.getTargetCollector().setName("magritteEvaluation");
	      gg
	    },
	    null
	  )
	)
      )( graph ).map(
	( rgg : JustifiedRootedGemGraph[Gem,Option[CGT.EdgeType]] ) =>
	  rgg match {
	    case JustifiedRootedGemGraph( root, ggraph, _ ) => {
	      root match {
		case faRoot : FunctionalAgentGem =>
		  faRoot.asInstanceOf[FunctionalAgentGem].getName().getQualifiedName() match {
		    case defaultOutputContinuationQNameStr => 
		      if (defaultOutputContinuationNeedsAccumulator)
			{
			  val defaultOutputContAcc : Gem = 
			    defaultOutputContinuationAccumulator( ggraph );
			  
			  ggraph.connectGems(
			    defaultOutputContAcc.getOutputPart(),
			    root.getInputPart( defaultOutputContinuationAccumulatorPos )
			  )
			}
		  }
	      };
	      ggraph.connectGems(root.getOutputPart(), ggraph.getTargetCollector().getInputPart(0));
	      ggraph
	    }
	  }
      )
	
  }

object TheGEMGraphCompiler
       extends ConcreteGraphTypes
       with GemTargetCompilers
{
}


