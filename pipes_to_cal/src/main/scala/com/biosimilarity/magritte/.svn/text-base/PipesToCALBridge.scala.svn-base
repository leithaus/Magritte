// -*- mode: Scala;-*- 
// Filename:    json2Cal.scala 
// Authors:     TName                                                    
// Creation:    Tue May 20 18:55:18 2008 
// Copyright:   Not supplied 
// Description: Takes the json from the pipe description and produces
// CAL code.
// ------------------------------------------------------------------------

package com.biosimilarity.magritte

import com.biosimilarity.magritte.json._
import com.biosimilarity.magritte.json.Absyn._
import org.jgrapht._;
import org.jgrapht.graph._;

import org.openquark.cal.compiler.io.EntryPointSpec;
import org.openquark.gems.client.CollectorGem;
import org.openquark.gems.client.Gem;
import org.openquark.gems.client.GemGraph;
import org.openquark.gems.client.FunctionalAgentGem;
import org.openquark.gems.client.ReflectorGem;
import org.openquark.gems.client.ValueGem;
import org.openquark.gems.client.services.GemFactory;
import org.openquark.cal.compiler.CALSourceGenerator;
import org.openquark.cal.compiler.Scope;
import org.openquark.cal.services.Status;

import java.io.StringReader

trait PipesToCALBridge {    
  def handleRequest( request : String ) : String

  def buildCALExprFromJSON( json : JSONObject ) : Option[String] =
    {
      Some( PrettyPrinter.show(json) )
    }
  
  def evalCALExpr( expr : String ) : Option[String] =
    {
      Some( expr )
    }
}

object ThePipesToCALBridge
extends PipesToCALBridge
with ConcreteGraphTypes
with Argonaut
with Fleece 
with Weaver
with Compiler {      

  object SExprStr 
  extends TheLambdaGenerators.RMExpressions.ExprVisitor[String] {  
    def separation(
      accStr : String,
      expr : TheLambdaGenerators.RMExpressions.Expression
    )
    : String =
      accStr match {
	case "" => ""
	case _ => 
	  expr match {
	    case TheLambdaGenerators.RMExpressions.Value( _ ) => " "
	    case TheLambdaGenerators.RMExpressions.Binder( _ ) => ","
	    case TheLambdaGenerators.RMExpressions.Mention( _ ) => " "
	    case TheLambdaGenerators.RMExpressions.Abstraction( _, _ ) => " "
	    case TheLambdaGenerators.RMExpressions.Application( _, _ ) => " "
	  }	
      }
    def sToStringAction(
      acc : Option[String],
      expr : Option[String]
      ) : Option[String] =
	acc match {
	  case None => None
	  case Some( accStr ) =>
	    expr match {
	      case None => None
	      case Some( exprStr ) =>
		accStr match {
		  case "" => Some( exprStr )
		  case _ => Some( accStr + " " + exprStr )
		}
	    }
	}
    def toStringAction(      
      acc : Option[String],
      bnd : Option[String],
      expr : TheLambdaGenerators.RMExpressions.Expression
    )
    : Option[String] =
      acc match {
	case None => None
	case Some( accStr ) => 
	  expr match {
	    case TheLambdaGenerators.RMExpressions.Bottom => Some( TheLambdaGenerators.RMExpressions.Bottom.toString )
	    case TheLambdaGenerators.RMExpressions.Value( v ) =>
	      v match {
		case jobj : JSONObjectMap =>
		  jobj match {
		    case JSONObjectMap( _, jmap ) =>
		      jmap.get("type") match {
			case None => None
			case Some( jstrVM ) => 
			  jstrVM match {
			    case JSONValueMap( jstrV ) =>
			      jstrV match {
				case jstr : JStr => 
				  Some(
				    ( accStr
				     + separation( accStr, expr )
				     + jstr.string_ )
				    )
			      }
			  }
		      }
		  }
		case _ => None
	      }
	    case TheLambdaGenerators.RMExpressions.Binder( _ ) => 
	      Some( accStr + separation( accStr, expr ) + expr.toString )
	    case TheLambdaGenerators.RMExpressions.Mention( _ ) => 
	      Some( accStr + separation( accStr, expr ) + expr.toString )
	    case TheLambdaGenerators.RMExpressions.Abstraction( _, _ ) => 
	      bnd match {
		case None => None
		case Some( bndStr ) =>
		  Some( bndStr  + " -> " + accStr )
	      }
	    case TheLambdaGenerators.RMExpressions.Application( _, _ ) => 
	      bnd match {
		case None => None
		case Some( bndStr ) =>
		  Some( 
		    ("(" 
		     + accStr
		     + separation( bndStr, expr )
		     + bndStr
		     + ")")
		    )
	      }
	  }
      }
}

  def exprStr(
    str : String,
    tExpr : TheLambdaGenerators.RMExpressions.Expression
  ) : String =
    (SExprStr.visit
     (Some(""))
     (SExprStr.sToStringAction)
     (
       (Some("") : Option[String]),
       tExpr,
       SExprStr.toStringAction
     )) match {
      case None => "failed to expr reduce to String"
      case Some( s ) => 
	str match {
	  case "" => s
	  case _ => (str + "," + s)
	}
    }

  override def buildCALExprFromJSON( json : JSONObject )
  : Option[String] =
    JSONToGraph( json ) match {
      case None => None
      case Some( graph ) =>
	Some(
	  ("["
	   + ( "" /: compile( None )( graph ) )( exprStr )
	   + "]")
	  )
    }

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

  override def handleRequest( request : String ) : String =
    {
      // really need to find out the Scala equivalent of do-notation
      parseJSONStr( request ) match {
	case Some( stuff ) => 
	  buildCALExprFromJSON( stuff ) match {
	    case Some( expr ) =>
	      evalCALExpr( expr ) match {
		case Some( rslt ) => rslt
		case None => "failed to evaluate"
	      }
	    case None => "failed to build CAL expr"
	  }
	case None => "failed to parse JSON"
      }
    }
}

object TheOtherPipesToCALBridge
extends PipesToCALBridge
with ConcreteGraphTypes
with Argonaut
with Fleece 
with Weaver
with GemTargetCompilers
with GraphToGemGraph {    
  def saySomething : String = "Ok... something\n"
  def saySomethingElse : String = "Ok... something else\n"  
  def evalGemGraph( ggraph : GemGraph ) : Option[String] 
  = // Some( ggraph.toString() )
    {
      try {
	// Does the graph type?
	logIt(
	  "/* **** type-checking graph **** */"
	);

	logIt(
	  "ggraph.typeGemGraph( CALServices.getTypeCheckInfo( GEM_GRAPH_TYPE_CHECKING_MODULE ) );"
	  );

	ggraph.typeGemGraph(
	  CALServices.getTypeCheckInfo(
	    GEM_GRAPH_TYPE_CHECKING_MODULE
	  )
	);

	logIt(
	  "/* **** type-checked graph **** */"
	);

	logIt(
	  "val entryPointSpec : EntryPointSpec = CALServices.addNewModuleWithFunction(RUN_MODULE,ggraph.getCALSource());"
	  );

	// Make an entry point
	val entryPointSpec : EntryPointSpec =
	  CALServices.addNewModuleWithFunction(
	    RUN_MODULE,
	    ggraph.getCALSource()
	  );

	// Run the expression -- this assumes that we've got a closed
	// expression or are comfortable return closures on expression
	// with free vars

	logIt(
	  "/* **** Running Gem graph  **** */"
	  );

	logIt(
	  "val rslt : java.lang.Object = CALServices.runFunction( entryPointSpec, (List() : List[java.lang.Object]).toArray );"
	  );

	val rslt : java.lang.Object =
	  CALServices.runFunction(
	    entryPointSpec, (List() : List[java.lang.Object]).toArray
	  );

	logIt(
	  "/* **** Ran Gem graph successfully  **** */"
	  );
      
	logIt(
	  "Some( rslt.toString )"
	  );

	Some( rslt.toString )
	} catch {
	  // Nesting the try's is tricky if you want this function to
	  // be re-usable independently of handleRequest
	    case e => {
	      val sw : java.io.StringWriter =
		new java.io.StringWriter( );		    

	      sw.write("/* **************************************** begin trace ****************************************\n");

	      e.printStackTrace( new java.io.PrintWriter( sw, true ) );	      

	      sw.write("**************************************** end trace **************************************** */");

	      logIt(
		"val entryPointSpec : EntryPointSpec = CALServices.addNewModuleWithFunction(RUN_MODULE,ggraph.getCALSource());"
	      );

	      logIt(
		"val rslt : java.lang.Object = CALServices.runFunction( entryPointSpec, (List() : List[java.lang.Object]).toArray );"
	      );

	      logIt(
		"Some( rslt.toString )"
	      );

	      logIt(
		sw.toString()
		);

	      None
	    }
	}
    }

  def evalRootedGemGraph(
    rggraph : JustifiedRootedGemGraph[Gem,Option[CGT.EdgeType]]
  ) : Option[String] 
  = 
    rggraph match {
      case JustifiedRootedGemGraph( root, ggraph, _ ) => {
	logIt(
	  "def evaluateGraph : Option[String] = "
	  );
	logIt(
	  "{\n"
	  );
	GemGraph.obtainUnboundDescendantInputs(
	  root,
	  GemGraph.TraversalScope.TREE,
	  GemGraph.InputCollectMode.ALL
	).isEmpty match {
	  case true => {
	    logIt(
	      "/* **** graph is a closed form **** */"
	    );
	    val ans = evalGemGraph( ggraph );

	    logIt(
		"}\n"
	      );

	    ans
	  }
	  case _ => {
	    logIt(
	      "/* **** graph has unconnected terminals -- treating as closure **** */"
	    );

	    logIt(
	      "Some( CALSourceGenerator.getFunctionText( ggraph.getTargetCollector().getUnqualifiedName(), ggraph.getTargetCollector(), Scope.PUBLIC ) )"
	      );
	    	    
	    logIt(
	      "}\n"
	    );

	    Some(
	      CALSourceGenerator.getFunctionText(
		ggraph.getTargetCollector().getUnqualifiedName(),
		ggraph.getTargetCollector(),
		Scope.PUBLIC
	      )
	    )
	  }
	}
      }
    }

  def logPreamble : Unit =
    {
      logIt(
	  "import org.openquark.cal.compiler.io.EntryPointSpec;"
	  );
	logIt(
	  "import org.openquark.gems.client.CollectorGem;"
	  );
	logIt(
	  "import org.openquark.gems.client.Gem;"
	  );
	logIt(
	  "import org.openquark.gems.client.GemGraph;"
	  );
	logIt(
	  "import org.openquark.gems.client.FunctionalAgentGem;"
	  );
	logIt(
	  "import org.openquark.gems.client.ReflectorGem;"
	  );
	logIt(
	  "import org.openquark.gems.client.ValueGem;"
	  );
	logIt(
	  "import org.openquark.gems.client.services.GemFactory;"
	  );
	logIt(
	  "import org.openquark.cal.compiler.CALSourceGenerator;"
	  );
	logIt(
	  "import org.openquark.cal.compiler.Scope;"
	  );
	logIt(
	  "import org.openquark.cal.services.Status;"
	  );
	logIt(
	  ("\n" + "object " + "GemGraph" + java.lang.System.currentTimeMillis)
	  );
	logIt(
	  "extends GraphToGemGraph"
	  );
	logIt(
	  "{\n"
	  );	


        logIt(
	  "var _ggraph : GemGraph = null\n"
	  )
	logIt(
	  "def ggraph : GemGraph = _ggraph match { case null => { _ggraph = new GemGraph(); _ggraph } case _ => _ggraph }\n"
	  );		
    }

  def logClosure : Unit =
    {
      logIt(
	("\n" + "}\n")
      );
    }

  def buildRootedGemGraphFromJSON( json : JSONObject ) 
  : Option[List[JustifiedRootedGemGraph[Gem,Option[CGT.EdgeType]]]] =
    JSONToGraph( json ) match {
      case None => None
      case Some( graph ) => {
	logIt(
	  "def BuildGraph : Unit = "
	  );
	logIt(
	  "{\n"
	  );
	logIt(
	  "val gTrgtCollector : CollectorGem = ggraph.getTargetCollector;\n"
	  );
	logIt(
	  "gTrgtCollector.setName(\"magritteEvaluation\");\n"	
	  );
	Some(
	  compile(
	    Some(
	      JustifiedRootedGemGraph(
		null,
		{
		  val gg : GemGraph = new GemGraph();
		  gg.getTargetCollector().setName("magritteEvaluation");
		  gg
		},
		None
	      )
	    )
	  )( graph ).map(
	    ( rgg : JustifiedRootedGemGraph[Gem,Option[CGT.EdgeType]] ) =>
	      rgg match {
		case JustifiedRootedGemGraph( root, ggraph, _ ) => {
		  root match {
		    case faRoot : FunctionalAgentGem => {
		      logIt(
			("/* **** graph root: "
			   + root.toString
			   + " is a FunctionalAgentGem **** */")
			);
		      faRoot.asInstanceOf[FunctionalAgentGem].getName().getQualifiedName()
		      match {			
			case defaultOutputContinuationQNameStr => {
			  logIt(
			    ("/* **** graph root: "
			     + root.toString
			     + " name matches defaultOutputContinuationQNameStr **** */")
			    );
			  if (defaultOutputContinuationNeedsAccumulator)
			    {
			      val defaultOutputContAcc : Gem = 
				defaultOutputContinuationAccumulator( ggraph );

			      logIt(
				"ggraph.connectGems( defaultOutputContAcc.getOutputPart(), root.getInputPart( defaultOutputContinuationAccumulatorPos ) )"
				);
			      
			      ggraph.connectGems(
				defaultOutputContAcc.getOutputPart(),
				root.getInputPart( defaultOutputContinuationAccumulatorPos )
			      )
			    }
			}
		      }
		    }
		  };
		  ggraph.connectGems(root.getOutputPart(), ggraph.getTargetCollector().getInputPart(0));
		  logIt(
		    ("ggraph.connectGems( "
		     + (root.toString.replace( '.', '_' ) + ".getOutputPart()")
		     + ", "
		     + ("gTrgtCollector" + ".getInputPart(0));"))
		    );
		  logIt(
		    ("\n" + "}\n")
		    );
		  
		  JustifiedRootedGemGraph( root, ggraph, None )
		}
	      }
	  )
	)
      }
    }
  
  def buildGemGraphFromJSON( json : JSONObject )
  : Option[List[GemGraph]] =
    JSONToGraph( json ) match {
      case None => None
      case Some( graph ) => 
	Some( GraphToGemGraph( graph ) )
    }

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

  override def handleRequest( request : String ) : String =
    {
      try {
	// really need to find out the Scala equivalent of do-notation
	logIt(
	  "/* **** Handling request **** "
	  );
	logIt(
	  request
	  );
	logIt(
	  " **** **** **** */"
	  );
	parseJSONStr( request ) match {
	  case Some( stuff ) => {
	    logIt(
	      "/* **** JSON request parsed **** */"
	      );

	    logIt(
	      "/* **** Building Gem graph(s) **** */"
	      );

	    logPreamble;

	    val ans : String = 
	      buildRootedGemGraphFromJSON( stuff ) match {
	      case Some( ggraphs ) => {
		logIt(
		  "/* **** Gem graph(s) built **** */"
		  );

		logIt(
		  "/* **** Evaluating Gem graph(s) **** */"
		  );

		(( "" /: ggraphs)
		 {(
		   acc : String,
		   rggraph : JustifiedRootedGemGraph[Gem,Option[CGT.EdgeType]]
		 ) => {
		   logIt(
			 "/* **** Evaluating Gem graph **** */"
		   );
		   evalRootedGemGraph( rggraph ) match {
		     case Some( rslt ) => {
		       logIt(
			 "/* **** Evaluated Gem graph **** */"
			 );
		       acc match {
			 case "" => rslt
			 case _ => acc + "\n" + rslt
		       }
		     }
		     case None => {
		       logIt(
			 "/* **** Evaluation failed **** */"
			 );		       
		       //MessageLogger.toString
		       logClosure;
		       dumpLogToString
		     }
		   }}})
	      }
		case None => {
		  logIt(
		    "/* **** Building Gem graph failed **** */"
		    );
		  //MessageLogger.toString
		  logClosure;
		  dumpLogToString
		}
	    };
	    
	    ans

	  }
	  case None => {
	    logIt(
	      "/* **** JSON parsing failed **** */"
	      );
	    //MessageLogger.toString
	    logClosure;
	    dumpLogToString
	  }
	}
      }
      catch {
	case e => {
	  val sw : java.io.StringWriter =
	    new java.io.StringWriter( );		    
	  
	  sw.write(
	    ("current location: "
	     + new java.io.File( "." ).getCanonicalPath()
	     + "\n")
	  );

	  e.printStackTrace( new java.io.PrintWriter( sw, true ) );

	  logClosure;
	  dumpLogToWriter( sw )
	}
      }
    }
}

