package net.javac.command;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class UserCooldown {
    final ScheduledExecutorService scheduledExecutorService;
    Map<Long, Integer> cooldowns = new ConcurrentHashMap<>();

    public UserCooldown(ScheduledExecutorService scheduledExecutorService) {
        this.scheduledExecutorService = scheduledExecutorService;
    }

    public void addCooldown(Long id, int second) {
        cooldowns.putIfAbsent(id, second);
        removeUser(id, second);
    }

    public void removeUser(Long id) {
        cooldowns.remove(id);
    }

    public void removeUser(Long id, int second) {
        scheduledExecutorService.schedule(() -> {
            if (cooldowns.get(id) < -100) {
                removeRateLimit(id);
                return;
            }
            removeUser(id);
        }, second, TimeUnit.SECONDS);
    }

    public void removeRateLimit(Long id) {
        scheduledExecutorService.schedule(() -> removeUser(id), 30, TimeUnit.SECONDS);
    }

    public boolean isOnCooldown(Long id) {
        final Integer cooldown = cooldowns.get(id);
        final boolean onCooldown = cooldown != null;
        if (onCooldown) {
            if (cooldown > 0) {
                cooldowns.replace(id, (-cooldown) - 50);
            } else {
                cooldowns.replace(id, cooldown-50);
            }
            System.out.println("Cooldown: " + cooldowns.get(id));
        }
        return onCooldown;
    }

    public boolean isRateLimited(Long id) {
        final Integer cooldown = cooldowns.get(id);
        if (cooldown == null) return false;
        return cooldown < -100;
    }
}
