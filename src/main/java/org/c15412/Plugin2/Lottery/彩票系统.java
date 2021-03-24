package org.c15412.Plugin2.Lottery;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class 彩票系统 implements CommandExecutor, Listener {
    public static File 文件夹 = 获取.插件.getDataFolder();
    public static String 路径 = String.format("jdbc:sqlite://%s\\彩票.db", 文件夹.getAbsolutePath());
    public static Connection 连接;
    public static Statement 表格;
    public static String[] 开奖时间 = new String[100];

    /*public static String 查找开奖结果(String 期数){
        String 结果="aaaaaaaaaa";
        try {
            ResultSet 查找结果=表格.executeQuery(String.format("select %s from 彩票记录;",期数));
            结果 = 查找结果.getString(2).replace("[","").replace(", ","").replace("]","").trim();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return 结果;
    }*/
    public static int[][] 开奖结果 = new int[100][10];
    public static Runnable 彩票运行器 = new Runnable() {
        @Override
        public void run() {
            long 时间 = System.currentTimeMillis() + 120000;
            IntStream.rangeClosed(0, 98).forEach(i1 -> {
                开奖结果[i1] = 开奖结果[i1 + 1];
                开奖时间[i1] = 开奖时间[i1 + 1];
            });
            Random 随机数 = new Random();
            开奖时间[99] = "" + 时间;
            IntStream.rangeClosed(0, 9).forEach(i -> 开奖结果[99][i] = 随机数.nextInt(10));
            Bukkit.broadcastMessage("§b本期彩票时间为： §3" + 开奖时间[98] + "\n§b本期开奖号码为： " + Arrays.toString(开奖结果[98]));
            彩票记录(开奖时间[98], Arrays.toString(开奖结果[98]));
        }
    };

    public static void 彩票记录(String 期数, String 结果) {
        try {
            表格.executeUpdate(String.format("insert into PrizeHistory values('%s','%s');", 期数, 结果));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static ItemStack 彩票(int 数量, String 时间, int[] 彩票值) {
        ItemStack 彩票 = new ItemStack(Material.PAPER, 数量);
        ItemMeta 数据 = 彩票.getItemMeta();
        数据.setUnbreakable(true);
        ((Damageable) 数据).setDamage(100);
        数据.setDisplayName("§b彩票");
        List<String> 彩票注释 = new ArrayList<>(), s = IntStream.rangeClosed(0, 9).mapToObj(i -> String.valueOf(彩票值[i])).collect(Collectors.toList());
        彩票注释.add("§b时间：");
        彩票注释.add("§e" + 时间);
        彩票注释.add("§b号码：");
        IntStream.range(0, 10).mapToObj(i -> String.valueOf(彩票值[i])).forEach(彩票注释::add);
        数据.setLore(彩票注释);
        彩票.setItemMeta(数据);
        return 彩票;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        new BukkitRunnable() {
            @Override
            public void run() {
                运行();
                获取.终止进程(this.getTaskId());
            }

            void 运行() {
                if (sender instanceof Player) {
                    if ("买彩票cpltlotterytickets".contains(label.toLowerCase())) {
                        Player 玩家 = (Player) sender;
                        if (获取.主手数据(玩家).equals(new ItemStack(Material.PAPER).getItemMeta())) {
                            int 数量 = 获取.主手(玩家).getAmount();
                            int 经验值 = 40 * 数量;
                            int 玩家经验 = 玩家.getTotalExperience();
                            if (玩家经验 > 经验值) {
                                int[] 彩票值 = new int[10];
                                if (args.length <= 0) 发信息(玩家, "请输入购买的数字，输入R将随机生成");
                                for (int i = 0; i < 10; i++) {
                                    Random 随机数 = new Random();
                                    if (args.length == i) {
                                        IntStream.rangeClosed(i, 9).forEach(j -> 彩票值[j] = 随机数.nextInt(10));
                                        break;
                                    }
                                    if (args[i].charAt(0) == 'R' || args[i].charAt(0) == 'r') {
                                        彩票值[i] = 随机数.nextInt(10);
                                    } else if (获取.数值(args[i]) >= 0) 彩票值[i] = 获取.数值(args[i]) % 10;
                                }
                                new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        获取.同步指令(String.format("minecraft:xp add %s -%d", 玩家.getName(), 经验值));
                                        玩家.getEquipment().setItemInMainHand(彩票(数量, 开奖时间[99], 彩票值));
                                        this.cancel();
                                    }
                                }.runTask(获取.插件);
                                发信息(玩家, String.format("§b您购买了彩票%d张，号码为 §e%s§b，", 数量, Arrays.toString(彩票值)));
                                发信息(玩家, String.format("§b您本次购买所需要的§e%d点经验§b已扣除。", 经验值));
                            } else {
                                发信息(玩家, "§c尊敬的玩家，您的经验不足，§b每张彩票需要§e40点经验值§b，");
                                发信息(玩家, String.format("您本次购买共需要经验值 §e%d点 ，§c仍差 %d，请补充经验再来！", 经验值, 玩家经验 - 经验值));
                            }
                        } else 发信息(玩家, "§c请手里拿着一张或多张空白的纸再购买彩票！");
                    } else if ("彩票开奖kj".contains(label)) {
                        Player 玩家 = (Player) sender;
                        ItemStack 物品 = 获取.主手(玩家);
                        ItemMeta 物品数据 = 物品.getItemMeta();
                        if (物品.getType().equals(Material.PAPER) && 物品.hasItemMeta() && 物品数据.hasDisplayName()
                                && 物品数据.hasLore() && ((Damageable) 物品数据).getDamage() == 100
                                && 物品数据.getDisplayName().equals("§b彩票")) {
                            int 相似度;
                            int 数量 = 物品.getAmount();
                            String 期数 = 物品数据.getLore().get(1).replace("§e", "");
                            final String[] 对应开奖结果 = {"aaaaaaaaaa"};
                            try {
                                对应开奖结果[0] = (表格.executeQuery(String.format("SELECT * FROM PrizeHistory WHERE TIME == %s;", 期数)).getString("result")).replace("[", "").replace(", ", "").replace("]", "").trim();
                            } catch (SQLException throwables) {
                                throwables.printStackTrace();
                            }
                            if (!对应开奖结果[0].equals("aaaaaaaaaa")) {
                                相似度 = (int) IntStream.range(0, 10).filter(s -> 物品数据.getLore().get(s + 3).equals(String.valueOf(对应开奖结果[0].charAt(s)))).count();
                                int 数值 = 9 * 数量 * 相似度 * 相似度 * 相似度 * 相似度;
                                发信息(玩家, String.format("§b亲爱的玩家，第§e %s §b期彩票您获得了 §e经验值： %d 点。", 期数, 数值));
                                new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        获取.同步指令("minecraft:xp add " + 玩家.getName() + " " + 数值);
                                        获取.主手(玩家).setItemMeta((new ItemStack(Material.PAPER)).getItemMeta());
                                        this.cancel();
                                    }
                                }.runTask(获取.插件);
                            } else if (物品数据.getLore().get(1).equals("§e" + 开奖时间[99])) {
                                long 时间 = System.currentTimeMillis();
                                发信息(玩家, String.format("现在时间为§e %d §3，距离本期开奖仍差§e%d§3秒。", 时间, (int) ((Long.parseLong(开奖时间[99]) - 时间) / 1000)));
                            } else {
                                new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        物品.setItemMeta((new ItemStack(Material.PAPER)).getItemMeta());
                                        this.cancel();
                                    }
                                }.runTask(获取.插件);
                            }
                        } else 发信息(玩家, "§c请拿着有效彩票再来使用开奖指令！ ");
                    }
                } else 发信息(sender, "§c§l只有在游戏中的玩家可以使用本功能！");
                if ("彩票记录查询彩票记录".contains(label)) {
                    if (sender.hasPermission("LotteryS.checkLottery")) {
                        if (获取.提取数字(args[0]).length() > 0) {
                            if (Long.parseLong(获取.提取数字(args[0])) < 99)
                                IntStream.rangeClosed(0, Integer.parseInt(获取.提取数字(args[0]))).forEach(i -> 发信息(sender, String.format("§b第：§3%s §b期彩票开奖号码为： %s", 开奖时间[99 - i], Arrays.toString(开奖结果[99 - i]))));
                            else {
                                final String[] 对应开奖结果 = {"aaaaaaaaaa"};
                                String 对应开奖结果2 = null;
                                try {
                                    对应开奖结果2 = 表格.executeQuery(String.format("SELECT * FROM PrizeHistory WHERE TIME == %s;", args[0])).getString("result");
                                    对应开奖结果[0] = 对应开奖结果2.replace("[", "").replace(", ", "").replace("]", "").trim();
                                } catch (Exception ignored) {
                                }
                                if (!对应开奖结果[0].equals("aaaaaaaaaa"))
                                    发信息(sender, String.format("§b第：§3%s §b期彩票开奖号码为： %s", args[0], 对应开奖结果2));
                                else 发信息(sender, String.format("§c彩票期数 %s 错误，不存在该期彩票！ ", args[0]));
                            }
                        } else 发信息(sender, String.format("§c彩票期数 %s 错误，不存在该期彩票！ ", args[0]));
                    } else 发信息(sender, "§c您无权限查询彩票记录和历史开奖结果！");
                }
            }
        }.runTaskAsynchronously(获取.插件);

        return true;
    }

    void 发信息(CommandSender 玩家, String 信息) {
        玩家.sendMessage(String.format("§a§l「彩票系统」： §3%s", 信息));
    }
}

