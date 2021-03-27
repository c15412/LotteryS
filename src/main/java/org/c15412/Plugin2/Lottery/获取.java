package org.c15412.Plugin2.Lottery;

import org.bukkit.*;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.bukkit.Bukkit.getScheduler;
import static org.bukkit.Bukkit.getServer;

public class 获取 {

    public static Plugin 插件 = getServer().getPluginManager().getPlugin("LotteryS");
    public static ConsoleCommandSender 控制台 = getServer().getConsoleSender();
    public final Logger logger = Logger.getLogger("Get-M");

    public static ItemStack 主手(LivingEntity 玩家) {
        return 玩家.getEquipment().getItemInMainHand();
    }

    public static ItemStack 副手(LivingEntity 玩家) {
        return 玩家.getEquipment().getItemInOffHand();
    }

    public static ItemStack 帽子(LivingEntity 玩家) {
        return 玩家.getEquipment().getHelmet();
    }

    public static ItemStack 衣服(LivingEntity 玩家) {
        return 玩家.getEquipment().getChestplate();
    }

    public static ItemStack 裤子(LivingEntity 玩家) {
        return 玩家.getEquipment().getLeggings();
    }

    public static ItemStack 鞋子(LivingEntity 玩家) {
        return 玩家.getEquipment().getBoots();
    }

    public static ItemMeta 物品数据(ItemStack 物品) {
        return 物品.getItemMeta();
    }

    public static ItemMeta 主手数据(LivingEntity 玩家) {
        return 主手(玩家).getItemMeta();
    }

    public static ItemMeta 副手数据(LivingEntity 玩家) {
        return 副手(玩家).getItemMeta();
    }

    public static ItemMeta 帽子数据(LivingEntity 玩家) {
        return 帽子(玩家).getItemMeta();
    }

    public static ItemMeta 衣服数据(LivingEntity 玩家) {
        return 衣服(玩家).getItemMeta();
    }

    public static ItemMeta 裤子数据(LivingEntity 玩家) {
        return 裤子(玩家).getItemMeta();
    }

    public static ItemMeta 鞋子数据(LivingEntity 玩家) {
        return 鞋子(玩家).getItemMeta();
    }

    public static Location MidLocation(Location 位置1, Location 位置2) {
        return 中点位置(位置1, 位置2);
    }

    public static Location 中点位置(Location 位置1, Location 位置2) {
        位置1.setX((位置1.getBlockX() + 位置2.getBlockX()) * 0.5);
        位置1.setY((位置1.getBlockY() + 位置2.getBlockY()) * 0.5);
        位置1.setZ((位置1.getBlockZ() + 位置2.getBlockZ()) * 0.5);
        return 位置1;
    }

    public static Inventory 基础物资() {
        Inventory 新盒子 = Bukkit.createInventory(null, 18, "§e§l新手物资盒子 §c§l为节约服务器开销，请根据需要自行领取");
        新盒子.addItem(new ItemStack(Material.COOKED_BEEF, 16));
        新盒子.addItem(new ItemStack(Material.IRON_AXE, 1));
        新盒子.addItem(new ItemStack(Material.IRON_PICKAXE, 1));
        新盒子.addItem(new ItemStack(Material.IRON_SWORD, 1));
        新盒子.addItem(new ItemStack(Material.IRON_SHOVEL, 1));
        新盒子.addItem(new ItemStack(Material.FISHING_ROD, 1));
        新盒子.addItem(new ItemStack(Material.TORCH, 16));
        新盒子.addItem(new ItemStack(Material.COBBLESTONE, 64));
        return 新盒子;
    }

    public static int 经验(Player 玩家) {
        return 玩家.getTotalExperience();
    }

    public static int 饥饿值(Player 玩家) {
        return 玩家.getFoodLevel();
    }

    public static int 数值(String 字符串) {
        String 数字 = 提取数字(字符串);
        if (数字.length() > 0) return Integer.parseInt(数字);
        else return 0;
    }

    public static String 提取字符(String 字符串, String 需要被提取的字符) {
        String 正则表达式 = String.format("[^%s]", 需要被提取的字符.trim());
        Pattern p = Pattern.compile(正则表达式);
        Matcher 标记器 = p.matcher(字符串);
        return 标记器.replaceAll("").trim();
    }
/*
    public static String 数字(String 字符串, String 正则表达式){
        if (正则表达式==null)正则表达式 = "[^0-9]";
        Pattern p = Pattern.compile(正则表达式);
        Matcher 标记器 = p.matcher(字符串);
        return 标记器.replaceAll("").trim();
    }
    public static String 提取数字(String 字符串){
        String 正则表达式 = "[^0123456789-]";
        Pattern p = Pattern.compile(正则表达式);
        Matcher 标记器 = p.matcher(字符串);
        return 标记器.replaceAll("").trim();
    }
*/

    public static String 提取数字(String 字符串) {
        return 提取字符(字符串, "0-9");
    }

    public static String 数字(String 字符串) {
        return 提取字符(字符串, "0-9");
    }

    public static String 物品名称(ItemStack 物品) {
        String 名称 = 物品显示名称(物品);
        if (名称.length() == 0) return 物品.getType().name();
        else return 名称;
    }

    public static String 物品显示名称(ItemStack 物品) {
        return 物品.getItemMeta().getDisplayName();
    }

    public static NamespacedKey key(String 字符串) {
        return new NamespacedKey(插件, 字符串);
    }

    public static NamespacedKey key(Recipe 合成表) {
        return ((Keyed) 合成表).getKey();
    }

    public static UUID 随机标识符() {
        return UUID.randomUUID();
    }

    public static UUID 唯一标识符(byte[] name) {
        UUID s;
        s = UUID.nameUUIDFromBytes(name);
        return s;
    }

    public static UUID 唯一标识符(String 字符串) {
        UUID s;
        s = UUID.fromString(字符串);
        return s;
    }

    public static void 控制台指令(String 指令) {
        new BukkitRunnable() {
            @Override
            public void run() {
                getServer().dispatchCommand(控制台, 指令);
                this.cancel();
            }
        }.runTask(插件);
    }

    public static void 同步指令(String 指令) {
        getServer().dispatchCommand(控制台, 指令);
    }

    public static void 控制台信息(String 信息) {
        控制台.sendMessage("[Get-M] " + 信息);
    }

    public static void 终止进程(int 进程ID) {
        new BukkitRunnable() {
            @Override
            public void run() {
                getScheduler().cancelTask(进程ID);
                this.cancel();
            }
        }.runTask(插件);
    }

    public static void 同步终止进程(int 进程ID) {
        getScheduler().cancelTask(进程ID);
    }

    public static ItemStack 物品堆(Material 材料) {
        return new ItemStack(材料);
    }

    public static RecipeChoice 材料单(Material 材料) {
        List<Material> 材料单 = new ArrayList<>();
        材料单.add(材料);
        return (new RecipeChoice.MaterialChoice(材料单));
    }

    public static RecipeChoice 材料单(List<Material> 材料表) {
        return (new RecipeChoice.MaterialChoice(材料表));
    }

    public static RecipeChoice.ExactChoice 准确原料(ItemStack 物品) {
        return (new RecipeChoice.ExactChoice(物品));
    }

    public static RecipeChoice.ExactChoice 准确原料(List<ItemStack> 物品列表) {
        return (new RecipeChoice.ExactChoice(物品列表));
    }

    public static RecipeChoice.ExactChoice 准确原料(ItemStack... 物品串) {
        return (new RecipeChoice.ExactChoice(new ArrayList<>(Arrays.asList(物品串))));
    }

    public static RecipeChoice.ExactChoice 准确原料(Material... 物品串) {
        List<ItemStack> 物品列表 = new ArrayList<>();
        for (Material material : 物品串) {
            物品列表.add(物品堆(material));
        }
        return (new RecipeChoice.ExactChoice(物品列表));
    }

    public static RecipeChoice.ExactChoice 准确原料(Material 物品, int 数量) {
        return (new RecipeChoice.ExactChoice(new ItemStack(物品, 数量)));
    }

    public static RecipeChoice.ExactChoice 准确原料(Material 物品) {
        return (new RecipeChoice.ExactChoice(new ItemStack(物品, 1)));
    }

}
