package it.uniprisma.exercise4dot2.models.wagon;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class RestaurantWagon extends Wagon {
    private String kitchenType;
    private int nTables;

    public RestaurantWagon(AllOfWagon w) {
        super(w.getId(), w.getTrainId(), w.getWagonClass(), w.getLength(), w.getWidth(), w.getHeight(), w.getBathroom(), w.getWagonType());
        if(trainId==null){
            super.trainId="disconnected";
        }
        this.kitchenType = w.getKitchenType();
        this.nTables = w.getNTables();
    }
}
