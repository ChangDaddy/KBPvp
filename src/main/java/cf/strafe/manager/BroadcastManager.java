package cf.strafe.manager;

import cf.strafe.KnockBackFFA;
import cf.strafe.config.Config;
import cf.strafe.data.DataManager;
import cf.strafe.data.PlayerData;
import cf.strafe.util.DefaultFontInfo;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.List;

public class BroadcastManager {

    private final static int CENTER_PX = 154;
    private int counter;

    public BroadcastManager() {
        FileConfiguration config = KnockBackFFA.INSTANCE.getPlugin().getConfig();
        Bukkit.getScheduler().scheduleAsyncRepeatingTask(KnockBackFFA.INSTANCE.getPlugin(), () -> {

            counter++;
            if (counter > Config.BROADCASTS) {
                counter = 1;
            }
            int count = 0;
            List<String> messages = config.getStringList("announcements.announcement" + counter);
            for (String message : messages) {
                count++;
                for (PlayerData data : DataManager.INSTANCE.getPlayerDataMap().values()) {
                    if (message.contains("[center]")) {
                        sendCenteredMessage(data.getPlayer(), message.replace("[center]", ""));
                    } else {
                        data.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                    }
                    if (count == 1)
                        data.saveData();
                }
            }
        }, 0, Config.DELAY);
    }

    public void sendCenteredMessage(Player player, String message) {
        if (message == null || message.equals(""))
            player.sendMessage("");
        message = ChatColor.translateAlternateColorCodes('&', message);

        int messagePxSize = 0;
        boolean previousCode = false;
        boolean isBold = false;


        for (char c : message.toCharArray()) {
            if (c == '�') {
                previousCode = true;
                continue;
            } else if (previousCode == true) {
                previousCode = false;
                if (c == 'l' || c == 'L') {
                    isBold = true;
                    continue;
                } else
                    isBold = false;
            } else {
                DefaultFontInfo dFI = DefaultFontInfo.getDefaultFontInfo(c);
                messagePxSize += isBold ? dFI.getBoldLength() : dFI.getLength();
                messagePxSize++;
            }
        }

        int halvedMessageSize = messagePxSize / 2;
        int toCompensate = CENTER_PX - halvedMessageSize;
        int spaceLength = DefaultFontInfo.SPACE.getLength() + 1;
        int compensated = 0;
        StringBuilder sb = new StringBuilder();
        while (compensated < toCompensate) {
            sb.append(" ");
            compensated += spaceLength;
        }
        player.sendMessage(sb + message);
    }
}