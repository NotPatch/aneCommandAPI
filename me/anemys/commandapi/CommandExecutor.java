package me.anemys.commandapi;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.ArrayList;

public class CommandExecutor extends Command {
    private final CommandBuilder builder;

    /**
     *
     * @param builder The CommandBuilder instance.
     * @param name    The name of the command.
     */
    protected CommandExecutor(CommandBuilder builder, String name) {
        super(name);
        this.builder = builder;
    }

    /**
     *
     * @param sender The sender of the command.
     * @param label  The command label.
     * @param args   The command arguments.
     * @return true if the command was executed successfully.
     */
    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, String[] args) {

        /*
         * Check if command is player-only
         * */
        if (builder.isPlayerOnly() && !(sender instanceof Player)) {
            sender.sendMessage("This command can only be used by players!");
            return true;
        }

        /*
         * Check permission if is not set
         * */
        if (builder.getPermission() != null && !sender.hasPermission(builder.getPermission())) {
            sender.sendMessage(builder.getNoPermissionMessage() != null ?
                    builder.getNoPermissionMessage() :
                    "You don't have permission to use this command!");
            return true;
        }

        /*
         * Check argument count
         * */
        if (builder.getMinArgs() > 0 || builder.getMaxArgs() >= 0) {
            if (args.length < builder.getMinArgs() ||
                    (builder.getMaxArgs() >= 0 && args.length > builder.getMaxArgs())) {
                sender.sendMessage(builder.getArgErrorMessage() != null ?
                        builder.getArgErrorMessage() :
                        "Invalid number of arguments! Usage: " + getUsage());
                return true;
            }
        }

        /*
         * Execute command
         * */
        if (sender instanceof Player && builder.getPlayerExecutor() != null) {
            builder.getPlayerExecutor().accept(sender, args);
        } else if (builder.getConsoleExecutor() != null) {
            builder.getConsoleExecutor().accept(sender, args);
        }

        return true;
    }

    /**
     *
     * @param sender The sender of the command.
     * @param alias  The alias used for the command.
     * @param args   The command arguments.
     * @return A list of tab completion options.
     */
    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, String[] args) {
        if (builder.getTabCompleter() != null) {
            List<String> completions = builder.getTabCompleter().apply(sender, args);
            return filterCompletions(completions, args.length > 0 ? args[args.length - 1] : "");
        }
        return super.tabComplete(sender, alias, args);
    }

    /**
     *
     * @param completions The list of completions.
     * @param input       The user's current input.
     * @return The filtered list of completions.
     */
    private List<String> filterCompletions(List<String> completions, String input) {
        if (input.isEmpty()) {
            return completions;
        }

        List<String> filteredCompletions = new ArrayList<>();
        for (String completion : completions) {
            if (completion.toLowerCase().startsWith(input.toLowerCase())) {
                filteredCompletions.add(completion);
            }
        }
        return filteredCompletions;
    }
}
