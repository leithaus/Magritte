class Terminal < ActiveRecord::Base

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
