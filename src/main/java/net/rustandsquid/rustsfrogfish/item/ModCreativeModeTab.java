package net.rustandsquid.rustsfrogfish.item;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ModCreativeModeTab {
    public static final CreativeModeTab RUSTSFROGFISH_TAB = new CreativeModeTab("rustfrogfishtab") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModItems.RAW_FROGFISH.get());
        }
    };
}
