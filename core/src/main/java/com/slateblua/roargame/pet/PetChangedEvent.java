package com.slateblua.roargame.pet;

import com.slateblua.roargame.systems.EventSystem;
import lombok.Getter;
import lombok.Setter;

public class PetChangedEvent extends EventSystem.Event {
    @Getter @Setter
    private PetData.PetId newPet;
}
