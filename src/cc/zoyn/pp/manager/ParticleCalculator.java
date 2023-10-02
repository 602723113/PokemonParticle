package cc.zoyn.pp.manager;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import com.darkblade12.particleeffect.ParticleEffect;

import cc.zoyn.pp.PokemonParticle;

public class ParticleCalculator extends BukkitRunnable {

	private String ownerName;
	private Entity entity;
	private ParticleEffect particle;
	private boolean run = true;
	private boolean isAsync = true;
	private BukkitTask runningTask;
	
	private int angle = 0;
	private double yChange = -0.2;
	// y轴改变量
	private double deltaY = 0.03;

	public ParticleCalculator(String ownerName, ParticleEffect particle, Entity entity) {
		this.ownerName = ownerName;
		this.particle = particle;
		this.entity = entity;
	}	

	public String getOwnerName() {
		return ownerName;
	}
	
	public ParticleEffect getParticle() {
		return particle;
	}
	
	public void setParticle(ParticleEffect particle) {
		this.particle = particle;
	}

	@Override
	public void run() {
		// 是否手动关闭宝可梦特效
		if (!run) {
			turnOff();
			return;
		}
		// 用于判断玩家掉线后宝可梦也掉线的措施
		if (entity == null || entity.isDead()) {
			turnOff();
			return;
		}
		Location location = entity.getLocation().clone();

		double radians = Math.toRadians(angle);
		double x = 2.5 * Math.cos(radians);
		double z = 2.5 * Math.sin(radians);

		if (yChange >= 2.5) {
			yChange = 0D;
		}

		if (angle > 360) {
			angle = 0;
		}

		location.add(x, yChange, z);
		particle.display(0, 0, 0, 0, 1, location, 20);
		location.subtract(x, yChange, z);

		yChange += deltaY;
		angle += 10;
	}

	public void turnOn() {
		if (isAsync) {
			runningTask = runTaskTimerAsynchronously(PokemonParticle.getInstance(), 20L, 2L);
		} else {
			runningTask = runTaskTimer(PokemonParticle.getInstance(), 20L, 2L);
		}
	}

	public void turnOff() {
		run = false;
		this.cancel();
		runningTask.cancel();
		
		ParticleManager.getCalculators().remove(this);
	}

}
