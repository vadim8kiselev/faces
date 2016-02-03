package com.kiselev.faces.filters;

import com.kiselev.faces.beans.AuthorizationBean;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthenticationFilter implements javax.servlet.Filter {

    HttpServletRequest request = null;
    HttpServletResponse response = null;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest req,
                         ServletResponse res, FilterChain chain)
            throws ServletException, IOException {

        request = (HttpServletRequest) req;
        response = (HttpServletResponse) res;

        AuthorizationBean session = (AuthorizationBean) request.getSession()
                .getAttribute("authorizationBean");

        String url = request.getRequestURI();
        boolean isRootURL = (url.charAt(url.length() - 1) == '/');

        if (url.contains("resource") || url.contains("favicon")) {
            chain.doFilter(req, res);
        }

        if (session == null) {
            if (url.contains("settings") || url.contains("register")) {
                redirect("/signin");
            } else {
                chain.doFilter(req, res);
            }

        } else if (!session.isLogged()) {

            if (!url.contains("signin") && !isRootURL) {
                session.setInMessage(null);
                session.setUpMessage(null);

                if (url.contains("settings") || url.contains("register")) {
                    redirect("/signin");
                }
            } else if (url.contains("signin")) {
                session.setUpMessage(null);
                session.setUsername(null);
            } else if (isRootURL) {
                session.setInMessage(null);
                session.setUsername(null);
            }

            chain.doFilter(req, res);

        } else {
            if (!url.contains("/register") && !session.isRegistered()) {
                redirect("/register");

            } else if (url.contains("/register") && !session.isRegistered()) {
                chain.doFilter(req, res);

            } else if (session.isRegistered()) {
                if (isRootURL || url.contains("signin")) {
                    redirect("/id" + session.getId());

                } else {
                    chain.doFilter(req, res);
                }
            }
        }
    }

    private void redirect(String url) throws IOException {
        response.sendRedirect(request.getContextPath() + url);
    }

    @Override
    public void destroy() {

    }
}
