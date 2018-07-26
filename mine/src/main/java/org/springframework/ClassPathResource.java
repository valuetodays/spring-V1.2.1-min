package org.springframework;

import java.io.InputStream;

/**
 * @author lei.liu@datatist.com
 * @since 2018-07-26 11:36:50
 */
public class ClassPathResource {
    String CLASSPATH_URL_PREFIX = "classpath:";

    private final String path;

    public ClassPathResource(String location) {
        if (location.startsWith(CLASSPATH_URL_PREFIX)) {
            String tempLocation = location.substring(CLASSPATH_URL_PREFIX.length());
            System.out.println(tempLocation);
            this.path = tempLocation.substring(1);
        } else {
            this.path = null;
        }
    }

    public InputStream getInputStream() {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();

        return cl.getResourceAsStream(this.path);
    }
}
