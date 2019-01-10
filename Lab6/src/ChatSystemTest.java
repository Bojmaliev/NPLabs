import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class ChatSystemTest {

    public static void main(String[] args) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, NoSuchRoomException {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if (k == 0) {
            ChatRoom cr = new ChatRoom(jin.next());
            int n = jin.nextInt();
            for (int i = 0; i < n; ++i) {
                k = jin.nextInt();
                if (k == 0) cr.addUser(jin.next());
                if (k == 1) cr.removeUser(jin.next());
                if (k == 2) System.out.println(cr.hasUser(jin.next()));
            }
            System.out.println("");
            System.out.println(cr.toString());
            n = jin.nextInt();
            if (n == 0) return;
            ChatRoom cr2 = new ChatRoom(jin.next());
            for (int i = 0; i < n; ++i) {
                k = jin.nextInt();
                if (k == 0) cr2.addUser(jin.next());
                if (k == 1) cr2.removeUser(jin.next());
                if (k == 2) cr2.hasUser(jin.next());
            }
            System.out.println(cr2.toString());
        }
        if (k == 1) {
            ChatSystem cs = new ChatSystem();
            Method mts[] = cs.getClass().getDeclaredMethods();
            while (true) {
                String cmd = jin.next();
                if (cmd.equals("stop")) break;
                if (cmd.equals("print")) {
                    System.out.println(cs.getRoom(jin.next()) + "\n");
                    continue;
                }
                for (Method m : mts) {
                    if (m.getName().equals(cmd)) {
                        String params[] = new String[m.getParameterTypes().length];
                        for (int i = 0; i < params.length; ++i) params[i] = jin.next();
                        m.invoke(cs, params);
                    }
                }
            }
        }
    }

}

class NoSuchRoomException extends RuntimeException {
    NoSuchRoomException(String roomName) {
        super(roomName);
    }
}

class NoSuchUserException extends RuntimeException {
    NoSuchUserException(String userName) {
        super(userName);
    }
}

class ChatRoom {
    String name;
    Set<String> users;

    ChatRoom(String name) {
        this.name = name;
        users = new TreeSet<>();
    }

    void addUser(String user) {
        users.add(user);
    }

    void removeUser(String user) {
        users.remove(user);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append("\n");
        if (users.size() == 0) {
            sb.append("EMPTY").append("\n");
        } else {
            for (String user : users) {
                sb.append(user).append("\n");
            }
        }
        return sb.toString();
    }

    boolean hasUser(String user) {
        return users.contains(user);
    }

    int numUsers() {
        return users.size();
    }
}

class ChatSystem {
    TreeMap<String, ChatRoom> rooms;
    HashSet<String> users;

    ChatSystem() {
        rooms = new TreeMap<>();
        users = new HashSet<>();
    }

    void addRoom(String roomName) {
        rooms.put(roomName, new ChatRoom(roomName));
    }

    void removeRoom(String roomName) {
        rooms.remove(roomName);
    }

    ChatRoom getRoom(String roomName) {
        if (!rooms.containsKey(roomName)) throw new NoSuchRoomException(roomName);
        return rooms.get(roomName);
    }

    void joinToRoomWithLowestNumUsers(String userName) {
        if (rooms.size() == 0) return;
        int lowestNum  = rooms.values().stream().mapToInt(ChatRoom::numUsers).min().getAsInt();
        ChatRoom cr = rooms.values().stream().filter(a-> a.numUsers() == lowestNum).findFirst().get();
        cr.addUser(userName);
    }

    void register(String userName) {
        users.add(userName);
        joinToRoomWithLowestNumUsers(userName);

    }

    void registerAndJoin(String userName, String roomName) {
        users.add(userName);
        joinRoom(userName, roomName);

    }

    void joinRoom(String userName, String roomName) {
        checkExist(userName, roomName);
        rooms.get(roomName).addUser(userName);
    }

    void leaveRoom(String userName, String roomName) {
        checkExist(userName, roomName);
        rooms.get(roomName).removeUser(userName);
    }

    void checkExist(String userName, String roomName) {
        userExists(userName);
        if (!rooms.containsKey(roomName)) throw new NoSuchRoomException(roomName);
    }

    void userExists(String userName) {
        if (!users.contains(userName)) throw new NoSuchUserException(userName);
    }

    void followFriend(String userName, String friendUserName) {
        userExists(userName);
        userExists(friendUserName);
        rooms.values().forEach(a -> {
            if (a.hasUser(friendUserName)) {
                a.addUser(userName);
            }
        });
    }
}
