package cn.ksmcbrigade.TABPPD.mixin;

import cn.ksmcbrigade.TABPPD.TABPlayerPositionDisplay;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.PlayerTabOverlay;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.Scoreboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Mixin(PlayerTabOverlay.class)
public abstract class PlayerTABMixin {

    @Unique private boolean tABPlayerPositionDisplay$skip = false;
    @Shadow protected abstract void renderPingIcon(GuiGraphics p_283286_, int p_281809_, int p_282801_, int p_282223_, PlayerInfo p_282986_);

    @Inject(method = "render",at = @At(value = "INVOKE",target = "Lnet/minecraft/client/gui/components/PlayerTabOverlay;renderPingIcon(Lnet/minecraft/client/gui/GuiGraphics;IIILnet/minecraft/client/multiplayer/PlayerInfo;)V"),locals = LocalCapture.CAPTURE_FAILSOFT, cancellable = true)
    public void renderIcon(GuiGraphics p_281484_, int p_283602_, Scoreboard p_282338_, Objective p_282369_, CallbackInfo ci, List list, int i, int j, int i3, int j3, int k3, boolean flag, int l, int i1, int j1, int k1, int l1, List list1, List list2, int l3, int i4, int j4, int j2, int k2, int l2, PlayerInfo playerinfo1, GameProfile gameprofile){
        if(TABPlayerPositionDisplay.level==null){
            return;
        }

        Minecraft MC = Minecraft.getInstance();
        Player player = this.tABPlayerPositionDisplay$findPlayer(TABPlayerPositionDisplay.level,gameprofile.getName());

        if(player==null){
            return;
        }

        Font font = MC.font;
        //System.out.println(333333333);
        String pos = "["+ String.format("%.0f",Objects.requireNonNull(player).getX()) + " " + String.format("%.0f",Objects.requireNonNull(player).getY()) + " " + String.format("%.0f",Objects.requireNonNull(player).getZ()) + "]";

        int z = i1 + 3 + font.width(pos);
        i1 = z;
        p_281484_.fill(k2, l2, k2 + z, l2 + 8, l3);

        //System.out.println(1111111111);
        //System.out.println(p_282986_.getProfile().getId().toString()+"           :            "+ Objects.requireNonNull(MC.getUser().getProfileId()).toString());
        p_281484_.drawString(font,pos,k2 - (flag ? 9 : 0) + z - 12 - font.width(pos),l2, Color.WHITE.getRGB());
        this.renderPingIcon(p_281484_, z, k2 - (flag ? 9 : 0), l2, playerinfo1);
        tABPlayerPositionDisplay$skip = true;
    }

    @Inject(method = "renderPingIcon",at = @At("HEAD"), cancellable = true)
    public void renderIcon(GuiGraphics p_283286_, int p_281809_, int p_282801_, int p_282223_, PlayerInfo p_282986_, CallbackInfo ci){
        if(tABPlayerPositionDisplay$skip){
            tABPlayerPositionDisplay$skip = false;
            ci.cancel();
        }
    }

    @Unique
    @Nullable
    private Player tABPlayerPositionDisplay$findPlayer(Level level, String id){
        Minecraft MC = Minecraft.getInstance();
        if(MC.player==null){
            return null;
        }

        for(Player p:level.players()){
            if(p.getGameProfile().getName().equals(id)){
                return p;
            }
        }
        return null;
    }
}
