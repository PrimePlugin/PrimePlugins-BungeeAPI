package de.primeapi.primeplugins.bungeeapi.sql;

import de.primeapi.primeplugins.bungeeapi.PrimeCore;
import de.primeapi.primeplugins.bungeeapi.configs.CoreConfig;
import de.primeapi.primeplugins.bungeeapi.enums.PlayerData;
import de.primeapi.primeplugins.bungeeapi.enums.PlayerSetting;
import de.primeapi.util.sql.queries.Retriever;
import lombok.AllArgsConstructor;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.UUID;

@AllArgsConstructor
public class SQLPlayer {
	public Integer id;
	public UUID uuid;

	public SQLPlayer(UUID uuid) {
		this.uuid = uuid;
	}

	private SQLPlayer(Integer id) {
		this.id = id;
	}

	public static Retriever<SQLPlayer> create(UUID uuid, String name) {
		return new Retriever<>(() -> {
			Integer id = null;
			try {
				PreparedStatement st = PrimeCore.getInstance()
				                                .getConnection()
				                                .prepareStatement("INSERT INTO core_players values (id, ?,?,?,?,?)");
				st.setString(1, uuid.toString());
				st.setString(2, name.toLowerCase());
				st.setString(3, name);
				st.setInt(4, CoreConfig.getInstance().getInt("settings.coins.startAmount"));
				st.setInt(5, 0);
				st.executeUpdate();
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					id = rs.getInt(1);
				}
				rs.close();
				st.close();
			} catch (SQLException throwables) {
				return new SQLPlayer(uuid);
			}
			assert (id != null);
			return new SQLPlayer(id);
		});
	}

	public static Retriever<SQLPlayer> loadPlayerByName(String name) {
		return new Retriever<>(() -> {
			Integer id = null;
			try {
				PreparedStatement st = PrimeCore.getInstance()
				                                .getConnection()
				                                .prepareStatement("SELECT id FROM core_players WHERE name = ?;");
				st.setString(1, name.toLowerCase());
				ResultSet rs = st.executeQuery();
				if (rs.next()) {
					id = rs.getInt("id");
				}
				rs.close();
				st.close();
			} catch (SQLException throwables) {
				throwables.printStackTrace();
			}

			if (id == null) {
				return null;
			}
			return new SQLPlayer(id);
		});
	}

	public void load() {
		if (Objects.isNull(id)) {
			try {
				PreparedStatement st = PrimeCore.getInstance()
				                                .getConnection()
				                                .prepareStatement("SELECT id,uuid FROM core_players WHERE uuid=?");
				st.setString(1, uuid.toString());
				ResultSet rs = st.executeQuery();
				if (rs.next()) {
					id = rs.getInt("id");
					uuid = UUID.fromString(rs.getString("uuid"));
					rs.close();
					st.close();
				} else {
					rs.close();
					st.close();
					throw new IllegalArgumentException("Player with UUID '" + uuid.toString() + "' not found");
				}
			} catch (SQLException throwables) {
				throwables.printStackTrace();
			}
		}
	}

	public Retriever<String> retrieveName() {
		return new Retriever<>(() -> {
			load();
			String s = null;
			try {
				PreparedStatement st = PrimeCore.getInstance()
				                                .getConnection()
				                                .prepareStatement("SELECT name FROM core_players WHERE id = ?");
				st.setInt(1, id);
				ResultSet rs = st.executeQuery();
				if (rs.next()) {
					s = rs.getString("name");
				}
				rs.close();
				st.close();
			} catch (SQLException throwables) {
				throwables.printStackTrace();
			}
			return s;
		});
	}

	public Retriever<UUID> retrieveUniqueId() {
		return new Retriever<>(() -> {
			if (uuid != null) {
				return uuid;
			} else {
				load();
				String s = null;
				try {
					PreparedStatement st = PrimeCore.getInstance()
					                                .getConnection()
					                                .prepareStatement("SELECT uuid FROM core_players WHERE id = ?");
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
		});
	}

	public Retriever<String> retrieveRealName() {
		return new Retriever<>(() -> {
			load();
			String s = null;
			try {
				PreparedStatement st = PrimeCore.getInstance()
				                                .getConnection()
				                                .prepareStatement("SELECT realname FROM core_players WHERE id = ?");
				st.setInt(1, id);
				ResultSet rs = st.executeQuery();
				if (rs.next()) {
					s = rs.getString("realname");
				}
				rs.close();
				st.close();
			} catch (SQLException throwables) {
				throwables.printStackTrace();
			}
			return s;
		});
	}

	public Retriever<Integer> retrieveCoins() {
		return new Retriever<>(() -> {
			load();
			int i = 0;
			try {
				PreparedStatement st = PrimeCore.getInstance()
				                                .getConnection()
				                                .prepareStatement("SELECT coins FROM core_players WHERE id = ?");
				st.setInt(1, id);
				ResultSet rs = st.executeQuery();
				if (rs.next()) {
					i = rs.getInt("coins");
				}
				rs.close();
				st.close();
			} catch (SQLException throwables) {
				throwables.printStackTrace();
			}
			return i;
		});
	}

	public void setCoins(int i) {
		load();
		PrimeCore.getInstance().getThreadPoolExecutor().submit(() -> {
			try {
				PreparedStatement st = PrimeCore.getInstance()
				                                .getConnection()
				                                .prepareStatement("UPDATE core_players SET coins = ? WHERE id=?");
				st.setInt(1, i);
				st.setInt(2, id);
				st.execute();
			} catch (SQLException throwables) {
				throwables.printStackTrace();
			}
		});
	}

	public void updateName(String name) {
		load();
		PrimeCore.getInstance().getThreadPoolExecutor().submit(() -> {
			try {
				PreparedStatement st = PrimeCore.getInstance()
				                                .getConnection()
				                                .prepareStatement(
						                                "UPDATE core_players SET name = ?,realname = ? WHERE id=?");
				st.setString(1, name.toLowerCase());
				st.setString(2, name);
				st.setInt(3, id);
				st.execute();
			} catch (SQLException throwables) {
				throwables.printStackTrace();
			}
		});
	}

	public Retriever<Integer> retrieveOnMins() {
		return new Retriever<>(() -> {
			load();
			int i = 0;
			try {
				PreparedStatement st = PrimeCore.getInstance()
				                                .getConnection()
				                                .prepareStatement("SELECT playtime FROM core_players WHERE id = ?");
				st.setInt(1, id);
				ResultSet rs = st.executeQuery();
				if (rs.next()) {
					i = rs.getInt("playtime");
				}
				rs.close();
				st.close();
			} catch (SQLException throwables) {
				throwables.printStackTrace();
			}
			return i;
		});
	}

	public void setOnMins(int i) {
		load();
		PrimeCore.getInstance().getThreadPoolExecutor().submit(() -> {
			try {
				PreparedStatement st = PrimeCore.getInstance()
				                                .getConnection()
				                                .prepareStatement("UPDATE core_players SET playtime = ? WHERE id=?");
				st.setInt(1, i);
				st.setInt(2, id);
				st.execute();
			} catch (SQLException throwables) {
				throwables.printStackTrace();
			}
		});
	}

	public void addOnMins(int i) {
		retrieveOnMins().submit(integer -> {
			setOnMins(integer + i);
		});
	}

	public void addCoins(int i) {
		retrieveCoins().submit(integer -> {
			setCoins(integer + i);
		});
	}

	public void removeCoins(int i) {
		retrieveCoins().submit(integer -> {
			setCoins(integer - i);
		});
	}

	public Retriever<Integer> retrieveSetting(PlayerSetting setting) {
		return new Retriever<>(() -> {
			load();
			Integer i = null;
			try {
				PreparedStatement st = PrimeCore.getInstance().getConnection().prepareStatement(
						"SELECT * FROM core_settings WHERE uuid = ? AND setting = ?"
				                                                                               );
				st.setString(1, retrieveUniqueId().complete().toString());
				st.setString(2, setting.toString());
				ResultSet rs = st.executeQuery();
				if (rs.next()) {
					i = rs.getInt("value");
				}
				rs.close();
				st.close();
			} catch (SQLException throwables) {
				throwables.printStackTrace();
			}
			return i;
		});
	}

	public Retriever<Integer> retrieveSettingSave(PlayerSetting setting) {
		return new Retriever<>(() -> {
			Integer i = retrieveSetting(setting).complete();
			if (Objects.isNull(i)) {
				return setting.getStandartValue();
			} else {
				return i;
			}
		});
	}

	public void setSetting(PlayerSetting setting, int value) {
		load();

		PrimeCore.getInstance().getThreadPoolExecutor().submit(() -> {
			if (Objects.isNull(retrieveSetting(setting).complete())) {
				try {
					PreparedStatement st = PrimeCore.getInstance().getConnection().prepareStatement(
							"INSERT INTO core_settings value (id, ?,?,?)"
					                                                                               );
					st.setString(1, retrieveUniqueId().complete().toString());
					st.setString(2, setting.toString());
					st.setInt(3, value);
					st.execute();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			} else {
				try {
					PreparedStatement st = PrimeCore.getInstance().getConnection().prepareStatement(
							"UPDATE core_settings SET value = ? WHERE uuid = ? AND setting = ?"
					                                                                               );
					st.setInt(1, value);
					st.setString(2, retrieveUniqueId().complete().toString());
					st.setString(3, setting.toString());
					st.execute();
				} catch (SQLException throwables) {
					throwables.printStackTrace();
				}
			}
		});
	}

	public Retriever<String> retrieveData(PlayerData type) {
		return retrieveData(type.toString());
	}

	public Retriever<String> retrieveData(String type) {
		return new Retriever<>(() -> {
			load();
			String s = null;
			try {
				PreparedStatement st = PrimeCore.getInstance().getConnection().prepareStatement(
						"SELECT * FROM core_playerdata WHERE uuid = ? AND type = ?"
				                                                                               );
				st.setString(1, retrieveUniqueId().complete().toString());
				st.setString(2, type);
				ResultSet rs = st.executeQuery();
				if (rs.next()) {
					s = rs.getString("value");
				}
				rs.close();
				st.close();
			} catch (SQLException throwables) {
				throwables.printStackTrace();
			}
			return s;
		});
	}

	public void setData(PlayerData type, String data) {
		setData(type.toString(), data);
	}

	public void setData(String type, String data) {
		load();


		retrieveData(type).submit(s -> {
			if (Objects.isNull(s)) {
				try {
					PreparedStatement st = PrimeCore.getInstance().getConnection().prepareStatement(
							"INSERT INTO core_playerdata value (id, ?,?,?)"
					                                                                               );
					st.setString(1, retrieveUniqueId().complete().toString());
					st.setString(2, type);
					st.setString(3, data);
					st.execute();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			} else {
				try {
					PreparedStatement st = PrimeCore.getInstance().getConnection().prepareStatement(
							"UPDATE core_playerdata SET value = ? WHERE uuid = ? AND type = ?"
					                                                                               );
					st.setString(1, data);
					st.setString(2, retrieveUniqueId().complete().toString());
					st.setString(3, type);
					st.execute();
				} catch (SQLException throwables) {
					throwables.printStackTrace();
				}
			}
		});
	}


}
