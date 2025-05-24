package com.ageinghippy.aspect;

import com.ageinghippy.model.entity.UserPrinciple;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class TrackSystemCopyAspect {

    @Pointcut("@annotation(TrackSystemCopy)")
    public void trackSystemCopy() {}

    @Before("trackSystemCopy()")
    public void logCopyAction(JoinPoint joinPoint) {
        StringBuilder message = new StringBuilder("### SYSTEM DATA COPY ### ");
        final Object[] args = joinPoint.getArgs();

        UserPrinciple principle =
                (UserPrinciple) Arrays.stream(args)
                        .filter(arg -> arg.getClass() == UserPrinciple.class)
                        .findFirst()
                        .orElse(null);

        if (principle != null) {
            message.append("username: ")
                    .append(principle.getUsername()).append(" ");
        }

        message.append("called: ")
                .append(joinPoint.getSignature().getName());

        log.info(message.toString());
    }
}
