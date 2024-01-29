package com.ciarahub.managers;

import org.bukkit.Bukkit;
import org.bukkit.World;

import java.util.Iterator;

public class WeatherManager {
    public WeatherManager() {
    }

    public static void setClearWeather() {
        Iterator var0 = Bukkit.getWorlds().iterator();

        while(var0.hasNext()) {
            World world = (World)var0.next();
            world.setStorm(false);
            world.setThundering(false);
            world.setWeatherDuration(Integer.MAX_VALUE);
        }

    }
}
