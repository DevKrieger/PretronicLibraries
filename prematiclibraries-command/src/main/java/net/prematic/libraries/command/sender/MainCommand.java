package net.prematic.libraries.command.sender;

/*
 *
 *  * Copyright (c) 2018 Philipp Elvin Friedhoff on 29.09.18 12:49
 *
 */

import net.prematic.libraries.command.Command;
import net.prematic.libraries.command.SubCommand;
import net.prematic.libraries.utility.GenerellUtil;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class MainCommand extends Command {

    private List<SubCommand> subCommands;

    public MainCommand(String name) {
        super(name);
        this.subCommands = new LinkedList<>();
    }

    public MainCommand(String name, String description) {
        super(name, description);
        this.subCommands = new LinkedList<>();
    }

    public MainCommand(String name, String description, String permission) {
        super(name, description, permission);
        this.subCommands = new LinkedList<>();
    }

    public MainCommand(String name, String description, String permission, String usage, String... aliases) {
        super(name, description, permission, usage, aliases);
        this.subCommands = new LinkedList<>();
    }

    public List<SubCommand> getSubCommands() {
        return subCommands;
    }

    private int getMaxPages() {
        return GenerellUtil.getMaxPages(8, subCommands);
    }

    public void registerSubCommand(SubCommand subCommand){
        this.subCommands.add(subCommand);
    }

    public void sendHelp(CommandSender sender, int page) {
        int maxpages = getMaxPages();
        if(page > maxpages) page = 1;
        int nextpage = page+1;
        if(nextpage > maxpages) nextpage = 1;
        if(nextpage == page){
            sender.sendMessage("Seite " + page + "/" + maxpages);
        }else sender.sendMessage("Seite " + page + "/" + maxpages + " | Weitere Hilfeseiten mit " + getName() + " " + nextpage);
        int from = 1;
        if(page > 1) from = 8 * (page - 1) + 1;
        int to = 8 * page;
        for(int h = from; h <= to; h++) {
            if(h > subCommands.size()) break;
            SubCommand subcommand = subCommands.get(h - 1);
            if(sender.hasPermission(subcommand.getPermission()) || subcommand.getPermission().equalsIgnoreCase("")){
                sender.sendMessage("/"+getName()+" "+subcommand.getUsage()+" "+subcommand.getDescription());
            }
        }
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length >= 1) {
            for (SubCommand subCommand : subCommands) {
                if (subCommand.hasAliases(args[0])) {
                    subCommand.execute(sender, Arrays.copyOfRange(args, 1, args.length));
                    return;
                }
            }
        }
        if(args.length == 1){
            if(GenerellUtil.isNumber(args[0])){
                sendHelp(sender, Integer.valueOf(args[0]));
                return;
            }
        }
        sendHelp(sender, 1);
    }
}