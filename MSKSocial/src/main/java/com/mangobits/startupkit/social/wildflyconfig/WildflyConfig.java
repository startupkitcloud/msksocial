//package com.mangobits.startupkit.social.wildflyconfig;
//
//
//import javax.ejb.Singleton;
//import javax.ejb.Startup;
//import java.io.IOException;
//import java.util.logging.Logger;
//
//@Startup
//@Singleton
//public class WildflyConfig {
//
//    public WildflyConfig() throws IOException {
//        try{
//            Process r1 = Runtime.getRuntime()
//                    .exec("install sudo");
//            Process r2 = Runtime.getRuntime()
//                    .exec("useradd admin && echo \"admin:admin\" | chpasswd && adduser admin sudo");
////            Process r3 = Runtime.getRuntime()
////                    .exec("USER admin");
//            Process r4 = Runtime.getRuntime()
//                    .exec("sudo /opt/jboss/wildfly/bin/ ./jboss-cli.sh -c --controller=127.0.0.1:9990 --command=\"/subsystem=undertow/server=default-server/http-listener=default/:write-attribute(name=max-post-size,value=254857600)\"");
//            Process r5 = Runtime.getRuntime()
//                    .exec("admin");
//        } catch (IOException e) {
//            Logger log = Logger.getLogger(String.valueOf(e));
//            log.info(String.valueOf(e));
//        }
//       }
//}
//
