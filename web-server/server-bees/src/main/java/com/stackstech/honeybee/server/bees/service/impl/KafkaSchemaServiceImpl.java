package com.stackstech.honeybee.server.bees.service.impl;

import com.stackstech.honeybee.server.bees.service.KafkaSchemaService;
import io.confluent.kafka.schemaregistry.client.rest.entities.Config;
import io.confluent.kafka.schemaregistry.client.rest.entities.Schema;
import io.confluent.kafka.schemaregistry.client.rest.entities.SchemaString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Service
public class KafkaSchemaServiceImpl implements KafkaSchemaService {

    private static final Logger log = LoggerFactory
            .getLogger(KafkaSchemaServiceImpl.class);

    @Value("${kafka.schema.registry.url}")
    private String url;

    RestTemplate restTemplate = new RestTemplate();

    private String registryUrl(final String path) {
        if (StringUtils.hasText(path)) {
            String usePath = path;
            if (!path.startsWith("/")) {
                usePath = "/" + path;
            }
            return this.url + usePath;
        }
        return "";
    }

    @Override
    public SchemaString getSchemaString(Integer id) {
        String path = "/schemas/ids/" + id;
        String regUrl = registryUrl(path);
        ResponseEntity<SchemaString> res = restTemplate.getForEntity(regUrl,
                SchemaString.class);
        SchemaString result = res.getBody();
        return result;
    }

    @Override
    public Iterable<String> getSubjects() {
        String path = "/subjects";
        String regUrl = registryUrl(path);
        ResponseEntity<String[]> res = restTemplate.getForEntity(regUrl,
                String[].class);
        Iterable<String> result = Arrays.asList(res.getBody());
        return result;
    }

    @Override
    public Iterable<Integer> getSubjectVersions(String subject) {
        String path = "/subjects/" + subject + "/versions";
        String regUrl = registryUrl(path);
        ResponseEntity<Integer[]> res = restTemplate.getForEntity(regUrl,
                Integer[].class);
        Iterable<Integer> result = Arrays.asList(res.getBody());
        return result;
    }

    @Override
    public Schema getSubjectSchema(String subject, String version) {
        String path = "/subjects/" + subject + "/versions/" + version;
        String regUrl = registryUrl(path);
        ResponseEntity<Schema> res = restTemplate.getForEntity(regUrl,
                Schema.class);
        Schema result = res.getBody();
        return result;
    }

    @Override
    public Config getTopLevelConfig() {
        String path = "/config";
        String regUrl = registryUrl(path);
        ResponseEntity<Config> res = restTemplate.getForEntity(regUrl,
                Config.class);
        Config result = res.getBody();
        return result;
    }

    @Override
    public Config getSubjectLevelConfig(String subject) {
        String path = "/config/" + subject;
        String regUrl = registryUrl(path);
        ResponseEntity<Config> res = restTemplate.getForEntity(regUrl,
                Config.class);
        Config result = res.getBody();
        return result;
    }
}
