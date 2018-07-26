package org.springframework;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author lei.liu@datatist.com
 * @since 2018-07-26 11:29:22
 */
public class DefaultListableBeanFactory {

    private final Map<String, BeanDefinition> beanDefinitionMap = new HashMap<>();
    /** List of bean definition names, in registration order */
    private final List<String> beanDefinitionNames = new ArrayList<>();
    /** Map from alias to canonical bean name */
    private final Map<String, String> aliasMap = Collections.synchronizedMap(new HashMap<>());
    private Map<String, Object> cachedBeans = new HashMap<>();

    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) {
        Object oldBeanDefinition = this.beanDefinitionMap.get(beanName);
        if (oldBeanDefinition != null) {
            throw new RuntimeException("already exists");
        }
        beanDefinitionNames.add(beanName);
        beanDefinitionMap.put(beanName, beanDefinition);
    }

    public void registerAlias(String beanName, String alias) {
        synchronized (this.aliasMap) {
            aliasMap.put(alias, beanName);
        }
    }

    public Map<String, BeanDefinition> getBeanDefinitionMap() {
        return beanDefinitionMap;
    }

    public List<String> getBeanDefinitionNames() {
        return beanDefinitionNames;
    }

    public Map<String, String> getAliasMap() {
        return aliasMap;
    }

    public Object getBean(String beanName, Class<?> beanClass) {
        Object cachedBean = cachedBeans.get(beanName);
        if (cachedBean != null) {
            return cachedBean;
        }

        Map<String, BeanDefinition> beanDefinitionMap = getBeanDefinitionMap();
        BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
        Class<?> aClass = beanDefinition.getBeanClass();
        MutablePropertyValues propertyValues = beanDefinition.getPropertyValues();
        try {
            Object o = aClass.newInstance();
            PropertyValue[] propertyValueArr = propertyValues.getPropertyValues();
            for (PropertyValue propertyValue : propertyValueArr) {
                String name = propertyValue.getName();
                Object value = propertyValue.getValue();
                PropertyDescriptor pd = new PropertyDescriptor(name, aClass);
                Method writeMethod = pd.getWriteMethod();
                Object newValue = translateValueIfNeed(value, pd.getPropertyType());
                writeMethod.invoke(o, new Object[] {newValue});
            }
            synchronized (cachedBeans) {
                cachedBeans.put(beanName, o);
            }
            return o;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IntrospectionException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;

    }

    private Object translateValueIfNeed(Object value, Class<?> propertyType) {
        if (value == null) {
            return null;
        }
        if (int.class == propertyType || Integer.class == propertyType) {
            return Integer.valueOf(value.toString());
        }
        return propertyType.cast(value);
    }
}
