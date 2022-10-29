package net.bliss.tutorialmod.block.entity;

import net.bliss.tutorialmod.block.custom.GemInfusingStationBlock;
import net.bliss.tutorialmod.item.ModItems;
import net.bliss.tutorialmod.networking.ModMessages;
import net.bliss.tutorialmod.recipe.GemInfusingRecipe;
import net.bliss.tutorialmod.screen.GemInfusingScreenHandler;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import team.reborn.energy.api.base.SimpleEnergyStorage;

import java.util.Optional;

public class GemInfusingBlockEntity extends BlockEntity implements ExtendedScreenHandlerFactory, ImplementedInventory {
    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(3, ItemStack.EMPTY);

    public final SimpleEnergyStorage energyStorage = new SimpleEnergyStorage(30000, 32, 32) {
        @Override
        protected void onFinalCommit() {
            markDirty();
            if(!world.isClient()) {
                PacketByteBuf data = PacketByteBufs.create();
                data.writeLong(amount);
                data.writeBlockPos(getPos());

                for(ServerPlayerEntity player : PlayerLookup.tracking((ServerWorld) world, getPos())) {
                    ServerPlayNetworking.send(player, ModMessages.ENERGY_SYNC, data);
                }
            }
        }
    };

    protected final PropertyDelegate propertyDelegate;
    private int progress = 0;
    private int maxProgress = 72;

    public GemInfusingBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.GEM_INFUSING_STATION, pos, state);
        this.propertyDelegate = new PropertyDelegate() {
            @Override
            public int get(int index) {
                switch (index) {
                    case 0: return GemInfusingBlockEntity.this.progress;
                    case 1: return GemInfusingBlockEntity.this.maxProgress;
                    default: return 0;
                }
            }

            @Override
            public void set(int index, int value) {
                switch(index) {
                    case 0: GemInfusingBlockEntity.this.progress = value; break;
                    case 1: GemInfusingBlockEntity.this.maxProgress = value; break;
                }
            }

            @Override
            public int size() {
                return 2;
            }
        };
    }

    public void setEnergyLevel(long energyLevel) {
        this.energyStorage.amount = energyLevel;
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return this.inventory;
    }

    @Override
    public Text getDisplayName() {
        return Text.literal("Gem Infusing Station");
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new GemInfusingScreenHandler(syncId, inv, this, this.propertyDelegate);
    }

    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
        buf.writeBlockPos(this.pos);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, inventory);
        nbt.putInt("gem_infusing_station.progress", progress);
        nbt.putLong("gem_infusing_station.energy", energyStorage.amount);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        Inventories.readNbt(nbt, inventory);
        super.readNbt(nbt);
        progress = nbt.getInt("gem_infusing_station.progress");
        energyStorage.amount = nbt.getLong("gem_infusing_station.energy");
    }

    private void resetProgress() {
        this.progress = 0;
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack, @Nullable Direction side) {
        Direction localDir = this.getWorld().getBlockState(this.pos).get(GemInfusingStationBlock.FACING);
        if(side == Direction.UP || side == Direction.DOWN) {
            return false;
        }
        //top insert 1
        //right insert 1
        //left insert 0
        return switch (localDir) {
            default ->
                side.getOpposite() == Direction.NORTH && slot == 1 ||
                        side.getOpposite() == Direction.EAST && slot == 1 ||
                        side.getOpposite() == Direction.WEST && slot == 0;
            case EAST ->
                    side.getOpposite() == Direction.NORTH && slot == 1 ||
                            side.rotateYClockwise() == Direction.EAST && slot == 1 ||
                            side.rotateYClockwise() == Direction.WEST && slot == 0;
            case SOUTH ->
                    side == Direction.NORTH && slot == 1 ||
                            side == Direction.EAST && slot == 1 ||
                            side == Direction.WEST && slot == 0;
            case WEST ->
                    side.getOpposite() == Direction.NORTH && slot == 1 ||
                            side.rotateYCounterclockwise() == Direction.EAST && slot == 1 ||
                            side.rotateYCounterclockwise() == Direction.WEST && slot == 0;
        };
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction side) {
        Direction localDir = this.getWorld().getBlockState(this.pos).get(GemInfusingStationBlock.FACING);

        if(side == Direction.UP) {
            return false;
        }

        // Down extract 2
        if(side == Direction.DOWN) {
            return slot == 2;
        }

        // bottom extract 2
        // right extract 2
        return switch (localDir) {
            default -> side.getOpposite() == Direction.SOUTH && slot == 2 ||
                    side.getOpposite() == Direction.EAST && slot == 2;
            case EAST -> side.rotateYClockwise() == Direction.SOUTH && slot == 2 ||
                    side.rotateYClockwise() == Direction.EAST && slot == 2;
            case SOUTH -> side == Direction.SOUTH && slot == 2 ||
                    side == Direction.EAST && slot == 2;
            case WEST -> side.rotateYCounterclockwise() == Direction.SOUTH && slot == 2 ||
                    side.rotateYCounterclockwise() == Direction.EAST && slot == 2;
        };
    }

    public static void tick(World world, BlockPos blockPos, BlockState state, GemInfusingBlockEntity entity) {
        if(world.isClient()) {
            return;
        }
        
        if(hasEnergyItem(entity) && hasEnoughEnergy(entity)) {
            try(Transaction transaction = Transaction.openOuter()) {
                entity.energyStorage.insert(16, transaction);
                transaction.commit();
            }
        }
        
        if(hasRecipie(entity)) {
            entity.progress++;
            extractEnergy(entity);
            markDirty(world, blockPos, state);
            if(entity.progress >= entity.maxProgress) {
                craftItem(entity);
            }
        } else {
            entity.resetProgress();
            markDirty(world, blockPos, state);
        }
    }

    private static void extractEnergy(GemInfusingBlockEntity entity) {
        try(Transaction transaction = Transaction.openOuter()) {
            entity.energyStorage.extract(32, transaction);
            transaction.commit();
        }
    }

    private static boolean hasEnoughEnergy(GemInfusingBlockEntity entity) {
        return entity.energyStorage.amount >= 32;
    }

    private static boolean hasEnergyItem(GemInfusingBlockEntity entity) {
        return entity.getStack(0).getItem() == ModItems.RUBIDIUM;
    }

    private static void craftItem(GemInfusingBlockEntity entity) {
        SimpleInventory inventory = new SimpleInventory(entity.size());
        for (int i = 0; i < entity.size(); i++) {
            inventory.setStack(i, entity.getStack(i));
        }

        Optional<GemInfusingRecipe> recipe = entity.getWorld().getRecipeManager()
                .getFirstMatch(GemInfusingRecipe.Type.INSTANCE, inventory, entity.getWorld());

        if(hasRecipie(entity)) {
            entity.removeStack(1, 1);
            entity.setStack(2, new ItemStack(recipe.get().getOutput().getItem(),
                    entity.getStack(2).getCount() + 1));
            entity.resetProgress();
        }

    }

    private static boolean hasRecipie(GemInfusingBlockEntity entity) {
        SimpleInventory inventory = new SimpleInventory(entity.size());
        for (int i = 0; i < entity.size(); i++) {
            inventory.setStack(i, entity.getStack(i));
        }

        Optional<GemInfusingRecipe> match = entity.getWorld().getRecipeManager()
                .getFirstMatch(GemInfusingRecipe.Type.INSTANCE, inventory, entity.getWorld());

        return match.isPresent() && canInsertAmountIntoOoutputSlot(inventory, 1)
                && canInsertItemIntoOutputSlot(inventory, match.get().getOutput().getItem());

    }

    private static boolean canInsertItemIntoOutputSlot(SimpleInventory inventory, Item output) {
        return inventory.getStack(2).getItem() == output || inventory.getStack(2).isEmpty();
    }

    private static boolean canInsertAmountIntoOoutputSlot(SimpleInventory inventory, int count) {
        return inventory.getStack(2).getMaxCount() > inventory.getStack(2).getCount();
    }

}
