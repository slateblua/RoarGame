package com.slateblua.roargame.gamecomponent.pet;

import com.slateblua.roargame.core.systems.Event;
import lombok.Getter;
import lombok.Setter;

public class PetChangedEvent extends Event {
    @Getter @Setter
    private PetData.PetId newPet;
}
