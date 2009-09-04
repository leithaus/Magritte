class InputTerminalsController < ApplicationController
  # GET /input_terminals
  # GET /input_terminals.xml
  def index
    @input_terminals = InputTerminal.find(:all)

    respond_to do |format|
      format.html # index.html.erb
      format.xml  { render :xml => @input_terminals }
    end
  end

  # GET /input_terminals/1
  # GET /input_terminals/1.xml
  def show
    @input_terminal = InputTerminal.find(params[:id])

    respond_to do |format|
      format.html # show.html.erb
      format.xml  { render :xml => @input_terminal }
    end
  end

  # GET /input_terminals/new
  # GET /input_terminals/new.xml
  def new
    @input_terminal = InputTerminal.new

    respond_to do |format|
      format.html # new.html.erb
      format.xml  { render :xml => @input_terminal }
    end
  end

  # GET /input_terminals/1/edit
  def edit
    @input_terminal = InputTerminal.find(params[:id])
  end

  # POST /input_terminals
  # POST /input_terminals.xml
  def create
    @input_terminal = InputTerminal.new(params[:input_terminal])

    respond_to do |format|
      if @input_terminal.save
        flash[:notice] = 'InputTerminal was successfully created.'
        format.html { redirect_to(@input_terminal) }
        format.xml  { render :xml => @input_terminal, :status => :created, :location => @input_terminal }
      else
        format.html { render :action => "new" }
        format.xml  { render :xml => @input_terminal.errors, :status => :unprocessable_entity }
      end
    end
  end

  # PUT /input_terminals/1
  # PUT /input_terminals/1.xml
  def update
    @input_terminal = InputTerminal.find(params[:id])

    respond_to do |format|
      if @input_terminal.update_attributes(params[:input_terminal])
        flash[:notice] = 'InputTerminal was successfully updated.'
        format.html { redirect_to(@input_terminal) }
        format.xml  { head :ok }
      else
        format.html { render :action => "edit" }
        format.xml  { render :xml => @input_terminal.errors, :status => :unprocessable_entity }
      end
    end
  end

  # DELETE /input_terminals/1
  # DELETE /input_terminals/1.xml
  def destroy
    @input_terminal = InputTerminal.find(params[:id])
    @input_terminal.destroy

    respond_to do |format|
      format.html { redirect_to(input_terminals_url) }
      format.xml  { head :ok }
    end
  end
end
