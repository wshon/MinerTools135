package MinerTools.graphics.draw.build;

import MinerTools.*;
import MinerTools.graphics.draw.*;
import arc.graphics.g2d.*;
import arc.util.*;
import mindustry.graphics.*;
import mindustry.world.blocks.defense.turrets.*;
import mindustry.world.blocks.defense.turrets.Turret.*;

import static mindustry.Vars.*;

public class TurretAlert extends BuildDrawer<TurretBuild>{
    public float turretAlertRadius;

    public TurretAlert(){
        super(block -> block instanceof Turret);
    }

    @Override
    public void readSetting(){
        turretAlertRadius = MinerVars.settings.getInt("turretAlertRadius") * tilesize;
    }

    @Override
    public boolean enabled(){
        return MinerVars.settings.getBool("turretAlert");
    }

    @Override
    public boolean isValid(){
        return !player.unit().isNull();
    }

    @Override
    public boolean isValid(TurretBuild turret){
        Turret block = (Turret)turret.block;
        return super.isValid(turret) &&
        (turret.team != player.team()) && // isEnemy
        (turret.hasAmmo()) && // hasAmmo
        (player.unit().isFlying() ? block.targetAir : block.targetGround) && // can hit player
        (turret.within(player, turretAlertRadius + block.range)); // within player
    }

    @Override
    protected void draw(TurretBuild turret){
        Draw.z(Layer.overlayUI);

        Lines.stroke(1.2f);
        Drawf.dashCircle(turret.x, turret.y, turret.range(), turret.team.color);

        Draw.color(turret.team.color);

        float dst = turret.dst(player);
        if(dst > turret.range()){
            Tmp.v1.set(turret).sub(player).setLength(dst - turret.range());
            Draw.rect(turret.block.fullIcon, player.x + Tmp.v1.x, player.y + Tmp.v1.y, 10f + turret.block.size * 3f, 10f + turret.block.size * 3f, Tmp.v1.angle() - 90f);
        }

        Draw.reset();
    }

}
