class ModuleCategory < ActiveRecord::Base
  belongs_to :pipe_module
  belongs_to :category
end
