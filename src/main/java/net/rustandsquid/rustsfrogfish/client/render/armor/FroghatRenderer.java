package net.rustandsquid.rustsfrogfish.client.render.armor;

import net.rustandsquid.rustsfrogfish.client.model.armor.FroghatModel;
import net.rustandsquid.rustsfrogfish.item.armor.FroghatItem;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;


    public class FroghatRenderer extends GeoArmorRenderer<FroghatItem> {
        public FroghatRenderer() {
            super(new FroghatModel());

            this.headBone = "armorHead";
        }
    }
