require File.dirname(__FILE__) + '/../test_helper'

class InputTerminalsControllerTest < ActionController::TestCase
  def test_should_get_index
    get :index
    assert_response :success
    assert_not_nil assigns(:input_terminals)
  end

  def test_should_get_new
    get :new
    assert_response :success
  end

  def test_should_create_input_terminal
    assert_difference('InputTerminal.count') do
      post :create, :input_terminal => { }
    end

    assert_redirected_to input_terminal_path(assigns(:input_terminal))
  end

  def test_should_show_input_terminal
    get :show, :id => input_terminals(:one).id
    assert_response :success
  end

  def test_should_get_edit
    get :edit, :id => input_terminals(:one).id
    assert_response :success
  end

  def test_should_update_input_terminal
    put :update, :id => input_terminals(:one).id, :input_terminal => { }
    assert_redirected_to input_terminal_path(assigns(:input_terminal))
  end

  def test_should_destroy_input_terminal
    assert_difference('InputTerminal.count', -1) do
      delete :destroy, :id => input_terminals(:one).id
    end

    assert_redirected_to input_terminals_path
  end
end
