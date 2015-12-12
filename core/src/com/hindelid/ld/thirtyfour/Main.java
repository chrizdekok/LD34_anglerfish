package com.hindelid.ld.thirtyfour;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;
import java.util.List;

public class Main extends ApplicationAdapter {
    private Viewport mViewPort;
    private OrthographicCamera mCamera;
    private ShapeRenderer mShapeRenderer;

    private TreeBranch mRoot;


    @Override
    public void create () {
        mShapeRenderer = new ShapeRenderer();
        mCamera = new OrthographicCamera();
        mViewPort = new FitViewport(Constants.MAP_SIZE_X, Constants.MAP_SIZE_Y, mCamera);
        mCamera.position.set(Constants.MAP_SIZE_X / 2, Constants.MAP_SIZE_Y / 2, 0);
        mCamera.update();


        mRoot = new TreeBranch(Constants.MAP_SIZE_X / 2f, 0, Constants.MAP_SIZE_X / 2f, 2);
    }

    @Override
    public void resize(int aWidth, int aHeight) {
        mViewPort.update(aWidth, aHeight);

    }

    @Override
    public void render () {
        tick();
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        mShapeRenderer.setProjectionMatrix(mCamera.combined);

        mShapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        mRoot.render(mShapeRenderer);

        mShapeRenderer.end();

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }
    }

    private void tick() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            mRoot.split();
        }
    }
}
