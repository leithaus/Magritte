require File.dirname(__FILE__) + '/../test_helper'

class TerminalsControllerTest < ActionController::TestCase
  def test_should_get_index
    get :index
    assert_response :success
    assert_not_nil assigns(:terminals)
  end

  def test_should_get_new
    get :new
    assert_response :success
  end

  def test_should_create_terminal
    assert_difference('Terminal.count') do
      post :create, :terminal => { }
    end

    assert_redirected_to terminal_path(assigns(:terminal))
  end

  def test_should_show_terminal
    get :show, :id => terminals(:one).id
    assert_response :success
  end

  def test_should_get_edit
    get :edit, :id => terminals(:one).id
    assert_response :success
  end

  def test_should_update_terminal
    put :update, :id => terminals(:one).id, :terminal => { }
    assert_redirected_to terminal_path(assigns(:terminal))
  end

  def test_should_destroy_terminal
    assert_difference('Terminal.count', -1) do
      delete :destroy, :id => terminals(:one).id
    end

    assert_redirected_to terminals_path
  end
end
