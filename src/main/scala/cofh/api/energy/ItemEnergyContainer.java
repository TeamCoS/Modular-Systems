package cofh.api.energy;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Reference implementation of {@link IEnergyContainerItem}. Use/extend this or implement your own.
 * 
 * @author King Lemming
 * 
 */
public class ItemEnergyContainer extends Item implements IEnergyContainerItem {

	protected int capacity;
	protected int maxReceive;
	protected int maxExtract;

	public ItemEnergyContainer() {

	}

	public ItemEnergyContainer(int capacity) {

		this(capacity, capacity, capacity);
	}

	public ItemEnergyContainer(int capacity, int maxTransfer) {

		this(capacity, maxTransfer, maxTransfer);
	}

	public ItemEnergyContainer(int capacity, int maxReceive, int maxExtract) {

		this.capacity = capacity;
		this.maxReceive = maxReceive;
		this.maxExtract = maxExtract;
	}

	public ItemEnergyContainer setCapacity(int capacity) {

		this.capacity = capacity;
		return this;
	}

	public void setMaxTransfer(int maxTransfer) {

		setMaxReceive(maxTransfer);
		setMaxExtract(maxTransfer);
	}

	public void setMaxReceive(int maxReceive) {

		this.maxReceive = maxReceive;
	}

	public void setMaxExtract(int maxExtract) {

		this.maxExtract = maxExtract;
	}

	/* IEnergyContainerItem */
	@Override
	public int receiveEnergy(ItemStack container, int maxReceive, boolean simulate) {

		/*if (container.stackTagCompound == null) {
			container.stackTagCompound = new NBTTagCompound();
		}*/
		int energy = container.getTagCompound().getInteger("Energy");
		int energyReceived = Math.min(capacity - energy, Math.min(this.maxReceive, maxReceive));

		if (!simulate) {
			energy += energyReceived;
			NBTTagCompound tag = new NBTTagCompound();
			tag.setInteger("Energy", energy);
			container.setTagCompound(tag);
		}
		return energyReceived;
	}

	@Override
	public int extractEnergy(ItemStack container, int maxExtract, boolean simulate) {

		if (container.hasTagCompound()) {
			NBTTagCompound tag = container.getTagCompound();
			if (tag.hasKey("Energy")) {
				int energy = tag.getInteger("Energy");
				int energyExtracted = Math.min(energy, Math.min(this.maxExtract, maxExtract));

				if (!simulate) {
					energy -= energyExtracted;
					NBTTagCompound tag1 = new NBTTagCompound();
					tag1.setInteger("Energy", energy);
					container.setTagCompound(tag1);
				}
				return energyExtracted;
			}
		}
		return 0;
	}

	@Override
	public int getEnergyStored(ItemStack container) {

        if (container.hasTagCompound()) {
            NBTTagCompound tag = container.getTagCompound();
            if (tag.hasKey("Energy")) {
                return tag.getInteger("Energy");
            }
        }
        return 0;
	}

	@Override
	public int getMaxEnergyStored(ItemStack container) {

		return capacity;
	}

}
