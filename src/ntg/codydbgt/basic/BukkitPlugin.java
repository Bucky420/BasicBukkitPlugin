package ntg.codydbgt.basic;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import ntg.codydbgt.basic.CMD.BasicCommands;
import ntg.codydbgt.basic.CMD.CmdManager;
import ntg.codydbgt.basic.config.singleConfig;

public class BukkitPlugin extends JavaPlugin{

	public static BukkitPlugin self;
	public static boolean debug =false;
	public singleConfig plugindata; 
	@Override
	public void onEnable() {
		self = this;
		new CmdManager(this,"basic").register(BasicCommands.class);
		plugindata = new singleConfig(this,"config.yml");
		debug = plugindata.loadValue("basic.debug",debug);
	}

	public void msg(Player p, String msg) {
	 p.sendMessage(ChatColor.translateAlternateColorCodes('&',msg));
	}

	public void log(String msg) {
		getLogger().info(ChatColor.translateAlternateColorCodes('&',msg) );
	}
	
	public void debug(String msg){
		if(debug){
			log("[DEBUG] "+msg);
		}
	}

}
