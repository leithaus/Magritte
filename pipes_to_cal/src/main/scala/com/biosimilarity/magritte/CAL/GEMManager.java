// -*- mode: Java/l;-*- 
// Filename:    GEMManager.java 
// Authors:     lgm                                                    
// Creation:    Fri Jul 25 14:07:08 2008 
// Copyright:   Not supplied 
// Description: 
// ------------------------------------------------------------------------

package com.biosimilarity.magritte.CAL;

import java.math.BigInteger;

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
import org.openquark.gems.client.services.GemFactory;

import org.openquark.cal.compiler.TypeExpr;

public class GEMManager {
    private static final ModuleName GEM_GRAPH_TYPE_CHECKING_MODULE =
	ModuleName.make("GemCutterSaveModule");
    private static final ModuleName RUN_MODULE =
	ModuleName.make("GemManager.RunModule");

    String           _workSpaceFileName = "gemcutter.default.cws";
    MessageLogger    _messageLogger;
    BasicCALServices _calServices;
    GemFactory       _gemFactory;

    public GEMManager()
    {
	_messageLogger = null;
	_calServices = null;
	_gemFactory = null;
    }

    public String getWorkspaceFileName() 
    {
	return ( _workSpaceFileName );
    }

    public void setWorkspaceFileName( String workSpaceFileName ) 
    {
	_workSpaceFileName = workSpaceFileName ;
    }

    public MessageLogger getMessageLogger() 
    {
	return ( _messageLogger );
    }

    public void setMessageLogger( MessageLogger messageLogger ) 
    {
	_messageLogger = messageLogger ;
    }

    public BasicCALServices getCalServices() 
    {
	return ( _calServices );
    }

    public void setCalServices( BasicCALServices calServices ) 
    {
	_calServices = calServices ;
    }

    public GemFactory getGemFactory() 
    {
	return ( _gemFactory );
    }

    public void setGemFactory( GemFactory gemFactory ) 
    {
	_gemFactory = gemFactory ;
    }

    public QualifiedName makeQualifiedName( String qnc ) 
    {
	int     lstDotPos  = qnc.lastIndexOf( "." );
	String  modNameStr = qnc.substring( 0, lstDotPos );
	String  fnNameStr  = qnc.substring( lstDotPos+1 );

	ModuleName modName = ModuleName.make( modNameStr );

	return ( QualifiedName.make( modName, fnNameStr ) );
    }

    public void startMessageLogger() 
    {
	setMessageLogger( new MessageLogger() );
    }

    public void startServices()
    {
	setCalServices(
		       BasicCALServices.makeCompiled(
						     getWorkspaceFileName(),
						     getMessageLogger()
						     )
		       );
    }

    public void startServices( String workspaceFileName )
    {
	setWorkspaceFileName( workspaceFileName );
	startServices();
    }

    public void startGemFactory()
    {
	setGemFactory( new GemFactory( getCalServices() ) );
    }

    public void startGemFactory( BasicCALServices calServices )
    {
	setGemFactory( new GemFactory( calServices ) );
    }
    
    public void startCAL()
    {
	startMessageLogger();
	startServices();
	startGemFactory();
    }

    public void startCAL( String workspaceFileName )
    {
	startMessageLogger();
	startServices( workspaceFileName );	
	startGemFactory();
    }

    public void startCAL( String workspaceFileName,
			  MessageLogger messageLogger )
    {
	setMessageLogger( messageLogger );
	startServices( workspaceFileName );	
	startGemFactory();
    }

    public void startCAL( MessageLogger messageLogger,
			  BasicCALServices calServices )
    {
	setMessageLogger( messageLogger );
	setCalServices( calServices );	
	startGemFactory( );
    }

    public Gem makeFAGem( String qnc )
    {
	return( getGemFactory().makeFunctionalAgentGem( makeQualifiedName( qnc ) ) );
    }

    public GemGraph makeTestGemGraph( )
    {
	startCAL();
	GemGraph ggraph = new GemGraph();
	ggraph.getTargetCollector().setName( "phred" );

	Gem idGem = makeFAGem( "Cal.Core.Prelude.id" );
	ggraph.addGem( idGem );
	Gem reverseStrGem = makeFAGem( "Cal.Core.String.reverse" );
	ggraph.addGem( reverseStrGem );
	Gem decimalToStrGem = makeFAGem( "Cal.Utilities.Decimal.toString" );
	ggraph.addGem( decimalToStrGem );
	Gem dblToDecimalGem = makeFAGem( "Cal.Utilities.Decimal.fromDouble" );
	ggraph.addGem( dblToDecimalGem );
	Gem powerGem = makeFAGem( "Cal.Utilities.Math.power" );
	ggraph.addGem( powerGem );
	Gem piGem = makeFAGem( "Cal.Utilities.Math.pi" );
	ggraph.addGem( piGem );
	Gem eGem = makeFAGem( "Cal.Utilities.Math.e" );
	ggraph.addGem( eGem );

	ggraph.connectGems(
			   piGem.getOutputPart(),
			   powerGem.getInputPart( 0 )
			   );

	ggraph.connectGems(
			   eGem.getOutputPart(),
			   powerGem.getInputPart( 1 )
			   );

	ggraph.connectGems(
			   powerGem.getOutputPart(),
			   dblToDecimalGem.getInputPart( 0 )
			   );

	ggraph.connectGems(
			   dblToDecimalGem.getOutputPart(),
			   decimalToStrGem.getInputPart( 0 )
			   );

	ggraph.connectGems(
			   decimalToStrGem.getOutputPart(),
			   reverseStrGem.getInputPart( 0 )
			   );

	ggraph.connectGems(
			   reverseStrGem.getOutputPart(),
			   idGem.getInputPart( 0 )
			   );
	
	try {
	    ggraph.typeGemGraph( getCalServices().getTypeCheckInfo( GEM_GRAPH_TYPE_CHECKING_MODULE ) );
	    ggraph.connectGems(idGem.getOutputPart(), ggraph.getTargetCollector().getInputPart(0));

	    EntryPointSpec entryPointSpec =
		getCalServices().addNewModuleWithFunction( RUN_MODULE, ggraph.getCALSource() );

	    Object result = getCalServices().runFunction( entryPointSpec, new Object[]{ } );

	    System.out.println( "test result : " + result );
	}
	catch (Exception e)
	    {
		e.printStackTrace(System.err);
		return ( null );
	    }

	return ( ggraph );
    }

    public GemGraph makeTestQuantGemGraph( )
    {
	startCAL();
	GemGraph ggraph = new GemGraph();
	ggraph.getTargetCollector().setName( "quant" );

	Gem idGem = makeFAGem( "Cal.Core.Prelude.id" );
	ggraph.addGem( idGem );	
	Gem vARGem = makeFAGem( "Cal.Experimental.Quant.VAR.vectorAutoRegressiveModel" );
	ggraph.addGem( vARGem );
	Gem oneGem =
	    new ValueGem(new LiteralValueNode(BigInteger.valueOf(1),
					      getCalServices().getPreludeTypeConstants().getIntegerType()));
	ggraph.addGem( oneGem );
	Gem timeSeriesOneGem = makeFAGem( "Cal.Experimental.Quant.VAR.timeseries1" );
	ggraph.addGem( timeSeriesOneGem );

	ggraph.connectGems(
			   oneGem.getOutputPart(),
			   vARGem.getInputPart( 0 )
			   );

	ggraph.connectGems(
			   timeSeriesOneGem.getOutputPart(),
			   vARGem.getInputPart( 1 )
			   );;

	ggraph.connectGems(
			   vARGem.getOutputPart(),
			   idGem.getInputPart( 0 )
			   );
	
	try {
	    Boolean closedExpr = GemGraph.obtainUnboundDescendantInputs(
									idGem,
									GemGraph.TraversalScope.TREE,
									GemGraph.InputCollectMode.ALL
									).isEmpty();

	    Object result = null;

	    if (closedExpr) 
		{
		    ggraph.connectGems(idGem.getOutputPart(), ggraph.getTargetCollector().getInputPart(0));
		    ggraph.typeGemGraph( getCalServices().getTypeCheckInfo( GEM_GRAPH_TYPE_CHECKING_MODULE ) );	    
	    
		    EntryPointSpec entryPointSpec =
			getCalServices().addNewModuleWithFunction( RUN_MODULE, ggraph.getCALSource() );

		    result = getCalServices().runFunction( entryPointSpec, new Object[]{ } );
		}
	    else {
		result = "#<closure>";
	    }

	    System.out.println( "test result : " + result );
	}
	catch (Exception e)
	    {
		e.printStackTrace(System.err);
		return ( null );
	    }

	return ( ggraph );
    }
}
