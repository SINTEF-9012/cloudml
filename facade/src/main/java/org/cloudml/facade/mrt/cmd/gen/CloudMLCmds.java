package org.cloudml.facade.mrt.cmd.gen;

import com.google.common.base.Objects;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import org.cloudml.facade.mrt.cmd.abstracts.Property;
import org.cloudml.facade.mrt.cmd.abstracts.XPath;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.StringExtensions;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.representer.Representer;

@SuppressWarnings("all")
public class CloudMLCmds {
  public CloudMLCmds() {
    yamlConstructor = new Constructor();
    yamlRepresenter = new Representer();
    yamlConstructor.addTypeDescription(new TypeDescription(GetSnapshot.class, "!getSnapshot"));
    yamlRepresenter.addClassTag(GetSnapshot.class, new org.yaml.snakeyaml.nodes.Tag("!getSnapshot"));
    yamlConstructor.addTypeDescription(new TypeDescription(Terminate.class, "!terminate"));
    yamlRepresenter.addClassTag(Terminate.class, new org.yaml.snakeyaml.nodes.Tag("!terminate"));
    yamlConstructor.addTypeDescription(new TypeDescription(ListenToAdded.class, "!listenToAdded"));
    yamlRepresenter.addClassTag(ListenToAdded.class, new org.yaml.snakeyaml.nodes.Tag("!listenToAdded"));
    yamlConstructor.addTypeDescription(new TypeDescription(Added.class, "!added"));
    yamlRepresenter.addClassTag(Added.class, new org.yaml.snakeyaml.nodes.Tag("!added"));
    yamlConstructor.addTypeDescription(new TypeDescription(Remove.class, "!remove"));
    yamlRepresenter.addClassTag(Remove.class, new org.yaml.snakeyaml.nodes.Tag("!remove"));
    yamlConstructor.addTypeDescription(new TypeDescription(Snapshot.class, "!snapshot"));
    yamlRepresenter.addClassTag(Snapshot.class, new org.yaml.snakeyaml.nodes.Tag("!snapshot"));
    yamlConstructor.addTypeDescription(new TypeDescription(Set.class, "!set"));
    yamlRepresenter.addClassTag(Set.class, new org.yaml.snakeyaml.nodes.Tag("!set"));
    yamlConstructor.addTypeDescription(new TypeDescription(Extended.class, "!extended"));
    yamlRepresenter.addClassTag(Extended.class, new org.yaml.snakeyaml.nodes.Tag("!extended"));
    yamlConstructor.addTypeDescription(new TypeDescription(CreateAndAdd.class, "!createAndAdd"));
    yamlRepresenter.addClassTag(CreateAndAdd.class, new org.yaml.snakeyaml.nodes.Tag("!createAndAdd"));
    yamlConstructor.addTypeDescription(new TypeDescription(Add.class, "!add"));
    yamlRepresenter.addClassTag(Add.class, new org.yaml.snakeyaml.nodes.Tag("!add"));
    yamlConstructor.addTypeDescription(new TypeDescription(Create.class, "!create"));
    yamlRepresenter.addClassTag(Create.class, new org.yaml.snakeyaml.nodes.Tag("!create"));
    yamlConstructor.addTypeDescription(new TypeDescription(Provision.class, "!provision"));
    yamlRepresenter.addClassTag(Provision.class, new org.yaml.snakeyaml.nodes.Tag("!provision"));
    yamlConstructor.addTypeDescription(new TypeDescription(ListenToFlush.class, "!listenToFlush"));
    yamlRepresenter.addClassTag(ListenToFlush.class, new org.yaml.snakeyaml.nodes.Tag("!listenToFlush"));
    yamlConstructor.addTypeDescription(new TypeDescription(Flush.class, "!flush"));
    yamlRepresenter.addClassTag(Flush.class, new org.yaml.snakeyaml.nodes.Tag("!flush"));
    yamlConstructor.addTypeDescription(new TypeDescription(Created.class, "!created"));
    yamlRepresenter.addClassTag(Created.class, new org.yaml.snakeyaml.nodes.Tag("!created"));
    yamlConstructor.addTypeDescription(new TypeDescription(Updated.class, "!updated"));
    yamlRepresenter.addClassTag(Updated.class, new org.yaml.snakeyaml.nodes.Tag("!updated"));
    yamlConstructor.addTypeDescription(new TypeDescription(Commit.class, "!commit"));
    yamlRepresenter.addClassTag(Commit.class, new org.yaml.snakeyaml.nodes.Tag("!commit"));
    yamlConstructor.addTypeDescription(new TypeDescription(RequestFlush.class, "!requestFlush"));
    yamlRepresenter.addClassTag(RequestFlush.class, new org.yaml.snakeyaml.nodes.Tag("!requestFlush"));
    yamlConstructor.addTypeDescription(new TypeDescription(Removed.class, "!removed"));
    yamlRepresenter.addClassTag(Removed.class, new org.yaml.snakeyaml.nodes.Tag("!removed"));
    yamlConstructor.addTypeDescription(new TypeDescription(ListenToAny.class, "!listenToAny"));
    yamlRepresenter.addClassTag(ListenToAny.class, new org.yaml.snakeyaml.nodes.Tag("!listenToAny"));
    yamlConstructor.addTypeDescription(new TypeDescription(org.cloudml.facade.mrt.cmd.abstracts.XPath.class, "!xpath"));
    yamlRepresenter.addClassTag(org.cloudml.facade.mrt.cmd.abstracts.XPath.class, new org.yaml.snakeyaml.nodes.Tag("!xpath"));
    
    yaml = new Yaml(yamlConstructor, yamlRepresenter);
    
  }
  
  public final static CloudMLCmds INSTANCE = new CloudMLCmds();
  
  private Constructor yamlConstructor;
  
  private Representer yamlRepresenter;
  
  private Yaml yaml;
  
  private TypeDescription nouse;
  
  public static Map<Object,String> tempObjects = new java.util.HashMap<Object,String>();
  
  public Yaml getYaml() {
    return this.yaml;
  }
  
  public static Object convert(final String type, final Object v, final Object context) {
    Object _switchResult = null;
    boolean _matched = false;
    if (!_matched) {
      if (v instanceof XPath) {
        final XPath _xPath = (XPath)v;
        _matched=true;
        Object _query = _xPath.query(context);
        _switchResult = _query;
      }
    }
    if (!_matched) {
      if (v instanceof String) {
        final String _string = (String)v;
        _matched=true;
        Object _switchResult_1 = null;
        boolean _matched_1 = false;
        if (!_matched_1) {
          if (Objects.equal(type,"int")) {
            _matched_1=true;
            int _parseInt = Integer.parseInt(_string);
            _switchResult_1 = Integer.valueOf(_parseInt);
          }
        }
        if (!_matched_1) {
          if (Objects.equal(type,"double")) {
            _matched_1=true;
            double _parseDouble = Double.parseDouble(_string);
            _switchResult_1 = Double.valueOf(_parseDouble);
          }
        }
        if (!_matched_1) {
          if (Objects.equal(type,"boolean")) {
            _matched_1=true;
            boolean _parseBoolean = Boolean.parseBoolean(_string);
            _switchResult_1 = Boolean.valueOf(_parseBoolean);
          }
        }
        if (!_matched_1) {
          if (Objects.equal(type,"String")) {
            _matched_1=true;
            _switchResult_1 = _string;
          }
        }
        if (!_matched_1) {
          _switchResult_1 = null;
        }
        _switchResult = ((Comparable<Object>)_switchResult_1);
      }
    }
    if (!_matched) {
      if (v instanceof Object) {
        final Object _object = (Object)v;
        _matched=true;
        _switchResult = _object;
      }
    }
    return _switchResult;
  }
  
  public static boolean setProperty(final Object obj, final Property p, final Object value, final Object context) {
    try {
      boolean _xblockexpression = false;
      {
        final Class<? extends Object> clazz = obj.getClass();
        Field[] _fields = clazz.getFields();
        final Function1<Field,Boolean> _function = new Function1<Field,Boolean>() {
            public Boolean apply(final Field it) {
              String _name = it.getName();
              boolean _equals = Objects.equal(_name, p.name);
              return Boolean.valueOf(_equals);
            }
          };
        final Field pubField = IterableExtensions.<Field>findFirst(((Iterable<Field>)Conversions.doWrapArray(_fields)), _function);
        boolean _notEquals = (!Objects.equal(pubField, null));
        if (_notEquals) {
          final Object original = pubField.get(obj);
          Class<? extends Object> _type = pubField.getType();
          String _simpleName = _type.getSimpleName();
          final Object newValue = CloudMLCmds.convert(_simpleName, value, context);
          boolean _notEquals_1 = (!Objects.equal(original, newValue));
          if (_notEquals_1) {
            pubField.set(obj, newValue);
            return true;
          }
        } else {
          Method[] _methods = clazz.getMethods();
          final Function1<Method,Boolean> _function_1 = new Function1<Method,Boolean>() {
              public Boolean apply(final Method it) {
                String _name = it.getName();
                String _firstUpper = StringExtensions.toFirstUpper(p.name);
                String _plus = ("set" + _firstUpper);
                boolean _equals = Objects.equal(_name, _plus);
                return Boolean.valueOf(_equals);
              }
            };
          boolean _exists = IterableExtensions.<Method>exists(((Iterable<Method>)Conversions.doWrapArray(_methods)), _function_1);
          if (_exists) {
            Method[] _methods_1 = clazz.getMethods();
            final Function1<Method,Boolean> _function_2 = new Function1<Method,Boolean>() {
                public Boolean apply(final Method it) {
                  String _name = it.getName();
                  String _firstUpper = StringExtensions.toFirstUpper(p.name);
                  String _plus = ("set" + _firstUpper);
                  boolean _equals = Objects.equal(_name, _plus);
                  return Boolean.valueOf(_equals);
                }
              };
            final Method setter = IterableExtensions.<Method>findFirst(((Iterable<Method>)Conversions.doWrapArray(_methods_1)), _function_2);
            Method[] _methods_2 = clazz.getMethods();
            final Function1<Method,Boolean> _function_3 = new Function1<Method,Boolean>() {
                public Boolean apply(final Method it) {
                  String _name = it.getName();
                  String _firstUpper = StringExtensions.toFirstUpper(p.name);
                  String _plus = ("get" + _firstUpper);
                  boolean _equals = Objects.equal(_name, _plus);
                  return Boolean.valueOf(_equals);
                }
              };
            final Method getter = IterableExtensions.<Method>findFirst(((Iterable<Method>)Conversions.doWrapArray(_methods_2)), _function_3);
            Class<? extends Object>[] _parameterTypes = null;
            if (setter!=null) {
              _parameterTypes=setter.getParameterTypes();
            }
            Class<? extends Object> _get = _parameterTypes[0];
            String _simpleName_1 = _get.getSimpleName();
            final Object newValue_1 = CloudMLCmds.convert(_simpleName_1, value, context);
            final Object original_1 = getter.invoke(obj);
            boolean _notEquals_2 = (!Objects.equal(original_1, newValue_1));
            if (_notEquals_2) {
              if (setter!=null) {
                setter.invoke(obj, newValue_1);
              }
              return true;
            }
          } else {
            boolean _startsWith = p.name.startsWith("properties/");
            if (_startsWith) {
              String _trim = p.name.trim();
              final String propName = _trim.substring(11);
              Method[] _methods_3 = clazz.getMethods();
              final Function1<Method,Boolean> _function_4 = new Function1<Method,Boolean>() {
                  public Boolean apply(final Method it) {
                    String _name = it.getName();
                    boolean _equals = Objects.equal(_name, "setProperty");
                    return Boolean.valueOf(_equals);
                  }
                };
              final Method setter_1 = IterableExtensions.<Method>findFirst(((Iterable<Method>)Conversions.doWrapArray(_methods_3)), _function_4);
              try {
                if (setter_1!=null) {
                  String _string = value.toString();
                  setter_1.invoke(obj, propName, _string);
                }
                return true;
              } catch (final Throwable _t) {
                if (_t instanceof Exception) {
                  final Exception e = (Exception)_t;
                  return false;
                } else {
                  throw Exceptions.sneakyThrow(_t);
                }
              }
            }
          }
        }
        _xblockexpression = (false);
      }
      return _xblockexpression;
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
}
