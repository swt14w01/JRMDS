package test;

import static org.junit.Assert.*;
import jrmds.Application;
import jrmds.main.JrmdsManagement;
import jrmds.model.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@IntegrationTest
@Transactional
public class JrmdsManagementTest {
	@Autowired
	JrmdsManagement jrmds;
	
	@Test
	public void projectGetterTest() {
		Project p = new Project("testpro");
		jrmds.saveProject(p);		
		assertNotNull(jrmds.getProject("testpro"));
	}
	
}
