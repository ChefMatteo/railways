package it.uniprisma.exercise4dot2.models.train;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Train {
    private String id;
    private TrainType trainType;
    private String companyName;
    private List<String> routesId = new ArrayList<>();
}
