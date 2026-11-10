package com.example.ironman;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;

public class MarkArmorMaterial implements ArmorMaterial {
	private static final int[] BASE_DURABILITY = {13, 15, 16, 11};
	private static final int[] BASE_PROTECTION = {2, 6, 5, 2};

	private final int mark;

	public MarkArmorMaterial(int mark) {
		this.mark = mark;
	}

	public int getMark() {
		return mark;
	}

	/** Mark 1 is the cave-built suit: no flight. */
	public boolean canFly() {
		return mark != 1;
	}

	/** Thrust per tick, scales gently with the Mark number. */
	public double getFlightSpeed() {
		return 0.04 + Math.min(mark, 85) * 0.0008;
	}

	@Override
	public int getDurability(EquipmentSlot slot) {
		return BASE_DURABILITY[slot.getEntitySlotId()] * (20 + mark / 3);
	}

	@Override
	public int getProtectionAmount(EquipmentSlot slot) {
		return BASE_PROTECTION[slot.getEntitySlotId()] + (mark >= 40 ? 1 : 0);
	}

	@Override
	public int getEnchantability() {
		return 15;
	}

	@Override
	public SoundEvent getEquipSound() {
		return SoundEvents.ITEM_ARMOR_EQUIP_IRON;
	}

	@Override
	public Ingredient getRepairIngredient() {
		return Ingredient.ofItems(Items.IRON_INGOT);
	}

	@Override
	@Environment(EnvType.CLIENT)
	public String getName() {
		// 1.14 looks this up in the "minecraft" namespace:
		// assets/minecraft/textures/models/armor/<name>_layer_1.png
		return "ironman_mark_" + mark;
	}

	@Override
	public float getToughness() {
		return mark >= 40 ? 2.0F : 1.0F;
	}
}
