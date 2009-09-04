class CreateModuleCategories < ActiveRecord::Migration
  def self.up
    create_table :module_categories do |t|
      t.integer :pipe_module_id
      t.integer :category_id

      t.timestamps
    end
  end

  def self.down
    drop_table :module_categories
  end
end
