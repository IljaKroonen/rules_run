package com.github.iljakroonen.rules.run;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.auth.Credentials;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class CloudRunClient {
    private static final ObjectMapper mapper = new ObjectMapper();

    private final HttpRequestFactory requestFactory = new NetHttpTransport().createRequestFactory();
    private final Credentials credentials;
    private final String region;

    CloudRunClient(
            Credentials credentials,
            String region
    ) {
        this.credentials = credentials;
        this.region = region;
    }

    /**
     * Deploy a publicly available service. This will create the service, or update it if it already exists.
     *
     * @return The URL of the service.
     */
    String deployService(
            String projectId,
            String serviceName,
            String image,
            Map<String, String> env,
            String memory,
            int concurrency,
            Duration timeout
    ) throws IOException, InterruptedException, TimeoutException {
        CreateServiceResult createServiceResult = createService(
                projectId,
                serviceName,
                image,
                env,
                memory,
                concurrency
        );

        if (createServiceResult == CreateServiceResult.ALREADY_EXISTS) {
            updateService(
                    projectId,
                    serviceName,
                    image,
                    env,
                    memory,
                    concurrency
            );
        }

        setIamPolicy(
                projectId,
                serviceName
        );

        return waitForUrl(
                projectId,
                serviceName,
                timeout
        );
    }

    private String waitForUrl(
            String projectId,
            String serviceName,
            Duration timeout
    ) throws InterruptedException, IOException, TimeoutException {
        Instant startTs = Instant.now();
        while (startTs.plus(timeout).isAfter(Instant.now())) {
            Thread.sleep(500);

            String describeServiceUri = "https://" + region + "-run.googleapis.com/apis/serving.knative.dev/v1/namespaces/" + projectId + "/services/" + serviceName;
            HttpRequest describeServiceRequest = configureRequest(requestFactory.buildGetRequest(new GenericUrl(describeServiceUri)));

            HttpResponse describeServiceResponse = describeServiceRequest.execute();

            JsonNode responseJson = mapper.readTree(describeServiceResponse.getContent());
            JsonNode status = responseJson.get("status");
            JsonNode conditions = status.get("conditions");
            for (JsonNode condition: conditions) {
                if (condition.get("type").asText().equals("Ready") && condition.get("status").asText().equals("True")) {
                    return status.get("url").asText();
                }
            }
        }

        throw new TimeoutException("Timed out waiting for service URL to be available");
    }

    private void setIamPolicy(
            String projectId,
            String serviceName
    ) throws IOException {
        String setIamUri = "https://" + region + "-run.googleapis.com/v1/projects/" + projectId + "/locations/" + region + "/services/" + serviceName + ":setIamPolicy";
        HttpRequest setIamRequest = configureRequest(requestFactory.buildPostRequest(new GenericUrl(setIamUri), new ByteArrayContent(
                "application/json",
                buildIamPolicyBody()
        )));
        setIamRequest.execute();
    }

    private void updateService(
            String projectId,
            String serviceName,
            String image,
            Map<String, String> env,
            String memory,
            int concurrency
    ) throws IOException {
        String updateUri = "https://" + region + "-run.googleapis.com/apis/serving.knative.dev/v1/namespaces/" + projectId + "/services/" + serviceName;

        HttpRequest updateRequest = configureRequest(requestFactory.buildPutRequest(new GenericUrl(updateUri), new ByteArrayContent(
                "application/json",
                buildServiceBody(serviceName, image, env, memory, concurrency)
        )));
        updateRequest.execute();
    }

    private enum CreateServiceResult {
        ALREADY_EXISTS,
        CREATED
    }

    private CreateServiceResult createService(
            String projectId,
            String serviceName,
            String image,
            Map<String, String> env,
            String memory,
            int concurrency
    ) throws IOException {
        String createUri = "https://" + region + "-run.googleapis.com/apis/serving.knative.dev/v1/namespaces/" + projectId + "/services";

        HttpRequest createRequest = configureRequest(requestFactory.buildPostRequest(new GenericUrl(createUri), new ByteArrayContent(
                "application/json",
                buildServiceBody(serviceName, image, env, memory, concurrency)
        )));

        try {
            createRequest.execute();
        } catch (HttpResponseException e) {
            if (e.getStatusCode() == 409) {
                return CreateServiceResult.ALREADY_EXISTS;
            }

            throw e;
        }

        return CreateServiceResult.CREATED;
    }

    private HttpRequest configureRequest(HttpRequest request) throws IOException {
        return request.setHeaders(
                new HttpHeaders().setAuthorization(credentials.getRequestMetadata().get("Authorization")).set("accept", "application/json")
        );
    }

    private static byte[] buildServiceBody(
            String serviceName,
            String image,
            Map<String, String> env,
            String memory,
            int concurrency
    ) throws IOException {
        ObjectNode node = mapper.createObjectNode();

        node.set("apiVersion", new TextNode("serving.knative.dev/v1"));
        node.set("kind", new TextNode("Service"));

        ObjectNode metadataNode = mapper.createObjectNode();
        node.set("metadata", metadataNode);
        metadataNode.set("name", new TextNode(serviceName));

        ObjectNode specNode = mapper.createObjectNode();
        node.set("spec", specNode);

        ObjectNode specTemplateNode = mapper.createObjectNode();
        specNode.set("template", specTemplateNode);

        ObjectNode specTemplateSpecNode = mapper.createObjectNode();
        specTemplateNode.set("spec", specTemplateSpecNode);
        specTemplateSpecNode.set("containerConcurrency", new IntNode(concurrency));

        ArrayNode containersNode = mapper.createArrayNode();
        specTemplateSpecNode.set("containers", containersNode);

        ObjectNode containerNode = mapper.createObjectNode();
        containersNode.add(containerNode);
        containerNode.set("image", new TextNode(image));

        ArrayNode envNode = mapper.createArrayNode();
        containerNode.set("env", envNode);

        for (Map.Entry<String, String> entry : env.entrySet()) {
            ObjectNode kv = mapper.createObjectNode();
            envNode.add(kv);
            kv.set("name", new TextNode(entry.getKey()));
            kv.set("value", new TextNode(entry.getValue()));
        }

        ObjectNode resourcesNode = mapper.createObjectNode();
        containerNode.set("resources", resourcesNode);

        ObjectNode limitsNode = mapper.createObjectNode();
        resourcesNode.set("limits", limitsNode);
        limitsNode.set("memory", new TextNode(memory));

        return mapper.writeValueAsBytes(node);
    }

    private static byte[] buildIamPolicyBody() throws IOException {
        ObjectNode node = mapper.createObjectNode();

        ObjectNode policyNode = mapper.createObjectNode();
        node.set("policy", policyNode);

        ArrayNode bindingsNode = mapper.createArrayNode();
        policyNode.set("bindings", bindingsNode);

        ObjectNode bindingNode = mapper.createObjectNode();
        bindingsNode.add(bindingNode);
        bindingNode.set("role", new TextNode("roles/run.invoker"));

        ArrayNode membersNode = mapper.createArrayNode();
        bindingNode.set("members", membersNode);
        membersNode.add(new TextNode("allUsers"));

        return mapper.writeValueAsBytes(node);
    }
}
