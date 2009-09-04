class AddSourceTextToCategories < ActiveRecord::Migration
  def self.up
    add_column :categories, :source_text, :string, :null => true
  end

  def self.down
    remove_column :categories, :source_text
  end
end
