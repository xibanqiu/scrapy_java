package com.github.job;

import com.github.task.BaseTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Profile("prod")
@Component
@EnableScheduling
public class SpiderJob {
    protected final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private BeanFactory factory;

    @Scheduled(fixedDelay = 3 * 60 * 60 * 1000)
//    @Scheduled(cron = "5 * * * * ? ")
    public void task() {
		String spider = Optional.ofNullable(System.getProperty("spider")).orElse("").toLowerCase();

		try {
			BaseTask task = getTask(spider);
			Objects.requireNonNull(task).start();
		}catch (Exception e){
			LOG.error("spider = {},task error:",spider, e);
		}

	}

	private BaseTask getTask(String spider){
		BaseTask task = null;
		try {
			if (spider.endsWith("incr")) {
				String name = spider.substring(0, spider.length() - 4) + "Task";
				task = (BaseTask) factory.getBean(name,1);
			} else {
				String name = spider + "Task";
				task = (BaseTask) factory.getBean(name);
			}
		} catch (Exception e) {
			throw new IllegalArgumentException("can't find this spider",e);
		}
		return task;
	}
}
