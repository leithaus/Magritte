require File.dirname(__FILE__) + '/../test_helper'

class OutputTerminalsControllerTest < ActionController::TestCase
  def test_should_get_index
    get :index
    assert_response :success
    assert_not_nil assigns(:output_terminals)
  end

  def test_should_get_new
    get :new
    assert_response :success
  end

  def test_should_create_output_terminal
    assert_difference('OutputTerminal.count') do
      post :create, :output_terminal => { }
    end

    assert_redirected_to output_terminal_path(assigns(:output_terminal))
  end

  def test_should_show_output_terminal
    get :show, :id => output_terminals(:one).id
    assert_response :success
  end

  def test_should_get_edit
    get :edit, :id => output_terminals(:one).id
    assert_response :success
  end

  def test_should_update_output_terminal
    put :update, :id => output_terminals(:one).id, :output_terminal => { }
    assert_redirected_to output_terminal_path(assigns(:output_terminal))
  end

  def test_should_destroy_output_terminal
    assert_difference('OutputTerminal.count', -1) do
      delete :destroy, :id => output_terminals(:one).id
    end

    assert_redirected_to output_terminals_path
  end
end
