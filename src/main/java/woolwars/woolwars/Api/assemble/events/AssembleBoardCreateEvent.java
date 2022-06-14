package woolwars.woolwars.Api.assemble.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class AssembleBoardCreateEvent extends Event implements Cancellable {

    public static HandlerList handlerList = new HandlerList();

    private Player player;
    private boolean cancelled = false;

    /**
     * Assemble Board Create Event.
     *
     * @param player that the board is being created for.
     */
    public AssembleBoardCreateEvent(Player player) {
        this.player = player;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public Player getPlayer() {
        return player;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public static void setHandlerList(HandlerList handlerList) {
        AssembleBoardCreateEvent.handlerList = handlerList;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
