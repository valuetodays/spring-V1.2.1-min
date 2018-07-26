package org.springframework;

import org.apache.commons.io.IOUtils;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;

/**
 * @author lei.liu@datatist.com
 * @since 2018-07-26 11:31:59
 */
public class XmlBeanDefinitionReader {

    private final DefaultListableBeanFactory beanFactory;
    private boolean validating = false;

    public XmlBeanDefinitionReader(DefaultListableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public int loadBeanDefinitions(ClassPathResource resource) {
        if (resource == null) {
            throw new RuntimeException("resource cannot be null: expected an XML file");
        }
        InputStream is = null;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(this.validating);
            DocumentBuilder docBuilder = factory.newDocumentBuilder();
            is = resource.getInputStream();
            Document doc = docBuilder.parse(is);
            return registerBeanDefinitions(doc, resource);
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            IOUtils.closeQuietly(is);
        }

        return 0;
    }

    private int registerBeanDefinitions(Document doc, ClassPathResource resource) {
        XmlBeanDefinitionParser parser = new XmlBeanDefinitionParser();
        return parser.registerBeanDefinitions(this, doc, resource);
    }

    public DefaultListableBeanFactory getBeanFactory() {
        return beanFactory;
    }
}
