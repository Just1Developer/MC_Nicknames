package net.justonedev.mc.nicknames.prefix.scoreboard;

import net.justonedev.mc.nicknames.Nicknames;
import net.justonedev.mc.nicknames.prefix.PrefixHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.Objects;

public class ScoreboardPrefixHandler implements PrefixHandler {

    private static Scoreboard scoreboard;
    private static Objective objective;

    public ScoreboardPrefixHandler() {
        assert Bukkit.getScoreboardManager() != null;
        scoreboard = Objects.requireNonNull(Bukkit.getScoreboardManager()).getNewScoreboard();
        objective = scoreboard.registerNewObjective("__prefix", Criteria.create("__none"), "displayNameNone");

        updateNicknames();
    }

    public void updateNicknames() {
        if (Nicknames.singleton.allNicknames == null) {
            Nicknames.singleton.getLogger().warning("Could not update nicknames because allNicknames list is null.");
            return;
        }
        for (String nick : Nicknames.singleton.allNicknames) {
            registerNickname(nick);
        }
    }

    public void registerNickname(String nickname) {
        if (scoreboard.getTeam(nickname) != null) return;
        Team t = scoreboard.registerNewTeam(nickname);
        t.setPrefix(nickname);
    }

    /**
     * Called when a player joins, makes all prefixes visible to the given player.
     * In the current implementation, this just means adding them to the scoreboard.
     * @param p The player.
     */
    public void setNickname(Player p) {
        p.setScoreboard(scoreboard);
    }

    /**
     * Updates a given players nickname to the provided nickname for all players to see.
     * In the current implementation, it is crucial that the nickname provided is a registered nickname.
     * @param p The player.
     * @param nickname The nickname.
     */
    public void setNickname(Player p, String nickname) {
        Team team = scoreboard.getTeam(nickname);
        if (team == null) {
            System.out.println("Tried to set the nickname of player " + p.getName() + " to " + nickname + ", but it wasn't registered.");
            return;
        }
        if (!team.getEntries().contains(p.getName())) team.addEntry(p.getName());
    }

}
