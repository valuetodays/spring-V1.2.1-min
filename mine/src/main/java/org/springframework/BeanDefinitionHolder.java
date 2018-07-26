package org.springframework;

/**
 * @author lei.liu@datatist.com
 * @since 2018-07-26 11:54:52
 */
public class BeanDefinitionHolder {
    private String beanName;
    private BeanDefinition beanDefinition;
    private String[] aliasesArray;

    public BeanDefinitionHolder(BeanDefinition beanDefinition, String beanName, String[] aliasesArray) {
        this.beanDefinition = beanDefinition;
        this.beanName = beanName;
        this.aliasesArray = aliasesArray;
    }

    public String getBeanName() {
        return beanName;
    }

    public BeanDefinition getBeanDefinition() {
        return beanDefinition;
    }

    public String[] getAliases() {
        return aliasesArray;
    }
}
