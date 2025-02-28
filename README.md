# aneCommandAPI
Simple command generation system without plugin.yml for minecraft bukkit api.
With this API; You can create a command in seconds, specify permissions, add arguments (max-min argument warning message included), aliases commands and tab complete. All you have to do is put the package you downloaded into the src\main\java\ directory. that's all.

# Example uses
* Basicly gamemode change command

![](https://github.com/therealthread/aneCommandAPI/blob/main/gmchange.png?raw=true)
![](https://github.com/therealthread/aneCommandAPI/blob/main/tabcomp.png?raw=true)

```java
public Commands(AneRequestShop plugin) {
        this.plugin = plugin;
        CommandManager manager = new CommandManager(plugin);

        manager.register("gamemode")
                .aliases("gm", "gamem", "mode")
                .commandType(CommandType.PLAYER)
                .permission("gamemode.permission")
                .noPermissionMessage("§cYou do not have permission to open this shop!")
                .description("gamemode permission")
                .usage("/gamemode 0, 1, 2, 3")
                .arguments(1, 1, plugin.getMsg().getPrefix() + "§cUsage: /gamemode 0, 1, 2, 3")
                .tabCompleter((sender, args) -> {
                    if (args.length == 1) {
                        return Arrays.asList("0", "1", "2", "3");
                    } else if (args.length == 10) {
                        return Collections.singletonList("gamemode 10 huh?");
                    }
                    return new ArrayList<>();
                })
                .playerExecutor((sender, args) -> {
                    Player p = (Player) sender;

                    switch (Integer.parseInt(args[0])) {
                        case 0:
                            p.setGameMode(GameMode.SURVIVAL);
                            p.sendMessage("§eYour game mode has been changed to §asurvival!");
                            break;
                        case 1:
                            p.setGameMode(GameMode.CREATIVE);
                            p.sendMessage("§eYour game mode has been changed to §acreative!");
                            break;
                        case 2:
                            p.setGameMode(GameMode.ADVENTURE);
                            p.sendMessage("§eYour game mode has been changed to §aadventure!");
                            break;
                        case 3:
                            p.setGameMode(GameMode.SPECTATOR);
                            p.sendMessage("§eYour game mode has been changed to §aspectator!");
                            break;
                        default:
                            p.sendMessage("§cUsage: /gamemode 0, 1, 2, 3");
                    }
                })
                .register();
        registerCommands();
    }
```

# Create new

Create the new command object.

```java
CommandManager manager = new CommandManager(plugin.getInstance());
```

...then start adding your commands!
```java
manager.register("hellohell").register(); //you know more ;)

```
