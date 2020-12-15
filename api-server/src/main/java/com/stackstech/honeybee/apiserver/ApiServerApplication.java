package com.stackstech.honeybee.apiserver;

import org.apache.commons.cli.ParseException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient
@EnableAutoConfiguration
@ComponentScan({"com.stackstech.dcp"})
public class ApiServerApplication {

//    @Autowired
//    private ApplicationConfig applicationConfig;
//
//    @Bean("resourceSessionManager")
//    public ResourceSessionManager get(ApplicationConfig applicationConfig) {
//        return new ResourceSessionManager(applicationConfig.getUpload());
//    }


    public static void main(String[] args) throws ParseException {

        SpringApplication.run(ApiServerApplication.class, args);

//        Options options = new Options();
//        options.addOption("h", "help", false, "Print this usage information.");
//        options.addOption("s", "start", false, "Start server.");
//        options.addOption("t", "stop", false, "Stop server.");
//
//        CommandLineParser parser = new DefaultParser();
//        CommandLine cmd = parser.parse(options, args);
//        HelpFormatter help = null;
//
//        if (cmd.hasOption("h") || cmd.getArgs().length == 0) {
//            help = new HelpFormatter();
//            help.setWidth(100);
//            help.printHelp(ApiServerApplication.class.getName(), options, true);
//            System.exit(0);
//        }
//        if (cmd.hasOption("s")) {
//            SpringApplication.run(ApiServerApplication.class, args);
//        }
//        if (cmd.hasOption("t")) {
//            //TODO stop server
//        }


    }
}
