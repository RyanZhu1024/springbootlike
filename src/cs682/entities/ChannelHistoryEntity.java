package cs682.entities;

import java.util.Collection;

/**
 * Created by RyanZhu on 05/02/2017.
 */
public class ChannelHistoryEntity extends BaseEntity {
    public ChannelHistoryEntity(boolean success) {
        super(success);
    }

    private Collection<String> messages;

    public Collection<String> getMessages() {
        return messages;
    }

    public void setMessages(Collection<String> messages) {
        this.messages = messages;
    }
}
