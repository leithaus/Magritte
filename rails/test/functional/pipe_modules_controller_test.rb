require File.dirname(__FILE__) + '/../test_helper'

class PipeModulesControllerTest < ActionController::TestCase
  def test_should_get_index
    get :index
    assert_response :success
    assert_not_nil assigns(:pipe_modules)
  end

  def test_should_get_new
    get :new
    assert_response :success
  end

  def test_should_create_pipe_module
    assert_difference('PipeModule.count') do
      post :create, :pipe_module => { }
    end

    assert_redirected_to pipe_module_path(assigns(:pipe_module))
  end

  def test_should_show_pipe_module
    get :show, :id => pipe_modules(:one).id
    assert_response :success
  end

  def test_should_get_edit
    get :edit, :id => pipe_modules(:one).id
    assert_response :success
  end

  def test_should_update_pipe_module
    put :update, :id => pipe_modules(:one).id, :pipe_module => { }
    assert_redirected_to pipe_module_path(assigns(:pipe_module))
  end

  def test_should_destroy_pipe_module
    assert_difference('PipeModule.count', -1) do
      delete :destroy, :id => pipe_modules(:one).id
    end

    assert_redirected_to pipe_modules_path
  end
end
