/*
 * Copyright 2002-2005 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework;

/**
 * Utility methods that are useful for bean definition readers implementations.
 *
 * @author Juergen Hoeller
 * @since 1.1
// * @see PropertiesBeanDefinitionReader
// * @see org.springframework.beans.factory.xml.DefaultXmlBeanDefinitionParser
 */
public class BeanDefinitionReaderUtils {

	/**
	 * Separator for generated bean names. If a class name or parent name is not
	 * unique, "#2", "#3" etc will be appended, until the name becomes unique.
	 */
	public static final String GENERATED_BEAN_NAME_SEPARATOR = "#";


	/**
	 * Register the given bean definition with the given bean factory.
	 * @param bdHolder the bean definition including name and aliases
	 * @param beanFactory the bean factory to register with
//	 * @throws BeansException if registration failedBeanDefinitionReaderUtils
	 */
	public static void registerBeanDefinition(
            BeanDefinitionHolder bdHolder, DefaultListableBeanFactory beanFactory) {

		// register bean definition under primary name
		beanFactory.registerBeanDefinition(bdHolder.getBeanName(), bdHolder.getBeanDefinition());

		// register aliases for bean name, if any
		if (bdHolder.getAliases() != null) {
			for (int i = 0; i < bdHolder.getAliases().length; i++) {
				beanFactory.registerAlias(bdHolder.getBeanName(), bdHolder.getAliases()[i]);
			}
		}
	}

	public static BeanDefinition createBeanDefinition(String className, String parent, Object o, MutablePropertyValues pvs) {
// TODO
		BeanDefinition beanDefinition = new BeanDefinition();
		try {
			Class<?> aClass = Class.forName(className);
			beanDefinition.setPropertyValues(pvs);
			beanDefinition.setBeanClass(aClass);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return beanDefinition;
	}
}
