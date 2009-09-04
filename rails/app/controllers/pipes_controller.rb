include Java

#require '/Users/lgm/.m2/repository/org/scala-lang/scala-library/2.7.1/scala-library-2.7.1'
require '/Users/lgm/.m2/repository/org/scala-lang/scala-library/2.7.4/scala-library-2.7.4'
require '/Users/lgm/work/src/projex/bobj/quant/quant/target/quant-1.0-SNAPSHOT'
require '/Users/lgm/work/src/projex/bobj/magritte/pipe-cutter.current/pipes_to_cal/vendor/lib/antlr/antlr/local/antlr-local'
require '/Users/lgm/work/src/projex/bobj/magritte/pipe-cutter.current/pipes_to_cal/vendor/lib/asm/asm-all-3.0/local/asm-all-3.0-local'
require '/Users/lgm/work/src/projex/bobj/magritte/pipe-cutter.current/pipes_to_cal/vendor/lib/OpenQuark/OpenQuark/mac-dbg-local/OpenQuark-mac-dbg-local'
require '/Users/lgm/work/src/projex/bobj/magritte/pipe-cutter.current/pipes_to_cal/vendor/lib/antlr/antlr/local/antlr-local'
require '/Users/lgm/work/src/projex/bobj/magritte/pipe-cutter.current/pipes_to_cal/vendor/lib/asm/asm-all-3.0/local/asm-all-3.0-local'
require '/Users/lgm/work/src/projex/bobj/magritte/pipe-cutter.current/pipes_to_cal/vendor/lib/icu4j/icu4j/local/icu4j-local'
require '/Users/lgm/work/src/projex/bobj/magritte/pipe-cutter.current/pipes_to_cal/vendor/lib/log4j/log4j/local/log4j-local'
require '/Users/lgm/work/src/projex/bobj/magritte/pipe-cutter.current/pipes_to_cal/vendor/lib/cup/java-cup-11a/local/java-cup-11a-local'
require '/Users/lgm/work/src/projex/bobj/magritte/pipe-cutter.current/pipes_to_cal/vendor/lib/cup/java-cup-11a-runtime/local/java-cup-11a-runtime-local'
require '/Users/lgm/work/src/projex/bobj/magritte/pipe-cutter.current/pipes_to_cal/vendor/lib/jlex/Jlex/local/JLex-local'
require '/Users/lgm/work/src/projex/bobj/magritte/pipe-cutter.current/pipes_to_cal/vendor/lib/jgrapht/jgrapht-jdk1.5/local/jgrapht-jdk1.5-local'
require '/Users/lgm/work/src/projex/bobj/magritte/pipe-cutter.current/pipes_to_cal/vendor/lib/jlinalg/Jama-1.0.2/local/Jama-1.0.2-local.jar'
require '/Users/lgm/work/src/projex/bobj/magritte/pipe-cutter.current/pipes_to_cal/vendor/lib/jws/webservices-api/local/webservices-api-local.jar'
require '/Users/lgm/work/src/projex/bobj/magritte/pipe-cutter.current/pipes_to_cal/vendor/lib/jws/webservices-rt/local/webservices-rt-local.jar'
require '/Users/lgm/work/src/projex/bobj/magritte/pipe-cutter.current/pipes_to_cal/vendor/lib/CALTrampoline/CALTrampoline/local/CALTrampoline-local.jar'
require '/Users/lgm/work/src/projex/bobj/magritte/pipe-cutter.current/pipes_to_cal/target/pipes_to_cal-1.0-SNAPSHOT'

$LOAD_PATH << "/Users/lgm/work/src/devtools/jruby-1.1.4/lib/ruby/gems/1.8/gems/json-jruby-1.1.2-universal-java/lib"
$LOAD_PATH << "/Users/lgm/work/src/devtools/jruby-1.1.4/lib/ruby/gems/1.8/gems/json-jruby-1.1.2-universal-java/lib/json"
$LOAD_PATH << "/Users/lgm/work/src/devtools/jruby-1.1.4/lib/ruby/gems/1.8/gems/json-jruby-1.1.2-universal-java/lib/json/add"
$LOAD_PATH << "/Users/lgm/work/src/devtools/jruby-1.1.4/lib/ruby/gems/1.8/gems/json-jruby-1.1.2-universal-java/lib/json/ext"
$LOAD_PATH << "/Users/lgm/work/src/devtools/jruby-1.1.4/lib/ruby/gems/1.8/gems/json-jruby-1.1.2-universal-java/lib/json/pure"

require 'json.rb'

# module Quant
#   include_package "com.biosimilarity.quant.mcbond"
# end

# module PipesToCAL
#   include_package "com.biosimilarity.magritte"
# end
import "com.biosimilarity.magritte.PipesToCALBridge"
import "com.biosimilarity.magritte.ThePipesToCALBridge"
import "com.biosimilarity.magritte.TheOtherPipesToCALBridge"

class PipesController < ApplicationController

  # todo: make preview the only thing that's posted?
  attr_reader :debugging
  attr_writer :debugging

  def index
    @categories = Category.find(:all, :order => :name)
  end

  def ajax_pipe_load
  end

  def ajax_toggle_debug
    @debugging = (not @debugging)
  end  

  def ajax_pipe_preview
    @debugging = true
    begin
      if (@debugging)
        # to grab the request
        reqOutputF = File.open("reqOutput.json","w+")
        reqOutputF << "\n JSON string\n"
        reqOutputF << params[:def]
        reqOutputF << "\n"

        reqOutputF << TheOtherPipesToCALBridge.saySomething
      end

      wrequest = JSON.parse( params[:def] )

      if (@debugging)
        reqOutputF << "\n parsed JSON string as Ruby Hash\n"
        reqOutputF << wrequest
      end

      #theFixedRequest = fix_request( params[:def] )      
      requestModules = wrequest["modules"]

      if (@debugging)
        reqOutputF << "\n modules\n"
        reqOutputF << requestModules.inspect
      end

      moduleTypes = requestModules.collect { | m | m["type"] }
      
      if (@debugging)
        reqOutputF << "\n module types\n"
        reqOutputF << moduleTypes.inspect
      end

      moduleRecords = moduleTypes.collect { | mtype |
        PipeModule.find( :all, :conditions => { :type => mtype } )
      }

      if (@debugging)
        reqOutputF << "\n matching database records\n"
        reqOutputF << moduleRecords.inspect
      end

      theFixedRequest = {
        "modules" => (wrequest["modules"]).collect { | m |
          pipeModules =
          PipeModule.find(
                          :all,
                          :conditions => { :type => m["type"] }
                          )          
          {
            "type" =>
            (if m["type"] == "output"
             then "output"
             else (pipeModules[0])[:description] end),
            "id" => m["id"],
            "conf" => m["conf"]
          }
        },
        "wires" => wrequest["wires"]
      }
      
      if (@debugging)
        reqOutputF << "\n the fixed request\n"
        reqOutputF << theFixedRequest.inspect
      end

      theFixedRequestAsJSON = theFixedRequest.to_json

      if (@debugging)
        reqOutputF << "\n the fixed request as JSON\n"      
        reqOutputF << theFixedRequestAsJSON
      end

      myReqRslt =
        TheOtherPipesToCALBridge.handleRequest( theFixedRequestAsJSON )

      if (@debugging)
        reqOutputF << TheOtherPipesToCALBridge.saySomethingElse
        reqOutputF << myReqRslt        
        reqOutputF << request.to_s()
        reqOutputF << "\n Raw post\n"
        reqOutputF << request.raw_post()
        reqOutputF << "\n Done\n"
      end

    rescue Exception => msg
      if (@debugging)
        reqOutputF << "\n Error calling Pipes to CAL bridge \n"
        reqOutputF << msg.to_s
      end

    ensure
      if (@debugging)
        reqOutputF.close
      end

      render(:json => "{\"ok\":1,\"errors\":{\"modules\":{},\"pipe\":null},\"preview\":{\"_OUTPUT\":{\"count\":\"1\",\"id\":\"_OUTPUT\",\"duration\":\"0\",\"start\":\"0\",\"module\":\"Output\",\"items\":#{myReqRslt.to_json}}},\"stats\":{\"pua\":{}}}")
    end
  end

  def ajax_module_featured
  end

# error message looks like:
# {"ERROR":true,"status":0,"message":"Invalid crumb"}
  def ajax_module_list
    @modules = PipeModule.find(:all,
                               :limit => 5001,
                               :include => [:in_terminals, :out_terminals, :categories])
    begin
      moduleListOutputF = File.open("moduleListOutput.json","w+")
      moduleListOutputF << "\n JSON string\n"
      moduleListOutputF << @modules.to_json
    ensure
      moduleListOutputF.close
    end
    render(:json => "{\"ok\":1,\"module\":#{@modules.to_json}}")
  end

  def ajax_module_help
    @module = PipeModule.find_by_type(params[:type])
    if @module.nil?
      # todo: do other things if not found?
      #
      render(:json => "{\"ok\":0}")
    else
      help = @module.help
      help ||= "<div class=\"module_help\">no help available</div>"

      render(:json => "{\"ok\":1,\"help\":#{help.to_json}}")
    end
  end

  def ajax_module_info
    # hack for "output" module
    #
    if ("output" == params[:type])
      # allow this to take an input of type "any"
      render(:json => '{"ok":1,"info":{"terminals":[{"input":"text","name":"_INPUT"}],"name":"Pipe Output","type":"output","description":"The pipe output needs to be fed to this module"}}')
      return
    end

    begin
      reqOutputF = File.open("infoReqOutput.json","w+")
      reqOutputF << "\n JSON string\n"
      reqOutputF << params[:type]
      reqOutputF << "\n"

      reqOutputF << TheOtherPipesToCALBridge.saySomething
      @module = PipeModule.find_by_type(params[:type])
      reqOutputF << TheOtherPipesToCALBridge.saySomethingElse

      reqOutputF << @module

      reqOutputF << "\n Done\n"

      reqOutputF << request.to_s()
      reqOutputF << "\n Raw post\n"
      reqOutputF << request.raw_post()

    rescue Exception => msg
      reqOutputF << "\n Error finding pipe module by type \n"
      reqOutputF << msg.to_s

    ensure
      reqOutputF.close

      if @module.nil?
      # todo: do other things if not found?
      #
      render(:json => "{\"ok\":0}")
      else
        render(:json => "{\"ok\":1,\"info\":#{@module.to_json}}")
      end
    end

  end
end
