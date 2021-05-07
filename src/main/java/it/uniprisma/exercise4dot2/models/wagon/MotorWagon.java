package it.uniprisma.exercise4dot2.models.wagon;

import it.uniprisma.exercise4dot2.models.wagon.enums.FuelType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class MotorWagon extends Wagon {
    private FuelType fuelType;

    public MotorWagon(AllOfWagon w) {
        super(w.getId(), w.getTrainId(), w.getWagonClass(), w.getLength(), w.getWidth(), w.getHeight(), w.getBathroom(), w.getWagonType());
        if(trainId==null){
            super.trainId="disconnected";
        }
        this.fuelType = w.getFuelType();
    }

}
