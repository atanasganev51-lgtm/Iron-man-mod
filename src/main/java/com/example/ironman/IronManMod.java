package com.example.ironman;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class IronManMod implements ModInitializer {
	public static final String MOD_ID = "ironman";
	public static final int MARKS = 85;

	private static final EquipmentSlot[] SLOTS = {
		EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET
	};
	private static final String[] NAMES = {"helmet", "chestplate", "leggings", "boots"};

	public static final ItemGroup GROUP = FabricItemGroupBuilder.build(
		new Identifier(MOD_ID, "suits"),
		() -> new ItemStack(Registry.ITEM.get(new Identifier(MOD_ID, "mark_1_helmet")))
	);

	@Override
	public void onInitialize() {
		for (int mark = 1; mark <= MARKS; mark++) {
			MarkArmorMaterial material = new MarkArmorMaterial(mark);
			for (int i = 0; i < 4; i++) {
				Registry.register(
					Registry.ITEM,
					new Identifier(MOD_ID, "mark_" + mark + "_" + NAMES[i]),
					new ArmorItem(material, SLOTS[i], new Item.Settings().group(GROUP).maxCount(1))
				);
			}
		}
	}

	/** Returns the suit material if all four pieces are the same Mark, else null. */
	public static MarkArmorMaterial getFullSuit(PlayerEntity player) {
		MarkArmorMaterial found = null;
		for (EquipmentSlot slot : SLOTS) {
			Item item = player.getEquippedStack(slot).getItem();
			if (!(item instanceof ArmorItem)) return null;
			Object mat = ((ArmorItem) item).getMaterial();
			if (!(mat instanceof MarkArmorMaterial)) return null;
			MarkArmorMaterial m = (MarkArmorMaterial) mat;
			if (found != null && found.getMark() != m.getMark()) return null;
			found = m;
		}
		return found;
	}
}
