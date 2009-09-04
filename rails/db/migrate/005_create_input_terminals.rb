class CreateInputTerminals < ActiveRecord::Migration
  def self.up
    create_table :input_terminals do |t|
      t.integer :pipe_module_id
      t.integer :terminal_id

      t.timestamps
    end
  end

  def self.down
    drop_table :input_terminals
  end
end
