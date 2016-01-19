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

        if (session == null || !session.isLogged) {
            if (url.contains("profile") || url.contains("logout")) {
                response.sendRedirect(request.getServletContext()
                        .getContextPath() + "/signin");
            } else {
                chain.doFilter(req, res);
            }
        } else {
            if (url.contains("signup") || url.contains("signin")) {
                response.sendRedirect(request.getServletContext()
                        .getContextPath() + "/profile");

            } else if (url.contains("logout")) {
                request.getSession(false).removeAttribute("authorizationBean");
                request.getSession(false).invalidate();
                response.sendRedirect(request.getServletContext()
                        .getContextPath() + "/");

            } else {
                chain.doFilter(req, res);
            }
        }
    }

    @Override
    public void destroy() {

    }
}
