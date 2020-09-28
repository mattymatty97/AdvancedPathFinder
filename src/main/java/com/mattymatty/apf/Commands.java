package com.mattymatty.apf;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("Duplicates")
public class Commands implements CommandExecutor {

    private Location pos1;
    private Location pos2;
    private final List<BukkitTask> particles = new LinkedList<>();

    Commands() {}

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be run by a player.");
        } else {
            Player player = (Player) sender;
            if (args.length >= 2) {
                {
                }
            } else if (args.length == 1) {
                switch (args[0].toLowerCase()) {
                    case "pos1": {
                        pos1 = player.getLocation();
                        sender.sendMessage("Pos1 set");
                        return true;
                    }
                    case "pos2": {
                        pos2 = player.getLocation();
                        sender.sendMessage("Pos2 set");
                        return true;
                    }
                    case "particle": {
                        particles.forEach(BukkitTask::cancel);
                        sender.sendMessage("Particles cleared");
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void showParticles(List<Location> locations) {
        showParticles(locations, false);
    }

    private void showParticles(List<Location> locations, boolean isPath) {
        showParticles(locations, isPath, Particle.VILLAGER_HAPPY);
    }

    private void showParticles(List<Location> locations, Particle type) {
        showParticles(locations, false, type);
    }

    private void showParticles(List<Location> locations, boolean isPath, Particle type) {
        BukkitTask particle = Bukkit.getServer().getScheduler().runTaskTimer(AdvancedPathfinder.instance,()->{
            int i = 0;
            for (Location loc : locations) {
                i++;
                Location act = cloneLoc(loc);
                if (!isPath || i==0)
                    Objects.requireNonNull(act.getWorld()).spawnParticle(type, act, 7);
                else {
                    Bukkit.getServer().getScheduler().runTaskLater(AdvancedPathfinder.instance,()-> {
                        Objects.requireNonNull(act.getWorld()).spawnParticle(type, act, 7);
                    }, i * 3);
                }
            }
        },5,Math.max(3,Math.min(locations.size()*3,60)));
        particles.add(particle);
    }


    private Location cloneLoc(Location loc) {
        return new Location(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ());
    }

}
