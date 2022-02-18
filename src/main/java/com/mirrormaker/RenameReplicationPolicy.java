package com.mirrormaker;

import org.apache.kafka.connect.mirror.DefaultReplicationPolicy;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Accepts topic mapping from replication config, otherwise sticks to the default replication policy.
 * */
public class RenameReplicationPolicy extends DefaultReplicationPolicy {

    private static final Logger log = LoggerFactory.getLogger(RenameReplicationPolicy.class);

    public static final String TOPICS_MAPPING_CONFIG = "replication.policy.topics.mapping";

    private Map<String, String> topicsMapping;

    @Override
    public void configure(Map<String, ?> props) {
        super.configure(props);

        try {
            if (props.containsKey(TOPICS_MAPPING_CONFIG)) {
                topicsMapping = new HashMap<>();
                String mapping = (String) props.get(TOPICS_MAPPING_CONFIG);
                Arrays.stream(mapping.split(";")).forEach(item -> {
                    String[] fromTo = item.split(",");
                    topicsMapping.put(fromTo[0], fromTo[1]);
                });

                log.info("Will rename the following topics during replication:\n{}",
                    String.join("\n", topicsMapping.entrySet().stream().map(kv ->
                        String.format("%s -> %s", kv.getKey(), kv.getValue())).collect(Collectors.toSet())
                    )
                );
            }
        } catch (Exception e) {
            String msg = "Failed to parse '%s' config param value, it should be in format: t1,t1x;t2,t2x;....;tN,tNx";
            log.error(String.format(msg, TOPICS_MAPPING_CONFIG), e);
        }
    }

    @Override
    public String formatRemoteTopic(String sourceClusterAlias, String topic) {
        if(topicsMapping != null && topicsMapping.containsKey(topic)) {
            return topicsMapping.get(topic);
        } else {
            return super.formatRemoteTopic(sourceClusterAlias, topic);
        }
    }

}
