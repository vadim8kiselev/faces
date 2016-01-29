package com.kiselev.faces.filters;

import com.kiselev.faces.beans.AuthorizationBean;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthenticationFilter implements javax.servlet.Filter {

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
        boolean isRootURL = (url.charAt(url.length() - 1) == '/');

        if (session == null) {
            if (url.contains("error")) {
                response.sendRedirect(request.getServletContext()
                        .getContextPath() + "/signin");
            } else {
                chain.doFilter(req, res);
            }

        } else if (!session.isLogged()) {

            if (!url.contains("signin") && !isRootURL) {
                session.setInMessage(null);
                session.setUpMessage(null);

                if (url.contains("error")) {
                    response.sendRedirect(request.getServletContext()
                            .getContextPath() + "/signin");
                }
            }
            if (url.contains("signin")) {
                session.setUpMessage(null);
            }
            if (isRootURL) {
                session.setInMessage(null);
            }
            session.setUsername(null);
            chain.doFilter(req, res);

        } else {
            if (isRootURL || url.contains("signin")) {
                response.sendRedirect(request.getServletContext()
                        .getContextPath() + "/id" + session.getId());
            } else if (url.contains("id")) {
                chain.doFilter(req, res);

            } else {
                response.sendRedirect(request.getServletContext()
                        .getContextPath() + "/error");
            }
        }
    }

    @Override
    public void destroy() {

    }
}
