package thaumicenergistics.network.packet.client;

import io.netty.buffer.ByteBuf;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.EntityPlayer;
import thaumcraft.api.aspects.Aspect;
import thaumicenergistics.aspect.AspectStack;
import thaumicenergistics.aspect.AspectStackComparator.ComparatorMode;
import thaumicenergistics.container.ContainerEssentiaTerminal;
import thaumicenergistics.gui.GuiEssentiaTerminal;
import thaumicenergistics.network.packet.AbstractClientPacket;
import thaumicenergistics.network.packet.AbstractPacket;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class PacketClientEssentiaTerminal
	extends AbstractClientPacket
{
	public static final int MODE_UPDATE_LIST = 0;
	public static final int MODE_SET_CURRENT = 1;
	public static final int MODE_SORT_MODE_CHANGED = 2;
	
	private static final ComparatorMode[] SORT_MODES = ComparatorMode.values();

	private List<AspectStack> aspectStackList;
	private Aspect selectedAspect;
	private ComparatorMode sortMode;

	public PacketClientEssentiaTerminal createListUpdate( EntityPlayer player, List<AspectStack> list )
	{
		// Set the player
		this.player = player;

		// Set the mode
		this.mode = PacketClientEssentiaTerminal.MODE_UPDATE_LIST;

		// Mark to use compression
		this.useCompression = true;

		// Set the list
		this.aspectStackList = list;

		return this;
	}

	public PacketClientEssentiaTerminal createSelectedAspectUpdate( EntityPlayer player, Aspect selectedAspect )
	{
		// Set the player
		this.player = player;

		// Set the mode
		this.mode = PacketClientEssentiaTerminal.MODE_SET_CURRENT;

		// Set the selected aspect
		this.selectedAspect = selectedAspect;

		return this;
	}

	public PacketClientEssentiaTerminal createSortModeUpdate( EntityPlayer player, ComparatorMode sortMode )
	{
		// Set the player
		this.player = player;

		// Set the mode
		this.mode = PacketClientEssentiaTerminal.MODE_SORT_MODE_CHANGED;

		// Set the sort mode
		this.sortMode = sortMode;

		return this;
	}

	@SideOnly(Side.CLIENT)
	@Override
	protected void wrappedExecute()
	{
		Gui gui = Minecraft.getMinecraft().currentScreen;

		if( gui instanceof GuiEssentiaTerminal )
		{
			ContainerEssentiaTerminal container = (ContainerEssentiaTerminal)( (GuiEssentiaTerminal)gui ).inventorySlots;
			
			switch ( this.mode )
			{
				case PacketClientEssentiaTerminal.MODE_UPDATE_LIST:
					container.updateAspectList( this.aspectStackList );
					break;

				case PacketClientEssentiaTerminal.MODE_SET_CURRENT:
					container.receiveSelectedAspect( this.selectedAspect );
					break;
					
				case PacketClientEssentiaTerminal.MODE_SORT_MODE_CHANGED:
					// Update the sorting mode
					( (GuiEssentiaTerminal)gui ).onSortModeChanged( this.sortMode );
					break;
			}
		}
	}

	@Override
	public void readData( ByteBuf stream )
	{

		switch ( this.mode )
		{
			case PacketClientEssentiaTerminal.MODE_UPDATE_LIST:
				this.aspectStackList = new ArrayList<AspectStack>();

				while( stream.readableBytes() > 0 )
				{
					this.aspectStackList.add( new AspectStack( AbstractPacket.readAspect( stream ), stream.readLong() ) );
				}
				break;

			case PacketClientEssentiaTerminal.MODE_SET_CURRENT:
				this.selectedAspect = AbstractPacket.readAspect( stream );
				break;
				
			case PacketClientEssentiaTerminal.MODE_SORT_MODE_CHANGED:
				// Read the mode ordinal
				this.sortMode = SORT_MODES[stream.readInt()];
				break;
		}
	}

	@Override
	public void writeData( ByteBuf stream )
	{

		switch ( this.mode )
		{
			case PacketClientEssentiaTerminal.MODE_UPDATE_LIST:
				for( AspectStack stack : this.aspectStackList )
				{
					AbstractPacket.writeAspect( stack.aspect, stream );

					stream.writeLong( stack.amount );
				}
				break;

			case PacketClientEssentiaTerminal.MODE_SET_CURRENT:
				AbstractPacket.writeAspect( this.selectedAspect, stream );
				break;
				
			case PacketClientEssentiaTerminal.MODE_SORT_MODE_CHANGED:
				// Write the mode ordinal
				stream.writeInt( this.sortMode.ordinal() );
				break;
		}
	}
}
