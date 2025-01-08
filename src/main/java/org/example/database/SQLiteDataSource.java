package org.example.database;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLiteDataSource {
    private static final Logger LOGGER = LoggerFactory.getLogger(SQLiteDataSource.class);
    private static final HikariConfig config = new HikariConfig();
    private static final HikariDataSource ds;
//if it doesn't work put ../
    //also if you are going to export put ./
    static {
        String databasePath = "./database.db"; // Update the path to the desired location of the database file
        File dbFile = new File(databasePath);

        try {
            if (!dbFile.exists()) {
                File parentDir = dbFile.getParentFile();
                if (!parentDir.exists()) {
                    parentDir.mkdirs();
                }
                if (dbFile.createNewFile()) {
                    LOGGER.info("Created database file");
                } else {
                    LOGGER.info("Could not create database file");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        config.setJdbcUrl("jdbc:sqlite:" + databasePath);
        config.setConnectionTestQuery("SELECT 1");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheCqlLimit", "2048");
        config.setMaximumPoolSize(15);
        config.setMaxLifetime(1800000);
        config.setMinimumIdle(5);
        ds = new HikariDataSource(config);

        try (final Connection connection = getConnection();
             final Statement statement = connection.createStatement()) {

            statement.execute("CREATE TABLE IF NOT EXISTS guild_settings (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "guild_id VARCHAR(20) NOT NULL," +
                    "prefix VARCHAR(255) NOT NULL DEFAULT 'i!'" +
                    ");");

            statement.execute("CREATE TABLE IF NOT EXISTS user_berries (" +
                    "user_id VARCHAR(20) PRIMARY KEY," +
                    "berry_count INTEGER NOT NULL DEFAULT 0" +
                    ");");

            statement.execute("CREATE TABLE IF NOT EXISTS ignored_channels (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "guild_id VARCHAR(20) NOT NULL," +
                    "channel_id VARCHAR(20) NOT NULL" +
                    ");");

            statement.execute("CREATE TABLE IF NOT EXISTS inventory (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "user_id VARCHAR(20) NOT NULL," +
                    "item_name VARCHAR(100) NOT NULL" +
                    ");");

            statement.execute("CREATE TABLE IF NOT EXISTS items (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name VARCHAR(100) NOT NULL," +
                    "price INTEGER NOT NULL" +
                    ");");


            statement.execute("CREATE TABLE IF NOT EXISTS hatch_log (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "user_id VARCHAR(20) NOT NULL," +
                    "pet VARCHAR(100) NOT NULL" +
                    ");");

            statement.execute("CREATE TABLE IF NOT EXISTS key_log (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "user_id VARCHAR(20) NOT NULL," +
                    "loot VARCHAR(100) NOT NULL" +
                    ");");

            statement.execute("CREATE TABLE IF NOT EXISTS user_guilds (" +
                    "user_id VARCHAR(20) NOT NULL," +
                    "guild_id VARCHAR(20) NOT NULL," +
                    "PRIMARY KEY (user_id, guild_id)" +
                    ");");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private SQLiteDataSource() {
    }

    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }
}