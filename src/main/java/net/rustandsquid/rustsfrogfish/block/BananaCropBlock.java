package net.rustandsquid.rustsfrogfish.block;


import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class BananaCropBlock extends CropBlock {
    public static final IntegerProperty AGE = IntegerProperty.create("age", 0, 4);
    public BananaCropBlock(Properties pProperties) {
        super(pProperties);
    }
    @Override
    protected ItemLike getBaseSeedId() {
        return null;
    }

    @Override
    public IntegerProperty getAgeProperty() {
        return AGE;
    }

    @Override
    public int getMaxAge() {
        return 4;
    }
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }
}
