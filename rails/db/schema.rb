# This file is auto-generated from the current state of the database. Instead of editing this file, 
# please use the migrations feature of ActiveRecord to incrementally modify your database, and
# then regenerate this schema definition.
#
# Note that this schema.rb definition is the authoritative source for your database schema. If you need
# to create the application database on another system, you should be using db:schema:load, not running
# all the migrations from scratch. The latter is a flawed and unsustainable approach (the more migrations
# you'll amass, the slower it'll run and the greater likelihood for issues).
#
# It's strongly recommended to check this file into your version control system.

ActiveRecord::Schema.define(:version => 8) do

  create_table "categories", :force => true do |t|
    t.string   "name",        :default => ""
    t.datetime "created_at"
    t.datetime "updated_at"
    t.string   "source_text"
  end

  add_index "categories", ["name"], :name => "index_categories_on_name", :unique => true

  create_table "input_terminals", :force => true do |t|
    t.integer  "pipe_module_id"
    t.integer  "terminal_id"
    t.datetime "created_at"
    t.datetime "updated_at"
  end

  create_table "module_categories", :force => true do |t|
    t.integer  "pipe_module_id"
    t.integer  "category_id"
    t.datetime "created_at"
    t.datetime "updated_at"
  end

  create_table "output_terminals", :force => true do |t|
    t.integer  "pipe_module_id"
    t.integer  "terminal_id"
    t.datetime "created_at"
    t.datetime "updated_at"
  end

  create_table "pipe_modules", :force => true do |t|
    t.string   "name"
    t.string   "type"
    t.string   "description"
    t.text     "ui"
    t.text     "help"
    t.datetime "created_at"
    t.datetime "updated_at"
  end

  add_index "pipe_modules", ["type"], :name => "index_pipe_modules_on_type", :unique => true

  create_table "terminals", :force => true do |t|
    t.string   "name"
    t.string   "type"
    t.datetime "created_at"
    t.datetime "updated_at"
  end

end
