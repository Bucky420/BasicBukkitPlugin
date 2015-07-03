package ntg.codydbgt.basic.config;

import java.util.List;
import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class singleConfig {

	
	private FileConfiguration config;
	private File f; 
 
	public singleConfig(JavaPlugin j,String configname) {
		 f = new File(j.getDataFolder(),configname);
		config = YamlConfiguration.loadConfiguration(f);
	}

	public singleConfig(JavaPlugin j,File file){
		f = file; 
		config = YamlConfiguration.loadConfiguration(f);
	}
	
	public List<?> loadValue(String node,List<?> l){
		if(!config.contains(node)){
			config.set(node,l);
			save();
		}
		return config.getList(node); 
	}
	
	public boolean loadValue(String node,boolean b){
		if(!config.contains(node)){
			config.set(node,b);
			save();
		}
		return config.getBoolean(node); 
	}
	
	public String loadValue(String node,String s){
		if(!config.contains(node)){
			config.set(node,s);
			save();
		}
		return config.getString(node); 
	}
	
	public int loadValue(String node,int i){
		if(!config.contains(node)){
			config.set(node,i);
			save();
		}
		return config.getInt(node); 
	}
	
	public FileConfiguration getConfig(){
		return config;
	}

	public void save(){
		try {
			config.save(f);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void reload(){
		try {
			config.load(f);
		} catch (IOException | InvalidConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
