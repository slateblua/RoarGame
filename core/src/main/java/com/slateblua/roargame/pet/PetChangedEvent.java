package com.slateblua.roargame.pet;

import com.slateblua.roargame.systems.Event;
import lombok.Getter;
import lombok.Setter;

public class PetChangedEvent extends Event {
    @Getter @Setter
    private PetData.PetId newPet;
}
