package json.Absyn; // Java Package generated by the BNF Converter.

public class JFal extends JSONValue {

  public JFal() { }

  public <R,A> R accept(json.Absyn.JSONValue.Visitor<R,A> v, A arg) { return v.visit(this, arg); }

  public boolean equals(Object o) {
    if (this == o) return true;
    if (o instanceof json.Absyn.JFal) {
      return true;
    }
    return false;
  }

  public int hashCode() {
    return 37;
  }


}
