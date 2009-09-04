class AddModuleCategories < ActiveRecord::Migration
  def self.up
    create_table :categories do | table |
      table.column :name, :string, :default => '', :nil => false
      table.timestamps
    end
    add_index :categories, :name, :unique => true
  end

  def self.down
    drop_table :categories
  end
end
