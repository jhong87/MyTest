package com.hong.test.mini3d;

import com.hong.min3d.core.Object3dContainer;
import com.hong.min3d.core.RendererActivity;
import com.hong.min3d.parser.IParser;
import com.hong.min3d.parser.Parser;
import com.hong.min3d.vos.Light;

/**
 * How to load a model from a .obj file
 *
 * @author dennis.ippel
 */
public class ExampleLoadObjFile extends RendererActivity {
    private Object3dContainer objModel;

    @Override
    public void initScene() {

        scene.lights().add(new Light());

        IParser parser = Parser.createParser(Parser.Type.OBJ,
                getResources(), "com.hong.test:raw/camaro_obj", true);
        parser.parse();

        objModel = parser.getParsedObject();
        objModel.scale().x = objModel.scale().y = objModel.scale().z = .7f;
        scene.addChild(objModel);
    }

    @Override
    public void updateScene() {
        objModel.rotation().x++;
        objModel.rotation().z++;
    }
}
