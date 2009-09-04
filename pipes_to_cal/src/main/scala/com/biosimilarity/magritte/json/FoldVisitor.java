package com.biosimilarity.magritte.json;

import com.biosimilarity.magritte.json.Absyn.*;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

/** BNFC-Generated Fold Visitor */
public abstract class FoldVisitor<R,A> implements AllVisitor<R,A> {
    public abstract R leaf(A arg);
    public abstract R combine(R x, R y, A arg);

/* JSONObject */
    public R visit(com.biosimilarity.magritte.json.Absyn.JObject p, A arg) {
      R r = leaf(arg);
      for (JSONPair x : p.listjsonpair_) {
        r = combine(x.accept(this,arg), r, arg);
      }
      return r;
    }

/* JSONPair */
    public R visit(com.biosimilarity.magritte.json.Absyn.JPair p, A arg) {
      R r = leaf(arg);
      r = combine(p.jsonvalue_.accept(this, arg), r, arg);
      return r;
    }

/* JSONArray */
    public R visit(com.biosimilarity.magritte.json.Absyn.JArray p, A arg) {
      R r = leaf(arg);
      for (JSONValue x : p.listjsonvalue_) {
        r = combine(x.accept(this,arg), r, arg);
      }
      return r;
    }

/* JSONValue */
    public R visit(com.biosimilarity.magritte.json.Absyn.JStr p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(com.biosimilarity.magritte.json.Absyn.JNum p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(com.biosimilarity.magritte.json.Absyn.JObj p, A arg) {
      R r = leaf(arg);
      r = combine(p.jsonobject_.accept(this, arg), r, arg);
      return r;
    }
    public R visit(com.biosimilarity.magritte.json.Absyn.JArr p, A arg) {
      R r = leaf(arg);
      r = combine(p.jsonarray_.accept(this, arg), r, arg);
      return r;
    }
    public R visit(com.biosimilarity.magritte.json.Absyn.JTru p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(com.biosimilarity.magritte.json.Absyn.JFal p, A arg) {
      R r = leaf(arg);
      return r;
    }
    public R visit(com.biosimilarity.magritte.json.Absyn.JNul p, A arg) {
      R r = leaf(arg);
      return r;
    }


}
