package com.songoda.ultimatemoderation.punish.player;

import com.songoda.ultimatemoderation.punish.AppliedPunishment;
import com.songoda.ultimatemoderation.punish.PunishmentType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class PlayerPunishData {

    private final UUID player;

    private final List<AppliedPunishment> activePunishments = new ArrayList<>();
    private final List<AppliedPunishment> expiredPunishments = new ArrayList<>();

    public PlayerPunishData(UUID player) {
        this.player = player;
    }

    public PlayerPunishData(UUID player, List<AppliedPunishment> punishments) {
        this.player = player;
        this.activePunishments.addAll(punishments);
    }

    public UUID getPlayer() {
        return player;
    }

    public List<AppliedPunishment> getActivePunishments() {
        audit();
        return new ArrayList<>(activePunishments);
    }

    public List<AppliedPunishment> getActivePunishments(PunishmentType type) {
        audit();
        return activePunishments.stream().filter(punishment -> punishment.getPunishmentType() == type).collect(Collectors.toList());
    }

    public List<AppliedPunishment> getExpiredPunishments() {
        audit();
        return new ArrayList<>(expiredPunishments);
    }

    public List<AppliedPunishment> getExpiredPunishments(PunishmentType type) {
        audit();
        return expiredPunishments.stream().filter(punishment -> punishment.getPunishmentType() == type).collect(Collectors.toList());
    }

    public AppliedPunishment[] addPunishment(AppliedPunishment... appliedPunishments) {
        this.activePunishments.addAll(Arrays.asList(appliedPunishments));
        return appliedPunishments;
    }

    public AppliedPunishment[] removePunishment(AppliedPunishment... appliedPunishments) {
        this.activePunishments.removeAll(Arrays.asList(appliedPunishments));
        return appliedPunishments;
    }

    public AppliedPunishment[] removeExpiredPunishment(AppliedPunishment... appliedPunishments) {
        this.activePunishments.removeAll(Arrays.asList(appliedPunishments));
        return appliedPunishments;
    }

    private void audit() {
        audit(false, PunishmentType.ALL);
    }

    private void audit(boolean forced, PunishmentType punishmentType) {
        List<AppliedPunishment> expired = activePunishments.stream().filter(appliedPunishment ->
                (appliedPunishment.getDuration() != -1 || forced)
                        && (appliedPunishment.getPunishmentType() == punishmentType || punishmentType == PunishmentType.ALL)
                        && appliedPunishment.getExpiration() <= System.currentTimeMillis()).collect(Collectors.toList());

        this.expiredPunishments.addAll(expired);
        this.activePunishments.removeAll(expired);
    }

    public void expirePunishments(PunishmentType type) {
        List<AppliedPunishment> toAudit = new ArrayList<>();
        activePunishments.stream().filter(appliedPunishment ->
                type == appliedPunishment.getPunishmentType()).forEach(appliedPunishment -> {
            appliedPunishment.expire();
            toAudit.add(appliedPunishment);
        });
        toAudit.stream().forEach(appliedPunishment -> this.audit(true, type));
    }
}
