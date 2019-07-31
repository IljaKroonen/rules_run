package com.github.iljakroonen.rules.run;

import com.google.auth.oauth2.GoogleCredentials;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.*;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Deploy {
    private static final int SERVICE_AVAILABLE_TIMEOUT_SECONDS = 120;

    public static void main(String[] args) throws Exception {
        ArgumentParser parser = ArgumentParsers.newFor("Deploy").build()
                .defaultHelp(true)
                .description("Script for deploying a Google Cloud Run service");
        parser.addArgument("--region")
                .required(true)
                .help("Specify the compute region in which to deploy the service (for example: europe-west1)");
        parser.addArgument("--projectId")
                .required(true)
                .help("Google Cloud project ID");
        parser.addArgument("--serviceName")
                .required(true)
                .help("Name of the service to be deployed");
        parser.addArgument("--image")
                .required(true)
                .help("Docker image to be deployed");
        parser.addArgument("--env")
                .required(true)
                .help("Environment variables, in the form VAR_NAME_1=value1,VAR_NAME_2=value2");
        parser.addArgument("--memory")
                .required(true)
                .help("Memory limit for the service (for example 1GiB)");
        parser.addArgument("--concurrency")
                .required(true)
                .type(Integer.class)
                .help("Concurrency of the service");
        parser.addArgument("--urlFile")
                .required(true)
                .help("Path to a file where the URL of the service will be written to");

        Namespace ns;
        try {
            ns = parser.parseArgs(args);
        } catch (ArgumentParserException e) {
            parser.handleError(e);
            System.exit(1);
            return;
        }

        String region = ns.get("region");
        String projectId = ns.get("projectId");
        String serviceName = ns.get("serviceName");
        String image = ns.get("image");
        Map<String, String> env = parseArgs(ns.get("env"));
        String memory = ns.get("memory");
        int concurrency = ns.getInt("concurrency");
        String urlFile = ns.get("urlFile");

        CloudRunClient client = new CloudRunClient(
                GoogleCredentials.getApplicationDefault().createScoped("https://www.googleapis.com/auth/cloud-platform"),
                region
        );

        String url = client.deployService(
                projectId,
                serviceName,
                image,
                env,
                memory,
                concurrency,
                Duration.ofSeconds(SERVICE_AVAILABLE_TIMEOUT_SECONDS)
        );

        try (OutputStream out = new FileOutputStream(urlFile)) {
            out.write(url.getBytes(StandardCharsets.UTF_8));
        }
    }

    private static Map<String, String> parseArgs(String value) {
        if (value.isEmpty()) {
            return Collections.emptyMap();
        }

        Map<String, String> ret = new HashMap<>();

        String[] pairs = value.split(",");

        for (String pair : pairs) {
            String[] spl = pair.split("=", 2);

            ret.put(spl[0], spl[1]);
        }

        return ret;
    }
}
