package MinerTools.graphics.renderer;

import MinerTools.graphics.draw.*;
import arc.*;
import arc.math.geom.*;
import arc.struct.*;
import arc.util.*;
import mindustry.*;
import mindustry.game.EventType.*;
import mindustry.game.Teams.*;
import mindustry.gen.*;
import mindustry.world.*;

public class BuildRender extends BaseRender<BuildDrawer<?>>{

    private final Seq<Building> tmp = new Seq<>();

    /* Tiles for render in camera */
    private static QuadTree<Tile> tiles;

    static {
        Events.on(WorldLoadEvent.class, e -> {
            tiles = new QuadTree<>(Vars.world.getQuadBounds(Tmp.r1));

            for(Tile tile : Vars.world.tiles){
                tiles.insert(tile);
            }
        });
    }

    @Override
    public void globalRender(Seq<BuildDrawer<?>> validDrawers){
        for(TeamData data : Vars.state.teams.getActive()){
            var buildings = data.buildings;

            tmp.clear();
            buildings.getObjects(tmp);
            for(Building building : tmp){
                for(BuildDrawer<?> drawer : validDrawers){
                    drawer.tryDraw(building);
                }
            }

            tmp.clear();
        }
    }

    @Override
    public void cameraRender(Seq<BuildDrawer<?>> validDrawers){
        tiles.intersect(Core.camera.bounds(Tmp.r1), tile -> {
            Building building = tile.build;

            if(building != null){
                for(BuildDrawer<?> drawer : validDrawers){
                    drawer.tryDraw(building);
                }
            }
        });
    }
}
