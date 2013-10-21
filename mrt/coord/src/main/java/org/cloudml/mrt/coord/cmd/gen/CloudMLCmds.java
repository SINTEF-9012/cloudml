package org.cloudml.mrt.coord.cmd.gen;

import com.google.common.base.Objects;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import org.cloudml.mrt.coord.cmd.abstracts.Property;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.StringExtensions;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

@SuppressWarnings("all")
public class CloudMLCmds {
  public CloudMLCmds() {
    yamlConstructor = new Constructor();
    yamlConstructor.addTypeDescription(new TypeDescription(GetSnapshot.class, "!getSnapshot"));
    yamlConstructor.addTypeDescription(new TypeDescription(ListenToAdded.class, "!listenToAdded"));
    yamlConstructor.addTypeDescription(new TypeDescription(Added.class, "!added"));
    yamlConstructor.addTypeDescription(new TypeDescription(Set.class, "!set"));
    yamlConstructor.addTypeDescription(new TypeDescription(CreateAndAdd.class, "!createAndAdd"));
    yamlConstructor.addTypeDescription(new TypeDescription(Add.class, "!add"));
    yamlConstructor.addTypeDescription(new TypeDescription(Create.class, "!create"));
    yamlConstructor.addTypeDescription(new TypeDescription(ListenToFlush.class, "!listenToFlush"));
    yamlConstructor.addTypeDescription(new TypeDescription(Flush.class, "!flush"));
    yamlConstructor.addTypeDescription(new TypeDescription(Created.class, "!created"));
    yamlConstructor.addTypeDescription(new TypeDescription(Updated.class, "!updated"));
    yamlConstructor.addTypeDescription(new TypeDescription(Commit.class, "!commit"));
    yamlConstructor.addTypeDescription(new TypeDescription(RequestFlush.class, "!requestFlush"));
    yaml = new Yaml(yamlConstructor);
    
  }
  
  public final static CloudMLCmds INSTANCE = new CloudMLCmds();
  
  private Constructor yamlConstructor;
  
  private Yaml yaml;
  
  private TypeDescription nouse;
  
  public Yaml getYaml() {
    return this.yaml;
  }
  
  public static Object convert(final String type, final Object v) {
    Object _switchResult = null;
    boolean _matched = false;
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
  
  public static boolean setProperty(final Object obj, final Property p, final Object value) {
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
          final Object newValue = CloudMLCmds.convert(_simpleName, value);
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
          final Method setter = IterableExtensions.<Method>findFirst(((Iterable<Method>)Conversions.doWrapArray(_methods)), _function_1);
          Method[] _methods_1 = clazz.getMethods();
          final Function1<Method,Boolean> _function_2 = new Function1<Method,Boolean>() {
              public Boolean apply(final Method it) {
                String _name = it.getName();
                String _firstUpper = StringExtensions.toFirstUpper(p.name);
                String _plus = ("get" + _firstUpper);
                boolean _equals = Objects.equal(_name, _plus);
                return Boolean.valueOf(_equals);
              }
            };
          final Method getter = IterableExtensions.<Method>findFirst(((Iterable<Method>)Conversions.doWrapArray(_methods_1)), _function_2);
          Class<? extends Object>[] _parameterTypes = null;
          if (setter!=null) {
            _parameterTypes=setter.getParameterTypes();
          }
          Class<? extends Object> _get = _parameterTypes[0];
          String _simpleName_1 = _get.getSimpleName();
          final Object newValue_1 = CloudMLCmds.convert(_simpleName_1, value);
          final Object original_1 = getter.invoke(obj);
          boolean _notEquals_2 = (!Objects.equal(original_1, newValue_1));
          if (_notEquals_2) {
            if (setter!=null) {
              setter.invoke(obj, newValue_1);
            }
            return true;
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
