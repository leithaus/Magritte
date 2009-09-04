ActionController::Routing::Routes.draw do |map|
  map.resources :module_categories

  map.resources :output_terminals

  map.resources :input_terminals

  map.resources :terminals

  map.resources :pipe_modules

  map.resources :categories

  # The priority is based upon order of creation: first created -> highest priority.

  # Sample of regular route:
  #   map.connect 'products/:id', :controller => 'catalog', :action => 'view'
  # Keep in mind you can assign values other than :controller and :action

  # Sample of named route:
  #   map.purchase 'products/:id/purchase', :controller => 'catalog', :action => 'purchase'
  # This route can be invoked with purchase_url(:id => product.id)

  # Sample resource route (maps HTTP verbs to controller actions automatically):
  #   map.resources :products

  # Sample resource route with options:
  #   map.resources :products, :member => { :short => :get, :toggle => :post }, :collection => { :sold => :get }

  # Sample resource route with sub-resources:
  #   map.resources :products, :has_many => [ :comments, :sales ], :has_one => :seller

  # Sample resource route within a namespace:
  #   map.namespace :admin do |admin|
  #     # Directs /admin/products/* to Admin::ProductsController (app/controllers/admin/products_controller.rb)
  #     admin.resources :products
  #   end

  # You can have the root of your site routed with map.root -- just remember to delete public/index.html.
  map.root :controller => 'pipes'

  map.connect '/ajax.module.featured',  :controller => 'pipes', :action => 'ajax_module_featured'
  map.connect '/ajax.module.help',      :controller => 'pipes', :action => 'ajax_module_help'
  map.connect '/ajax.module.info',      :controller => 'pipes', :action => 'ajax_module_info'
  map.connect '/ajax.module.list',      :controller => 'pipes', :action => 'ajax_module_list'
  map.connect '/ajax.pipe.load',        :controller => 'pipes', :action => 'ajax_pipe_load'
  map.connect '/ajax.pipe.preview',     :controller => 'pipes', :action => 'ajax_pipe_preview'

  # See how all your routes lay out with "rake routes"

  # Install the default routes as the lowest priority.
  map.connect ':controller/:action/:id'
  map.connect ':controller/:action/:id.:format'
end
