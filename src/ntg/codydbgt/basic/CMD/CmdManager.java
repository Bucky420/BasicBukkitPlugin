package ntg.codydbgt.basic.CMD;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import ntg.codydbgt.basic.BukkitPlugin;



public class CmdManager implements CommandExecutor{
	
	private String[] aliases;
	private BukkitPlugin//should only need to edit this to the class you have made
	plugin;
	private HashMap<String,Method> MethodList = new HashMap<String,Method>();
	private ArrayList<Method> RootMethodList = new ArrayList<Method>(); 
	
	/**
	 *  You will have to register the command executer your self
	 * @param p
	 */
	public CmdManager(JavaPlugin p) {
		plugin = (BukkitPlugin)p;
		aliases=null;
	}

	public CmdManager(JavaPlugin p,String _aliases) {
		plugin = (BukkitPlugin)p;
		aliases = new String[]{_aliases};
		init(p);
	}
	
	public CmdManager(JavaPlugin p,String[] _aliases){
		plugin = (BukkitPlugin)p;
		aliases  = _aliases;
		init(p);
	}
	
	private void init(JavaPlugin p){
		
		for(String name : aliases){
			p.getCommand(name).setExecutor(this);
		}
	}
	
	@Override
	public boolean onCommand(CommandSender Sender,org.bukkit.command.Command CMD, String label, String[] args) {
		
		if(aliases!=null){
			boolean found = false;
			
			for(String i : aliases){
				if(i.equalsIgnoreCase(CMD.getName())){ 
					found = true;
					break;
				}
			}
			if(!found){
			plugin.log("not registerd for this command: "+CMD.getName()); 
			return false;
			}
		}
		
		if(args.length==0){
			return Call(Sender,CMD,args);
		}
		
		if(Call(Sender,CMD,args) ){
			return true;
		}else{
			return Call(args[0],StringPop(args),Sender,CMD);
		}
	}

	private boolean Call(CommandSender Sender, org.bukkit.command.Command CMD,String[] args) {

		if(!RootMethodList.isEmpty()){
			boolean ran = false;
			for(Method m : RootMethodList){
			String error = "";
			ROOTCMD ano = m.getAnnotation(ROOTCMD.class); 
			if(ano.Player()==true && !(Sender instanceof Player)){
				error = "You have to be ingame to use this command";
				}else{
					if(Sender instanceof Player){
						Player p = (Player) Sender;
						if( !ano.perms().equalsIgnoreCase("null") && p.hasPermission(ano.perms()) ){

							if(args.length==ano.Args() || ano.Args()==-1 ){
								try {
									ran = true;
									m.invoke(null,(Object)new EventCMD(this,Sender, args)); 
								} catch (IllegalAccessException e) {
									e.printStackTrace();
								} catch (IllegalArgumentException e) {
									e.printStackTrace();
								} catch (InvocationTargetException e) {
									e.printStackTrace();
								}
							return true;
							}else{//wrong num of args	
							error = "Wrong number of arguments for command &a"+m.getName();
							}
							
						}else{
							error= "&4Not enough Permission you need:&a "+ano.perms();
						}
					}else{
						if(args.length==ano.Args() || ano.Args()==-1 ){
							try {
								ran = true;
								m.invoke(null,(Object)new EventCMD(this,Sender, args)); 
							} catch (IllegalAccessException e) {
								e.printStackTrace();
							} catch (IllegalArgumentException e) {
								e.printStackTrace();
							} catch (InvocationTargetException e) {
								e.printStackTrace();
							}
						return true;
						}else{//wrong num of args	
						error = "Wrong number of arguments for command &a"+m.getName();
						}
					}

				}
			
				if(ano.Error() && error.length()>0)
				reply(error,Sender);
				
			}
			if(ran){
			reply("USE: "+CMD.getUsage(),Sender);
			return false;
			}
		}
		return false;
	}

	public boolean Call(String cmdname,String[] args,CommandSender Sender,org.bukkit.command.Command CMD){
		String error = "";
		
		if(MethodList.containsKey(cmdname)){
			
			Method m = MethodList.get(cmdname);
			Command ano = m.getAnnotation(Command.class);

			if(ano.Player()==true && !(Sender instanceof Player)){
			error = "You have to be ingame to use this command";
			}else{
				if(Sender instanceof Player){
					Player p = (Player) Sender;
					if( !ano.perms().equalsIgnoreCase("null") && p.hasPermission(ano.perms()) ){

						if(args.length==ano.Args() || ano.Args()==-1 ){
							try {
								m.invoke(null,(Object)new EventCMD(this,Sender, args)); 
							} catch (IllegalAccessException e) {
								e.printStackTrace();
							} catch (IllegalArgumentException e) {
								e.printStackTrace();
							} catch (InvocationTargetException e) {
								e.printStackTrace();
							}
						return true;
						}else{//wrong num of args	
						error = "Wrong number of arguments for command: &a"+cmdname;
						}
						
					}else{
						error= "&4Not enough Permission you need:&a "+ano.perms();
					}
				}else{
					if(args.length==ano.Args() || ano.Args()==-1 ){
						try {
							m.invoke(null,(Object)new EventCMD(this,Sender, args)); 
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						} catch (IllegalArgumentException e) {
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							e.printStackTrace();
						}
					return true;
					}else{//wrong num of args	
					error = "Wrong number of arguments for command &a"+cmdname;
					}
				}

			}
			
		if(error.length()>0)
		reply(error,Sender);
		return false;
		}
		reply("UnKnown Command "+cmdname,Sender);
		return false;
	}
	
	private void reply(String msg,CommandSender sender) {
		if(sender instanceof Player){ 
		Player p = (Player) sender;
		plugin.msg(p, msg);	
		}else{
		plugin.log(msg);
		}
		
	}

	/**
	 * registers all commands Defined by the @Command or @ROOTCMD annotation in said class
	 * @param _class
	 * @return 
	 */
	public CmdManager register(Class<?> _class){
		for (Method i : _class.getMethods() ){ 
			
			if(!Modifier.isStatic(i.getModifiers())){
				continue;
			}
			if(i.getAnnotation(Command.class)!=null){
				String cmdName = i.getAnnotation(Command.class).Name();
				MethodList.put(cmdName, i);
				plugin.log("Registerd Command: "+cmdName);
		
			}else if(i.getAnnotation(ROOTCMD.class)!=null){
				RootMethodList.add(i);
				plugin.log("Registerd ROOT Command: "+i.getName());
			}
		}
		return this;
	}
	
	/**
	 * 
	 * @return returns list of all registered methods 
	 */
	public HashMap<String, Method> List(){ 
		return MethodList;
	}
	
	/**
	 * 
	 * @return returns list of all registered ROOT methods 
	 */
	public ArrayList<Method> RootList(){
		return RootMethodList; 
	}

	/**
	 * Removes the first index in the string array and shifts the array up
	 * @return returns new string array or NULL if the str.length<=1 
	 */
	public static String[] StringPop(String[] str){
		if(!(str.length>1))return new String[0];
		String[] out = new String[str.length-1];
		for(int i=0;i<str.length-1;i++){
			out[i]=str[i+1];
		}
		return out;
	}


}
