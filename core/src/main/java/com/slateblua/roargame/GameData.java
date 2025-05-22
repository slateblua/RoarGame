package com.slateblua.roargame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.XmlReader;
import com.slateblua.roargame.bonus.BonusData;
import com.slateblua.roargame.pet.PetData;
import com.slateblua.roargame.weapon.Weapon;
import com.slateblua.roargame.weapon.WeaponData;
import lombok.Getter;

public class GameData {
    private final XmlReader reader = new XmlReader();

    @Getter
    private final Array<PetData> pets = new Array<>();

    @Getter
    private final ObjectMap<PetData.PetId, PetData> petMap = new ObjectMap<>();

    @Getter
    private final Array<BonusData> bonuses = new Array<>();
    @Getter
    private final Array<WeaponData> weapons = new Array<>();

    @Getter
    private final ObjectMap<WeaponData, Weapon> weaponMap = new ObjectMap<>();

    public GameData () {
        loadWeapons();
        loadPets();
    }

    private static final String WEAPON_TAG = "weapon";
    private static final String PET_TAG = "pet";

    private void loadWeapons () {
        final FileHandle handle = Gdx.files.internal("data/weapons.xml");
        final XmlReader.Element root = reader.parse(handle);

        final Array<XmlReader.Element> weaponsXml = root.getChildrenByName(WEAPON_TAG);

        for (final XmlReader.Element weapon : weaponsXml) {
            final WeaponData weaponData = new WeaponData(weapon);
            weapons.add(weaponData);

            weaponMap.put(weaponData, Weapon.fromData(weaponData));
        }
    }

    private void loadPets () {
        final FileHandle handle = Gdx.files.internal("data/pets.xml");
        final XmlReader.Element root = reader.parse(handle);

        final Array<XmlReader.Element> petsXml = root.getChildrenByName(PET_TAG);

        for (final XmlReader.Element pet : petsXml) {
            final PetData petData = new PetData(pet);
            pets.add(petData);
            petMap.put(petData.getPetId(), petData);
        }
    }
}
