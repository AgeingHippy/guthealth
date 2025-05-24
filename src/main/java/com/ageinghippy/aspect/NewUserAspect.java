package com.ageinghippy.aspect;

import com.ageinghippy.model.entity.UserPrinciple;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class NewUserAspect {

    @Pointcut("execution(* com.ageinghippy.service.UserPrincipleService.create*User(..))")
    private void pointCutCreateAnyUser() {}

    @AfterReturning(pointcut = "pointCutCreateAnyUser()", returning="result")
    public void welcomeNewUser(Object result) {
        //todo - send user welcome message with usage details
        //for now we simply log the new user as created
        UserPrinciple principle;

        StringBuilder message = new StringBuilder("### NEW USER ###");

        if (result instanceof UserPrinciple) {
            principle = (UserPrinciple)  result;
            message.append(" New User: ")
                    .append(principle.getUsername())
                    .append(" Type: ")
                    .append(principle.getOauth2Provider() == null ? "UsernamePassword" : "Oauth2");
        } else {
            message.append(" *********** NO USER RETURNED ***********");
        }

        log.info(message.toString());
    }

}
