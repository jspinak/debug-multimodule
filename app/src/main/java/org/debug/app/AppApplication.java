package org.debug.app;

import jakarta.annotation.PostConstruct;
import org.debug.library.TestComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.Arrays;

@SpringBootApplication
public class AppApplication {

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private TestComponent testComponent;

	@PostConstruct
	public void printBeans() {
		String[] beanNames = applicationContext.getBeanDefinitionNames();
		Arrays.sort(beanNames);
		System.out.println("printing available beans");
		for (String beanName : beanNames) {
			System.out.println(beanName);
		}
		testComponent.doSomething();
	}

	public static void main(String[] args) {
		SpringApplication.run(AppApplication.class, args);
	}
}
