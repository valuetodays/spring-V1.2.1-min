package test.billy.springmin;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import test.billy.springmin.bean.PersonBean;

/**
 * @author lei.liu@datatist.com
 * @since 2018-07-25 17:10:31
 */
public class SpringFirstTest {

    @Test
    public void runForIoc() {
        String[] locations = new String[] {"classpath:/spring/applicationContext-run.xml"};
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(locations);
//        System.out.println(context);

        PersonBean person = (PersonBean)context.getBean("personBean", PersonBean.class);
        System.out.println(person);
        context.close();
    }
}
