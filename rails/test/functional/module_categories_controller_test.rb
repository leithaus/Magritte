require File.dirname(__FILE__) + '/../test_helper'

class ModuleCategoriesControllerTest < ActionController::TestCase
  def test_should_get_index
    get :index
    assert_response :success
    assert_not_nil assigns(:module_categories)
  end

  def test_should_get_new
    get :new
    assert_response :success
  end

  def test_should_create_module_category
    assert_difference('ModuleCategory.count') do
      post :create, :module_category => { }
    end

    assert_redirected_to module_category_path(assigns(:module_category))
  end

  def test_should_show_module_category
    get :show, :id => module_categories(:one).id
    assert_response :success
  end

  def test_should_get_edit
    get :edit, :id => module_categories(:one).id
    assert_response :success
  end

  def test_should_update_module_category
    put :update, :id => module_categories(:one).id, :module_category => { }
    assert_redirected_to module_category_path(assigns(:module_category))
  end

  def test_should_destroy_module_category
    assert_difference('ModuleCategory.count', -1) do
      delete :destroy, :id => module_categories(:one).id
    end

    assert_redirected_to module_categories_path
  end
end
