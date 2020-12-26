package com.stackstech.honeybee.server.quality.controller;

import com.stackstech.honeybee.server.quality.service.impl.KafkaSchemaServiceImpl;
import io.confluent.kafka.schemaregistry.client.rest.entities.Config;
import io.confluent.kafka.schemaregistry.client.rest.entities.Schema;
import io.confluent.kafka.schemaregistry.client.rest.entities.SchemaString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/metadata/kafka")
public class KafkaSchemaController {

    @Autowired
    KafkaSchemaServiceImpl kafkaSchemaService;

    @RequestMapping(value = "/schema/{id}", method = RequestMethod.GET)
    public SchemaString getSchemaString(@PathVariable("id") Integer id) {
        return kafkaSchemaService.getSchemaString(id);
    }

    @RequestMapping(value = "/subject", method = RequestMethod.GET)
    public Iterable<String> getSubjects() {
        return kafkaSchemaService.getSubjects();
    }

    @RequestMapping(value = "/versions", method = RequestMethod.GET)
    public Iterable<Integer> getSubjectVersions(
            @RequestParam("subject") String subject) {
        return kafkaSchemaService.getSubjectVersions(subject);
    }

    @RequestMapping(value = "/subjectSchema", method = RequestMethod.GET)
    public Schema getSubjectSchema(@RequestParam("subject") String subject,
                                   @RequestParam("version") String version) {
        return kafkaSchemaService.getSubjectSchema(subject, version);
    }

    @RequestMapping(value = "/config", method = RequestMethod.GET)
    public Config getTopLevelConfig() {
        return kafkaSchemaService.getTopLevelConfig();
    }

    @RequestMapping(value = "/config/{subject}", method = RequestMethod.GET)
    public Config getSubjectLevelConfig(@PathVariable("subject") String subject) {
        return kafkaSchemaService.getSubjectLevelConfig(subject);
    }

}
