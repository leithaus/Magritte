package json.Absyn; // Java Package generated by the BNF Converter.

public abstract class JSONObject implements java.io.Serializable {
  public abstract <R,A> R accept(JSONObject.Visitor<R,A> v, A arg);
  public interface Visitor <R,A> {
    public R visit(json.Absyn.JObject p, A arg);

  }

}
