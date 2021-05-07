package it.uniprisma.exercise4dot2.models.wagon;

import it.uniprisma.exercise4dot2.models.wagon.enums.WagonClass;
import it.uniprisma.exercise4dot2.models.wagon.enums.WagonType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class Wagon {
    protected String id;
    protected String trainId;
    protected WagonClass wagonClass;
    protected double length;
    protected double width;
    protected double height;
    protected Boolean bathroom;
    protected WagonType wagonType;
}
