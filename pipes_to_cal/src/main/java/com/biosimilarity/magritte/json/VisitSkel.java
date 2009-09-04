package com.biosimilarity.magritte.json;
import com.biosimilarity.magritte.json.Absyn.*;
/*** BNFC-Generated Visitor Design Pattern Skeleton. ***/
/* This implements the common visitor design pattern.
   Tests show it to be slightly less efficient than the
   instanceof method, but easier to use. 
   Replace the R and A parameters with the desired return
   and context types.*/

public class VisitSkel
{
  public class JSONObjectVisitor<R,A> implements JSONObject.Visitor<R,A>
  {
    public R visit(com.biosimilarity.magritte.json.Absyn.JObject p, A arg)
    {
      /* Code For JObject Goes Here */

      for (JSONPair x : p.listjsonpair_) {
      }

      return null;
    }

  }
  public class JSONPairVisitor<R,A> implements JSONPair.Visitor<R,A>
  {
    public R visit(com.biosimilarity.magritte.json.Absyn.JPair p, A arg)
    {
      /* Code For JPair Goes Here */

      //p.string_;
      p.jsonvalue_.accept(new JSONValueVisitor<R,A>(), arg);

      return null;
    }

  }
  public class JSONArrayVisitor<R,A> implements JSONArray.Visitor<R,A>
  {
    public R visit(com.biosimilarity.magritte.json.Absyn.JArray p, A arg)
    {
      /* Code For JArray Goes Here */

      for (JSONValue x : p.listjsonvalue_) {
      }

      return null;
    }

  }
  public class JSONValueVisitor<R,A> implements JSONValue.Visitor<R,A>
  {
    public R visit(com.biosimilarity.magritte.json.Absyn.JStr p, A arg)
    {
      /* Code For JStr Goes Here */

      //p.string_;

      return null;
    }
    public R visit(com.biosimilarity.magritte.json.Absyn.JNum p, A arg)
    {
      /* Code For JNum Goes Here */

      //p.double_;

      return null;
    }
    public R visit(com.biosimilarity.magritte.json.Absyn.JObj p, A arg)
    {
      /* Code For JObj Goes Here */

      p.jsonobject_.accept(new JSONObjectVisitor<R,A>(), arg);

      return null;
    }
    public R visit(com.biosimilarity.magritte.json.Absyn.JArr p, A arg)
    {
      /* Code For JArr Goes Here */

      p.jsonarray_.accept(new JSONArrayVisitor<R,A>(), arg);

      return null;
    }
    public R visit(com.biosimilarity.magritte.json.Absyn.JTru p, A arg)
    {
      /* Code For JTru Goes Here */


      return null;
    }
    public R visit(com.biosimilarity.magritte.json.Absyn.JFal p, A arg)
    {
      /* Code For JFal Goes Here */


      return null;
    }
    public R visit(com.biosimilarity.magritte.json.Absyn.JNul p, A arg)
    {
      /* Code For JNul Goes Here */


      return null;
    }

  }
}