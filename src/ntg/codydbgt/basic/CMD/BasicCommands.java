package ntg.codydbgt.basic.CMD;

import java.lang.reflect.Method;
import java.util.Map.Entry;

import ntg.codydbgt.basic.BukkitPlugin;

public class BasicCommands {
	
	public static BukkitPlugin plugin = BukkitPlugin.self;
	
	@ROOTCMD(Args=0,decription="default help command",Player=false)
	//ALL COMMANDS HAVE TO VE STATIC OR IT WILL NOT BE REGISTERD
	public static void help(EventCMD e){
		e.Reply("============== HELP ==============");
		
		for(Entry<String, Method> m : e.getManager().List().entrySet()){
			Command ano = m.getValue().getAnnotation(Command.class);
			e.Reply("- "+ano.Name()+" Decription:"+ano.decription()+" argCont:"+ano.Args()+" Parent:"+ano.Parent()+" Perms:"+ano.perms());
		}
		
		e.Reply("==================================");
	}
	
	@Command(Name="reload",decription="Reload configs :) ",Player=false,Args=0)
	public static void reload(EventCMD e){
		e.plugin.plugindata.reload();
		e.Reply("Reloaded Configs"); 
	}
	
	@Command(Name="save",decription="Save configs :) ",Player=false,Args=0)
	public static void save(EventCMD e){
		e.plugin.plugindata.save();
		e.Reply("Reloaded Configs"); 
	}
	
	@Command(Name="set",decription="set configs :) ",Player=false,Args=2)
	public static void setstring(EventCMD e){
		e.plugin.plugindata.loadValue(e.getArgs()[0],e.getArgs()[1]);
	}
}
