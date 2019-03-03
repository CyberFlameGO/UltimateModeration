package com.songoda.ultimatemoderation.command.commands;

import com.songoda.ultimatemoderation.UltimateModeration;
import com.songoda.ultimatemoderation.command.AbstractCommand;
import com.songoda.ultimatemoderation.punish.Punishment;
import com.songoda.ultimatemoderation.punish.PunishmentType;
import com.songoda.ultimatemoderation.utils.Methods;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import java.util.List;

public class CommandWarn extends AbstractCommand {

    public CommandWarn() {
        super(false, true, "Warn");
    }

    @Override
    protected ReturnType runCommand(UltimateModeration instance, CommandSender sender, String... args) {
        if (args.length < 1)
            return ReturnType.SYNTAX_ERROR;

        // I dream of the day where someone creates a ticket because
        // they can't ban someone for the reason "Stole me 2h sword".
        long duration = 0;
        StringBuilder reasonBuilder = new StringBuilder();
        if (args.length > 1) {
            for (int i = 1; i < args.length; i++) {
                String line = args[i];
                long time = Methods.parseTime(line);
                if (time != 0)
                    duration += time;
                else
                    reasonBuilder.append(line).append(" ");

            }
        }
        String reason = reasonBuilder.toString().trim();

        OfflinePlayer player = Bukkit.getOfflinePlayer(args[0]);

        if (player == null) {
            sender.sendMessage(instance.getReferences().getPrefix() + "That player does not exist.");
            return ReturnType.FAILURE;
        }

        new Punishment(PunishmentType.WARNING, duration == 0 ? -1 : duration, reason.equals("") ? null : reason)
                .execute(sender, player);

        return ReturnType.SUCCESS;
    }

    @Override
    protected List<String> onTab(UltimateModeration instance, CommandSender sender, String... args) {
        return null;
    }

    @Override
    public String getPermissionNode() {
        return "um.warning";
    }

    @Override
    public String getSyntax() {
        return "/Warn <player> [duration] [reason]";
    }

    @Override
    public String getDescription() {
        return "Allows you to warn players.";
    }
}
