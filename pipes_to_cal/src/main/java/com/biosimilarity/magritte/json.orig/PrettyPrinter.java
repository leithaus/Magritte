package json;
import json.Absyn.*;

public class PrettyPrinter
{
  //For certain applications increasing the initial size of the buffer may improve performance.
  private static final int INITIAL_BUFFER_SIZE = 128;
  //You may wish to change the parentheses used in precedence.
  private static final String _L_PAREN = new String("(");
  private static final String _R_PAREN = new String(")");
  //You may wish to change render
  private static void render(String s)
  {
    if (s.equals("{"))
    {
       buf_.append("\n");
       indent();
       buf_.append(s);
       _n_ = _n_ + 2;
       buf_.append("\n");
       indent();
    }
    else if (s.equals("(") || s.equals("["))
       buf_.append(s);
    else if (s.equals(")") || s.equals("]"))
    {
       backup();
       buf_.append(s);
       buf_.append(" ");
    }
    else if (s.equals("}"))
    {
       _n_ = _n_ - 2;
       backup();
       backup();
       buf_.append(s);
       buf_.append("\n");
       indent();
    }
    else if (s.equals(","))
    {
       backup();
       buf_.append(s);
       buf_.append(" ");
    }
    else if (s.equals(";"))
    {
       backup();
       buf_.append(s);
       buf_.append("\n");
       indent();
    }
    else if (s.equals("")) return;
    else
    {
       buf_.append(s);
       buf_.append(" ");
    }
  }


  //  print and show methods are defined for each category.
  public static String print(json.Absyn.JSONObject foo)
  {
    pp(foo, 0);
    trim();
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String show(json.Absyn.JSONObject foo)
  {
    sh(foo);
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String print(json.Absyn.JSONPair foo)
  {
    pp(foo, 0);
    trim();
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String show(json.Absyn.JSONPair foo)
  {
    sh(foo);
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String print(json.Absyn.JSONArray foo)
  {
    pp(foo, 0);
    trim();
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String show(json.Absyn.JSONArray foo)
  {
    sh(foo);
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String print(json.Absyn.JSONValue foo)
  {
    pp(foo, 0);
    trim();
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String show(json.Absyn.JSONValue foo)
  {
    sh(foo);
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String print(json.Absyn.ListJSONPair foo)
  {
    pp(foo, 0);
    trim();
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String show(json.Absyn.ListJSONPair foo)
  {
    sh(foo);
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String print(json.Absyn.ListJSONValue foo)
  {
    pp(foo, 0);
    trim();
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  public static String show(json.Absyn.ListJSONValue foo)
  {
    sh(foo);
    String temp = buf_.toString();
    buf_.delete(0,buf_.length());
    return temp;
  }
  /***   You shouldn't need to change anything beyond this point.   ***/

  private static void pp(json.Absyn.JSONObject foo, int _i_)
  {
    if (foo instanceof json.Absyn.JObject)
    {
       json.Absyn.JObject _jobject = (json.Absyn.JObject) foo;
       if (_i_ > 0) render(_L_PAREN);
       render("{");
       pp(_jobject.listjsonpair_, 0);
       render("}");
       if (_i_ > 0) render(_R_PAREN);
    }
  }

  private static void pp(json.Absyn.JSONPair foo, int _i_)
  {
    if (foo instanceof json.Absyn.JPair)
    {
       json.Absyn.JPair _jpair = (json.Absyn.JPair) foo;
       if (_i_ > 0) render(_L_PAREN);
       printQuoted(_jpair.string_);
       render(":");
       pp(_jpair.jsonvalue_, 0);
       if (_i_ > 0) render(_R_PAREN);
    }
  }

  private static void pp(json.Absyn.JSONArray foo, int _i_)
  {
    if (foo instanceof json.Absyn.JArray)
    {
       json.Absyn.JArray _jarray = (json.Absyn.JArray) foo;
       if (_i_ > 0) render(_L_PAREN);
       render("[");
       pp(_jarray.listjsonvalue_, 0);
       render("]");
       if (_i_ > 0) render(_R_PAREN);
    }
  }

  private static void pp(json.Absyn.JSONValue foo, int _i_)
  {
    if (foo instanceof json.Absyn.JStr)
    {
       json.Absyn.JStr _jstr = (json.Absyn.JStr) foo;
       if (_i_ > 0) render(_L_PAREN);
       printQuoted(_jstr.string_);
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof json.Absyn.JNum)
    {
       json.Absyn.JNum _jnum = (json.Absyn.JNum) foo;
       if (_i_ > 0) render(_L_PAREN);
       pp(_jnum.double_, 0);
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof json.Absyn.JObj)
    {
       json.Absyn.JObj _jobj = (json.Absyn.JObj) foo;
       if (_i_ > 0) render(_L_PAREN);
       pp(_jobj.jsonobject_, 0);
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof json.Absyn.JArr)
    {
       json.Absyn.JArr _jarr = (json.Absyn.JArr) foo;
       if (_i_ > 0) render(_L_PAREN);
       pp(_jarr.jsonarray_, 0);
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof json.Absyn.JTru)
    {
       json.Absyn.JTru _jtru = (json.Absyn.JTru) foo;
       if (_i_ > 0) render(_L_PAREN);
       render("true");
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof json.Absyn.JFal)
    {
       json.Absyn.JFal _jfal = (json.Absyn.JFal) foo;
       if (_i_ > 0) render(_L_PAREN);
       render("false");
       if (_i_ > 0) render(_R_PAREN);
    }
    else     if (foo instanceof json.Absyn.JNul)
    {
       json.Absyn.JNul _jnul = (json.Absyn.JNul) foo;
       if (_i_ > 0) render(_L_PAREN);
       render("null");
       if (_i_ > 0) render(_R_PAREN);
    }
  }

  private static void pp(json.Absyn.ListJSONPair foo, int _i_)
  {
     for (java.util.Iterator<JSONPair> it = foo.iterator(); it.hasNext();)
     {
       pp(it.next(), 0);
       if (it.hasNext()) {
         render(",");
       } else {
         render("");
       }
     }
  }

  private static void pp(json.Absyn.ListJSONValue foo, int _i_)
  {
     for (java.util.Iterator<JSONValue> it = foo.iterator(); it.hasNext();)
     {
       pp(it.next(), 0);
       if (it.hasNext()) {
         render(",");
       } else {
         render("");
       }
     }
  }


  private static void sh(json.Absyn.JSONObject foo)
  {
    if (foo instanceof json.Absyn.JObject)
    {
       json.Absyn.JObject _jobject = (json.Absyn.JObject) foo;
       render("(");
       render("JObject");
       render("[");
       sh(_jobject.listjsonpair_);
       render("]");
       render(")");
    }
  }

  private static void sh(json.Absyn.JSONPair foo)
  {
    if (foo instanceof json.Absyn.JPair)
    {
       json.Absyn.JPair _jpair = (json.Absyn.JPair) foo;
       render("(");
       render("JPair");
       sh(_jpair.string_);
       sh(_jpair.jsonvalue_);
       render(")");
    }
  }

  private static void sh(json.Absyn.JSONArray foo)
  {
    if (foo instanceof json.Absyn.JArray)
    {
       json.Absyn.JArray _jarray = (json.Absyn.JArray) foo;
       render("(");
       render("JArray");
       render("[");
       sh(_jarray.listjsonvalue_);
       render("]");
       render(")");
    }
  }

  private static void sh(json.Absyn.JSONValue foo)
  {
    if (foo instanceof json.Absyn.JStr)
    {
       json.Absyn.JStr _jstr = (json.Absyn.JStr) foo;
       render("(");
       render("JStr");
       sh(_jstr.string_);
       render(")");
    }
    if (foo instanceof json.Absyn.JNum)
    {
       json.Absyn.JNum _jnum = (json.Absyn.JNum) foo;
       render("(");
       render("JNum");
       sh(_jnum.double_);
       render(")");
    }
    if (foo instanceof json.Absyn.JObj)
    {
       json.Absyn.JObj _jobj = (json.Absyn.JObj) foo;
       render("(");
       render("JObj");
       sh(_jobj.jsonobject_);
       render(")");
    }
    if (foo instanceof json.Absyn.JArr)
    {
       json.Absyn.JArr _jarr = (json.Absyn.JArr) foo;
       render("(");
       render("JArr");
       sh(_jarr.jsonarray_);
       render(")");
    }
    if (foo instanceof json.Absyn.JTru)
    {
       json.Absyn.JTru _jtru = (json.Absyn.JTru) foo;
       render("JTru");
    }
    if (foo instanceof json.Absyn.JFal)
    {
       json.Absyn.JFal _jfal = (json.Absyn.JFal) foo;
       render("JFal");
    }
    if (foo instanceof json.Absyn.JNul)
    {
       json.Absyn.JNul _jnul = (json.Absyn.JNul) foo;
       render("JNul");
    }
  }

  private static void sh(json.Absyn.ListJSONPair foo)
  {
     for (java.util.Iterator<JSONPair> it = foo.iterator(); it.hasNext();)
     {
       sh(it.next());
       if (it.hasNext())
         render(",");
     }
  }

  private static void sh(json.Absyn.ListJSONValue foo)
  {
     for (java.util.Iterator<JSONValue> it = foo.iterator(); it.hasNext();)
     {
       sh(it.next());
       if (it.hasNext())
         render(",");
     }
  }


  private static void pp(Integer n, int _i_) { buf_.append(n); buf_.append(" "); }
  private static void pp(Double d, int _i_) { buf_.append(d); buf_.append(" "); }
  private static void pp(String s, int _i_) { buf_.append(s); buf_.append(" "); }
  private static void pp(Character c, int _i_) { buf_.append("'" + c.toString() + "'"); buf_.append(" "); }
  private static void sh(Integer n) { render(n.toString()); }
  private static void sh(Double d) { render(d.toString()); }
  private static void sh(Character c) { render(c.toString()); }
  private static void sh(String s) { printQuoted(s); }
  private static void printQuoted(String s) { render("\"" + s + "\""); }
  private static void indent()
  {
    int n = _n_;
    while (n > 0)
    {
      buf_.append(" ");
      n--;
    }
  }
  private static void backup()
  {
     if (buf_.charAt(buf_.length() - 1) == ' ') {
      buf_.setLength(buf_.length() - 1);
    }
  }
  private static void trim()
  {
     while (buf_.length() > 0 && buf_.charAt(0) == ' ')
        buf_.deleteCharAt(0); 
    while (buf_.length() > 0 && buf_.charAt(buf_.length()-1) == ' ')
        buf_.deleteCharAt(buf_.length()-1);
  }
  private static int _n_ = 0;
  private static StringBuilder buf_ = new StringBuilder(INITIAL_BUFFER_SIZE);
}

