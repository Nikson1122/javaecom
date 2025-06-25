/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.milan.sessionBeans;
import javax.annotation.security.PermitAll;
import javax.ejb.LocalBean;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;

@Singleton
@Startup
@LocalBean
public class ShedularBean {

    private String lastExecutionTime;
    @PermitAll

    @Schedule(minute = "*/1", hour = "*", persistent = false)
    public void runEveryMinute() {
        lastExecutionTime = java.time.LocalTime.now().toString();
        System.out.println("Task executed at: " + lastExecutionTime);
    }
    @PermitAll

    public String getLastExecutionTime() {
        return lastExecutionTime;
    }
}
