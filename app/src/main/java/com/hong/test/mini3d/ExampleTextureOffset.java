package com.hong.test.mini3d;

import javax.microedition.khronos.opengles.GL10;

import com.hong.min3d.Shared;
import com.hong.min3d.Utils;
import com.hong.min3d.core.Object3dContainer;
import com.hong.min3d.core.RendererActivity;
import com.hong.min3d.objectPrimitives.Sphere;
import com.hong.min3d.vos.Light;
import com.hong.min3d.vos.TextureVo;
import com.hong.test.R;

import android.graphics.Bitmap;

/**
 * Demonstrates use of offset properties of TextureVo.
 * <p>
 * We have a alpha PNG of a cloud layer, which is added as a second texture to the earth sphere.
 * While the earth rotates from 'left to right', we're changing the "offsetU" of the cloud layer
 * so the texture moves from right to left slightly frame by frame.
 *
 * @author Lee
 */
public class ExampleTextureOffset extends RendererActivity {
    Object3dContainer _earth;
    TextureVo _cloudTexture;

    int _count = 0;

    public void initScene() {
        Light light = new Light();
        light.ambient.setAll((short) 64, (short) 64, (short) 64, (short) 255);
        light.position.setAll(3, 3, 3);
        scene.lights().add(light);

        _earth = new Sphere(1.2f, 15, 10);
        scene.addChild(_earth);

        Bitmap b = Utils.makeBitmapFromResourceId(this, R.drawable.earth);
        Shared.textureManager().addTextureId(b, "jupiter", false);
        b.recycle();

        b = Utils.makeBitmapFromResourceId(this, R.drawable.clouds_alpha2b);
        Shared.textureManager().addTextureId(b, "clouds", false);
        b.recycle();

        TextureVo t = new TextureVo("jupiter");
        _earth.textures().add(t);

        _cloudTexture = new TextureVo("clouds");
        _cloudTexture.textureEnvs.get(0).param = GL10.GL_DECAL;
        _cloudTexture.repeatU = true; // .. this is the default, but just to be explicit

        _earth.textures().add(_cloudTexture);

        _count = 0;
    }

    @Override
    public void updateScene() {
        _earth.rotation().y += 1.0f;

        // Animate texture offset
        _cloudTexture.offsetU += 0.001f;

        _count++;
    }
}
