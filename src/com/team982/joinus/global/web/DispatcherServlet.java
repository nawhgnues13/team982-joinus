package com.team982.joinus.global.web;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.team982.joinus.global.common.handler.CommandHandler;
import com.team982.joinus.global.common.handler.InvalidActionHandler;

public class DispatcherServlet extends HttpServlet {
private static final long serialVersionUID = 1L;
	
	private Map<String, CommandHandler> commandHandlerMap = new HashMap<>();
	
	public void init() throws ServletException {
		String configFile = getInitParameter("configFile");
		String configFilePath = getServletContext().getRealPath(configFile);
		Properties prop = new Properties();
		try (FileReader reader = new FileReader(configFilePath)) {
			prop.load(reader);
		} catch (IOException e) {
			throw new ServletException(e);
		}
		
		Iterator<?> keys = prop.keySet().iterator();
		while (keys.hasNext()) {
			String uri = (String) keys.next();
			String handlerClassName = prop.getProperty(uri);
			try {
				Class<?> handlerClass = Class.forName(handlerClassName);
				CommandHandler handlerInstance = (CommandHandler) handlerClass.getDeclaredConstructor().newInstance();
				commandHandlerMap.put(uri, handlerInstance);
			} catch (Exception e) {
				throw new ServletException(e);
			}
		}
	}
	
	private void processServlet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String command = request.getRequestURI();
		
		CommandHandler handler = commandHandlerMap.get(command);
		if (handler == null) {
			handler = new InvalidActionHandler();
		}
		
		String viewPage = null;
		try {
			viewPage = handler.process(request, response);
			if ((viewPage != null) && (viewPage.indexOf("redirect:")==0)) {
				viewPage = viewPage.substring(9);
				response.sendRedirect(viewPage);
				return;
			}
		} catch (Exception e) {
			throw new ServletException(e);
		}
		
		if (viewPage != null) {
			viewPage = "/WEB-INF/views/" + viewPage;
		} else {
			viewPage = "index.jsp";
		}
		RequestDispatcher disp = request.getRequestDispatcher(viewPage);
		disp.forward(request, response);
	}
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processServlet(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processServlet(request, response);
	}

}
