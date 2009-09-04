class InputTerminal < ActiveRecord::Base
  belongs_to :pipe_module
  belongs_to :terminal
end
