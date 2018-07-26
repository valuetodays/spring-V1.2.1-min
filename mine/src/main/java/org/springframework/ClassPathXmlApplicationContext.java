package org.springframework;

/**
 * @author lei.liu@datatist.com
 * @since 2018-07-26 11:20:01
 */
public class ClassPathXmlApplicationContext {

    private String[] configLocations;
    private boolean refresh = true;

    private DefaultListableBeanFactory beanFactory;



    public ClassPathXmlApplicationContext(String[] configLocations) {
        this.configLocations = configLocations;
        if (refresh) {
            refresh();
        }
    }

    private void refresh() {
        refreshBeanFactory();

    }

    private void refreshBeanFactory() {
        if (this.beanFactory != null) {
            this.beanFactory = null;
        }
        DefaultListableBeanFactory beanFactory = createBeanFactory();
        loadBeanDefinitions(beanFactory);
        this.beanFactory = beanFactory;
    }

    private void loadBeanDefinitions(DefaultListableBeanFactory beanFactory) {
        XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(beanFactory);
//        beanDefinitionReader.setEntityResolver(new ResourceEntityResolver(this));
        loadBeanDefinitions(beanDefinitionReader);
    }

    private void loadBeanDefinitions(XmlBeanDefinitionReader reader) {
        String[] configLocations = this.configLocations;
        if (configLocations != null) {
            for (int i = 0; i < configLocations.length; i++) {
                reader.loadBeanDefinitions(new ClassPathResource(configLocations[i]));
            }
        }
    }

    private DefaultListableBeanFactory createBeanFactory() {
        return new DefaultListableBeanFactory();
    }

    public void close() {

    }

    public Object getBean(String beanName, Class<?> beanClass) {
        return beanFactory.getBean(beanName, beanClass);
    }
}
