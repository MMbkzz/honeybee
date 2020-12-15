package com.stackstech.honeybee.connector.kafka;

import com.stackstech.honeybee.connector.core.AbstractPoolSession;
import com.stackstech.honeybee.connector.core.DriverPoolFactory;
import com.stackstech.honeybee.connector.core.ResourceSession;
import com.stackstech.honeybee.connector.core.entity.DriverMessageModel;
import com.stackstech.honeybee.connector.core.entity.DriverMetaData;
import com.stackstech.honeybee.connector.core.entity.DriverModel;
import com.stackstech.honeybee.connector.core.enums.MetaDataTypeEnum;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.*;

public class KafkaSession extends AbstractPoolSession implements ResourceSession {

    private static final Logger logger = LoggerFactory.getLogger(KafkaSession.class);


    private KafkaTemplate kafkaTemplate;

    ConsumerConfig consumerConfig = null;
    private ProducerFactory producerFactory = null;

    Map<String, Object> config = null;

    /**
     * 消息者缓存
     */
    private static final Map<String, KafkaConsumer> consumerCache = new HashMap<>();

    private KafkaSession(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public static KafkaSession getInstance(ClassLoader classLoader) {
        return new KafkaSession(classLoader);
    }

    public KafkaSession initialize(Map<String, Object> config) {
        pool = new GenericObjectPool(new DriverPoolFactory());
        this.config = config;

        pool.setMaxTotal(Integer.valueOf((String) config.get("kafka.datasource.max-total")));
        pool.setMaxIdle(Integer.valueOf((String) config.get("kafka.datasource.max-idle")));
        pool.setMinIdle(Integer.valueOf((String) config.get("kafka.datasource.min-idle")));
        Map<String, Object> producerConfig = producerConfigs(config);
        if (config.get("kafka.datasource.maxwait") != null) {
            pool.setMaxWaitMillis(Integer.valueOf((String) config.get("kafka.datasource.maxwait")) * 1000);
        }
        producerFactory = new DefaultKafkaProducerFactory<>(producerConfig);

        kafkaTemplate = new KafkaTemplate<String, String>(producerFactory, true); //初始化生产者句柄

        //       consumer = new KafkaConsumer<>(consumerConfigs(config)); //初始化消费者句柄
        return this;
    }

    @Override
    public List<Map<String, Object>> get(String topic) {
        return null;
    }

    @Override
    public DriverMetaData get(DriverModel driverModel) {
        DriverMessageModel messageModel = (DriverMessageModel) driverModel;
        System.out.println("kafka test=====" + messageModel.getConsumerId());
        KafkaConsumer kafkaConsumer = getConsumer(messageModel.getConsumerId());

        String topic = driverModel.getExpression();
        List<Map<String, Object>> list = new ArrayList<>();
        Object borrow = null;
        try {
            borrow = pool.borrowObject();
            kafkaConsumer.subscribe(Collections.singletonList(topic));
            ConsumerRecords<String, String> records = kafkaConsumer.poll(Long.MAX_VALUE);

            for (ConsumerRecord<String, String> record : records) {
                Map<String, Object> data = new HashMap<>();
                data.put("partition", record.partition());
                data.put("offset", record.offset());
                data.put("value", record.value());
                list.add(data);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            pool.returnObject(borrow);
        }
        return new DriverMetaData(MetaDataTypeEnum.DATA, list);
    }

    public void sent(String topicName, String message) {
        kafkaTemplate.setDefaultTopic(topicName);
        kafkaTemplate.send(topicName, message);
    }

    @Override
    public DriverMetaData put(DriverModel driverModel) {

        DriverMessageModel model = (DriverMessageModel) driverModel;

        Map<String, Object> messageMap = (Map<String, Object>) model.getRequestData();
        String topic = model.getExpression();

        Object borrow = null;
        try {
            borrow = pool.borrowObject();
            kafkaTemplate.send(topic, messageMap.get(topic));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            pool.returnObject(borrow);
        }

        return new DriverMetaData(MetaDataTypeEnum.NOSQL, "发送数据至" + topic + "成功");
    }

    @Override
    public boolean valid() {
        return true;
    }

    @Override
    public void close() {
    }

    protected Map<String, Object> producerConfigs(Map<String, Object> config) {

        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, config.get("kafka.datasource.producer.servers"));
        props.put(ProducerConfig.RETRIES_CONFIG, config.get("kafka.datasource.producer.retries"));
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, config.get("kafka.datasource.batch.size"));
        props.put(ProducerConfig.LINGER_MS_CONFIG, config.get("kafka.datasource.producer.linger"));
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, config.get("kafka.datasource.producer.buffer.memory"));
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        //开启kerbos验证
        if (config.get("kafka.security.protocol") != null) {
            props.put("security.protocol", config.get("kafka.security.protocol"));
            props.put("sasl.mechanism", config.get("kafka.datasource.mechanism"));
        }
        return props;
    }

    protected Properties consumerConfigs(Map<String, Object> config, String consumerId) {

        Properties props = new Properties();
        props.put("bootstrap.servers", config.get("kafka.datasource.consumer.bootstrap.servers"));
        //props.put("group.id", config.get("kafka.datasource.consumer.group.id"));
        props.put("group.id", consumerId);
        props.put("auto.offset.reset", config.get("kafka.datasource.consumer.auto.offset.reset"));
        props.put("session.timeout.ms", config.get("kafka.datasource.consumer.session.timeout.ms"));
        props.put("auto.commit.interval.ms", config.get("kafka.datasource.consumer.auto.commit.interval.ms"));
        props.put("key.deserializer", StringDeserializer.class.getName());
        props.put("value.deserializer", StringDeserializer.class.getName());

        //开启kerbos验证
        if (config.get("kafka.security.protocol") != null) {
            props.put("security.protocol", config.get("kafka.security.protocol"));
            props.put("sasl.mechanism", config.get("kafka.security.mechanism"));
        }
        return props;
    }

    /**
     * 获取消费者
     *
     * @param consumerId
     * @return
     */
    protected KafkaConsumer getConsumer(String consumerId) {
        KafkaConsumer kafkaConsumer = consumerCache.get(consumerId);

        if (kafkaConsumer != null) {

            return kafkaConsumer;
        }
        kafkaConsumer = new KafkaConsumer<>(consumerConfigs(config, consumerId)); //初始化消费者句柄
        consumerCache.put(consumerId, kafkaConsumer);

        return kafkaConsumer;

    }
}
