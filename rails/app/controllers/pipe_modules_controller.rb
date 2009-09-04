class PipeModulesController < ApplicationController
  # GET /pipe_modules
  # GET /pipe_modules.xml
  def index
    @pipe_modules = PipeModule.find(:all)

    respond_to do |format|
      format.html # index.html.erb
      format.xml  { render :xml => @pipe_modules }
    end
  end

  # GET /pipe_modules/1
  # GET /pipe_modules/1.xml
  def show
    @pipe_module = PipeModule.find(params[:id])

    respond_to do |format|
      format.html # show.html.erb
      format.xml  { render :xml => @pipe_module }
    end
  end

  # GET /pipe_modules/new
  # GET /pipe_modules/new.xml
  def new
    @pipe_module = PipeModule.new

    respond_to do |format|
      format.html # new.html.erb
      format.xml  { render :xml => @pipe_module }
    end
  end

  # GET /pipe_modules/1/edit
  def edit
    @pipe_module = PipeModule.find(params[:id])
  end

  # POST /pipe_modules
  # POST /pipe_modules.xml
  def create
    @pipe_module = PipeModule.new(params[:pipe_module])

    respond_to do |format|
      if @pipe_module.save
        flash[:notice] = 'PipeModule was successfully created.'
        format.html { redirect_to(@pipe_module) }
        format.xml  { render :xml => @pipe_module, :status => :created, :location => @pipe_module }
      else
        format.html { render :action => "new" }
        format.xml  { render :xml => @pipe_module.errors, :status => :unprocessable_entity }
      end
    end
  end

  # PUT /pipe_modules/1
  # PUT /pipe_modules/1.xml
  def update
    @pipe_module = PipeModule.find(params[:id])

    respond_to do |format|
      if @pipe_module.update_attributes(params[:pipe_module])
        flash[:notice] = 'PipeModule was successfully updated.'
        format.html { redirect_to(@pipe_module) }
        format.xml  { head :ok }
      else
        format.html { render :action => "edit" }
        format.xml  { render :xml => @pipe_module.errors, :status => :unprocessable_entity }
      end
    end
  end

  # DELETE /pipe_modules/1
  # DELETE /pipe_modules/1.xml
  def destroy
    @pipe_module = PipeModule.find(params[:id])
    @pipe_module.destroy

    respond_to do |format|
      format.html { redirect_to(pipe_modules_url) }
      format.xml  { head :ok }
    end
  end
end
