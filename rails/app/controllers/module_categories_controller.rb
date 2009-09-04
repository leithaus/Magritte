class ModuleCategoriesController < ApplicationController
  # GET /module_categories
  # GET /module_categories.xml
  def index
    @module_categories = ModuleCategory.find(:all)

    respond_to do |format|
      format.html # index.html.erb
      format.xml  { render :xml => @module_categories }
    end
  end

  # GET /module_categories/1
  # GET /module_categories/1.xml
  def show
    @module_category = ModuleCategory.find(params[:id])

    respond_to do |format|
      format.html # show.html.erb
      format.xml  { render :xml => @module_category }
    end
  end

  # GET /module_categories/new
  # GET /module_categories/new.xml
  def new
    @module_category = ModuleCategory.new

    respond_to do |format|
      format.html # new.html.erb
      format.xml  { render :xml => @module_category }
    end
  end

  # GET /module_categories/1/edit
  def edit
    @module_category = ModuleCategory.find(params[:id])
  end

  # POST /module_categories
  # POST /module_categories.xml
  def create
    @module_category = ModuleCategory.new(params[:module_category])

    respond_to do |format|
      if @module_category.save
        flash[:notice] = 'ModuleCategory was successfully created.'
        format.html { redirect_to(@module_category) }
        format.xml  { render :xml => @module_category, :status => :created, :location => @module_category }
      else
        format.html { render :action => "new" }
        format.xml  { render :xml => @module_category.errors, :status => :unprocessable_entity }
      end
    end
  end

  # PUT /module_categories/1
  # PUT /module_categories/1.xml
  def update
    @module_category = ModuleCategory.find(params[:id])

    respond_to do |format|
      if @module_category.update_attributes(params[:module_category])
        flash[:notice] = 'ModuleCategory was successfully updated.'
        format.html { redirect_to(@module_category) }
        format.xml  { head :ok }
      else
        format.html { render :action => "edit" }
        format.xml  { render :xml => @module_category.errors, :status => :unprocessable_entity }
      end
    end
  end

  # DELETE /module_categories/1
  # DELETE /module_categories/1.xml
  def destroy
    @module_category = ModuleCategory.find(params[:id])
    @module_category.destroy

    respond_to do |format|
      format.html { redirect_to(module_categories_url) }
      format.xml  { head :ok }
    end
  end
end
