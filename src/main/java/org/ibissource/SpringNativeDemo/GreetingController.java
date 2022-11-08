package org.ibissource.SpringNativeDemo;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import nl.nn.testtool.TestTool;

@RestController
public class GreetingController {
	@Autowired
	TestTool testTool;

	private static final String template = "Hello, %s!";
	private final AtomicLong counter = new AtomicLong();

	@GetMapping("/greeting")
	public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
		testTool.startpoint("test1", this.getClass().getName(), "test", "mijn begin bericht");
        testTool.startpoint("test1", this.getClass().getName(), "test", "mijn begin bericht");
        testTool.endpoint("test1", this.getClass().getName(), "test", "mijn eind bericht");
        testTool.endpoint("test1", this.getClass().getName(), "test", "mijn eind bericht");
		return new Greeting(counter.incrementAndGet(), String.format(template, name));
	}
}