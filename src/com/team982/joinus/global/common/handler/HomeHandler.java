package com.team982.joinus.global.common.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class HomeHandler implements CommandHandler {

	@Override
	public String process(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession(false);
		String error = (String) request.getParameter("error");
		boolean loggedIn = (session != null && session.getAttribute("memberid") != null);
		if (loggedIn) {
			return "main/main.jsp";
		} else {
			request.setAttribute("error", error);
			return "index.jsp";
		}
	}

}
