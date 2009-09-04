class CreateOutputTerminals < ActiveRecord::Migration
  def self.up
    create_table :output_terminals do |t|
      t.integer :pipe_module_id
      t.integer :terminal_id

      t.timestamps
    end
  end

  def self.down
    drop_table :output_terminals
  end
end
