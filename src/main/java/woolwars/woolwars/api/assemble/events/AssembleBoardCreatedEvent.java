package woolwars.woolwars.api.assemble.events;


import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import woolwars.woolwars.api.assemble.AssembleBoard;

public class AssembleBoardCreatedEvent extends Event {

    public static HandlerList handlerList = new HandlerList();

    private boolean cancelled = false;
    private final AssembleBoard board;

    /**
     * Assemble Board Created Event.
     *
     * @param board of player.
     */
    public AssembleBoardCreatedEvent(AssembleBoard board) {
        this.board = board;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

    public static void setHandlerList(HandlerList handlerList) {
        AssembleBoardCreatedEvent.handlerList = handlerList;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public AssembleBoard getBoard() {
        return board;
    }
}
