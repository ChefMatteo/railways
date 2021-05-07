package it.uniprisma.exercise4dot2.models;

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
public class Route {
    private String id;
    private String binaryType;
    private String startStationName;
    private String destinationStationName;
    private Double length;
    private List<String> trainsId = new ArrayList<>();
}
