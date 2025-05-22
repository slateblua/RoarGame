package com.slateblua.roargame.scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Scaling;
import com.slateblua.roargame.*;
import com.slateblua.roargame.pet.PetChangedEvent;
import com.slateblua.roargame.pet.PetData;
import com.slateblua.roargame.scenes.components.*;
import com.slateblua.roargame.scenes.pages.BasePage;
import com.slateblua.roargame.systems.EventSystem;

public class PetsPage extends BasePage {

    private PetData selectedPet;
    private PetTable selectedPetTable;

    public PetsPage () {
        initDialogBorder();
    }

    @Override
    protected void constructContent (Table content) {
        content.pad(130, 90, 40, 90);
        int i = 0;

        final Table wrapper = new Table();

        for (final PetData pet : Locator.get(GameData.class).getPets()) {
            final PetTable petTable = new PetTable();
            petTable.setData(pet);
            i++;

            petTable.setOnClick(() -> {
                if (selectedPetTable != null) {
                    selectedPetTable.deselect();
                }

                petTable.select();
                selectedPetTable = petTable;

                selectedPet = pet;
            });

            if (i % 3 == 1) {
                wrapper.row();
            }

            wrapper.add(petTable).size(200, 200).pad(10);
        }

        content.add(wrapper);

        final OffsetButton selectButton = new OffsetButton(Style.GREEN_PASTEL_35_15);

        content.row();
        content.add(selectButton).size(300, 120).spaceTop(40);

        final Label actor = new Label("Ընտրել", Locator.get(Resources.class).getLabelStyle());
        actor.setColor(Color.valueOf("#f4e7de"));
        selectButton.getFrontTable().add(actor);

        selectButton.setOnClick(() -> {
            selectPet(selectedPet);
            hide();
        });
    }

    public void selectPet (PetData pet) {
        final EventSystem eventSystem = Locator.get(EventSystem.class);
        final PetChangedEvent event = eventSystem.obtain(PetChangedEvent.class);
        event.setNewPet(pet.getPetId());

        eventSystem.fire(event);
    }

    public static class PetTable extends BorderedTable {
        private final Image petImage;

        private final Table selectedOverlay;

        public PetTable () {
            pad(20);
            setBorderDrawable(Shape.ROUNDED_30_BORDER.getDrawable(Color.valueOf("#B49F8C")));
            setBackground(Shape.ROUNDED_30.getDrawable(Color.valueOf("#e5ad4b")));

            petImage = new Image();
            petImage.setScaling(Scaling.fit);
            add(petImage).grow();

            selectedOverlay = new Table();
            selectedOverlay.setFillParent(true);
            selectedOverlay.pad(-10);
            selectedOverlay.setBackground(Shape.ROUNDED_30_BORDER.getDrawable(Color.valueOf("#7ed97b")));
        }

        public void select () {
            addActor(selectedOverlay);
        }

        public void deselect () {
            removeActor(selectedOverlay);
        }

        public void setData (final PetData petData) {
            petImage.setDrawable(petData.getDrawable());
        }
    }
}
