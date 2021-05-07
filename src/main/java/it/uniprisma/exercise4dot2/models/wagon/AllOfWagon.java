package it.uniprisma.exercise4dot2.models.wagon;

import it.uniprisma.exercise4dot2.models.wagon.enums.FuelType;
import it.uniprisma.exercise4dot2.models.wagon.enums.WagonClass;
import it.uniprisma.exercise4dot2.models.wagon.enums.WagonType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AllOfWagon {
    private String id;
    private String trainId;
    private WagonClass wagonClass;
    private WagonType wagonType;
    private double length;
    private double width;
    private double height;
    private Boolean bathroom;
    private FuelType fuelType;
    private int nSeats;
    private String kitchenType;
    private int nTables;
    private String bedType;
    private int nBeds;
}
