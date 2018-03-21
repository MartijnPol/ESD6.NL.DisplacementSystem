package interceptor;

import service.LogService;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

/**
 * @author Thom van de Pas on 14-3-2018
 */
@Interceptor
@Logging
public class OverloadRequestsLoggingInterceptor {

    @Inject
    private LogService logService;

    @AroundInvoke
    public Object log(InvocationContext invocationContext) throws Exception {
        String name = invocationContext.getMethod().getName();

        System.out.println("The method " + name + " has reached an overload, there were too many requests!");

        logService.logOverloadRequestError(name);
        return invocationContext.proceed();
    }
}
