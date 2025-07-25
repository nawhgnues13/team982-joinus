package com.team982.joinus.domain.member.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.team982.joinus.domain.member.dao.MemberDao;
import com.team982.joinus.domain.member.model.MemberDto;
import com.team982.joinus.global.common.handler.CommandHandler;

public class LoginPostHandler implements CommandHandler {

	@Override
	public String process(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		String email = (String) request.getParameter("email");
		System.out.println("email: " + email);
		String password = (String) request.getParameter("password");
		System.out.println("password: " + password);
		MemberDao memberDao = new MemberDao();
		try {
			MemberDto member = new MemberDto();
			member.setEmail(email);
			member.setPassword(password);
			int memberId = memberDao.login(member);
			System.out.println(memberId);
			if (memberId > -1) {
				session.setAttribute("memberid", memberId);
				return "redirect:/";
			} else {
				return "redirect:/home.do?error=1";
			}
		} catch (Exception e) {
			session.invalidate();
			request.setAttribute("message", "오류가 발생하였습니다!");
			return "error/error.jsp";
		}
	}

}
