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

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceEditor;

import java.beans.PropertyEditorSupport;
import java.io.IOException;

/**
 * One-way PropertyEditor, which can convert from a text string to a
 * <code>java.io.InputStream</code>, allowing InputStream properties
 * to be set directly as a text string.
 *
 * <p>Supports Spring-style URL notation: any fully qualified standard URL
 * ("file:", "http:", etc) and Spring's special "classpath:" pseudo-URL.
 *
 * <p>Note that in the default usage, the stream is not closed by Spring itself!
 *
 * @author Juergen Hoeller
 * @since 1.0.1
 * @see java.io.InputStream
 */
public class InputStreamEditor extends PropertyEditorSupport {

	private final ResourceEditor resourceEditor;

	/**
	 * Create a new InputStreamEditor,
	 * using the default ResourceEditor underneath.
	 */
	public InputStreamEditor() {
		this.resourceEditor = new ResourceEditor();
	}

	/**
	 * Create a new InputStreamEditor,
	 * using the given ResourceEditor underneath.
	 * @param resourceEditor the ResourceEditor to use
	 */
	public InputStreamEditor(ResourceEditor resourceEditor) {
		this.resourceEditor = resourceEditor;
	}

	public void setAsText(String text) throws IllegalArgumentException {
		this.resourceEditor.setAsText(text);
		Resource resource = (Resource) this.resourceEditor.getValue();
		try {
			setValue(resource != null ? resource.getInputStream() : null);
		}
		catch (IOException ex) {
			throw new IllegalArgumentException(
					"Could not retrieve InputStream for " + resource + ": " + ex.getMessage());
		}
	}

	/**
	 * This implementation returns null to indicate that there is no
	 * appropriate text representation.
	 */
	public String getAsText() {
		return null;
	}

}
