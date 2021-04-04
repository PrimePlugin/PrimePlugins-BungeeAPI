package de.primeapi.primeplugins.bungeeapi.sql;

import de.primeapi.primeplugins.bungeeapi.configs.CoreConfig;
import lombok.AllArgsConstructor;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.UUID;
import de.primeapi.primeplugins.bungeeapi.PrimeCore;

@AllArgsConstructor
public class SQLPlayer {
    public Integer id;
    public UUID uuid;

    public static SQLPlayer create(UUID uuid, String name){
        Integer id = null;
        try {
            PreparedStatement st = PrimeCore.getInstance().getConnection().prepareStatement("INSERT INTO core_players values (id, ?,?,?,?)");
            st.setString(1, uuid.toString());
            st.setString(2, name.toLowerCase());
            st.setString(3, name);
            st.setInt(4, CoreConfig.getInstance().getInt("settings.coins.startAmount"));
            st.executeUpdate();
            ResultSet rs = st.getGeneratedKeys();
            if(rs.next()){
                id = rs.getInt(1);
            }
            rs.close();
            st.close();
        } catch (SQLException throwables) {
            return new SQLPlayer(uuid);
        }
        assert (id != null);
        return new SQLPlayer(id);
    }

    public static SQLPlayer loadPlayerByName(String name){
        Integer id = null;
        try {
            PreparedStatement st = PrimeCore.getInstance().getConnection().prepareStatement("SELECT id FROM core_players WHERE name = ?;");
            st.setString(1, name.toLowerCase());
            ResultSet rs = st.executeQuery();
            if(rs.next()){
                id = rs.getInt("id");
            }
            rs.close();
            st.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        if(id == null){
            return null;
        }
        return new SQLPlayer(id);
    }


    public SQLPlayer(UUID uuid){
        this.uuid = uuid;
    }

    private SQLPlayer(Integer id){
        this.id = id;
    }

    public void load() {
        if(Objects.isNull(id)){
            try {
                PreparedStatement st = PrimeCore.getInstance().getConnection().prepareStatement("SELECT id FROM core_players WHERE uuid=?");
                st.setString(1, uuid.toString());
                ResultSet rs = st.executeQuery();
                if(rs.next()){
                    id = rs.getInt("id");
                    rs.close();
                    st.close();
                }else {
                    rs.close();
                    st.close();
                    throw new IllegalArgumentException("Player with UUID '" + uuid.toString() + "' not found");
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    public String getName(){
        load();
        String s = null;
        try {
            PreparedStatement st = PrimeCore.getInstance().getConnection().prepareStatement("SELECT name FROM core_players WHERE id = ?");
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            if(rs.next()){
                s = rs.getString("name");
            }
            rs.close();
            st.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return s;
    }
    public UUID getUniqueId(){
        if(uuid != null){
            return uuid;
        }else {
            load();
            String s = null;
            try {
                PreparedStatement st = PrimeCore.getInstance().getConnection().prepareStatement("SELECT uuid FROM core_players WHERE id = ?");
                st.setInt(1, id);
                ResultSet rs = st.executeQuery();
                if (rs.next()) {
                    s = rs.getString("uuid");
                }
                rs.close();
                st.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            assert s != null;
            return UUID.fromString(s);
    }
    }
    public String getRealName(){
        load();
        String s = null;
        try {
            PreparedStatement st = PrimeCore.getInstance().getConnection().prepareStatement("SELECT realname FROM core_players WHERE id = ?");
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            if(rs.next()){
                s = rs.getString("realname");
            }
            rs.close();
            st.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return s;
    }
    public int getCoins(){
        load();
        int i  = 0;
        try {
            PreparedStatement st = PrimeCore.getInstance().getConnection().prepareStatement("SELECT coins FROM core_players WHERE id = ?");
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            if(rs.next()){
                i = rs.getInt("coins");
            }
            rs.close();
            st.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return i;
    }

    public void setCoins(int i){
        load();
        PrimeCore.getInstance().getThreadPoolExecutor().submit(() -> {
                try {
                    PreparedStatement st = PrimeCore.getInstance().getConnection().prepareStatement("UPDATE core_players SET coins = ? WHERE id=?");
                    st.setInt(1, i);
                    st.setInt(2, id);
                    st.execute();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
        });
    }
    public void updateName(String name){
        load();
        PrimeCore.getInstance().getThreadPoolExecutor().submit(() -> {
        try {
            PreparedStatement st = PrimeCore.getInstance().getConnection().prepareStatement("UPDATE core_players SET name = ?,realname = ? WHERE id=?");
            st.setString(1, name.toLowerCase());
            st.setString(2, name);
            st.setInt(3, id);
            st.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        });
    }

    public void addCoins(int i){
        setCoins(getCoins() + i);
    }

}