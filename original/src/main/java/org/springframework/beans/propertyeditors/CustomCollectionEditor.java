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

package org.springframework.beans.propertyeditors;

import org.springframework.core.CollectionFactory;

import java.beans.PropertyEditorSupport;
import java.lang.reflect.Array;
import java.util.*;

/**
 * Property editor for Collections, converting any source Collection
 * to a given target Collection type.
 *
 * <p>By default registered for Set, SortedSet and List,
 * to automatically convert any given Collection to one of those
 * target types if the type does not match the target property.
 *
 * @author Juergen Hoeller
 * @since 1.1.3
 * @see Collection
 * @see java.util.Set
 * @see SortedSet
 * @see List
 */
public class CustomCollectionEditor extends PropertyEditorSupport {

	private final Class collectionType;

	/**
	 * Create a new CustomCollectionEditor for the given target type.
	 * <p>If the incoming value is of the given type, it will be used as-is.
	 * If it is a different Collection type or an array, it will be converted
	 * to a default implementation of the given Collection type.
	 * If the value is anything else, a target Collection with that single
	 * value will be created.
	 * <p>The default Collection implementations are: ArrayList for List,
	 * TreeSet for SortedSet, and LinkedHashSet or HashSet for Set.
	 * @param collectionType the target type, which needs to be a
	 * sub-interface of Collection or a concrete Collection class
	 * @see Collection
	 * @see ArrayList
	 * @see TreeSet
	 * @see CollectionFactory#createLinkedSetIfPossible
	 */
	public CustomCollectionEditor(Class collectionType) {
		if (collectionType == null) {
			throw new IllegalArgumentException("Collection type is required");
		}
		if (!Collection.class.isAssignableFrom(collectionType)) {
			throw new IllegalArgumentException(
					"Collection type [" + collectionType.getName() + "] does not implement [java.util.Collection]");
		}
		this.collectionType = collectionType;
	}

	/**
	 * Convert the given text value to a Collection with a single element.
	 */
	public void setAsText(String text) throws IllegalArgumentException {
		setValue(text);
	}

	/**
	 * Convert the given value to a Collection of the target type.
	 */
	public void setValue(Object value) {
		if (this.collectionType.isInstance(value) && !alwaysCreateNewCollection()) {
			// Use the source value as-is, as it matches the target type.
			super.setValue(value);
		}
		else if (value instanceof Collection) {
			// Convert Collection elements to array elements.
			Collection source = (Collection) value;
			Collection target = createCollection(this.collectionType, source.size());
			for (Iterator it = source.iterator(); it.hasNext();) {
				target.add(convertElement(it.next()));
			}
			super.setValue(target);
		}
		else if (value != null && value.getClass().isArray()) {
			// Convert array elements to Collection elements.
			int length = Array.getLength(value);
			Collection target = createCollection(this.collectionType, length);
			for (int i = 0; i < length; i++) {
				target.add(convertElement(Array.get(value, i)));
			}
			super.setValue(target);
		}
		else {
			// A plain value: convert it to a Collection with a single element.
			Collection target = createCollection(this.collectionType, 1);
			target.add(convertElement(value));
			super.setValue(target);
		}
	}

	/**
	 * Create a Collection of the given type, with the given
	 * initial capacity (if supported by the Collection type).
	 * @param collectionType a sub-interface of Collection
	 * @param initialCapacity the initial capacity
	 * @return the new Collection instance
	 */
	protected Collection createCollection(Class collectionType, int initialCapacity) {
		if (!collectionType.isInterface()) {
			try {
				return (Collection) collectionType.newInstance();
			}
			catch (Exception ex) {
				throw new IllegalArgumentException(
						"Could not instantiate collection class [" + collectionType.getName() + "]: " + ex.getMessage());
			}
		}
		else if (List.class.equals(collectionType)) {
			return new ArrayList(initialCapacity);
		}
		else if (SortedSet.class.equals(collectionType)) {
			return new TreeSet();
		}
		else {
			return CollectionFactory.createLinkedSetIfPossible(initialCapacity);
		}
	}

	/**
	 * Return whether to always create a new Collection,
	 * even if the type of the passed-in Collection already matches.
	 * <p>Default is false; can be overridden to enforce creation of a
	 * new Collection, for example to convert elements in any case.
	 * @see #convertElement
	 */
	protected boolean alwaysCreateNewCollection() {
		return false;
	}

	/**
	 * Hook to convert each encountered Collection/array element.
	 * The default implementation simply returns the passed-in element as-is.
	 * <p>Can be overridden to perform conversion of certain elements,
	 * for example String to Integer if a String array comes in and
	 * should be converted to a Set of Integer objects.
	 * <p>Only called if actually creating a new Collection!
	 * This is by default not the case if the type of the passed-in Collection
	 * already matches. Override <code>alwaysCreateNewCollection</code> to
	 * enforce creating a new Collection in every case.
	 * @param element the source element
	 * @return the element to be used in the target Collection
	 * @see #alwaysCreateNewCollection
	 */
	protected Object convertElement(Object element) {
		return element;
	}

	/**
	 * This implementation returns null to indicate that there is no
	 * appropriate text representation.
	 */
	public String getAsText() {
		return null;
	}

}
