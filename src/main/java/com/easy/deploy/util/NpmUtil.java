package com.easy.deploy.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author tanyongpeng
 * <p>des</p>
 **/
public class NpmUtil {

    public static void runExecution(String vuePath, String npmCommand) throws IOException, InterruptedException {
        String cmdPrompt = "cmd";
        String path = "/c";
        File jsFile = new File(vuePath);
        List<String> updateCommand = new ArrayList<>();
        updateCommand.add(cmdPrompt);
        updateCommand.add(path);
        updateCommand.add(npmCommand);
        runExecution(updateCommand, jsFile);
    }

    private static void runExecution(List<String> command, File navigatePath) throws IOException, InterruptedException {
        LogUtil.info(command.toString());
        ProcessBuilder executeProcess = new ProcessBuilder(command);
        executeProcess.directory(navigatePath);
        Process resultExecution = executeProcess.start();
        BufferedReader br = new BufferedReader(new InputStreamReader(resultExecution.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line).append(System.getProperty("line.separator"));
            LogUtil.info(sb.toString());
        }
        br.close();
        LogUtil.info("Result of Execution" + (resultExecution.waitFor() == 0 ? "\tSuccess" : "\tFailure"));
    }

}
