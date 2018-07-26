package test.billy.springmin;

import com.billy.bean.PersonBean;
import org.junit.Test;
import org.springframework.ClassPathXmlApplicationContext;

/**
 * @author lei.liu@datatist.com
 * @since 2018-07-26 10:10:31
 */
public class SpringFirstMineTest {

    @Test
    public void runForIoc() {
        String[] xmlArr = new String[] { "classpath:/spring/beans-run.xml" };
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(xmlArr);

        PersonBean person = (PersonBean)context.getBean("personBean", PersonBean.class);
        System.out.println(person);
        PersonBean person2 = (PersonBean)context.getBean("personBean", PersonBean.class);
        System.out.println(person2);
        context.close();
    }
}
