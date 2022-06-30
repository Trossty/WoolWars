package woolwars.woolwars.game;

import org.bukkit.entity.Player;
import woolwars.woolwars.enums.TeamType;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class GameTeam {

    private final Set<UUID> playerList;
    private final TeamType teamType;
    private int score;

    private int remaningPlayerCount;

    public GameTeam(final TeamType teamType){
        playerList = new HashSet<>();
        this.teamType = teamType;
        this.score = 0;
        this.remaningPlayerCount = 0;
    }

    public Set<UUID> getPlayerList() {
        return playerList;
    }

    public void addPlayer(Player player){
        playerList.add(player.getUniqueId());
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void removePlayer(Player player){
        removePlayer(player.getUniqueId());
    }

    public void removePlayer(UUID uuid){
        playerList.remove(uuid);
    }

    public TeamType getTeamType() {
        return teamType;
    }


    public int getRemaningPlayerCount() {
        return remaningPlayerCount;
    }

    public void setRemaningPlayerCount(int remaningPlayerCount) {
        this.remaningPlayerCount = remaningPlayerCount;
    }
}
