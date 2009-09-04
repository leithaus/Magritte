package json;
import json.Absyn.*;
/** BNFC-Generated Composition Visitor
*/

public class ComposVisitor<A> implements
  json.Absyn.JSONObject.Visitor<json.Absyn.JSONObject,A>,
  json.Absyn.JSONPair.Visitor<json.Absyn.JSONPair,A>,
  json.Absyn.JSONArray.Visitor<json.Absyn.JSONArray,A>,
  json.Absyn.JSONValue.Visitor<json.Absyn.JSONValue,A>
{
/* JSONObject */
    public JSONObject visit(json.Absyn.JObject p, A arg)
    {
      ListJSONPair listjsonpair_ = new ListJSONPair();
      for (JSONPair x : p.listjsonpair_) {
        listjsonpair_.add(x.accept(this,arg));
      }

      return new json.Absyn.JObject(listjsonpair_);
    }

/* JSONPair */
    public JSONPair visit(json.Absyn.JPair p, A arg)
    {
      String string_ = p.string_;
      JSONValue jsonvalue_ = p.jsonvalue_.accept(this, arg);

      return new json.Absyn.JPair(string_, jsonvalue_);
    }

/* JSONArray */
    public JSONArray visit(json.Absyn.JArray p, A arg)
    {
      ListJSONValue listjsonvalue_ = new ListJSONValue();
      for (JSONValue x : p.listjsonvalue_) {
        listjsonvalue_.add(x.accept(this,arg));
      }

      return new json.Absyn.JArray(listjsonvalue_);
    }

/* JSONValue */
    public JSONValue visit(json.Absyn.JStr p, A arg)
    {
      String string_ = p.string_;

      return new json.Absyn.JStr(string_);
    }
    public JSONValue visit(json.Absyn.JNum p, A arg)
    {
      Double double_ = p.double_;

      return new json.Absyn.JNum(double_);
    }
    public JSONValue visit(json.Absyn.JObj p, A arg)
    {
      JSONObject jsonobject_ = p.jsonobject_.accept(this, arg);

      return new json.Absyn.JObj(jsonobject_);
    }
    public JSONValue visit(json.Absyn.JArr p, A arg)
    {
      JSONArray jsonarray_ = p.jsonarray_.accept(this, arg);

      return new json.Absyn.JArr(jsonarray_);
    }
    public JSONValue visit(json.Absyn.JTru p, A arg)
    {

      return new json.Absyn.JTru();
    }
    public JSONValue visit(json.Absyn.JFal p, A arg)
    {

      return new json.Absyn.JFal();
    }
    public JSONValue visit(json.Absyn.JNul p, A arg)
    {

      return new json.Absyn.JNul();
    }

}