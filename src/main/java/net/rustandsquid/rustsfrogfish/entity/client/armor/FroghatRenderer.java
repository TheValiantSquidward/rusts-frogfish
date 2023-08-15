package net.rustandsquid.rustsfrogfish.entity.client.armor;

import net.rustandsquid.rustsfrogfish.entity.custom.Froghat;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

public class FroghatRenderer extends GeoArmorRenderer<Froghat> {
    public FroghatRenderer() {
        super(new FroghatModel());
        this.headBone = "armorHead";
        this.bodyBone = "armorBody";
        this.rightArmBone = "armorRightArm";
        this.leftArmBone = "armorLeftArm";
        this.rightLegBone = "armorRightLeg";
        this.leftLegBone = "armorLeftLeg";
        this.rightBootBone = "armorRightBoot";
        this.leftBootBone = "armorLeftBoot";
    }

}
