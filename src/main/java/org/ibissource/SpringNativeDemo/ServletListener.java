/*
   Copyright 2021 WeAreFrank!

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
package org.ibissource.SpringNativeDemo;


import java.lang.invoke.MethodHandles;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.webjars.servlet.WebjarsServlet;

import nl.nn.testtool.web.ApiServlet;
import nl.nn.testtool.web.FrontendServlet;

/**
 * One of several methods to add the Ladybug servlets to an application. For this method to work in Tomcat make sure
 * the Ladybug jar is enabled for jar scanning (see jarsToSkip and jarsToScan in catalina.properties) or explicitly add
 * the listener to the web.xml
 */
@WebListener
public class ServletListener implements ServletContextListener {
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		ServletContext context = servletContextEvent.getServletContext();

		String name = "LadybugBackendServlet";
		String mapping = ApiServlet.getDefaultMapping();
		logger.info("Registering servlet with name [" + name + "] with mapping [" + mapping + "]");
		ServletRegistration.Dynamic servletRegistration = context.addServlet(name, ApiServlet.class);
		servletRegistration.setLoadOnStartup(0);
		servletRegistration.addMapping(mapping);
		servletRegistration.setInitParameters(ApiServlet.getDefaultInitParameters());
		context.log("Finished registering servlet with name [" + name + "] with mapping [" + mapping + "]");

		name = "WebjarsServlet";
		logger.info("Registering servlet with name [" + name + "]");
		servletRegistration = context.addServlet(name, WebjarsServlet.class);
		servletRegistration.setLoadOnStartup(0);
		servletRegistration.addMapping("/webjars/*");
		context.log("Finished registering servlet with name [" + name + "]");

		name = "LadybugFrontendServlet";
		mapping = FrontendServlet.getDefaultMapping();
		logger.info("Registering servlet with name [" + name + "] with mapping [" + mapping + "]");
		servletRegistration = context.addServlet(name, FrontendServlet.class);
		servletRegistration.setLoadOnStartup(1);
		servletRegistration.addMapping(mapping);
		context.log("Finished registering servlet with name [" + name + "] with mapping [" + mapping + "]");
	}

	@Override
	public void contextDestroyed(ServletContextEvent servletContextEvent) {
	}
}
