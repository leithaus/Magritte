/*
 * Copyright (c) 2007 BUSINESS OBJECTS SOFTWARE LIMITED
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 *     * Redistributions of source code must retain the above copyright notice,
 *       this list of conditions and the following disclaimer.
 *  
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *  
 *     * Neither the name of Business Objects nor the names of its contributors
 *       may be used to endorse or promote products derived from this software
 *       without specific prior written permission.
 *  
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */


/*
 * NavAddress.java
 * Creation date: Aug 5, 2003
 * By: Frank Worsley
 */
package org.openquark.gems.client.navigator;

import java.util.HashMap;
import java.util.Map;

import org.openquark.cal.compiler.ClassInstance;
import org.openquark.cal.compiler.ClassInstanceIdentifier;
import org.openquark.cal.compiler.ModuleName;
import org.openquark.cal.compiler.ModuleTypeInfo;
import org.openquark.cal.compiler.QualifiedName;
import org.openquark.cal.compiler.ScopedEntity;
import org.openquark.cal.services.CALFeatureName;
import org.openquark.cal.services.GemEntity;
import org.openquark.cal.services.MetaModule;
import org.openquark.cal.services.FeatureName.FeatureType;
import org.openquark.gems.client.CollectorGem;


/**
 * This class implements the addresses that are used within the CAL navigator to uniquely
 * identify any location within the CAL workspace that the navigator can display. Not every
 * location corresponds to a CAL language entity, addresses also encode 'virtual' locations
 * such as the search results page or a module vault.
 * <br><br>
 * This class also provides helper methods to work with the metadata that may be associated
 * with a location. It allows a client to save/load metadata for a location and correctly
 * determine the display name for a location using the metadata if applicable.
 * <br><br>
 * Note that this class is immutable.
 * <br><br>
 * A navigator address is similiar to a URL. It can be expressed in string form and consists
 * of a method, base, parameters and an anchor. The method identifies the type of entity the
 * address is for. The base is the unique name that can be used to resolve the entity.
 * The parameters are a set of name-value pairs for any additional information that is attached
 * to an address. The anchor part is used to specify a location on the HTML page that is
 * associated with the address. HTML pages for an address are generated by the NavHtmlFactory.
 * <br><br>
 * The string form of a url looks as follows:
 * <br><br>
 *     function://List.map&argument=1#relatedFeatures
 * <br><br>
 * Method:     function<br>
 * Base:       List.map<br>
 * Parameters: argument=1<br>
 * Anchor:     relatedFeatures<br>
 * <br><br>
 * This url identifies the function List.map in the Perspective. There is one parameter with
 * the name 'argument' and value '1'. The anchor for the address is "relatedFeatures".
 *
 * @author Frank Worsley
 */
public final class NavAddress {

    /**
     * The class for the address method enum pattern.
     * @author Frank Worsley
     */
    public static final class NavAddressMethod {
        
        private final String method;
        
        private NavAddressMethod(String method) {
            
            if (method == null) {
                throw new NullPointerException();
            }
            
            this.method = method;
        }
        
        @Override
        public String toString() {
            return method;
        }
    }
    
    /* Methods for addresses. */

    /** The method of addresses that point to functions. */
    public static final NavAddressMethod FUNCTION_METHOD = new NavAddressMethod("function");
        
    /** The method of addresses that point to method constructors. */
    public static final NavAddressMethod TYPE_CONSTRUCTOR_METHOD = new NavAddressMethod("typeConstructor");
        
    /** The method of addresses that point to data constructors. */
    public static final NavAddressMethod DATA_CONSTRUCTOR_METHOD = new NavAddressMethod("dataConstructor");
        
    /** The method of addresses that point to class methods. */
    public static final NavAddressMethod CLASS_METHOD_METHOD = new NavAddressMethod("classMethod");
        
    /** The method of addresses that point to method classes. */
    public static final NavAddressMethod TYPE_CLASS_METHOD = new NavAddressMethod("typeClass");
        
    /** The method of addresses that point to class instances. */
    public static final NavAddressMethod CLASS_INSTANCE_METHOD = new NavAddressMethod("classInstance");
        
    /** The method of addresses that point to instance methods. */
    public static final NavAddressMethod INSTANCE_METHOD_METHOD = new NavAddressMethod("instanceMethod");
    
    /** The method of addresses that point to modules. */
    public static final NavAddressMethod MODULE_METHOD = new NavAddressMethod("module");

    /** The method of addresses that point to module namespaces. */
    public static final NavAddressMethod MODULE_NAMESPACE_METHOD = new NavAddressMethod("moduleNamespace");

    /** The method of addresses that point to the root of the workspace. */
    public static final NavAddressMethod WORKSPACE_METHOD = new NavAddressMethod("workspace");

    /** The method of addresses that point to search results. */
    public static final NavAddressMethod SEARCH_METHOD = new NavAddressMethod("search");
    
    /** The method of addresses that point to collectors. */
    public static final NavAddressMethod COLLECTOR_METHOD = new NavAddressMethod("collector");
    
    
    /* Parameter names. */
    
    /** The parameter that identifies what vault to display for modules. */
    public static final String VAULT_PARAMETER = "vault";
    
    /** The parameter that identifies the argument number for gem entities. */
    public static final String ARGUMENT_PARAMETER = "argument";
    
    /** The parameter that identifies the type class for class instances. */
    public static final String INSTANCE_CLASS_PARAMETER = "instanceTypeClass";
    
    /** The parameter that identifies the type constructor of class instances. */
    public static final String INSTANCE_TYPE_PARAMETER = "instanceTypeConstructor";
    
    /** The parameter that identifies the module a class instance is declared in. */
    public static final String INSTANCE_MODULE_PARAMETER = "instanceModule";

    /** The parameter that identifies the method name of instance methods. */
    public static final String INSTANCE_METHOD_PARAMETER = "instanceMethod";
    

    /* Parameter values. */
    
    /** The parameter value for the type vault. */
    public static final String TYPE_VAULT_VALUE = "TypeVault";

    /** The parameter value for the class vault. */
    public static final String CLASS_VAULT_VALUE = "ClassVault";

    /** The parameter value for the instance vault. */
    public static final String INSTANCE_VAULT_VALUE = "InstanceVault";

    /** The parameter value for the function vault. */
    public static final String FUNCTION_VAULT_VALUE = "FunctionVault";
    
    
    /* Special characters. */
    
    /** The special string used to separate the method from the base. */
    public static final String METHOD_SEPARATOR = "://";
    
    /** The special character used to separate the anchor from the base. */
    public static final String ANCHOR_SEPARATOR = "#";

    /** The special character used to separate parameters. */    
    public static final String PARAMETER_SEPARATOR = "&";

    /** The special charater used to separate values from their parameter name. */
    public static final String VALUE_SEPARATOR = "=";


    /* Instance Fields. */
    
    /** The method of the address. This field is never null. */
    private final NavAddressMethod method;
    
    /** The base of the address or null if no base. */
    private final String base;
    
    /** The anchor of the address or null if no anchor. */
    private final String anchor;
    
    /** The parameters included in the address. This field is never null. */
    private final Map<String, String> parameters;

    /**
     * Private constructor for a new address with the given information.
     * Use the public static factory methods to get an instance of an address.
     * 
     * @param method the method of the address
     * @param base the base of the address
     * @param anchor the anchor of the address
     * @param parameters the parameters for the address
     */
    private NavAddress(NavAddressMethod method, String base, String anchor, Map<String, String> parameters) {
        
        if (method == null) {
            throw new NullPointerException();
        }

        this.method = method;
        this.base = base;
        this.anchor = anchor;
        this.parameters = parameters != null ? parameters : new HashMap<String, String>();
    }
    
    /**
     * @param featureName the feature name to get an address for
     * @return an address for the given feature name
     */
    public static NavAddress getAddress(CALFeatureName featureName) {
        
        FeatureType type = featureName.getType();
        
        if (type == CALFeatureName.FUNCTION) {
            return new NavAddress(FUNCTION_METHOD, featureName.getName(), null, null);
            
        } else if (type == CALFeatureName.TYPE_CONSTRUCTOR) {
            return new NavAddress(TYPE_CONSTRUCTOR_METHOD, featureName.getName(), null, null); 
            
        } else if (type == CALFeatureName.TYPE_CLASS) {
            return new NavAddress(TYPE_CLASS_METHOD, featureName.getName(), null, null);
            
        } else if (type == CALFeatureName.DATA_CONSTRUCTOR) {
            return new NavAddress(DATA_CONSTRUCTOR_METHOD, featureName.getName(), null, null);
            
        } else if (type == CALFeatureName.CLASS_METHOD) {
            return new NavAddress(CLASS_METHOD_METHOD, featureName.getName(), null, null);
            
        } else if (type == CALFeatureName.MODULE) {
            return new NavAddress(MODULE_METHOD, featureName.getName(), null, null);
        
        } else if (type == CALFeatureName.CLASS_INSTANCE) {
            
            ClassInstanceIdentifier identifier = featureName.toInstanceIdentifier();
            ModuleName moduleName = featureName.toModuleName();
            
            Map<String, String> parameters = new HashMap<String, String>();
            parameters.put(INSTANCE_CLASS_PARAMETER, identifier.getTypeClassName().getQualifiedName());
            parameters.put(INSTANCE_TYPE_PARAMETER, identifier.getTypeIdentifier());
            parameters.put(INSTANCE_MODULE_PARAMETER, moduleName.toSourceText());
            
            return new NavAddress(CLASS_INSTANCE_METHOD, null, null, parameters);
            
        } else if (type == CALFeatureName.INSTANCE_METHOD) {
            
            ClassInstanceIdentifier identifier = featureName.toInstanceIdentifier();
            ModuleName moduleName = featureName.toModuleName();
            String methodName = featureName.toInstanceMethodName();
            
            Map<String, String> parameters = new HashMap<String, String>();
            parameters.put(INSTANCE_CLASS_PARAMETER, identifier.getTypeClassName().getQualifiedName());
            parameters.put(INSTANCE_TYPE_PARAMETER, identifier.getTypeIdentifier());
            parameters.put(INSTANCE_MODULE_PARAMETER, moduleName.toSourceText());
            parameters.put(INSTANCE_METHOD_PARAMETER, methodName);
            
            return new NavAddress(INSTANCE_METHOD_METHOD, null, null, parameters);
        }
        
        throw new IllegalArgumentException("feature type not supported: " + type);
    }
    
    /**
     * @param module the module to get an address for
     * @return the address for the given module
     */
    public static NavAddress getAddress(MetaModule module) {
        return new NavAddress(MODULE_METHOD, module.getName().toSourceText(), null, null);
    }
    
    /**
     * @param moduleInfo the method info of the module to get an address for
     * @return the address for the module of the given method info
     */
    public static NavAddress getAddress(ModuleTypeInfo moduleInfo) {
        return new NavAddress(MODULE_METHOD, moduleInfo.getModuleName().toSourceText(), null, null);        
    }
    
    /**
     * @param moduleName the module name to get an address for
     * @return the address for the module of the given method info
     */
    public static NavAddress getModuleNamespaceAddress(final ModuleName moduleName) {
        return new NavAddress(MODULE_NAMESPACE_METHOD, moduleName.toSourceText(), null, null);        
    }
    
    /**
     * @param entity the entity to get an address for
     * @return the address for the given entity
     */
    public static NavAddress getAddress(GemEntity entity) {
        return getAddress(entity.getFunctionalAgent());
    }
    
    /**
     * @param entity the entity to get an address for
     * @return the address for the given entity
     */
    public static NavAddress getAddress(ScopedEntity entity) {
        return getAddress(CALFeatureName.getScopedEntityFeatureName(entity));
    }
    
    /**
     * @param classInstance the class instance to get an address for
     * @return the address for the given class instance
     */
    public static NavAddress getAddress(ClassInstance classInstance) {
        
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put(INSTANCE_CLASS_PARAMETER, classInstance.getTypeClass().getName().getQualifiedName());
        parameters.put(INSTANCE_TYPE_PARAMETER, classInstance.getIdentifier().getTypeIdentifier());
        parameters.put(INSTANCE_MODULE_PARAMETER, classInstance.getModuleName().toSourceText());
        
        return new NavAddress(CLASS_INSTANCE_METHOD, null, null, parameters);
    }
    
    /**
     * @param collector the collector to get an address for
     * @return the address for the given collector
     */
    public static NavAddress getAddress(CollectorGem collector) {
        return new NavAddress(COLLECTOR_METHOD, collector.getUnqualifiedName(), null, null);
    }
    
    /**
     * @param searchString the search string being searched for
     * @return the address for the given search result page
     */
    public static NavAddress getSearchAddress(String searchString) {
        return new NavAddress(SEARCH_METHOD, searchString, null, null);
    }
    
    /**
     * Parses the given string and returns a corresponding address object.
     * @param address the string to parse
     * @return the address object corresponding to the string address
     * @throws IllegalArgumentException if the string does not represent a valid address
     */
    public static NavAddress getAddress(String address) {
        
        int i = address.lastIndexOf(METHOD_SEPARATOR);
        int a = address.lastIndexOf(ANCHOR_SEPARATOR);
        
        String method = address.substring(0, i);
        String base = (a != -1) ? address.substring(i + 3, a) : address.substring(i + 3);
        String anchor = (a == -1) ? null : address.substring(a + 1);
        Map<String, String> parameters = null;

        if (base.trim().length() == 0) {
            base = null;
        }
        
        if (base != null && base.indexOf(PARAMETER_SEPARATOR) != -1) {
            
            parameters = new HashMap<String, String>();
            
            int p = -1;
            while ((p = base.indexOf(PARAMETER_SEPARATOR, p + 1)) != -1) {
                
                int c = base.indexOf(VALUE_SEPARATOR, p);
                int d = base.indexOf(PARAMETER_SEPARATOR, c);
                
                String name = base.substring(p + 1, c);
                String value = base.substring(c + 1, d != -1 ? d : base.length());
                parameters.put(name, value);
            }
            
            p = base.indexOf(PARAMETER_SEPARATOR);
            base = base.substring(0, p);
        }
        
        return new NavAddress(stringToMethod(method), base, anchor, parameters);
    }
    
    /**
     * Checks if we know about a method that matches that string and returns
     * that method, if any.
     * @param methodString the string to find a method for
     * @return the method for the given string
     * @throws IllegalArgumentException if the string doesn't match any method
     */
    private static NavAddressMethod stringToMethod(String methodString) {
        
        if (methodString.equals(FUNCTION_METHOD.toString())) {
            return FUNCTION_METHOD;
            
        } else if (methodString.equals(TYPE_CONSTRUCTOR_METHOD.toString())) {
            return TYPE_CONSTRUCTOR_METHOD;
            
        } else if (methodString.equals(DATA_CONSTRUCTOR_METHOD.toString())) {
            return DATA_CONSTRUCTOR_METHOD;

        } else if (methodString.equals(CLASS_METHOD_METHOD.toString())) {
            return CLASS_METHOD_METHOD;
            
        } else if (methodString.equals(TYPE_CLASS_METHOD.toString())) {
            return TYPE_CLASS_METHOD;
        
        } else if (methodString.equals(CLASS_INSTANCE_METHOD.toString())) {
            return CLASS_INSTANCE_METHOD;
            
        } else if (methodString.equals(INSTANCE_METHOD_METHOD.toString())) {
            return INSTANCE_METHOD_METHOD;
            
        } else if (methodString.equals(MODULE_METHOD.toString())) {
            return MODULE_METHOD;
            
        } else if (methodString.equals(MODULE_NAMESPACE_METHOD.toString())) {
            return MODULE_NAMESPACE_METHOD;
            
        } else if (methodString.equals(WORKSPACE_METHOD.toString())) {
            return WORKSPACE_METHOD;
                
        } else if (methodString.equals(SEARCH_METHOD.toString())) {
            return SEARCH_METHOD;
            
        } else if (methodString.equals(COLLECTOR_METHOD.toString())) {
            return COLLECTOR_METHOD;
        }
        
        throw new IllegalArgumentException("method not supported: " + methodString);
    }
    
    /**
     * @param method the method to get a root address for
     * @return the address object for the root with the given method
     */
    public static NavAddress getRootAddress(NavAddressMethod method) {
        return new NavAddress(method, null, null, null);
    }

    /**
     * @param method the method to convert to
     * @return converts this address to a new address object with the given method
     */
    public NavAddress withMethod(NavAddressMethod method) {
        return new NavAddress(method, base, anchor, parameters);
    }
    
    /**
     * @param anchor the anchor to convert to
     * @return convert this address to a new address object with the given anchor
     */
    public NavAddress withAnchor(String anchor) {
        return new NavAddress(method, base, anchor, parameters);
    }
    
    /**
     * @param name the name of the parameter to add
     * @param value the value of the parameter to add
     * @return a new address object with the given parameter added
     * @throws IllegalArgumentException if the parameter name or value are invalid
     */
    public NavAddress withParameter(String name, String value) {
        
        if (value.indexOf(PARAMETER_SEPARATOR) != -1 || name.indexOf(PARAMETER_SEPARATOR) != -1) {
            throw new IllegalArgumentException("parameter names and values cannot contain the separator string: " + PARAMETER_SEPARATOR);
        }
        
        if (value.indexOf(VALUE_SEPARATOR) != -1 || name.indexOf(VALUE_SEPARATOR) != -1) {
            throw new IllegalArgumentException("parameters names and values cannot contain the separator string: " + VALUE_SEPARATOR);
        }
        
        Map<String, String> parameters = new HashMap<String, String>(this.parameters);
        parameters.put(name, value);
        return new NavAddress(method, base, anchor, parameters);
    }
    
    /**
     * @return returns a new address object without the anchor part
     */
    public NavAddress withAnchorStripped() {
        return new NavAddress(method, base, null, parameters);
    }

    /**
     * @return returns a new address object with anchor and parameters removed
     */    
    public NavAddress withAllStripped() {
        return new NavAddress(method, base, null, null);
    }

    /**
     * @return the anchor of the address
     */    
    public String getAnchor() {
        return anchor;
    }
    
    /**
     * @return the method of the address
     */
    public NavAddressMethod getMethod() {
        return method;
    }
    
    /**
     * @return the base of the address
     */
    public String getBase() {
        return base;
    }
    
    /**
     * @param name the name of the parameter
     * @return the value of the parameter with the given name or null if no such parameter
     */
    public String getParameter(String name) {
        return parameters.get(name);
    }
    
    /**
     * Converts this address to a feature name, if possible.
     * @return a CALFeatureName for the entity this address is for
     * @throws UnsupportedOperationException of this address cannot be converted to a feature name
     */
    public CALFeatureName toFeatureName() {
        
        if (method == FUNCTION_METHOD) {
            return CALFeatureName.getFunctionFeatureName(QualifiedName.makeFromCompoundName(base));
            
        } else if (method == CLASS_METHOD_METHOD) {
            return CALFeatureName.getClassMethodFeatureName(QualifiedName.makeFromCompoundName(base));
            
        } else if (method == TYPE_CLASS_METHOD) {
            return CALFeatureName.getTypeClassFeatureName(QualifiedName.makeFromCompoundName(base));
            
        } else if (method == TYPE_CONSTRUCTOR_METHOD) {
            return CALFeatureName.getTypeConstructorFeatureName(QualifiedName.makeFromCompoundName(base));
        
        } else if (method == DATA_CONSTRUCTOR_METHOD) {
            return CALFeatureName.getDataConstructorFeatureName(QualifiedName.makeFromCompoundName(base));
        
        } else if (method == MODULE_METHOD) {
            return CALFeatureName.getModuleFeatureName(ModuleName.make(base));
        
        } else if (method == MODULE_NAMESPACE_METHOD) {
            return CALFeatureName.getModuleFeatureName(ModuleName.make(base));
        
        } else if (method == CLASS_INSTANCE_METHOD) {
            ModuleName moduleName = ModuleName.make(getParameter(INSTANCE_MODULE_PARAMETER));
            QualifiedName className = QualifiedName.makeFromCompoundName(getParameter(INSTANCE_CLASS_PARAMETER));
            String typeIdentifier = getParameter(INSTANCE_TYPE_PARAMETER);
            ClassInstanceIdentifier identifier = ClassInstanceIdentifier.make(className, typeIdentifier);
            
            return CALFeatureName.getClassInstanceFeatureName(identifier, moduleName);
            
        } else if (method == INSTANCE_METHOD_METHOD) {
            ModuleName moduleName = ModuleName.make(getParameter(INSTANCE_MODULE_PARAMETER));
            QualifiedName className = QualifiedName.makeFromCompoundName(getParameter(INSTANCE_CLASS_PARAMETER));
            String typeIdentifier = getParameter(INSTANCE_TYPE_PARAMETER);
            String methodName = getParameter(INSTANCE_METHOD_PARAMETER);
            ClassInstanceIdentifier identifier = ClassInstanceIdentifier.make(className, typeIdentifier);
            
            return CALFeatureName.getInstanceMethodFeatureName(identifier, moduleName, methodName);
        }
        
        throw new UnsupportedOperationException("this type of address cannot be converted to a feature name");
    }
    
    /**
     * @return the string form of the address
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return method + METHOD_SEPARATOR 
                      + (base != null ? base : "")
                      + getParametersString()
                      + (anchor != null ? ANCHOR_SEPARATOR + anchor : "");
    }
    
    /**
     * @return the string representation of the parameters in the form &foo=value&bar=baz
     */
    private String getParametersString() {

        if (parameters.size() == 0) {
            return "";
        }
        
        StringBuilder builder = new StringBuilder();
        
        for (final Map.Entry<String, String> entry : parameters.entrySet()) {
            
            builder.append(PARAMETER_SEPARATOR);
            
            String name = entry.getKey();
            String value = entry.getValue();
            builder.append(name + VALUE_SEPARATOR + value);
        }
        
        return builder.toString();
    }
    
    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object o) {
        
        if (o instanceof NavAddress) {
            return o.toString().equals(toString());
        }
        
        return false;
    }
    
    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return toString().hashCode();
    }
}