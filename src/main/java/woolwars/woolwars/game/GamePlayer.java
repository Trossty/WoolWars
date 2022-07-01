package woolwars.woolwars.game;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import woolwars.woolwars.enums.TeamType;
import woolwars.woolwars.game.classes.AbstractClass;

import java.util.*;

public class GamePlayer {

    private static final Map<UUID, GamePlayer> gamePlayerMap = new HashMap<>();

    private final UUID playerUUID;
    private GameTeam team = new GameTeam(TeamType.NONE);
    private int killCount = 0;

    private AbstractClass abstractClass;

    public GamePlayer(UUID uuid){
        playerUUID = uuid;
        gamePlayerMap.put(uuid,this);
        abstractClass=null;
    }

    public GamePlayer(Player player){
        this(player.getUniqueId());
    }

    public void setTeam(GameTeam team) {
        if(this.team!=null){
            if(this.team==team) return;
            this.team.removePlayer(Objects.requireNonNull(Bukkit.getPlayer(playerUUID)));
        }
        this.team = team;
        this.team.addPlayer(Objects.requireNonNull(Bukkit.getPlayer(playerUUID)));
    }

    public GameTeam getTeam() {
        return team;
    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }

    public static Optional<GamePlayer> getGamePlayer(Player player){
        return getGamePlayer(player.getUniqueId());
    }

    public static Optional<GamePlayer> getGamePlayer(UUID uuid){
        if(!gamePlayerMap.containsKey(uuid)){
            gamePlayerMap.put(uuid,new GamePlayer(uuid));
        }
        return Optional.of(gamePlayerMap.get(uuid));
    }

    public static void removePlayer(UUID uuid){

        GamePlayer gamePlayer = getGamePlayer(uuid).get();

        gamePlayer.setTeam(new GameTeam(TeamType.NONE));

        gamePlayerMap.remove(uuid);
    }

    public static Map<UUID, GamePlayer> getGamePlayerMap() {
        return gamePlayerMap;
    }

    public int getKillCount() {
        return killCount;
    }

    public void setKillCount(int killCount) {
        this.killCount = killCount;
    }

    public void setAbstractClass(AbstractClass abstractClass) {
        if(this.abstractClass!=null){
            this.abstractClass.removePlayer(getPlayerUUID());
        }
        abstractClass.addPlayer(getPlayerUUID());
        this.abstractClass = abstractClass;
    }

    public AbstractClass getAbstractClass() {
        return abstractClass;
    }
}
