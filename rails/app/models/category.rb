class Category < ActiveRecord::Base
  has_many :module_categories
  has_many :pipe_modules, :through => :module_categories
end
  