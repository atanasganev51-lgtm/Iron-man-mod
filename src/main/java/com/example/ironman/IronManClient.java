package com.example.ironman;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.event.client.ClientTickCallback;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.Vec3d;

public class IronManClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ClientTickCallback.EVENT.register(client -> {
			ClientPlayerEntity p = client.player;
			if (p == null || client.isPaused()) return;

			MarkArmorMaterial suit = IronManMod.getFullSuit(p);
			if (suit == null || !suit.canFly()) return;

			boolean up = client.options.keyJump.isPressed();
			boolean down = client.options.keySneak.isPressed();
			if (!up && !down) return;
			if (p.onGround && !up) return;

			double speed = suit.getFlightSpeed();
			Vec3d v = p.getVelocity();
			double vy = v.y;
			double vx = v.x;
			double vz = v.z;

			if (up) {
				vy = Math.min(vy + speed * 2.5, 0.7);
				if (client.options.keyForward.isPressed()) {
					Vec3d look = p.getRotationVector();
					vx += look.x * speed;
					vz += look.z * speed;
				}
				p.fallDistance = 0;
				p.world.addParticle(ParticleTypes.FLAME, p.x, p.y + 0.1, p.z, 0, -0.1, 0);
			} else if (down) {
				vy = Math.max(vy - speed * 2.5, -0.5);
			}
			p.setVelocity(vx, vy, vz);
		});
	}
}
