package cf.strafe.listener;

import cf.strafe.data.DataManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class DataListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        DataManager.INSTANCE.addPlayer(event.getPlayer());
        event.getPlayer().teleport(event.getPlayer().getWorld().getSpawnLocation());
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        DataManager.INSTANCE.removePlayer(event.getPlayer());
    }
}
