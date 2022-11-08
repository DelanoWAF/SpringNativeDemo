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

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import nextapp.echo2.app.ApplicationInstance;
import nextapp.echo2.webcontainer.WebContainerServlet;
import nl.nn.testtool.echo2.Echo2Application;

/**
 * @author Jaco de Groot
 */
public class Servlet extends WebContainerServlet {
	private static final long serialVersionUID = 1L;
	private WebApplicationContext webApplicationContext;
	ClassPathXmlApplicationContext applicationContext;

	@Override
	public void init(ServletConfig servletConfig) throws ServletException {
		super.init(servletConfig);
		webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletConfig.getServletContext());
	}

	@Override
	public ApplicationInstance newApplicationInstance() {
		if (webApplicationContext == null) {
			// Fall back to ClassPathXmlApplicationContext depending on the
			// presents of ContextLoaderListener in web.xml
			applicationContext = new ClassPathXmlApplicationContext("springTestToolTestWebapp.xml");
			return (Echo2Application)applicationContext.getBean("echo2Application");
		} else {
			return (Echo2Application)webApplicationContext.getBean("echo2Application");
		}
	}

	@Override
	public void destroy() {
		if (applicationContext != null) {
			applicationContext.close();
		}
		super.destroy();
	}
}
