package cc.zoyn.pp.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.darkblade12.particleeffect.ParticleEffect;
import com.pixelmonmod.pixelmon.api.events.PixelmonSendOutEvent;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;

import catserver.api.bukkit.event.ForgeEvent;
import cc.zoyn.pp.PokemonParticle;
import cc.zoyn.pp.manager.ParticleCalculator;
import cc.zoyn.pp.manager.ParticleManager;
import net.minecraft.nbt.NBTTagCompound;

public class PokemonListener implements Listener {

	@EventHandler
	public void onPixelmonSendOut(ForgeEvent event) {
		if (event.getForgeEvent() instanceof PixelmonSendOutEvent) {
			PixelmonSendOutEvent e = (PixelmonSendOutEvent) event.getForgeEvent();
			if (e.pokemon == null) {
				return;
			}
			// 判断六V
			if (!isSixV(e.pokemon)) {
				return;
			}
			Bukkit.getScheduler().runTaskLater(PokemonParticle.getInstance(), () -> {
				Entity livingEntity = Bukkit.getEntity(e.pokemon.getUUID());
				
				// 收回宝可梦
				if (livingEntity == null) {
					return;
				}
				Player player = e.player.getBukkitEntity();
				ParticleCalculator calc = ParticleManager.getPlayerCalc(player.getName());
				if (calc != null) {
					return;
				}
				ParticleEffect playerSelect = ParticleManager.getPlayerSelectParticle().getOrDefault(player.getName(),
						ParticleEffect.FIREWORKS_SPARK);

				ParticleCalculator calculator = new ParticleCalculator(player.getName(), playerSelect, livingEntity);
				// 添加至正在执行列表
				ParticleManager.addParticle(calculator);
				calculator.turnOn();
			}, 5 * 10L);
		}
	}

	public boolean isSixV(Pokemon pokemon) {
		for (int data : pokemon.getIVs().getArray()) {
			if (data != 31) {
				return false;
			}
		}
		return true;
	}
}
