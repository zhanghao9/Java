## 1.日志注解
@Target(ElementType.METHOD) //注解放置的目标位置,METHOD是可注解在方法级别上
@Retention(RetentionPolicy.RUNTIME) //注解在哪个阶段执行
@Documented //生成文档
public @interface LogAnnotation {
    String value() default "";
}

## 2.日志bean
public class SysLog implements Serializable {
	private Long id;
	private String userName; // 用户名
	private String operation; // 操作
	private String method; // 方法名
	private String ip; // ip地址

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date createDateTime; // 操作时间
}

## 3.切面处理
@Aspect
@Component
public class SysLogAspect {
    @Resource
    private SysLogService sysLogService;

    //定义切点 @Pointcut
    //在注解的位置切入代码
    @Pointcut("@annotation(LogAnnotation)")
    public void logPoinCut() {
    }

    //切面 配置通知
    @AfterReturning("logPoinCut()")
    public void saveSysLog(JoinPoint joinPoint) {
        //保存日志
        SysLog sysLog = new SysLog();

        //从切面织入点处通过反射机制获取织入点处的方法
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        //获取切入点所在的方法
        Method method = signature.getMethod();
        //获取操作
        MesLog myLog = method.getAnnotation(MesLog.class);
        if (myLog != null) {
            String value = myLog.value();
            sysLog.setOperation(value);//保存获取的操作
        }

        //获取请求的类名
        String className = joinPoint.getTarget().getClass().getName();
        //获取请求的方法名
        String methodName = method.getName();
        sysLog.setMethod(className + "." + methodName);
        sysLog.setCreateDateTime(new Date());
        //获取用户名
        sysLog.setUserName(SecurityContextUtil.getCurrentUser().getUserCode());
        //获取用户ip地址
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        sysLog.setIp(RequestUtils.getRealIpAddr(request));
        //保存
        sysLogService.save(sysLog);
    }
}

## 4.在需要操作的方法上添加注解
@LogAnnotation("数据导出")
public void test(){}
