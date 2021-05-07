// 
// Decompiled by Procyon v0.5.36
// 

package cat.yoink.xanax.client.handling;

import io.netty.channel.ChannelPromise;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.common.MinecraftForge;
import cat.yoink.xanax.client.event.PacketEvent;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelHandler;
import cat.yoink.xanax.client.MinecraftInstance;
import io.netty.channel.ChannelDuplexHandler;

public class ConnectionHandler extends ChannelDuplexHandler implements MinecraftInstance
{
    public ConnectionHandler() {
        try {
            final ChannelPipeline pipeline = ConnectionHandler.mc.getConnection().getNetworkManager().channel().pipeline();
            pipeline.addBefore("packet_handler", "PacketHandler", (ChannelHandler)this);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void channelRead(final ChannelHandlerContext ctx, final Object msg) throws Exception {
        final PacketEvent event = new PacketEvent(msg, PacketEvent.Type.INCOMING);
        MinecraftForge.EVENT_BUS.post((Event)event);
        if (!event.isCanceled()) {
            super.channelRead(ctx, event.getPacket());
        }
    }
    
    public void write(final ChannelHandlerContext ctx, final Object msg, final ChannelPromise promise) throws Exception {
        final PacketEvent event = new PacketEvent(msg, PacketEvent.Type.OUTGOING);
        MinecraftForge.EVENT_BUS.post((Event)event);
        if (!event.isCanceled()) {
            super.write(ctx, event.getPacket(), promise);
        }
    }
}
