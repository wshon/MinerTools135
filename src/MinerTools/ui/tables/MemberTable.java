package MinerTools.ui.tables;

import arc.scene.style.*;
import arc.scene.ui.layout.*;

public class MemberTable extends Table{
    /* 仅手机显示 */
    public boolean mobileOnly = false;
    public Drawable icon;

    /**
     * MemberTable设置成自己时会调用此方法
     */
    public void memberRebuild(){}
}
