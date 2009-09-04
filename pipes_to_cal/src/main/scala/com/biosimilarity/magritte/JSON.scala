// -*- mode: Scala;-*- 
// Filename:    JSON.scala 
// Authors:     lgm                                                    
// Creation:    Mon Jun 23 12:07:38 2008 
// Copyright:   Not supplied 
// Description: 
// ------------------------------------------------------------------------

package com.biosimilarity.magritte

import com.biosimilarity.magritte.json._
import com.biosimilarity.magritte.json.Absyn._
import java.io.StringReader

trait Argonaut {
  var inputStream : java.io.StringReader = null

  def lexer () = new Yylex(inputStream)
  def lexer (str : String) = new Yylex(new StringReader(str))
  def parser () = new parser(lexer())
  def parser (str : String) = new parser(lexer(str))
  def parseJSONStr( str : String ) =
    {
      try {
	Some((parser(str)).pJSONObject())
      }
      catch {
	case _ => None
      }
    }
}

object TheArgonaut extends Argonaut with Fleece {
  def parseAndWrap( str : String ) : Option[JSONMap] =
    parseJSONStr( str ) match {
      case Some( json ) => wrapJSON( json ) 
      case None => None
    }
  def wrappedJSONToStr( wrj : Option[JSONMap] ) : String =
    try {
      wrj match {
	case Some( jmap ) => jmap.toStr("")
	case None => "None"
      }
    }
  catch {
    case _ => "None"
  }
  // ideally we test by parsing and wrapping the deparsing of the
  // parsed string and checking structural equivalence
}
