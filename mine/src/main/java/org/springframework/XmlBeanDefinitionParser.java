package org.springframework;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author lei.liu@datatist.com
 * @since 2018-07-26 11:48:41
 */
public class XmlBeanDefinitionParser {

    public static final String BEAN_NAME_DELIMITERS = ",; ";

    /**
     * Value of a T/F attribute that represents true.
     * Anything else represents false. Case seNsItive.
     */
    public static final String TRUE_VALUE = "true";
    public static final String DEFAULT_VALUE = "default";
    public static final String DESCRIPTION_ELEMENT = "description";

    public static final String AUTOWIRE_BY_NAME_VALUE = "byName";
    public static final String AUTOWIRE_BY_TYPE_VALUE = "byType";
    public static final String AUTOWIRE_CONSTRUCTOR_VALUE = "constructor";
    public static final String AUTOWIRE_AUTODETECT_VALUE = "autodetect";

    public static final String DEPENDENCY_CHECK_ALL_ATTRIBUTE_VALUE = "all";
    public static final String DEPENDENCY_CHECK_SIMPLE_ATTRIBUTE_VALUE = "simple";
    public static final String DEPENDENCY_CHECK_OBJECTS_ATTRIBUTE_VALUE = "objects";

    public static final String DEFAULT_LAZY_INIT_ATTRIBUTE = "default-lazy-init";
    public static final String DEFAULT_DEPENDENCY_CHECK_ATTRIBUTE = "default-dependency-check";
    public static final String DEFAULT_AUTOWIRE_ATTRIBUTE = "default-autowire";

    public static final String IMPORT_ELEMENT = "import";
    public static final String RESOURCE_ATTRIBUTE = "resource";

    public static final String ALIAS_ELEMENT = "alias";
    public static final String NAME_ATTRIBUTE = "name";
    public static final String ALIAS_ATTRIBUTE = "alias";

    public static final String BEAN_ELEMENT = "bean";
    public static final String ID_ATTRIBUTE = "id";
    public static final String PARENT_ATTRIBUTE = "parent";

    public static final String CLASS_ATTRIBUTE = "class";
    public static final String ABSTRACT_ATTRIBUTE = "abstract";
    public static final String SINGLETON_ATTRIBUTE = "singleton";
    public static final String LAZY_INIT_ATTRIBUTE = "lazy-init";
    public static final String AUTOWIRE_ATTRIBUTE = "autowire";
    public static final String DEPENDENCY_CHECK_ATTRIBUTE = "dependency-check";
    public static final String DEPENDS_ON_ATTRIBUTE = "depends-on";
    public static final String INIT_METHOD_ATTRIBUTE = "init-method";
    public static final String DESTROY_METHOD_ATTRIBUTE = "destroy-method";
    public static final String FACTORY_METHOD_ATTRIBUTE = "factory-method";
    public static final String FACTORY_BEAN_ATTRIBUTE = "factory-bean";

    public static final String CONSTRUCTOR_ARG_ELEMENT = "constructor-arg";
    public static final String INDEX_ATTRIBUTE = "index";
    public static final String TYPE_ATTRIBUTE = "type";
    public static final String PROPERTY_ELEMENT = "property";
    public static final String REF_ATTRIBUTE = "ref";
    public static final String VALUE_ATTRIBUTE = "value";
    public static final String LOOKUP_METHOD_ELEMENT = "lookup-method";

    public static final String REPLACED_METHOD_ELEMENT = "replaced-method";
    public static final String REPLACER_ATTRIBUTE = "replacer";
    public static final String ARG_TYPE_ELEMENT = "arg-type";
    public static final String ARG_TYPE_MATCH_ATTRIBUTE = "match";

    public static final String REF_ELEMENT = "ref";
    public static final String IDREF_ELEMENT = "idref";
    public static final String BEAN_REF_ATTRIBUTE = "bean";
    public static final String LOCAL_REF_ATTRIBUTE = "local";
    public static final String PARENT_REF_ATTRIBUTE = "parent";

    public static final String VALUE_ELEMENT = "value";
    public static final String NULL_ELEMENT = "null";
    public static final String LIST_ELEMENT = "list";
    public static final String SET_ELEMENT = "set";
    public static final String MAP_ELEMENT = "map";
    public static final String ENTRY_ELEMENT = "entry";
    public static final String KEY_ELEMENT = "key";
    public static final String KEY_ATTRIBUTE = "key";
    public static final String KEY_REF_ATTRIBUTE = "key-ref";
    public static final String VALUE_REF_ATTRIBUTE = "value-ref";
    public static final String PROPS_ELEMENT = "props";
    public static final String PROP_ELEMENT = "prop";

    private XmlBeanDefinitionReader beanDefinitionReader;
    private ClassPathResource resource;
    private String defaultLazyInit;
    private String defaultDependencyCheck;
    private String defaultAutowire;

    public int registerBeanDefinitions(XmlBeanDefinitionReader reader, Document doc, ClassPathResource resource) {

        this.beanDefinitionReader = reader;
        this.resource = resource;

        Element root = doc.getDocumentElement();

        this.defaultLazyInit = root.getAttribute(DEFAULT_LAZY_INIT_ATTRIBUTE);
        this.defaultDependencyCheck = root.getAttribute(DEFAULT_DEPENDENCY_CHECK_ATTRIBUTE);
        this.defaultAutowire = root.getAttribute(DEFAULT_AUTOWIRE_ATTRIBUTE);

        int beanDefinitionCount = parseBeanDefinitions(root);
//        if (logger.isDebugEnabled()) {
//            logger.debug("Found " + beanDefinitionCount + " <bean> elements defining beans");
//        }
        return beanDefinitionCount;
    }

    private int parseBeanDefinitions(Element root) {
        NodeList nl = root.getChildNodes();
        int beanDefinitionCounter = 0;
        for (int i = 0; i < nl.getLength(); i++) {
            Node node = nl.item(i);
            if (node instanceof Element) {
                Element ele = (Element) node;
                if (BEAN_ELEMENT.equals(node.getNodeName())) {
                    beanDefinitionCounter++;
                    BeanDefinitionHolder bdHolder = parseBeanDefinitionElement(ele);
                    BeanDefinitionReaderUtils.registerBeanDefinition(
                            bdHolder, this.beanDefinitionReader.getBeanFactory());
                }
            }
        }
        return beanDefinitionCounter;
    }

    protected BeanDefinitionHolder parseBeanDefinitionElement(Element ele) {
        String id = ele.getAttribute(ID_ATTRIBUTE);
        String nameAttr = ele.getAttribute(NAME_ATTRIBUTE);

        List aliases = new ArrayList();
        if (StringUtils.hasLength(nameAttr)) {
            String[] nameArr = StringUtils.tokenizeToStringArray(nameAttr, BEAN_NAME_DELIMITERS);
            aliases.addAll(Arrays.asList(nameArr));
        }

        String beanName = id;
        if (StringUtils.hasText(beanName) && !aliases.isEmpty()) {
            beanName = (String) aliases.remove(0);
        }

        BeanDefinition beanDefinition = parseBeanDefinitionElement(ele, beanName);

        String[] aliasesArray = (String[]) aliases.toArray(new String[aliases.size()]);
        return new BeanDefinitionHolder(beanDefinition, beanName, aliasesArray);
    }

    private BeanDefinition parseBeanDefinitionElement(Element ele, String beanName) {
        String className = null;
        if (ele.hasAttribute(CLASS_ATTRIBUTE)) {
            className = ele.getAttribute(CLASS_ATTRIBUTE);
        }
        String parent = null;
        if (ele.hasAttribute(PARENT_ATTRIBUTE)) {
            parent = ele.getAttribute(PARENT_ATTRIBUTE);
        }


//            ConstructorArgumentValues cargs = parseConstructorArgElements(ele, beanName);
            MutablePropertyValues pvs = parsePropertyElements(ele, beanName);

            BeanDefinition bd = BeanDefinitionReaderUtils.createBeanDefinition(
                    className, parent, null, pvs);

            if (ele.hasAttribute(DEPENDS_ON_ATTRIBUTE)) {
                String dependsOn = ele.getAttribute(DEPENDS_ON_ATTRIBUTE);
                bd.setDependsOn(StringUtils.tokenizeToStringArray(dependsOn, BEAN_NAME_DELIMITERS));
            }

            if (ele.hasAttribute(FACTORY_METHOD_ATTRIBUTE)) {
                bd.setFactoryMethodName(ele.getAttribute(FACTORY_METHOD_ATTRIBUTE));
            }
            if (ele.hasAttribute(FACTORY_BEAN_ATTRIBUTE)) {
                bd.setFactoryBeanName(ele.getAttribute(FACTORY_BEAN_ATTRIBUTE));
            }

            String dependencyCheck = ele.getAttribute(DEPENDENCY_CHECK_ATTRIBUTE);
            if (DEFAULT_VALUE.equals(dependencyCheck)) {
                dependencyCheck = this.defaultDependencyCheck;
            }
            //bd.setDependencyCheck(getDependencyCheck(dependencyCheck));

            String autowire = ele.getAttribute(AUTOWIRE_ATTRIBUTE);
            if (DEFAULT_VALUE.equals(autowire)) {
                autowire = this.defaultAutowire;
            }
            //bd.setAutowireMode(getAutowireMode(autowire));

            /*String initMethodName = ele.getAttribute(INIT_METHOD_ATTRIBUTE);
            if (!initMethodName.equals("")) {
                bd.setInitMethodName(initMethodName);
            }
            String destroyMethodName = ele.getAttribute(DESTROY_METHOD_ATTRIBUTE);
            if (!destroyMethodName.equals("")) {
                bd.setDestroyMethodName(destroyMethodName);
            }*/

            // TODO parseLookupOverrideSubElements(ele, beanName, bd.getMethodOverrides());
            // TODO parseReplacedMethodSubElements(ele, beanName, bd.getMethodOverrides());

            //bd.setResourceDescription(this.resource.getDescription());

            if (ele.hasAttribute(ABSTRACT_ATTRIBUTE)) {
            //    bd.setAbstract(TRUE_VALUE.equals(ele.getAttribute(ABSTRACT_ATTRIBUTE)));
            }

            if (ele.hasAttribute(SINGLETON_ATTRIBUTE) ) {
                bd.setSingleton(TRUE_VALUE.equals(ele.getAttribute(SINGLETON_ATTRIBUTE)));
            }

            String lazyInit = ele.getAttribute(LAZY_INIT_ATTRIBUTE);
            if (DEFAULT_VALUE.equals(lazyInit) && bd.isSingleton()) {
                // Just apply default to singletons, as lazy-init has no meaning for prototypes.
                lazyInit = this.defaultLazyInit;
            }
            bd.setLazyInit(TRUE_VALUE.equals(lazyInit));

            return bd;

    }

    private MutablePropertyValues parsePropertyElements(Element beanEle, String beanName) {
        NodeList nl = beanEle.getChildNodes();
        MutablePropertyValues pvs = new MutablePropertyValues();
        for (int i = 0; i < nl.getLength(); i++) {
            Node node = nl.item(i);
            if (node instanceof Element && PROPERTY_ELEMENT.equals(node.getNodeName())) {
                parsePropertyElement((Element) node, beanName, pvs);
            }
        }
        return pvs;
    }

    protected void parsePropertyElement(Element ele, String beanName, MutablePropertyValues pvs)
             {

        String propertyName = ele.getAttribute(NAME_ATTRIBUTE);
        if (!StringUtils.hasLength(propertyName)) {
            throw new RuntimeException(
                    this.resource + ", " + beanName + "Tag 'property' must have a 'name' attribute");
        }
        if (pvs.contains(propertyName)) {
            throw new RuntimeException(
                    this.resource + ", " + beanName +  "Multiple 'property' definitions for property '" + propertyName + "'");
        }
        Object val = parsePropertyValue(ele, beanName, propertyName);
        pvs.addPropertyValue(propertyName, val);
    }

    protected Object parsePropertyValue(Element ele, String beanName, String propertyName)
             {
        String elementName = (propertyName != null) ?
                "<property> element for property '" + propertyName + "'" :
                "<constructor-arg> element";

        // Should only have one child element: ref, value, list, etc.
        NodeList nl = ele.getChildNodes();
        Element subElement = null;
        for (int i = 0; i < nl.getLength(); i++) {
            if (nl.item(i) instanceof Element) {
                Element candidateEle = (Element) nl.item(i);
                if (DESCRIPTION_ELEMENT.equals(candidateEle.getTagName())) {
                    // Keep going: we don't use this value for now.
                }
                else {
                    // Child element is what we're looking for.
                    if (subElement != null) {
                        throw new RuntimeException(
                                this.resource + beanName + elementName + " must not contain more than one sub-element");
                    }
                    subElement = candidateEle;
                }
            }
        }

        boolean hasRefAttribute = ele.hasAttribute(REF_ATTRIBUTE);
        boolean hasValueAttribute = ele.hasAttribute(VALUE_ATTRIBUTE);
        if ((hasRefAttribute && hasValueAttribute) ||
                ((hasRefAttribute || hasValueAttribute)) && subElement != null) {
            throw new RuntimeException(
                    this.resource + beanName + elementName +
                    " is only allowed to contain either a 'ref' attribute OR a 'value' attribute OR a sub-element");
        }
        if (hasRefAttribute) {
            return new RuntimeException(ele.getAttribute(REF_ATTRIBUTE));
        }
        else if (hasValueAttribute) {
            return ele.getAttribute(VALUE_ATTRIBUTE);
        }

        if (subElement == null) {
            // Neither child element nor "ref" or "value" attribute found.
            throw new RuntimeException(
                    this.resource + beanName + elementName + " must specify a ref or value");
        }

        return parsePropertySubElement(subElement, beanName);
    }

    protected Object parsePropertySubElement(Element ele, String beanName) {
        if (ele.getTagName().equals(BEAN_ELEMENT)) {
            return parseBeanDefinitionElement(ele);
        }
        /*
        else if (ele.getTagName().equals(REF_ELEMENT)) {
            // A generic reference to any name of any bean.
            String beanRef = ele.getAttribute(BEAN_REF_ATTRIBUTE);
            if (!StringUtils.hasLength(beanRef)) {
                // A reference to the id of another bean in the same XML file.
                beanRef = ele.getAttribute(LOCAL_REF_ATTRIBUTE);
                if (!StringUtils.hasLength(beanRef)) {
                    // A reference to the id of another bean in a parent context.
                    beanRef = ele.getAttribute(PARENT_REF_ATTRIBUTE);
                    if (!StringUtils.hasLength(beanRef)) {
                        throw new RuntimeException(
                                this.resource + beanName + "'bean', 'local' or 'parent' is required for a reference");
                    }
                    return new RuntimeBeanReference(beanRef, true);
                }
            }
            return new RuntimeBeanReference(beanRef);
        }
        else if (ele.getTagName().equals(IDREF_ELEMENT)) {
            // A generic reference to any name of any bean.
            String beanRef = ele.getAttribute(BEAN_REF_ATTRIBUTE);
            if (!StringUtils.hasLength(beanRef)) {
                // A reference to the id of another bean in the same XML file.
                beanRef = ele.getAttribute(LOCAL_REF_ATTRIBUTE);
                if (!StringUtils.hasLength(beanRef)) {
                    throw new RuntimeException(
                            this.resource + beanName + "Either 'bean' or 'local' is required for an idref");
                }
            }
            return beanRef;
        }
        else if (ele.getTagName().equals(VALUE_ELEMENT)) {
            // It's a literal value.
            String value = DomUtils.getTextValue(ele);
            if (ele.hasAttribute(TYPE_ATTRIBUTE)) {
                String typeClassName = ele.getAttribute(TYPE_ATTRIBUTE);
                try {
                    Class typeClass = ClassUtils.forName(typeClassName, this.beanDefinitionReader.getBeanClassLoader());
                    return new TypedStringValue(value, typeClass);
                }
                catch (ClassNotFoundException ex) {
                    throw new RuntimeException(
                            this.resource + beanName + "Value type class [" + typeClassName + "] not found", ex);
                }
            }
            return value;
        }
        else if (ele.getTagName().equals(NULL_ELEMENT)) {
            // It's a distinguished null value.
            return null;
        }
        else if (ele.getTagName().equals(LIST_ELEMENT)) {
            return parseListElement(ele, beanName);
        }
        else if (ele.getTagName().equals(SET_ELEMENT)) {
            return parseSetElement(ele, beanName);
        }
        else if (ele.getTagName().equals(MAP_ELEMENT)) {
            return parseMapElement(ele, beanName);
        }
        else if (ele.getTagName().equals(PROPS_ELEMENT)) {
            return parsePropsElement(ele, beanName);
        }*/
        throw new RuntimeException(
                this.resource + beanName + " Unknown property sub-element: <" + ele.getTagName() + ">");
    }
}
