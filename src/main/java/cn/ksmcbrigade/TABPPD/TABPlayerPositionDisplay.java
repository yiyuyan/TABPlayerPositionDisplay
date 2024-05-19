package cn.ksmcbrigade.TABPPD;

import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("tab_ppd")
@Mod.EventBusSubscriber
public class TABPlayerPositionDisplay {

    public static Level level = null;

    public TABPlayerPositionDisplay() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public static void clientTick(TickEvent.ClientTickEvent event){
        if(Minecraft.getInstance().player==null){
            level = null;
        }
    }

    @SubscribeEvent
    public static void playerTick(TickEvent.PlayerTickEvent event){
        //if(event.player.level() instanceof ServerLevel PlayerServerLevel){
            level = event.player.level();
        //}
    }
}
