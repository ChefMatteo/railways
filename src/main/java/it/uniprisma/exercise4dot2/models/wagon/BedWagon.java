package it.uniprisma.exercise4dot2.models.wagon;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class BedWagon extends Wagon {
    private String bedType;
    private int nBeds;

    public BedWagon(AllOfWagon w) {
        super(w.getId(), w.getTrainId(), w.getWagonClass(), w.getLength(), w.getWidth(), w.getHeight(), w.getBathroom(), w.getWagonType());
        if(trainId==null){
            super.trainId="disconnected";
        }
        this.bedType = w.getBedType();
        this.nBeds = w.getNBeds();
    }

}
