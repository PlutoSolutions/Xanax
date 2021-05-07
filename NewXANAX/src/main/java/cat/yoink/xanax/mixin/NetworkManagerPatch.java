package cat.yoink.xanax.mixin;

import cat.yoink.xanax.core.Main;
import cat.yoink.xanax.main.event.events.PacketEvent;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetworkManager.class)
public abstract class NetworkManagerPatch
{
    @Inject(method = "sendPacket(Lnet/minecraft/network/Packet;)V", at = @At("HEAD"), cancellable = true)
    private void onSendPacket(final Packet<?> packet, final CallbackInfo callbackInfo)
    {
        final PacketEvent event = new PacketEvent(packet, PacketEvent.Type.OUTGOING);
        Main.EVENT_BUS.dispatch(event);

        if (event.isCancelled()) callbackInfo.cancel();
    }

    @Inject(method = "channelRead0", at = @At("HEAD"), cancellable = true)
    private void onChannelRead(final ChannelHandlerContext context, final Packet<?> packet, final CallbackInfo callbackInfo)
    {
        final PacketEvent event = new PacketEvent(packet, PacketEvent.Type.INCOMING);
        Main.EVENT_BUS.dispatch(event);

        if (event.isCancelled()) callbackInfo.cancel();
    }
}
