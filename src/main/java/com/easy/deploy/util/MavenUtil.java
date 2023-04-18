package com.easy.deploy.util;

import org.apache.maven.shared.invoker.*;

import java.io.File;
import java.util.Collections;

/**
 * @author tanyongpeng
 * <p>des</p>
 **/
public class MavenUtil {


    public static void mvn(String mavenPath, String pomXmlPath, String exec) {
        InvocationRequest request = new DefaultInvocationRequest();
        request.setPomFile(new File(pomXmlPath));
        request.setGoals(Collections.singletonList(exec));
        Invoker invoker = new DefaultInvoker();
        invoker.setMavenHome(new File(mavenPath));
        invoker.setLogger(new PrintStreamLogger(System.err, InvokerLogger.ERROR) {
        });
        invoker.setOutputHandler(System.out::println);
        try {
            invoker.execute(request);
        } catch (MavenInvocationException e) {
            e.printStackTrace();
        }
    }

}
