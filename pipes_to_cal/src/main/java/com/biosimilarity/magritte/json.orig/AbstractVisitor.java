package json;
import json.Absyn.*;
/** BNFC-Generated Abstract Visitor */
public class AbstractVisitor<R,A> implements AllVisitor<R,A> {
/* JSONObject */
    public R visit(json.Absyn.JObject p, A arg) { return visitDefault(p, arg); }
    public R visitDefault(json.Absyn.JSONObject p, A arg) {
      throw new IllegalArgumentException(this.getClass().getName() + ": " + p);
    }
/* JSONPair */
    public R visit(json.Absyn.JPair p, A arg) { return visitDefault(p, arg); }
    public R visitDefault(json.Absyn.JSONPair p, A arg) {
      throw new IllegalArgumentException(this.getClass().getName() + ": " + p);
    }
/* JSONArray */
    public R visit(json.Absyn.JArray p, A arg) { return visitDefault(p, arg); }
    public R visitDefault(json.Absyn.JSONArray p, A arg) {
      throw new IllegalArgumentException(this.getClass().getName() + ": " + p);
    }
/* JSONValue */
    public R visit(json.Absyn.JStr p, A arg) { return visitDefault(p, arg); }
    public R visit(json.Absyn.JNum p, A arg) { return visitDefault(p, arg); }
    public R visit(json.Absyn.JObj p, A arg) { return visitDefault(p, arg); }
    public R visit(json.Absyn.JArr p, A arg) { return visitDefault(p, arg); }
    public R visit(json.Absyn.JTru p, A arg) { return visitDefault(p, arg); }
    public R visit(json.Absyn.JFal p, A arg) { return visitDefault(p, arg); }
    public R visit(json.Absyn.JNul p, A arg) { return visitDefault(p, arg); }
    public R visitDefault(json.Absyn.JSONValue p, A arg) {
      throw new IllegalArgumentException(this.getClass().getName() + ": " + p);
    }

}
