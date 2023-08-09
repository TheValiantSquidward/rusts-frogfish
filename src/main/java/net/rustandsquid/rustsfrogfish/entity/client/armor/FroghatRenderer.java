package net.rustandsquid.rustsfrogfish.entity.client.armor;

import net.rustandsquid.rustsfrogfish.entity.custom.Froghat;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

public class FroghatRenderer extends GeoArmorRenderer<Froghat> {
    public FroghatRenderer() {
        super(new FroghatModel());
        this.headBone = "armorHead";
    }

}
