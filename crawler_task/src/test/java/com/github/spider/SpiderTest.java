package com.github.spider;

import com.github.task.UnsplashTask;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

@SpringBootTest
@RunWith(SpringRunner.class)
public class SpiderTest {

	@Autowired
	private BeanFactory factory;
	@Test
	public void test() throws InterruptedException {

	factory.getBean(UnsplashTask.class,1).start();
		//huabanTask.start();
		TimeUnit.MINUTES.sleep(10);
	}


}