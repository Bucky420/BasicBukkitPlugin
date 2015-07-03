package ntg.codydbgt.basic.CMD;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ntg.codydbgt.basic.BukkitPlugin;

public class EventCMD {

	private CommandSender sender;
	private String[] args; 
	public BukkitPlugin plugin = BukkitPlugin.self;
	private CmdManager manager;
	public EventCMD(CmdManager cmdManager, CommandSender Sender, String[] _args) {
		sender = Sender;
		args = _args; 
		manager =cmdManager;
	}
	
	public void Reply(String msg){
		 if (sender instanceof Player){
			 Player p = (Player) sender;
			 plugin.msg(p, msg);
		 }else{
			 plugin.log(msg);
		 }
	}
	
	public CommandSender getSender(){
	return sender; 
	}
	
	public String[] getArgs(){
		return args;
	}

	public CmdManager getManager() {
		return manager;
	}
}
