package base;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

/**
 * 
 */

/**
 * @author manan
 *
 */
@ContextConfiguration(locations="/resources/spring/applicationContext-base.xml")
public class BaseSpringTest extends AbstractJUnit4SpringContextTests{
	
	@BeforeClass
	public static void setUpBeforeClass(){
		
	}
	
	@Test
	public void test(){
		
	}

}
