// 
// Decompiled by Procyon v0.5.36
// 

package cat.yoink.xanax.client.friend;

import java.util.function.Predicate;
import java.util.ArrayList;
import java.util.List;

public enum FriendManager
{
    INSTANCE;
    
    private final List<String> friends;
    
    private FriendManager() {
        this.friends = new ArrayList<String>();
    }
    
    public boolean isFriend(final String name) {
        return this.friends.stream().anyMatch(name::equalsIgnoreCase);
    }
    
    public void addFriend(final String name) {
        this.friends.add(name);
    }
    
    public void removeFriend(final String name) {
        this.friends.removeIf(f -> f.equalsIgnoreCase(name));
    }
    
    public List<String> getFriends() {
        return this.friends;
    }
}
