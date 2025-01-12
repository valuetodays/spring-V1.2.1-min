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

package org.springframework.context;

import org.springframework.beans.BeansException;

/**
 * Interface to be implemented by any object that wishes to be notified
 * of the ApplicationContext that it runs in.
 *
 * <p>Implementing this interface makes sense for example when an object
 * requires access to a set of collaborating beans. Note that configuration
 * via bean references is preferable to implementing this interface just
 * for bean lookup purposes.
 *
 * <p>This interface can also be implemented if an object needs access to file
 * resources, i.e. wants to call getResource, or access to the MessageSource.
 * However, it is preferable to implement the more specific ResourceLoaderAware
 * interface or receive a reference to the MessageSource bean in that scenario.
 *
 * <p>Note that Resource dependencies can also be exposed as bean properties
 * of type Resource, populated via Strings with automatic type conversion by
 * the bean factory. This removes the need for implementing any callback
 * interface just for the purpose of accessing a specific file resource.
 *
 * <p>ApplicationObjectSupport is a convenience base class for
 * application objects, implementing this interface.
 * 
 * <p>For a list of all bean lifecycle methods, see the BeanFactory javadocs.
 *
 * @author Rod Johnson
 * @see ResourceLoaderAware
// * @see org.springframework.context.support.ApplicationObjectSupport
// * @see org.springframework.beans.factory.BeanFactoryAware
// * @see org.springframework.beans.factory.InitializingBean
 * @see org.springframework.beans.factory.BeanFactory
 */
public interface ApplicationContextAware {
	
	/** 
	 * Set the ApplicationContext that this object runs in.
	 * Normally this call will be used to initialize the object.
	 * <p>Invoked after population of normal bean properties but before an init
	 * callback like InitializingBean's afterPropertiesSet or a custom init-method.
	 * Invoked after ResourceLoaderAware's setResourceLoader.
	 * @param applicationContext ApplicationContext object to be used by this object
	 * @throws BeansException in case of applicationContext initialization errors
	 * @throws BeansException if thrown by application applicationContext methods
//	 * @see org.springframework.beans.factory.BeanInitializationException
	 */
	void setApplicationContext(ApplicationContext applicationContext) throws BeansException;

}
