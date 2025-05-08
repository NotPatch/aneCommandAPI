package me.anemys.commandapi;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

@SuppressWarnings("unused")
public class CommandBuilder {
    private final JavaPlugin plugin;
    private final String name;
    private BiConsumer<CommandSender, String[]> playerExecutor;
    private BiConsumer<CommandSender, String[]> consoleExecutor;
    private BiFunction<CommandSender, String[], List<String>> tabCompleter;
    private String permission;
    private String noPermissionMessage;
    private boolean playerOnly = false;
    private List<String> aliases = new ArrayList<>();
    private String description = "";
    private String usage = "";
    private int minArgs = 0;
    private int maxArgs = -1;
    private String argErrorMessage;
    private List<String> argErrorMessageList = new ArrayList<>();

    /**
     *
     * @param plugin The plugin instance.
     * @param name   The name of the command.
     */
    CommandBuilder(JavaPlugin plugin, String name) {
        this.plugin = plugin;
        this.name = name;
    }

    /**
     *
     * @param executor The BiConsumer to handle player commands.
     * @return The CommandBuilder instance.
     */
    public CommandBuilder playerExecutor(BiConsumer<CommandSender, String[]> executor) {
        this.playerExecutor = executor;
        return this;
    }

    /**
     *
     * @param executor The BiConsumer to handle console commands.
     * @return The CommandBuilder instance.
     */
    public CommandBuilder consoleExecutor(BiConsumer<CommandSender, String[]> executor) {
        this.consoleExecutor = executor;
        return this;
    }

    /**
     *
     * @param completer The BiFunction to handle tab completion.
     * @return The CommandBuilder instance.
     */
    public CommandBuilder tabCompleter(BiFunction<CommandSender, String[], List<String>> completer) {
        this.tabCompleter = completer;
        return this;
    }

    /**
     *
     * @param permission The permission string.
     * @return The CommandBuilder instance.
     */
    public CommandBuilder permission(String permission) {
        this.permission = permission;
        return this;
    }

    /**
     *
     * @param message The no permission message.
     * @return The CommandBuilder instance.
     */
    public CommandBuilder noPermissionMessage(String message) {
        this.noPermissionMessage = message;
        return this;
    }

    /**
     * Sets the command type (player only or console).
     *
     * @param commandType The command type.
     * @return The CommandBuilder instance.
     */
    public CommandBuilder commandType(CommandType commandType) {
        switch (commandType) {
            case PLAYER:
                this.playerOnly = true;
                break;
            case CONSOLE:
                this.playerOnly = false;
                break;
        }
        return this;
    }

    /**
     *
     * @param aliases The command aliases.
     * @return The CommandBuilder instance.
     */
    public CommandBuilder aliases(String... aliases) {
        this.aliases = Arrays.asList(aliases);
        return this;
    }

    /**
     *
     * @param description The command description.
     * @return The CommandBuilder instance.
     */
    public CommandBuilder description(String description) {
        this.description = description;
        return this;
    }

    /**
     *
     * @param usage The usage message.
     * @return The CommandBuilder instance.
     */
    public CommandBuilder usage(String usage) {
        this.usage = usage;
        return this;
    }

    /**
     * Sets a list of usage examples for the command.
     *
     * @param usageList The list of usage examples.
     * @return The CommandBuilder instance.
     */
    public CommandBuilder argErrorMessageList(List<String> usageList) {
        this.argErrorMessageList = usageList;
        return this;
    }

    /**
     * Sets the command usage examples from a varargs list.
     *
     * @param usage The usage examples.
     * @return The CommandBuilder instance.
     */
    public CommandBuilder argErrorMessageList(String... usage) {
        this.argErrorMessageList = Arrays.asList(usage);
        return this;
    }

    // usageList getter method
    public List<String> getArgErrorMessageList() {
        return argErrorMessageList;
    }

    /**
     *
     * @param minArgs       The minimum number of arguments.
     * @param maxArgs       The maximum number of arguments.
     * @param errorMessage  The error message if arguments are incorrect.
     * @return The CommandBuilder instance.
     */
    public CommandBuilder arguments(int minArgs, int maxArgs, String errorMessage) {
        this.minArgs = minArgs;
        this.maxArgs = maxArgs;
        this.argErrorMessage = errorMessage;
        return this;
    }

    public CommandBuilder arguments(int minArgs, int maxArgs, List<String> errorMessages) {
        this.minArgs = minArgs;
        this.maxArgs = maxArgs;
        this.argErrorMessageList = errorMessages;
        return this;
    }

    /**
     * Registers the command with the plugin.
     */
    public void register() {
        CommandExecutor executor = new CommandExecutor(this, name);
        if (!aliases.isEmpty()) {
            executor.setAliases(aliases);
        }

        if (!description.isEmpty()) {
            executor.setDescription(description);
        }
        if (!usage.isEmpty()) {
            executor.setUsage(usage);
        }

        CommandManager.getCommands().put(name, executor);
        CommandManager.getCommandMap().register(plugin.getName().toLowerCase(), executor);
    }

    protected BiConsumer<CommandSender, String[]> getPlayerExecutor() { return playerExecutor; }
    protected BiConsumer<CommandSender, String[]> getConsoleExecutor() { return consoleExecutor; }
    protected BiFunction<CommandSender, String[], List<String>> getTabCompleter() { return tabCompleter; }
    protected String getPermission() { return permission; }
    protected String getNoPermissionMessage() { return noPermissionMessage; }
    protected boolean isPlayerOnly() { return playerOnly; }
    protected int getMinArgs() { return minArgs; }
    protected int getMaxArgs() { return maxArgs; }
    protected String getArgErrorMessage() { return argErrorMessage; }
}
