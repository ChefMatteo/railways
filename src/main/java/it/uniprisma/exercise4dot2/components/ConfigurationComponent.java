package it.uniprisma.exercise4dot2.components;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("uni-prisma")
@Data
public class ConfigurationComponent {
    private Integer defaultPageLimit;
    private Integer maxPageLimit;
    private String dataPath;
}
