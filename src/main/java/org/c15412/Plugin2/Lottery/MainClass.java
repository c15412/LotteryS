package org.c15412.Plugin2.Lottery;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;


public class MainClass extends JavaPlugin {

    @Override
    public void onLoad() {
        super.onLoad();
        控制台信息("§b§l彩票插件已经开始加载。");
    }

    @Override
    public void onEnable() {
        super.onEnable();

        getCommand("彩票").setExecutor(new 彩票系统());
        try {
            彩票系统.文件夹.mkdir();
            彩票系统.连接 = DriverManager.getConnection(彩票系统.路径);
            彩票系统.表格 = 彩票系统.连接.createStatement();
            彩票系统.表格.executeUpdate("create table if not exists PrizeHistory(TIME String, result String, PRIMARY KEY (TIME,result));");
            彩票系统.表格.executeUpdate("create table if not exists BuyHistory(TIME String, Number String, UUID String, PRIMARY KEY (UUID));");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }//启用彩票系统及其数据库

        控制台信息("§b§l彩票插件已启用。");

        Bukkit.getScheduler().runTaskTimerAsynchronously(获取.插件, 彩票系统.彩票运行器, 40, 2400);

    }

    @Override
    public void onDisable() {
        super.onDisable();

        try {
            if (彩票系统.开奖时间[9]!=null)彩票系统.彩票记录(彩票系统.开奖时间[9], Arrays.toString(彩票系统.开奖结果[9]));
            彩票系统.表格.close();
            彩票系统.连接.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }//停用数据库

    }

    private void 控制台信息(String 信息) {
        super.getServer().getConsoleSender().sendMessage(String.format("§a§l「彩票系统」： §3 %s", 信息));
    }
}
