获取ip:
private ipAddress getRemoteAddrIp(HttpServletRequest request) {
    String value = request.getHeader("X-Real-IP"); 
    if ((!StringUtils.isEmpty(value)) && !"unknown".equalsIgnoreCase(value)){
	   return value;
    }else{
        return request.getRemoteAddr();
   }
 }
  
  ------------------------------------------------------------------------
   public static String getRealIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        return ip;
    }
