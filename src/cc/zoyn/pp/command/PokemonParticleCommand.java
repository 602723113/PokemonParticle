package cc.zoyn.pp.command;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.darkblade12.particleeffect.ParticleEffect;
import com.google.common.collect.Lists;

import cc.zoyn.pp.manager.ParticleCalculator;
import cc.zoyn.pp.manager.ParticleManager;

public class PokemonParticleCommand implements CommandExecutor {

	// 禁止使用的粒子类型, 防止卡客户端
	private static List<ParticleEffect> bannedParticle = Lists.newArrayList(ParticleEffect.WATER_BUBBLE,
			ParticleEffect.EXPLOSION_NORMAL, ParticleEffect.EXPLOSION_LARGE,
			ParticleEffect.EXPLOSION_HUGE, ParticleEffect.WATER_WAKE, ParticleEffect.SUSPENDED,
			ParticleEffect.FOOTSTEP, ParticleEffect.LAVA, ParticleEffect.DRAGON_BREATH,
			ParticleEffect.ITEM_CRACK, ParticleEffect.BLOCK_CRACK, ParticleEffect.BLOCK_DUST,
			ParticleEffect.WATER_DROP, ParticleEffect.ITEM_TAKE, ParticleEffect.MOB_APPEARANCE,
			ParticleEffect.DRAGON_BREATH, ParticleEffect.END_ROD, ParticleEffect.DAMAGE_INDICATOR,
			ParticleEffect.SWEEP_ATTACK, ParticleEffect.FALLING_DUST, ParticleEffect.BARRIER);

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("pp")) {
			if (args.length == 0) {
				sender.sendMessage("§6精灵宝可梦粒子特效插件 >>");
				sender.sendMessage("§e/pp particle <粒子名> §7更换你的特效的粒子");
				sender.sendMessage("§e/pp effects §7查看所有可用的粒子");
				sender.sendMessage("§e/pp off §7关闭粒子 (重新开启粒子需重新召唤)");
//				sender.sendMessage("§e/pp particle REDSTONE <R> <G> <B>§7利用可更改颜色的粒子给粒子换色");
				return true;
			}
			if (!(sender instanceof Player)) {
				sender.sendMessage("此命令只能由玩家使用!");
				return true;
			}
			Player player = (Player) sender;
			if (args[0].equalsIgnoreCase("particle")) {
				if (args.length < 2) {
					sender.sendMessage("§c请正确使用指令! 正确格式: §e/pp particle <粒子名> §7更换你的特效的粒子");
					return true;
				}

				// 判断有无该粒子
				String particleName = args[1].toUpperCase();
				ParticleEffect effect;
				try {
					effect = ParticleEffect.valueOf(particleName);
				} catch (Exception e) {
					sender.sendMessage("§c没有这个粒子噢...");
					return true;
				}
				
				// 判断粒子
				if (!player.hasPermission("pp." + effect.toString())) {
					sender.sendMessage("§6你没有使用该粒子的权限噢!");
					return true;
				}
				
				// 设置玩家已设置的粒子
				ParticleManager.getPlayerSelectParticle().put(sender.getName(), ParticleEffect.valueOf(particleName));
				ParticleCalculator calc = ParticleManager.getPlayerCalc(player.getName());
				// 是否在运行
				if (calc != null) {
					calc.setParticle(effect);
				}
				sender.sendMessage("§e你的粒子类型已更改为: §e" + particleName);
				return true;
			}

			if (args[0].equalsIgnoreCase("effects")) {
				sender.sendMessage("§6正在列出所有可用的粒子类型...");
				for (ParticleEffect effect : ParticleEffect.values()) {
					if (!bannedParticle.contains(effect)) {
						if (effect.equals(ParticleEffect.FIREWORKS_SPARK)) {
							sender.sendMessage(" - §2" + effect.toString() + " §6(§f白色§6闪耀粒子 - 强烈推荐!)");
						} else if(effect.equals(ParticleEffect.HEART)) {
							sender.sendMessage(" - §2" + effect.toString() + " §6(§c爱心§6粒子 - 热!)");
						} else if(effect.equals(ParticleEffect.FLAME)) {
							sender.sendMessage(" - §2" + effect.toString() + " §6(§e火焰§6粒子 - NEW!)");
						} else if(effect.equals(ParticleEffect.VILLAGER_HAPPY)) {
							sender.sendMessage(" - §2" + effect.toString() + " §6(§a绿色§6闪耀粒子 - HOT!)");
						} else if(effect.equals(ParticleEffect.REDSTONE)) {
							sender.sendMessage(" - §2" + effect.toString() + " §6(§4红色§6闪耀粒子 - 推荐大佬使用!)");
						} else {
							sender.sendMessage(" - §2" + effect.toString());
						}
					}
				}
				return true;
			}

//			if (args[0].equalsIgnoreCase("test")) {
//				ParticleEffect playerSelect = ParticleManager.getPlayerSelectParticle().getOrDefault(player.getName(),
//						ParticleEffect.FIREWORKS_SPARK);
//
//				ParticleCalculator calculator = new ParticleCalculator(player.getName(), playerSelect, player);
//				// 加入manager进行管理
//				ParticleManager.addParticle(calculator);
//				calculator.turnOn();
//				return true;
//			}

			if (args[0].equalsIgnoreCase("off")) {
				ParticleCalculator calc = ParticleManager.getPlayerCalc(player.getName());
				if (calc == null) {
					sender.sendMessage("§6你并没有正在运行的粒子特效噢");
					return true;
				}
				calc.turnOff();
				ParticleManager.getCalculators().remove(calc);

				return true;
			}
		}
		return false;
	}

}
