package com.hindelid.ld.thirtyfour;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import sun.reflect.generics.tree.Tree;

public class Main extends ApplicationAdapter {
    private Viewport mViewPort;
    private OrthographicCamera mCamera;
    private ShapeRenderer mShapeRenderer;

    private TreeBranch mRoot;
    private Vector2 mCurrentViewCord;


    @Override
    public void create () {
        mShapeRenderer = new ShapeRenderer();
        mCamera = new OrthographicCamera();
        mViewPort = new ExtendViewport(Constants.VIEW_SIZE_X, Constants.VIEW_SIZE_Y, mCamera);
        mCurrentViewCord = new Vector2(Constants.VIEW_SIZE_X / 2, Constants.VIEW_SIZE_Y / 2);
        moveAndUpdateCamera();

        mRoot = new TreeBranch(
                new Vector2(Constants.VIEW_SIZE_X / 2f, 0),
                new Vector2(Constants.VIEW_SIZE_X / 2f, 0.5f),
                true,
                1);
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
        long before = TimeUtils.nanoTime();
        mShapeRenderer.setColor(Color.BROWN);
        mRoot.render(mShapeRenderer);

        mShapeRenderer.setColor(Color.GREEN);
        mRoot.renderLeefs(mShapeRenderer);
        long after = TimeUtils.nanoTime();
        mShapeRenderer.end();

        System.out.println("Time:" + (after - before));

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }
    }

    private void tick() {
        moveAndUpdateCamera();
        if (TreeBranch.sNext) {
            TreeBranch.sNext = false;
            mRoot.split();
        }
    }

    private void moveAndUpdateCamera() {
        TreeBranch.sGlobal.y += Constants.SPEED;

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            mCamera.zoom *= 5f;
        }

        mCurrentViewCord.set(TreeBranch.sGlobal);
        mCamera.position.set(mCurrentViewCord, 0);
        mCamera.update();
    }
}
