package cf.strafe.data;

import cf.strafe.KnockBackFFA;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public enum DataManager {
    INSTANCE;
    private final ConcurrentHashMap<UUID, PlayerData> playerDataMap = new ConcurrentHashMap<>();

    public void addPlayer(Player player) {
        KnockBackFFA.INSTANCE.getExecutorService().execute(() -> {
            PlayerData playerData = new PlayerData(player);
            playerDataMap.put(player.getUniqueId(), playerData);
            playerData.loadData();
            KnockBackFFA.INSTANCE.getScoreboardManager().create(player);
        });
    }

    public void removePlayer(Player player) {
        KnockBackFFA.INSTANCE.getExecutorService().execute(() -> {
            getPlayer(player).saveData();
            KnockBackFFA.INSTANCE.getScoreboardManager().remove(player);
            playerDataMap.remove(player.getUniqueId(), new PlayerData(player));
        });
    }

    public void saveAll() {
        for(PlayerData data : playerDataMap.values()) {
            data.saveData();
        }
        playerDataMap.clear();
    }

    public PlayerData getPlayer(Player player) {
        return playerDataMap.get(player.getUniqueId());
    }
}
