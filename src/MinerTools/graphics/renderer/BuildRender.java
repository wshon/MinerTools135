package MinerTools.graphics.renderer;

import MinerTools.graphics.draw.*;
import arc.*;
import arc.func.*;
import arc.math.geom.*;
import arc.struct.*;
import arc.util.*;
import mindustry.*;
import mindustry.game.EventType.*;
import mindustry.game.Teams.*;
import mindustry.gen.*;
import mindustry.world.*;

public class BuildRender<T extends Building> extends BaseRender<T>{
    private final Seq<Block> types;

    private final Seq<Building> tmp = new Seq<>();

    /* Tiles for render in camera */
    private QuadTree<Tile> tiles;

    public BuildRender(Seq<Block> types){
        this.types = types;

        Events.on(WorldLoadEvent.class, e -> {
            tiles = new QuadTree<>(Vars.world.getQuadBounds(Tmp.r1));

            for(Tile tile : Vars.world.tiles){
                tiles.insert(tile);
            }
        });
    }

    public BuildRender(Boolf<Block> predicate){
        this(Vars.content.blocks().select(predicate));
    }

    @Override
    public void globalRender(Seq<BaseDrawer<T>> validDrawers){
        for(TeamData data : Vars.state.teams.getActive()){
            if(data.buildings==null) continue;

            tmp.clear();
            data.buildings.getObjects(tmp);
            for(Building building : tmp){
                if(!types.contains(building.block())) continue;
                for(BaseDrawer<T> drawer : validDrawers){
                    drawer.tryDraw((T)building);
                }
            }

            tmp.clear();
        }
    }

    @Override
    public void cameraRender(Seq<BaseDrawer<T>> validDrawers){
        tiles.intersect(Core.camera.bounds(Tmp.r1), tile -> {
            Building building = tile.build;

            if(building != null && types.contains(building.block)){
                for(BaseDrawer<T> drawer : validDrawers){
                    drawer.tryDraw((T)building);
                }
            }
        });
    }
}
