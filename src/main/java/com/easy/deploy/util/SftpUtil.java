package com.easy.deploy.util;


import cn.hutool.core.util.StrUtil;
import com.easy.deploy.vo.DeployPath;
import com.easy.util.DESUtil;
import com.jcraft.jsch.*;
import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.common.IOUtils;
import net.schmizz.sshj.connection.channel.direct.Session;
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;

import java.io.*;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;

/**
 * @author tanyongpeng
 * <p>des</p>
 **/
public class SftpUtil {


    private static final ThreadLocal<SSHClient> sshClientThreadLocal = new ThreadLocal<>();

    public static SSHClient getSSHClient() {
        SSHClient sshClient = sshClientThreadLocal.get();
        if (sshClient == null) {
            sshClient = new SSHClient();
            sshClientThreadLocal.set(sshClient);
        }
        return sshClient;
    }

    public static void clearSSHClient() {
        sshClientThreadLocal.remove();
    }

    public static Set<String> exec(String host, int port, String username, String password, String e, Set<String> resultArray) throws IOException {
        final SSHClient ssh = new SSHClient();
        ssh.addHostKeyVerifier(new PromiscuousVerifier());
        ssh.connect(host, port);
        InputStream inputStream = null;
        OutputStream output = null;
        ssh.authPassword(username, DESUtil.decrypt(password));
        try (Session session = ssh.startSession()) {
            final Session.Command cmd = session.exec(e);
            inputStream = cmd.getInputStream();
            output = IOUtils.readFully(inputStream);
            if (!output.toString().trim().equals("")) {
                return CharUtil.getProcessNumber(output.toString(), resultArray);
            } else {
                return resultArray;
            }
        } catch (Exception e1) {
            return resultArray;
        } finally {
            // 处理异常
            ssh.disconnect();
            if (inputStream != null) {
                inputStream.close();
            }
            if (output != null) {
                output.close();
            }
        }
    }



    public static void exec(String host, int port, String username, String password, String e,Integer type) throws IOException {
        final SSHClient sshClient = getSSHClient();
        if (type == 1){
            sshClient.addHostKeyVerifier(new PromiscuousVerifier());
            if (sshClient.isConnected()) {
                sshClient.disconnect();
            }
            sshClient.connect(host, port);
            sshClient.authPassword(username, DESUtil.decrypt(password));
            try (Session session = sshClient.startSession()) {
                session.exec(e);
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            } finally {
                sshClient.disconnect();
            }
        }else {
            try (Session session = getSSHClient().startSession()) {
                session.exec(e);
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            } finally {
                sshClient.disconnect();
            }

        }
    }



    public static void delete(String host, int port, String username, String password, String servicePath, String fileName) {
        ChannelSftp sftp = null;
        com.jcraft.jsch.Session session = null;
        try {
            JSch jsch = new JSch();
            session = jsch.getSession(username, host, port);
            session.setPassword(DESUtil.decrypt(password));
            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.connect();
            Channel channel = session.openChannel("sftp");
            channel.connect();
            sftp = (ChannelSftp) channel;
            sftp.cd(servicePath);
            sftp.rm(fileName);
        } catch (JSchException | SftpException e) {
            System.out.println(e.getMessage());
        } finally {
            if (sftp != null) {
                if (sftp.isConnected()) {
                    sftp.disconnect();
                }
            }
            if (session != null) {
                if (session.isConnected()) {
                    session.disconnect();
                }
            }
        }
    }

    public static void upload(String host, int port, String username, String password, String serverPath, String localPath, String connectId) {
        ChannelSftp sftp = null;
        com.jcraft.jsch.Session session = null;
        try (FileInputStream fileInputStream = new FileInputStream(new File(localPath))) {
            JSch jsch = new JSch();
            session = jsch.getSession(username, host, port);
            session.setPassword(DESUtil.decrypt(password));
            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.connect();
            Channel channel = session.openChannel("sftp");
            channel.connect();
            sftp = (ChannelSftp) channel;
            LogUtil.info("文件：" + serverPath, connectId, host);
            sftp.put(fileInputStream, serverPath);
        } catch (JSchException | SftpException | IOException e) {
            e.printStackTrace();
        } finally {
            if (sftp != null) {
                if (sftp.isConnected()) {
                    sftp.disconnect();
                }
            }
            if (session != null) {
                if (session.isConnected()) {
                    session.disconnect();
                }
            }
        }
    }

    public static void uploadFiles(String host, int port, String username, String password,String connectId,
                                   String localDir, String remoteDir){
        ChannelSftp sftp = null;
        Channel channel = null;
        com.jcraft.jsch.Session session = null;
        try {
            // 创建JSch对象
            JSch jsch = new JSch();
            // 连接远程服务器
            session = jsch.getSession(username, host, port);
            session.setPassword(DESUtil.decrypt(password));
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();
            // 获取SFTP通道
            channel = session.openChannel("sftp");
            channel.connect();
            sftp = (ChannelSftp) channel;
            // 如果远程目录不存在，则创建目录
            try {
                sftp.stat(remoteDir);
            } catch (SftpException e) {
                if (e.id == ChannelSftp.SSH_FX_NO_SUCH_FILE) {
                    sftp.mkdir(remoteDir);
                }
            }
            // 递归上传文件
            File localFile = new File(localDir);
            if (localFile.isDirectory()) {
                uploadDirectory(sftp, localFile, remoteDir,connectId,host);
            } else {
                uploadFile(sftp, localFile, remoteDir);
            }
        }catch (JSchException|SftpException e){
            e.printStackTrace();
        }finally {
            // 关闭SFTP通道和SSH连接
            if (sftp != null){
                sftp.exit();
            }
            if (channel != null){
                channel.disconnect();
            }
            if (session != null){
                session.disconnect();
            }
        }

    }

    private static void uploadDirectory(ChannelSftp sftp, File localDir, String remoteDir,String connectId,String host) throws SftpException {
        // 如果远程目录不存在，则创建目录
        try {
            sftp.stat(remoteDir);
        } catch (SftpException e) {
            if (e.id == ChannelSftp.SSH_FX_NO_SUCH_FILE) {
                sftp.mkdir(remoteDir);
            }
        }
        // 递归上传目录下的文件和子目录
        File[] files = localDir.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                String name = StrUtil.isBlank(file.getName())?"正在上传根目录文件":"上传文件索引 "+file.getName()+"...";
                LogUtil.info(name,connectId,host);
                String remoteSubDir = remoteDir + "/" + file.getName();
                uploadDirectory(sftp, file, remoteSubDir,connectId,host);
            } else {
                uploadFile(sftp, file, remoteDir);
            }

        }
    }

    private static void uploadFile(ChannelSftp sftp, File localFile, String remoteDir) throws SftpException {
        sftp.put(localFile.getAbsolutePath(), remoteDir + "/" + localFile.getName());
    }

}
