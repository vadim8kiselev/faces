package com.kiselev.faces.filters;

import com.kiselev.faces.beans.AuthorizationBean;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthorizationFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest req,
                         ServletResponse res, FilterChain chain)
            throws ServletException, IOException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        AuthorizationBean session = (AuthorizationBean) request.getSession()
                .getAttribute("authorizationBean");

        String url = request.getRequestURI();

        if (session == null) {
            if (url.contains("logout")) {
                response.sendRedirect(request.getServletContext()
                        .getContextPath() + "/");
            } else {
                chain.doFilter(req, res);
            }
        } else if (!session.isLogged()) {
            if (!url.contains("signin") && !(url.charAt(url.length() - 1) ==
                    '/')) {
                session.setInMessage(null);
                session.setUpMessage(null);
            }
            if (url.contains("signin")) {
                session.setUpMessage(null);
            }
            if (url.charAt(url.length() - 1) == '/') {
                session.setInMessage(null);
            }
            session.setUsername(null);
            chain.doFilter(req, res);

        } else {
            if ((url.charAt(url.length() - 1) == '/') ||
                    url.contains("profile") || url.contains("signin")) {

                response.sendRedirect(request.getServletContext()
                        .getContextPath() + "/id" +
                        session.getId());
            } else {
                chain.doFilter(req, res);
            }
        }
    }

    @Override
    public void destroy() {

    }
}
