class CreatePipeModules < ActiveRecord::Migration
  def self.up
    create_table :pipe_modules do |t|
      t.string :name
      t.string :type
      t.string :description
      t.text :ui
      t.text :help

      t.timestamps
    end

    add_index :pipe_modules, :type, :unique => true
  end

  def self.down
    drop_table :pipe_modules
  end
end
