package it.uniprisma.exercise4dot2.models.wagon;

import it.uniprisma.exercise4dot2.models.wagon.enums.WagonClass;
import it.uniprisma.exercise4dot2.models.wagon.enums.WagonType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Wagon wagon = (Wagon) o;
        return Objects.equals(id, wagon.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
