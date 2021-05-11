package it.uniprisma.exercise4dot2.models.wagon;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class PassengerWagon extends Wagon {
    private int nSeats;

    public PassengerWagon(AllOfWagon w) {
        super(w.getId(), w.getTrainId(), w.getWagonClass(), w.getLength(), w.getWidth(), w.getHeight(), w.getBathroom(), w.getWagonType());
        if(trainId==null){
            super.trainId="disconnected";
        }
        this.nSeats = w.getNSeats();
    }

}
