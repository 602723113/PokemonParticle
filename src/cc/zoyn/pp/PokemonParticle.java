package cc.zoyn.pp;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.events.PacketListener;
import com.comphenix.protocol.wrappers.EnumWrappers;

import cc.zoyn.pp.command.PokemonParticleCommand;
import cc.zoyn.pp.listener.PokemonListener;
import cc.zoyn.pp.manager.ParticleManager;

public class PokemonParticle extends JavaPlugin {

	private static PokemonParticle instance;

	@Override
	public void onEnable() {
		instance = this;
		
		Bukkit.getConsoleSender().sendMessage("§aPokemonParticle 已加载");
		Bukkit.getPluginCommand("pp").setExecutor(new PokemonParticleCommand());
		Bukkit.getPluginManager().registerEvents(new PokemonListener(), this);
	}

	public static PokemonParticle getInstance() {
		return instance;
	}

	@Override
	public void onDisable() {
		// 关闭特效
		ParticleManager.turnOffAllParticle();
		Bukkit.getConsoleSender().sendMessage("§aPokemonParticle 已卸载");
	}
}
