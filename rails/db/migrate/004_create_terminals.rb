class CreateTerminals < ActiveRecord::Migration
  def self.up
    create_table :terminals do |t|
      t.string :name
      t.string :type

      t.timestamps
    end
    
#    add_index :terminals, [:name, :type], :unique => true
  end

  def self.down
    drop_table :terminals
  end
end
