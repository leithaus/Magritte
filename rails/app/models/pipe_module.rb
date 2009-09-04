class PipeModule < ActiveRecord::Base
  has_many :module_categories, :dependent => :destroy
  has_many :categories,        :through => :module_categories

  has_many :input_terminals,   :dependent => :destroy
  has_many :output_terminals,  :dependent => :destroy

  has_many :in_terminals,  :through => :input_terminals, :source => :terminal
  has_many :out_terminals,  :through => :output_terminals, :source => :terminal
  
  def terminals
    # return terminals in the format that pipes expects
    #
    # input and output terminals appear in the same list
    # input terminals appear as:
    #     name: term_name
    #     input: input_type
    #
    # output terminals appear as:
    #     name: term_name
    #     output: output_type
    #
    [
      in_terminals.collect do | in_terminal |
        { :name => in_terminal.name,
          :input => 'text' #in_terminal.type
        }
      end,

      out_terminals.collect do | out_terminal |
        { :name => out_terminal.name,
          :output => 'text' #out_terminal.type
        }
      end
    ].flatten
  end

  def tags
    categories.collect do | category |
      'system:' + category.name
    end
  end

  def to_json_with_pipes(options = {})
    if options.empty?
      options = {
        :only => [ :name, :description, :ui, :type ],
        :methods => [ :terminals, :tags ]
        }
    end
    self.to_json_without_pipes(options)
    #.gsub(/\u003E/, '>').gsub(/\u003C/, '<')

  end
  alias_method_chain :to_json, :pipes

  def ui
    if(39 == categories.first.id)
      "<b>" + description + "</b><br/>" + help + "<div label='Value' repeat=\"false\" class=\"horizontal\"><input name=\"_INPUT\" type=\"string\" required=\"true\"\/>"
    else
      "<b>" + description + "</b><br/>" + help
    end
  end

  # by default, "type" is a reserved word in ActiveRecord
  # used for polymorphic assocations.
  #
  # Since we don't want to use this featuer in this case,
  # and just want an attribute called "type" to make Pipes
  # happy, we redifine the polymorphic column to be
  # something arbitrarially different.
  #
  def self.inheritance_column
    "not_type"
  end
end
