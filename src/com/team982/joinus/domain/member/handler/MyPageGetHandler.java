package com.team982.joinus.domain.member.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.team982.joinus.domain.member.dao.MemberDao;
import com.team982.joinus.global.common.handler.CommandHandler;

public class MyPageGetHandler implements CommandHandler {

	@Override
	public String process(HttpServletRequest request, HttpServletResponse response) {
		MemberDao memberDao = new MemberDao();
		HttpSession session = request.getSession();
//		int memberId = Integer.parseInt((String) session.getAttribute("memberid"));
//		String memberName = memberDao.getMemberNameById(memberId);
		String memberName = memberDao.getMemberNameById(1);
		if (!memberName.isEmpty()) {
			request.setAttribute("membername", memberName);
			return "member/mypage.jsp";
		} else {
			return "redirect:/home.do";
		}
	}

}
