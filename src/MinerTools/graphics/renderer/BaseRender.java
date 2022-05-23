package MinerTools.graphics.renderer;

import MinerTools.graphics.draw.*;
import arc.math.geom.*;
import arc.struct.*;

public abstract class BaseRender<T extends Position>{
    protected final Seq<BaseDrawer<T>> allGlobalDrawers = new Seq<>();
    protected final Seq<BaseDrawer<T>> allCameraDrawers = new Seq<>();

    Seq<BaseDrawer<T>> enableDrawers;
    Seq<BaseDrawer<T>> enableCameraDrawers;

    @SafeVarargs
    public final BaseRender<T> addDrawers(BaseDrawer<T>... drawers){
        for(BaseDrawer<T> drawer : drawers){
            if(!allGlobalDrawers.contains(drawer)){
                allGlobalDrawers.add(drawer);
            }
        }
        return this;
    }

    @SafeVarargs
    public final BaseRender<T> addCameraDrawers(BaseDrawer<T>... drawers){
        for(BaseDrawer<T> drawer : drawers){
            if(!allCameraDrawers.contains(drawer)){
                allCameraDrawers.add(drawer);
            }
        }

        return this;
    }

    public void updateEnable(){
        enableDrawers = allGlobalDrawers.select(BaseDrawer::enabled);
        enableCameraDrawers = allCameraDrawers.select(BaseDrawer::enabled);
    }

    public void updateSetting(){
        for(BaseDrawer<?> drawer : allGlobalDrawers){
            drawer.readSetting();
        }
        for(BaseDrawer<?> drawer : allCameraDrawers){
            drawer.readSetting();
        }
    }

    public void render(){
        if(allGlobalDrawers.any()){
            var validDrawers = enableDrawers.select(BaseDrawer::isValid);

            if(validDrawers.any()){
                globalRender(validDrawers);
            }
        }

        if(enableCameraDrawers.any()){
            var validCameraDrawers = enableCameraDrawers.select(BaseDrawer::isValid);

            if(validCameraDrawers.any()){
                cameraRender(validCameraDrawers);
            }
        }
    }

    public abstract void globalRender(Seq<BaseDrawer<T>> validDrawers);

    public abstract void cameraRender(Seq<BaseDrawer<T>> validDrawers);
}
