package com.biosimilarity.magritte;

import com.biosimilarity.magritte._;
import com.biosimilarity.magritte.CAL._;
import junit.framework._;
import Assert._;

import org.incava.util.diff._;

import org.openquark.cal.compiler.CALSourceGenerator;
import org.openquark.cal.compiler.io.EntryPointSpec;
import org.openquark.cal.compiler.Scope;
import org.openquark.cal.services.Status;
import org.openquark.gems.client.CollectorGem;
import org.openquark.gems.client.Gem;
import org.openquark.gems.client.GemGraph;
import org.openquark.gems.client.ReflectorGem;
import org.openquark.gems.client.ValueGem;
import org.openquark.gems.client.services.GemFactory;

object AppTest {
    def suite: Test = {
        val suite = new TestSuite(classOf[AppTest]);
        suite
    }

    def main(args : Array[String]) {
        junit.textui.TestRunner.run(suite);
    }
}

/**
 * Unit test for simple App.
 */
class AppTest extends TestCase("app") {
  val schemaDirStr : String = "/Users/lgm/work/src/projex/bobj/magritte/pipe-cutter/pipes_to_cal/src/test/data/schema"

  val compilerTestOneOutcomeIllTyped : String =
    "(output (Cal.Core.String.reverse (Cal.Utilities.Decimal.toString (Cal.Utilities.Math.power (Cal.Utilities.Math.pi) (Cal.Utilities.Math.e)))))"

  val compilerTestOneOutcome : String =
    "(output (Cal.Core.String.reverse (Cal.Utilities.Decimal.toString (Cal.Utilities.Decimal.fromDouble (Cal.Utilities.Math.power (Cal.Utilities.Math.pi) (Cal.Utilities.Math.e))))))"

  val compilerTestOneInputIllTyped : String =
    "{\"modules\":[{\"type\":\"Cal.Utilities.Math.e\",\"id\":\"sw-4676\",\"conf\":{}},{\"type\":\"output\",\"id\":\"_OUTPUT\",\"conf\":{}},{\"type\":\"Cal.Utilities.Math.power\",\"id\":\"sw-4683\",\"conf\":{}},{\"type\":\"Cal.Utilities.Math.pi\",\"id\":\"sw-4695\",\"conf\":{}},{\"type\":\"Cal.Utilities.Decimal.toString\",\"id\":\"sw-4699\",\"conf\":{}},{\"type\":\"Cal.Core.String.reverse\",\"id\":\"sw-4706\",\"conf\":{}}],\"wires\":[{\"id\":\"_w3\",\"src\":{\"id\":\"_OUTPUT\",\"moduleid\":\"sw-4676\"},\"tgt\":{\"id\":\"terminal-4684\",\"moduleid\":\"sw-4683\"}},{\"id\":\"_w9\",\"src\":{\"id\":\"_OUTPUT\",\"moduleid\":\"sw-4683\"},\"tgt\":{\"id\":\"decimal\",\"moduleid\":\"sw-4699\"}},{\"id\":\"_w5\",\"src\":{\"id\":\"_OUTPUT\",\"moduleid\":\"sw-4695\"},\"tgt\":{\"id\":\"terminal-4688\",\"moduleid\":\"sw-4683\"}},{\"id\":\"_w13\",\"src\":{\"id\":\"_OUTPUT\",\"moduleid\":\"sw-4699\"},\"tgt\":{\"id\":\"string\",\"moduleid\":\"sw-4706\"}},{\"id\":\"_w15\",\"src\":{\"id\":\"_OUTPUT\",\"moduleid\":\"sw-4706\"},\"tgt\":{\"id\":\"_INPUT\",\"moduleid\":\"_OUTPUT\"}}]}"

  val compilerTestZeroInput : String =
    "{\"modules\":[{\"type\":\"Cal.Utilities.Math.pi\",\"id\":\"sw-4676\",\"conf\":{}},{\"type\":\"output\",\"id\":\"_OUTPUT\",\"conf\":{}}],\"wires\":[{\"id\":\"_w1\",\"src\":{\"id\":\"_OUTPUT\",\"moduleid\":\"sw-4676\"},\"tgt\":{\"id\":\"_INPUT\",\"moduleid\":\"_OUTPUT\"}}]}"

  val compilerTestOneInput : String =
    "{\"modules\":[{\"type\":\"Cal.Utilities.Math.e\",\"id\":\"sw-4676\",\"conf\":{}},{\"type\":\"output\",\"id\":\"_OUTPUT\",\"conf\":{}},{\"type\":\"Cal.Utilities.Math.power\",\"id\":\"sw-4683\",\"conf\":{}},{\"type\":\"Cal.Utilities.Math.pi\",\"id\":\"sw-4695\",\"conf\":{}},{\"type\":\"Cal.Utilities.Decimal.fromDouble\",\"id\":\"sw-4697\",\"conf\":{}},{\"type\":\"Cal.Utilities.Decimal.toString\",\"id\":\"sw-4699\",\"conf\":{}},{\"type\":\"Cal.Core.String.reverse\",\"id\":\"sw-4706\",\"conf\":{}}],\"wires\":[{\"id\":\"_w3\",\"src\":{\"id\":\"_OUTPUT\",\"moduleid\":\"sw-4676\"},\"tgt\":{\"id\":\"terminal-4684\",\"moduleid\":\"sw-4683\"}},{\"id\":\"_w9\",\"src\":{\"id\":\"_OUTPUT\",\"moduleid\":\"sw-4683\"},\"tgt\":{\"id\":\"decimal\",\"moduleid\":\"sw-4697\"}},{\"id\":\"_w11\",\"src\":{\"id\":\"_OUTPUT\",\"moduleid\":\"sw-4697\"},\"tgt\":{\"id\":\"decimal\",\"moduleid\":\"sw-4699\"}},{\"id\":\"_w5\",\"src\":{\"id\":\"_OUTPUT\",\"moduleid\":\"sw-4695\"},\"tgt\":{\"id\":\"terminal-4688\",\"moduleid\":\"sw-4683\"}},{\"id\":\"_w13\",\"src\":{\"id\":\"_OUTPUT\",\"moduleid\":\"sw-4699\"},\"tgt\":{\"id\":\"string\",\"moduleid\":\"sw-4706\"}},{\"id\":\"_w15\",\"src\":{\"id\":\"_OUTPUT\",\"moduleid\":\"sw-4706\"},\"tgt\":{\"id\":\"_INPUT\",\"moduleid\":\"_OUTPUT\"}}]}"

  val compilerTestTwoInput : String = "{\"modules\":[{\"type\":\"Cal.WS.Trampoline.calculatorWSService_4CALTrampoline_add\",\"id\":\"sw-5007\",\"conf\":{}},{\"type\":\"output\",\"id\":\"_OUTPUT\",\"conf\":{}},{\"type\":\"Cal.Utilities.Math.round\",\"id\":\"sw-5042\",\"conf\":{}},{\"type\":\"Cal.Utilities.Math.round\",\"id\":\"sw-5050\",\"conf\":{}},{\"type\":\"Cal.Core.Prelude.toInt\",\"id\":\"sw-5082\",\"conf\":{}},{\"type\":\"Cal.Core.Prelude.toInt\",\"id\":\"sw-5089\",\"conf\":{}},{\"type\":\"Cal.WS.Trampoline.calculatorWSService_4CALTrampoline_new\",\"id\":\"sw-5096\",\"conf\":{}},{\"type\":\"Cal.Utilities.Math.e\",\"id\":\"sw-5100\",\"conf\":{}},{\"type\":\"Cal.Utilities.Math.pi\",\"id\":\"sw-5104\",\"conf\":{}}],\"wires\":[{\"id\":\"_w1\",\"src\":{\"id\":\"_OUTPUT\",\"moduleid\":\"sw-5007\"},\"tgt\":{\"id\":\"_INPUT\",\"moduleid\":\"_OUTPUT\"}},{\"id\":\"_w23\",\"src\":{\"id\":\"_OUTPUT\",\"moduleid\":\"sw-5042\"},\"tgt\":{\"id\":\"_INPUT\",\"moduleid\":\"sw-5082\"}},{\"id\":\"_w27\",\"src\":{\"id\":\"_OUTPUT\",\"moduleid\":\"sw-5050\"},\"tgt\":{\"id\":\"_INPUT\",\"moduleid\":\"sw-5089\"}},{\"id\":\"_w25\",\"src\":{\"id\":\"_OUTPUT\",\"moduleid\":\"sw-5082\"},\"tgt\":{\"id\":\"j\",\"moduleid\":\"sw-5007\"}},{\"id\":\"_w29\",\"src\":{\"id\":\"_OUTPUT\",\"moduleid\":\"sw-5089\"},\"tgt\":{\"id\":\"i\",\"moduleid\":\"sw-5007\"}},{\"id\":\"_w31\",\"src\":{\"id\":\"_OUTPUT\",\"moduleid\":\"sw-5096\"},\"tgt\":{\"id\":\"jCalculatorWSService_4CALTrampoline\",\"moduleid\":\"sw-5007\"}},{\"id\":\"_w33\",\"src\":{\"id\":\"_OUTPUT\",\"moduleid\":\"sw-5100\"},\"tgt\":{\"id\":\"terminal-5043\",\"moduleid\":\"sw-5042\"}},{\"id\":\"_w35\",\"src\":{\"id\":\"_OUTPUT\",\"moduleid\":\"sw-5104\"},\"tgt\":{\"id\":\"terminal-5051\",\"moduleid\":\"sw-5050\"}}]}"

  def maybeGraph : Option[ThePipesToCALBridge.GraphType] =
    ThePipesToCALBridge.parseAndGraph( compilerTestOneInput )

  def theOtherMaybeGraph( inputStr : String ) : Option[TheOtherPipesToCALBridge.GraphType] =
    TheOtherPipesToCALBridge.parseAndGraph( inputStr );
    
  def testOK() = assertTrue(true);
    //def testKO() = assertTrue(false);

  def testCompiler() = {          
    val exprs : List[TheLambdaGenerators.RMExpressions.Expression] =
      ThePipesToCALBridge.compile( None )(
	maybeGraph match {
	  case Some( graph ) => graph
	  case None => throw new Exception( "bad graph" )
	});

    val maybeCALExprStr : Option[String] =
      (com.biosimilarity.magritte.ThePipesToCALBridge.SExprStr.visit(Some(""))
       (com.biosimilarity.magritte.ThePipesToCALBridge.SExprStr.sToStringAction)
       (
	 (Some("") : Option[String]) ,
	 exprs.head,
	 com.biosimilarity.magritte.ThePipesToCALBridge.SExprStr.toStringAction
       ));

    val theCALExprStr : String =
      maybeCALExprStr match {
	case Some( s ) => s
	case None => throw new Exception("bad abstract syntax")
      }
      
    println(
      ("expected compiler outcome : "
       + "\n" 
       + compilerTestOneOutcome)
    )
    println(
      ("actual compiler outcome : "
       + "\n" 
       + theCALExprStr)
    )
    assertTrue( theCALExprStr == compilerTestOneOutcome );
  }

  def testGemDemo() = {
    GemModelDemo.entryPoint(null)
  }


  def gemTargetCompilerTester( inputTestStr : String ) : String = {
    TheOtherPipesToCALBridge.handleRequest( inputTestStr )
  }

  def gemTargetCompilerTesterOrig( inputTestStr : String ) = {
    val ggraphs : List[GemGraph] =
      TheOtherPipesToCALBridge.GraphToGemGraph(
	theOtherMaybeGraph( inputTestStr ) match {
	  case Some( gemgraphs ) => gemgraphs
	  case None => throw new Exception( "JSON to graph compilation failed" )
	}
      );    
    
    println(
      "gem graphs source: "
      + (("" /: ggraphs)(
	 {( acc : String, ggraph : GemGraph ) => {
	   ggraph.typeGemGraph(
	     TheOtherPipesToCALBridge.CALServices.getTypeCheckInfo(
	       TheOtherPipesToCALBridge.GEM_GRAPH_TYPE_CHECKING_MODULE
	     )
	   );
	   val entryPointSpec : EntryPointSpec =
	     TheOtherPipesToCALBridge.CALServices.addNewModuleWithFunction(
	       TheOtherPipesToCALBridge.RUN_MODULE,
	       ggraph.getCALSource()
	     );

	   val rslt : java.lang.Object =
	     TheOtherPipesToCALBridge.CALServices.runFunction(
	       entryPointSpec, (List() : List[java.lang.Object]).toArray
	     );
	   
	   (acc
	    + "\n"
	    + CALSourceGenerator.getFunctionText(
		ggraph.getTargetCollector().getUnqualifiedName(),
		ggraph.getTargetCollector(),
		Scope.PUBLIC
	      )
	    + ggraph.toString()
	    + "\n"
	    + "evaluates to: "
	    + "\n"
	    + rslt.toString()
	    )
	 }}
	)));

    assertTrue( true );
  }
  //def donttestGemTargetCompiler() = {
  def testGemTargetCompiler() = {
    println( gemTargetCompilerTester( compilerTestZeroInput ) );
    println( gemTargetCompilerTester( compilerTestOneInput ) );
    println( gemTargetCompilerTester( compilerTestTwoInput ) );
    assertTrue( true );
  }

  def testGEMManager() = {
    val gm : GEMManager = new GEMManager();

    try {
      gm.startServices();
      val ggraph : GemGraph = gm.makeTestGemGraph();      
      
      assertTrue( ggraph != null );

      val qggraph : GemGraph = gm.makeTestQuantGemGraph();

      assertTrue( qggraph != null );

      println(
	(
	  "GEM Manager test graph source: \n" 
	  + CALSourceGenerator.getFunctionText(
	    ggraph.getTargetCollector().getUnqualifiedName(),
	    ggraph.getTargetCollector(),
	    Scope.PUBLIC
	 )
       )
      )
    } catch {
      case e => {
	e.printStackTrace(System.err);
	assertTrue( false )
      }
    };
    
    assertTrue( gm.getCalServices() != null );
  }

  def testWSDLCompiler() {
    TheWSDL2CALWSClient.generateJavaWSClientToFile(
	"org.me.calculator.client",
	"org.me.calculator.client"
      )(
	"CalculatorWSService.wsdl",
	"CalculatorWSService_schema1.xsd",
	 schemaDirStr
	);

    val fileDiff : FileDiff =
      new FileDiff(
	schemaDirStr + "/CalculatorWSService_4CALTrampoline.java",
	schemaDirStr + "/WSDLCompilerTestExpectedOutput.java",
	);
    assertTrue( true )
  }

}
