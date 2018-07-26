package org.springframework;

/**
 * @author lei.liu@datatist.com
 * @since 2018-07-26 11:57:15
 */
public class BeanDefinition {

    private String[] dependsOn;
    private String factoryMethodName;
    private String factoryBeanName;
    private boolean singleton;
    private boolean lazyInit;
    private Class beanClass;

    // private ConstructorArgumentValues constructorArgumentValues;

    private MutablePropertyValues propertyValues;


    public DefaultListableBeanFactory getBeanFactory() {
        return null;
    }


    private ClassLoader getBeanClassLoader() {
        return null;
    }

    public void setDependsOn(String[] dependsOn) {
        this.dependsOn = dependsOn;
    }

    public String[] getDependsOn() {
        return dependsOn;
    }

    public void setFactoryMethodName(String factoryMethodName) {
        this.factoryMethodName = factoryMethodName;
    }

    public void setFactoryBeanName(String factoryBeanName) {
        this.factoryBeanName = factoryBeanName;
    }

    public String getFactoryBeanName() {
        return factoryBeanName;
    }

    public void setSingleton(boolean singleton) {
        this.singleton = singleton;
    }

    public boolean isSingleton() {
        return singleton;
    }

    public void setLazyInit(boolean lazyInit) {
        this.lazyInit = lazyInit;
    }
    // ResourceLoader getResourceLoader();

    public MutablePropertyValues getPropertyValues() {
        return propertyValues;
    }

    public void setPropertyValues(MutablePropertyValues propertyValues) {
        this.propertyValues = propertyValues;
    }

    public Class getBeanClass() {
        return beanClass;
    }

    public void setBeanClass(Class beanClass) {
        this.beanClass = beanClass;
    }
}
