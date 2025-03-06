package me.anemys.commandapi;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.entity.Player;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings({"unchecked", "unused"})
public class CommandManager {
    private final JavaPlugin plugin;
    private static Map<String, CommandExecutor> commands = null;
    private static CommandMap commandMap;

    /**
     *
     * @param plugin The plugin instance.
     */
    public CommandManager(JavaPlugin plugin) {
        this.plugin = plugin;
        commands = new HashMap<>();
        setupCommandMap();
    }

    /**
     * Sets up the command map using reflection.
     */
    private void setupCommandMap() {
        try {
            SimplePluginManager spm = (SimplePluginManager) plugin.getServer().getPluginManager();
            Field commandMapField = SimplePluginManager.class.getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
            commandMap = (CommandMap) commandMapField.get(spm);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Unregisters all commands.
     */
    public void unregisterAll() {
        try {
            if(!commands.isEmpty()) {
                Field knownCommandsField = SimpleCommandMap.class.getDeclaredField("knownCommands");
                knownCommandsField.setAccessible(true);
                Map<String, Command> knownCommands = (Map<String, Command>) knownCommandsField.get(commandMap);

                commands.forEach((name, cmd) -> {
                    knownCommands.remove(plugin.getName().toLowerCase() + ":" + name);
                    knownCommands.remove(name);
                });

                commands.clear();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates player commands on the server.
     */
    public void updatePlayerCommands() {

        Bukkit.getScheduler().runTask(plugin, () -> {
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.updateCommands();
            }
        });
    }

    /**
     *
     * @param name The name of the command.
     * @return A new instance of CommandBuilder.
     */
    public CommandBuilder register(String name) {
        return new CommandBuilder(plugin, name);
    }

    /**
     *
     * @return The map of CommandExecutors.
     */
    public static Map<String, CommandExecutor> getCommands() {
        return commands;
    }

    /**
     *
     * @return The CommandMap instance.
     */
    public static CommandMap getCommandMap() {
        return commandMap;
    }

}
