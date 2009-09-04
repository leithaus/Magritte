class OutputTerminalsController < ApplicationController
  # GET /output_terminals
  # GET /output_terminals.xml
  def index
    @output_terminals = OutputTerminal.find(:all)

    respond_to do |format|
      format.html # index.html.erb
      format.xml  { render :xml => @output_terminals }
    end
  end

  # GET /output_terminals/1
  # GET /output_terminals/1.xml
  def show
    @output_terminal = OutputTerminal.find(params[:id])

    respond_to do |format|
      format.html # show.html.erb
      format.xml  { render :xml => @output_terminal }
    end
  end

  # GET /output_terminals/new
  # GET /output_terminals/new.xml
  def new
    @output_terminal = OutputTerminal.new

    respond_to do |format|
      format.html # new.html.erb
      format.xml  { render :xml => @output_terminal }
    end
  end

  # GET /output_terminals/1/edit
  def edit
    @output_terminal = OutputTerminal.find(params[:id])
  end

  # POST /output_terminals
  # POST /output_terminals.xml
  def create
    @output_terminal = OutputTerminal.new(params[:output_terminal])

    respond_to do |format|
      if @output_terminal.save
        flash[:notice] = 'OutputTerminal was successfully created.'
        format.html { redirect_to(@output_terminal) }
        format.xml  { render :xml => @output_terminal, :status => :created, :location => @output_terminal }
      else
        format.html { render :action => "new" }
        format.xml  { render :xml => @output_terminal.errors, :status => :unprocessable_entity }
      end
    end
  end

  # PUT /output_terminals/1
  # PUT /output_terminals/1.xml
  def update
    @output_terminal = OutputTerminal.find(params[:id])

    respond_to do |format|
      if @output_terminal.update_attributes(params[:output_terminal])
        flash[:notice] = 'OutputTerminal was successfully updated.'
        format.html { redirect_to(@output_terminal) }
        format.xml  { head :ok }
      else
        format.html { render :action => "edit" }
        format.xml  { render :xml => @output_terminal.errors, :status => :unprocessable_entity }
      end
    end
  end

  # DELETE /output_terminals/1
  # DELETE /output_terminals/1.xml
  def destroy
    @output_terminal = OutputTerminal.find(params[:id])
    @output_terminal.destroy

    respond_to do |format|
      format.html { redirect_to(output_terminals_url) }
      format.xml  { head :ok }
    end
  end
end
