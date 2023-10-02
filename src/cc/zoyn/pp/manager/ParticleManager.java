package cc.zoyn.pp.manager;

import java.util.List;
import java.util.Map;

import com.darkblade12.particleeffect.ParticleEffect;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class ParticleManager {

	private static Map<String, ParticleEffect> playerSelectParticle = Maps.newHashMap();
	private static List<ParticleCalculator> calculators = Lists.newArrayList();

	public static ParticleCalculator getPlayerCalc(String playerName) {
		for (ParticleCalculator calc : calculators) {
			if (calc.getOwnerName().equalsIgnoreCase(playerName)) {
				return calc;
			}
		}
		return null;
	}

	public static void setPlayerParticle(String playerName, ParticleEffect particle) {
		playerSelectParticle.put(playerName, particle);
	}
	
	public static ParticleEffect getPlayerParticle(String playerName) {
		return playerSelectParticle.getOrDefault(playerName, ParticleEffect.FIREWORKS_SPARK);
	}
	
	public static List<ParticleCalculator> getCalculators() {
		return calculators;
	}
	
	public static Map<String, ParticleEffect> getPlayerSelectParticle() {
		return playerSelectParticle;
	}

	public static void addParticle(ParticleCalculator calculator) {
		if (calculator == null) {
			return;
		}
		calculators.add(calculator);
	}
	
	public static void turnOffAllParticle() {
		getCalculators().forEach(calc -> {
			calc.turnOff();
		});
	}
}
