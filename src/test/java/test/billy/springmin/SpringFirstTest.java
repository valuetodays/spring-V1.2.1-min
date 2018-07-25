package test.billy.springmin;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author lei.liu@datatist.com
 * @since 2018-07-25 17:10:31
 */
public class SpringFirstTest {

    @Test
    public void run() {
        String[] locations = new String[] {"classpath:/spring/applicationContext-run.xml"};
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(locations);
        // TODO do it
        System.out.println(context);
/*
        PersonBean person = (PersonBean)context.getBean("personBean", PersonBean.class);
        System.out.println(person);
        context.close();*/
    }
}
