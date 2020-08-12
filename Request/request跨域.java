class CrossOriginFilter extends Filter {

        @Override
        public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
            HttpServletResponse res = (HttpServletResponse) servletResponse;
            HttpServletRequest request=(HttpServletRequest)servletRequest;
            res.setContentType("text/html;charset=UTF-8");
            res.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
            res.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE,PUT");
            res.setHeader("Access-Control-Max-Age", "0");
            res.setHeader("Access-Control-Allow-Headers", "Origin, No-Cache, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With,userId,token");
            res.setHeader("Access-Control-Allow-Credentials", "true");
            res.setHeader("XDomainRequestAllowed","1");
            filterChain.doFilter(servletRequest,servletResponse);
        }

    }
