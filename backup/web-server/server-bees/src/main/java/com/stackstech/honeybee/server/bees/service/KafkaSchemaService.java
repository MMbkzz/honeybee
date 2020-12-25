package com.stackstech.honeybee.server.bees.service;

import io.confluent.kafka.schemaregistry.client.rest.entities.Config;
import io.confluent.kafka.schemaregistry.client.rest.entities.Schema;
import io.confluent.kafka.schemaregistry.client.rest.entities.SchemaString;

public interface KafkaSchemaService {
    SchemaString getSchemaString(Integer id);

    Iterable<String> getSubjects();

    Iterable<Integer> getSubjectVersions(String subject);

    Schema getSubjectSchema(String subject, String version);

    Config getTopLevelConfig();

    Config getSubjectLevelConfig(String subject);

}
