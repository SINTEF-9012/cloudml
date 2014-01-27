package org.cloudml.facade.mrt.cmd.gen;

import com.google.common.base.Objects;
import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Map;
import org.cloudml.facade.mrt.cmd.abstracts.Change;
import org.cloudml.facade.mrt.cmd.abstracts.Modification;
import org.cloudml.facade.mrt.cmd.abstracts.Parameter;
import org.cloudml.facade.mrt.cmd.abstracts.Property;
import org.cloudml.facade.mrt.cmd.abstracts.Type;
import org.cloudml.facade.mrt.cmd.abstracts.XPath;
import org.cloudml.facade.mrt.cmd.gen.CloudMLCmds;
import org.cloudml.facade.mrt.cmd.gen.Created;
import org.cloudml.facade.mrt.cmd.gen.Updated;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.Functions.Function2;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.MapExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure2;

/**
 * Initialize an object
 */
@SuppressWarnings("all")
public class Create extends Modification {
  public Create() {
  }
  
  public Create(final String nouse) {
  }
  
  public Create(final Procedure1<Create> initializer) {
    initializer.apply(this);
  }
  
  public Type type;
  
  public List<Parameter> initializer;
  
  public Map<Property,Object> keyValues;
  
  @Override
  protected Object _execute(final Object context, final List<Change> changes) {
    try {
      Object _xblockexpression = null;
      {
        final Class<? extends Object> clz = this.type.obtainClass();
        Object _xifexpression = null;
        boolean _or = false;
        boolean _equals = Objects.equal(this.initializer, null);
        if (_equals) {
          _or = true;
        } else {
          boolean _isEmpty = this.initializer.isEmpty();
          _or = (_equals || _isEmpty);
        }
        if (_or) {
          Object _newInstance = clz.newInstance();
          _xifexpression = _newInstance;
        } else {
          Object _xblockexpression_1 = null;
          {
            final Constructor<? extends Object>[] constructors = clz.getConstructors();
            Object _switchResult = null;
            int _size = this.initializer.size();
            final int _switchValue = _size;
            boolean _matched = false;
            if (!_matched) {
              if (Objects.equal(_switchValue,1)) {
                _matched=true;
                final Function1<Constructor<? extends Object>,Boolean> _function = new Function1<Constructor<? extends Object>,Boolean>() {
                    public Boolean apply(final Constructor<? extends Object> it) {
                      boolean _and = false;
                      Class<? extends Object>[] _parameterTypes = it.getParameterTypes();
                      int _size = ((List<Class<? extends Object>>)Conversions.doWrapArray(_parameterTypes)).size();
                      boolean _equals = (_size == 1);
                      if (!_equals) {
                        _and = false;
                      } else {
                        Class<? extends Object>[] _parameterTypes_1 = it.getParameterTypes();
                        Class<? extends Object> _get = _parameterTypes_1[0];
                        String _name = _get.getName();
                        Parameter _get_1 = Create.this.initializer.get(0);
                        boolean _equals_1 = Objects.equal(_name, _get_1.type);
                        _and = (_equals && _equals_1);
                      }
                      return Boolean.valueOf(_and);
                    }
                  };
                Constructor<? extends Object> _findFirst = IterableExtensions.<Constructor<? extends Object>>findFirst(((Iterable<Constructor<? extends Object>>)Conversions.doWrapArray(constructors)), _function);
                Object _newInstance_1 = null;
                if (_findFirst!=null) {
                  Parameter _get = this.initializer.get(0);
                  Parameter _get_1 = this.initializer.get(0);
                  Object _convert = CloudMLCmds.convert(_get.type, _get_1.value, context);
                  _newInstance_1=_findFirst.newInstance(_convert);
                }
                _switchResult = _newInstance_1;
              }
            }
            if (!_matched) {
              if (Objects.equal(_switchValue,2)) {
                _matched=true;
                final Function1<Constructor<? extends Object>,Boolean> _function_1 = new Function1<Constructor<? extends Object>,Boolean>() {
                    public Boolean apply(final Constructor<? extends Object> it) {
                      boolean _and = false;
                      boolean _and_1 = false;
                      Class<? extends Object>[] _parameterTypes = it.getParameterTypes();
                      int _size = ((List<Class<? extends Object>>)Conversions.doWrapArray(_parameterTypes)).size();
                      boolean _equals = (_size == 2);
                      if (!_equals) {
                        _and_1 = false;
                      } else {
                        Class<? extends Object>[] _parameterTypes_1 = it.getParameterTypes();
                        Class<? extends Object> _get = _parameterTypes_1[0];
                        String _simpleName = _get.getSimpleName();
                        Parameter _get_1 = Create.this.initializer.get(0);
                        boolean _equals_1 = Objects.equal(_simpleName, _get_1.type);
                        _and_1 = (_equals && _equals_1);
                      }
                      if (!_and_1) {
                        _and = false;
                      } else {
                        Class<? extends Object>[] _parameterTypes_2 = it.getParameterTypes();
                        Class<? extends Object> _get_2 = _parameterTypes_2[1];
                        String _simpleName_1 = _get_2.getSimpleName();
                        Parameter _get_3 = Create.this.initializer.get(1);
                        boolean _equals_2 = Objects.equal(_simpleName_1, _get_3.type);
                        _and = (_and_1 && _equals_2);
                      }
                      return Boolean.valueOf(_and);
                    }
                  };
                Constructor<? extends Object> _findFirst_1 = IterableExtensions.<Constructor<? extends Object>>findFirst(((Iterable<Constructor<? extends Object>>)Conversions.doWrapArray(constructors)), _function_1);
                Object _newInstance_2 = null;
                if (_findFirst_1!=null) {
                  Parameter _get_2 = this.initializer.get(0);
                  Parameter _get_3 = this.initializer.get(0);
                  Object _convert_1 = CloudMLCmds.convert(_get_2.type, _get_3.value, context);
                  Parameter _get_4 = this.initializer.get(1);
                  Parameter _get_5 = this.initializer.get(1);
                  Object _convert_2 = CloudMLCmds.convert(_get_4.type, _get_5.value, context);
                  _newInstance_2=_findFirst_1.newInstance(_convert_1, _convert_2);
                }
                _switchResult = _newInstance_2;
              }
            }
            if (!_matched) {
              if (Objects.equal(_switchValue,3)) {
                _matched=true;
                final Function1<Constructor<? extends Object>,Boolean> _function_2 = new Function1<Constructor<? extends Object>,Boolean>() {
                    public Boolean apply(final Constructor<? extends Object> it) {
                      boolean _and = false;
                      boolean _and_1 = false;
                      boolean _and_2 = false;
                      Class<? extends Object>[] _parameterTypes = it.getParameterTypes();
                      int _size = ((List<Class<? extends Object>>)Conversions.doWrapArray(_parameterTypes)).size();
                      boolean _equals = (_size == 3);
                      if (!_equals) {
                        _and_2 = false;
                      } else {
                        Class<? extends Object>[] _parameterTypes_1 = it.getParameterTypes();
                        Class<? extends Object> _get = _parameterTypes_1[0];
                        String _simpleName = _get.getSimpleName();
                        Parameter _get_1 = Create.this.initializer.get(0);
                        boolean _equals_1 = Objects.equal(_simpleName, _get_1.type);
                        _and_2 = (_equals && _equals_1);
                      }
                      if (!_and_2) {
                        _and_1 = false;
                      } else {
                        Class<? extends Object>[] _parameterTypes_2 = it.getParameterTypes();
                        Class<? extends Object> _get_2 = _parameterTypes_2[1];
                        String _simpleName_1 = _get_2.getSimpleName();
                        Parameter _get_3 = Create.this.initializer.get(1);
                        boolean _equals_2 = Objects.equal(_simpleName_1, _get_3.type);
                        _and_1 = (_and_2 && _equals_2);
                      }
                      if (!_and_1) {
                        _and = false;
                      } else {
                        Class<? extends Object>[] _parameterTypes_3 = it.getParameterTypes();
                        Class<? extends Object> _get_4 = _parameterTypes_3[2];
                        String _simpleName_2 = _get_4.getSimpleName();
                        Parameter _get_5 = Create.this.initializer.get(2);
                        boolean _equals_3 = Objects.equal(_simpleName_2, _get_5.type);
                        _and = (_and_1 && _equals_3);
                      }
                      return Boolean.valueOf(_and);
                    }
                  };
                Constructor<? extends Object> _findFirst_2 = IterableExtensions.<Constructor<? extends Object>>findFirst(((Iterable<Constructor<? extends Object>>)Conversions.doWrapArray(constructors)), _function_2);
                Object _newInstance_3 = null;
                if (_findFirst_2!=null) {
                  Parameter _get_6 = this.initializer.get(0);
                  Parameter _get_7 = this.initializer.get(0);
                  Object _convert_3 = CloudMLCmds.convert(_get_6.type, _get_7.value, context);
                  Parameter _get_8 = this.initializer.get(1);
                  Parameter _get_9 = this.initializer.get(1);
                  Object _convert_4 = CloudMLCmds.convert(_get_8.type, _get_9.value, context);
                  Parameter _get_10 = this.initializer.get(2);
                  Parameter _get_11 = this.initializer.get(2);
                  Object _convert_5 = CloudMLCmds.convert(_get_10.type, _get_11.value, context);
                  _newInstance_3=_findFirst_2.newInstance(_convert_3, _convert_4, _convert_5);
                }
                _switchResult = _newInstance_3;
              }
            }
            _xblockexpression_1 = (_switchResult);
          }
          _xifexpression = _xblockexpression_1;
        }
        final Object newObject = _xifexpression;
        boolean _notEquals = (!Objects.equal(newObject, null));
        if (_notEquals) {
          long _currentTimeMillis = System.currentTimeMillis();
          String _valueOf = String.valueOf(_currentTimeMillis);
          CloudMLCmds.tempObjects.put(newObject, _valueOf);
        }
        final Procedure1<Created> _function = new Procedure1<Created>() {
            public void apply(final Created it) {
              it.object = newObject;
              String _get = CloudMLCmds.tempObjects.get(newObject);
              it.object_repr = _get;
              it.initializer = Create.this.initializer;
            }
          };
        Created _created = new Created(_function);
        changes.add(_created);
        Map<Property,Object> _filter = null;
        if (this.keyValues!=null) {
          final Function2<Property,Object,Boolean> _function_1 = new Function2<Property,Object,Boolean>() {
              public Boolean apply(final Property p, final Object v) {
                boolean _setProperty = CloudMLCmds.setProperty(newObject, p, v, context);
                return Boolean.valueOf(_setProperty);
              }
            };
          _filter=MapExtensions.<Property, Object>filter(this.keyValues, _function_1);
        }
        final Procedure2<Property,Object> _function_2 = new Procedure2<Property,Object>() {
            public void apply(final Property p, final Object v) {
              final Procedure1<Updated> _function = new Procedure1<Updated>() {
                  public void apply(final Updated it) {
                    it.parent = newObject;
                    String _get = CloudMLCmds.tempObjects.get(newObject);
                    it.parent_repr = _get;
                    it.property = p.name;
                    Object _xifexpression = null;
                    if ((v instanceof XPath)) {
                      _xifexpression = ((XPath) v).literal;
                    } else {
                      _xifexpression = v;
                    }
                    it.newValue = _xifexpression;
                  }
                };
              Updated _updated = new Updated(_function);
              changes.add(_updated);
            }
          };
        MapExtensions.<Property, Object>forEach(_filter, _function_2);
        _xblockexpression = (newObject);
      }
      return _xblockexpression;
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
}
