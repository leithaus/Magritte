class SetDbDefaults < ActiveRecord::Migration
  def self.up
    db_name = ActiveRecord::Base::connection.current_database()
    execute "ALTER DATABASE #{db_name} CHARACTER SET utf8 COLLATE utf8_general_ci"
  end

  def self.down
  end
end
